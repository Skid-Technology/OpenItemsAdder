package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

final class ResourcePackCompressor {
   static final String COMPRESSION_MODE = "flat_4_fast_compression";

   static void compressForVersion(Path var0, Path var1, PackVersion var2) {
      compressForVersions(var0, var1, List.of(var2));
   }

   static void compressForVersions(Path var0, Path var1, List var2) {
      PackVersion var3 = (PackVersion)var2.getFirst();

      try (ZipFile var4 = new ZipFile(var0.toFile())) {
         PackManifest var5 = readManifest(var4, var3);
         JSONObject var6 = buildPackMcmeta(var5.bsX, var2);

         try (ZipOutputStream var7 = new ZipOutputStream(Files.newOutputStream(var1))) {
            var7.setLevel(1);
            TreeMap var8 = new TreeMap(var5.bta);
            var8.putAll(var5.bsY);

            for (String var10 : var5.bsZ) {
               if (var8.keySet().stream().noneMatch(var1x -> var1x.startsWith(var10 + "/"))) {
                  var8.put(var10 + "/", null);
               }
            }

            byte[] var24 = new byte[65536];

            for (Entry var11 : var8.entrySet()) {
               checkInterrupted();
               ZipEntry var12 = new ZipEntry((String)var11.getKey());
               var12.setTimeLocal(LocalDateTime.of(1980, 1, 1, 0, 0));
               var7.putNextEntry(var12);
               if (var11.getValue() != null) {
                  int var14;
                  try (InputStream var13 = var4.getInputStream((ZipEntry)var11.getValue())) {
                     while ((var14 = var13.read(var24)) != -1) {
                        checkInterrupted();
                        var7.write(var24, 0, var14);
                     }
                  }
               }

               var7.closeEntry();
            }

            ZipEntry var26 = new ZipEntry("pack.mcmeta");
            var26.setTimeLocal(LocalDateTime.of(1980, 1, 1, 0, 0));
            var7.putNextEntry(var26);
            var7.write(var6.toJSONString(new Feature[0]).getBytes(StandardCharsets.UTF_8));
            var7.closeEntry();
         }

         for (PackVersion var23 : var2) {
            verifyDerivedPack(var4, var1, var23, var2, var23.equals(var3) ? var5 : readManifest(var4, var23));
         }
      }
   }

   static void stripHumanoidEquipment(Path var0, Path var1) {
      try (ZipFile var2 = new ZipFile(var0.toFile())) {
         Map var3 = indexEntries(var2);
         JSONObject var4 = readPackMcmeta(var2, var3);
         HashSet var5 = new HashSet();

         for (String var7 : var3.keySet()) {
            String var8 = var7.split("/", 2)[0];
            if (var8.startsWith("ia_humanoid_equipment_") && var7.contains("/")) {
               var5.add(var8);
            }
         }

         JSONObject var16 = removeEquipmentEntries(var4);

         try (ZipOutputStream var17 = new ZipOutputStream(Files.newOutputStream(var1))) {
            var17.setLevel(1);
            byte[] var18 = new byte[65536];

            for (Entry var10 : var3.entrySet()) {
               checkInterrupted();
               String var11 = (String)var10.getKey();
               if (!var11.equals("pack.mcmeta") && !var11.equals("ia_humanoid_equipment.json") && !var5.contains(var11.split("/", 2)[0])) {
                  copyEntry(var2, var17, (ZipEntry)var10.getValue(), var18);
               }
            }

            ZipEntry var19 = new ZipEntry("pack.mcmeta");
            var17.putNextEntry(var19);
            var17.write(var16.toJSONString(new Feature[0]).getBytes(StandardCharsets.UTF_8));
            var17.closeEntry();
         }

         verifyParity(var2, var1);
      }
   }

   static void verifyStrippedPack(Path var0, Path var1) {
      try (ZipFile var2 = new ZipFile(var0.toFile())) {
         verifyParity(var2, var1);
      }
   }

   private static void verifyParity(ZipFile var0, Path var1) {
      Map var2 = indexEntries(var0);
      JSONObject var3 = removeEquipmentEntries(readPackMcmeta(var0, var2));

      try (ZipFile var4 = new ZipFile(var1.toFile())) {
         Map var5 = indexEntries(var4);
         JSONObject var6 = readPackMcmeta(var4, var5);
         checkCondition(var3.equals(var6), "Unknown-client metadata differs");
         var2.remove("pack.mcmeta");
         var5.remove("pack.mcmeta");
         var2.keySet().removeIf(ResourcePackCompressor::isHumanoidEquipmentPath);
         checkCondition(var2.keySet().equals(var5.keySet()), "Unknown-client paths differ");
         compareEntryBytes(var0, var2, var4, var5);
      }
   }

