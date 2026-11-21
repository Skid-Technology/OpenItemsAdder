package dev.lone.itemsadder.block;

import java.util.UUID;

class PlayerTimedAction {
   private final UUID nS;
   private final long nT;
   private final long nU;

   private PlayerTimedAction(UUID var1, long var2, long var4) {
      this.nS = var1;
      this.nT = var2;
      this.nU = var4;
   }

   public UUID playerId() {
      return this.nS;
   }

   public long connectionId() {
      return this.nT;
   }

   public long callbackGeneration() {
      return this.nU;
   }
}
