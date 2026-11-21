package dev.lone.itemsadder.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;

public final class LegacyColorParser {
   public static Component parseLegacy(String var0) {
      return parseLegacyFull(var0, null, true);
   }

   public static Component parseLegacyWithColor(String var0, TextColor var1) {
      return parseLegacyFull(var0, var1, true);
   }

   public static Component parseLegacyFull(String var0, TextColor var1, boolean var2) {
      if (var0 != null && !var0.isEmpty()) {
         Object var3 = Component.empty();
         StringBuilder var4 = new StringBuilder();
         StyleBuilderState var5 = new StyleBuilderState(var1);

         for (int var6 = 0; var6 < var0.length(); var6++) {
            char var7 = var0.charAt(var6);
            StyleCacheEntry var8 = readStyleCode(var0, var6);
            if (var8 != null) {
               var3 = flushBuffer((Component)var3, var4, var5);
               var8.applyTo(var5, var1);
               var6 += var8.code - 1;
            } else if (var2 && var7 == '#' && isHexDigitsAt(var0, var6 + 1, 6)) {
               var3 = flushBuffer((Component)var3, var4, var5);
               var5.setColorAndReset(parseHexColor(var0.substring(var6, var6 + 7)));
               var6 += 6;
            } else {
               var4.append(var7);
            }
         }

         return var3.children().isEmpty() && var4.length() == 0 ? var3.color(var5.color) : flushBuffer((Component)var3, var4, var5);
      } else {
         return Component.empty();
      }
   }

   private static Component flushBuffer(Component var0, StringBuilder var1, StyleBuilderState var2) {
      if (var1.length() == 0) {
         return var0;
      }

      Object var3 = Component.text(var1.toString());
      if (var2.color != null) {
         var3 = var3.color(var2.color);
      }

      Component var4 = var3.decoration(TextDecoration.BOLD, toDecorationState(var2.bold));
      var4 = var4.decoration(TextDecoration.ITALIC, toDecorationState(var2.italic));
      var4 = var4.decoration(TextDecoration.UNDERLINED, toDecorationState(var2.underlined));
      var4 = var4.decoration(TextDecoration.STRIKETHROUGH, toDecorationState(var2.strikethrough));
      var4 = var4.decoration(TextDecoration.OBFUSCATED, toDecorationState(var2.obfuscated));
      var1.setLength(0);
      return var0.append(var4);
   }

   private static StyleCacheEntry readStyleCode(String var0, int var1) {
      if (var1 + 1 >= var0.length()) {
         return null;
      }

      char var2 = var0.charAt(var1);
      if (!isStyleChar(var2)) {
         return null;
      }

      char var3 = Character.toLowerCase(var0.charAt(var1 + 1));
      if (var3 == '#') {
         if (var1 + 7 < var0.length() && isHexDigitsAt(var0, var1 + 2, 6)) {
            String var7 = "#" + var0.substring(var1 + 2, var1 + 8);
            return StyleCacheEntry.ofColor(8, parseHexColor(var7));
         } else {
            return null;
         }
      } else if (var3 == 'x') {
         TextColor var6 = readColorAt(var0, var1);
         return var6 != null ? StyleCacheEntry.ofColor(14, var6) : null;
      } else {
         TextColor var4 = colorForCode(var3);
         if (var4 != null) {
            return StyleCacheEntry.ofColor(2, var4);
         } else {
            TextDecoration var5 = decorationForCode(var3);
            if (var5 != null) {
               return StyleCacheEntry.ofDecoration(2, var5);
            } else {
               return var3 == 'r' ? StyleCacheEntry.ofReset(2) : null;
            }
         }
      }
   }

   private static TextColor readColorAt(String var0, int var1) {
      if (var1 + 13 >= var0.length()) {
         return null;
      }

      StringBuilder var2 = new StringBuilder("#");

      for (int var3 = 0; var3 < 6; var3++) {
         int var4 = var1 + 2 + var3 * 2;
         int var5 = var4 + 1;
         if (!isStyleChar(var0.charAt(var4))) {
            return null;
         }

         char var6 = var0.charAt(var5);
         if (!isHexDigit(var6)) {
            return null;
         }

         var2.append(var6);
      }

      return parseHexColor(var2.toString());
   }

   private static boolean isStyleChar(char var0) {
      return var0 == '&' || var0 == 167;
   }

   private static boolean isHexDigitsAt(String var0, int var1, int var2) {
      if (var1 + var2 > var0.length()) {
         return false;
      }

      for (int var3 = var1; var3 < var1 + var2; var3++) {
         if (!isHexDigit(var0.charAt(var3))) {
            return false;
         }
      }

      return true;
   }

   private static boolean isHexDigit(char var0) {
      return var0 >= '0' && var0 <= '9' || var0 >= 'a' && var0 <= 'f' || var0 >= 'A' && var0 <= 'F';
   }

   private static TextColor parseHexColor(String var0) {
      return TextColor.color(Integer.parseInt(var0.substring(1), 16));
   }

   private static TextColor colorForCode(char var0) {
      switch (var0) {
         case '0':
            return NamedTextColor.BLACK;
         case '1':
            return NamedTextColor.DARK_BLUE;
         case '2':
            return NamedTextColor.DARK_GREEN;
         case '3':
            return NamedTextColor.DARK_AQUA;
         case '4':
            return NamedTextColor.DARK_RED;
         case '5':
            return NamedTextColor.DARK_PURPLE;
         case '6':
            return NamedTextColor.GOLD;
         case '7':
            return NamedTextColor.GRAY;
         case '8':
            return NamedTextColor.DARK_GRAY;
         case '9':
            return NamedTextColor.BLUE;
         case ':':
         case ';':
         case '<':
         case '=':
         case '>':
         case '?':
         case '@':
         case 'A':
         case 'B':
         case 'C':
         case 'D':
         case 'E':
         case 'F':
         case 'G':
         case 'H':
         case 'I':
         case 'J':
         case 'K':
         case 'L':
         case 'M':
         case 'N':
         case 'O':
         case 'P':
         case 'Q':
         case 'R':
         case 'S':
         case 'T':
         case 'U':
         case 'V':
         case 'W':
         case 'X':
         case 'Y':
         case 'Z':
         case '[':
         case '\\':
         case ']':
         case '^':
         case '_':
         case '`':
         default:
            return null;
         case 'a':
            return NamedTextColor.GREEN;
         case 'b':
            return NamedTextColor.AQUA;
         case 'c':
            return NamedTextColor.RED;
         case 'd':
            return NamedTextColor.LIGHT_PURPLE;
         case 'e':
            return NamedTextColor.YELLOW;
         case 'f':
            return NamedTextColor.WHITE;
      }
   }

   private static TextDecoration decorationForCode(char var0) {
      switch (var0) {
         case 'k':
            return TextDecoration.OBFUSCATED;
         case 'l':
            return TextDecoration.BOLD;
         case 'm':
            return TextDecoration.STRIKETHROUGH;
         case 'n':
            return TextDecoration.UNDERLINED;
         case 'o':
            return TextDecoration.ITALIC;
         default:
            return null;
      }
   }

   private static State toDecorationState(boolean var0) {
      return var0 ? State.TRUE : State.FALSE;
   }
}
