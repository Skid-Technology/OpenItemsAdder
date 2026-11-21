package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.util.IntBoolPair;
import dev.lone.itemsadder.util.IntLabel;
import dev.lone.itemsadder.util.LoadFailedException;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.Utils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

public class PackBuildWorker {
   private static final Object cd = new Object();
   private static final int ce = 10000;
   private static final int cf = 30000;
   private static final int cg = 3;
   private static final long ch = Duration.ofMinutes(3L).toNanos();
   private static final long ci = 1000000000L;
   private static final long cj = 2000000000L;
   private static final int ck = 50000;
   private static final int cl = 16384;
   private static final long cm = 15000L;
   private static final long cn = 30000L;
   private static final long co = Duration.ofMinutes(10L).toMillis();
   private static final Pattern cp = Pattern.compile("Duration:\\s*(\\d+):(\\d+):(\\d+(?:\\.\\d+)?)");
   private static volatile PackBuildWorker cq;
   private final AtomicBoolean cr = new AtomicBoolean(false);
   private final AtomicBoolean cs = new AtomicBoolean(false);
   private final Set ct = ConcurrentHashMap.newKeySet();
   @Nullable
   private final Path cu;
   @Nullable
   private volatile File cv;
   @Nullable
   private volatile BukkitTask cw;
   private final AtomicReference cx = new AtomicReference();
   private volatile String cy = "disabled";
   private volatile boolean cz;
   private volatile boolean cA;
   private volatile long cB = -1L;
   private volatile long cC = -1L;
   private boolean cD;
   private boolean cE;
   private long cF;
   private long cG = -1L;
   private volatile long cH;

   public static PackBuildWorker getInstance() {
      return cq;
   }

   public static synchronized void configure(File var0) {
      Path var1 = toNormalizedPath(var0);
      boolean var2 = Settings.get().ffmpegDownload;
      if (cq != null && !cq.cz && Objects.equals(var1, cq.cu)) {
         cq.verifyInstallation(var2);
      } else {
         if (cq != null) {
            cq.terminate("reconfigured");
         }

         cq = new PackBuildWorker(var1, var2);
      }
   }

   public static synchronized void disable() {
      if (cq == null || cq.cu != null || cq.cz) {
         if (cq != null) {
            cq.terminate("disabled");
         }

         cq = new PackBuildWorker(null, false);
      }
   }

   public static synchronized void dispose() {
      if (cq != null) {
         cq.terminate("disposed");
      }
   }

   public PackBuildWorker(File var1) {
      this(toNormalizedPath(var1), var1 != null && Settings.get().ffmpegDownload);
   }

   private PackBuildWorker(@Nullable Path var1, boolean var2) {
      this.cu = var1;
      this.cA = var2;
      if (var1 != null) {
         this.verifyInstallation(var2);
      }
   }

   public boolean isAvailable() {
      return this.cr.get();
   }

   public boolean isDownloading() {
      return this.cs.get();
   }

   public String getStatus() {
      return this.cy;
   }

   public synchronized void requestNotice() {
      this.cE = true;
      this.notifyPendingIfReady();
   }

