package dev.lone.itemsadder.util;

import org.jetbrains.annotations.Nullable;

public class ToggleState {
   public boolean pM;
   public boolean pN;
   @Nullable
   public String pO;

   public ToggleState(boolean var1, boolean var2, @Nullable String var3) {
      this.pM = var1;
      this.pN = var2;
      this.pO = var3;
   }
}
