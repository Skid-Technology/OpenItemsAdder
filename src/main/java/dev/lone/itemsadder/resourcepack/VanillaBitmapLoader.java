package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent.PackEntry;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

final class VanillaBitmapLoader {
   private static final String bGR = "vanilla-bitmap-v1";

   static BitmapTriple loadBitmapFonts(Map var0, Path var1, String var2) {
      return loadBitmapFontsWith(var0, var1, var2, null);
   }

   static synchronized BitmapTriple loadBitmapFontsWith(Map var0, Path var1, String var2, AssetFetcher var3) {
      LinkedHashMap var4 = new LinkedHashMap();
      LinkedHashMap var5 = new LinkedHashMap();
      RemoteAssetRef var6 = null;
      int var7 = 0;
      int var8 = 0;
      int var9 = 0;

      for (Entry var11 : var0.entrySet()) {
         String var12 = (String)var11.getKey();
         if (!((PackEntry)var11.getValue()).isRemoved() && var12.matches("(?:.*/)?assets/minecraft/font/(default|uniform)\\.json")) {
            JSONObject var13 = JSON.parseObject(((PackEntry)var11.getValue()).getText());
            JSONArray var14 = var13.getJSONArray("providers");
            List var15 = collectEligibleEntries(var14, var0);
            if (!var15.isEmpty()) {
               if (var6 == null) {
                  var6 = var3 == null
                     ? VanillaAssetCache.loadVersionAsset(var1.resolve("mojang"), var2)
                     : VanillaAssetCache.loadVersionAssetWith(var1.resolve("mojang"), var2, var3);
               }

               JSONArray var16 = new JSONArray();

               for (VanillaBitmapEntry var18 : var15) {
                  JSONObject var19 = var18.bGT;
                  var16.add(
                     List.of(
                        var18.bGS, var19.getString("file"), var19.getJSONArray("chars").getString(0), var19.getIntValue("height"), var19.getIntValue("ascent")
                     )
                  );
               }

               String var34 = sha256Hex(
                  ("vanilla-bitmap-v1\n" + var6.version() + "\n" + var6.sha1() + "\n" + var16.toJSONString(new Feature[0])).getBytes(StandardCharsets.UTF_8)
               );
               Path var35 = var1.resolve("sheets");
               Files.createDirectories(var35);
               JSONObject var36 = readCachedManifest(var35, var34);
               if (var36 == null) {
                  LinkedHashSet var20 = new LinkedHashSet();
                  var15.forEach(var1x -> var20.add(texturePath(var1x.bGT.getString("file"))));
                  Map var21 = var3 == null
                     ? VanillaAssetCache.loadEquipmentAssets(var1.resolve("mojang"), var6, var20)
                     : VanillaAssetCache.loadPngAssets(var1.resolve("mojang"), var6, var20, var3);
                  var36 = buildSheets(var15, var21, var35);
                  var36.put("key", var34);
                  var36.put("revision", "vanilla-bitmap-v1");
                  var36.put("minecraft_version", var6.version());
                  var36.put("client_sha1", var6.sha1());
                  var36.put("request", var16);
                  byte[] var22 = var36.toJSONString(new Feature[0]).getBytes(StandardCharsets.UTF_8);
                  VanillaAssetCache.writeCacheFile(
                     var35.resolve(var34 + ".json"), (sha256Hex(var22) + "\n" + new String(var22, StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8)
                  );
                  var8 += var36.getJSONArray("groups").size();
               } else {
                  var9++;
               }

               HashMap var37 = new HashMap();
               HashSet var38 = new HashSet();

               for (Object var23 : var36.getJSONArray("groups")) {
                  JSONObject var24 = (JSONObject)var23;
                  JSONArray var25 = var24.getJSONArray("indices");
                  int var26 = var25.getIntValue(0);
                  JSONObject var27 = new JSONObject(var14.getJSONObject(var26));
                  String var28 = var24.getString("sha256");
                  String var29 = "_iainternal:vanilla_bitmap/" + var28 + ".png";
                  var27.put("file", var29);
                  var27.put("chars", var24.getJSONArray("chars"));
                  var37.put(var26, var27);

                  for (int var30 = 1; var30 < var25.size(); var30++) {
                     var38.add(var25.getIntValue(var30));
                  }

                  byte[] var42 = Files.readAllBytes(var35.resolve(var28 + ".png"));
                  if (!sha256Hex(var42).equals(var28)) {
                     throw new IOException("Vanilla font sheet cache changed while reading " + var28);
                  }

                  String var31 = texturePath(var29);

                  for (Entry var33 : var0.entrySet()) {
                     if (!((PackEntry)var33.getValue()).isRemoved()
                        && (((String)var33.getKey()).equals(var31) || ((String)var33.getKey()).endsWith("/" + var31))
                        && !Arrays.equals(((PackEntry)var33.getValue()).getBytes(), var42)) {
                        throw new IOException("Conflicting vanilla font sheet: " + (String)var33.getKey());
                     }
                  }

                  var5.put(var31, var42);
               }

               JSONArray var40 = new JSONArray();

               for (int var41 = 0; var41 < var14.size(); var41++) {
                  if (!var38.contains(var41)) {
                     var40.add(var37.containsKey(var41) ? var37.get(var41) : var14.get(var41));
                  }
               }

               var13.put("providers", var40);
               var4.put((PackEntry)var11.getValue(), var13.toJSONString(new Feature[0]));
               var7 += var38.size();
            }
         }
      }

      var5.forEach((var1x, var2x) -> var0.put(var1x, PackEntry.bytes(var1x, var2x)));
      var4.forEach(PackEntry::setText);
      return new BitmapTriple(var7, var8, var9);
   }

   private static List collectEligibleEntries(JSONArray var0, Map var1) {
      if (var0 == null) {
         return List.of();
      }

      HashSet var2 = new HashSet();
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < var0.size(); var4++) {
         if (!(var0.get(var4) instanceof JSONObject var5)
            || !"bitmap".equals(var5.getString("type"))
            || var5.containsKey("filter")
            || var5.getJSONArray("chars") == null) {
            return List.of();
         }

         for (Object var7 : var5.getJSONArray("chars")) {
            if (!(var7 instanceof String var8)) {
               return List.of();
            }

            for (int var12 : var8.codePoints().toArray()) {
               if (var12 != 0 && !var2.add(var12)) {
                  return List.of();
               }
            }
         }

         String var14 = var5.getString("file");
         if (var5.size() == 5
            && var14 != null
            && var14.matches("minecraft:item/[a-z0-9_./-]+\\.png")
            && !var14.contains("..")
            && var5.containsKey("height")
            && var5.containsKey("ascent")
            && var5.getIntValue("height") > 0
            && var5.getJSONArray("chars").size() == 1) {
            String var15 = var5.getJSONArray("chars").getString(0);
            if (var15.codePointCount(0, var15.length()) == 1 && var15.codePointAt(0) != 0) {
               String var16 = texturePath(var14);
               boolean var17 = var1.entrySet()
                  .stream()
                  .anyMatch(
                     var1x -> !((PackEntry)var1x.getValue()).isRemoved()
                        && (((String)var1x.getKey()).equals(var16) || ((String)var1x.getKey()).endsWith("/" + var16))
                  );
               if (!var17) {
                  var3.add(new VanillaBitmapEntry(var4, var5));
               }
            }
         }
      }

      return var3;
   }

