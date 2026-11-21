package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.util.Log;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public final class PackDownloadThrottle {
   private static final long bJi = TimeUnit.SECONDS.toNanos(1L);
   private static final Map bJj = new ConcurrentHashMap();
   private final DownloadPolicy bJk;
   private final DownloadRateTracker bJl;
   private final AtomicLong bJm = new AtomicLong(Long.MIN_VALUE);

   public PackDownloadThrottle(DownloadPolicy var1) {
      this.bJk = var1;
      this.bJl = new DownloadRateTracker(var1);
   }

   public PackDownloadResult checkRequest(String var1, Function var2) {
      DownloadRateEntry var3 = this.bJl.checkDownload(var1);
      if (var3.cooldownStarted() && this.shouldLogCooldown()) {
         Log.warn("[PackHosting] '" + var1 + "' exceeded the request limit and is blocked for " + this.bJk.cooldownMinutes() + " minutes.");
      }

      if (var3.retryAfterSeconds() > 0L) {
         return new PackDownloadResult(429, "Too Many Requests", var3.retryAfterSeconds());
      }

      if (this.bJk.blockNonGameRequests()) {
         String var4 = (String)var2.apply("User-Agent");
         if (var4 == null
            || !var4.startsWith("Minecraft Java")
            || var4.toLowerCase(Locale.ROOT).contains("bot")
            || isHeaderMissing(var2, "X-Minecraft-Username")
            || isHeaderMissing(var2, "X-Minecraft-UUID")
            || isHeaderMissing(var2, "X-Minecraft-Version")
            || isHeaderMissing(var2, "X-Minecraft-Version-ID")) {
            return new PackDownloadResult(401, "Unauthorized", 0L);
         }
      }

      return null;
   }

   long maxBytesPerSecond() {
      return this.bJk.maxSpeedMibPerSecond() * 1024L * 1024L;
   }

   Runnable acquireSlot(String var1) {
      AtomicBoolean var2 = new AtomicBoolean();
      bJj.compute(var1, (var2x, var3x) -> {
         int var4 = var3x == null ? 0 : var3x;
         var2.set(var4 < this.bJk.maxConcurrentDownloadsPerIp());
         return var2.get() ? var4 + 1 : var3x;
      });
      if (!var2.get()) {
         return null;
      }

      AtomicBoolean var3 = new AtomicBoolean();
      return () -> {
         if (var3.compareAndSet(false, true)) {
            bJj.computeIfPresent(var1, (var0x, var1xx) -> var1xx <= 1 ? null : var1xx - 1);
         }
      };
   }

   private boolean shouldLogCooldown() {
      long var1 = System.nanoTime();
      long var3 = this.bJm.get();
      return (var3 == Long.MIN_VALUE || var1 - var3 >= bJi) && this.bJm.compareAndSet(var3, var1);
   }

   private static boolean isHeaderMissing(Function var0, String var1) {
      String var2 = (String)var0.apply(var1);
      return var2 == null || var2.isBlank();
   }
}
