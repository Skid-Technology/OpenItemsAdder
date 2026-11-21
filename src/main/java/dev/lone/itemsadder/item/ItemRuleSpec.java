package dev.lone.itemsadder.item;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;

public class ItemRuleSpec {
   private final NamespacedKey aqp;
   private final String aqq;
   @Nullable
   private final String aqr;
   private final int aqs;
   private final ToggleSpec aqt;
   private final double aqu;
   private final double aqv;

   public ItemRuleSpec(NamespacedKey var1, String var2, @Nullable String var3, int var4, ToggleSpec var5, double var6, double var8) {
      if (var2 == null || var2.isBlank()) {
         throw new IllegalArgumentException("'elytra.wings_texture' is required.");
      }

      if (var4 < 0) {
         throw new IllegalArgumentException("'elytra.max_flight_ticks' must be non-negative.");
      }

      method77("elytra.durability_damage_multiplier", var6);
      method77("elytra.collision_damage_multiplier", var8);
      this.aqp = var1;
      this.aqq = var2;
      this.aqr = var3;
      this.aqs = var4;
      this.aqt = var5;
      this.aqu = var6;
      this.aqv = var8;
   }

   public boolean bB(int var1) {
      return this.aqs > 0 && var1 >= this.aqs;
   }

   public int bC(int var1) {
      return Math.max(0, (int)Math.round(var1 * this.aqu));
   }

   public double method76(double var1) {
      return var1 * this.aqv;
   }

   private static void method77(String var0, double var1) {
      if (!Double.isFinite(var1) || var1 < 0.0) {
         throw new IllegalArgumentException("'" + var0 + "' must be a non-negative finite number.");
      }
   }

   public NamespacedKey EO() {
      return this.aqp;
   }

   public String EP() {
      return this.aqq;
   }

   @Nullable
   public String EQ() {
      return this.aqr;
   }

   public int ER() {
      return this.aqs;
   }

   public ToggleSpec ES() {
      return this.aqt;
   }

   public double ET() {
      return this.aqu;
   }

   public double EU() {
      return this.aqv;
   }
}
