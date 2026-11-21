package dev.lone.itemsadder.gui;

import java.util.Objects;
import net.kyori.adventure.text.Component;

class GuiTitleLayout {
   private final Component aLR;
   private final TitleFontSpec aLS;
   private final int aLT;
   private final int aLU;

   private GuiTitleLayout(Component var1, TitleFontSpec var2, int var3, int var4) {
      Objects.requireNonNull(var1, "title");
      method63(var3, "titleOffset");
      method63(var4, "backgroundOffset");
      if (var2 != null) {
         long var5 = -var2.aLX + var3;
         if (var5 < -2147483648L || var5 > 2147483647L) {
            throw new IllegalArgumentException("title compensation is outside the integer range");
         }

         method63((int)var5, "title compensation");
      }

      this.aLR = var1;
      this.aLS = var2;
      this.aLT = var3;
      this.aLU = var4;
   }

   private static void method63(int var0, String var1) {
      if (var0 < -2048 || var0 > 2048) {
         throw new IllegalArgumentException(var1 + " must be between -2048 and 2048");
      }
   }

   public Component MH() {
      return this.aLR;
   }

   public TitleFontSpec MI() {
      return this.aLS;
   }

   public int MJ() {
      return this.aLT;
   }

   public int MK() {
      return this.aLU;
   }
}
