package dev.lone.itemsadder.script;

@Deprecated
enum ScriptTargetType {
   ITEM,
   CUSTOM_ENTITY,
   BOTH,
   UNKNOWN;

   public static ScriptTargetType eg(String var0) {
      boolean var1 = var0.contains("$customStack") || var0.contains("$itemStack");
      boolean var2 = var0.contains("$customEntity");
      if (var1 && var2) {
         return BOTH;
      } else if (var1) {
         return ITEM;
      } else {
         return var2 ? CUSTOM_ENTITY : UNKNOWN;
      }
   }
}
