package dev.lone.itemsadder.resourcepack;

import java.util.HashMap;
import java.util.Map;

final class PackResolverState {
   final PackFileCache bBy = new PackFileCache(true);
   final PackFileCache bBz = new PackFileCache(true);
   final Map bBA = new HashMap();

   private PackResolverState() {
   }
}
