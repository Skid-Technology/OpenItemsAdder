package dev.lone.itemsadder.util;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

final class StyleCacheEntry {
   private final int code;
   private final TextColor color;
   private final TextDecoration decoration;
   private final boolean reset;

   private StyleCacheEntry(int var1, TextColor var2, TextDecoration var3, boolean var4) {
      this.code = var1;
      this.color = var2;
      this.decoration = var3;
      this.reset = var4;
   }

   private static StyleCacheEntry ofColor(int var0, TextColor var1) {
      return new StyleCacheEntry(var0, var1, null, false);
   }

   private static StyleCacheEntry ofDecoration(int var0, TextDecoration var1) {
      return new StyleCacheEntry(var0, null, var1, false);
   }

   private static StyleCacheEntry ofReset(int var0) {
      return new StyleCacheEntry(var0, null, null, true);
   }

   private void applyTo(StyleBuilderState var1, TextColor var2) {
      if (this.reset) {
         var1.setColorAndResetDup2(var2);
      } else if (this.color != null) {
         var1.setColorAndReset(this.color);
      } else {
         if (this.decoration != null) {
            var1.addDecoration(this.decoration);
         }
      }
   }
}
