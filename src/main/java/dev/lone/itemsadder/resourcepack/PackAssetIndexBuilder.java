package dev.lone.itemsadder.resourcepack;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

final class PackAssetIndexBuilder {
   private final PackAssetResult bxf;
   private final Set bxg = new TreeSet();
   private final Map bxh = new TreeMap();

   private PackAssetIndexBuilder(PackAssetResult var1) {
      this.bxf = var1;
   }

   private void method6039(String var1, List var2) {
      this.bxh.computeIfAbsent(var1, var0 -> new TreeSet<>(Comparator.comparing(var0x -> String.join("\u0000", var0x)))).add(var2);
   }
}
