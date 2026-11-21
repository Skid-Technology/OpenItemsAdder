package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

final class ResourcePackRepository implements AutoCloseable {
   static final long RETIRED_RETENTION_MILLIS = TimeUnit.DAYS.toMillis(1L);
   private final Path cacheDir;
   private static final Set DEFAULT_LIVE_ARTIFACTS = ConcurrentHashMap.newKeySet();
   private final Set liveArtifacts;
   private volatile String currentUniversalHash;
   private boolean cleanupWarned;
   private String cachedUniversalSha1;
   private String cachedContentFingerprint;
   private final Map derivations = new ConcurrentHashMap();
   private final ExecutorService executor = Executors.newFixedThreadPool(Math.min(2, Runtime.getRuntime().availableProcessors()), var0 -> {
      Thread var1x = new Thread(var0, "ItemsAdder_client_pack");
      var1x.setDaemon(true);
      return var1x;
   });

   ResourcePackRepository(Path var1) {
      this(var1, DEFAULT_LIVE_ARTIFACTS);
   }

   ResourcePackRepository(Path var1, Set var2) {
      this.cacheDir = var1;
      this.liveArtifacts = var2;
   }

   void protectArtifact(Path var1) {
      this.liveArtifacts.add(var1);
   }

   void writeArtifactManifest(PackSource var1, String var2) {
      String var3 = var1.sha1();
      if (!var1.file().equals(this.cacheDir.resolve(var3 + ".zip"))) {
         throw new IOException("Published artifact is outside the content-addressed cache");
      }

      JSONObject var4 = new JSONObject();
      var4.put("key", var3);
      var4.put("universal_sha1", var2);
      var4.put("sha1", var3);
      var4.put("sha256", ResourcePackCompressor.digestFile(var1.file(), "SHA-256"));
      VanillaAssetCache.writeCacheFile(this.cacheDir.resolve(var3 + ".json"), JSON.toJSONBytes(var4));
      this.protectArtifact(var1.file());
   }

