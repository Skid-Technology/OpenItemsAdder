package dev.lone.itemsadder.Core.OtherPlugins;

import com.sk89q.jnbt.ByteTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.EditSession.Stage;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extension.factory.BlockFactory;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.history.changeset.ChangeSet;
import com.sk89q.worldedit.internal.registry.AbstractFactory;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.Core.OtherPlugins.WorldEditIntegration.CropHistory;
import dev.lone.itemsadder.Core.OtherPlugins.WorldEditIntegration.CustomBlockExtent;
import dev.lone.itemsadder.Core.OtherPlugins.WorldEditIntegration.CustomBlockParser;
import dev.lone.itemsadder.Core.OtherPlugins.WorldEditIntegration.EntityExtent;
import dev.lone.itemsadder.Core.OtherPlugins.WorldEditIntegration.PaperEntityListener;
import dev.lone.itemsadder.Core.OtherPlugins.WorldEditIntegration.PendingPasteCrop;
import dev.lone.itemsadder.Core.OtherPlugins.WorldEditSchematicSidecar.Data;
import dev.lone.itemsadder.Core.OtherPlugins.WorldEditSchematicSidecar.Stamp;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomBlock.Advanced;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import dev.lone.itemsadder.block.BlockInteractionListener;
import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.block.ChunkBlockTracker;
import dev.lone.itemsadder.block.ChunkLocalBlock;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.FurnitureItemEntity;
import dev.lone.itemsadder.item.ItemEntityAction;
import dev.lone.itemsadder.item.PlaceableItemSpec;
import dev.lone.itemsadder.item.PlacedItemInstance;
import dev.lone.itemsadder.resourcepack.ByteNamedKey;
import dev.lone.itemsadder.util.EnvironmentDetector;
import dev.lone.itemsadder.util.InstalledVersionState;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.VersionComparator;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.Files;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.Nullable;

public final class WorldEditIntegration implements Listener {
   private static final String ID_TAG = "ItemsAdderId";
   private static final String CROP_ID_TAG = "ItemsAdderCropId";
   private static final String CROP_AGE_TAG = "ItemsAdderCropAge";
   private volatile Map blockIds = Map.of();
   private final boolean fawe;
   private Object fawePasteListener;
   private Listener paperEntityListener;
   private final Map pendingCrops = new ConcurrentHashMap();
   private final AtomicBoolean cropFlushScheduled = new AtomicBoolean();
   private final Map clipboardCrops = Collections.synchronizedMap(new WeakHashMap());
   private final Map clipboardBlocks = Collections.synchronizedMap(new WeakHashMap());
   private final Map fawePasteCrops = new ConcurrentHashMap();
   private final Map cropHistory = Collections.synchronizedMap(new WeakHashMap());
   private final Map pendingSchematics = new ConcurrentHashMap();
   private static final ByteNamedKey NO_CROP = new ByteNamedKey("", (byte)0);

   private WorldEditIntegration(boolean fawe) {
      this.fawe = fawe;
   }

   public static WorldEditIntegration register(Plugin plugin, boolean fawe) {
      WorldEditIntegration var2 = new WorldEditIntegration(fawe);
      Bukkit.getPluginManager().registerEvents(var2, plugin);
      if (EnvironmentDetector.paperAdventure) {
         Objects.requireNonNull(var2);
         var2.paperEntityListener = new PaperEntityListener(var2);
         Bukkit.getPluginManager().registerEvents(var2.paperEntityListener, plugin);
      }

      WorldEdit.getInstance().getEventBus().register(var2);
      if (fawe) {
         InstalledVersionState var3 = VersionComparator.compareInstalledVersion("FastAsyncWorldEdit", "2.11.2");
         if (var3 == InstalledVersionState.INSTALLED_IS_LOWER || var3 == InstalledVersionState.INSTALLED_IS_SAME) {
            Log.warn(
               "WorldEdit integration: FAWE 2.11.2 or older can leave pasted furniture after //undo. Update FAWE to a version compatible with this server (2.12.2 or newer verified on Minecraft 1.20.6)."
            );
         }

         FaweWorldEditBridge.allowExtent(EntityExtent.class.getName());
         var2.fawePasteListener = FaweWorldEditBridge.registerPasteListener(var2);
      }

      BlockFactory var10000 = WorldEdit.getInstance().getBlockFactory();
      Objects.requireNonNull(var2);
      var10000.register(new CustomBlockParser(var2));
      return var2;
   }

   public static void removeLegacyParsers(Plugin legacyPlugin) {
      ClassLoader var1 = legacyPlugin.getClass().getClassLoader();

      try {
         Field var2 = AbstractFactory.class.getDeclaredField("parsers");
         var2.setAccessible(true);
         removeParsersOwnedBy(var2, WorldEdit.getInstance().getBlockFactory(), var1);
         removeParsersOwnedBy(var2, WorldEdit.getInstance().getPatternFactory(), var1);
      } catch (ReflectiveOperationException | RuntimeException var3) {
         Log.warn("Could not remove WorldEdit parsers from disabled " + legacyPlugin.getName() + ": " + var3.getMessage());
      }
   }

