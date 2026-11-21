package dev.lone.itemsadder.event;

import java.util.UUID;

public class PlayerTriggerKey {
   private final UUID bkI;
   private final long bkJ;
   private final float bkK;
   private final float bkL;
   private final long bkM;

   public PlayerTriggerKey(UUID var1, long var2, float var4, float var5, long var6) {
      this.bkI = var1;
      this.bkJ = var2;
      this.bkK = var4;
      this.bkL = var5;
      this.bkM = var6;
   }

   public UUID bk() {
      return this.bkI;
   }

   public long bl() {
      return this.bkJ;
   }

   public float bm() {
      return this.bkK;
   }

   public float method66() {
      return this.bkL;
   }

   public long ai() {
      return this.bkM;
   }
}
