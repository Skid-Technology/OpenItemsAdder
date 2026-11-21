package dev.lone.itemsadder.entity;

import java.util.UUID;

final class EntityPositionSnapshot {
   UUID jD;
   double pW;
   double pX;
   double pY;
   long pZ;

   EntityPositionSnapshot(UUID var1, double var2, double var4, double var6, long var8) {
      this.jD = var1;
      this.pW = var2;
      this.pX = var4;
      this.pY = var6;
      this.pZ = var8;
   }
}
