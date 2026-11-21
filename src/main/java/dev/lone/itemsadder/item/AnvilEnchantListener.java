package dev.lone.itemsadder.item;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EventRegistrar;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.Plugin;

public class AnvilEnchantListener implements IaListener {
   final Plugin agJ;
   final ContentManager agK;

   public AnvilEnchantListener(Plugin var1, ContentManager var2) {
      this.agJ = var1;
      this.agK = var2;
      EventRegistrar.registerIfAbsent(this, var1);
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method1265(EnchantItemEvent var1) {
      Integer var2 = (Integer)var1.getEnchantsToAdd().get(Enchantment.SHARPNESS);
      if (var2 != null) {
         CustomItemDefinition var3 = this.agK.method8149(var1.getItem());
         if (var3 != null && var3.hasCustomDurability()) {
            var1.getItem().addUnsafeEnchantment(Enchantment.SHARPNESS, var2);
            CustomItemDefinition.method8802(var1.getItem(), EquipmentSlotType.MAINHAND);
            var1.getInventory().setItem(0, var1.getItem());
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   private void method1266(InventoryClickEvent var1) {
      if (Settings.get().fixAttackDamageLore) {
         if (var1.getInventory().getType() != InventoryType.ANVIL) {
            return;
         }

         if (!ItemStackUtils.method7217(var1)) {
            return;
         }

         if (var1.getSlot() != 2) {
            return;
         }

         if (!(var1.getInventory() instanceof AnvilInventory var2)) {
            return;
         }

         ItemStack var7 = var2.getItem(2);
         if (var7 == null) {
            return;
         }

         ItemStack var4 = var2.getItem(1);
         if (var4 == null || var4.getType() == Material.AIR) {
            return;
         }

         if (var4.getType() != Material.ENCHANTED_BOOK) {
            return;
         }

         CustomItemDefinition var5 = this.agK.method8149(var7);
         if (var5 != null && !var5.hasCustomDurability()) {
            return;
         }

         EnchantmentStorageMeta var6 = (EnchantmentStorageMeta)var4.getItemMeta();
         if (var6.getStoredEnchants().containsKey(Enchantment.SHARPNESS)) {
            CustomItemDefinition.method8802(var7, EquipmentSlotType.MAINHAND);
         }
      }
   }

   @EventHandler(priority = EventPriority.LOWEST)
   private void method1267(PrepareAnvilEvent var1) {
      AnvilInventory var2 = var1.getInventory();
      ItemStack var3 = var2.getItem(0);
      ItemStack var4 = var2.getItem(1);
      if (var4 != null && var4.getType() != Material.AIR) {
         if (var4.getType() == Material.ENCHANTED_BOOK) {
            CustomItemDefinition var5 = this.agK.method8149(var3);
            if (var5 != null) {
               ItemStack var6 = var1.getResult();
               if (var6 != null) {
                  if (Settings.get().fixAttackDamageLore) {
                     CustomItemDefinition.method8802(var6, EquipmentSlotType.MAINHAND);
                  }

                  var1.setResult(var6);
               }
            }
         }
      }
   }
}
