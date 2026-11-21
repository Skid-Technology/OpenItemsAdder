package dev.lone.itemsadder.item;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EventRegistrar;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class PrepareEnchantListener implements IaListener {
   final ContentManager agH;

   public PrepareEnchantListener(Plugin var1, ContentManager var2) {
      this.agH = var2;
      EventRegistrar.registerIfAbsent(this, var1);
   }

   @EventHandler(priority = EventPriority.LOWEST)
   private void onPrepareEnchant(PrepareItemEnchantEvent var1) {
      CustomItemDefinition var2 = this.agH.method8149(var1.getItem());
      if (var2 != null) {
         if (var2.isBlockAllEnchants()) {
            var1.setCancelled(true);
         } else {
            ArrayList var3 = var2.Dw();
            if (var3 != null) {
               ArrayList var4 = new ArrayList();

               for (EnchantmentOffer var8 : var1.getOffers()) {
                  if (var8 != null) {
                     if (!var3.contains(var8.getEnchantment())) {
                        var4.add(var8);
                     } else if (var4.size() != 0) {
                        var8.setEnchantment(((EnchantmentOffer)var4.get(0)).getEnchantment());
                        var8.setEnchantmentLevel(((EnchantmentOffer)var4.get(0)).getEnchantmentLevel());
                        var8.setCost(((EnchantmentOffer)var4.get(0)).getCost());
                     }
                  }
               }

               if (var4.size() == 0) {
                  var1.setCancelled(true);
               }
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.LOWEST)
   private void onEnchantItem(EnchantItemEvent var1) {
      CustomItemDefinition var2 = this.agH.method8149(var1.getItem());
      if (var2 != null) {
         ArrayList var3 = var2.Dw();
         if (var3 != null) {
            for (Enchantment var5 : var3) {
               var1.getEnchantsToAdd().remove(var5);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.LOWEST)
   private void onPrepareAnvil(PrepareAnvilEvent var1) {
      AnvilInventory var2 = var1.getInventory();
      ItemStack var3 = var1.getResult();
      if (var3 != null && var3.getType() != Material.AIR) {
         ItemStack var4 = var2.getItem(1);
         if (var4 != null && var4.getType() != Material.AIR) {
            ItemStack var5 = var2.getItem(0);
            if (var5 != null) {
               CustomItemDefinition var6 = this.agH.method8149(var5);
               if (var6 != null) {
                  ArrayList var7 = var6.Dw();
                  if (var7 != null) {
                     if (var4.getType() == Material.ENCHANTED_BOOK) {
                        if (var6.isBlockAllEnchants()) {
                           onPrepareAnvilDup2(var1);
                           return;
                        }

                        EnchantmentStorageMeta var8 = (EnchantmentStorageMeta)var4.getItemMeta();

                        for (Enchantment var10 : var7) {
                           if (var8.hasStoredEnchant(var10)) {
                              onPrepareAnvilDup2(var1);
                              return;
                           }
                        }
                     }

                     ItemMeta var13 = var5.getItemMeta();
                     ItemMeta var14 = var4.getItemMeta();
                     ItemMeta var15 = var3.getItemMeta();

                     for (Enchantment var12 : var7) {
                        if (var13.hasEnchant(var12)) {
                           var15.addEnchant(var12, var13.getEnchantLevel(var12), true);
                        } else if (var14.hasEnchant(var12)) {
                           var15.addEnchant(var12, var14.getEnchantLevel(var12), true);
                        } else {
                           var15.removeEnchant(var12);
                        }
                     }

                     var3.setItemMeta(var15);
                  }
               }
            }
         }
      }
   }

   private static void onPrepareAnvilDup2(PrepareAnvilEvent var0) {
      var0.setResult(null);
   }
}
