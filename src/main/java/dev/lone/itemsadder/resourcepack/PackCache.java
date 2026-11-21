package dev.lone.itemsadder.resourcepack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

final class PackCache {
   private final Map buk = new HashMap();
   private final long bul;
   private long bum;

   PackCache(long var1) {
      this.bul = var1;
   }

   OverlayPackFile getOrCompute(PackCacheKey var1, Supplier var2) {
      OverlayPackFile var3 = (OverlayPackFile)this.buk.get(var1);
      if (var3 != null) {
         return var3;
      }

      OverlayPackFile var4 = (OverlayPackFile)var2.get();
      long var5 = var4.bytes() == null ? 256L : var4.bytes().length + 256L;
      if (var5 <= this.bul) {
         if (this.bum + var5 > this.bul) {
            this.clear();
         }

         this.buk.put(var1, var4);
         this.bum += var5;
      }

      return var4;
   }

   void clear() {
      this.buk.clear();
      this.bum = 0L;
   }
}
