package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.particle.ParticleEmitterBase;
import org.jetbrains.annotations.Nullable;

public class TriggerRecord {
   private final double zW;
   @Nullable
   private final ParticleEmitterBase zX;
   @Nullable
   private final String zY;

   public TriggerRecord(double var1, @Nullable ParticleEmitterBase var3, @Nullable String var4) {
      this.zW = var1;
      this.zX = var3;
      this.zY = var4;
   }

   public double time() {
      return this.zW;
   }

   @Nullable
   public ParticleEmitterBase emitter() {
      return this.zX;
   }

   @Nullable
   public String tag() {
      return this.zY;
   }
}
