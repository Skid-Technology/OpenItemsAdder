package dev.lone.itemsadder.item;

import org.jetbrains.annotations.Nullable;

public class ItemActionBase implements Cloneable {
   public CustomItemDefinition aeT;

   public ItemActionBase(CustomItemDefinition var1) {
      this.aeT = var1;
   }

   @Nullable
   public ItemActionBase copyForItem(CustomItemDefinition var1) {
      ItemActionBase var2 = null;

      try {
         var2 = (ItemActionBase)super.clone();
         var2.aeT = var1;
      } catch (CloneNotSupportedException var4) {
         var4.printStackTrace();
      }

      return var2;
   }
}
