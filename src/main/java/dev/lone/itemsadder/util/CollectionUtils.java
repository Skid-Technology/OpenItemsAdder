package dev.lone.itemsadder.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;

public class CollectionUtils {
   public static void incrementCount(HashMap var0, Object var1) {
      var0.merge(var1, 1, Integer::sum);
   }

   public static Object computeNested(HashMap var0, Object var1, Object var2, Function var3) {
      return ((HashMap)var0.computeIfAbsent(var1, var0x -> new HashMap())).computeIfAbsent(var2, var3);
   }

   public static String[] stringArrayOf(String... var0) {
      return var0;
   }

   @SafeVarargs
   public static List asFixedList(Object... var0) {
      return Arrays.asList(var0);
   }

   @SafeVarargs
   public static List newArrayList(Object... var0) {
      return new ArrayList<>(Arrays.asList(var0));
   }

   public static List newListWithTrueCapacity(boolean... var0) {
      return new ArrayList(countTrue(var0));
   }

   @SafeVarargs
   public static List nonNullListOf(boolean var0, Object... var1) {
      ArrayList var2 = new ArrayList();

      for (Object var6 : var1) {
         if (var6 != null) {
            var2.add(var6);
         }
      }

      return var2;
   }

   public static List addOrCreate(List var0, Object var1) {
      if (var0 == null) {
         var0 = new ArrayList();
      }

      var0.add(var1);
      return var0;
   }

   public static boolean listContains(List var0, Object var1) {
      return var0 == null ? false : var0.contains(var1);
   }

   @Nullable
   public static Object findKeyByValue(Map var0, Object var1) {
      for (Entry var3 : var0.entrySet()) {
         if (var3.getValue().equals(var1)) {
            return var3.getKey();
         }
      }

      return null;
   }

   public static boolean matchesIfNonNull(Object var0, UnaryFunctionC var1) {
      return var0 != null ? (Boolean)var1.call(var0) : false;
   }

   public static Object mapIfNonNull(Object var0, UnaryFunctionC var1, Object var2) {
      return var0 != null ? var1.call(var0) : var2;
   }

   public static int countTrue(boolean... var0) {
      int var1 = 0;

      for (boolean var5 : var0) {
         if (var5) {
            var1++;
         }
      }

      return var1;
   }
}