   private static JSONObject buildSheets(List var0, Map var1, Path var2) {
      LinkedHashMap var3 = new LinkedHashMap();
      HashMap var4 = new HashMap();
      JSONObject var5 = new JSONObject();

      try {
         for (VanillaBitmapEntry var7 : var0) {
            String var8 = texturePath(var7.bGT.getString("file"));
            if (!var4.containsKey(var8)) {
               byte[] var9 = (byte[])var1.get(var8);
               if (var9 == null) {
                  throw new IOException("Mojang client does not contain vanilla glyph PNG " + var8);
               }

               var4.put(var8, decodeGlyph(var9));
               var5.put(var8, sha256Hex(var9));
            }

            BufferedImage var33 = (BufferedImage)var4.get(var8);
            var3.computeIfAbsent(
                  new BitmapRect(var33.getWidth(), var33.getHeight(), var7.bGT.getIntValue("height"), var7.bGT.getIntValue("ascent")),
                  var0x -> new ArrayList<>()
               )
               .add(var7);
         }

         JSONArray var29 = new JSONArray();

         for (Entry var32 : var3.entrySet()) {
            BitmapRect var34 = (BitmapRect)var32.getKey();
            int var10 = 1024 / var34.bGX;
            int var11 = 1024 / var34.bGY;
            int var12 = var10 * var11;

            for (int var13 = 0; var13 < ((List)var32.getValue()).size(); var13 += var12) {
               List var14 = ((List)var32.getValue()).subList(var13, Math.min(var13 + var12, ((List)var32.getValue()).size()));
               int var15 = Math.max(Math.min((int)Math.ceil(Math.sqrt(var14.size())), var10), (var14.size() + var11 - 1) / var11);
               int var16 = (var14.size() + var15 - 1) / var15;
               BufferedImage var17 = new BufferedImage(var15 * var34.bGX, var16 * var34.bGY, 2);
               JSONArray var18 = new JSONArray();
               JSONArray var19 = new JSONArray();

               for (int var20 = 0; var20 < var16; var20++) {
                  StringBuilder var21 = new StringBuilder();

                  for (int var22 = 0; var22 < var15; var22++) {
                     int var23 = var20 * var15 + var22;
                     if (var23 >= var14.size()) {
                        var21.append('\u0000');
                     } else {
                        VanillaBitmapEntry var24 = (VanillaBitmapEntry)var14.get(var23);
                        BufferedImage var25 = (BufferedImage)var4.get(texturePath(var24.bGT.getString("file")));
                        var17.setRGB(
                           var22 * var34.bGX,
                           var20 * var34.bGY,
                           var34.bGX,
                           var34.bGY,
                           var25.getRGB(0, 0, var34.bGX, var34.bGY, null, 0, var34.bGX),
                           0,
                           var34.bGX
                        );
                        var21.append(var24.bGT.getJSONArray("chars").getString(0));
                        var19.add(var24.bGS);
                     }
                  }

                  var18.add(var21.toString());
               }

               ByteArrayOutputStream var35 = new ByteArrayOutputStream();
               if (!ImageIO.write(var17, "PNG", var35)) {
                  throw new IOException("No PNG encoder for vanilla font sheet");
               }

               var17.flush();
               byte[] var36 = var35.toByteArray();
               String var37 = sha256Hex(var36);
               VanillaAssetCache.writeCacheFile(var2.resolve(var37 + ".png"), var36);
               JSONObject var38 = new JSONObject();
               var38.put("indices", var19);
               var38.put("chars", var18);
               var38.put("sha256", var37);
               var29.add(var38);
            }
         }

         JSONObject var31 = new JSONObject();
         var31.put("inputs", var5);
         var31.put("groups", var29);
         return var31;
      } finally {
         var4.values().forEach(Image::flush);
      }
   }

