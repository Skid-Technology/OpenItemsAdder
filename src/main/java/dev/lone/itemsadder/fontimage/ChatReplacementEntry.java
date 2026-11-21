package dev.lone.itemsadder.fontimage;

import dev.lone.itemsadder.util.AdventureText;
import net.kyori.adventure.text.Component;

final class ChatReplacementEntry {
   final Component aGU;
   final Component aGV;
   private volatile Object aGW;

   ChatReplacementEntry(Component var1, Component var2) {
      this.aGU = var1;
      this.aGV = var2;
   }

   Object Lo() {
      Object var1 = this.aGW;
      if (var1 == null) {
         this.aGW = var1 = AdventureText.toNative(this.aGU);
      }

      return var1;
   }
}
