package dev.lone.itemsadder.item;

import java.util.HashSet;
import org.bukkit.block.Block;

public class ItemBlockAction extends ItemActionBase {
   public static final String adD = "fuel";
   public short adE;
   public HashSet adF;

   public ItemBlockAction(CustomItemDefinition var1, short var2) {
      super(var1);
      this.adE = var2;
      this.adF = new HashSet();
   }

   public boolean method335(Block var1) {
      return this.adF.isEmpty() ? true : this.adF.contains(var1.getType());
   }
}
