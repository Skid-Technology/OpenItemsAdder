package dev.lone.itemsadder.item;

import org.bukkit.potion.PotionEffectType;

public class PotionEffectSpec {
   private final PotionEffectType aYS;
   private final int aYT;
   private final int aYU;
   private final boolean aYV;
   private final boolean aYW;
   private final boolean aYX;

   public PotionEffectSpec(PotionEffectType var1, int var2, int var3, boolean var4, boolean var5, boolean var6) {
      this.aYS = var1;
      this.aYT = var2;
      this.aYU = var3;
      this.aYV = var4;
      this.aYW = var5;
      this.aYX = var6;
   }

   public PotionEffectType type() {
      return this.aYS;
   }

   public int duration() {
      return this.aYT;
   }

   public int amplifier() {
      return this.aYU;
   }

   public boolean ambient() {
      return this.aYV;
   }

   public boolean showParticles() {
      return this.aYW;
   }

   public boolean showIcon() {
      return this.aYX;
   }
}
