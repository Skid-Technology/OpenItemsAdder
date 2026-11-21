package dev.lone.itemsadder.block;

import beer.devs.fastnbt.nms.Version;
import beer.devs.fastnbt.nms.nbt.NItem;
import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.PickItemFromBlockPacketView;
import beer.devs.nativepackets.api.PickItemFromEntityPacketView;
import beer.devs.nativepackets.api.SynchronizedPacketAccess;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.BlockInteractKey;
import dev.lone.itemsadder.item.BlockPlacementKey;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.FurnitureItemEntity;
import dev.lone.itemsadder.item.ItemEntityAction;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.item.ItemUseHandler;
import dev.lone.itemsadder.item.PlacedBlockItemDefinition;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.nms.NmsBlockSetterService;
import dev.lone.itemsadder.util.BlockDataBatch;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.IntTableConstantsJ;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.ServerCompat;
import dev.lone.itemsadder.util.Utils;
import dev.lone.itemsadder.world.BlockEffectsService;
import dev.lone.itemsadder.world.ChunkBlockHookHolder;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import dev.lone.itemsadder.worldgen.TreePopulator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.WeakHashMap;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

public final class CustomBlockListener implements IaListener {
   private static CustomBlockListener ahD;
   private static final BlockFace[] ahE = new BlockFace[]{BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST, BlockFace.UP, BlockFace.DOWN};
   @Nullable
   private BlockEventRelayListener ahF;
   @Nullable
   private StructureGrowListener ahG;
   @Nullable
   private NoteBlockListener ahH;
   @Nullable
   private BucketFluidListener ahI;
   @Nullable
   private BlockSpreadListener ahJ;
   @Nullable
   private PacketListenerHandle ng;
   @Nullable
   private PacketListenerHandle ahK;
   private volatile boolean cV = true;
   WeakHashMap ahL = new WeakHashMap();

   CustomBlockListener() {
      ahD = this;
      this.register();
   }

   private void register() {
      if (Settings.get().customBlocksChorus || Settings.get().customBlocksMushroom || Settings.get().customBlocksWire || Settings.get().customBlocksFire) {
         if (Settings.get().customBlocksChorus) {
            this.ahF = new BlockEventRelayListener();
            this.ahF.register(Main.getInstance());
         }

         if (Settings.get().customBlocksMushroom) {
            this.ahG = new StructureGrowListener();
            this.ahG.register(Main.getInstance());
         }

         if (Settings.get().customBlocksNote) {
            this.ahH = new NoteBlockListener();
            this.ahH.register(Main.getInstance());
         }

         if (Settings.get().customBlocksWire) {
            this.ahI = new BucketFluidListener();
            this.ahI.register(Main.getInstance());
         }

         if (Settings.get().customBlocksFire) {
            this.ahJ = new BlockSpreadListener();
            this.ahJ.register(Main.getInstance());
         }

         EventRegistrar.registerIfAbsent(this, Main.getInstance());
         if (Version.isAtLeast(Version.v1_21_4)) {
            this.ng = NativePacketsHolder.PE()
               .method1888(
                  Main.getInstance(),
                  PacketSelector.exact(PacketTypes.PICK_ITEM_FROM_BLOCK),
                  ListenerPriority.NORMAL,
                  true,
                  ListenerFailurePolicy.CANCEL_PACKET,
                  var1 -> {
                     if (this.cV) {
                        PickItemFromBlockPacketView var2 = (PickItemFromBlockPacketView)var1.view(PickItemFromBlockPacketView.class);
                        UUID var3 = NativePacketsHolder.PE().method1883(var1);
                        if (var3 != null) {
                           BlockPlacementKey var4 = new BlockPlacementKey(var3, var1.connection().id(), var2.blockX(), var2.blockY(), var2.blockZ());
                           var1.sync((var2x, var3x) -> {
                              if (this.method1151(var3x.player(), var4.bk(), var4.bl())) {
                                 Location var4x = (Location)var3x.location(var4.method1(), var4.method2(), var4.method3());
                                 var2x.syncAt(var4x, (var2xx, var3xx) -> this.method1153(var2xx, var3xx, var4));
                              }
                           });
                        }
                     }
                  }
               );
            this.ahK = NativePacketsHolder.PE()
               .method1888(
                  Main.getInstance(),
                  PacketSelector.exact(PacketTypes.PICK_ITEM_FROM_ENTITY),
                  ListenerPriority.NORMAL,
                  true,
                  ListenerFailurePolicy.CANCEL_PACKET,
                  var1 -> {
                     if (this.cV) {
                        PickItemFromEntityPacketView var2 = (PickItemFromEntityPacketView)var1.view(PickItemFromEntityPacketView.class);
                        UUID var3 = NativePacketsHolder.PE().method1883(var1);
                        if (var3 != null) {
                           BlockInteractKey var4 = new BlockInteractKey(var3, var1.connection().id(), var2.entityId());
                           var1.sync((var2x, var3x) -> this.method1154(var2x, var3x, var4));
                        }
                     }
                  }
               );
         }
      }
   }

