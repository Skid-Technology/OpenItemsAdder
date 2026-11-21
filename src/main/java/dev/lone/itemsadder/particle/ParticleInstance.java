package dev.lone.itemsadder.particle;

import dev.lone.itemsadder.entity.DisplayEntityKey;
import org.joml.Quaternionf;

class ParticleInstance {
   private final DisplayEntityKey azy;
   private final ParticleState azz;
   private final Quaternionf azA;

   private ParticleInstance(DisplayEntityKey var1, ParticleState var2, Quaternionf var3) {
      this.azy = var1;
      this.azz = var2;
      this.azA = var3;
   }

   public DisplayEntityKey Jv() {
      return this.azy;
   }

   public ParticleState Jw() {
      return this.azz;
   }

   public Quaternionf Jx() {
      return this.azA;
   }
}
