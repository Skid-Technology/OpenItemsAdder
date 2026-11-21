package dev.lone.itemsadder.item;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.ServerCompat;
import java.util.function.Consumer;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class GrindstoneListener implements IaListener {
   public GrindstoneListener(Plugin var1) {
      EventRegistrar.registerIfAbsent(this, var1);
      if (ServerCompat.classExists("com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent")) {
         EventRegistrar.registerIfAbsent(new PaperGrindstoneListener(), var1);
      } else if (ServerCompat.classExists("org.bukkit.event.inventory.PrepareGrindstoneEvent")) {
         EventRegistrar.registerIfAbsent(new BukkitGrindstoneListener(), var1);
      } else {
         EventRegistrar.registerIfAbsent(new LegacyGrindstoneListener(), var1);
      }
   }

   static void method1068(InventoryEvent var0, Consumer var1) {
      Inventory var2 = var0.getInventory();
      ItemStack var3 = var2.getItem(0);
      ItemStack var4 = var2.getItem(1);
      if (ItemStackUtils.aE(var4)) {
         ItemStack var5 = var2.getItem(2);
         method1070(var3, var5, var1);
      } else {
         method1069(var3, var4, var2, var1);
      }
   }

   static void method1069(ItemStack var0, ItemStack var1, Inventory var2, Consumer var3) {
      if (CustomItemDefinition.isCustomItem(var0) || CustomItemDefinition.isCustomItem(var1)) {
         if (!CustomItemDefinition.method8780(var0, var1)) {
            var3.accept(ItemStackUtils.acf());
         } else {
            ItemStack var4 = var2.getItem(2);
            method1070(var0, var4, var3);
         }
      }
   }

   private static void method1070(ItemStack var0, ItemStack var1, Consumer var2) {
      if (var1 != null) {
         CustomItemDefinition var3 = ContentManager.method8084().method8149(var1);
         if (var3 != null) {
            CustomItemDefinition.method8758(var3, var1);
            if (Settings.get().fixAttackDamageLore) {
               CustomItemDefinition.method8802(var1, EquipmentSlotType.MAINHAND);
            }

            if (var1.equals(var0)) {
               var2.accept(ItemStackUtils.acf());
            } else {
               var2.accept(var1);
            }
         }
      }
   }
}
