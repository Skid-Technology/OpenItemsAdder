package dev.lone.itemsadder.item;

import java.util.UUID;

class ThrowAimState {
   private final UUID afI;
   private final ThrowTaskRef afJ;
   private final boolean afK;
   private final boolean afL;
   private final boolean afM;
   private final boolean afN;

   private ThrowAimState(UUID var1, ThrowTaskRef var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      this.afI = var1;
      this.afJ = var2;
      this.afK = var3;
      this.afL = var4;
      this.afM = var5;
      this.afN = var6;
   }

   public UUID playerId() {
      return this.afI;
   }

   public ThrowTaskRef taskRef() {
      return this.afJ;
   }

   public boolean Bi() {
      return this.afK;
   }

   public boolean Bj() {
      return this.afL;
   }

   public boolean kL() {
      return this.afM;
   }

   public boolean kM() {
      return this.afN;
   }
}
