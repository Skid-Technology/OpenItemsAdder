package dev.lone.itemsadder.world;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

final class ChunkViewerIndex {
   private final Map chunkViewers = new ConcurrentHashMap();

   void addViewer(UUID var1, long var2, Object var4) {
      this.chunkViewers.computeIfAbsent(var1, var0 -> new ConcurrentHashMap<>()).computeIfAbsent(var2, var0 -> ConcurrentHashMap.newKeySet()).add(var4);
   }

   void removeViewer(UUID var1, long var2, Object var4) {
      ConcurrentMap var5 = (ConcurrentMap)this.chunkViewers.get(var1);
      if (var5 != null) {
         var5.computeIfPresent(var2, (var1x, var2x) -> {
            var2x.remove(var4);
            return (Set)(var2x.isEmpty() ? null : var2x);
         });
      }
   }

   Set getViewers(UUID var1, long var2) {
      Map var4 = (Map)this.chunkViewers.get(var1);
      return var4 == null ? Set.of() : var4.getOrDefault(var2, Set.of());
   }

   void clear() {
      this.chunkViewers.clear();
   }
}
