package dev.lone.itemsadder.resourcepack;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public final class PackWarningSummary {
   private static final Map warnings = new ConcurrentHashMap();

   private PackWarningSummary() {
   }

   public static void reset() {
      warnings.clear();
   }

   public static void addWarning(String var0, String var1) {
      warnings.computeIfAbsent(var0, var0x -> ConcurrentHashMap.newKeySet()).add(var1);
   }

   static boolean isEmpty() {
      return warnings.isEmpty();
   }

   static String buildReport() {
      StringBuilder var0 = new StringBuilder("ItemsAdder resource-pack diagnostics\n");
      new TreeMap<String, Set>(warnings).forEach((var1, var2) -> {
         var0.append("\n## ").append(var1).append(" (").append(var2.size()).append(")\n");
         var2.stream().sorted().forEach(var1x -> var0.append("\n").append(var1x).append('\n'));
      });
      return var0.toString();
   }
}
