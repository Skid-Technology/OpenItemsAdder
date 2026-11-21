package dev.lone.itemsadder.physics;

import java.util.UUID;

public class EntityBoundsSnapshot {
   private final UUID IX;
   private final double IY;
   private final double IZ;
   private final double Ja;
   private final double Jb;
   private final double Jc;
   private final double Jd;

   public EntityBoundsSnapshot(UUID var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      this.IX = var1;
      this.IY = var2;
      this.IZ = var4;
      this.Ja = var6;
      this.Jb = var8;
      this.Jc = var10;
      this.Jd = var12;
   }

   public UUID method20() {
      return this.IX;
   }

   public double ka() {
      return this.IY;
   }

   public double kb() {
      return this.IZ;
   }

   public double kc() {
      return this.Ja;
   }

   public double su() {
      return this.Jb;
   }

   public double sv() {
      return this.Jc;
   }

   public double sw() {
      return this.Jd;
   }
}
