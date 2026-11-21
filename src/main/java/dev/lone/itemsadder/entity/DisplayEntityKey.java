package dev.lone.itemsadder.entity;

import java.util.UUID;

public class DisplayEntityKey {
   private final int blm;
   private final UUID bln;

   public DisplayEntityKey(int var1, UUID var2) {
      this.blm = var1;
      this.bln = var2;
   }

   public int entityId() {
      return this.blm;
   }

   public UUID worldId() {
      return this.bln;
   }
}
