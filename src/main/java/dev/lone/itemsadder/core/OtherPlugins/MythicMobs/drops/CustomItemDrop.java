package dev.lone.itemsadder.Core.OtherPlugins.MythicMobs.drops;

import dev.lone.itemsadder.item.CustomItemDefinition;
import io.lumine.mythic.api.adapters.AbstractItemStack;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.drops.DropMetadata;
import io.lumine.mythic.api.drops.IItemDrop;
import io.lumine.mythic.bukkit.adapters.item.ItemComponentBukkitItemStack;
import org.bukkit.inventory.ItemStack;

public class CustomItemDrop implements IItemDrop {
   private final CustomItemDefinition customItem;

   public CustomItemDrop(MythicLineConfig config, String argument, CustomItemDefinition customItem) {
      this.customItem = customItem;
   }

   public AbstractItemStack getDrop(DropMetadata data, double amount) {
      int var4 = (int)amount;
      if (var4 > 0) {
         ItemStack var5 = this.customItem.Ds();
         var5.setAmount(var4);
         return new ItemComponentBukkitItemStack(var5);
      } else {
         return null;
      }
   }
}