   public File transcodeAudio(File var1, File var2, IntBoolPair var3) {
      File var4 = this.requireExecutable();
      if (!var3.hasValue()) {
         throw new IllegalArgumentException("At least one of bitrateQuality or mono must be set");
      }

      if (var3.key == null || var3.key >= 1 && var3.key <= 100) {
         Path var5 = var2.toPath().toAbsolutePath();
         Path var6 = var5.getParent();
         if (var6 == null) {
            throw new IOException("FFmpeg output has no parent directory: " + var2);
         }

         Files.createDirectories(var6);
         Path var7 = Files.createTempFile(var6, ".ffmpeg-", "-" + var2.getName());
         ArrayList var8 = new ArrayList();
         var8.add(var4.getAbsolutePath());
         var8.add("-y");
         var8.add("-hide_banner");
         var8.add("-loglevel");
         var8.add("error");
         var8.add("-i");
         var8.add(var1.getAbsolutePath());
         if (var3.flag != null) {
            var8.add("-ac");
            var8.add(var3.flag ? "1" : "2");
         }

         var8.add("-c:a");
         var8.add("libvorbis");
         if (var3.key != null && var3.key < 100) {
            int var9 = 48 + (int)(var3.key.intValue() / 100.0 * 144.0);
            var8.add("-b:a");
            var8.add(var9 + "k");
         }

         var8.add(var7.toString());

         try {
            IntLabel var14 = this.runCancellable(var8, co, () -> this.cz);
            if (var14.id() != 0) {
               throw new IOException("FFmpeg failed: " + var14.label());
            } else if (Files.isRegularFile(var7) && Files.size(var7) != 0L) {
               moveReplace(var7, var5);
               return var2;
            } else {
               throw new IOException("FFmpeg produced no output for: " + var1);
            }
         } finally {
            Files.deleteIfExists(var7);
         }
      } else {
         throw new IllegalArgumentException("quality must be between 1 and 100");
      }
   }

   public double probeDuration(File var1) {
      File var2 = this.requireExecutable();
      IntLabel var3 = this.runCancellable(List.of(var2.getAbsolutePath(), "-hide_banner", "-i", var1.getAbsolutePath()), 30000L, () -> this.cz);
      double var4 = parseDuration(var3.label());
      if (var4 < 0.0) {
         throw new IOException("Unable to determine duration from FFmpeg output: " + var3.label());
      } else {
         return var4;
      }
   }

   private File requireExecutable() {
      File var1 = this.cv;
      if (this.cr.get() && var1 != null) {
         return var1;
      } else {
         throw new IOException("FFmpeg is not available (" + this.cy + ")");
      }
   }

   private synchronized void verifyInstallation(boolean var1) {
      if (!this.cz && this.cu != null) {
         this.cA = var1;
         if (this.cr.get()) {
            File var2 = this.cv;
            if (var2 != null && var2.isFile() && (isWindows() || var2.canExecute()) && var2.length() == this.cB && var2.lastModified() == this.cC) {
               return;
            }

            this.cr.set(false);
            this.cv = null;
            this.cy = "cached executable changed or disappeared";
         }

         if (this.cs.get()) {
            if (!var1) {
               this.cancelDownload("download disabled");
               this.warnUnavailable();
            }
         } else {
            try {
               Files.createDirectories(this.cu);
               this.cy = "checking cached installation";
               File var5 = this.findExecutable(this.cu.resolve(platformKey()), -1L);
               if (var5 != null) {
                  this.markAvailable(var5);
                  Log.devLog("[Libs] FFmpeg is available at: " + var5.getAbsolutePath());
                  return;
               }
            } catch (InterruptedException var3) {
               Thread.currentThread().interrupt();
               this.cy = "cached installation check interrupted";
               return;
            } catch (IOException var4) {
               this.cy = "cached installation check failed: " + messageOf(var4);
               Log.warn("[Libs] Failed to check the cached FFmpeg installation: " + messageOf(var4));
            }

            if (!var1) {
               this.cancelDownload("download disabled");
               this.warnUnavailable();
            } else if (!this.cs.get()) {
               this.cD = false;
               this.startDownload();
            }
         }
      }
   }

   private synchronized void startDownload() {
      if (!this.cz && this.cu != null && this.cA && !this.cr.get() && !this.cs.get()) {
         String var1;
         String var2;
         try {
            var1 = platformKey();
            var2 = downloadUrlFor(var1);
         } catch (IOException var7) {
            this.cy = "unsupported platform";
            Log.error("[Libs] " + messageOf(var7));
            return;
         }

         long var3 = ++this.cH;
         this.cs.set(true);
         this.cy = "queued for download";
         logDownloadStart(this.cu.resolve(var1), var2);

         try {
            this.cw = SchedulerUtils.runAsync(var5 -> {
               synchronized (cd) {
                  this.runDownload(var3, var1, var2);
               }
            });
         } catch (RuntimeException var6) {
            this.cs.set(false);
            this.cy = "could not schedule download: " + messageOf(var6);
            Log.error("[Libs] Failed to schedule the FFmpeg download: " + messageOf(var6));
         }
      }
   }

