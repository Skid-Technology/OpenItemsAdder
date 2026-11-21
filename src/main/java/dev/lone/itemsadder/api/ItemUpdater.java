package dev.lone.itemsadder.api;

import dev.lone.itemsadder.api.ItemUpdater.State;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemMetaTracker;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

public final class ItemUpdater {
   private ItemUpdater() {
   }

   @Nullable
   public static String snapshot(@Nullable ItemStack item) {
      CustomItemDefinition var1 = ContentManager.method8084().method8149(item);
      return var1 != null && var1.DQ() != null ? encode(var1.getNamespacedID(), var1.DQ()) : null;
   }

   public static String snapshot(Inventory inventory) {
      requireVirtualInventory(inventory);
      StringBuilder var1 = new StringBuilder("1");

      for (int var2 = 0; var2 < inventory.getSize(); var2++) {
         String var3 = snapshot(inventory.getItem(var2));
         if (var3 != null) {
            var1.append(';').append(var2).append(',').append(var3, 2, var3.length());
         }
      }

      return var1.toString();
   }

   @Nullable
   public static ItemStack update(@Nullable ItemStack item, String savedState) {
      State var2 = parseItem(savedState);
      CustomItemDefinition var3 = ContentManager.method8084().method8149(item);
      return var3 != null && needsUpdate(var2, var3.getNamespacedID(), var3.DQ()) ? ItemMetaTracker.updateItemFromDefinition(item) : null;
   }

   public static int update(Inventory inventory, String savedState) {
      requireVirtualInventory(inventory);
      Map var2 = parseInventory(savedState, inventory.getSize());
      int var3 = 0;

      for (Entry var5 : var2.entrySet()) {
         int var6 = (Integer)var5.getKey();
         ItemStack var7 = inventory.getItem(var6);
         CustomItemDefinition var8 = ContentManager.method8084().method8149(var7);
         State var9 = (State)var5.getValue();
         if (var8 != null && needsUpdate(var9, var8.getNamespacedID(), var8.DQ())) {
            ItemStack var10 = ItemMetaTracker.updateItemFromDefinition(var7);
            if (var10 != null) {
               inventory.setItem(var6, var10);
               var3++;
            }
         }
      }

      return var3;
   }

   private static void requireVirtualInventory(Inventory inventory) {
      InventoryHolder var1 = inventory.getHolder();
      if (inventory instanceof PlayerInventory
         || inventory.getLocation() != null
         || var1 instanceof Entity
         || var1 instanceof BlockInventoryHolder
         || var1 instanceof DoubleChest) {
         throw new IllegalArgumentException("ItemUpdater inventory snapshots require a virtual inventory without a physical owner");
      }
   }

   static String encode(String id, long hash) {
      return "1," + Long.toUnsignedString(hash, 36) + "," + id;
   }

   static boolean needsUpdate(State previous, String currentId, @Nullable Long currentHash) {
      return currentHash != null && previous.id().equals(currentId) && previous.hash() != currentHash;
   }

   static State parseItem(String savedState) {
      String[] var1 = savedState.split(",", 3);
      if (var1.length == 3 && var1[0].equals("1")) {
         return parseState(var1[1], var1[2]);
      } else {
         throw new IllegalArgumentException("Invalid item update snapshot");
      }
   }

   static Map parseInventory(String savedState, int size) {
      String[] var2 = savedState.split(";", -1);
      if (!var2[0].equals("1")) {
         throw new IllegalArgumentException("Invalid inventory update snapshot");
      }

      LinkedHashMap var3 = new LinkedHashMap();

      for (int var4 = 1; var4 < var2.length; var4++) {
         String[] var5 = var2[var4].split(",", 3);
         if (var5.length != 3) {
            throw new IllegalArgumentException("Invalid inventory update snapshot entry");
         }

         try {
            int var6 = Integer.parseInt(var5[0]);
            if (var6 < 0 || var6 >= size || var3.putIfAbsent(var6, parseState(var5[1], var5[2])) != null) {
               throw new IllegalArgumentException("Invalid inventory update snapshot slot: " + var6);
            }
         } catch (NumberFormatException var7) {
            throw new IllegalArgumentException("Invalid inventory update snapshot slot", var7);
         }
      }

      return var3;
   }

   private static State parseState(String hash, String id) {
      if (!id.isEmpty() && id.indexOf(44) < 0 && id.indexOf(59) < 0) {
         try {
            return new State(Long.parseUnsignedLong(hash, 36), id);
         } catch (NumberFormatException var3) {
            throw new IllegalArgumentException("Invalid item update snapshot hash", var3);
         }
      } else {
         throw new IllegalArgumentException("Invalid item update snapshot ID");
      }
   }
}
