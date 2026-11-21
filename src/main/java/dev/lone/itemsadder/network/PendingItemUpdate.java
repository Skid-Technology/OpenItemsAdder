package dev.lone.itemsadder.network;

import java.util.UUID;
import org.bukkit.inventory.ItemStack;

class PendingItemUpdate {
   private final UUID aHJ;
   private final long aHK;
   private final long aHL;
   private final ItemStack aHM;
   private final int aHN;

   private PendingItemUpdate(UUID var1, long var2, long var4, ItemStack var6, int var7) {
      this.aHJ = var1;
      this.aHK = var2;
      this.aHL = var4;
      this.aHM = var6;
      this.aHN = var7;
   }

   public UUID bk() {
      return this.aHJ;
   }

   public long bl() {
      return this.aHK;
   }

   public long ai() {
      return this.aHL;
   }

   public ItemStack dp() {
      return this.aHM;
   }

   public int Lw() {
      return this.aHN;
   }
}