   private static boolean isHumanoidEquipmentPath(String var0) {
      String var1 = var0.split("/", 2)[0];
      return var0.equals("ia_humanoid_equipment.json") || var1.startsWith("ia_humanoid_equipment_") && var0.contains("/");
   }

   private static JSONObject removeEquipmentEntries(JSONObject var0) {
      JSONObject var1 = new JSONObject(var0);
      if (var1.get("overlays") instanceof JSONObject var2 && var2.get("entries") instanceof JSONArray var3) {
         JSONArray var11 = new JSONArray();

         for (Object var6 : var3) {
            if (!(var6 instanceof JSONObject var7 && var7.get("directory") instanceof String var8 && var8.startsWith("ia_humanoid_equipment_"))) {
               var11.add(var6);
            }
         }

         JSONObject var12 = new JSONObject(var2);
         var12.put("entries", var11);
         var1.put("overlays", var12);
      }

      return var1;
   }

   private static Map indexEntries(ZipFile var0) {
      LinkedHashMap var1 = new LinkedHashMap();
      Enumeration var2 = var0.entries();

      while (var2.hasMoreElements()) {
         checkInterrupted();
         ZipEntry var3 = (ZipEntry)var2.nextElement();
         checkCondition(var1.put(var3.getName(), var3) == null, "Duplicate ZIP path: " + var3.getName());
      }

      checkCondition(var1.containsKey("pack.mcmeta"), "Missing pack.mcmeta");
      return var1;
   }

   private static JSONObject readPackMcmeta(ZipFile var0, Map var1) {
      try (InputStream var2 = var0.getInputStream((ZipEntry)var1.get("pack.mcmeta"))) {
         JSONObject var3 = JSON.parseObject(var2.readAllBytes());
         checkCondition(var3 != null, "Invalid pack.mcmeta");
         return var3;
      } catch (RuntimeException var7) {
         throw new IOException("Invalid pack.mcmeta", var7);
      }
   }

   private static void copyEntry(ZipFile var0, ZipOutputStream var1, ZipEntry var2, byte[] var3) {
      ZipEntry var4 = new ZipEntry(var2.getName());
      var1.putNextEntry(var4);

      int var6;
      try (InputStream var5 = var0.getInputStream(var2)) {
         while ((var6 = var5.read(var3)) != -1) {
            checkInterrupted();
            var1.write(var3, 0, var6);
         }
      }

      var1.closeEntry();
   }

   static void deriveForVersion(Path var0, Path var1, PackVersion var2) {
      deriveForVersions(var0, var1, List.of(var2));
   }

   static void deriveForVersions(Path var0, Path var1, List var2) {
      try (ZipFile var3 = new ZipFile(var0.toFile())) {
         for (PackVersion var5 : var2) {
            verifyDerivedPack(var3, var1, var5, var2, readManifest(var3, var5));
         }
      }
   }

   private static void verifyDerivedPack(ZipFile var0, Path var1, PackVersion var2, List var3, PackManifest var4) {
      try (ZipFile var5 = new ZipFile(var1.toFile())) {
         PackManifest var6 = readManifest(var5, var2);
         checkCondition(!var6.bsX.containsKey("overlays"), "Derived pack contains overlays");
         checkCondition(buildPackMcmeta(var4.bsX, var3).equals(var6.bsX), "Derived metadata differs");
         checkCondition(var4.bsZ.equals(var6.bsZ), "Derived namespaces differ");
         compareEntryBytes(var0, var4.bsY, var5, var6.bsY);
         compareEntryBytes(var0, var4.bta, var5, var6.bta);
      }
   }

   private static void compareEntryBytes(ZipFile var0, Map var1, ZipFile var2, Map var3) {
      checkCondition(var1.keySet().equals(var3.keySet()), "Derived resource/accessory paths differ");
      byte[] var4 = new byte[65536];
      byte[] var5 = new byte[65536];

      for (String var7 : var1.keySet()) {
         try (
            InputStream var8 = var0.getInputStream((ZipEntry)var1.get(var7));
            InputStream var9 = var2.getInputStream((ZipEntry)var3.get(var7));
         ) {
            while (true) {
               checkInterrupted();
               int var10 = var8.readNBytes(var4, 0, var4.length);
               checkCondition(var10 == var9.readNBytes(var5, 0, var5.length) && Arrays.equals(var4, 0, var10, var5, 0, var10), "Derived bytes differ: " + var7);
               if (var10 == 0) {
                  break;
               }
            }
         }
      }
   }

