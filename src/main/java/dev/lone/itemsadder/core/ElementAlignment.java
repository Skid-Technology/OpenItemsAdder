package dev.lone.itemsadder.core;

public enum ElementAlignment {
   RIGHT("RIGHT"),
   LEFT("LEFT");

   private final String text;

   ElementAlignment(String var3) {
      this.text = var3;
   }

   @Override
   public String toString() {
      return this.text;
   }
}
