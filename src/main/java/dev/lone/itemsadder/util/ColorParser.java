package dev.lone.itemsadder.util;

import java.lang.reflect.Field;
import org.bukkit.Color;
import org.jetbrains.annotations.Nullable;

public final class ColorParser {
   private ColorParser() {
   }

   @Nullable
   public static Integer parse(@Nullable String var0) {
      if (var0 == null) {
         return null;
      }

      String var1 = var0.trim();
      if (var1.isEmpty()) {
         return null;
      }

      Integer var2 = parseRgbTriplet(var1);
      if (var2 != null) {
         return var2;
      }

      var2 = parseBukkitColorName(var1);
      if (var2 != null) {
         return var2;
      }

      var2 = parseHex(var1);
      if (var2 != null) {
         return var2;
      }

      var2 = parseDecimal(var1);
      return var2 != null ? var2 : null;
   }

   @Nullable
   private static Integer parseRgbTriplet(String var0) {
      if (!var0.contains(",")) {
         return null;
      }

      String[] var1 = var0.split(",");
      if (var1.length != 3) {
         return null;
      }

      try {
         int var2 = clampByte(Integer.parseInt(var1[0].trim()));
         int var3 = clampByte(Integer.parseInt(var1[1].trim()));
         int var4 = clampByte(Integer.parseInt(var1[2].trim()));
         return var2 << 16 | var3 << 8 | var4;
      } catch (NumberFormatException var5) {
         return null;
      }
   }

   private static int clampByte(int var0) {
      if (var0 < 0) {
         return 0;
      } else {
         return var0 > 255 ? 255 : var0;
      }
   }

   @Nullable
   private static Integer parseBukkitColorName(String var0) {
      String var1 = var0.trim().replace(' ', '_').replace('-', '_').toUpperCase();

      try {
         Field var2 = Color.class.getField(var1);
         if (var2.get(null) instanceof Color var4) {
            return var4.asRGB() & 16777215;
         }
      } catch (NoSuchFieldException var5) {
      } catch (IllegalAccessException var6) {
      }

      return null;
   }

   @Nullable
   private static Integer parseHex(String var0) {
      String var1 = var0.trim();
      if (var1.startsWith("#")) {
         var1 = var1.substring(1);
      } else if (var1.startsWith("0x") || var1.startsWith("0X")) {
         var1 = var1.substring(2);
      }

      if (!var1.matches("^[0-9a-fA-F]+$")) {
         return null;
      }

      if (var1.length() == 3) {
         char var2 = var1.charAt(0);
         char var3 = var1.charAt(1);
         char var4 = var1.charAt(2);
         var1 = "" + var2 + var2 + var3 + var3 + var4 + var4;
      } else if (var1.length() == 4) {
         char var10 = var1.charAt(0);
         char var12 = var1.charAt(1);
         char var13 = var1.charAt(2);
         char var5 = var1.charAt(3);
         String var6 = "" + var12 + var12;
         String var7 = "" + var13 + var13;
         String var8 = "" + var5 + var5;
         var1 = var6 + var7 + var8;
      } else if (var1.length() == 8) {
         var1 = var1.substring(2);
      } else if (var1.length() != 6) {
         return null;
      }

      try {
         int var11 = (int)Long.parseLong(var1, 16);
         return var11 & 16777215;
      } catch (NumberFormatException var9) {
         return null;
      }
   }

   @Nullable
   private static Integer parseDecimal(String var0) {
      if (!var0.matches("^[+-]?\\d+$")) {
         return null;
      }

      try {
         int var1 = Integer.parseInt(var0);
         return var1 & 16777215;
      } catch (NumberFormatException var2) {
         return null;
      }
   }
}