   private static PackManifest readManifest(ZipFile var0, PackVersion var1) {
      LinkedHashMap var2 = new LinkedHashMap();
      Enumeration var3 = var0.entries();

      while (var3.hasMoreElements()) {
         checkInterrupted();
         ZipEntry var4 = (ZipEntry)var3.nextElement();
         String var5 = var4.getName();
         checkCondition(
            !var5.startsWith("/") && !var5.contains("\\") && Arrays.stream(var5.split("/")).noneMatch(var0x -> var0x.equals("..") || var0x.equals(".")),
            "Unsafe ZIP path: " + var5
         );
         checkCondition(var2.put(var5, var4) == null, "Duplicate ZIP path: " + var5);
      }

      checkCondition(var2.containsKey("pack.mcmeta"), "Missing pack.mcmeta");

      try (InputStream var22 = var0.getInputStream((ZipEntry)var2.get("pack.mcmeta"))) {
         var21 = JSON.parseObject(var22.readAllBytes());
         checkCondition(var21 != null && var21.get("pack") instanceof JSONObject, "Missing pack metadata");
      } catch (RuntimeException var20) {
         throw new IOException("Invalid pack.mcmeta", var20);
      }

      JSONObject var23 = var21.getJSONObject("pack");
      checkCondition(var23.containsKey("description"), "Missing pack description");
      checkCondition(parseFormatRange(var23, "supported_formats", false, true).contains(var1), "Target outside universal pack format range");
      JSONArray var24 = new JSONArray();
      if (var21.containsKey("overlays")) {
         checkCondition(var21.get("overlays") instanceof JSONObject var6 && var6.get("entries") instanceof JSONArray, "Invalid overlay entries");
         var24 = var21.getJSONObject("overlays").getJSONArray("entries");
      }

      boolean var25 = false;

      for (Object var8 : var24) {
         checkCondition(var8 instanceof JSONObject, "Invalid overlay");
         JSONObject var9 = (JSONObject)var8;
         if (var9.containsKey("min_format")) {
            var25 |= parseFormatEndpoint(var9.get("min_format"), 0).major <= 64;
         }

         if (var9.containsKey("formats")) {
            var25 |= parseLegacyFormats(var9.get("formats"))[0] <= 64;
         }
      }

      HashSet var27 = new HashSet();
      ArrayList var28 = new ArrayList<>(List.of(""));

      for (Object var10 : var24) {
         JSONObject var11 = (JSONObject)var10;
         checkCondition(var11.get("directory") instanceof String, "Missing overlay directory");
         String var12 = var11.getString("directory");
         checkCondition(var12.matches("[-_a-zA-Z0-9.]+") && !Set.of(".", "..", "assets", "data").contains(var12), "Ambiguous overlay directory: " + var12);
         var27.add(var12);
         if (parseFormatRange(var11, "formats", var25, false).contains(var1)) {
            var28.add(var12 + "/");
         }
      }

      LinkedHashMap var30 = new LinkedHashMap();
      LinkedHashMap var31 = new LinkedHashMap();
      HashSet var32 = new HashSet();

      for (Entry var13 : var2.entrySet()) {
         String var14 = (String)var13.getKey();
         String var15 = var14.split("/", 2)[0];
         if (!((ZipEntry)var13.getValue()).isDirectory()
            && !var14.equals("pack.mcmeta")
            && !var14.equals("ia_humanoid_equipment.json")
            && !var27.contains(var15)
            && !isAssetPath(var14)) {
            var31.put(var14, (ZipEntry)var13.getValue());
         }

         if (var27.contains(var15)
            && !((ZipEntry)var13.getValue()).isDirectory()
            && (
               var14.equals(var15)
                  || !isAssetPath(var14.substring(var15.length() + 1))
                  || var14.substring(var14.lastIndexOf(47) + 1).toLowerCase(Locale.ROOT).contains("license")
            )) {
            var31.put(var14, (ZipEntry)var13.getValue());
         }
      }

      for (String var35 : var28) {
         for (Entry var37 : var2.entrySet()) {
            if (((String)var37.getKey()).startsWith(var35)) {
               String var16 = ((String)var37.getKey()).substring(var35.length());
               String[] var17 = var16.split("/", 3);
               if (var17.length >= 2 && Set.of("assets", "data").contains(var17[0]) && var17[1].matches("[a-z0-9_.-]+")) {
                  var32.add(var17[0] + "/" + var17[1]);
                  if (!((ZipEntry)var37.getValue()).isDirectory() && isAssetPath(var16)) {
                     var30.put(var16, (ZipEntry)var37.getValue());
                  }
               }
            }
         }
      }

      return new PackManifest(var21, var30, var32, var31);
   }

   private static boolean isAssetPath(String var0) {
      return var0.matches("(?:assets|data)/[a-z0-9_.-]+/[a-z0-9/._-]+") && !var0.endsWith("/");
   }

