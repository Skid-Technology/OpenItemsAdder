package dev.lone.itemsadder.item;

public enum ItemKind {
   BLOCK("BLOCK"),
   ITEM("ITEM"),
   VANILLA("VANILLA");

   private final String text;

   ItemKind(String var3) {
      this.text = var3;
   }

   @Override
   public String toString() {
      return this.text;
   }
}