   private static void removeParsersOwnedBy(Field field, AbstractFactory factory, ClassLoader owner) {
      ((List)field.get(factory)).removeIf(parser -> parser.getClass().getClassLoader() == owner);
   }

   public void unregister() {
      if (this.paperEntityListener != null) {
         HandlerList.unregisterAll(this.paperEntityListener);
      }

      WorldEdit.getInstance().getEventBus().unregister(this);
      if (this.fawePasteListener != null) {
         WorldEdit.getInstance().getEventBus().unregister(this.fawePasteListener);
      }
   }

   @EventHandler
   public void onItemsAdderLoad(ItemsAdderLoadDataEvent event) {
      HashMap var2 = new HashMap();
      HashSet var3 = new HashSet();

      for (String var5 : CustomBlock.getNamespacedIdsInRegistry()) {
         CustomBlock var6 = CustomBlock.getInstance(var5);
         if (var6 != null && var6.getBaseBlockData() != null) {
            BlockState var7 = BukkitAdapter.adapt(var6.getBaseBlockData());
            String var8 = var2.putIfAbsent(var7, var5);
            if (var8 != null && !var8.equals(var5)) {
               var3.add(var7);
            }
         }
      }

      HashMap var9 = new HashMap(this.blockIds);
      HashSet var10 = new HashSet(var2.values());
      var9.entrySet().removeIf(entry -> var10.contains(entry.getValue()) && !var2.containsKey(entry.getKey()));
      var9.putAll(var2);
      var3.forEach(var9::remove);
      if (!var3.isEmpty()) {
         Log.warn("WorldEdit: " + var3.size() + " custom block states share a vanilla state; clipboard inference is unavailable for them.");
      }

      this.blockIds = Map.copyOf(var9);
   }

