package dev.lone.itemsadder.util;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public final class TrackedSet {
   private final Set items = Collections.synchronizedSet(Collections.newSetFromMap(new IdentityHashMap()));

   boolean addTracked(Object var1) {
      return this.items.add(var1);
   }

   void removeTracked(Object var1) {
      this.items.remove(var1);
   }

   void clear() {
      this.items.clear();
   }
}
