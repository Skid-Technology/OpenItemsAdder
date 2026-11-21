package dev.lone.itemsadder.item;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class ItemMaterialAction extends ItemActionBase {
   public static final String aet = "liquid_bucket";
   ColoredNamespacedId aeu;

   public ItemMaterialAction(CustomItemDefinition var1, ColoredNamespacedId var2) {
      super(var1);
      this.aeu = var2;
   }

   public ColoredNamespacedId AO() {
      return this.aeu;
   }

   public static boolean method380(Block var0) {
      return method381(var0.getType());
   }

   public static boolean method381(Material var0) {
      return var0 == Material.WATER;
   }
}
