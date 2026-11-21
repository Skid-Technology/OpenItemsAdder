package dev.lone.itemsadder.util;

public class DoubleStringPair {
   private final double number;
   private final String text;

   DoubleStringPair(double var1, String var3) {
      this.number = var1;
      this.text = var3;
   }

   public double getNumber() {
      return this.number;
   }

   public String getText() {
      return this.text;
   }
}