   private void runDownload(long var1, String var3, String var4) {
      Path var5 = null;

      try {
         this.checkNotCancelled(var1);
         var5 = Files.createTempDirectory(this.cu, ".ffmpeg-staging-");
         Path var45 = var5.resolve("download.zip");
         this.downloadArchive(var1, var4, var45);
         this.checkNotCancelled(var1);
         this.setStatus(var1, "extracting download");
         Path var7 = var5.resolve("extracted");
         extractArchive(var45, var7, this, var1);
         this.checkNotCancelled(var1);
         this.setStatus(var1, "validating download");
         File var8 = this.findExecutable(var7, var1);
         if (var8 == null) {
            throw new IOException("FFmpeg executable not found or invalid after extraction");
         }

         Path var9 = var7.relativize(var8.toPath());
         Path var10 = this.cu.resolve(var3);
         Files.createDirectories(var10);
         synchronized (this) {
            this.checkNotCancelled(var1);
            Path var12 = var10.resolve(".install-" + UUID.randomUUID());
            atomicInstall(var7, var12);
            File var13 = var12.resolve(var9).toFile();
            this.markAvailable(var13);
            Log.log("[Libs] FFmpeg download, extraction and validation completed: " + var13.getAbsolutePath());
         }
      } catch (InterruptedException var42) {
         Thread.currentThread().interrupt();
         synchronized (this) {
            if (var1 == this.cH && !this.cz) {
               this.cy = this.cA ? "download interrupted" : "download disabled";
            }
         }
      } catch (IOException | RuntimeException var43) {
         IOException var6 = var43;
         synchronized (this) {
            if (var1 == this.cH && !this.cz) {
               this.cy = "installation failed: " + messageOf(var6);
               Log.error("[Libs] Failed to install FFmpeg: " + messageOf(var6));
            }
         }
      } finally {
         if (var5 != null) {
            try {
               deleteRecursively(var5);
            } catch (IOException var38) {
               Log.warn("[Libs] Failed to clean FFmpeg staging files: " + messageOf(var38));
            }
         }

         synchronized (this) {
            if (var1 == this.cH) {
               this.cs.set(false);
               this.cw = null;
            }
         }
      }
   }

   private void downloadArchive(long var1, String var3, Path var4) {
      long var5 = System.nanoTime() + ch;
      LoadFailedException var7 = null;

      for (int var8 = 1; var8 <= 3; var8++) {
         this.checkNotCancelled(var1);
         if (System.nanoTime() >= var5) {
            throw new SocketTimeoutException("FFmpeg download exceeded three minutes");
         }

         this.setStatus(var1, "downloading (attempt " + var8 + "/3)");
         Log.log("[Libs] Downloading FFmpeg: 0% (attempt " + var8 + "/3)");

         try {
            this.downloadAttempt(var1, var3, var4, var5);
            return;
         } catch (LoadFailedException var11) {
            var7 = var11;
            if (!var11.fatal || var8 == 3) {
               throw var11;
            }

            Log.warn("[Libs] FFmpeg download attempt " + var8 + " failed: " + messageOf(var11) + ". Retrying...");
            long var9 = 500L * var8;
            if (System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(var9) >= var5) {
               throw new SocketTimeoutException("FFmpeg download exceeded three minutes");
            }

            Thread.sleep(var9);
         }
      }

      throw var7 == null ? new IOException("FFmpeg download failed") : var7;
   }

