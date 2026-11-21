package dev.lone.itemsadder.item.effect;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

class TridentTrackingKey {
   private final TridentRef amv;
   private final int amw;
   private final UUID amx;
   private final long amy;
   private final AtomicBoolean amz;

   private TridentTrackingKey(TridentRef var1, int var2, UUID var3, long var4, AtomicBoolean var6) {
      this.amv = var1;
      this.amw = var2;
      this.amx = var3;
      this.amy = var4;
      this.amz = var6;
   }

   public TridentRef ref() {
      return this.amv;
   }

   public int jI() {
      return this.amw;
   }

   public UUID CN() {
      return this.amx;
   }

   public long ai() {
      return this.amy;
   }

   public AtomicBoolean CP() {
      return this.amz;
   }
}
