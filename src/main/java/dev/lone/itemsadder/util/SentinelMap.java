package dev.lone.itemsadder.util;

import java.util.concurrent.ConcurrentHashMap;

public class SentinelMap extends ConcurrentHashMap {
   private static final Object SENTINEL = (byte)0;

   public void putSentinel(Object var1) {
      this.put(var1, SENTINEL);
   }
}
