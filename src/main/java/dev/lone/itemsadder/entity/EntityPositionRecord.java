package dev.lone.itemsadder.entity;

import java.util.UUID;

public class EntityPositionRecord {
   private final UUID amn;
   private final long amo;
   private final int amp;
   private final UUID amq;
   private final double amr;
   private final double ams;
   private final double amt;
   private final long amu;

   private EntityPositionRecord(UUID var1, long var2, int var4, UUID var5, double var6, double var8, double var10, long var12) {
      this.amn = var1;
      this.amo = var2;
      this.amp = var4;
      this.amq = var5;
      this.amr = var6;
      this.ams = var8;
      this.amt = var10;
      this.amu = var12;
   }

   public UUID entityId() {
      return this.amn;
   }

   public long timestamp() {
      return this.amo;
   }

   public int index() {
      return this.amp;
   }

   public UUID worldId() {
      return this.amq;
   }

   public double x() {
      return this.amr;
   }

   public double y() {
      return this.ams;
   }

   public double z() {
      return this.amt;
   }

   public long sequence() {
      return this.amu;
   }
}
