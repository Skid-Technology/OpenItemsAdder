package dev.lone.itemsadder.item;

import java.util.UUID;

public class ItemSlotMapping {
   private final UUID amS;
   private final long amT;
   private final int[] amU;
   private final long amV;

   private ItemSlotMapping(UUID var1, long var2, int[] var4, long var5) {
      this.amS = var1;
      this.amT = var2;
      this.amU = var4;
      this.amV = var5;
   }

   public UUID bk() {
      return this.amS;
   }

   public long bl() {
      return this.amT;
   }

   public int[] Da() {
      return this.amU;
   }

   public long ai() {
      return this.amV;
   }
}
