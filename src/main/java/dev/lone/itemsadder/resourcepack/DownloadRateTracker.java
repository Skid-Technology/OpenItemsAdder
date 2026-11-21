package dev.lone.itemsadder.resourcepack;

import com.google.common.base.Ticker;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public final class DownloadRateTracker {
   private static final DownloadRateEntry bJC = new DownloadRateEntry(0L, false);
   static final int bJD = 10000;
   private final DownloadPolicy bJE;
   private final Ticker bJF;
   private final long bJG;
   private final long bJH;
   private final long bJI;
   private final int bJJ;
   private final LinkedHashMap bJK = new LinkedHashMap(16, 0.75F, true);

   public DownloadRateTracker(DownloadPolicy var1) {
      this(var1, Ticker.systemTicker());
   }

   DownloadRateTracker(DownloadPolicy var1, Ticker var2) {
      this(var1, var2, 10000);
   }

   DownloadRateTracker(DownloadPolicy var1, Ticker var2, int var3) {
      if (var3 < 1) {
         throw new IllegalArgumentException("maximumClients must be positive");
      }

      this.bJE = var1;
      this.bJF = var2;
      this.bJJ = var3;
      this.bJG = TimeUnit.SECONDS.toNanos(var1.periodSeconds());
      this.bJH = TimeUnit.MINUTES.toNanos(var1.cooldownMinutes());
      this.bJI = var1.cooldownEnabled() ? Math.max(this.bJG, this.bJH) : this.bJG;
   }

   public synchronized DownloadRateEntry checkDownload(String var1) {
      if (!this.bJE.rateLimitEnabled()) {
         return bJC;
      }

      if (var1 == null) {
         throw new NullPointerException("remoteIp");
      }

      long var2 = this.bJF.read();
      DownloadRateSnapshot var4 = (DownloadRateSnapshot)this.bJK.get(var1);
      if (var4 != null && var2 - var4.lastAccess() >= this.bJI) {
         this.bJK.remove(var1);
         var4 = null;
      }

      if (var4 == null) {
         if (this.bJK.size() == this.bJJ) {
            Entry var5 = (Entry)this.bJK.entrySet().iterator().next();
            if (var2 - ((DownloadRateSnapshot)var5.getValue()).lastAccess() < this.bJI) {
               return cooldownEntry(this.bJG, false);
            }

            this.bJK.remove(var5.getKey());
         }

         var4 = new DownloadRateSnapshot(var2, 0, 0, var2, 0L, false, var2);
      }

      if (var4.blocked()) {
         long var9 = this.bJH - (var2 - var4.blockedAt());
         if (var9 > 0L) {
            this.bJK
               .put(var1, new DownloadRateSnapshot(var4.windowStart(), var4.count(), var4.rejections(), var4.lastRejection(), var4.blockedAt(), true, var2));
            return cooldownEntry(var9, false);
         }

         var4 = new DownloadRateSnapshot(var2, 0, 0, var2, 0L, false, var2);
      }

      long var10 = var4.windowStart();
      int var7 = var4.count();
      if (var2 - var10 >= this.bJG) {
         var10 = var2;
         var7 = 0;
      }

      if (var7 < this.bJE.maxRequests()) {
         this.bJK.put(var1, new DownloadRateSnapshot(var10, var7 + 1, var4.rejections(), var4.lastRejection(), 0L, false, var2));
         return bJC;
      }

      if (this.bJE.cooldownEnabled()) {
         int var8 = var2 - var4.lastRejection() >= this.bJH ? 1 : var4.rejections() + 1;
         if (var8 >= this.bJE.afterRejections()) {
            this.bJK.put(var1, new DownloadRateSnapshot(var10, var7, 0, var2, var2, true, var2));
            return cooldownEntry(this.bJH, true);
         } else {
            this.bJK.put(var1, new DownloadRateSnapshot(var10, var7, var8, var2, 0L, false, var2));
            return cooldownEntry(this.bJG - (var2 - var10), false);
         }
      } else {
         this.bJK.put(var1, new DownloadRateSnapshot(var10, var7, var4.rejections(), var4.lastRejection(), 0L, false, var2));
         return cooldownEntry(this.bJG - (var2 - var10), false);
      }
   }

   private static DownloadRateEntry cooldownEntry(long var0, boolean var2) {
      return new DownloadRateEntry(TimeUnit.NANOSECONDS.toSeconds(var0 - 1L) + 1L, var2);
   }
}
