package dev.lone.itemsadder.entity;

import org.bukkit.SoundCategory;

public class SoundEffectSpec {
   private final String zP;
   private final SoundCategory zQ;
   private final float zR;
   private final float zS;
   private final float zT;

   public SoundEffectSpec(String var1, SoundCategory var2, float var3, float var4, float var5) {
      this.zP = var1;
      this.zQ = var2;
      this.zR = var3;
      this.zS = var4;
      this.zT = var5;
   }

   public String sound() {
      return this.zP;
   }

   public SoundCategory category() {
      return this.zQ;
   }

   public float volume() {
      return this.zR;
   }

   public float method66() {
      return this.zS;
   }

   public float pitch() {
      return this.zT;
   }
}
