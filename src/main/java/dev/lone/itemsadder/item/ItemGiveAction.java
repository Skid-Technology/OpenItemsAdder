package dev.lone.itemsadder.item;

import org.bukkit.inventory.ItemStack;

public class ItemGiveAction extends ItemActionBase {
   public static final String aeo = "gun";
   public final ItemStack aep;

   public ItemGiveAction(CustomItemDefinition var1, ItemStack var2) {
      super(var1);
      this.aep = var2;
   }
}
