package dev.lone.itemsadder.gui;

import net.kyori.adventure.text.format.TextColor;

class TitleFontSpec {
   private final String aLV;
   private final TextColor aLW;
   private final int aLX;

   private TitleFontSpec(String var1, TextColor var2, int var3) {
      this.aLV = var1;
      this.aLW = var2;
      this.aLX = var3;
   }

   public String ML() {
      return this.aLV;
   }

   public TextColor method64() {
      return this.aLW;
   }

   public int method4() {
      return this.aLX;
   }
}
