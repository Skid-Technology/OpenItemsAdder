package dev.lone.itemsadder.item;

import java.util.UUID;

public class BlockPlacementKey {
   private final UUID ahN;
   private final long ahO;
   private final int ahP;
   private final int ahQ;
   private final int ahR;

   private BlockPlacementKey(UUID var1, long var2, int var4, int var5, int var6) {
      this.ahN = var1;
      this.ahO = var2;
      this.ahP = var4;
      this.ahQ = var5;
      this.ahR = var6;
   }

   public UUID bk() {
      return this.ahN;
   }

   public long bl() {
      return this.ahO;
   }

   public int method1() {
      return this.ahP;
   }

   public int method2() {
      return this.ahQ;
   }

   public int method3() {
      return this.ahR;
   }
}
