package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class VanillaAssetCache {
   public static final String bxx = "assets-list.mojang";
   static final URI bxy = URI.create("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json");
   private static final Pattern bxz = Pattern.compile("^(?:[0-9]+(?:\\.[0-9]+)+(?:-(?:pre|rc)[0-9]+)?|[0-9]{2}w[0-9]{2}[a-z])");
   private static final long bxA = 4194304L;
   private static final long bxB = 8388608L;
   private static final long bxC = 1073741824L;
   private static final long bxD = 67108864L;
   private static final long bxE = 268435456L;
   private static final long bxF = TimeUnit.MINUTES.toNanos(5L);
   private static final byte[] bxG = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10};
   private static final AssetFetcher bxH = new DefaultAssetFetcher();
   private static final AssetDownloader bxI = VanillaAssetCache::fetchRange;
   private static final Map bxJ = new HashMap();

   private VanillaAssetCache() {
   }

   static synchronized RemoteAssetRef loadVersionAsset(Path var0, String var1) {
      return loadVersionAssetWith(var0, var1, bxH);
   }

   static synchronized RemoteAssetRef loadVersionAssetWith(Path var0, String var1, AssetFetcher var2) {
      Path var3 = requireMojangDir(var0);
      if (var1 != null) {
         Matcher var4 = bxz.matcher(var1);
         if (var4.find()) {
            var1 = var4.group();
         }
      }

      requireVersion(var1);
      var1 = var1.replaceFirst("\\.build\\.[0-9]+$", "");
      Objects.requireNonNull(var2, "transport");
      Path var20 = var3.resolve("versions").resolve(var1 + ".json");
      IOException var5 = null;
      if (Files.exists(var20)) {
         try {
            return readCachedVersionRef(var20, var1);
         } catch (IOException | RuntimeException var18) {
            var5 = var18 instanceof IOException var7 ? var7 : new IOException("Cached Mojang metadata is invalid", var18);
         }
      }

      try {
         byte[] var6 = downloadToCache(var3, bxy, 4194304L, var2);
         JSONObject var23 = parseJsonPayload(var6, "Mojang version manifest");
         JSONArray var8 = var23.getJSONArray("versions");
         if (var8 == null) {
            throw new IOException("Mojang version manifest has no versions array");
         }

         JSONObject var9 = null;

         for (int var10 = 0; var10 < var8.size(); var10++) {
            JSONObject var11 = var8.getJSONObject(var10);
            if (var11 != null && var1.equals(var11.getString("id"))) {
               var9 = var11;
               break;
            }
         }

         if (var9 == null) {
            throw new UnsupportedPackVersionException(var1);
         }

         String var24 = requireSha1(var9.getString("sha1"), "version metadata");
         URI var25 = requireMojangUri(var9.getString("url"), "version metadata");
         byte[] var12 = downloadToCache(var3, var25, 8388608L, var2);
         verifySha1(var12, var24, "Mojang version metadata for " + var1);
         RemoteAssetRef var13 = parseVersionRef(var12, var1);
         JSONObject var14 = new JSONObject();
         var14.put("id", var1);
         var14.put("metadataUrl", var25.toString());
         var14.put("metadataSha1", var24);
         var14.put("metadata", Base64.getEncoder().encodeToString(var12));
         writeCacheFile(var20, JSON.toJSONBytes(var14));
         return var13;
      } catch (UnsupportedPackVersionException var15) {
         throw var15;
      } catch (IOException var16) {
         IOException var22 = new IOException("Failed to resolve official Minecraft client " + var1 + ": " + var16.getMessage(), var16);
         if (var5 != null) {
            var22.addSuppressed(var5);
         }

         throw var22;
      } catch (RuntimeException var17) {
         IOException var21 = new IOException("Failed to resolve official Minecraft client " + var1, var17);
         if (var5 != null) {
            var21.addSuppressed(var5);
         }

         throw var21;
      }
   }

   static synchronized Map loadEquipmentAssets(Path var0, RemoteAssetRef var1, Set var2) {
      return loadPngAssets(var0, var1, var2, bxH);
   }

   static synchronized Map loadEquipmentAssetsDefault(Path var0, RemoteAssetRef var1) {
      return loadEquipmentAssetsWith(var0, var1, bxI);
   }

   static synchronized Map loadEquipmentAssetsWith(Path var0, RemoteAssetRef var1, AssetDownloader var2) {
      Path var3 = requireMojangDir(var0).resolve("equipment");
      verifyClientRef(var1);
      Objects.requireNonNull(var2, "transport");
      Path var4 = var3.resolve(var1.sha1() + ".zip");
      Map var5 = readEquipmentZip(var4);
      if (var5 != null) {
         return var5;
      }

      List var6 = loadZipAssetsWith(var1, VanillaAssetCache::isEquipmentPath, var2, "").selected();
      if (var6.isEmpty()) {
         throw new IOException("Official Minecraft client has no equipment assets");
      }

      long var7 = 0L;

      for (AssetFileHeader var10 : var6) {
         requireEquipmentPath(var10.name());
         var7 += var10.uncompressedSize();
         if (var7 > 268435456L) {
            throw new IOException("Minecraft equipment assets exceed the byte limit");
         }
      }

      LinkedHashMap var17 = new LinkedHashMap();

      for (List var11 : filterPaths(var6)) {
         long var12 = ((AssetFileHeader)var11.getFirst()).localOffset();
         byte[] var14 = downloadRange(var1.url(), var12, ((AssetFileHeader)var11.getLast()).recordEnd() - 1L, var2);

         for (AssetFileHeader var16 : var11) {
            if (var17.putIfAbsent(var16.name(), readZipEntryNamed(var14, Math.toIntExact(var16.localOffset() - var12), var16, "")) != null) {
               throw new IOException("Duplicate Minecraft equipment asset: " + var16.name());
            }
         }
      }

      byte[] var19 = writeEquipmentZip(var17);
      writeCacheFile(var4, var19);
      writeCacheFile(resolveMojangDir(var4), sha1Hex(sha1Digest().digest(var19)).getBytes(StandardCharsets.US_ASCII));
      return var17;
   }

   private static boolean isEquipmentPath(String var0) {
      if (!var0.equals("assets/minecraft/atlases/blocks.json")
         && !var0.equals("assets/minecraft/atlases/items.json")
         && !var0.equals("assets/minecraft/atlases/armor_trims.json")
         && !var0.equals("assets/minecraft/textures/palettes/trim_base.png")) {
         if ((
               var0.startsWith("data/minecraft/trim_material/")
                  || var0.startsWith("data/minecraft/trim_pattern/")
                  || var0.startsWith("assets/minecraft/equipment/")
            )
            && var0.endsWith(".json")) {
            return true;
         } else {
            return !var0.endsWith(".png") && !var0.endsWith(".mcmeta")
               ? false
               : var0.startsWith("assets/minecraft/textures/entity/equipment/humanoid/")
                  || var0.startsWith("assets/minecraft/textures/entity/equipment/humanoid_leggings/")
                  || var0.startsWith("assets/minecraft/textures/trims/entity/humanoid/")
                  || var0.startsWith("assets/minecraft/textures/trims/entity/humanoid_leggings/")
                  || var0.startsWith("assets/minecraft/textures/trims/color_palettes/")
                  || var0.startsWith("assets/minecraft/textures/palettes/trim/");
         }
      } else {
         return true;
      }
   }

   private static void requireEquipmentPath(String var0) {
      requireAssetPath(var0);
      if (!isEquipmentPath(var0)) {
         throw new IOException("Unexpected Minecraft equipment asset: " + var0);
      }
   }

   private static Map readEquipmentZip(Path var0) {
      try {
         Path var1 = resolveMojangDir(var0);
         if (Files.isRegularFile(var0) && Files.isRegularFile(var1) && Files.size(var0) <= 268435456L && Files.size(var1) == 40L) {
            byte[] var2 = Files.readAllBytes(var0);
            verifySha1(var2, Files.readString(var1), "cached Minecraft equipment assets " + var0);
            LinkedHashMap var3 = new LinkedHashMap();
            long var4 = 0L;

            ZipEntry var7;
            try (ZipInputStream var6 = new ZipInputStream(new ByteArrayInputStream(var2))) {
               while ((var7 = var6.getNextEntry()) != null) {
                  if (var7.isDirectory()) {
                     throw new IOException("Unexpected equipment ZIP directory");
                  }

                  requireEquipmentPath(var7.getName());
                  if (var7.getSize() > 268435456L - var4) {
                     throw new IOException("Equipment ZIP exceeds byte limit");
                  }

                  byte[] var8 = readLimited(var6, 268435456L - var4, "cached equipment ZIP");
                  var6.closeEntry();
                  var4 += var8.length;
                  if (var3.putIfAbsent(var7.getName(), var8) != null) {
                     throw new IOException("Duplicate equipment ZIP entry");
                  }
               }
            }

            return var3.isEmpty() ? null : var3;
         } else {
            return null;
         }
      } catch (IOException | RuntimeException var11) {
         return null;
      }
   }

   private static byte[] writeEquipmentZip(Map var0) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      ZipOutputStream var2 = new ZipOutputStream(var1);

      try {
         for (Entry var4 : var0.entrySet()) {
            requireEquipmentPath((String)var4.getKey());
            var2.putNextEntry(new ZipEntry((String)var4.getKey()));
            var2.write((byte[])var4.getValue());
            var2.closeEntry();
         }
      } catch (Throwable var6) {
         try {
            var2.close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      var2.close();
      if (var1.size() > 268435456L) {
         throw new IOException("Equipment ZIP exceeds byte limit");
      } else {
         return var1.toByteArray();
      }
   }

   static synchronized Map loadPngAssets(Path var0, RemoteAssetRef var1, Set var2, AssetFetcher var3) {
      Path var4 = requireMojangDir(var0);
      verifyClientRef(var1);
      Objects.requireNonNull(var2, "resourcePaths");
      Objects.requireNonNull(var3, "transport");

      for (String var6 : var2) {
         requirePngPath(var6);
      }

      if (var2.isEmpty()) {
         return Map.of();
      }

      Path var14 = var4.resolve("pngs").resolve(var1.sha1());
      LinkedHashMap var15 = new LinkedHashMap();
      LinkedHashSet var7 = new LinkedHashSet();
      long var8 = 0L;

      for (String var11 : var2) {
         byte[] var12 = readCachedBytes(var14.resolve(var11));
         if (var12 == null) {
            var7.add(var11);
         } else {
            var8 += var12.length;
            if (var8 > 268435456L) {
               throw new IOException("Requested Minecraft PNGs exceed the byte limit");
            }

            var15.put(var11, var12);
         }
      }

      if (!var7.isEmpty()) {
         Map var16 = downloadPngAssets(var1, var7, var3::fetchBytes);

         for (byte[] var19 : var16.values()) {
            var8 += var19.length;
            if (var8 > 268435456L) {
               throw new IOException("Requested Minecraft PNGs exceed the byte limit");
            }
         }

         for (Entry var20 : var16.entrySet()) {
            Path var13 = var14.resolve((String)var20.getKey());
            writeCacheFile(var13, (byte[])var20.getValue());
            writeCacheFile(resolveMojangDir(var13), sha1Hex(sha1Digest().digest((byte[])var20.getValue())).getBytes(StandardCharsets.US_ASCII));
            var15.put((String)var20.getKey(), (byte[])var20.getValue());
         }
      }

      return var15;
   }

   private static Path resolveMojangDir(Path var0) {
      return var0.resolveSibling(var0.getFileName() + ".sha1");
   }

   private static byte[] readCachedBytes(Path var0) {
      Path var1 = resolveMojangDir(var0);

      try {
         if (Files.isRegularFile(var0) && Files.isRegularFile(var1) && Files.size(var0) <= 67108864L && Files.size(var1) == 40L) {
            byte[] var2 = Files.readAllBytes(var0);
            verifyPngHeader(var2, var0.toString());
            verifySha1(var2, Files.readString(var1), "cached Minecraft PNG " + var0);
            return var2;
         } else {
            return null;
         }
      } catch (IOException var3) {
         return null;
      }
   }

   private static Map downloadPngAssets(RemoteAssetRef var0, Set var1, AssetDownloader var2) {
      List var3 = loadZipAssets(var0, var1::contains, var2).selected();
      if (var3.size() != var1.size()) {
         throw new IOException("Minecraft client is missing requested PNG");
      }

      long var4 = 0L;

      for (AssetFileHeader var7 : var3) {
         if (var7.uncompressedSize() > 67108864L) {
            throw new IOException("Minecraft PNG exceeds the byte limit: " + var7.name());
         }

         var4 += var7.uncompressedSize();
         if (var4 > 268435456L) {
            throw new IOException("Requested Minecraft PNGs exceed the byte limit");
         }
      }

      LinkedHashMap var16 = new LinkedHashMap();

      for (List var8 : filterPaths(var3)) {
         long var9 = ((AssetFileHeader)var8.getFirst()).localOffset();
         byte[] var11 = downloadRange(var0.url(), var9, ((AssetFileHeader)var8.getLast()).recordEnd() - 1L, var2);

         for (AssetFileHeader var13 : var8) {
            byte[] var14 = readZipEntry(var11, Math.toIntExact(var13.localOffset() - var9), var13);
            String var15 = "assets/minecraft/" + var13.name();
            verifyPngHeader(var14, var15);
            var16.put(var15, var14);
         }
      }

      return var16;
   }

   private static List filterPaths(List var0) {
      var0.sort(Comparator.comparingLong(AssetFileHeader::localOffset));
      ArrayList var1 = new ArrayList();
      int var2 = 0;

      while (var2 < var0.size()) {
         int var3 = var2 + 1;

         while (var3 < var0.size() && ((AssetFileHeader)var0.get(var3)).localOffset() == ((AssetFileHeader)var0.get(var3 - 1)).recordEnd()) {
            var3++;
         }

         var1.add(List.copyOf(var0.subList(var2, var3)));
         var2 = var3;
      }

      return var1;
   }

   static void writeCacheFile(Path var0, byte[] var1) {
      Objects.requireNonNull(var0, "target");
      Objects.requireNonNull(var1, "bytes");
      Path var2 = var0.toAbsolutePath().normalize().getParent();
      if (var2 == null) {
         throw new IOException("Cache target has no parent: " + var0);
      }

      Files.createDirectories(var2);
      Path var3 = Files.createTempFile(var2, "." + var0.getFileName() + "-", ".tmp");

      try {
         Files.write(var3, var1, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
         atomicMove(var3, var0.toAbsolutePath().normalize());
      } finally {
         Files.deleteIfExists(var3);
      }
   }

   public static synchronized AssetFileStamp ensureAsset(Path var0, String var1, Path var2) {
      return ensureAssetFlagged(var0, var1, var2, false);
   }

   public static synchronized AssetFileStamp ensureAssetFlagged(Path var0, String var1, Path var2, boolean var3) {
      return ensureAssetProcessed(var0, var1, var2, var3, var1x -> var2.resolve(var1x));
   }

   public static synchronized AssetFileStamp ensureAssetProcessed(Path var0, String var1, Path var2, boolean var3, Function var4) {
      RemoteAssetRef var5;
      try {
         var5 = loadVersionAsset(var0, var1);
      } catch (UnsupportedPackVersionException var7) {
         throw var7;
      } catch (IOException var8) {
         throw new AssetDownloadException(var8);
      }

      return downloadAssetFileProcessed(var5, var2, bxI, var3, var4);
   }

   static AssetFileStamp downloadAssetFile(RemoteAssetRef var0, Path var1, AssetDownloader var2) {
      return downloadAssetFileFlagged(var0, var1, var2, false);
   }

   static AssetFileStamp downloadAssetFileFlagged(RemoteAssetRef var0, Path var1, AssetDownloader var2, boolean var3) {
      return downloadAssetFileProcessed(var0, var1, var2, var3, var1x -> var1.resolve(var1x));
   }

   static AssetFileStamp downloadAssetFileProcessed(RemoteAssetRef var0, Path var1, AssetDownloader var2, boolean var3, Function var4) {
      long var5 = System.nanoTime();
      verifyClientRef(var0);
      Objects.requireNonNull(var1, "destination");
      Objects.requireNonNull(var2, "transport");
      Objects.requireNonNull(var4, "cachedFiles");
      AssetCacheStamp var7 = var3 ? null : readStamp(var1);
      if (var7 != null) {
         return new AssetFileStamp(0, 0L, 0, var7.files(), var7.bytes(), var7.names(), TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - var5));
      }

      CountingAssetDownloader var8 = new CountingAssetDownloader(var2);

      PackFileGroups var9;
      try {
         var9 = loadZipAssets(var0, VanillaAssetCache::isAllowedAssetPath, var8);
      } catch (IOException var58) {
         throw new AssetDownloadException(var58);
      }

      List var10 = var9.selected();
      if (var10.isEmpty()) {
         throw new IOException("Official Minecraft client has no required resource-pack assets");
      }

      long var11 = 0L;

      for (AssetFileHeader var14 : var10) {
         var11 += var14.uncompressedSize();
         if (var11 > 268435456L) {
            throw new IOException("Required Minecraft assets exceed the 268435456 byte limit");
         }
      }

      Path var62 = var1.resolve("assets/minecraft").toAbsolutePath().normalize();
      ArrayList var63 = new ArrayList();
      ArrayList var15 = new ArrayList();
      byte[] var16 = new byte[32768];

      for (AssetFileHeader var18 : var10) {
         Path var19 = var62.resolve(var18.name()).normalize();
         if (!var19.startsWith(var62)) {
            throw new IOException("Unsafe Minecraft asset path: " + var18.name());
         }

         if (!verifyEntry(var19, var18, var16) && !verifyEntry((Path)var4.apply("assets/minecraft/" + var18.name()), var18, var16)) {
            var15.add(var18);
         }
      }

      List var64 = filterPaths(var15);
      ExecutorService var65 = Executors.newFixedThreadPool(4);

      try {
         ArrayList var66 = new ArrayList();

         for (List var21 : var64) {
            var66.add(var65.submit(() -> downloadMissingAssets(var0.url(), var62, var21, var8)));
         }

         for (Future var70 : var66) {
            try {
               var63.addAll((Collection)var70.get());
            } catch (ExecutionException var57) {
               throw new IOException("Failed to extract official Minecraft assets", var57.getCause());
            }
         }
      } catch (InterruptedException var60) {
         Thread.currentThread().interrupt();
         throw new InterruptedIOException("Interrupted extracting official Minecraft assets");
      } finally {
         var65.shutdownNow();
         boolean var25 = Thread.interrupted();

         try {
            while (!var65.isTerminated()) {
               try {
                  var65.awaitTermination(1L, TimeUnit.SECONDS);
               } catch (InterruptedException var55) {
                  var25 = true;
               }
            }
         } finally {
            if (var25) {
               Thread.currentThread().interrupt();
            }
         }
      }

      List var67 = var9.assetNames();
      Collections.sort(var67);
      byte[] var69 = String.join("\n", var67).concat("\n").getBytes(StandardCharsets.UTF_8);

      try {
         String var71 = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var69));
         writeCacheFile(var1.resolve("assets-list.mojang"), (var0.version() + "\n" + var0.sha1() + "\n" + var71 + "\n").getBytes(StandardCharsets.UTF_8));
      } catch (NoSuchAlgorithmException var56) {
         throw new AssertionError(var56);
      }

      writeCacheFile(var1.resolve("assets-list.txt"), var69);
      return new AssetFileStamp(
         var8.byf.get(), var8.byi.sum(), var8.byh.get(), var10.size(), var11, var67.size(), TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - var5)
      );
   }

   public static synchronized void fetchVanillaAsset(Path var0, String var1, String var2, Path var3) {
      RemoteAssetRef var4 = loadVersionAsset(var0, var1);
      repairAsset(var4, var2, var3, bxI, groupAssets(var4));
   }

   static void downloadVanillaAsset(RemoteAssetRef var0, String var1, Path var2, AssetDownloader var3) {
      repairAsset(var0, var1, var2, var3, null);
   }

   private static PackFileGroups groupAssets(RemoteAssetRef var0) {
      PackFileGroups var1 = (PackFileGroups)bxJ.get(var0);
      if (var1 == null) {
         var1 = loadZipAssets(var0, var0x -> var0x.startsWith("assets/minecraft/"), bxI);
         bxJ.put(var0, var1);
      }

      return var1;
   }

   public static synchronized void fetchCatalog(Path var0, String var1, Path var2) {
      RemoteAssetRef var3;
      PackFileGroups var4;
      try {
         var3 = loadVersionAsset(var0, var1);
         var4 = groupAssets(var3);
      } catch (UnsupportedPackVersionException var6) {
         throw var6;
      } catch (IOException var7) {
         throw new AssetDownloadException(var7);
      }

      writeGroups(var3, var2, var4);
   }

   static void downloadRemoteAsset(RemoteAssetRef var0, Path var1, AssetDownloader var2) {
      writeGroups(var0, var1, loadZipAssets(var0, var0x -> false, var2));
   }

   private static void writeGroups(RemoteAssetRef var0, Path var1, PackFileGroups var2) {
      writeAssetList(var0, var1, var2.assetNames());
   }

   private static void writeAssetList(RemoteAssetRef var0, Path var1, List var2) {
      ArrayList var3 = new ArrayList(var2);
      Collections.sort(var3);
      byte[] var4 = String.join("\n", var3).concat("\n").getBytes(StandardCharsets.UTF_8);

      try {
         String var5 = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var4));
         writeCacheFile(var1.resolve("assets-list.mojang"), (var0.version() + "\n" + var0.sha1() + "\n" + var5 + "\n").getBytes(StandardCharsets.UTF_8));
      } catch (NoSuchAlgorithmException var6) {
         throw new AssertionError(var6);
      }

      writeCacheFile(var1.resolve("assets-list.txt"), var4);
   }

   private static void repairAsset(RemoteAssetRef var0, String var1, Path var2, AssetDownloader var3, PackFileGroups var4) {
      verifyClientRef(var0);
      boolean var5 = var1.equals("assets-list.txt") || var1.equals("assets-list.mojang");
      if (var5 || var1.startsWith("assets/minecraft/") && !var1.contains("..") && !var1.contains("\\")) {
         PackFileGroups var6 = var4 == null ? loadZipAssets(var0, var2x -> !var5 && var2x.equals(var1), var3) : var4;
         if (var5) {
            ArrayList var7 = new ArrayList(var6.assetNames());
            Collections.sort(var7);
            byte[] var8 = String.join("\n", var7).concat("\n").getBytes(StandardCharsets.UTF_8);
            if (var1.equals("assets-list.mojang")) {
               try {
                  String var9 = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var8));
                  var8 = (var0.version() + "\n" + var0.sha1() + "\n" + var9 + "\n").getBytes(StandardCharsets.UTF_8);
               } catch (NoSuchAlgorithmException var10) {
                  throw new AssertionError(var10);
               }
            }

            writeCacheFile(var2.resolve(var1), var8);
         } else {
            List var11 = var6.selected().stream().filter(var1x -> ("assets/minecraft/" + var1x.name()).equals(var1)).toList();
            if (var11.size() != 1) {
               throw new IOException("Official Minecraft asset unavailable: " + var1);
            }

            AssetFileHeader var12 = (AssetFileHeader)var11.getFirst();
            if (var12.uncompressedSize() > 268435456L) {
               throw new IOException("Repair asset too large");
            }

            downloadMissingAssets(var0.url(), var2.resolve("assets/minecraft").toAbsolutePath().normalize(), List.of(var12), var3);
         }
      } else {
         throw new IOException("Unsafe vanilla repair path: " + var1);
      }
   }

   private static List downloadMissingAssets(URI var0, Path var1, List var2, AssetDownloader var3) {
      long var4 = ((AssetFileHeader)var2.getFirst()).localOffset();
      long var6 = ((AssetFileHeader)var2.getLast()).recordEnd();
      byte[] var8 = downloadRange(var0, var4, var6 - 1L, var3);
      ArrayList var9 = new ArrayList();

      for (AssetFileHeader var11 : var2) {
         Path var12 = var1.resolve(var11.name()).normalize();
         if (!var12.startsWith(var1)) {
            throw new IOException("Unsafe Minecraft asset path: " + var11.name());
         }

         Files.createDirectories(var12.getParent());
         int var13 = Math.toIntExact(var11.localOffset() - var4);
         byte[] var14 = readZipEntry(var8, var13, var11);
         writeCacheFile(var12, var14);
         var9.add(var11.name());
      }

      return var9;
   }

   private static boolean verifyEntry(Path var0, AssetFileHeader var1, byte[] var2) {
      if (var0 != null && Files.isRegularFile(var0) && Files.size(var0) == var1.uncompressedSize()) {
         CRC32 var3 = new CRC32();

         int var5;
         try (InputStream var4 = Files.newInputStream(var0)) {
            while ((var5 = var4.read(var2)) != -1) {
               checkInterrupted();
               var3.update(var2, 0, var5);
            }
         }

         return var3.getValue() == var1.crc();
      } else {
         return false;
      }
   }

   private static boolean isAllowedAssetPath(String var0) {
      return var0.startsWith("assets/minecraft/models/")
         || var0.startsWith("assets/minecraft/items/")
         || var0.startsWith("assets/minecraft/blockstates/")
         || var0.startsWith("assets/minecraft/shaders/");
   }

   private static AssetCacheStamp readStamp(Path var0) {
      Path var1 = var0.resolve("assets-list.txt");
      Path var2 = var0.resolve("assets/minecraft").toAbsolutePath().normalize();
      if (!Files.isRegularFile(var1)) {
         return null;
      }

      int var3 = 0;
      int var4 = 0;
      long var5 = 0L;
      boolean var7 = false;
      boolean var8 = false;
      boolean var9 = false;
      boolean var10 = false;

      try {
         for (String var12 : Files.readAllLines(var1)) {
            String var13 = var12.trim();
            if (!var13.isEmpty() && !var13.startsWith("#")) {
               requireAssetPath(var13);
               var4++;
               var7 |= var13.startsWith("models/");
               var8 |= var13.startsWith("blockstates/");
               var9 |= var13.startsWith("shaders/");
               var10 |= var13.startsWith("textures/");
               if (isAllowedAssetPath("assets/minecraft/" + var13)) {
                  Path var14 = var2.resolve(var13).normalize();
                  if (!var14.startsWith(var2) || !Files.isRegularFile(var14)) {
                     return null;
                  }

                  var3++;
                  var5 += Files.size(var14);
                  if (var5 > 268435456L) {
                     return null;
                  }
               }
            }
         }

         return var7 && var8 && var9 && var10 && var3 > 0 ? new AssetCacheStamp(var3, var5, var4) : null;
      } catch (IOException | RuntimeException var15) {
         return null;
      }
   }

   private static PackFileGroups loadZipAssets(RemoteAssetRef var0, Predicate var1, AssetDownloader var2) {
      return loadZipAssetsWith(var0, var1, var2, "assets/minecraft/");
   }

   private static PackFileGroups loadZipAssetsWith(RemoteAssetRef var0, Predicate var1, AssetDownloader var2, String var3) {
      URI var4 = var0.url();
      byte[] var5 = downloadRange(var4, Math.max(0L, var0.size() - 65557L), var0.size() - 1L, var2);
      int var6 = -1;

      for (int var7 = var5.length - 22; var7 >= 0; var7--) {
         if (readU32(var5, var7) == 101010256L && var7 + 22 + readU16(var5, var7 + 20) == var5.length) {
            var6 = var7;
            break;
         }
      }

      if (var6 < 0) {
         throw new IOException("Official Minecraft client ZIP has no end record");
      }

      int var34 = readU16(var5, var6 + 8);
      int var8 = readU16(var5, var6 + 10);
      long var9 = readU32(var5, var6 + 12);
      long var11 = readU32(var5, var6 + 16);
      if (readU16(var5, var6 + 4) == 0 && readU16(var5, var6 + 6) == 0 && var34 == var8 && var8 != 65535 && var9 != 4294967295L && var11 != 4294967295L) {
         if (var9 > 0L && var9 <= 268435456L && var11 + var9 <= var0.size() - 22L) {
            byte[] var13 = downloadRange(var4, var11, var11 + var9 - 1L, var2);
            ArrayList var14 = new ArrayList(var8);
            ArrayList var15 = new ArrayList();
            int var16 = 0;

            while (var16 < var13.length) {
               if (var16 > var13.length - 46) {
                  throw new IOException("Truncated Minecraft client ZIP directory");
               }

               if (readU32(var13, var16) != 33639248L) {
                  throw new IOException("Invalid Minecraft client ZIP directory");
               }

               int var17 = readU16(var13, var16 + 8);
               int var18 = readU16(var13, var16 + 10);
               int var19 = readU16(var13, var16 + 28);
               int var20 = readU16(var13, var16 + 30);
               int var21 = readU16(var13, var16 + 32);
               long var22 = readU32(var13, var16 + 16);
               long var24 = readU32(var13, var16 + 20);
               long var26 = readU32(var13, var16 + 24);
               long var28 = readU32(var13, var16 + 42);
               long var30 = var16 + 46L + var19 + var20 + var21;
               if (var30 > var13.length) {
                  throw new IOException("Truncated Minecraft client ZIP directory entry");
               }

               String var32 = new String(var13, var16 + 46, var19, StandardCharsets.UTF_8);
               if ((var17 & 1) != 0) {
                  throw new IOException("Encrypted Minecraft client ZIP entry: " + var32);
               }

               var14.add(new AssetMetadata(var32, var28, var24, var18, var26, var22));
               if (!var32.endsWith("/") && var32.startsWith("assets/minecraft/")) {
                  String var33 = var32.substring("assets/minecraft/".length());
                  requireAssetPath(var33);
                  var15.add(var33);
               }

               var16 = (int)var30;
            }

            if (var14.size() != var8) {
               throw new IOException("Minecraft client ZIP directory entry count mismatch");
            }

            var14.sort(Comparator.comparingLong(AssetMetadata::localOffset));
            ArrayList var35 = new ArrayList();

            for (int var36 = 0; var36 < var14.size(); var36++) {
               AssetMetadata var37 = (AssetMetadata)var14.get(var36);
               long var38 = var36 + 1 < var14.size() ? ((AssetMetadata)var14.get(var36 + 1)).localOffset() : var11;
               if (var37.localOffset() < 0L || var38 <= var37.localOffset() || var38 > var11) {
                  throw new IOException("Invalid Minecraft client ZIP entry bounds: " + var37.fullName());
               }

               if (!var37.fullName().endsWith("/") && var1.test(var37.fullName())) {
                  if (!var37.fullName().startsWith(var3)) {
                     throw new IOException("Invalid selected Minecraft asset path: " + var37.fullName());
                  }

                  requireAssetPath(var37.fullName());
                  var35.add(
                     new AssetFileHeader(
                        var37.fullName().substring(var3.length()),
                        var37.localOffset(),
                        var38,
                        var37.compressedSize(),
                        var37.method(),
                        var37.uncompressedSize(),
                        var37.crc()
                     )
                  );
               }
            }

            return new PackFileGroups(var35, var15);
         } else {
            throw new IOException("Invalid Minecraft client ZIP directory size: " + var9);
         }
      } else {
         throw new IOException("Unsupported multi-disk or ZIP64 Minecraft client");
      }
   }

   private static byte[] readZipEntry(byte[] var0, int var1, AssetFileHeader var2) {
      return readZipEntryNamed(var0, var1, var2, "assets/minecraft/");
   }

   private static byte[] readZipEntryNamed(byte[] var0, int var1, AssetFileHeader var2, String var3) {
      if (var1 < 0 || var1 > var0.length - 30 || readU32(var0, var1) != 67324752L) {
         throw new IOException("Invalid Minecraft client ZIP entry: " + var2.name());
      }

      if (readU16(var0, var1 + 8) != var2.method()) {
         throw new IOException("Minecraft ZIP compression mismatch: " + var2.name());
      }

      int var4 = readU16(var0, var1 + 26);
      int var5 = readU16(var0, var1 + 28);
      long var6 = var1 + 30L + var4 + var5;
      long var8 = var6 + var2.compressedSize();
      if (var6 <= 2147483647L && var8 <= var0.length) {
         String var10 = new String(var0, var1 + 30, var4, StandardCharsets.UTF_8);
         if (!var10.equals(var3 + var2.name())) {
            throw new IOException("Minecraft ZIP entry name mismatch: " + var2.name());
         } else {
            return inflateEntry(Arrays.copyOfRange(var0, (int)var6, (int)var8), var2.method(), var2.uncompressedSize(), var2.crc(), var2.name());
         }
      } else {
         throw new IOException("Minecraft ZIP entry exceeds requested range: " + var2.name());
      }
   }

   private static void requireAssetPath(String var0) {
      if (!var0.isEmpty() && !var0.startsWith("/") && var0.indexOf(92) < 0 && !var0.contains("//")) {
         for (String var4 : var0.split("/")) {
            if (var4.isEmpty() || var4.equals(".") || var4.equals("..")) {
               throw new IOException("Invalid Minecraft asset path: " + var0);
            }
         }
      } else {
         throw new IOException("Invalid Minecraft asset path: " + var0);
      }
   }

   private static byte[] inflateEntry(byte[] var0, int var1, long var2, long var4, String var6) {
      if (var2 < 0L || var2 > 268435456L) {
         throw new IOException("Minecraft asset is too large: " + var6);
      }

      if (var1 == 0) {
         if (var0.length != var2) {
            throw new IOException("Invalid Minecraft ZIP entry size: " + var6);
         } else {
            return verifyCrc(var0, var4, var6);
         }
      } else {
         if (var1 != 8) {
            throw new IOException("Unsupported Minecraft ZIP compression: " + var1);
         }

         try (InflaterInputStream var7 = new InflaterInputStream(new ByteArrayInputStream(var0), new Inflater(true))) {
            byte[] var8 = readLimited(var7, var2, "Minecraft ZIP entry " + var6);
            if (var8.length != var2) {
               throw new IOException("Invalid Minecraft ZIP entry size: " + var6);
            } else {
               return verifyCrc(var8, var4, var6);
            }
         }
      }
   }

   private static byte[] verifyCrc(byte[] var0, long var1, String var3) {
      CRC32 var4 = new CRC32();
      var4.update(var0);
      if (var4.getValue() != var1) {
         throw new IOException("Invalid Minecraft ZIP entry CRC: " + var3);
      } else {
         return var0;
      }
   }

   private static byte[] downloadRange(URI var0, long var1, long var3, AssetDownloader var5) {
      if (var1 >= 0L && var3 >= var1 && var3 - var1 + 1L <= 268435456L) {
         byte[] var6 = var5.download(var0, "bytes=" + var1 + "-" + var3, var1, var3);
         if (var6.length != var3 - var1 + 1L) {
            throw new IOException("Minecraft client returned truncated byte range");
         } else {
            return var6;
         }
      } else {
         throw new IOException("Invalid Minecraft client byte range: " + var1 + "-" + var3);
      }
   }

   private static byte[] fetchRange(URI var0, String var1, long var2, long var4) {
      HttpURLConnection var6 = (HttpURLConnection)requireMojangUri(var0.toString(), "Minecraft client").toURL().openConnection();
      var6.setConnectTimeout(15000);
      var6.setReadTimeout(60000);
      var6.setRequestProperty("Range", var1);

      try {
         if (var6.getResponseCode() != 206) {
            throw new IOException("Official Minecraft client does not support byte ranges");
         }

         String var7 = var6.getHeaderField("Content-Range");
         if (var7 != null && var7.matches("bytes [0-9]+-[0-9]+/[0-9]+")) {
            String[] var8 = var7.substring(6).split("[-/]");

            long var9;
            long var11;
            long var13;
            try {
               var9 = Long.parseLong(var8[0]);
               var11 = Long.parseLong(var8[1]);
               var13 = Long.parseLong(var8[2]);
            } catch (NumberFormatException var21) {
               throw new IOException("Official Minecraft client returned invalid Content-Range", var21);
            }

            if (var11 >= var9 && var13 > var11 && (var2 < 0L || var9 == var2 && var11 == var4)) {
               if (var2 < 0L) {
                  long var15 = Long.parseLong(var1.substring("bytes=-".length()));
                  if (var11 != var13 - 1L || var11 - var9 + 1L > var15) {
                     throw new IOException("Official Minecraft client returned unexpected suffix range");
                  }
               }

               try (InputStream var24 = var6.getInputStream()) {
                  long var16 = var2 >= 0L ? var4 - var2 + 1L : Long.parseLong(var1.substring("bytes=-".length()));
                  byte[] var18 = readLimited(var24, var16, "Minecraft client range");
                  if (var18.length != var11 - var9 + 1L) {
                     throw new IOException("Official Minecraft client returned truncated byte range");
                  } else {
                     return var18;
                  }
               }
            } else {
               throw new IOException("Official Minecraft client returned unexpected byte range");
            }
         } else {
            throw new IOException("Official Minecraft client returned invalid Content-Range");
         }
      } catch (IOException | RuntimeException | Error var23) {
         var6.disconnect();
         throw var23;
      }
   }

   private static int readU16(byte[] var0, int var1) {
      return var0[var1] & 0xFF | (var0[var1 + 1] & 0xFF) << 8;
   }

   private static long readU32(byte[] var0, int var1) {
      return (readU16(var0, var1) | (long)readU16(var0, var1 + 2) << 16) & 4294967295L;
   }

   private static RemoteAssetRef readCachedVersionRef(Path var0, String var1) {
      long var2 = Files.size(var0);
      if (var2 > 16777216L) {
         throw new IOException("Cached Mojang metadata wrapper is too large: " + var0);
      }

      checkInterrupted();
      byte[] var4 = Files.readAllBytes(var0);
      JSONObject var5 = parseJsonPayload(var4, "cached Mojang metadata");
      if (!var1.equals(var5.getString("id"))) {
         throw new IOException("Cached Mojang metadata belongs to another version");
      }

      requireMojangUri(var5.getString("metadataUrl"), "cached version metadata");
      String var6 = requireSha1(var5.getString("metadataSha1"), "cached version metadata");
      String var7 = var5.getString("metadata");
      if (var7 == null) {
         throw new IOException("Cached Mojang metadata has no verified JSON payload");
      }

      byte[] var8;
      try {
         var8 = Base64.getDecoder().decode(var7);
      } catch (IllegalArgumentException var10) {
         throw new IOException("Cached Mojang metadata payload is not valid Base64", var10);
      }

      if (var8.length > 8388608L) {
         throw new IOException("Cached Mojang version metadata exceeds the size limit");
      }

      verifySha1(var8, var6, "cached Mojang version metadata");
      return parseVersionRef(var8, var1);
   }

   private static RemoteAssetRef parseVersionRef(byte[] var0, String var1) {
      JSONObject var2 = parseJsonPayload(var0, "Mojang version metadata");
      if (!var1.equals(var2.getString("id"))) {
         throw new IOException("Mojang version metadata id does not match " + var1);
      }

      JSONObject var3 = var2.getJSONObject("downloads");
      JSONObject var4 = var3 == null ? null : var3.getJSONObject("client");
      if (var4 == null) {
         throw new IOException("Mojang version metadata has no client download");
      }

      RemoteAssetRef var5 = new RemoteAssetRef(
         var1, requireSha1(var4.getString("sha1"), "Minecraft client"), var4.getLongValue("size"), requireMojangUri(var4.getString("url"), "Minecraft client")
      );
      verifyClientRef(var5);
      return var5;
   }

   private static JSONObject parseJsonPayload(byte[] var0, String var1) {
      try {
         JSONObject var2 = JSON.parseObject(var0);
         if (var2 == null) {
            throw new IOException(var1 + " is empty");
         } else {
            return var2;
         }
      } catch (RuntimeException var3) {
         throw new IOException(var1 + " is not valid JSON", var3);
      }
   }

   private static byte[] downloadToCache(Path var0, URI var1, long var2, AssetFetcher var4) {
      Path var5 = resolveCacheFile(var0, ".metadata-", ".part");

      try {
         var4.fetchToFile(var1, var5, var2);
         long var6 = Files.size(var5);
         if (var6 > var2) {
            throw new IOException("Download exceeds the " + var2 + " byte limit: " + var1);
         } else {
            return Files.readAllBytes(var5);
         }
      } finally {
         Files.deleteIfExists(var5);
      }
   }

   private static void downloadFollowingRedirects(URI var0, Path var1, long var2) {
      URI var4 = requireMojangUri(var0 == null ? null : var0.toString(), "download");
      long var5 = System.nanoTime() + bxF;

      for (int var7 = 0; var7 <= 5; var7++) {
         checkInterrupted();
         checkDeadline(var5, var0.toString());
         HttpURLConnection var8 = (HttpURLConnection)var4.toURL().openConnection();
         var8.setConnectTimeout(15000);
         var8.setReadTimeout(60000);
         var8.setInstanceFollowRedirects(false);
         var8.setRequestProperty("User-Agent", "ItemsAdder/MojangClientAssets");

         try {
            int var9 = var8.getResponseCode();
            checkDeadline(var5, var0.toString());
            if (var9 < 300 || var9 >= 400) {
               if (var9 != 200) {
                  throw new IOException("HTTP " + var9 + " downloading " + var4);
               }

               long var21 = var8.getContentLengthLong();
               if (var21 > var2) {
                  throw new IOException("Download exceeds the " + var2 + " byte limit: " + var4);
               }

               try (
                  InputStream var12 = var8.getInputStream();
                  OutputStream var13 = Files.newOutputStream(var1, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
               ) {
                  copyLimitedWithOffset(var12, var13, var2, "download " + var4, var5);
               }

               return;
            }

            if (var7 == 5) {
               throw new IOException("Too many redirects downloading " + var0);
            }

            String var10 = var8.getHeaderField("Location");
            if (var10 == null) {
               throw new IOException("Redirect has no Location downloading " + var0);
            }

            var4 = requireMojangUri(var4.resolve(var10).toString(), "redirect");
            var8.disconnect();
         } catch (IOException | RuntimeException | Error var20) {
            var8.disconnect();
            throw var20;
         }
      }

      throw new IOException("Too many redirects downloading " + var0);
   }

   private static void copyLimited(InputStream var0, OutputStream var1, long var2, String var4) {
      copyLimitedWithOffset(var0, var1, var2, var4, Long.MAX_VALUE);
   }

   private static void copyLimitedWithOffset(InputStream var0, OutputStream var1, long var2, String var4, long var5) {
      byte[] var7 = new byte[8192];
      long var8 = 0L;

      int var10;
      while ((var10 = var0.read(var7)) != -1) {
         checkInterrupted();
         if (var5 != Long.MAX_VALUE) {
            checkDeadline(var5, var4);
         }

         var8 += var10;
         if (var8 > var2) {
            throw new IOException(var4 + " exceeds the " + var2 + " byte limit");
         }

         var1.write(var7, 0, var10);
      }
   }

   private static byte[] readLimited(InputStream var0, long var1, String var3) {
      ByteArrayOutputStream var4 = new ByteArrayOutputStream();
      copyLimited(var0, var4, var1, var3);
      return var4.toByteArray();
   }

   private static void verifySha1(byte[] var0, String var1, String var2) {
      MessageDigest var3 = sha1Digest();
      var3.update(var0);
      String var4 = sha1Hex(var3.digest());
      if (!var4.equals(var1)) {
         throw new IOException(var2 + " has SHA-1 " + var4 + ", expected " + var1);
      }
   }

   private static MessageDigest sha1Digest() {
      try {
         return MessageDigest.getInstance("SHA-1");
      } catch (NoSuchAlgorithmException var1) {
         throw new IOException("JVM does not provide SHA-1", var1);
      }
   }

   private static String sha1Hex(byte[] var0) {
      StringBuilder var1 = new StringBuilder(var0.length * 2);

      for (byte var5 : var0) {
         var1.append(Character.forDigit(var5 >>> 4 & 15, 16)).append(Character.forDigit(var5 & 15, 16));
      }

      return var1.toString();
   }

   private static void verifyClientRef(RemoteAssetRef var0) {
      if (var0 == null) {
         throw new IOException("Minecraft client metadata is missing");
      }

      requireVersion(var0.version());
      requireSha1(var0.sha1(), "Minecraft client");
      if (var0.size() > 0L && var0.size() <= 1073741824L) {
         requireMojangUri(var0.url() == null ? null : var0.url().toString(), "Minecraft client");
         if (!var0.url().getPath().equals("/v1/objects/" + var0.sha1() + "/client.jar")) {
            throw new IOException("Minecraft client URL does not match its content hash");
         }
      } else {
         throw new IOException("Minecraft client size is outside the supported range: " + var0.size());
      }
   }

   private static void requireVersion(String var0) {
      if (var0 == null || !var0.matches("[0-9A-Za-z._-]{1,100}")) {
         throw new IOException("Invalid Minecraft version: " + var0);
      }
   }

   private static String requireSha1(String var0, String var1) {
      if (var0 != null && var0.matches("[0-9a-fA-F]{40}")) {
         return var0.toLowerCase(Locale.ROOT);
      } else {
         throw new IOException(var1 + " has an invalid SHA-1");
      }
   }

   private static URI requireMojangUri(String var0, String var1) {
      URI var2;
      try {
         var2 = var0 == null ? null : URI.create(var0);
      } catch (IllegalArgumentException var4) {
         throw new IOException(var1 + " has an invalid URL", var4);
      }

      if (var2 != null
         && "https".equalsIgnoreCase(var2.getScheme())
         && var2.getHost() != null
         && var2.getUserInfo() == null
         && var2.getFragment() == null
         && (var2.getHost().equalsIgnoreCase("mojang.com") || var2.getHost().toLowerCase(Locale.ROOT).endsWith(".mojang.com"))) {
         return var2;
      } else {
         throw new IOException(var1 + " URL is not official Mojang HTTPS: " + var0);
      }
   }

   private static void requirePngPath(String var0) {
      if (var0 != null
         && var0.startsWith("assets/minecraft/textures/")
         && var0.endsWith(".png")
         && var0.indexOf(92) < 0
         && !var0.startsWith("/")
         && !var0.contains("//")) {
         for (String var4 : var0.split("/")) {
            if (var4.isEmpty() || var4.equals(".") || var4.equals("..")) {
               throw new IOException("Invalid Minecraft PNG resource path: " + var0);
            }
         }
      } else {
         throw new IOException("Invalid Minecraft PNG resource path: " + var0);
      }
   }

   private static void verifyPngHeader(byte[] var0, String var1) {
      if (var0.length < bxG.length) {
         throw new IOException("Minecraft resource is not a PNG: " + var1);
      }

      for (int var2 = 0; var2 < bxG.length; var2++) {
         if (var0[var2] != bxG[var2]) {
            throw new IOException("Minecraft resource has an invalid PNG header: " + var1);
         }
      }
   }

   private static Path requireMojangDir(Path var0) {
      if (var0 == null) {
         throw new IOException("Mojang cache path is missing");
      }

      Path var1 = var0.toAbsolutePath().normalize();
      Files.createDirectories(var1);
      return var1;
   }

   private static Path resolveCacheFile(Path var0, String var1, String var2) {
      Files.createDirectories(var0);
      return Files.createTempFile(var0, var1, var2);
   }

   private static void atomicMove(Path var0, Path var1) {
      Files.createDirectories(var1.getParent());

      try {
         Files.move(var0, var1, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
      } catch (AtomicMoveNotSupportedException var3) {
         throw new IOException("Cache filesystem does not support atomic writes for " + var1, var3);
      }
   }

   private static void checkInterrupted() {
      if (Thread.currentThread().isInterrupted()) {
         throw new InterruptedIOException("Interrupted while reading Mojang assets");
      }
   }

   private static void checkDeadline(long var0, String var2) {
      if (System.nanoTime() - var0 >= 0L) {
         throw new IOException("Download timed out after five minutes: " + var2);
      }
   }
}
