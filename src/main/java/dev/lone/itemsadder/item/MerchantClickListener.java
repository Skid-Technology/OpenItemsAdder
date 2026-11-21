package dev.lone.itemsadder.item;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.EventRegistrar;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.Plugin;

public class MerchantClickListener implements Listener {
   public void register(Plugin plugin) {
      EventRegistrar.registerIfAbsent(this, plugin);
   }

   @EventHandler
   private void onMerchantClick(InventoryClickEvent var1) {
      if (var1.getClickedInventory() != null) {
         if (var1.getClickedInventory().getType() == InventoryType.MERCHANT) {
            if (!ContentManager.method8084().bf.isMerchantOpen((Player)var1.getWhoClicked())) {
               MerchantInventory var2 = (MerchantInventory)var1.getClickedInventory();
               MerchantRecipe var3 = var2.getSelectedRecipe();
               if (var3 == null) {
                  if (var2.getMerchant().getRecipeCount() == 0) {
                     return;
                  }

                  var3 = var2.getMerchant().getRecipe(0);
               }

               List var4 = var3.getIngredients();
               if (!var4.isEmpty()) {
                  if (var1.getSlotType() == SlotType.RESULT) {
                     ItemStack var5 = var2.getItem(0);
                     ItemStack var6 = var2.getItem(1);
                     ItemStack var7 = (ItemStack)var4.getFirst();
                     ItemStack var8 = var4.size() > 1 ? (ItemStack)var4.get(1) : null;
                     if ((!this.isMatchingTradeStack(var5, var7) || !this.isMatchingTradeStack(var6, var8))
                        && (!this.isMatchingTradeStack(var6, var7) || !this.isMatchingTradeStack(var5, var8))) {
                        var1.setCancelled(true);
                     }
                  }
               }
            }
         }
      }
   }

   private boolean isMatchingTradeStack(ItemStack var1, ItemStack var2) {
      return ItemStackUtils.aE(var2)
         ? ItemStackUtils.aE(var1)
         : !ItemStackUtils.aE(var1)
            && var1.getType() == var2.getType()
            && (!CustomItemDefinition.isCustomItem(var1) || CustomItemDefinition.isCustomItem(var2));
   }
}
