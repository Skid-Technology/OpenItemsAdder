package dev.lone.itemsadder.gui;

import org.bukkit.NamespacedKey;

class ScrollableGuiKeySet {
   private final NamespacedKey bEl;
   private final NamespacedKey bEm;
   private final NamespacedKey bEn;

   private ScrollableGuiKeySet(NamespacedKey var1, NamespacedKey var2, NamespacedKey var3) {
      this.bEl = var1;
      this.bEm = var2;
      this.bEn = var3;
   }

   public NamespacedKey XT() {
      return this.bEl;
   }

   public NamespacedKey XU() {
      return this.bEm;
   }

   public NamespacedKey XV() {
      return this.bEn;
   }
}
