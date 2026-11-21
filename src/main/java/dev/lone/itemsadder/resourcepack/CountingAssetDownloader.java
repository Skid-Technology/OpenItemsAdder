package dev.lone.itemsadder.resourcepack;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

final class CountingAssetDownloader implements AssetDownloader {
   private final AssetDownloader bye;
   private final AtomicInteger byf = new AtomicInteger();
   private final AtomicInteger byg = new AtomicInteger();
   private final AtomicInteger byh = new AtomicInteger();
   private final LongAdder byi = new LongAdder();

   private CountingAssetDownloader(AssetDownloader var1) {
      this.bye = var1;
   }

   @Override
   public byte[] download(URI source, String range, long expectedStart, long expectedEnd) {
      this.byf.incrementAndGet();
      int var7 = this.byg.incrementAndGet();
      this.byh.accumulateAndGet(var7, Math::max);

      try {
         byte[] var8 = this.bye.download(source, range, expectedStart, expectedEnd);
         this.byi.add(var8.length);
         return var8;
      } finally {
         this.byg.decrementAndGet();
      }
   }
}