   PackSource publishUniversalPack(Path var1, String var2) {
      if (var2 != null && var2.matches("[a-f0-9]{40}")) {
         Files.createDirectories(this.cacheDir);
         Path var3 = this.cacheDir.resolve(var2 + ".zip");
         if (!Files.isRegularFile(var3) || !ResourcePackCompressor.digestFile(var3, "SHA-1").equals(var2)) {
            Path var4 = Files.createTempFile(this.cacheDir, ".universal_", ".tmp");

            try {
               Files.copy(var1, var4, StandardCopyOption.REPLACE_EXISTING);
               if (!ResourcePackCompressor.digestFile(var4, "SHA-1").equals(var2)) {
                  throw new IOException("Universal pack changed before publication");
               }

               Files.move(var4, var3, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } finally {
               Files.deleteIfExists(var4);
            }
         }

         PackSource var8 = new PackSource(var3, var2);
         this.writeArtifactManifest(var8, var2);
         return var8;
      } else {
         throw new IOException("Invalid universal pack hash");
      }
   }

   Executor getExecutor() {
      return this.executor;
   }

   void shutdownExecutor() {
      this.executor.shutdownNow();
   }

   CompletableFuture deriveClientPackAsync(Path var1, String var2, String var3, List var4) {
      if (var2 != null && var2.matches("[a-fA-F0-9]{40}") && var3.matches("[a-z0-9_]+")) {
         String var5 = var3 + (var4.isEmpty() ? "_unknown" : var4.stream().map(var0 -> "_" + var0.major() + "_" + var0.minor()).reduce("", String::concat));
         String var6 = "flat_4_fast_compression_" + var2.toLowerCase(Locale.ROOT) + "_" + var5;
         return this.derivations.computeIfAbsent(var6, var6x -> CompletableFuture.supplyAsync(() -> {
            try {
               return this.deriveClientPack(var1, var2, var6, var5, var4);
            } catch (IOException var7) {
               throw new CompletionException(var7);
            }
         }, this.executor));
      } else {
         return CompletableFuture.failedFuture(new IOException("Missing completed universal hash or invalid family"));
      }
   }

   private PackSource deriveClientPack(Path var1, String var2, String var3, String var4, List var5) {
      if (!ResourcePackCompressor.digestFile(var1, "SHA-1").equalsIgnoreCase(var2)) {
         throw new IOException("Universal pack changed before derivation");
      }

      String var6 = this.contentFingerprintFor(var1, var2);
      Files.createDirectories(this.cacheDir);
      Path var7 = this.cacheDir.resolve(var3 + ".zip");
      Path var8 = this.cacheDir.resolve(var3 + ".json");
      if (Files.isRegularFile(var7) && Files.isRegularFile(var8)) {
         try {
            JSONObject var9 = JSON.parseObject(Files.readString(var8));
            if (var3.equals(var9.getString("key")) && ResourcePackCompressor.digestFile(var7, "SHA-256").equals(var9.getString("sha256"))) {
               if (var5.isEmpty()) {
                  ResourcePackCompressor.verifyStrippedPack(var1, var7);
               } else if (!manifestMatches(var9, var4, var6)) {
                  ResourcePackCompressor.deriveForVersions(var1, var7, var5);
               }

               verifyUniversalUnchanged(var1, var2);
               this.protectArtifact(var7);
               return new PackSource(var7, ResourcePackCompressor.digestFile(var7, "SHA-1"));
            }
         } catch (IOException | RuntimeException var18) {
         }
      }

      Path var20 = Files.createTempFile(this.cacheDir, ".client_pack_", ".tmp");

      try {
         boolean var10 = this.copyMatchingArtifact(var4, var20, var6);
         if (!var10) {
            if (var5.isEmpty()) {
               ResourcePackCompressor.stripHumanoidEquipment(var1, var20);
            } else {
               ResourcePackCompressor.compressForVersions(var1, var20, var5);
               PackLoadResult var11 = ResourcePackLoader.optimizePackTextures(var1, var20, var5);

               for (String var13 : var11.buildSummaryLines()) {
                  if (var13.contains("[WARNING/")) {
                     Log.warn(var13);
                  } else {
                     Log.log(var13);
                  }
               }

               if (!var11.diagnostics().isEmpty()) {
                  var11.writeReport(this.cacheDir.resolve(var3 + ".textures.txt"));
               }
            }
         } else if (var5.isEmpty()) {
            ResourcePackCompressor.verifyStrippedPack(var1, var20);
         }

         verifyUniversalUnchanged(var1, var2);
         String var21 = ResourcePackCompressor.digestFile(var20, "SHA-1");
         String var22 = ResourcePackCompressor.digestFile(var20, "SHA-256");
         Files.move(var20, var7, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         JSONObject var23 = new JSONObject();
         var23.put("key", var3);
         var23.put("sha256", var22);
         var23.put("sha1", var21);
         var23.put("universal_sha1", var2);
         var23.put("revision", "flat_4_fast_compression");
         var23.put("content_fingerprint", var6);
         var23.put("target", var4);
         VanillaAssetCache.writeCacheFile(var8, JSON.toJSONBytes(var23));
         this.protectArtifact(var7);
         return new PackSource(var7, var21);
      } finally {
         Files.deleteIfExists(var20);
      }
   }

   private synchronized String contentFingerprintFor(Path var1, String var2) {
      if (!var2.equals(this.cachedUniversalSha1)) {
         String var3 = computeContentFingerprint(var1);
         verifyUniversalUnchanged(var1, var2);
         this.cachedContentFingerprint = var3;
         this.cachedUniversalSha1 = var2;
      }

      return this.cachedContentFingerprint;
   }

   private static boolean manifestMatches(JSONObject var0, String var1, String var2) {
      return var2.equals(var0.getString("content_fingerprint"))
         && "flat_4_fast_compression".equals(var0.getString("revision"))
         && var1.equals(var0.getString("target"));
   }

   private boolean copyMatchingArtifact(String var1, Path var2, String var3) {
      try (DirectoryStream var4 = Files.newDirectoryStream(this.cacheDir, "*.json")) {
         for (Path var6 : var4) {
            try {
               JSONObject var7 = JSON.parseObject(Files.readString(var6));
               if (var7 != null && manifestMatches(var7, var1, var3)) {
                  String var8 = var7.getString("key");
                  if (var8 != null && var8.matches("[a-zA-Z0-9_]+") && var6.getFileName().toString().equals(var8 + ".json")) {
                     Path var9 = this.cacheDir.resolve(var8 + ".zip");
                     if (Files.isRegularFile(var9) && !Files.isSymbolicLink(var9)) {
                        Files.copy(var9, var2, StandardCopyOption.REPLACE_EXISTING);
                        if (ResourcePackCompressor.digestFile(var2, "SHA-256").equals(var7.getString("sha256"))) {
                           return true;
                        }
                     }
                  }
               }
            } catch (IOException | RuntimeException var12) {
            }
         }
      }

      return false;
   }

   static String computeContentFingerprint(Path var0) {
      try (ZipFile var1 = new ZipFile(var0.toFile())) {
         MessageDigest var2 = MessageDigest.getInstance("SHA-256");
         MessageDigest var3 = MessageDigest.getInstance("SHA-256");
         TreeMap var4 = new TreeMap();
         Enumeration var5 = var1.entries();

         while (var5.hasMoreElements()) {
            ZipEntry var6 = (ZipEntry)var5.nextElement();
            if (var4.put(var6.getName(), var6) != null) {
               throw new IOException("Duplicate ZIP path: " + var6.getName());
            }
         }

         byte[] var17 = new byte[65536];

         for (Entry var7 : var4.entrySet()) {
            byte[] var8 = ((String)var7.getKey()).getBytes(StandardCharsets.UTF_8);
            var2.update(ByteBuffer.allocate(4).putInt(var8.length).array());
            var2.update(var8);
            var3.reset();

            int var10;
            try (InputStream var9 = var1.getInputStream((ZipEntry)var7.getValue())) {
               while ((var10 = var9.read(var17)) != -1) {
                  if (Thread.currentThread().isInterrupted()) {
                     throw new IOException("Client pack fingerprint stopped");
                  }

                  var3.update(var17, 0, var10);
               }
            }

            var2.update(var3.digest());
         }

         return "zip_content_1_" + HexFormat.of().formatHex(var2.digest());
      } catch (NoSuchAlgorithmException var16) {
         throw new IllegalStateException(var16);
      }
   }

   private static void verifyUniversalUnchanged(Path var0, String var1) {
      if (Thread.currentThread().isInterrupted() || !ResourcePackCompressor.digestFile(var0, "SHA-1").equalsIgnoreCase(var1)) {
         throw new IOException("Universal pack changed or derivation stopped");
      }
   }

   void setCurrentUniversalHash(String var1) {
      this.currentUniversalHash = var1.toLowerCase(Locale.ROOT);
      String var2 = "flat_4_fast_compression_" + var1.toLowerCase(Locale.ROOT) + "_";
      this.derivations
         .entrySet()
         .removeIf(
            var1x -> ((CompletableFuture)var1x.getValue()).isDone()
               && (!((String)var1x.getKey()).startsWith(var2) || ((CompletableFuture)var1x.getValue()).isCompletedExceptionally())
         );

      try {
         this.executor.execute(() -> {
            try {
               this.cleanupFor(var1.toLowerCase(Locale.ROOT));
            } catch (IOException | RuntimeException var3) {
               if (!this.cleanupWarned) {
                  this.cleanupWarned = true;
                  Log.warn("[Pack] per_client_version cache cleanup skipped: " + var3.getMessage());
               }
            }
         });
      } catch (RejectedExecutionException var4) {
      }
   }

   private synchronized void cleanupFor(String var1) {
      if (var1.equals(this.currentUniversalHash)) {
         this.pruneGenerations(var1, System.currentTimeMillis());
      }
   }

   synchronized void pruneGenerations(String var1, long var2) {
      Files.createDirectories(this.cacheDir);
      Path var4 = this.cacheDir.resolve("generations.json");
      JSONObject var5 = Files.exists(var4) ? JSON.parseObject(Files.readString(var4)) : new JSONObject();
      if (var5 == null) {
         throw new IOException("Invalid generation journal");
      }

      JSONObject var6 = var5.getJSONObject("retired");
      if (var6 == null) {
         var6 = new JSONObject();
      }

      String var7 = var5.getString("current");
      String var8 = var5.getString("previous");
      if (!var1.equals(var7)) {
         if (var8 != null && !var8.equals(var1)) {
            var6.put(var8, var2);
         }

         var8 = var7;
         var7 = var1;
      }

      var6.remove(var7);
      if (var8 != null) {
         var6.remove(var8);
      }

      var5.put("current", var7);
      var5.put("previous", var8);
      var5.put("retired", var6);
      VanillaAssetCache.writeCacheFile(var4, JSON.toJSONBytes(var5));
      boolean var9 = false;
      HashSet var10 = new HashSet();

      try (DirectoryStream var11 = Files.newDirectoryStream(this.cacheDir, "*.json")) {
         for (Path var13 : var11) {
            if (!var13.equals(var4) && !var13.getFileName().toString().endsWith(".url.json")) {
               try {
                  JSONObject var14 = JSON.parseObject(Files.readString(var13));
                  String var15 = var14.getString("universal_sha1");
                  String var16 = var14.getString("key");
                  if (var15 != null) {
                     var10.add(var15);
                  }

                  if (var15 != null && !var15.equals(var7) && !var15.equals(var8) && !var15.equals(this.currentUniversalHash) && var6.containsKey(var15)) {
                     long var17 = var6.getLongValue(var15);
                     if (var17 > 0L
                        && var2 >= var17
                        && var2 - var17 >= RETIRED_RETENTION_MILLIS
                        && var16 != null
                        && var16.matches("[a-zA-Z0-9_]+")
                        && var13.getFileName().toString().equals(var16 + ".json")) {
                        Path var19 = this.cacheDir.resolve(var16 + ".zip");
                        if (!this.liveArtifacts.contains(var19)
                           && !this.derivations.containsKey(var16)
                           && !Files.isSymbolicLink(var19)
                           && (!Files.exists(var19) || ResourcePackCompressor.digestFile(var19, "SHA-256").equals(var14.getString("sha256")))) {
                           Files.deleteIfExists(var19);
                           Files.deleteIfExists(var13);
                        }
                     }
                  }
               } catch (IOException | RuntimeException var21) {
                  var9 = true;
               }
            }
         }
      }

      if (var9) {
         throw new IOException("Some obsolete manifests could not be cleaned");
      }

      if (var6.keySet().retainAll(var10)) {
         VanillaAssetCache.writeCacheFile(var4, JSON.toJSONBytes(var5));
      }
   }

   @Override
   public void close() {
      this.executor.shutdownNow();
      this.derivations.values().forEach(var0 -> var0.cancel(false));
      this.derivations.clear();

      try {
         if (!this.executor.awaitTermination(10L, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Client pack derivation did not stop within 10 seconds");
         }
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
      }
   }
}
