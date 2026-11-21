package dev.lone.itemsadder.text;

import java.util.List;

public final class LoreLines {
   private LoreLines() {
   }

   static String[] method2723(List var0) {
      String[] var1 = new String[4];

      for (int var2 = 0; var2 < var1.length; var2++) {
         var1[var2] = var2 < var0.size() ? (String)var0.get(var2) : "";
      }

      return var1;
   }

   static String method2724(String[] var0) {
      return var0.length == 0 ? "" : var0[0].replaceAll("(?i)§[0-9A-FK-ORX]", "").trim();
   }

   static String method2725(String var0, String var1) {
      return var0 + var1.replaceAll("(?i)§(?:[0-9a-f]|x(?:§[0-9a-f]){6})", "");
   }
}
