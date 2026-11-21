package dev.lone.itemsadder.resourcepack;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

class PackResolverCache extends LinkedHashMap {
   PackResolverCache(PackResolver var1, int var2, float var3, boolean var4) {
      super(var2, var3, var4);
      this.bBx = var1;
   }

   @Override
   protected boolean removeEldestEntry(Entry eldest) {
      return this.size() > 64;
   }
}
