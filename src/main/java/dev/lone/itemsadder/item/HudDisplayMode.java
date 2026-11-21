package dev.lone.itemsadder.item;

public enum HudDisplayMode {
   HIDDEN,
   VANILLA,
   TITLE,
   HUD,
   BOSSBAR;

   public boolean isVisible() {
      return this != HIDDEN && this != VANILLA;
   }
}
