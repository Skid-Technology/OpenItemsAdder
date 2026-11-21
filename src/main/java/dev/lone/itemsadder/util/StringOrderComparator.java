package dev.lone.itemsadder.util;

import java.util.Comparator;
import java.util.List;

public class StringOrderComparator {
   static String NON_NUMERIC_REGEX = "[^\\d.]";

   public static int toNumber(String var0) {
      var0 = var0.replaceAll(NON_NUMERIC_REGEX, "");
      if (var0.isEmpty()) {
         return 0;
      }

      try {
         return Integer.parseInt(var0);
      } catch (NumberFormatException var2) {
         return 0;
      }
   }

   public static Comparator numericComparator() {
      return Comparator.comparingDouble(StringOrderComparator::toNumber);
   }

   public static List sortNumeric(List var0) {
      var0.sort(numericComparator());
      return var0;
   }
}
