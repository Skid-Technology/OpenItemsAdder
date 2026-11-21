package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.nbt.NCompound;
import beer.devs.fastnbt.nms.nbt.NItem;
import com.google.common.collect.UnmodifiableIterator;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.BaseItemsAdderEvent;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.fontimage.FontImageLoader;
import dev.lone.itemsadder.internal.SwitchMap96;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.ServerCompat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import net.jpountz.xxhash.StreamingXXHash64;
import net.jpountz.xxhash.XXHashFactory;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

public class ItemMetaTracker implements Listener {
   private static final NamespacedKey df = new NamespacedKey("itemsadder", "base_items_properties_hashes");

   public ItemMetaTracker() {
      EventRegistrar.registerIfAbsent(this, Main.getInstance());
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   private void onPlayerInteractJukebox(PlayerInteractEvent var1) {
      if (isContentManagerReady()) {
         if (var1.getAction() == Action.RIGHT_CLICK_BLOCK
            && var1.getClickedBlock() != null
            && var1.getClickedBlock().getType() == Material.JUKEBOX
            && var1.getHand() != null) {
            ItemStack var2 = var1.getPlayer().getInventory().getItem(var1.getHand());
            CustomItemDefinition var3 = ContentManager.method8084().method8149(var2);
            if (var3 != null && var3.DR() != null) {
               var3.method8799(var2);
               var1.getPlayer().getInventory().setItem(var1.getHand(), var2);
               if (FontImageLoader.method1929(var3.DR())) {
                  var1.setCancelled(true);
               }
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   private void onHopperMoveIntoJukebox(InventoryMoveItemEvent var1) {
      if (isContentManagerReady()) {
         if (var1.getDestination().getType() == InventoryType.JUKEBOX) {
            CustomItemDefinition var2 = ContentManager.method8084().method8149(var1.getItem());
            if (var2 != null && var2.DR() != null && FontImageLoader.method1929(var2.DR())) {
               var1.setCancelled(true);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   private void onHopperPickupItem(InventoryPickupItemEvent var1) {
      if (isContentManagerReady()) {
         if (migrateEntityPaperItem(var1.getItem())) {
            var1.setCancelled(true);
         } else {
            refreshEntityItemMeta(var1.getItem());
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   private void onPaperMoveFromInventory(InventoryMoveItemEvent var1) {
      if (isContentManagerReady()) {
         if (ContentManager.method8084().method8149(var1.getItem()) instanceof CustomBlockDefinition var3
            && var3.isInventoryBlockStateApplied()
            && var1.getItem().getType() == Material.PAPER) {
            var1.setCancelled(true);
            Inventory var4 = var1.getSource();
            SchedulerUtils.runLaterSync(() -> refreshInventoryItems(var4, null), 1L);
         }
      }
   }

   private static boolean migrateEntityPaperItem(Item var0) {
      if (!isContentManagerReady()) {
         return false;
      }

      ItemStack var1 = var0.getItemStack();
      ItemStack var2 = migratePaperBlockItem(var1, ContentManager.method8084().method8149(var1));
      if (var2 == null) {
         return false;
      }

      var0.setItemStack(var2);
      return true;
   }

   @Nullable
   private static CustomItemDefinition refreshEntityItemMeta(Item var0) {
      if (!isContentManagerReady()) {
         return null;
      }

      ItemStack var1 = var0.getItemStack();
      CustomItemDefinition var2 = ContentManager.method8084().method8149(var1);
      if (var2 != null && var2.method8799(var1)) {
         var0.setItemStack(var1);
      }

      return var2;
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   private void onPlayerJoinRefresh(PlayerJoinEvent var1) {
      if (isContentManagerReady()) {
         Player var2 = var1.getPlayer();
         PlayerInventory var3 = var2.getInventory();
         debugLog(ChatColor.GREEN + "Player joined: " + var2.getName() + ", updating inventory...");
         Map var4 = loadInventorySnapshot(var3, var2.getPersistentDataContainer());
         refreshInventoryItems(var3, var4);
         refreshInventoryItems(var2.getEnderChest(), null);
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   private void onPlayerQuitSave(PlayerQuitEvent var1) {
      if (isContentManagerReady()) {
         Player var2 = var1.getPlayer();

         try {
            savePlayerInventorySnapshot(var2.getInventory());
         } catch (Exception var4) {
            Log.error("Failed to save custom items configs for player: " + var2.getName(), var4);
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   private void onBlockBreakClearMeta(BlockBreakEvent var1) {
      var1.getBlock().removeMetadata(df.toString(), Main.getInstance());
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   private void onItemsAdderEventSaveAll(BaseItemsAdderEvent var1) {
      if (isContentManagerReady()) {
         for (Player var3 : Bukkit.getOnlinePlayers()) {
            try {
               savePlayerInventorySnapshot(var3.getInventory());
            } catch (Exception var5) {
               Log.error("Failed to save custom items configs for player: " + var3.getName(), var5);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   private void onItemsAdderLoadData(ItemsAdderLoadDataEvent var1) {
      if (isContentManagerReady()) {
         for (Player var3 : Bukkit.getOnlinePlayers()) {
            debugLog(ChatColor.GREEN + "Reloaded plugin. Player : " + var3.getName() + ", updating inventory...");
            PlayerInventory var4 = var3.getInventory();
            Map var5 = loadInventorySnapshot(var4, var3.getPersistentDataContainer());
            refreshInventoryItems(var4, var5);
            refreshInventoryItems(var3.getEnderChest(), null);
            refreshInventoryItems(var3.getOpenInventory().getTopInventory(), null);
            ItemStack var6 = var3.getItemOnCursor();
            ItemStack var7 = migratePaperBlockItem(var6, ContentManager.method8084().method8149(var6));
            if (var7 != null) {
               var3.setItemOnCursor(var7);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   private void onInventoryClose(InventoryCloseEvent var1) {
      if (isContentManagerReady()) {
         HumanEntity var2 = var1.getPlayer();
         debugLog(ChatColor.YELLOW + "Closed inventory: " + var2);
         Inventory var3 = var1.getInventory();
         if (var3 instanceof CraftingInventory) {
            try {
               savePlayerInventorySnapshot(var2.getInventory());
            } catch (Exception var8) {
               Log.error("Failed to serialize custom items configs for player: " + var2.getName(), var8);
            }
         } else if (var3.getType() == InventoryType.CHEST) {
            InventoryHolder var4 = ServerCompat.isPaper() ? var3.getHolder(false) : var3.getHolder();
            if (var4 instanceof Entity) {
               return;
            }

            Location var5 = var3.getLocation();
            if (var5 != null) {
               try {
                  saveBlockInventorySnapshot(var3);
               } catch (Exception var7) {
                  Log.error("Failed to serialize custom items configs for block: " + BlockFaceUtils.locationCoordsString(var5), var7);
               }
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   private void onInventoryOpen(InventoryOpenEvent var1) {
      if (isContentManagerReady()) {
         debugLog(ChatColor.GREEN + "Opened inventory");
         Inventory var2 = var1.getInventory();
         TileState var3 = getInventoryTileState(var2);
         Location var4 = var2.getLocation();
         if (var4 != null && var3 != null) {
            debugLog(ChatColor.GREEN + "TileState found for inventory: " + BlockFaceUtils.locationCoordsString(var4) + ", updating inventory...");
            Map var5 = loadInventorySnapshot(var2, var3.getPersistentDataContainer());
            refreshInventoryItems(var2, var5);
         } else {
            refreshInventoryItems(var2, null);
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   private void onEntityPickupItem(EntityPickupItemEvent var1) {
      if (isContentManagerReady()) {
         if (migrateEntityPaperItem(var1.getItem())) {
            var1.setCancelled(true);
         } else {
            CustomItemDefinition var2 = refreshEntityItemMeta(var1.getItem());
            if (var1.getEntity() instanceof Player) {
               if (var2 != null) {
                  Map var3 = loadItemSnapshot(var1.getItem());
                  if (var3 != null) {
                     Long var4 = (Long)var3.get(0);
                     if (var4 != null) {
                        debugLog(ChatColor.GREEN + "Updating item: " + var2.getNamespacedID() + " with hash: " + var4);
                        refreshItemIfHashChanged(var1.getItem(), var4);
                     } else {
                        debugLog(ChatColor.YELLOW + "No previous data found for item: " + var2.getNamespacedID());
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   private void onPlayerDropItem(PlayerDropItemEvent var1) {
      if (isContentManagerReady()) {
         try {
            migrateEntityPaperItem(var1.getItemDrop());
            saveItemSnapshot(var1.getItemDrop());
         } catch (Exception var3) {
            Log.warn("Failed to save custom items configs for dropped item: " + var1.getItemDrop().getItemStack(), var3);
         }
      }
   }

   private static Map snapshotInventoryHashes(Inventory var0) {
      HashMap var1 = new HashMap();

      for (int var2 = 0; var2 < var0.getSize(); var2++) {
         ItemStack var3 = var0.getItem(var2);
         CustomItemDefinition var4 = ContentManager.method8084().method8149(var3);
         if (var4 != null) {
            ConfigSectionWrapper var5 = var4.DS();
            if (var5 != null) {
               Long var6 = var4.DQ();
               debugLog(var2 + " - Hash: " + var6);
               var1.put(var2, var6);
            }
         }
      }

      return var1;
   }

   private static Map snapshotItemHash(Item var0) {
      HashMap var1 = new HashMap();
      ItemStack var2 = var0.getItemStack();
      CustomItemDefinition var3 = ContentManager.method8084().method8149(var2);
      if (var3 != null) {
         ConfigSectionWrapper var4 = var3.DS();
         if (var4 != null) {
            Long var5 = var3.DQ();
            debugLog(" - Hash: " + var5);
            var1.put(0, var5);
         }
      }

      return var1;
   }

   @Nullable
   private static Map readMetadataSnapshot(Metadatable var0) {
      List var1 = var0.getMetadata(df.toString());
      if (var1.isEmpty()) {
         return null;
      }

      MetadataValue var2 = (MetadataValue)var1.getFirst();
      return var2 != null && var2.value() instanceof Map ? (Map)var2.value() : null;
   }

   @Nullable
   private static Map readPdcSnapshot(PersistentDataContainer var0) {
      byte[] var1 = (byte[])var0.get(df, PersistentDataType.BYTE_ARRAY);
      if (var1 == null) {
         debugLog(ChatColor.DARK_GRAY + "CustomItemsConfigs is null");
         return null;
      }

      Map var2;
      try {
         var2 = deserializeSnapshot(var1);
      } catch (Exception var4) {
         Log.error("Failed to deserialize custom items configs", var4);
         return null;
      }

      if (var2.isEmpty()) {
         debugLog("CustomItemsConfigs is empty");
         return null;
      } else {
         return var2;
      }
   }

   @Nullable
   private static Map loadInventorySnapshot(Inventory var0, PersistentDataContainer var1) {
      Location var2 = var0.getLocation();
      Map var3 = null;
      if (var0 instanceof PlayerInventory var4 && var4.getHolder() instanceof Player var5) {
         var3 = readMetadataSnapshot(var5);
      } else if (var2 != null) {
         var3 = readMetadataSnapshot(var2.getBlock());
      }

      if (var3 == null) {
         var3 = readPdcSnapshot(var1);
         if (var3 == null) {
            return null;
         }

         if (var2 != null) {
            var2.getBlock().setMetadata(df.toString(), new FixedMetadataValue(Main.getInstance(), var3));
         } else if (var0 instanceof PlayerInventory var7 && var7.getHolder() instanceof Player var8) {
            var8.setMetadata(df.toString(), new FixedMetadataValue(Main.getInstance(), var3));
         } else {
            debugLog("CustomItemsConfigs is not saved, no location or holder found");
         }
      }

      return var3;
   }

   @Nullable
   private static Map loadItemSnapshot(Item var0) {
      Map var1 = readMetadataSnapshot(var0);
      if (var1 == null) {
         var1 = readPdcSnapshot(var0.getPersistentDataContainer());
         if (var1 == null) {
            return null;
         }

         var0.setMetadata(df.toString(), new FixedMetadataValue(Main.getInstance(), var1));
      }

      return var1;
   }

   private static void saveBlockInventorySnapshot(Inventory var0) {
      Location var1 = var0.getLocation();
      if (var1 == null) {
         throw new RuntimeException("Inventory location is null, cannot save custom items configs");
      }

      Map var2 = snapshotInventoryHashes(var0);
      if (isSnapshotChanged(readMetadataSnapshot(var1.getBlock()), var2)) {
         TileState var3 = getInventoryTileState(var0);
         if (var3 != null) {
            var1.getBlock().setMetadata(df.toString(), new FixedMetadataValue(Main.getInstance(), var2));

            try {
               var3.getPersistentDataContainer().set(df, PersistentDataType.BYTE_ARRAY, serializeSnapshot(var2));
               var3.update(true, false);
            } catch (Exception var5) {
               Log.error("Failed to serialize custom items configs for block: " + BlockFaceUtils.locationCoordsString(var1), var5);
            }
         }
      }
   }

   private static void savePlayerInventorySnapshot(PlayerInventory var0) {
      HumanEntity var1 = var0.getHolder();
      if (!(var1 instanceof Player)) {
         throw new RuntimeException("Inventory holder is null or not a player, cannot save custom items configs");
      }

      Map var2 = snapshotInventoryHashes(var0);
      if (isSnapshotChanged(readMetadataSnapshot(var1), var2)) {
         var1.setMetadata(df.toString(), new FixedMetadataValue(Main.getInstance(), var2));
         var1.getPersistentDataContainer().set(df, PersistentDataType.BYTE_ARRAY, serializeSnapshot(snapshotInventoryHashes(var0)));
      }
   }

   private static void saveItemSnapshot(Item var0) {
      Map var1 = snapshotItemHash(var0);
      var0.setMetadata(df.toString(), new FixedMetadataValue(Main.getInstance(), var1));
      var0.getPersistentDataContainer().set(df, PersistentDataType.BYTE_ARRAY, serializeSnapshot(snapshotItemHash(var0)));
   }

   private static boolean isSnapshotChanged(Map var0, Map var1) {
      if ((var0 != null && !var0.isEmpty() || !var1.isEmpty()) && !var1.equals(var0)) {
         return true;
      }

      debugLog("CustomItemsConfigs is the same, skipping update");
      return false;
   }

   private static void refreshInventoryItems(Inventory var0, Map var1) {
      if (isContentManagerReady()) {
         for (int var2 = 0; var2 < var0.getSize(); var2++) {
            ItemStack var3 = var0.getItem(var2);
            CustomItemDefinition var4 = ContentManager.method8084().method8149(var3);
            if (var4 != null) {
               ItemStack var5 = migratePaperBlockItem(var3, var4);
               if (var5 != null) {
                  var3 = var5;
                  var0.setItem(var2, var5);
               }

               if (var4.method8799(var3)) {
                  var0.setItem(var2, var3);
               }

               if (var1 != null && !var1.isEmpty()) {
                  ConfigSectionWrapper var6 = var4.DS();
                  if (var6 != null) {
                     Long var7 = var4.DQ();
                     Long var8 = (Long)var1.get(var2);
                     if (!Objects.equals(var8, var7)) {
                        debugLog("" + ChatColor.YELLOW + var2 + " - Prev " + var8 + " | New " + var7 + " - UPDATED");
                        ItemStack var9 = updateItemFromDefinition(var3);
                        if (var9 != null) {
                           var0.setItem(var2, var9);
                        }
                     } else {
                        debugLog("" + ChatColor.DARK_GRAY + var2 + " - Prev " + var8 + " | New " + var7 + " - NO CHANGE");
                     }
                  }
               }
            }
         }
      }
   }

   private static void refreshItemIfHashChanged(Item var0, @Nullable Long var1) {
      if (isContentManagerReady()) {
         if (var1 == null) {
            debugLog(ChatColor.YELLOW + "Stored YAML hash is null, skipping update for item: " + var0.getItemStack());
         } else {
            ItemStack var2 = var0.getItemStack();
            CustomItemDefinition var3 = ContentManager.method8084().method8149(var2);
            if (var3 != null) {
               ConfigSectionWrapper var4 = var3.DS();
               if (var4 != null) {
                  Long var5 = var3.DQ();
                  if (!Objects.equals(var1, var5)) {
                     debugLog(ChatColor.YELLOW + "- Prev " + var1 + " | New " + var5 + " - UPDATED");
                     ItemStack var6 = updateItemFromDefinition(var2);
                     if (var6 == null) {
                        return;
                     }

                     var0.setItemStack(var6);
                  } else {
                     debugLog(ChatColor.DARK_GRAY + " - Prev " + var1 + " | New " + var5 + " - NO CHANGE");
                  }
               }
            }
         }
      }
   }

   @Nullable
   public static ItemStack updateItemFromDefinition(ItemStack var0) {
      ContentManager var1 = ContentManager.method8084();
      if (var1 != null && var1.method8111()) {
         CustomItemDefinition var2 = var1.method8149(var0);
         if (var2 == null) {
            return null;
         }

         ItemStack var3 = migratePaperBlockItem(var0, var2);
         if (var3 != null) {
            var0 = var3;
         }

         if (hasOverrideAutoUpdate(var0)) {
            return var3;
         }

         ItemUpdatePolicy var4 = var2.DT();
         if (var4 == null) {
            debugLog("Item " + var2.getNamespacedID() + " is not enabled for updates, skipping.");
            return var3;
         }

         ItemMeta var5 = var0.getItemMeta();
         ItemStack var6 = var2.Ds();
         var6.setAmount(var0.getAmount());
         if (ItemStackUtils.aG(var0)) {
            ItemStackUtils.method7240(var6, ItemStackUtils.method7233(var0));
            int var7 = ItemStackUtils.aw(var0);
            int var8 = ItemStackUtils.method7233(var6);
            int var9 = (int)Math.ceil((double)var7 / ItemStackUtils.method7233(var0) * var8);
            ItemStackUtils.method7235(var6, var9);
         } else if (ItemStackUtils.aA(var0)) {
            ItemStackUtils.method7235(var6, ItemStackUtils.aw(var0));
            ItemStackUtils.method7240(var6, ItemStackUtils.method7233(var0));
         }

         ItemMeta var13 = var6.getItemMeta();
         var5.getPersistentDataContainer().copyTo(var13.getPersistentDataContainer(), true);
         if (var5 instanceof ArmorMeta var14 && var13 instanceof ArmorMeta var18 && var14.hasTrim()) {
            var18.setTrim(var14.getTrim());
         }

         if (var5.hasDisplayName()) {
            try {
               var13.displayName(var5.displayName());
               var6.setItemMeta(var13);
            } catch (Exception var12) {
               Log.errorToSenderWithTrace(null, "Failed to set display name on item: " + var6, var12);
            }
         }

         if (var2.isBlockAllEnchants()) {
            var13.removeEnchantments();
         } else if (var4.getMode() == UpdateMode.MERGE) {
            for (Entry var19 : var5.getEnchants().entrySet()) {
               Enchantment var10 = (Enchantment)var19.getKey();
               Integer var11 = (Integer)var19.getValue();
               if (var2.Dw() == null || !var2.Dw().contains(var10)) {
                  var13.addEnchant(var10, var11, true);
               }
            }
         } else if (var4.getMode() == UpdateMode.REPLACE) {
            var13.removeEnchantments();
            UnmodifiableIterator var16 = var2.DU().entrySet().iterator();

            while (var16.hasNext()) {
               Entry var20 = (Entry)var16.next();
               Enchantment var22 = (Enchantment)var20.getKey();
               Integer var23 = (Integer)var20.getValue();
               var13.addEnchant(var22, var23, true);
            }
         }

         if (var5 instanceof Repairable var17 && var13 instanceof Repairable var21 && var17.hasRepairCost()) {
            var21.setRepairCost(var17.getRepairCost() + 1);
            var6.setItemMeta(var21);
         }

         var6.setItemMeta(var13);
         return var6;
      } else {
         return null;
      }
   }

   @Nullable
   private static ItemStack migratePaperBlockItem(ItemStack var0, @Nullable CustomItemDefinition var1) {
      return var1 instanceof CustomBlockDefinition var2 && var2.isInventoryBlockStateApplied() ? buildPaperBlockStack(var0, var1) : null;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Nullable
   static ItemStack buildPaperBlockStack(ItemStack var0, ItemStack var1) {
      if (var0 != null && var0.getType() == Material.PAPER) {
         boolean var2 = switch (SwitchMap96.dg[var1.getType().ordinal()]) {
            case 1, 2, 3, 4, 5 -> true;
            default -> false;
         };
         if (var2 && var1.getItemMeta() instanceof BlockDataMeta var3 && var3.hasBlockData()) {
            ItemStack var7 = var0.clone();
            var7.setType(var1.getType());
            if (var7.getItemMeta() instanceof BlockDataMeta var5) {
               var5.setBlockData(var3.getBlockData(var1.getType()));
               var5.setCustomModelData(var3.hasCustomModelData() ? var3.getCustomModelData() : null);
               if (!var7.setItemMeta(var5)) {
                  throw new IllegalStateException("Cannot apply migrated block metadata for " + var1.getType());
               } else {
                  return var7;
               }
            } else {
               throw new IllegalStateException("Cannot migrate PAPER to " + var1.getType());
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   @Nullable
   private static TileState getInventoryTileState(Inventory var0) {
      Location var1 = var0.getLocation();
      if (var1 == null) {
         return null;
      }

      Block var2 = var0.getLocation().getBlock();
      return (ServerCompat.isPaper() ? var2.getState(false) : var2.getState()) instanceof TileState var3 ? var3 : null;
   }

   public static void stackSimilarItemsOnClick(InventoryClickEvent var0) {
      if (isContentManagerReady()) {
         ItemStack var1 = var0.getCurrentItem();
         ItemStack var2 = var0.getCursor();
         if (!ItemStackUtils.aE(var1) && !ItemStackUtils.aE(var2)) {
            if (var1.getType() == var2.getType()) {
               if (!var1.isSimilar(var2)) {
                  int var3 = ItemStackUtils.ax(var1);
                  int var4 = ItemStackUtils.ax(var2);
                  if (var3 > 1 && var4 > 1) {
                     if (var3 != var1.getAmount() && var4 != var2.getAmount()) {
                        CustomItemDefinition var5 = ContentManager.method8084().method8149(var1);
                        CustomItemDefinition var6 = ContentManager.method8084().method8149(var2);
                        if (var5 != null && var6 != null) {
                           if (var6.DT() == null || var5.DT() == null || var5.DT().isEnabled() && var6.DT().isEnabled()) {
                              if (var5.getNamespacedID().equals(var6.getNamespacedID())) {
                                 if (!hasOverrideAutoUpdate(var1) && hasOverrideAutoUpdate(var2)) {
                                    ItemStack var7;
                                    ItemStack var8;
                                    if (var5.isSimilar(var1)) {
                                       var7 = var1;
                                       var8 = var2;
                                    } else {
                                       var7 = var2;
                                       var8 = var1;
                                    }

                                    if (ItemOperationsService.getRegistryBridge().method3436(var1, var2)) {
                                       var0.setCancelled(true);
                                       int var9 = var7.getAmount() + var8.getAmount();
                                       if (var9 <= ItemStackUtils.ax(var7)) {
                                          var0.setCursor(null);
                                          var7.setAmount(var9);
                                          var0.setCurrentItem(var7);
                                          debugLog("Stacked items: " + var5.getNamespacedID() + " x" + var7.getAmount());
                                       } else {
                                          var7.setAmount(var3);
                                          var8.setAmount(var9 - var3);
                                          var0.setCurrentItem(var7);
                                          var0.setCursor(var8);
                                          debugLog(
                                             "Stacked items: "
                                                + var5.getNamespacedID()
                                                + " x"
                                                + var7.getAmount()
                                                + " and "
                                                + var6.getNamespacedID()
                                                + " x"
                                                + var2.getAmount()
                                          );
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static byte[] serializeSnapshot(Map var0) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      ObjectOutputStream var2 = new ObjectOutputStream(var1);
      var2.writeObject(var0);
      var2.flush();
      return var1.toByteArray();
   }

   public static Map deserializeSnapshot(byte[] var0) {
      ByteArrayInputStream var1 = new ByteArrayInputStream(var0);
      ObjectInputStream var2 = new ObjectInputStream(var1);
      return (Map)var2.readObject();
   }

   public static void setOverrideAutoUpdate(ItemStack var0, @Nullable Boolean var1) {
      NItem var2 = new NItem(var0);
      NCompound var3 = var2.getCompound("itemsadder");
      if (var3 != null) {
         if (var1 != null && var1) {
            var3.setBoolean("override_auto_update", true);
         } else {
            var3.remove("override_auto_update");
         }

         var2.save();
      }
   }

   public static boolean hasOverrideAutoUpdate(ItemStack var0) {
      NItem var1 = new NItem(var0);
      NCompound var2 = var1.getCompound("itemsadder");
      return var2 == null ? false : var2.hasKey("override_auto_update") && var2.getBoolean("override_auto_update");
   }

   public static long xxHashString(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         byte[] var1 = var0.getBytes();
         byte[] var2 = new byte[8192];
         XXHashFactory var3 = XXHashFactory.fastestInstance();
         StreamingXXHash64 var4 = var3.newStreamingHash64(0L);

         long var11;
         try {
            int var5 = 0;
            int var6 = var1.length;

            while (var6 > 0) {
               int var7 = Math.min(var2.length, var6);
               System.arraycopy(var1, var5, var2, 0, var7);
               var4.update(var2, 0, var7);
               var5 += var7;
               var6 -= var7;
            }

            var11 = var4.getValue();
         } catch (Throwable var10) {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }
            }

            throw var10;
         }

         if (var4 != null) {
            var4.close();
         }

         return var11;
      } else {
         return 0L;
      }
   }

   private static boolean isContentManagerReady() {
      ContentManager var0 = ContentManager.method8084();
      return var0 != null && var0.method8110();
   }

   private static void debugLog(String var0) {
   }
}
