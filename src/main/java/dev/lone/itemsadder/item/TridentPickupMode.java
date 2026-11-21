package dev.lone.itemsadder.item;

public enum TridentPickupMode {
   PICKING_UP,
   LOYAL_PICKING_UP,
   NONE;

   public boolean CY() {
      return this == PICKING_UP || this == LOYAL_PICKING_UP;
   }
}
