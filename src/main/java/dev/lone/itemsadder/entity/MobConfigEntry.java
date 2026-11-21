package dev.lone.itemsadder.entity;

import java.util.Set;

class MobConfigEntry {
   private final MobConfigValueType aay;
   private final Object aaz;
   private final boolean aaA;
   private final double aaB;
   private final double aaC;
   private final Set aaD;

   private MobConfigEntry(MobConfigValueType var1, Object var2, boolean var3, double var4, double var6, Set var8) {
      this.aay = var1;
      this.aaz = var2;
      this.aaA = var3;
      this.aaB = var4;
      this.aaC = var6;
      this.aaD = var8;
   }

   public MobConfigValueType type() {
      return this.aay;
   }

   public Object value() {
      return this.aaz;
   }

   public boolean required() {
      return this.aaA;
   }

   public double min() {
      return this.aaB;
   }

   public double max() {
      return this.aaC;
   }

   public Set options() {
      return this.aaD;
   }
}
