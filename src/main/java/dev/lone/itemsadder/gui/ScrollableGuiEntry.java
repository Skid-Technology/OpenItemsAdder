package dev.lone.itemsadder.gui;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.NamespacedKey;

final class ScrollableGuiEntry {
   private final NamespacedKey bEo;
   private final Set bEp = new HashSet();
   private boolean bEq;

   private ScrollableGuiEntry(NamespacedKey var1) {
      this.bEo = var1;
   }
}