   private void onEntityAdd(Entity entity) {
      ContentManager var2 = ContentManager.method8084();
      if (var2 != null && var2.method8110()) {
         if (this.missingEntityDefinition(entity)) {
            entity.setVisibleByDefault(false);
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
               if (entity.isValid()) {
                  entity.remove();
               }
            });
         } else {
            if (FurnitureItemEntity.method409(entity)) {
               Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                  ContentManager var1x = ContentManager.method8084();
                  if (var1x != null && var1x.method8110() && entity.isValid()) {
                     FurnitureItemEntity var2x = (FurnitureItemEntity)ItemEntityAction.method364(entity, "furniture");
                     if (var2x != null) {
                        var2x.method367(entity);
                        BlockUtils.markBlockState(entity.getLocation().getBlock(), var2x.Az());
                        if (var2x.method406()) {
                           var2x.method368(entity);
                        }
                     }
                  }
               });
            } else if (entity instanceof LivingEntity var3 && CustomEntityManager.isInitialized() && CustomEntityManager.isTaggedCustomEntity(entity)) {
               Bukkit.getScheduler()
                  .runTask(
                     Main.getInstance(),
                     () -> {
                        ContentManager var2x = ContentManager.method8084();
                        if (var2x != null && var2x.method8110() && entity.isValid() && CustomEntityManager.isTaggedCustomEntity(entity)) {
                           if (CustomEntityManager.getInstance().isCustomEntityUuid(entity.getUniqueId())
                              || CustomEntityManager.getInstance().wrapEntity(var3) != null) {
                              PlacedItemInstance var3x = PlaceableItemSpec.findByEntity(var3);
                              if (var3x != null) {
                                 PlaceableItemSpec.method345(var3x);
                              }
                           }
                        }
                     }
                  );
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
   public void onEntitySpawn(EntitySpawnEvent event) {
      ContentManager var2 = ContentManager.method8084();
      if (var2 != null && var2.method8110() && this.missingEntityDefinition(event.getEntity())) {
         event.setCancelled(true);
      } else {
         if (!EnvironmentDetector.paperAdventure) {
            this.onEntityAdd(event.getEntity());
         }
      }
   }

   @Subscribe
   public void onEditSession(EditSessionEvent event) {
      ContentManager var2 = ContentManager.method8084();
      if (var2 != null && var2.method8110()) {
         if (event.getStage() == Stage.BEFORE_CHANGE && event.getWorld() != null) {
            World var3 = BukkitAdapter.adapt(event.getWorld());
            if (this.fawe) {
               FaweWorldEditBridge.register(event, var3, this);
            } else {
               event.setExtent(new CustomBlockExtent(this, event.getExtent(), var3));
            }

            event.setExtent(new EntityExtent(this, event.getExtent()));
         }
      }
   }

   String idOf(BlockStateHolder state) {
      CompoundTag var2 = state instanceof BaseBlock var3 ? var3.getNbtData() : null;
      if (var2 != null && var2.getValue().containsKey("ItemsAdderId")) {
         Tag var6 = (Tag)var2.getValue().get("ItemsAdderId");
         return var6 instanceof StringTag var4 && CustomBlock.isInRegistry(var4.getValue()) ? var4.getValue() : null;
      } else {
         String var5 = (String)this.blockIds.get(state.toImmutableState());
         return var5 != null && CustomBlock.isInRegistry(var5) ? var5 : null;
      }
   }

   private boolean missingDefinition(BlockStateHolder state) {
      CompoundTag var2 = state instanceof BaseBlock var3 ? var3.getNbtData() : null;
      if (var2 != null) {
         Tag var6 = (Tag)var2.getValue().get("ItemsAdderId");
         if (var6 != null) {
            return !(var6 instanceof StringTag var8 && CustomBlock.isInRegistry(var8.getValue()));
         }

         Tag var4 = (Tag)var2.getValue().get("ItemsAdderCropId");
         if (var4 != null) {
            return !(var4 instanceof StringTag var5 && ContentManager.method8084().au.containsKey(var5.getValue()));
         }
      }

      String var7 = (String)this.blockIds.get(state.toImmutableState());
      return var7 != null && !CustomBlock.isInRegistry(var7);
   }

   BitSet missingBlockOrdinals() {
      BitSet var1 = new BitSet();

      for (Entry var3 : this.blockIds.entrySet()) {
         if (!CustomBlock.isInRegistry((String)var3.getValue())) {
            var1.set(((BlockState)var3.getKey()).getOrdinal());
         }
      }

      return var1;
   }

   @Nullable
   private String entityTag(CompoundTag nbt, String key) {
      if (nbt != null && nbt.getValue().get("BukkitValues") instanceof CompoundTag var3) {
         Tag var6 = (Tag)var3.getValue().get(key);
         return var6 instanceof StringTag var5 ? var5.getValue() : null;
      } else {
         return null;
      }
   }

   boolean missingEntityDefinition(CompoundTag nbt) {
      return this.missingEntityDefinition(
         this.entityTag(nbt, "itemsadder:placeable_entity_item"),
         this.entityTag(nbt, "itemsadder:placeable_behaviour_type"),
         this.entityTag(nbt, "itemsadder:custom_entity"),
         this.entityTag(nbt, "itemsadder:complex_furniture")
      );
   }

   private boolean missingEntityDefinition(Entity entity) {
      PersistentDataContainer var2 = entity.getPersistentDataContainer();
      return this.missingEntityDefinition(
         (String)var2.get(ItemEntityAction.acU, PersistentDataType.STRING),
         (String)var2.get(ItemEntityAction.acV, PersistentDataType.STRING),
         (String)var2.get(CustomEntityManager.oB, PersistentDataType.STRING),
         (String)var2.get(PlaceableItemSpec.adi, PersistentDataType.STRING)
      );
   }

   private boolean missingEntityDefinition(String itemId, String furnitureType, String entityId, String complexId) {
      if ("furniture".equals(furnitureType)) {
         CustomItemDefinition var5 = ContentManager.method8084().method8154(itemId);
         if (var5 == null || !(var5.aoh.getAction("furniture") instanceof FurnitureItemEntity)) {
            return true;
         }
      }

      if (entityId != null && (!CustomEntityManager.isInitialized() || CustomEntityManager.getInstance().getSettings(entityId) == null)) {
         return true;
      }

      if (complexId == null) {
         return false;
      }

      CustomItemDefinition var6 = ContentManager.method8084().method8154(complexId);
      return var6 == null || !(var6.aoh.getAction("complex_furniture") instanceof PlaceableItemSpec);
   }

   private boolean missingEntityDefinition(BaseEntity state) {
      return this.missingEntityDefinition(state.getNbtData());
   }

   private BaseEntity withEntityTypeId(BaseEntity state) {
      CompoundTag var2 = state.getNbtData();
      if (var2 != null && var2.getString("id").isEmpty()) {
         HashMap var3 = new HashMap(var2.getValue());
         var3.put("id", new StringTag(state.getType().getId()));
         return new BaseEntity(state.getType(), new CompoundTag(var3));
      } else {
         return state;
      }
   }

   @Nullable
   private ChunkBlockTracker crops() {
      BlockInteractionListener var1 = ContentManager.method8084().method8143();
      return var1 == null ? null : var1.mZ;
   }

   @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
   public void onWorldEditPaste(PlayerCommandPreprocessEvent event) {
      ContentManager var2 = ContentManager.method8084();
      if (var2 != null && var2.method8110()) {
         if (event.getMessage().split("\\s+", 2)[0].equalsIgnoreCase("//paste")) {
            if (this.fawe) {
               FaweWorldEditBridge.beforePaste(event.getPlayer(), this);
            } else {
               try {
                  Clipboard var3 = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(event.getPlayer())).getClipboard().getClipboard();
                  this.removeMissingEntities(var3);
               } catch (EmptyClipboardException var4) {
               }
            }
         }
      }
   }

   private Set furnitureHitboxes(World world, Region selection) {
      BlockVector3 var3 = selection.getMinimumPoint();
      BlockVector3 var4 = selection.getMaximumPoint();
      Location var5 = new Location(
         world, ((double)var3.x() + var4.x() + 1.0) / 2.0, ((double)var3.y() + var4.y() + 1.0) / 2.0, ((double)var3.z() + var4.z() + 1.0) / 2.0
      );
      HashSet var6 = new HashSet();

      for (Entity var8 : world.getNearbyEntities(var5, (var4.x() - var3.x() + 1) / 2.0, (var4.y() - var3.y() + 1) / 2.0, (var4.z() - var3.z() + 1) / 2.0)) {
         boolean var9 = FurnitureItemEntity.method409(var8);
         if (var9 || var8 instanceof LivingEntity var10 && CustomEntityManager.isTaggedCustomEntity(var8) && PlaceableItemSpec.findByEntity(var10) != null) {
            Location var16 = var8.getLocation();
            if (selection.contains(BlockVector3.at(var16.getBlockX(), var16.getBlockY(), var16.getBlockZ()))) {
               BoundingBox var11 = var8.getBoundingBox();

               for (int var12 = Math.max(var3.x(), (int)Math.floor(var11.getMinX())); var12 <= Math.min(var4.x(), (int)Math.floor(var11.getMaxX())); var12++) {
                  for (int var13 = Math.max(var3.y(), (int)Math.floor(var11.getMinY())); var13 <= Math.min(var4.y(), (int)Math.floor(var11.getMaxY())); var13++) {
                     for (int var14 = Math.max(var3.z(), (int)Math.floor(var11.getMinZ()));
                        var14 <= Math.min(var4.z(), (int)Math.floor(var11.getMaxZ()));
                        var14++
                     ) {
                        BlockVector3 var15 = BlockVector3.at(var12, var13, var14);
                        if (selection.contains(var15) && world.getBlockAt(var12, var13, var14).getType() == Material.BARRIER) {
                           var6.add(var15);
                        }
                     }
                  }
               }
            }
         }
      }

      return var6;
   }

   private void clearFurnitureHitboxes(Clipboard clipboard, Set hitboxes) {
      for (BlockVector3 var4 : hitboxes) {
         if (clipboard.getRegion().contains(var4) && clipboard.getBlock(var4).getBlockType() == BlockTypes.BARRIER) {
            this.clearClipboardBlock(clipboard, var4);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onWorldEditCopy(PlayerCommandPreprocessEvent event) {
      ContentManager var2 = ContentManager.method8084();
      if (var2 != null && var2.method8110()) {
         String var3 = event.getMessage().split("\\s+", 2)[0].toLowerCase(Locale.ROOT);
         if (var3.equals("//copy") || var3.equals("//cut")) {
            Player var4 = event.getPlayer();
            LocalSession var5 = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(var4));

            Region var6;
            try {
               var6 = var5.getSelection(BukkitAdapter.adapt(var4.getWorld()));
            } catch (IncompleteRegionException var22) {
               return;
            }

            HashMap var7 = new HashMap();
            HashMap var8 = new HashMap();
            World var9 = var4.getWorld();
            BlockVector3 var10 = var6.getMinimumPoint();
            BlockVector3 var11 = var6.getMaximumPoint();

            for (int var12 = var10.getX() >> 4; var12 <= var11.getX() >> 4; var12++) {
               for (int var13 = var10.getZ() >> 4; var13 <= var11.getZ() >> 4; var13++) {
                  Chunk var14 = var9.getChunkAt(var12, var13);
                  ChunkBlockTracker var15 = this.crops();
                  if (var15 != null) {
                     for (ChunkLocalBlock var17 : var15.getChunkTable(var14).snapshot().entries()) {
                        BlockVector3 var18 = BlockVector3.at((var12 << 4) + var17.relX(), var17.y(), (var13 << 4) + var17.relZ());
                        if (var6.contains(var18)) {
                           var7.put(var18, new ByteNamedKey(var17.namespacedId(), var17.age()));
                        }
                     }
                  }

                  CustomBlockChunkStoreBase var27 = ContentManager.method8084().method8182(var14);
                  if (var27 != null) {
                     Map var28 = var27.getAllBlocksLocations(var14);
                     if (var28 != null) {
                        for (Entry var19 : var28.entrySet()) {
                           BlockVector3 var20 = BlockVector3.at(
                              ((Location)var19.getKey()).getBlockX(), ((Location)var19.getKey()).getBlockY(), ((Location)var19.getKey()).getBlockZ()
                           );
                           if (var19.getValue() != null && var6.contains(var20)) {
                              var8.put(var20, (String)var19.getValue());
                           }
                        }
                     }
                  }
               }
            }

            Set var23 = this.furnitureHitboxes(var9, var6);
            if (!var7.isEmpty() || !var8.isEmpty() || !var23.isEmpty()) {
               ClipboardHolder var24;
               try {
                  var24 = var5.getClipboard();
               } catch (EmptyClipboardException var21) {
                  var24 = null;
               }

               boolean var25 = var3.equals("//cut");
               int var26 = this.fawe && var25 ? var5.getHistory().size() : 0;
               this.patchCopiedData(var4, var24, var7, var8, var23, var25, var26, 100);
            }
         }
      }
   }

   private void patchCopiedData(Player player, ClipboardHolder previous, Map snapshot, Map blocks, Set hitboxes, boolean cut, int historySize, int attempts) {
      Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
         if (player.isOnline()) {
            ClipboardHolder var9;
            try {
               var9 = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player)).getClipboard();
            } catch (EmptyClipboardException var16) {
               var9 = null;
            }

            if (var9 != null && var9 != previous) {
               Clipboard var10 = var9.getClipboard();
               this.clearFurnitureHitboxes(var10, hitboxes);
               this.clipboardCrops.put(var10, Map.copyOf(snapshot));
               this.clipboardBlocks.put(var10, Map.copyOf(blocks));
               if (cut) {
                  for (BlockVector3 var12 : snapshot.keySet()) {
                     this.clearCrop(player.getWorld(), var12.getX(), var12.getY(), var12.getZ());
                  }

                  if (this.fawe && !snapshot.isEmpty()) {
                     this.linkCutHistory(player, snapshot, historySize, 100);
                  }
               }

               if (!this.fawe) {
                  for (Entry var19 : blocks.entrySet()) {
                     BlockVector3 var13 = (BlockVector3)var19.getKey();
                     if (var10.getRegion().contains(var13)) {
                        BaseBlock var14 = var10.getFullBlock(var13);
                        HashMap var15 = new HashMap();
                        if (var14.getNbtData() != null) {
                           var15.putAll(var14.getNbtData().getValue());
                        }

                        var15.put("ItemsAdderId", new StringTag((String)var19.getValue()));
                        var10.setBlock(var13, var14.toImmutableState().toBaseBlock(new CompoundTag(var15)));
                     }
                  }

                  for (Entry var20 : snapshot.entrySet()) {
                     BlockVector3 var21 = (BlockVector3)var20.getKey();
                     if (var10.getRegion().contains(var21)) {
                        BaseBlock var22 = var10.getFullBlock(var21);
                        HashMap var23 = new HashMap();
                        if (var22.getNbtData() != null) {
                           var23.putAll(var22.getNbtData().getValue());
                        }

                        var23.put("ItemsAdderCropId", new StringTag(((ByteNamedKey)var20.getValue()).namespacedId()));
                        var23.put("ItemsAdderCropAge", new ByteTag(((ByteNamedKey)var20.getValue()).age()));
                        var10.setBlock(var21, var22.toImmutableState().toBaseBlock(new CompoundTag(var23)));
                     }
                  }
               }
            } else {
               if (attempts > 0) {
                  this.patchCopiedData(player, previous, snapshot, blocks, hitboxes, cut, historySize, attempts - 1);
               }
            }
         }
      }, 1L);
   }

   private void linkCutHistory(Player player, Map snapshot, int previousSize, int attempts) {
      LocalSession var5 = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player));
      List var6 = var5.getHistory();
      if (var6.size() <= previousSize) {
         if (attempts > 0) {
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> this.linkCutHistory(player, snapshot, previousSize, attempts - 1), 1L);
         }
      } else {
         CropHistory var7 = new CropHistory();

         for (Entry var9 : snapshot.entrySet()) {
            BlockVector3 var10 = (BlockVector3)var9.getKey();
            Location var11 = new Location(player.getWorld(), var10.getX(), var10.getY(), var10.getZ());
            var7.before.put(var11, (ByteNamedKey)var9.getValue());
            var7.after.put(var11, NO_CROP);
         }

         this.cropHistory.put((ChangeSet)var6.get(var6.size() - 1), var7);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onWorldEditHistory(PlayerCommandPreprocessEvent event) {
      ContentManager var2 = ContentManager.method8084();
      if (var2 != null && var2.method8110()) {
         if (this.fawe) {
            String var3 = event.getMessage().split("\\s+", 2)[0].toLowerCase(Locale.ROOT);
            boolean var4 = var3.equals("//redo");
            if (var4 || var3.equals("//undo")) {
               Player var5 = event.getPlayer();
               LocalSession var6 = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(var5));
               List var7 = var6.getHistory();
               int var8 = var6.getHistoryIndex() + (var4 ? 1 : 0);
               if (var8 >= 0 && var8 < var7.size()) {
                  CropHistory var9 = (CropHistory)this.cropHistory.get(var7.get(var8));
                  if (var9 != null) {
                     int var10 = var6.getHistoryNegativeIndex() + (var4 ? -1 : 1);
                     this.waitForHistory(var6, var9, var4, var10, 100);
                  }
               }
            }
         }
      }
   }

   private void waitForHistory(LocalSession session, CropHistory crops, boolean redo, int expected, int attempts) {
      Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
         if (session.getHistoryNegativeIndex() != expected) {
            if (attempts > 0) {
               this.waitForHistory(session, crops, redo, expected, attempts - 1);
            }
         } else {
            HashMap var6 = new HashMap();

            for (Entry var8 : (redo ? crops.after : crops.before).entrySet()) {
               ByteNamedKey var9 = ((ByteNamedKey)var8.getValue()).namespacedId().isEmpty() ? null : (ByteNamedKey)var8.getValue();
               var6.put((Location)var8.getKey(), new PendingPasteCrop(var9, null, null));
            }

            this.rememberPending(var6);

            for (Entry var11 : var6.entrySet()) {
               this.queueCrop((Location)var11.getKey(), ((PendingPasteCrop)var11.getValue()).entry());
            }
         }
      }, 1L);
   }

   private void rememberPending(Map pending) {
      this.fawePasteCrops.putAll(pending);
      Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> pending.forEach(this.fawePasteCrops::remove), 1200L);
   }

   void onFaweSchematicSave(Actor actor, Clipboard clipboard, URI source, URI destination) {
      if (WorldEditSchematicSidecar.path(destination) != null) {
         ClipboardHolder var5 = WorldEdit.getInstance().getSessionManager().get(actor).getClipboard();
         if (var5.getClipboard() != clipboard) {
            Log.warn("WorldEdit: clipboard changed during schematic save; ItemsAdder metadata was not saved.");
         } else {
            Map var6 = (Map)this.clipboardCrops.get(clipboard);
            Map var7 = (Map)this.clipboardBlocks.get(clipboard);
            if (var6 == null && var7 == null && source != null) {
               this.loadSchematicData(clipboard, source);
            }

            this.sanitizeFaweClipboard(clipboard);
            var6 = (Map)this.clipboardCrops.get(clipboard);
            var7 = (Map)this.clipboardBlocks.get(clipboard);
            BlockVector3 var8 = clipboard.getOrigin();
            HashMap var9 = new HashMap();
            if (var6 != null) {
               for (Entry var11 : var6.entrySet()) {
                  var9.put(var5.getTransform().apply(((BlockVector3)var11.getKey()).subtract(var8).toVector3()).toBlockPoint(), (ByteNamedKey)var11.getValue());
               }
            }

            HashMap var17 = new HashMap();
            if (var7 != null) {
               for (Entry var12 : var7.entrySet()) {
                  var17.put(var5.getTransform().apply(((BlockVector3)var12.getKey()).subtract(var8).toVector3()).toBlockPoint(), (String)var12.getValue());
               }
            }

            Data var19 = new Data(Map.copyOf(var17), Map.copyOf(var9));
            if (!var19.isEmpty() || Files.exists(WorldEditSchematicSidecar.path(destination))) {
               Stamp var20;
               try {
                  var20 = WorldEditSchematicSidecar.stamp(destination);
               } catch (IOException var14) {
                  Log.warn("WorldEdit: could not inspect schematic before save: " + var14.getMessage());
                  return;
               }

               this.pendingSchematics.put(destination, var19);
               Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                  try {
                     WorldEditSchematicSidecar.writeWhenReady(destination, var19, var20);
                  } catch (IOException var9x) {
                     Log.warn("WorldEdit: could not save ItemsAdder schematic data: " + var9x.getMessage());
                  } catch (InterruptedException var10) {
                     Thread.currentThread().interrupt();
                  } finally {
                     this.pendingSchematics.remove(destination, var19);
                  }
               });
            }
         }
      }
   }

   private void loadSchematicData(Clipboard clipboard, URI uri) {
      Data var3 = (Data)this.pendingSchematics.get(uri);
      if (var3 == null) {
         try {
            var3 = WorldEditSchematicSidecar.read(uri);
         } catch (IOException var10) {
            Log.warn("WorldEdit: could not read ItemsAdder schematic data: " + var10.getMessage());
            var3 = Data.EMPTY;
         }
      }

      BlockVector3 var4 = clipboard.getOrigin();
      HashMap var5 = new HashMap();

      for (Entry var7 : var3.crops().entrySet()) {
         BlockVector3 var8 = var4.add((BlockVector3)var7.getKey());
         if (clipboard.getRegion().contains(var8)) {
            if (ContentManager.method8084().au.containsKey(((ByteNamedKey)var7.getValue()).namespacedId())) {
               var5.put(var8, (ByteNamedKey)var7.getValue());
            } else {
               this.clearClipboardBlock(clipboard, var8);
            }
         }
      }

      HashMap var11 = new HashMap();

      for (Entry var13 : var3.blocks().entrySet()) {
         BlockVector3 var9 = var4.add((BlockVector3)var13.getKey());
         if (clipboard.getRegion().contains(var9)) {
            if (CustomBlock.isInRegistry((String)var13.getValue())) {
               var11.put(var9, (String)var13.getValue());
            } else {
               this.clearClipboardBlock(clipboard, var9);
            }
         }
      }

      this.clipboardCrops.put(clipboard, Map.copyOf(var5));
      this.clipboardBlocks.put(clipboard, Map.copyOf(var11));
   }

   private void clearClipboardBlock(Clipboard clipboard, BlockVector3 position) {
      try {
         clipboard.setBlock(position, BlockTypes.AIR.getDefaultState());
      } catch (WorldEditException var4) {
         throw new IllegalStateException("Could not clear missing ItemsAdder content from clipboard", var4);
      }
   }

   private void sanitizeFaweClipboard(Clipboard clipboard) {
      this.removeMissingEntities(clipboard);
      Map var2 = (Map)this.clipboardBlocks.get(clipboard);
      if (var2 != null) {
         HashMap var3 = new HashMap();

         for (Entry var5 : var2.entrySet()) {
            if (CustomBlock.isInRegistry((String)var5.getValue())) {
               var3.put((BlockVector3)var5.getKey(), (String)var5.getValue());
            } else {
               this.clearClipboardBlock(clipboard, (BlockVector3)var5.getKey());
            }
         }

         this.clipboardBlocks.put(clipboard, Map.copyOf(var3));
      }

      Map var7 = (Map)this.clipboardCrops.get(clipboard);
      if (var7 != null) {
         HashMap var8 = new HashMap();

         for (Entry var6 : var7.entrySet()) {
            if (ContentManager.method8084().au.containsKey(((ByteNamedKey)var6.getValue()).namespacedId())) {
               var8.put((BlockVector3)var6.getKey(), (ByteNamedKey)var6.getValue());
            } else {
               this.clearClipboardBlock(clipboard, (BlockVector3)var6.getKey());
            }
         }

         this.clipboardCrops.put(clipboard, Map.copyOf(var8));
      }
   }

   private void removeMissingEntities(Clipboard clipboard) {
      Clipboard var2 = this.fawe ? FaweWorldEditBridge.entitySource(clipboard) : clipboard;
      boolean var3 = false;

      for (com.sk89q.worldedit.entity.Entity var5 : List.copyOf(var2.getEntities())) {
         if (this.missingEntityDefinition(var5.getState().getNbtData())) {
            var5.remove();
            var3 = true;
         }
      }

      if (var3) {
         for (BlockVector3 var7 : clipboard.getRegion()) {
            if (clipboard.getBlock(var7).getBlockType() == BlockTypes.BARRIER) {
               this.clearClipboardBlock(clipboard, var7);
            }
         }
      }
   }

   void beforeFawePaste(Clipboard clipboard, URI source) {
      if (this.clipboardCrops.get(clipboard) == null && this.clipboardBlocks.get(clipboard) == null && source != null) {
         this.loadSchematicData(clipboard, source);
      }

      this.sanitizeFaweClipboard(clipboard);
   }

   void onFawePaste(EditSession edit, Actor actor, Clipboard clipboard, BlockVector3 target, URI source, BooleanSupplier cancelled) {
      ClipboardHolder var7 = WorldEdit.getInstance().getSessionManager().get(actor).getClipboard();
      Vector3 var8 = clipboard.getOrigin().toVector3();
      World var9 = BukkitAdapter.adapt(edit.getWorld());
      HashMap var10 = new HashMap();
      this.beforeFawePaste(clipboard, source);
      Map var11 = (Map)this.clipboardCrops.get(clipboard);
      Map var12 = (Map)this.clipboardBlocks.get(clipboard);
      if (var11 != null || var12 != null) {
         if (var11 != null) {
            for (Entry var14 : var11.entrySet()) {
               BlockVector3 var15 = (BlockVector3)var14.getKey();
               Vector3 var16 = var15.toVector3().subtract(var8);
               BlockVector3 var17 = var7.getTransform().apply(var16).toBlockPoint().add(target);
               var10.put(var17, (ByteNamedKey)var14.getValue());
            }
         }

         HashMap var22 = new HashMap();
         if (var12 != null) {
            for (Entry var25 : var12.entrySet()) {
               Vector3 var27 = ((BlockVector3)var25.getKey()).toVector3().subtract(var8);
               BlockVector3 var29 = var7.getTransform().apply(var27).toBlockPoint().add(target);
               var22.put(var29, (String)var25.getValue());
            }
         }

         if (!var10.isEmpty() || !var22.isEmpty()) {
            CropHistory var24 = edit.getChangeSet() == null ? null : new CropHistory();
            if (var24 != null && !var10.isEmpty()) {
               this.cropHistory.put(edit.getChangeSet(), var24);
            }

            HashMap var26 = new HashMap();
            if (var11 != null) {
               for (Entry var30 : var11.entrySet()) {
                  BlockVector3 var18 = (BlockVector3)var30.getKey();
                  BlockVector3 var19 = var7.getTransform().apply(var18.toVector3().subtract(var8)).toBlockPoint().add(target);
                  Location var20 = new Location(var9, var19.getX(), var19.getY(), var19.getZ());
                  PendingPasteCrop var21 = new PendingPasteCrop((ByteNamedKey)var30.getValue(), clipboard.getBlock(var18).getBlockType(), var24);
                  var26.put(var20, var21);
                  if (var24 != null) {
                     var24.after.put(var20, (ByteNamedKey)var30.getValue());
                  }
               }
            }

            if (!var26.isEmpty()) {
               this.rememberPending(var26);
            }

            Bukkit.getScheduler()
               .runTaskLater(
                  Main.getInstance(),
                  () -> {
                     if (!cancelled.getAsBoolean()) {
                        for (Entry var7x : var10.entrySet()) {
                           Location var8x = new Location(
                              var9, ((BlockVector3)var7x.getKey()).getX(), ((BlockVector3)var7x.getKey()).getY(), ((BlockVector3)var7x.getKey()).getZ()
                           );
                           this.rememberCropBefore(var8x, (PendingPasteCrop)var26.get(var8x));
                           this.queueCrop(var8x, (ByteNamedKey)var7x.getValue());
                        }

                        for (Entry var11x : var22.entrySet()) {
                           Location var12x = new Location(
                              var9, ((BlockVector3)var11x.getKey()).getX(), ((BlockVector3)var11x.getKey()).getY(), ((BlockVector3)var11x.getKey()).getZ()
                           );
                           String var9x = Advanced.getInCustomRegion(var12x);
                           if (!Objects.equals(var9x, var11x.getValue())) {
                              if (var9x != null) {
                                 Advanced.removeFromCustomRegion(var12x);
                              }

                              if (CustomBlock.isInRegistry((String)var11x.getValue())) {
                                 Advanced.placeInCustomRegion((String)var11x.getValue(), var12x);
                              } else {
                                 var12x.getBlock().setType(Material.AIR, false);
                              }
                           }
                        }
                     }
                  },
                  2L
               );
         }
      }
   }

   private ByteNamedKey cropOf(BlockStateHolder state) {
      CompoundTag var2 = state instanceof BaseBlock var3 ? var3.getNbtData() : null;
      return var2 != null
            && var2.getValue().get("ItemsAdderCropId") instanceof StringTag var6
            && var2.getValue().get("ItemsAdderCropAge") instanceof ByteTag var4
            && ContentManager.method8084().au.containsKey(var6.getValue())
         ? new ByteNamedKey(var6.getValue(), var4.getValue())
         : null;
   }

   private BaseBlock withoutItemsAdderTags(BaseBlock block) {
      CompoundTag var2 = block.getNbtData();
      if (var2 != null && (var2.getValue().containsKey("ItemsAdderId") || var2.getValue().containsKey("ItemsAdderCropId"))) {
         HashMap var3 = new HashMap(var2.getValue());
         var3.remove("ItemsAdderId");
         var3.remove("ItemsAdderCropId");
         var3.remove("ItemsAdderCropAge");
         return var3.isEmpty() ? block.toImmutableState().toBaseBlock() : block.toImmutableState().toBaseBlock(new CompoundTag(var3));
      } else {
         return block;
      }
   }

   private void applyCrop(Location location, ByteNamedKey next) {
      ChunkBlockTracker var3 = this.crops();
      if (var3 != null) {
         if (next != null && !ContentManager.method8084().au.containsKey(next.namespacedId())) {
            next = null;
         }

         ByteNamedKey var4 = var3.getBlockStateKey(location.getBlock());
         if (!Objects.equals(var4, next)) {
            if (next == null) {
               var3.removeBlockStateAt(location.getBlock());
            } else {
               var3.setBlockStateAt(location.getBlock(), next.namespacedId(), next.age());
            }
         }
      }
   }

   void clearCrop(World world, int x, int y, int z) {
      this.applyCrop(new Location(world, x, y, z), null);
   }

   void afterFaweBlockChange(World world, int x, int y, int z, BlockType written) {
      Location var6 = new Location(world, x, y, z);
      PendingPasteCrop var7 = (PendingPasteCrop)this.fawePasteCrops.remove(var6);
      this.rememberCropBefore(var6, var7);
      this.clearCrop(world, x, y, z);
      if (var7 != null && (var7.blockType() == null || var7.blockType() == written)) {
         this.applyCrop(var6, var7.entry());
      }
   }

   private void rememberCropBefore(Location location, PendingPasteCrop pending) {
      if (pending != null && pending.history() != null) {
         ChunkBlockTracker var3 = this.crops();
         if (var3 == null) {
            return;
         }

         ByteNamedKey var4 = var3.getBlockStateKey(location.getBlock());
         pending.history().before.putIfAbsent(location, var4 == null ? NO_CROP : var4);
      }
   }

   private void queueCrop(Location location, ByteNamedKey next) {
      this.pendingCrops.put(location, next == null ? new ByteNamedKey("", (byte)0) : next);
      if (this.cropFlushScheduled.compareAndSet(false, true)) {
         Bukkit.getScheduler().runTask(Main.getInstance(), this::flushCrops);
      }
   }

   private void flushCrops() {
      HashMap var1 = new HashMap(this.pendingCrops);
      var1.keySet().forEach(this.pendingCrops::remove);

      for (Entry var3 : var1.entrySet()) {
         this.applyCrop((Location)var3.getKey(), ((ByteNamedKey)var3.getValue()).namespacedId().isEmpty() ? null : (ByteNamedKey)var3.getValue());
      }

      this.cropFlushScheduled.set(false);
      if (!this.pendingCrops.isEmpty() && this.cropFlushScheduled.compareAndSet(false, true)) {
         Bukkit.getScheduler().runTask(Main.getInstance(), this::flushCrops);
      }
   }
}
