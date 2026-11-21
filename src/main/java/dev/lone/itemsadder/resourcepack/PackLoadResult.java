package dev.lone.itemsadder.resourcepack;

import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class PackLoadResult {
   private final List diagnostics;
   private final Map textureSources;

   PackLoadResult(List var1) {
      this(var1, Map.of());
   }

   PackLoadResult(List var1, Map var2) {
      this.diagnostics = var1;
      this.textureSources = var2;
   }

   List buildSummaryLines() {
      LinkedHashMap var1 = new LinkedHashMap();

      for (PackFinding var3 : this.diagnostics) {
         var1.computeIfAbsent(var3.severity + "/" + var3.code, var0 -> new ArrayList<>()).add(var3);
      }

      ArrayList var4 = new ArrayList();
      var1.forEach(
         (var1x, var2) -> {
            StringBuilder var3x = new StringBuilder("[Pack textures][").append(var1x).append("] ").append(var2.size()).append(" issue(s)");

            for (PackFinding var5 : var2.stream().limit(4L).toList()) {
               var3x.append("\n  - ")
                  .append(var5.source)
                  .append(" -> ")
                  .append(var5.path)
                  .append(": ")
                  .append(var5.detail.length() <= 700 ? var5.detail : var5.detail.substring(0, 697) + "...");
            }

            if (var2.size() > 4) {
               var3x.append("\n  ... ").append(var2.size() - 4).append(" more; see output/pack-texture-validation.txt");
            }

            var4.add(var3x.toString());
         }
      );
      return var4;
   }

   void writeReport(Path var1) {
      Files.createDirectories(var1.toAbsolutePath().getParent());
      StringBuilder var2 = new StringBuilder("ItemsAdder generated pack texture validation\n");

      for (PackFinding var4 : this.diagnostics) {
         var2.append(var4.severity)
            .append(' ')
            .append(var4.code)
            .append(' ')
            .append(var4.source)
            .append(" -> ")
            .append(var4.path)
            .append(": ")
            .append(var4.detail)
            .append('\n');
      }

      Files.writeString(var1, var2, StandardCharsets.UTF_8);
      if (!this.textureSources.isEmpty()) {
         Files.writeString(var1.resolveSibling("pack-texture-sources.json"), new Gson().toJson(this.textureSources));
      }
   }
}
