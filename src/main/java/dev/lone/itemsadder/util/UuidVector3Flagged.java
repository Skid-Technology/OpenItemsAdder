package dev.lone.itemsadder.util;

import java.util.UUID;

public class UuidVector3Flagged {
   private final boolean flagged;
   private final UUID uuid;
   private final double x;
   private final double y;
   private final double z;

   private UuidVector3Flagged(boolean var1, UUID var2, double var3, double var5, double var7) {
      this.flagged = var1;
      this.uuid = var2;
      this.x = var3;
      this.y = var5;
      this.z = var7;
   }

   public boolean isFlagged() {
      return this.flagged;
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
