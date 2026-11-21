package dev.lone.itemsadder.item;

import java.util.UUID;

public class SlotIndexRecord {
   private final UUID anb;
   private final long anc;
   private final int and;
   private final int ane;
   private final long anf;

   private SlotIndexRecord(UUID var1, long var2, int var4, int var5, long var6) {
      this.anb = var1;
      this.anc = var2;
      this.and = var4;
      this.ane = var5;
      this.anf = var6;
   }

   public UUID bk() {
      return this.anb;
   }

   public long bl() {
      return this.anc;
   }

   public int Dc() {
      return this.and;
   }

   public int Dd() {
      return this.ane;
   }

   public long ai() {
      return this.anf;
   }
}
