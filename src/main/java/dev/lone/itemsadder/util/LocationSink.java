package dev.lone.itemsadder.util;

import org.bukkit.Location;

public interface LocationSink {
   void placeLight(Location var1, int var2);

   void removeLight(Location var1);

   static LocationSink create() {
      try {
         return new LocationSinkImplA();
      } catch (Throwable var1) {
         Log.error("Failed to initialize built-in lights implementation.", var1);
         return new LocationSinkImplB();
      }
   }
}
