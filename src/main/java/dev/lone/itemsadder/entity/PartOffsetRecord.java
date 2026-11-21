package dev.lone.itemsadder.entity;

import java.util.UUID;

class PartOffsetRecord {
   private final UUID xO;
   private final double xP;
   private final double xQ;
   private final double xR;
   private final float xS;
   private final float xT;

   private PartOffsetRecord(UUID var1, double var2, double var4, double var6, float var8, float var9) {
      this.xO = var1;
      this.xP = var2;
      this.xQ = var4;
      this.xR = var6;
      this.xS = var8;
      this.xT = var9;
   }

   public UUID entityId() {
      return this.xO;
   }

   public double x() {
      return this.xP;
   }

   public double y() {
      return this.xQ;
   }

   public double z() {
      return this.xR;
   }

   public float yaw() {
      return this.xS;
   }

   public float pitch() {
      return this.xT;
   }
}
