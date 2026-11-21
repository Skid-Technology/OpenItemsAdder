package dev.lone.itemsadder.util;

import org.jetbrains.annotations.Nullable;

public class IntBoolPair {
   @Nullable
   public Integer key;
   @Nullable
   public Boolean flag;

   public IntBoolPair(@Nullable Integer var1, @Nullable Boolean var2) {
      this.key = var1;
      this.flag = var2;
   }

   public boolean hasValue() {
      return this.key != null || this.flag != null;
   }
}
