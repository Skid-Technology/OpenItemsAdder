package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader.Feature;
import dev.lone.itemsadder.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public final class ShaderPackBuilder {
   static final String bFK = "@shaders";
   private static final LocalDateTime bFL = LocalDateTime.of(1980, 1, 1, 0, 0);
   private static final int bFM = 65536;
   private static final int bFN = 1048576;

   private ShaderPackBuilder() {
   }

   static Map buildSoundSplit(Path var0, Path var1, ShaderFingerprint var2, boolean var3, boolean var4, String var5, Consumer var6) {
      checkInterrupted();
      requireCondition(var5 != null && var5.matches("[a-f0-9]{40}"), "Invalid universal pack hash");
      String var7 = ResourcePackCompressor.digestFile(var0, "SHA-256");
      String var8 = "sound_split_1_" + var5 + "_" + var7 + "_" + var2.getHash() + "_" + var3 + "_" + var4;
      Path var9 = var1.resolve(var8 + ".json");
      LinkedHashMap var10 = new LinkedHashMap();
      ArrayList var11 = new ArrayList();
      if (Files.isRegularFile(var9)) {
         try {
            JSONObject var12 = JSON.parseObject(Files.readString(var9));
            requireCondition(var8.equals(var12.getString("key")), "Invalid split cache key");
            JSONObject var13 = var12.getJSONObject("packs");
            requireCondition(var13 != null && var13.containsKey(""), "Missing cached base pack");
            requireCondition(var12.getIntValue("pack_count") == var13.size(), "Incomplete split cache");

            for (String var15 : var13.keySet()) {
               requireCondition(
                  var15.isEmpty() || var2.getSoundPacks().containsKey(var15) || var15.equals("@shaders") && var3 && var4, "Invalid cached pack ID"
               );
               JSONObject var16 = var13.getJSONObject(var15);
               String var17 = var16.getString("sha1");
               requireCondition(var17 != null && var17.matches("[a-f0-9]{40}"), "Invalid cached pack hash");
               Path var18 = var1.resolve(var17 + ".zip");
               requireCondition(
                  Files.isRegularFile(var18)
                     && !Files.isSymbolicLink(var18)
                     && ResourcePackCompressor.digestFile(var18, "SHA-256").equals(var16.getString("sha256"))
                     && ResourcePackCompressor.digestFile(var18, "SHA-1").equals(var17),
                  "Invalid cached split pack"
               );
               var10.put(var15, var18);
            }

            var11.addAll(var12.getJSONArray("warnings").toJavaList(String.class, new Feature[0]));
         } catch (IOException | RuntimeException var19) {
            checkInterrupted();
            var10.clear();
            var11.clear();
         }
      }

      if (var10.isEmpty()) {
         var10.putAll(splitSoundPacksImpl(var0, var1, var2, var3, var4, var11::add));
         requireCondition(ResourcePackCompressor.digestFile(var0, "SHA-256").equals(var7), "Source pack changed during partitioning");
         JSONObject var20 = new JSONObject();

         for (Entry var23 : var10.entrySet()) {
            JSONObject var24 = new JSONObject();
            var24.put("sha1", ResourcePackCompressor.digestFile((Path)var23.getValue(), "SHA-1"));
            var24.put("sha256", ResourcePackCompressor.digestFile((Path)var23.getValue(), "SHA-256"));
            var20.put((String)var23.getKey(), var24);
         }

         JSONObject var22 = new JSONObject();
         var22.put("key", var8);
         var22.put("universal_sha1", var5);
         var22.put("packs", var20);
         var22.put("pack_count", var20.size());
         var22.put("warnings", var11);
         VanillaAssetCache.writeCacheFile(var9, JSON.toJSONBytes(var22));
      }

      var11.forEach(var6);
      return Collections.unmodifiableMap(var10);
   }

   public static Map splitSoundPacks(Path var0, Path var1, ShaderFingerprint var2) {
      return splitSoundPacksFiltered(var0, var1, var2, false, Log::warn);
   }

   static Map splitSoundPacksWarn(Path var0, Path var1, ShaderFingerprint var2, Consumer var3) {
      return splitSoundPacksFiltered(var0, var1, var2, false, var3);
   }

   static Map splitSoundPacksFiltered(Path var0, Path var1, ShaderFingerprint var2, boolean var3, Consumer var4) {
      return splitSoundPacksImpl(var0, var1, var2, var3, true, var4);
   }

   static Map splitSoundPacksImpl(Path var0, Path var1, ShaderFingerprint var2, boolean var3, boolean var4, Consumer var5) {
      Objects.requireNonNull(var0, "source");
      Objects.requireNonNull(var1, "outputDirectory");
      Objects.requireNonNull(var2, "settings");
      Objects.requireNonNull(var5, "warning");
      checkInterrupted();
      Files.createDirectories(var1);

      try (ZipFile var6 = new ZipFile(var0.toFile())) {
         Map var7 = indexZipEntries(var6);
         ZipEntry var8 = (ZipEntry)var7.get("pack.mcmeta");
         requireCondition(var8 != null && !var8.isDirectory(), "Missing pack.mcmeta");
         ShaderArchiveData var9 = buildArchiveData(var6, var8);
         HashMap var10 = new HashMap();
         var2.getSoundPacks().forEach((var1x, var2x) -> var2x.forEach(var2xx -> var10.put(var2xx, var1x)));
         TreeMap var11 = new TreeMap();
         var2.getSoundPacks().keySet().forEach(var1x -> var11.put(var1x, new TreeMap()));
         HashSet var12 = new HashSet();
         TreeMap var13 = new TreeMap();
         TreeMap var14 = new TreeMap();

         for (Entry var16 : var7.entrySet()) {
            String var17 = ((ZipEntry)var16.getValue()).isDirectory() ? null : stripOverlayPrefix((String)var16.getKey(), var9.bFQ);
            if (var3 && isShaderAsset(var17)) {
               var14.put((String)var16.getKey(), (ZipEntry)var16.getValue());
            } else {
               String var18 = var17 == null ? null : soundPackNamespace(var17);
               String var19 = var18 == null ? null : (String)var10.get(var18);
               if (var19 == null) {
                  var13.put((String)var16.getKey(), (ZipEntry)var16.getValue());
               } else {
                  ((Map)var11.get(var19)).put((String)var16.getKey(), (ZipEntry)var16.getValue());
                  var12.add(var18);
               }
            }
         }

         for (Entry var24 : var2.getSoundPacks().entrySet()) {
            for (String var29 : (List)var24.getValue()) {
               if (!var12.contains(var29)) {
                  var5.accept("Sound pack '" + (String)var24.getKey() + "' namespace '" + var29 + "' has no sound resources");
               }
            }
         }

         LinkedHashMap var23 = new LinkedHashMap();
         var23.put("", writeSoundPackZip(var6, var1, var13, null));

         for (Entry var28 : var11.entrySet()) {
            if (!((Map)var28.getValue()).isEmpty()) {
               var23.put((String)var28.getKey(), writeSoundPackZip(var6, var1, (Map)var28.getValue(), var9.bFO));
            }
         }

         if (var4 && !var14.isEmpty()) {
            var23.put("@shaders", writeSoundPackZip(var6, var1, var14, var9.bFP));
         }

         return Collections.unmodifiableMap(var23);
      }
   }

   private static Map indexZipEntries(ZipFile var0) {
      TreeMap var1 = new TreeMap();
      Enumeration var2 = var0.entries();

      while (var2.hasMoreElements()) {
         checkInterrupted();
         ZipEntry var3 = (ZipEntry)var2.nextElement();
         String var4 = var3.getName();
         requireCondition(isSafeZipPath(var4), "Unsafe ZIP path: " + var4);
         requireCondition(var1.put(var4, var3) == null, "Duplicate ZIP path: " + var4);
      }

      return var1;
   }

   private static ShaderArchiveData buildArchiveData(ZipFile var0, ZipEntry var1) {
      byte[] var2;
      try (InputStream var3 = var0.getInputStream(var1)) {
         var2 = var3.readNBytes(1048577);
         requireCondition(var2.length <= 1048576, "pack.mcmeta is too large");
      }

      JSONObject var15;
      try {
         var15 = JSON.parseObject(var2);
      } catch (RuntimeException var13) {
         throw new IOException("Invalid pack.mcmeta", var13);
      }

      requireCondition(var15 != null && var15.get("pack") instanceof Map, "Missing pack metadata");
      Map var4 = (Map)var15.get("pack");
      TreeMap var5 = new TreeMap();
      var5.put("description", "ItemsAdder sounds");

      for (String var7 : List.of("pack_format", "supported_formats", "min_format", "max_format")) {
         if (var4.containsKey(var7)) {
            var5.put(var7, normalizeMetadata(var4.get(var7)));
         }
      }

      requireCondition(var5.size() > 1, "Missing pack format metadata");
      HashSet var16 = new HashSet();
      Object var17 = var15.get("overlays");
      if (var17 != null) {
         requireCondition(var17 instanceof Map, "Invalid overlays metadata");
         Object var8 = ((Map)var17).get("entries");
         requireCondition(var8 instanceof List, "Invalid overlay entries");

         for (Object var10 : (List)var8) {
            requireCondition(var10 instanceof Map, "Invalid overlay entry");
            Object var11 = ((Map)var10).get("directory");
            requireCondition(
               var11 instanceof String && ((String)var11).matches("[-_a-zA-Z0-9.]+") && !Set.of(".", "..", "assets", "data").contains(var11),
               "Invalid overlay directory: " + var11
            );
            var16.add((String)var11);
         }
      }

      TreeMap var18 = new TreeMap();
      var18.put("pack", var5);
      if (var17 != null) {
         var18.put("overlays", normalizeMetadata(var17));
      }

      if (var15.containsKey("filter")) {
         Object var19 = var15.get("filter");
         requireCondition(var19 instanceof Map, "Invalid filter metadata");
         Object var21 = ((Map)var19).get("block");
         requireCondition(var21 == null || var21 instanceof List, "Invalid filter block");
         requireCondition(!(var21 instanceof List var22 && !var22.isEmpty()), "Sound pack splitting cannot preserve a non-empty filter block");
         var18.put("filter", normalizeMetadata(var19));
      }

      byte[] var20 = JSON.toJSONBytes(var18);
      var5.put("description", "ItemsAdder shaders");
      return new ShaderArchiveData(var20, JSON.toJSONBytes(var18), Set.copyOf(var16));
   }

   private static Object normalizeMetadata(Object var0) {
      if (var0 instanceof Map var5) {
         TreeMap var6 = new TreeMap();

         for (Entry var8 : var5.entrySet()) {
            requireCondition(var8.getKey() instanceof String, "Non-string pack.mcmeta key");
            var6.put((String)var8.getKey(), normalizeMetadata(var8.getValue()));
         }

         return var6;
      } else if (var0 instanceof Collection var1) {
         ArrayList var2 = new ArrayList(var1.size());

         for (Object var4 : var1) {
            var2.add(normalizeMetadata(var4));
         }

         return var2;
      } else {
         requireCondition(var0 == null || var0 instanceof String || var0 instanceof Number || var0 instanceof Boolean, "Invalid pack.mcmeta value");
         return var0;
      }
   }

   private static String stripOverlayPrefix(String var0, Set var1) {
      int var2 = var0.indexOf(47);
      return var2 > 0 && var1.contains(var0.substring(0, var2)) ? var0.substring(var2 + 1) : var0;
   }

   private static String soundPackNamespace(String var0) {
      String[] var1 = var0.split("/", 4);
      if (var1.length < 3 || !var1[0].equals("assets")) {
         return null;
      } else if (var1.length == 3 && var1[2].equals("sounds.json")) {
         return var1[1];
      } else {
         return var1.length == 4 && var1[2].equals("sounds") && !var1[3].isEmpty() ? var1[1] : null;
      }
   }

   private static boolean isShaderAsset(String var0) {
      if (var0 == null) {
         return false;
      }

      String[] var1 = var0.split("/", 4);
      return var1.length == 4
         && var1[0].equals("assets")
         && !var1[1].isEmpty()
         && !var1[3].isEmpty()
         && (var1[2].equals("shaders") || var1[2].equals("post_effect"));
   }

   private static Path writeSoundPackZip(ZipFile var0, Path var1, Map var2, byte[] var3) {
      Path var4 = Files.createTempFile(var1, ".sound-pack-", ".tmp");

      try {
         TreeSet var5 = new TreeSet(var2.keySet());
         if (var3 != null) {
            var5.add("pack.mcmeta");
         }

         byte[] var6 = new byte[65536];

         try (ZipOutputStream var7 = new ZipOutputStream(Files.newOutputStream(var4))) {
            var7.setLevel(1);

            for (String var9 : var5) {
               checkInterrupted();
               ZipEntry var10 = new ZipEntry(var9);
               var10.setTimeLocal(bFL);
               var7.putNextEntry(var10);
               if (var3 != null && var9.equals("pack.mcmeta")) {
                  var7.write(var3);
               } else {
                  int var12;
                  try (InputStream var11 = var0.getInputStream((ZipEntry)var2.get(var9))) {
                     while ((var12 = var11.read(var6)) != -1) {
                        checkInterrupted();
                        var7.write(var6, 0, var12);
                     }
                  }
               }

               var7.closeEntry();
            }
         }

         checkInterrupted();
         String var25 = ResourcePackCompressor.digestFile(var4, "SHA-1");
         Path var26 = var1.resolve(var25 + ".zip");
         publishArtifact(var4, var26, var25);
         return var26;
      } finally {
         Files.deleteIfExists(var4);
      }
   }

   private static void publishArtifact(Path var0, Path var1, String var2) {
      if (!Files.exists(var1, LinkOption.NOFOLLOW_LINKS)) {
         try {
            Files.createLink(var1, var0);
            Files.delete(var0);
         } catch (FileAlreadyExistsException var6) {
            requireCondition(ResourcePackCompressor.digestFile(var1, "SHA-1").equals(var2), "Conflicting sound-pack artifact: " + var1);
         } catch (UnsupportedOperationException | AccessDeniedException var7) {
            try {
               Files.move(var0, var1);
            } catch (FileAlreadyExistsException var5) {
               requireCondition(ResourcePackCompressor.digestFile(var1, "SHA-1").equals(var2), "Conflicting sound-pack artifact: " + var1);
            }
         }
      } else {
         if (Files.isSymbolicLink(var1) || !ResourcePackCompressor.digestFile(var1, "SHA-1").equals(var2)) {
            Files.move(var0, var1, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         }
      }
   }

   private static boolean isSafeZipPath(String var0) {
      if (!var0.isEmpty() && !var0.startsWith("/") && var0.indexOf(92) < 0 && var0.indexOf(0) < 0) {
         for (String var4 : var0.split("/", -1)) {
            if (var4.equals(".") || var4.equals("..")) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static void requireCondition(boolean var0, String var1) {
      if (!var0) {
         throw new IOException(var1);
      }
   }

   private static void checkInterrupted() {
      if (Thread.currentThread().isInterrupted()) {
         throw new InterruptedIOException("Sound pack build stopped");
      }
   }
}
