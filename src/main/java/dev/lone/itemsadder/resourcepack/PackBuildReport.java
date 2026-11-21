package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent.PackEntry;
import dev.lone.itemsadder.util.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

final class PackBuildReport {
   private final List bAL;
   private final Set bAM;
   private final Map bAN;

   private PackBuildReport(List var1, Map var2) {
      this.bAL = List.copyOf(var1);
      this.bAM = var1.stream().filter(var0 -> !var0.VR().isRemoved()).map(PackEntryWithPath::path).collect(Collectors.toUnmodifiableSet());
      this.bAN = Map.copyOf(var2);
   }

   private static PackBuildReport method6512(Map var0, PackBuildMetrics var1) {
      ArrayList var2 = new ArrayList(var0.size());
      HashMap var3 = new HashMap(var0.size());
      var0.forEach((var3x, var4) -> {
         String var5 = Utils.normalizeSlashes(var3x);
         var1.bAS++;
         var2.add(new PackEntryWithPath(var5, var4));
         var3.put(var3x, var5);
      });
      return new PackBuildReport(var2, var3);
   }

   private PackBuildReport method6513(Set var1) {
      return var1.isEmpty() ? this : new PackBuildReport(this.bAL.stream().filter(var1x -> !var1.contains(var1x.path())).toList(), this.bAN);
   }

   private PackEntry hB(String var1) {
      PackEntry var2 = null;

      for (PackEntryWithPath var4 : this.bAL) {
         if (var4.path().equals(var1) && !var4.VR().isRemoved()) {
            var2 = var4.VR();
         }
      }

      return var2;
   }
}
