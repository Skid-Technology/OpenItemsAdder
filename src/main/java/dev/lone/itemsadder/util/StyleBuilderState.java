package dev.lone.itemsadder.util;

import dev.lone.itemsadder.internal.SwitchMap85;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

final class StyleBuilderState {
   private TextColor color;
   private boolean bold;
   private boolean italic;
   private boolean underlined;
   private boolean strikethrough;
   private boolean obfuscated;

   private StyleBuilderState(TextColor var1) {
      this.color = var1;
   }

   private void setColorAndReset(TextColor var1) {
      this.color = var1;
      this.resetDecorations();
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void addDecoration(TextDecoration var1) {
      switch (SwitchMap85.bMA[var1.ordinal()]) {
         case 1:
            this.bold = true;
            break;
         case 2:
            this.italic = true;
            break;
         case 3:
            this.underlined = true;
            break;
         case 4:
            this.strikethrough = true;
            break;
         case 5:
            this.obfuscated = true;
      }
   }

   private void setColorAndResetDup2(TextColor var1) {
      this.color = var1;
      this.resetDecorations();
   }

   private void resetDecorations() {
      this.bold = false;
      this.italic = false;
      this.underlined = false;
      this.strikethrough = false;
      this.obfuscated = false;
   }
}