   private static JSONObject buildPackMcmeta(JSONObject var0, List var1) {
      PackVersion var2 = Collections.min(var1);
      PackVersion var3 = Collections.max(var1);
      JSONObject var4 = new JSONObject(var0);
      JSONObject var5 = new JSONObject(var0.getJSONObject("pack"));
      var4.remove("overlays");

      for (String var7 : List.of("pack_format", "supported_formats", "min_format", "max_format")) {
         var5.remove(var7);
      }

      if (var2.major <= 64) {
         var5.put("pack_format", var2.major);
         var5.put("supported_formats", List.of(var2.major, Math.min(var3.major, 64)));
      }

      var5.put("min_format", List.of(var2.major, var2.minor));
      var5.put("max_format", List.of(var3.major, var3.minor));
      var4.put("pack", var5);
      return var4;
   }

   private static PackVersionRange parseFormatRange(JSONObject var0, String var1, boolean var2, boolean var3) {
      boolean var4 = var0.containsKey("min_format");
      checkCondition(var4 == var0.containsKey("max_format"), "Both format endpoints required");
      int[] var5 = var0.containsKey(var1) ? parseLegacyFormats(var0.get(var1)) : null;
      checkCondition(!var2 || var5 != null, "Missing legacy overlay formats");
      Integer var6 = var3 && var0.containsKey("pack_format") ? parseFormatInt(var0.get("pack_format"), false) : null;
      PackVersion var7;
      PackVersion var8;
      if (var4) {
         var7 = parseFormatEndpoint(var0.get("min_format"), 0);
         var8 = parseFormatEndpoint(var0.get("max_format"), Integer.MAX_VALUE);
         if (var7.major > 64 && !var2) {
            checkCondition(var5 == null, "Deprecated format range on modern-only entry");
         } else {
            checkCondition(
               var5 != null && var5[0] == var7.major && (var5[1] == var8.major || var5[1] == 64) && (!var3 || var6 != null), "Legacy/modern format mismatch"
            );
         }
      } else if (var5 != null) {
         checkCondition(var5[1] <= 64 && (!var3 || var6 != null), "Invalid legacy format range");
         var7 = new PackVersion(var5[0], 0);
         var8 = new PackVersion(var5[1], 0);
      } else {
         checkCondition(var3 && var6 != null && var6 <= 64, "Missing format range");
         var7 = var8 = new PackVersion(var6, 0);
      }

      checkCondition(var7.compareVersion(var8) <= 0, "Reversed format range");
      if (var6 != null) {
         checkCondition(var6 >= var7.major && var6 <= var8.major && (!var4 && var5 == null || var6 >= 15), "Invalid pack_format");
      }

      return new PackVersionRange(var7, var8);
   }

   private static int[] parseLegacyFormats(Object var0) {
      int var1;
      int var2;
      if (var0 instanceof JSONArray var3) {
         checkCondition(var3.size() == 2, "Invalid legacy range");
         var1 = parseFormatInt(var3.get(0), false);
         var2 = parseFormatInt(var3.get(1), false);
      } else if (var0 instanceof JSONObject var4) {
         var1 = parseFormatInt(var4.get("min_inclusive"), false);
         var2 = parseFormatInt(var4.get("max_inclusive"), false);
      } else {
         var1 = var2 = parseFormatInt(var0, false);
      }

      checkCondition(var1 <= var2, "Reversed legacy range");
      return new int[]{var1, var2};
   }

   private static PackVersion parseFormatEndpoint(Object var0, int var1) {
      if (!(var0 instanceof JSONArray var2)) {
         return new PackVersion(parseFormatInt(var0, true), var1);
      } else {
         checkCondition(!var2.isEmpty() && var2.size() <= 256, "Invalid format endpoint");

         for (Object var4 : var2) {
            parseFormatInt(var4, true);
         }

         return new PackVersion(parseFormatInt(var2.get(0), true), var2.size() > 1 ? parseFormatInt(var2.get(1), true) : var1);
      }
   }

   private static int parseFormatInt(Object var0, boolean var1) {
      checkCondition(var0 instanceof Integer || var0 instanceof Long, "Invalid format integer");
      long var2 = ((Number)var0).longValue();
      checkCondition(var2 >= (var1 ? 0 : Integer.MIN_VALUE) && var2 <= 2147483647L, "Format integer out of range");
      return (int)var2;
   }

   static String digestFile(Path var0, String var1) {
      try (InputStream var2 = Files.newInputStream(var0)) {
         MessageDigest var3 = MessageDigest.getInstance(var1);
         byte[] var4 = new byte[65536];

         int var5;
         while ((var5 = var2.read(var4)) != -1) {
            checkInterrupted();
            var3.update(var4, 0, var5);
         }

         return HexFormat.of().formatHex(var3.digest());
      } catch (NoSuchAlgorithmException var9) {
         throw new IllegalStateException(var9);
      }
   }

   private static void checkCondition(boolean var0, String var1) {
      if (!var0) {
         throw new IOException(var1);
      }
   }

   private static void checkInterrupted() {
      if (Thread.currentThread().isInterrupted()) {
         throw new InterruptedIOException("Client pack build stopped");
      }
   }
}
