package dev.lone.itemsadder.item;

import java.util.UUID;

class ItemActionKey {
   private final UUID agz;
   private final long agA;
   private final UUID agB;
   private final int agC;
   private final int agD;
   private final int agE;

   private ItemActionKey(UUID var1, long var2, UUID var4, int var5, int var6, int var7) {
      this.agz = var1;
      this.agA = var2;
      this.agB = var4;
      this.agC = var5;
      this.agD = var6;
      this.agE = var7;
   }

   public UUID bk() {
      return this.agz;
   }

   public long bl() {
      return this.agA;
   }

   public UUID method20() {
      return this.agB;
   }

   public int method1() {
      return this.agC;
   }

   public int method2() {
      return this.agD;
   }

   public int method3() {
      return this.agE;
   }
}
