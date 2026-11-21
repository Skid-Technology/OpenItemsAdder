package dev.lone.itemsadder.entity;

import org.jetbrains.annotations.Nullable;

class MobGoalConfig {
   private final boolean Zz;
   private final String ZA;
   @Nullable
   private final Integer ZB;
   private final boolean ZC;

   private MobGoalConfig(boolean var1, String var2, @Nullable Integer var3, boolean var4) {
      this.Zz = var1;
      this.ZA = var2;
      this.ZB = var3;
      this.ZC = var4;
   }

   public boolean xY() {
      return this.Zz;
   }

   public String xZ() {
      return this.ZA;
   }

   @Nullable
   public Integer ya() {
      return this.ZB;
   }

   public boolean enabled() {
      return this.ZC;
   }
}
