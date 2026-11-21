package dev.lone.itemsadder.item.effect;

public enum ClickMode {
   RELATIVE,
   CLICKED;

   public static ClickMode parse(String var0) {
      switch (var0) {
         case "RELATIVE":
            return RELATIVE;
         case "CLICKED":
            return CLICKED;
         default:
            return CLICKED;
      }
   }
}