   private boolean method1151(Object var1, UUID var2, long var3) {
      return this.method1152(var1, var2, var3) && ((Player)var1).isOnline() && ((Player)var1).getGameMode() == GameMode.CREATIVE;
   }

   private boolean method1152(Object var1, UUID var2, long var3) {
      return this.cV && var1 instanceof Player var5 && NativePacketsHolder.PE().method1885(var2, var3) == var5;
   }

   private void method1153(PacketEvent var1, SynchronizedPacketAccess var2, BlockPlacementKey var3) {
      if (this.method1152(var2.player(), var3.bk(), var3.bl())) {
         Block var4 = (Block)var2.block(var3.method1(), var3.method2(), var3.method3());
         if (var4 != null) {
            CustomBlockChunkStoreBase var5 = ContentManager.method8084().method8183(var4);
            if (var5.isCustomBlock(var4)) {
               var1.cancel();
               CustomBlockDefinition var6 = ContentManager.method8084().method8160(var5.getCustomBlockId(var4));
               if (var6 != null && var6.anF != null) {
                  ItemStack var7 = var6.Ds();
                  var1.sync((var3x, var4x) -> {
                     if (this.method1151(var4x.player(), var3.bk(), var3.bl())) {
                        this.method1155(var7, (Player)var4x.player());
                     }
                  });
               }
            }
         }
      }
   }

   private void method1154(PacketEvent var1, SynchronizedPacketAccess var2, BlockInteractKey var3) {
      if (this.method1151(var2.player(), var3.bk(), var3.bl())) {
         if (var2.entity(var3.jI()) instanceof Entity var5 && NativePacketsHolder.PE().IP().ownsEntity(var5) && FurnitureItemEntity.method409(var5)) {
            CustomFurniture var6 = CustomFurniture.byAlreadySpawned(var5);
            if (var6 != null) {
               var1.cancel();
               ItemStack var7 = var6.getItemStack().clone();
               NItem var8 = new NItem(var7);
               if (var8.hasKey("IAOrigName")) {
                  var8.setCustomName(var8.getString("IAOrigName"));
                  var8.remove("IAOrigName");
                  var8.save();
               }

               this.method1155(var7, (Player)var2.player());
            }
         }
      }
   }

   private void method1155(ItemStack var1, Player var2) {
      if (var1 != null) {
         ItemStack var3 = var1.clone();
         var3.setAmount(1);
         PlayerInventory var4 = var2.getInventory();
         ItemStack[] var5 = var4.getStorageContents();

         for (int var6 = 0; var6 < var5.length; var6++) {
            ItemStack var7 = var5[var6];
            if (!ItemStackUtils.aE(var7) && var7.isSimilar(var3)) {
               if (var6 >= 9) {
                  int var8 = var4.getHeldItemSlot();
                  var4.setItem(var6, var4.getItem(var8));
                  var4.setItem(var8, var7);
               } else {
                  var4.setHeldItemSlot(var6);
               }

               return;
            }
         }

         int var9 = var4.getHeldItemSlot();

         for (int var10 = 0; var10 < 9; var10++) {
            if (ItemStackUtils.aE(var4.getItem(var10))) {
               var9 = var10;
               break;
            }
         }

         ItemStack var11 = var4.getItem(var9);
         if (!ItemStackUtils.aE(var11)) {
            int var12 = var4.firstEmpty();
            if (var12 != -1) {
               var4.setItem(var12, var11);
            }
         }

         var4.setHeldItemSlot(var9);
         var4.setItem(var9, var3);
      }
   }

   public static CustomBlockListener BU() {
      return new CustomBlockListener();
   }

   public static CustomBlockListener BV() {
      return ahD;
   }

   @Override
   public void unregister() {
      this.cV = false;
      if (this.ng != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.ng);
         this.ng = null;
      }