   static BufferedImage decodeGlyph(byte[] var0) {
      try (ImageInputStream var1 = ImageIO.createImageInputStream(new ByteArrayInputStream(var0))) {
         Iterator var2 = ImageIO.getImageReaders(var1);
         if (!var2.hasNext()) {
            throw new IOException("Invalid vanilla glyph PNG");
         }

         ImageReader var3 = (ImageReader)var2.next();

         try {
            var3.setInput(var1);
            int var4 = var3.getWidth(0);
            int var5 = var3.getHeight(0);
            if (var4 >= 1 && var5 >= 1 && var4 <= 256 && var5 <= 256) {
               BufferedImage var6 = var3.read(0);
               BufferedImage var7 = new BufferedImage(var4, var5, 2);
               ColorModel var8 = var6.getColorModel();
               int[] var9 = var8.getComponentSize();
               boolean var10 = var8.getColorSpace().getType() == 6;
               if (!var8.isAlphaPremultiplied() && (var8 instanceof IndexColorModel || var10 || var8.getColorSpace().getType() == 5)) {
                  for (int var11 = 0; var11 < var5; var11++) {
                     for (int var12 = 0; var12 < var4; var12++) {
                        if (var8 instanceof IndexColorModel) {
                           var7.setRGB(var12, var11, var6.getRGB(var12, var11));
                        } else {
                           int[] var13 = var6.getRaster().getPixel(var12, var11, (int[])null);

                           for (int var14 = 0; var14 < var13.length; var14++) {
                              var13[var14] = var9[var14] == 16
                                 ? var13[var14] >>> 8
                                 : (var9[var14] == 8 ? var13[var14] : var13[var14] * 255 / ((1 << var9[var14]) - 1));
                           }

                           int var26 = var13[0];
                           int var15 = var10 ? var26 : var13[1];
                           int var16 = var10 ? var26 : var13[2];
                           int var17 = var8.hasAlpha() ? var13[var10 ? 1 : 3] : 255;
                           var7.setRGB(var12, var11, var17 << 24 | var26 << 16 | var15 << 8 | var16);
                        }
                     }
                  }

                  var6.flush();
                  return var7;
               } else {
                  throw new IOException("Unsupported vanilla PNG color model");
               }
            } else {
               throw new IOException("Vanilla glyph cell exceeds 256 pixels: " + var4 + "x" + var5);
            }
         } finally {
            var3.dispose();
         }
      }
   }

   private static JSONObject readCachedManifest(Path var0, String var1) {
      try {
         String var2 = Files.readString(var0.resolve(var1 + ".json"));
         int var3 = var2.indexOf(10);
         if (var3 == 64 && var2.substring(0, var3).equals(sha256Hex(var2.substring(var3 + 1).getBytes(StandardCharsets.UTF_8)))) {
            JSONObject var4 = JSON.parseObject(var2.substring(var3 + 1));
            if (var1.equals(var4.getString("key")) && "vanilla-bitmap-v1".equals(var4.getString("revision"))) {
               JSONArray var5 = var4.getJSONArray("groups");
               if (var5 != null && !var5.isEmpty()) {
                  for (Object var7 : var5) {
                     String var8 = ((JSONObject)var7).getString("sha256");
                     if (var8 == null || !var8.matches("[0-9a-f]{64}") || !var8.equals(sha256Hex(Files.readAllBytes(var0.resolve(var8 + ".png"))))) {
                        return null;
                     }
                  }

                  return var4;
               } else {
                  return null;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      } catch (IOException | RuntimeException var9) {
         return null;
      }
   }

   private static String texturePath(String var0) {
      String[] var1 = var0.split(":", 2);
      return "assets/" + var1[0] + "/textures/" + var1[1];
   }

   static String sha256Hex(byte[] var0) {
      try {
         return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var0));
      } catch (NoSuchAlgorithmException var2) {
         throw new IllegalStateException(var2);
      }
   }
}
