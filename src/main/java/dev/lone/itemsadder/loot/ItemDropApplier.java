package dev.lone.itemsadder.loot;

import dev.lone.itemsadder.item.ItemVariantHolder;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class ItemDropApplier extends LootDropRules {
   public void method2017(Item var1, Player var2) {
      for (ItemVariantHolder var4 : this.Hn()) {
         if (var4.gC()) {
            var1.setItemStack(var4.method10017(var2.getItemInHand(), true));
            if (this.atH) {
               return;
            }
         }
      }
   }
}
