package dev.lone.itemsadder.item;

import java.util.UUID;

class PendingItemAction {
   private final UUID agw;
   private long cY;
   private UUID jD;
   private int field139;
   private int field140;
   private int field141;
   public long agx;
   private boolean agy;

   PendingItemAction(UUID var1, long var2, UUID var4, int var5, int var6, int var7) {
      this.agw = var1;
      this.reschedule(var2, var4, var5, var6, var7);
      this.agx = System.currentTimeMillis();
      this.agy = true;
   }

   public boolean isExpired() {
      boolean var1 = System.currentTimeMillis() - this.agx > 150L || this.agy;
      this.agy = false;
      return var1;
   }

   public void execute() {
      this.agx = System.currentTimeMillis();
   }

   public void reschedule(long var1, UUID var3, int var4, int var5, int var6) {
      this.cY = var1;
      this.jD = var3;
      this.field139 = var4;
      this.field140 = var5;
      this.field141 = var6;
   }

   ItemActionKey getKey() {
      return new ItemActionKey(this.agw, this.cY, this.jD, this.field139, this.field140, this.field141);
   }
}
