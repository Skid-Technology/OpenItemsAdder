package dev.lone.itemsadder.util;

import java.util.UUID;

public class UuidVector3 {
   private final UUID uuid;
   private final double x;
   private final double y;
   private final double z;

   private UuidVector3(UUID var1, double var2, double var4, double var6) {
      this.uuid = var1;
      this.x = var2;
      this.y = var4;
      this.z = var6;
   }

   public UUID getUuid() {
      return this.uuid;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }
}