   private void downloadAttempt(long var1, String var3, Path var4, long var5) {
      HttpURLConnection var7;
      try {
         var7 = (HttpURLConnection)new URL(var3).openConnection();
      } catch (IOException var35) {
         throw new LoadFailedException("Failed to connect: " + messageOf(var35), true, var35);
      }

      this.cx.set(var7);

      try {
         this.checkNotCancelled(var1);
         var7.setConnectTimeout(remainingTimeoutMillis(var5, 10000));
         var7.setReadTimeout(remainingTimeoutMillis(var5, 30000));
         var7.setInstanceFollowRedirects(true);
         var7.setRequestProperty("User-Agent", "ItemsAdder FFmpeg installer");

         int var8;
         try {
            var8 = var7.getResponseCode();
         } catch (IOException var34) {
            throw new LoadFailedException("Failed to connect: " + messageOf(var34), true, var34);
         }

         if (var8 < 200 || var8 >= 300) {
            boolean var39 = var8 == 408 || var8 == 429 || var8 >= 500;
            throw new LoadFailedException("HTTP " + var8 + " while downloading FFmpeg", var39);
         }

         long var9 = var7.getContentLengthLong();
         if (var9 > 1000000000L) {
            throw new LoadFailedException("FFmpeg archive is unexpectedly large: " + var9 + " bytes", false);
         }

         try (OutputStream var11 = Files.newOutputStream(var4, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            InputStream var12;
            try {
               var12 = var7.getInputStream();
            } catch (IOException var33) {
               throw new LoadFailedException("Failed to read the response: " + messageOf(var33), true, var33);
            }

            byte[] var13 = new byte[8192];
            long var14 = 0L;
            int var16 = 0;

            try (var12) {
               while (true) {
                  this.checkNotCancelled(var1);
                  if (System.nanoTime() >= var5) {
                     throw new LoadFailedException("FFmpeg download exceeded three minutes", true);
                  }

                  int var18;
                  try {
                     var18 = var12.read(var13);
                  } catch (IOException var32) {
                     throw new LoadFailedException("Failed while reading the archive: " + messageOf(var32), true, var32);
                  }

                  if (var18 == -1) {
                     break;
                  }

                  var14 += var18;
                  if (var14 > 1000000000L) {
                     throw new LoadFailedException("FFmpeg archive exceeded the size limit", false);
                  }

                  var11.write(var13, 0, var18);
                  if (var9 > 0L) {
                     int var19 = (int)(var14 * 100L / var9);
                     if (var19 >= var16 + 10) {
                        var16 = var19;
                        Log.log("[Libs] Downloading FFmpeg: " + Math.min(var19, 100) + "%");
                     }
                  }
               }
            }

            if (var14 == 0L) {
               throw new IOException("FFmpeg download was empty");
            }

            if (var9 > 0L && var14 < var9) {
               throw new LoadFailedException("FFmpeg download ended early (" + var14 + "/" + var9 + " bytes)", true);
            }
         }
      } finally {
         var7.disconnect();
         this.cx.compareAndSet(var7, null);
      }
   }

   @Nullable
   private File findExecutable(Path var1, long var2) {
      if (!Files.isDirectory(var1)) {
         return null;
      }

      List var4;
      try (Stream var5 = Files.walk(var1)) {
         var4 = var5.filter(var0 -> Files.isRegularFile(var0)).filter(var0 -> isFfmpegBinary(var0.toFile())).toList();
      }

      for (Path var6 : var4) {
         if (var2 >= 0L) {
            this.checkNotCancelled(var2);
         }

         File var7 = var6.toFile();
         if (!var7.setExecutable(true)) {
            Log.warn("[Libs] Failed to set executable permission for FFmpeg binary: " + var7.getAbsolutePath());
         }

         try {
            IntLabel var8 = this.runCancellable(
               List.of(var7.getAbsolutePath(), "-version"), 15000L, () -> this.cz || var2 >= 0L && (!this.cA || this.cH != var2)
            );
            if (var8.id() == 0 && var8.label().toLowerCase(Locale.ROOT).contains("ffmpeg version")) {
               return var7;
            }

            Log.warn("[Libs] Ignoring invalid FFmpeg executable " + var7.getAbsolutePath() + " (exit code " + var8.id() + ")");
         } catch (IOException var10) {
            Log.warn("[Libs] Ignoring invalid FFmpeg executable " + var7.getAbsolutePath() + ": " + messageOf(var10));
         }
      }

      return null;
   }

   private synchronized void markAvailable(File var1) {
      boolean var2 = !this.cr.get();
      this.cB = var1.length();
      this.cC = var1.lastModified();
      this.cv = var1;
      this.cr.set(true);
      this.cy = "available";
      if (var2) {
         this.cF++;
      }

      this.notifyPendingIfReady();
   }

   private synchronized void notifyPendingIfReady() {
      if (this.cr.get() && this.cE && this.cG != this.cF) {
         this.cE = false;
         this.cG = this.cF;
         Log.warn("[Libs] FFmpeg is now available. Run /iazip to process the audio work skipped while it was unavailable.");
      } else if (this.cr.get() && this.cG == this.cF) {
         this.cE = false;
      }
   }

   private synchronized void terminate(String var1) {
      if (!this.cz) {
         this.cz = true;
         this.cE = false;
         this.cancelDownload(var1);
         this.cr.set(false);
         this.cv = null;
         this.cB = -1L;
         this.cC = -1L;
         this.cy = var1;
      }
   }

   private synchronized void cancelDownload(String var1) {
      this.cA = false;
      this.cH++;
      BukkitTask var2 = this.cw;
      this.cw = null;
      if (var2 != null) {
         var2.cancel();
      }

      HttpURLConnection var3 = (HttpURLConnection)this.cx.getAndSet(null);
      if (var3 != null) {
         var3.disconnect();
      }

      this.ct.forEach(Process::destroyForcibly);
      this.cs.set(false);
      this.cy = var1;
   }

   private void checkNotCancelled(long var1) {
      if (Thread.currentThread().isInterrupted() || this.cz || !this.cA || var1 != this.cH) {
         throw new InterruptedException("FFmpeg installation was cancelled");
      }
   }

   private synchronized void setStatus(long var1, String var3) {
      this.checkNotCancelled(var1);
      this.cy = var3;
   }

   private static int remainingTimeoutMillis(long var0, int var2) {
      long var3 = var0 - System.nanoTime();
      if (var3 <= 0L) {
         throw new LoadFailedException("FFmpeg download exceeded three minutes", true);
      } else {
         return (int)Math.max(1L, Math.min(var2, TimeUnit.NANOSECONDS.toMillis(var3)));
      }
   }

   private void warnUnavailable() {
      if (!this.cD && this.cu != null) {
         this.cD = true;

         try {
            String var1 = platformKey();
            String var2 = downloadUrlFor(var1);
            this.cy = "unavailable; automatic download disabled";
            Log.warn("[Libs] FFmpeg is unavailable and 'resource_pack.ffmpeg.download' is false.");
            Log.warn("[Libs] Download from: " + var2);
            Log.warn("[Libs] Extract it into: " + Utils.normalizePluginPath(this.cu.resolve(var1).toString()));
         } catch (IOException var3) {
            this.cy = "unsupported platform";
            Log.error("[Libs] " + messageOf(var3));
         }
      }
   }

   private static void logDownloadStart(Path var0, String var1) {
      Log.warn("[Libs] FFmpeg is unavailable; downloading it in the background.");
      Log.warn("[Libs] Source: " + var1);
      Log.warn("[Libs] Destination: " + var0 + " (set 'resource_pack.ffmpeg.download' to false to disable automatic downloads)");
   }

   static void extractZip(Path var0, Path var1) {
      try {
         extractArchive(var0, var1, null, -1L);
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
         throw new IOException("FFmpeg extraction interrupted", var3);
      }
   }

   private static void extractArchive(Path var0, Path var1, @Nullable PackBuildWorker var2, long var3) {
      Files.createDirectories(var1);
      Path var5 = var1.toAbsolutePath().normalize();
      long var6 = 0L;
      int var8 = 0;
      byte[] var9 = new byte[8192];

      ZipEntry var11;
      try (ZipInputStream var10 = new ZipInputStream(Files.newInputStream(var0))) {
         for (; (var11 = var10.getNextEntry()) != null; var10.closeEntry()) {
            if (var2 != null) {
               var2.checkNotCancelled(var3);
            }

            if (++var8 > 50000) {
               throw new IOException("FFmpeg archive contains too many entries");
            }

            Path var12 = var5.resolve(var11.getName()).normalize();
            if (!var12.startsWith(var5)) {
               throw new IOException("Unsafe path in FFmpeg archive: " + var11.getName());
            }

            if (var11.isDirectory()) {
               Files.createDirectories(var12);
            } else {
               Path var13 = var12.getParent();
               if (var13 == null) {
                  throw new IOException("Invalid path in FFmpeg archive: " + var11.getName());
               }

               Files.createDirectories(var13);

               int var15;
               try (OutputStream var14 = Files.newOutputStream(var12, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
                  while ((var15 = var10.read(var9)) != -1) {
                     if (var2 != null) {
                        var2.checkNotCancelled(var3);
                     }

                     var6 += var15;
                     if (var6 > 2000000000L) {
                        throw new IOException("FFmpeg archive exceeded the extracted size limit");
                     }

                     var14.write(var9, 0, var15);
                  }
               }
            }
         }
      }
   }

   static IntLabel runProcess(List var0, long var1) {
      return execProcess(var0, var1, null, () -> false);
   }

   private IntLabel runCancellable(List var1, long var2, BooleanSupplier var4) {
      return execProcess(var1, var2, this.ct, var4);
   }

   private static IntLabel execProcess(List var0, long var1, @Nullable Set var3, BooleanSupplier var4) {
      Process var5 = new ProcessBuilder(var0).redirectErrorStream(true).start();
      if (var3 != null) {
         var3.add(var5);
      }

      ByteArrayOutputStream var6 = new ByteArrayOutputStream();
      AtomicReference var7 = new AtomicReference();
      Thread var8 = Thread.ofVirtual().name("ItemsAdder FFmpeg output").start(() -> {
         byte[] var3x = new byte[4096];

         try (InputStream var4x = var5.getInputStream()) {
            int var6x = 0;

            int var5x;
            while ((var5x = var4x.read(var3x)) != -1) {
               int var7x = Math.min(var5x, 16384 - var6x);
               if (var7x > 0) {
                  var6.write(var3x, 0, var7x);
                  var6x += var7x;
               }
            }
         } catch (IOException var10) {
            if (var5.isAlive()) {
               var7.set(var10);
            }
         }
      });

      try {
         var5.getOutputStream().close();
         if (var4.getAsBoolean()) {
            throw new IOException("FFmpeg process was cancelled");
         } else if (!var5.waitFor(var1, TimeUnit.MILLISECONDS)) {
            destroyProcess(var5);
            awaitOutputReader(var5, var8);
            throw new IOException("FFmpeg process timed out after " + var1 + "ms");
         } else {
            awaitOutputReader(var5, var8);
            IOException var9 = (IOException)var7.get();
            if (var9 != null) {
               throw var9;
            } else {
               return new IntLabel(var5.exitValue(), var6.toString(StandardCharsets.UTF_8));
            }
         }
      } catch (InterruptedException var24) {
         var5.destroyForcibly();
         closeQuietly(var5.getInputStream());
         Thread.currentThread().interrupt();
         throw var24;
      } finally {
         boolean var14 = Thread.interrupted();
         if (var5.isAlive()) {
            var5.destroyForcibly();

            try {
               var5.waitFor(1L, TimeUnit.SECONDS);
            } catch (InterruptedException var23) {
               var14 = true;
            }
         }

         closeQuietly(var5.getInputStream());
         closeQuietly(var5.getErrorStream());
         closeQuietly(var5.getOutputStream());
         if (var8.isAlive()) {
            try {
               var8.join(1000L);
            } catch (InterruptedException var22) {
               var14 = true;
            }
         }

         if (var3 != null) {
            var3.remove(var5);
         }

         if (var14) {
            Thread.currentThread().interrupt();
         }
      }
   }

   private static void awaitOutputReader(Process var0, Thread var1) {
      var1.join(1000L);
      if (var1.isAlive()) {
         closeQuietly(var0.getInputStream());
         var1.join(1000L);
         if (var1.isAlive()) {
            throw new IOException("FFmpeg output reader did not stop");
         }
      }
   }

   private static void destroyProcess(Process var0) {
      var0.destroy();
      if (!var0.waitFor(1L, TimeUnit.SECONDS)) {
         var0.destroyForcibly();
         var0.waitFor(1L, TimeUnit.SECONDS);
      }
   }

   private static void closeQuietly(AutoCloseable var0) {
      try {
         var0.close();
      } catch (Exception var2) {
      }
   }

   static double parseDuration(String var0) {
      Matcher var1 = cp.matcher(var0);
      if (!var1.find()) {
         return -1.0;
      }

      try {
         double var2 = Double.parseDouble(var1.group(2));
         double var4 = Double.parseDouble(var1.group(3));
         double var6 = Double.parseDouble(var1.group(1)) * 3600.0 + var2 * 60.0 + var4;
         return var2 < 60.0 && var4 < 60.0 && var6 > 0.0 && Double.isFinite(var6) ? var6 : -1.0;
      } catch (NumberFormatException var8) {
         return -1.0;
      }
   }

   static void deleteRecursively(Path var0) {
      if (Files.exists(var0)) {
         try (Stream var1 = Files.walk(var0)) {
            for (Path var3 : var1.sorted(Comparator.reverseOrder()).toList()) {
               Files.deleteIfExists(var3);
            }
         }
      }
   }

   private static void moveReplace(Path var0, Path var1) {
      try {
         Files.move(var0, var1, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
      } catch (AtomicMoveNotSupportedException var3) {
         Files.move(var0, var1, StandardCopyOption.REPLACE_EXISTING);
      }
   }

   private static void atomicInstall(Path var0, Path var1) {
      try {
         Files.move(var0, var1, StandardCopyOption.ATOMIC_MOVE);
      } catch (AtomicMoveNotSupportedException var3) {
         throw new IOException("The FFmpeg cache filesystem does not support atomic installation", var3);
      }
   }

   private static boolean isFfmpegBinary(File var0) {
      String var1 = var0.getName().toLowerCase(Locale.ROOT);
      return isWindows() ? var1.equals("ffmpeg.exe") : var1.equals("ffmpeg");
   }

   private static boolean isWindows() {
      return System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win");
   }

   private static String platformKey() {
      String var0 = System.getProperty("os.name").toLowerCase(Locale.ROOT);
      if (var0.contains("win")) {
         return "windows";
      } else if (var0.contains("mac")) {
         return "mac";
      } else {
         return !var0.contains("nux") && !var0.contains("nix") ? "unknown" : "linux";
      }
   }

   private static String downloadUrlFor(String var0) {
      return switch (var0) {
         case "windows" -> "https://github.com/ItemsAdder/ffmpeg-assets/releases/download/2025-05-19/ffmpeg_windows.zip";
         case "linux" -> "https://github.com/ItemsAdder/ffmpeg-assets/releases/download/2025-05-19/ffmpeg_linux.zip";
         case "mac" -> "https://github.com/ItemsAdder/ffmpeg-assets/releases/download/2025-05-19/ffmpeg_mac.zip";
         default -> throw new IOException("Unsupported OS: " + var0);
      };
   }

   private static Path toNormalizedPath(File var0) {
      return var0 == null ? null : var0.toPath().toAbsolutePath().normalize();
   }

   private static String messageOf(Throwable var0) {
      String var1 = var0.getMessage();
      return var1 != null && !var1.isBlank() ? var1 : var0.getClass().getSimpleName();
   }
}