      if (this.ahK != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.ahK);
         this.ahK = null;
      }

      IaListener.super.unregister();
      IaListener.method532(this.ahF);
      IaListener.method532(this.ahG);
      IaListener.method532(this.ahH);
      IaListener.method532(this.ahI);
      IaListener.method532(this.ahJ);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static boolean method1156(Material var0) {
      return switch (IntTableConstantsJ.SWITCH_MAP[var0.ordinal()]) {
         case 1, 2, 3, 4 -> true;
         default -> false;
      };
   }

   public boolean method1157(Block var1, boolean var2, boolean var3, boolean var4) {
      return this.method1158(var1, var2, var3, var4, false);
   }

   public boolean method1158(Block var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      Material var6 = var1.getType();
      if (var3 && !method1156(var6) && var6 != Material.AIR && var6 != Material.CAVE_AIR) {
         return false;
      }

      if (!var2) {
         CustomBlockChunkStoreBase var7 = ContentManager.method8084().method8183(var1);
         if (var7 == null) {
            return false;
         }

         if (var7.isCustomBlock(var1)) {
            if (var4) {
               CustomBlockDefinition var8 = ContentManager.method8084().method8160(var7.getCustomBlockId(var1));
               if (var8 != null) {
                  var8.anF.aE(var1);
               }
            } else {
               NmsBlockSetterService.method3400(var1, var1.getBlockData());
            }

            return true;
         }

         if (var5) {
            return false;
         }
      }

      if (var4) {
         if (var6 != Material.NOTE_BLOCK) {
            NmsBlockSetterService.OD().bk(var1);
         } else {
            NoteBlock var9 = (NoteBlock)var1.getBlockData();
            var9.setInstrument(Instrument.PIANO);
            NmsBlockSetterService.OD().setBlockData(var1, var9);
         }
      }

      if (var6 != Material.NOTE_BLOCK) {
         BlockEffectsService.sendBlockChange(NoteBlockDataTable.method9656(var6), var1.getLocation());
      }

      return false;
   }

   public static void method1159(Block var0) {
      method1160(var0, null);
   }

   private static void method1160(Block var0, @Nullable BlockDataBatch var1) {
      Material var2 = var0.getType();
      if (var2 != Material.NOTE_BLOCK) {
         NmsBlockSetterService.OD().bk(var0);
      } else {
         NoteBlock var3 = (NoteBlock)var0.getBlockData();
         var3.setInstrument(Instrument.PIANO);
         NmsBlockSetterService.OD().setBlockData(var0, var3);
      }

      BlockData var4 = NoteBlockDataTable.method9656(var2);
      if (var4 != null) {
         if (var1 == null) {
            BlockEffectsService.sendBlockChange(var4, var0.getLocation());
         } else {
            var1.setBlockAt(var0.getX(), var0.getY(), var0.getZ(), var4);
         }
      }
   }

   public void method1161(Location var1) {
      List var2 = ChunkBlockHookHolder.getInstance().hook.findNoteBlocksInColumn(var1);
      if (!var2.isEmpty()) {
         Collection var3 = BlockFaceUtils.playersInView(var1);
         BlockDataBatch var4 = new BlockDataBatch(var3);

         for (Location var6 : var2) {
            var4.setBlock(var6.toVector(), var6.getBlock().getBlockData());
         }

         var4.flush();
      }
   }

   public void method1162(Location var1, boolean var2) {
      for (double var3 = var1.getX() - 5.0; var3 < var1.getX() + 5.0; var3++) {
         for (double var5 = var1.getY() - 5.0; var5 < var1.getY() + 5.0; var5++) {
            for (double var7 = var1.getZ() - 5.0; var7 < var1.getZ() + 5.0; var7++) {
               this.method1157(var1.getWorld().getBlockAt((int)var3, (int)var5, (int)var7), false, true, var2);
            }
         }
      }
   }

   public void method1163(Location var1) {
      this.method1162(var1, false);
   }

   boolean method1164(Block var1) {
      return this.method1165(var1, false);
   }

   boolean method1165(Block var1, boolean var2) {
      boolean var3 = false;

      for (BlockFace var7 : ahE) {
         if (this.method1158(var1.getRelative(var7), false, true, true, var2)) {
            var3 = true;
         }
      }

      return var3;
   }

   boolean method1166(Block var1) {
      for (BlockFace var5 : ahE) {
         if (CustomBlockDefinition.isCustomBlockMaterial(var1.getRelative(var5).getType())) {
            return true;
         }
      }

      return false;
   }

   boolean method1167(Block var1) {
      for (BlockFace var5 : ahE) {
         Block var6 = var1.getRelative(var5);
         if (CustomBlockDefinition.isCustomBlockType(var6) && ContentManager.method8084().method8183(var6).isCustomBlock(var6)) {
            return true;
         }
      }

      return false;
   }

   private static boolean method1168(BlockPhysicsEvent var0, Material var1) {
      Material var2 = var0.getBlock().getType();
      Material var3 = var0.getSourceBlock().getType();
      return method1169(var0.getChangedType(), var2, var3, var1);
   }

   private static boolean method1169(Material var0, Material var1, Material var2, Material var3) {
      if (var3 == null || var0 != var3 && var1 != var3 && var2 != var3) {
         return var0 == Material.WATER || var1 == Material.WATER || var2 == Material.WATER
            ? true
            : var0 == Material.LAVA || var1 == Material.LAVA || var2 == Material.LAVA;
      } else {
         return false;
      }
   }

   private void method1170(BlockPhysicsEvent var1) {
      if (Settings.get().customBlocksChorus) {
         Block var2 = var1.getBlock();
         if (TreePopulator.method1579(var2.getX() >> 4, var2.getZ() >> 4)) {
            var1.setCancelled(true);
         } else {
            Material var3 = var2.getType();
            Material var4 = var1.getSourceBlock().getType();
            if (var1.getChangedType() != Material.CHORUS_PLANT && var3 != Material.CHORUS_PLANT && var4 != Material.CHORUS_PLANT) {
               if (method1171(var1.getSourceBlock(), var4)) {
                  var1.setCancelled(true);
               }
            } else {
               var1.setCancelled(true);
               this.method1164(var1.getSourceBlock());
               CustomBlockChunkStoreBase var5 = ContentManager.method8084().method8183(var2);
               if (var5.hasBlockEntryAt(var2)) {
                  CustomBlockDefinition var6 = ContentManager.method8084().method8160(var5.getCustomBlockId(var2));
                  if (var1.getSourceBlock().getType() == Material.WATER) {
                     var5.removeCustomBlockAndRefresh(var2, true);
                     var1.getSourceBlock().setType(Material.AIR, false);
                     LocationScanner.dropItem(var2.getLocation(), var6);
                     SchedulerUtils.runLaterSync(() -> var2.setType(Material.AIR, false), 2L);
                     return;
                  }
               }
            }
         }
      }
   }

   public static boolean method1171(Block var0, Material var1) {
      if (!method1156(var1) && var1 != Material.AIR && var1 != Material.CAVE_AIR) {
         return false;
      }

      CustomBlockChunkStoreBase var2 = ContentManager.method8084().method8183(var0);
      if (var2 == null) {
         return false;
      }

      if (var2.isCustomBlock(var0)) {
         CustomBlockDefinition var3 = ContentManager.method8084().method8160(var2.getCustomBlockId(var0));
         if (var3 != null) {
            var3.anF.aE(var0);
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean method1172(BlockPhysicsEvent var1) {
      if (!Settings.get().customBlocksMushroom) {
         return false;
      }

      if (var1.getChangedType() == null || var1.getSourceBlock() == null) {
         return false;
      }

      if (!StructureGrowListener.method1073(var1.getChangedType())) {
         return false;
      }

      if (method1168(var1, null)) {
         return false;
      }

      var1.setCancelled(true);
      this.method1157(var1.getBlock(), false, false, true);
      return true;
   }

   public static void method1173(ChunkLoadEvent var0, CustomBlockChunkStoreBase var1) {
      Chunk var2 = var0.getChunk();
      List var3 = null;
      if (Settings.get().recoverLegacyBlockPositions) {
         int var4 = var2.getX() <= 0 ? 1 : 0;
         int var5 = var2.getZ() <= 0 ? 1 : 0;

         for (int var6 = 0; var6 <= var4; var6++) {
            for (int var7 = 0; var7 <= var5; var7++) {
               if (var6 != 0 || var7 != 0) {
                  int var8 = var2.getX() - var6;
                  int var9 = var2.getZ() - var7;
                  if (var2.getWorld().isChunkLoaded(var8, var9)) {
                     Chunk var10 = var2.getWorld().getChunkAt(var8, var9);
                     CustomBlockChunkStoreBase var11 = ContentManager.method8084().method8182(var10);
                     if (var11 != null) {
                        var11.refreshNeighborBlocks(var10);
                     }
                  }
               }
            }
         }
      }

      if (Settings.get().recoverLegacyBlockPositions && !var0.isNewChunk()) {
         var1.refreshNeighborBlocks(var2);
      }

      if (method1174(Settings.get().field36, var0.isNewChunk())) {
         method1175(var2, var1);
      }

      if (Settings.get().unregBlockstatesRegisterAsCustom) {
         if (Settings.get().unregBlockstatesOnlyNewChunks && !var0.isNewChunk()) {
            return;
         }

         if (var3 == null) {
            var3 = ChunkBlockHookHolder.getInstance().hook.findTrackedBlocksInChunk(var2);
         }

         if (var3.isEmpty()) {
            return;
         }

         int var12 = 0;

         for (Location var16 : var3) {
            if (!var1.isCustomBlockAt(var16)) {
               Block var18 = var16.getBlock();
               CustomBlockDefinition var20 = ContentManager.method8166(var18.getBlockData());
               if (var20 != null) {
                  var1.setCustomBlock(var18, var20.getNamespacedID());
                  if (Settings.get().logConvertVanillaBlocks) {
                     Log.log(
                        "Converted vanilla block "
                           + var18.getBlockData().getAsString(true)
                           + " to "
                           + var20.aok
                           + " | "
                           + BlockFaceUtils.locationCoordsString(var18.getLocation())
                     );
                     var12++;
                  }
               } else if (Settings.get().logConvertVanillaBlocks) {
                  Log.log("No custom block found for vanilla block " + BlockFaceUtils.locationCoordsString(var18.getLocation()));
               }
            }
         }

         if (Settings.get().logConvertVanillaBlocks && var12 > 0) {
            Log.log(ChatColor.GREEN + "Converted vanilla blocks: " + var12);
         }
      } else if (Settings.get().unregBlockstatesRestoreVanilla) {
         if (Settings.get().unregBlockstatesOnlyNewChunksAlias && !var0.isNewChunk()) {
            return;
         }

         if (var1.fileWriter.isChunkFixed(var2)) {
            return;
         }

         if (var3 == null) {
            var3 = ChunkBlockHookHolder.getInstance().hook.findTrackedBlocksInChunk(var2);
         }

         if (var3.isEmpty()) {
            return;
         }

         BlockDataBatch var13 = null;

         for (Location var17 : var3) {
            if (!var1.isCustomBlockAt(var17)) {
               Block var19 = var17.getBlock();
               if (var13 == null) {
                  Collection var21 = ServerCompat.isPaper() ? var2.getPlayersSeeingChunk() : BlockFaceUtils.playersInView(var17);
                  var13 = new BlockDataBatch(var21);
               }

               method1160(var19, var13);
               if (Settings.get().logFixBuggedBlocks) {
                  Log.log("Fixed glitched block at " + BlockFaceUtils.locationCoordsString(var19.getLocation()));
               }
            }
         }

         if (var13 != null) {
            var13.flush();
            var1.fileWriter.markChunkFixed(var2);
         }
      }
   }

   static boolean method1174(boolean var0, boolean var1) {
      return var0 && !var1;
   }

   private static void method1175(Chunk var0, CustomBlockChunkStoreBase var1) {
      BlockDataBatch[] var2 = new BlockDataBatch[1];
      ArrayList var3 = new ArrayList();
      var1.forEachBlockInChunk(var0, (var4, var5) -> {
         CustomBlockDefinition var6 = ContentManager.method8084().method8160(var4);
         if (var6 != null) {
            CustomBlockVariant var7 = var6.anF;
            if (var7 != null) {
               if (!Settings.get().recoverLegacyBlockPositions || var5.getWorld().isChunkLoaded(var5.getBlockX() >> 4, var5.getBlockZ() >> 4)) {
                  Block var8 = var5.getBlock();
                  if (!Settings.get().recoverLegacyBlockPositions || !var1.relocateMatchingNeighbor(var8, var7.getMaterial(), var7.getBlockData())) {
                     if (var8.getType().isAir()) {
                        var3.add(var8);
                     } else {
                        BlockData var9 = var7.getBlockData();
                        boolean var10 = var9 == null ? var8.getType() != var7.getMaterial() : !var9.equals(var8.getBlockData());
                        if (var10) {
                           if (var9 == null) {
                              var7.aE(var8);
                           } else {
                              var7.method10029(var8, false);
                              if (var2[0] == null) {
                                 Collection var11 = ServerCompat.isPaper() ? var0.getPlayersSeeingChunk() : BlockFaceUtils.playersInView(var5);
                                 var2[0] = new BlockDataBatch(var11);
                              }

                              var2[0].setBlockAt(var8.getX(), var8.getY(), var8.getZ(), var9);
                           }
                        }
                     }
                  }
               }
            }
         }
      });
      var3.forEach(var1::removeCustomBlock);
      if (var2[0] != null) {
         var2[0].flush();
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onBlockIgnite(BlockIgniteEvent var1) {
      if (var1.getCause() != IgniteCause.FLINT_AND_STEEL) {
         Block var2 = var1.getBlock().getRelative(BlockFace.DOWN);
         if (CustomBlockDefinition.isCustomBlockType(var2) && ContentManager.isCustomBlock(var2)) {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onBlockPhysicsCustom(BlockPhysicsEvent var1) {
      if (!this.method1172(var1)) {
         this.method1170(var1);
      }
   }

   @EventHandler
   private void onCustomBlockBreak(CustomBlockBreakEvent var1) {
      if (!var1.isCancelled()) {
         this.method1163(var1.getBlock().getLocation());
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onBlockPlaceCustom(BlockPlaceEvent var1) {
      if (!EventRegistrar.isCustomBlockPlaceEvent(var1)) {
         if (!ItemUseHandler.aX(var1.getPlayer())) {
            if (CustomBlockDefinition.isCustomBlockMaterial(var1.getBlockPlaced().getType())) {
               Material var2 = var1.getBlockPlaced().getType();
               if (Settings.get().customBlocksMushroom
                  && CustomBlockDefinition.isMushroomMaterial(var2)
                  && (!Settings.get().customBlocksNote || !CustomBlockDefinition.isNoteMaterial(var2))
                  && (!Settings.get().customBlocksChorus || !CustomBlockDefinition.isChorusMaterial(var2))) {
                  var1.setCancelled(true);
                  Block var3 = var1.getBlockPlaced();
                  if (var1.getBlockPlaced().getType() == Material.CHORUS_PLANT) {
                     for (Block var5 : LocationScanner.getNeighborBlocksWith(var1.getBlock(), false, true)) {
                        CustomBlockChunkStoreBase var6 = ContentManager.method8084().method8183(var5);
                        if (var6.isCustomBlock(var5)) {
                           var1.setCancelled(true);
                           this.method1163(var5.getLocation());
                           return;
                        }
                     }
                  }

                  SchedulerUtils.runLaterSync(() -> {
                     NmsBlockSetterService.OD().method3401(var3, var2);
                     var1.getBlock().setBlockData(NoteBlockDataTable.method9656(var2), false);
                     if (var1.getPlayer().getGameMode() != GameMode.CREATIVE) {
                        ItemStackUtils.method7252(var1);
                     }
                  }, 0L);
                  if (this.method1166(var3)) {
                     Location var7 = var3.getLocation();
                     if (var1.getBlockPlaced().getType() != Material.NOTE_BLOCK) {
                        this.method1163(var7);
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onBlockExplode(BlockExplodeEvent var1) {
      if (!BlockEffectsService.getAdapter().handlesEvent(var1)) {
         this.method1183(var1.blockList(), var1.getBlock().getLocation().add(0.5, 0.5, 0.5), null);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onEntityExplode(EntityExplodeEvent var1) {
      UUID var2 = var1.getEntity().getUniqueId();
      if (BlockEffectsService.getAdapter().handlesEvent(var1)) {
         this.ahL.remove(var2);
      } else {
         this.method1183(var1.blockList(), var1.getLocation(), var2);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onExplosionPrimeCustom(ExplosionPrimeEvent var1) {
      this.ahL.put(var1.getEntity().getUniqueId(), var1.getRadius());
   }

   void method1183(List var1, Location var2, @Nullable UUID var3) {
      float var4 = var3 == null ? 1.0F : this.ahL.getOrDefault(var3, 1.0F);
      if (var3 != null) {
         this.ahL.remove(var3);
      }

      LongOpenHashSet var5 = null;
      IdentityHashMap var6 = null;
      ListIterator var7 = var1.listIterator();

      while (var7.hasNext()) {
         Block var8 = (Block)var7.next();
         CustomBlockDefinition var9 = this.method1184(var8);
         if (var9 != null) {
            if (var9.anK) {
               var7.remove();
               var9.placeBlockDefault(var8);
               if (var5 == null) {
                  var5 = new LongOpenHashSet();
               }

               var5.add(method1186(var8.getX(), var8.getY(), var8.getZ()));
            } else {
               if (var9.getBreakTime() > 0.0F) {
                  float var10 = var4 * (0.7F + Utils.random.nextFloat() * 0.6F);
                  var10 -= (var9.getBreakTime() + 0.3F) * 0.3F;
                  if (var10 <= 0.0F) {
                     var7.remove();
                     var9.placeBlockDefault(var8);
                     if (var5 == null) {
                        var5 = new LongOpenHashSet();
                     }

                     var5.add(method1186(var8.getX(), var8.getY(), var8.getZ()));
                     continue;
                  }
               }

               if (var6 == null) {
                  var6 = new IdentityHashMap();
               }

               var6.put(var8, var9);
            }
         }
      }

      if (var5 != null) {
         double var17 = var2.getX();
         double var21 = var2.getY();
         double var12 = var2.getZ();
         var7 = var1.listIterator();

         while (var7.hasNext()) {
            Block var14 = (Block)var7.next();
            if (method1185(var17, var21, var12, var14.getX(), var14.getY(), var14.getZ(), var5)) {
               var7.remove();
            }
         }
      }

      if (var6 != null) {
         var7 = var1.listIterator();

         while (var7.hasNext()) {
            Block var18 = (Block)var7.next();
            CustomBlockDefinition var19 = (CustomBlockDefinition)var6.get(var18);
            if (var19 != null) {
               ExplosiveBlockSpec var22 = var19.getExplosiveSpec();
               if (var22 != null && var22.hasTrigger(8)) {
                  var22.explode(var19, var18.getLocation());
                  var7.remove();
               } else {
                  var7.remove();
                  CustomBlockChunkStoreBase var11 = ContentManager.method8084().method8183(var18);
                  var11.removeCustomBlockAndRefreshNms(var18, true, true);
                  List var23 = ContentManager.method8084().aJ.method1985(var18, var19);
                  if (var23 != null && !var23.isEmpty()) {
                     var23.forEach(var1x -> var1x.method2002(var18.getLocation()));
                  } else if (var19.Dj()) {
                     LocationScanner.dropItem(var18.getLocation(), var19);
                  }
               }
            }
         }
      }
   }

   @Nullable
   private CustomBlockDefinition method1184(Block var1) {
      CustomBlockChunkStoreBase var2 = ContentManager.method8084().method8183(var1);
      if (!var2.isCustomBlock(var1)) {
         return null;
      }

      PlacedBlockItemDefinition var3 = (PlacedBlockItemDefinition)ContentManager.method8084().method8154(var2.getCustomBlockId(var1));
      return var3 != null && var3.aps != BlockRealizationMode.FIRE ? (CustomBlockDefinition)var3 : null;
   }

   static boolean method1185(double var0, double var2, double var4, int var6, int var7, int var8, LongOpenHashSet var9) {
      int var10 = Location.locToBlock(var0);
      int var11 = Location.locToBlock(var2);
      int var12 = Location.locToBlock(var4);
      if (var10 == var6 && var11 == var7 && var12 == var8) {
         return false;
      }

      if (var9.contains(method1186(var10, var11, var12))) {
         return true;
      }

      double var13 = var6 + 0.5 - var0;
      double var15 = var7 + 0.5 - var2;
      double var17 = var8 + 0.5 - var4;
      int var19 = var13 > 0.0 ? 1 : (var13 < 0.0 ? -1 : 0);
      int var20 = var15 > 0.0 ? 1 : (var15 < 0.0 ? -1 : 0);
      int var21 = var17 > 0.0 ? 1 : (var17 < 0.0 ? -1 : 0);
      double var22 = var19 == 0 ? Double.POSITIVE_INFINITY : 1.0 / Math.abs(var13);
      double var24 = var20 == 0 ? Double.POSITIVE_INFINITY : 1.0 / Math.abs(var15);
      double var26 = var21 == 0 ? Double.POSITIVE_INFINITY : 1.0 / Math.abs(var17);
      double var28 = var19 > 0 ? (var10 + 1 - var0) / var13 : (var19 < 0 ? (var0 - var10) / -var13 : Double.POSITIVE_INFINITY);
      double var30 = var20 > 0 ? (var11 + 1 - var2) / var15 : (var20 < 0 ? (var2 - var11) / -var15 : Double.POSITIVE_INFINITY);
      double var32 = var21 > 0 ? (var12 + 1 - var4) / var17 : (var21 < 0 ? (var4 - var12) / -var17 : Double.POSITIVE_INFINITY);

      do {
         double var34 = Math.min(var28, Math.min(var30, var32));
         if (var34 > 1.0) {
            return false;
         }

         boolean var36 = var28 == var34;
         boolean var37 = var30 == var34;
         boolean var38 = var32 == var34;
         if (var36) {
            var10 += var19;
            var28 += var22;
         }

         if (var37) {
            var11 += var20;
            var30 += var24;
         }

         if (var38) {
            var12 += var21;
            var32 += var26;
         }

         if (var10 == var6 && var11 == var7 && var12 == var8) {
            return false;
         }
      } while (!var9.contains(method1186(var10, var11, var12)));

      return true;
   }

   static long method1186(int var0, int var1, int var2) {
      return (long)(var0 & 67108863) << 38 | (long)(var2 & 67108863) << 12 | var1 & 4095L;
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onPistonExtendCustom(BlockPistonExtendEvent var1) {
      for (Block var3 : var1.getBlocks()) {
         if (CustomBlockDefinition.isCustomBlockType(var3)) {
            CustomBlockChunkStoreBase var4 = ContentManager.method8084().method8183(var3);
            if (var4.isCustomBlock(var3)) {
               var1.setCancelled(true);
               LocationScanner.dropItem(var1.getBlock().getLocation(), new ItemStack(var1.getBlock().getType()));
               var1.getBlock().setType(Material.AIR);
               return;
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onPistonRetract(BlockPistonRetractEvent var1) {
      for (Block var3 : var1.getBlocks()) {
         if (CustomBlockDefinition.isCustomBlockType(var3)) {
            CustomBlockChunkStoreBase var4 = ContentManager.method8084().method8183(var3);
            if (var4.isCustomBlock(var3)) {
               var1.setCancelled(true);
               LocationScanner.dropItem(var1.getBlock().getLocation(), new ItemStack(var1.getBlock().getType()));
               var1.getBlock().setType(Material.AIR);
               return;
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method1189(BlockPlaceEvent var1) {
      if (!EventRegistrar.isCustomBlockPlaceEvent(var1)) {
         if (var1.getBlockPlaced().getType().toString().contains("REDSTONE")) {
            Block var2 = var1.getBlockPlaced();
            if (this.method1166(var2)) {
               SchedulerUtils.runLaterSync(() -> this.method1163(var2.getLocation()), 5L);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onBlockBreakCustom(BlockBreakEvent var1) {
      if (!EventRegistrar.isCustomBlockBreakEvent(var1)) {
         if (var1.getBlock().getType().toString().contains("REDSTONE")) {
            Block var2 = var1.getBlock();
            if (this.method1166(var2)) {
               SchedulerUtils.runLaterSync(() -> this.method1162(var2.getLocation(), true), 20L);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.LOWEST)
   private void onCreativeInventory(InventoryCreativeEvent var1) {
      if (var1.getWhoClicked().getGameMode() == GameMode.CREATIVE) {
         if (var1.getWhoClicked() instanceof Player) {
            ItemStack var2 = var1.getCursor();
            if (var1.getSlotType() == SlotType.QUICKBAR && var1.getClick() == ClickType.CREATIVE && var1.getAction() == InventoryAction.PLACE_ALL) {
               Player var3 = (Player)var1.getWhoClicked();
               if (dn.method730(var3)) {
                  return;
               }

               CustomItemDefinition var4 = ContentManager.method8084().method8149(var2);
               if (var4 != null && var4.aoh.hasAction("furniture")) {
                  Entity var5 = ItemEntityAction.aC(var3);
                  CustomFurniture var6 = var5 == null ? null : CustomFurniture.byAlreadySpawned(var5);
                  if (var6 != null && var6.getNamespacedID().equals(var4.getNamespacedID())) {
                     this.handleCreativeItem(var1, var4);
                     return;
                  }
               }

               if (CustomBlockDefinition.isCustomBlockMaterialOrString(var2.getType())) {
                  Block var9 = PlayerInteractionFacade.cL(var3);
                  CustomBlockChunkStoreBase var11 = ContentManager.method8084().method8183(var9);
                  if (var11.isCustomBlock(var9)) {
                     CustomBlockDefinition var7 = ContentManager.method8084().method8160(var11.getCustomBlockId(var9));
                     if (var7 == null) {
                        return;
                     }

                     if (var7.anF != null && !var7.anF.method10027(var2.getType())) {
                        return;
                     }

                     this.handleCreativeItem(var1, var7);
                  }
               } else if (var2.getType() == Material.BARRIER || var2.getType() == Material.ARMOR_STAND || ProjectileTypeHelper.method3228(var2.getType())) {
                  Entity var10 = ItemEntityAction.aC(var3);
                  if (var10 != null) {
                     CustomFurniture var12 = CustomFurniture.byAlreadySpawned(var10);
                     if (var12 != null) {
                        CustomItemDefinition var13 = ContentManager.method8084().method8149(var12.getItemStack());
                        if (var13 != null) {
                           ItemEntityAction var8 = (ItemEntityAction)var13.aoh.getAction("furniture");
                           if (var8 != null) {
                              this.handleCreativeItem(var1, var13);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void handleCreativeItem(InventoryCreativeEvent var1, CustomItemDefinition var2) {
      var1.setCancelled(true);
      var1.setCursor(ItemStackUtils.acf());
      HumanEntity var3 = var1.getWhoClicked();
      PlayerInventory var4 = var3.getInventory();

      for (int var5 = 0; var5 <= 8; var5++) {
         if (!ItemStackUtils.aE(var4.getItem(var5)) && CustomBlockDefinition.c(var4.getItem(var5), var2)) {
            var4.setHeldItemSlot(var5);
            var4.setItem(var5, var2);
            return;
         }
      }

      if (ItemStackUtils.aE(var4.getItemInMainHand())) {
         var4.setItemInMainHand(var2);
      } else {
         for (int var6 = 0; var6 <= 8; var6++) {
            if (ItemStackUtils.aE(var4.getItem(var6))) {
               var4.setHeldItemSlot(var6);
               var4.setItem(var6, var2);
               return;
            }
         }

         var4.setItemInMainHand(var2);
      }
   }
}
