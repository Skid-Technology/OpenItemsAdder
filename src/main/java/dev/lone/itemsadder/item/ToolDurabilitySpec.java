package dev.lone.itemsadder.item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ToolDurabilitySpec {
   @NotNull
   public String RU;
   @Nullable
   public Integer apN;
   @Nullable
   public Integer qx;
   @Nullable
   public Integer qy;
   @Nullable
   public Boolean apO;

   public ToolDurabilitySpec(@NotNull String var1, @Nullable Integer var2, @Nullable Integer var3, @Nullable Integer var4, @Nullable Boolean var5) {
      this.RU = var1;
      this.apN = var2;
      this.qx = var3;
      this.qy = var4;
      this.apO = var5;
   }
}
