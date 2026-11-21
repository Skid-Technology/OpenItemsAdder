package dev.lone.itemsadder.entity;

import java.util.UUID;

class TimedEntityRef {
   private final UUID cX;
   private final long cY;
   private final float cZ;
   private final long da;

   private TimedEntityRef(UUID var1, long var2, float var4, long var5) {
      this.cX = var1;
      this.cY = var2;
      this.cZ = var4;
      this.da = var5;
   }

   public UUID entityId() {
      return this.cX;
   }

   public long createdAt() {
      return this.cY;
   }

   public float distance() {
      return this.cZ;
   }

   public long expiresAt() {
      return this.da;
   }
}
