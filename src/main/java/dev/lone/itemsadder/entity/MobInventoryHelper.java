package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.util.LocationScanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Mob;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public final class MobInventoryHelper {
   public static final NamespacedKey aai = MobPersistentKeys.keyOf("cargo");

   private MobInventoryHelper() {
   }

   public static boolean hasInventory(Mob var0) {
      return var0.getPersistentDataContainer().has(aai, PersistentDataType.STRING);
   }

   private static String method9581(Mob var0) {
      return (String)var0.getPersistentDataContainer().get(aai, PersistentDataType.STRING);
   }

   public static List method9582(Mob var0) {
      String var1 = method9581(var0);
      if (var1 == null) {
         return new ArrayList();
      }

      if (var1.length() > 2000000) {
         throw new IllegalStateException("Cargo exceeds persistence limit; retained unchanged");
      }

      YamlConfiguration var2 = new YamlConfiguration();

      try {
         var2.loadFromString(var1);
      } catch (InvalidConfigurationException var9) {
         throw new IllegalStateException("Invalid cargo; retained unchanged", var9);
      }

      List var3 = var2.getList("items");
      if (var3 != null && var3.size() <= 128) {
         ArrayList var4 = new ArrayList();
         int var5 = 0;
         Iterator var6 = var3.iterator();

         while (var6.hasNext()) {
            if (!(var6.next() instanceof ItemStack var8) || var8.getType().isAir() || var8.getAmount() < 1 || var8.getAmount() > var8.getMaxStackSize()) {
               throw new IllegalStateException("Invalid cargo item; retained unchanged");
            }

            var5 += var8.getAmount();
            var4.add(var8.clone());
         }

         if (var5 > 4096) {
            throw new IllegalStateException("Cargo item limit exceeded; retained unchanged");
         } else {
            return var4;
         }
      } else {
         throw new IllegalStateException("Invalid cargo item list; retained unchanged");
      }
   }

   private static String method9583(List var0) {
      if (var0.isEmpty()) {
         return null;
      } else {
         YamlConfiguration var1 = new YamlConfiguration();
         var1.set("items", var0);
         String var2 = var1.saveToString();
         if (var2.length() > 2000000) {
            throw new IllegalStateException("Cargo exceeds persistence limit");
         } else {
            return var2;
         }
      }
   }

   private static void method9584(Mob var0, String var1) {
      if (var1 == null) {
         var0.getPersistentDataContainer().remove(aai);
      } else {
         var0.getPersistentDataContainer().set(aai, PersistentDataType.STRING, var1);
      }
   }

   public static int method9585(List var0) {
      return var0.stream().mapToInt(ItemStack::getAmount).sum();
   }

   public static int method9586(List var0, ItemStack var1, int var2, int var3) {
      int var4 = Math.min(var2, Math.max(0, var3 - method9585(var0)));
      int var5 = var4;

      for (ItemStack var7 : var0) {
         if (var7.isSimilar(var1)) {
            int var8 = Math.min(var4, Math.max(0, var7.getMaxStackSize() - var7.getAmount()));
            var7.setAmount(var7.getAmount() + var8);
            var4 -= var8;
         }
      }

      while (var4 > 0 && var0.size() < 128) {
         ItemStack var9 = var1.clone();
         int var10 = Math.min(var4, var9.getMaxStackSize());
         var9.setAmount(var10);
         var0.add(var9);
         var4 -= var10;
      }

      return var5 - var4;
   }

   public static int method9587(Mob var0, Item var1, Predicate var2, int var3, int var4) {
      if (MobPersistentKeys.method9611(var0)
         && var1.isValid()
         && var1.getPickupDelay() <= 0
         && (var1.getOwner() == null || var1.getOwner().equals(var0.getUniqueId()))) {
         String var5 = method9581(var0);
         List var6 = method9582(var0);
         ItemStack var7 = var1.getItemStack().clone();
         if (!var2.test(var7)) {
            return 0;
         }

         int var8 = method9586(var6, var7, Math.min(var4, var7.getAmount()), var3);
         if (var8 == 0) {
            return 0;
         }

         EntityPickupItemEvent var9 = new EntityPickupItemEvent(var0, var1, var7.getAmount() - var8);
         Bukkit.getPluginManager().callEvent(var9);
         if (!var9.isCancelled()
            && var1.isValid()
            && var1.getPickupDelay() <= 0
            && (var1.getOwner() == null || var1.getOwner().equals(var0.getUniqueId()))
            && Objects.equals(var5, method9581(var0))
            && var1.getWorld() == var0.getWorld()
            && !(var1.getLocation().distanceSquared(var0.getLocation()) > 9.0)
            && var1.getItemStack().equals(var7)) {
            String var10 = method9583(var6);
            method9584(var0, var10);
            if (var7.getAmount() == var8) {
               var1.remove();
            } else {
               var7.setAmount(var7.getAmount() - var8);
               var1.setItemStack(var7);
            }

            return var8;
         } else {
            return 0;
         }
      } else {
         return 0;
      }
   }

   public static int method9588(Mob var0, Inventory var1, Predicate var2, int var3, int var4) {
      if (!MobPersistentKeys.method9611(var0)) {
         return 0;
      }

      for (int var5 = 0; var5 < var1.getSize(); var5++) {
         ItemStack var6 = var1.getItem(var5);
         if (var6 != null && var2.test(var6)) {
            ItemStack var7 = var6.clone();
            String var8 = method9581(var0);
            List var9 = method9582(var0);
            int var10 = method9586(var9, var7, Math.min(var4, var7.getAmount()), var3);
            if (var10 == 0) {
               return 0;
            }

            ItemStack var11 = var7.clone();
            var11.setAmount(var10);
            Inventory var12 = createInventory(var0, var11);
            InventoryMoveItemEvent var13 = new InventoryMoveItemEvent(var1, var11.clone(), var12, false);
            Bukkit.getPluginManager().callEvent(var13);
            if (!var13.isCancelled() && var13.getItem().equals(var11) && Objects.equals(var8, method9581(var0)) && Objects.equals(var7, var1.getItem(var5))) {
               String var14 = method9583(var9);
               ItemStack var15 = var7.clone();
               var15.setAmount(var7.getAmount() - var10);
               var1.setItem(var5, var15.getAmount() == 0 ? null : var15);

               try {
                  method9584(var0, var14);
                  return var10;
               } catch (RuntimeException var17) {
                  var1.setItem(var5, var7);
                  throw var17;
               }
            }

            return 0;
         }
      }

      return 0;
   }

   public static int method9589(Mob var0, Inventory var1, int var2) {
      String var3 = method9581(var0);
      List var4 = method9582(var0);
      if (var4.isEmpty()) {
         return 0;
      }

      ItemStack var5 = ((ItemStack)var4.get(0)).clone();
      int var6 = Math.min(Math.min(var2, var5.getAmount()), method9590(var1, var5));
      if (var6 == 0) {
         return 0;
      }

      ItemStack var7 = var5.clone();
      var7.setAmount(var6);
      InventoryMoveItemEvent var8 = new InventoryMoveItemEvent(createInventory(var0, var5), var7.clone(), var1, true);
      Bukkit.getPluginManager().callEvent(var8);
      if (!var8.isCancelled() && var8.getItem().equals(var7) && Objects.equals(var3, method9581(var0))) {
         var6 = Math.min(var6, method9590(var1, var5));
         if (var6 == 0) {
            return 0;
         }

         var7.setAmount(var6);
         ItemStack[] var9 = Arrays.stream(var1.getStorageContents()).map(var0x -> var0x == null ? null : var0x.clone()).toArray(ItemStack[]::new);
         int var10 = var1.addItem(new ItemStack[]{var7.clone()}).values().stream().mapToInt(ItemStack::getAmount).sum();
         int var11 = var6 - var10;
         if (var11 <= 0) {
            return 0;
         }

         ((ItemStack)var4.get(0)).setAmount(var5.getAmount() - var11);
         if (((ItemStack)var4.get(0)).getAmount() == 0) {
            var4.remove(0);
         }

         try {
            method9584(var0, method9583(var4));
            return var11;
         } catch (RuntimeException var13) {
            var1.setStorageContents(var9);
            throw var13;
         }
      } else {
         return 0;
      }
   }

   public static int method9590(Inventory var0, ItemStack var1) {
      int var2 = 0;

      for (ItemStack var6 : var0.getStorageContents()) {
         if (var6 == null || var6.getType().isAir()) {
            var2 += Math.min(var1.getMaxStackSize(), var0.getMaxStackSize());
         } else if (var6.isSimilar(var1)) {
            var2 += Math.max(0, Math.min(var6.getMaxStackSize(), var0.getMaxStackSize()) - var6.getAmount());
         }
      }

      return var2;
   }

   public static boolean method9591(List var0, List var1) {
      ArrayList var2 = var0.stream().map(ItemStack::clone).collect(Collectors.toCollection(ArrayList::new));
      return method9592(var2, var1);
   }

   private static boolean method9592(List var0, List var1) {
      for (ItemStack var3 : var1) {
         int var4 = var3.getAmount();

         for (ItemStack var6 : var0) {
            if (var6.isSimilar(var3)) {
               int var7 = Math.min(var4, var6.getAmount());
               var6.setAmount(var6.getAmount() - var7);
               var4 -= var7;
            }
         }

         if (var4 > 0) {
            return false;
         }
      }

      var0.removeIf(var0x -> var0x.getAmount() == 0);
      return true;
   }

   public static boolean method9593(Mob var0, List var1, List var2, int var3, boolean var4) {
      List var5 = method9582(var0);
      if (method9591(var5, var1) && method9592(var5, var1)) {
         if (!var4) {
            for (ItemStack var7 : var2) {
               if (method9586(var5, var7, var7.getAmount(), var3) != var7.getAmount()) {
                  return false;
               }
            }
         }

         String var9 = method9583(var5);
         method9584(var0, var9);
         if (var4) {
            for (ItemStack var8 : var2) {
               LocationScanner.dropItem(var0.getLocation(), var8.clone());
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static List getMobItems(Mob var0) {
      List var1 = method9582(var0);
      method9584(var0, null);
      return var1;
   }

   public static void clearInventory(Mob var0) {
      for (ItemStack var2 : getMobItems(var0)) {
         LocationScanner.dropItem(var0.getLocation(), var2);
      }
   }

   private static Inventory createInventory(Mob var0, ItemStack var1) {
      MobInventoryHolder var2 = new MobInventoryHolder(var0);
      var2.aak = Bukkit.createInventory(var2, 9);
      var2.aak.setItem(0, var1.clone());
      return var2.aak;
   }
}
