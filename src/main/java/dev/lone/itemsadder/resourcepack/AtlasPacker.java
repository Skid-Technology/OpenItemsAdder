package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent.PackEntry;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;

final class AtlasPacker {
   static int packFontAtlases(Map var0) {
      HashSet var1 = new HashSet();

      for (String var3 : var0.keySet()) {
         int var4 = var3.indexOf("/assets/");
         if (var4 >= 0) {
            var1.add(var3.substring(var4 + 1));
         }
      }

      int var15 = 0;

      for (String var17 : List.of("assets/minecraft/font/default.json", "assets/minecraft/font/uniform.json")) {
         PackEntry var5 = (PackEntry)var0.get(var17);
         if (var5 != null && !var5.isRemoved()) {
            JSONObject var6 = JSON.parseObject(var5.getText());
            JSONArray var7 = var6.getJSONArray("providers");
            if (var7 != null) {
               JSONArray var8 = new JSONArray();
               ArrayList var9 = new ArrayList();
               HashSet var10 = new HashSet();

               for (Object var12 : var7) {
                  AtlasEntry var13 = var12 instanceof JSONObject var14 ? parseAtlasProvider(var14, var0, var1) : null;
                  if (var13 == null || var9.size() == 64 || var10.contains(var13.bur)) {
                     var15 += packAtlasBatch(var9, var8, var0);
                     var10.clear();
                  }

                  if (var13 == null) {
                     var8.add(var12);
                  } else {
                     var9.add(var13);
                     var10.add(var13.bur);
                  }
               }

               var15 += packAtlasBatch(var9, var8, var0);
               var6.put("providers", var8);
               var5.setText(var6.toJSONString(new Feature[0]));
            }
         }
      }

      return var15;
   }

   private static AtlasEntry parseAtlasProvider(JSONObject var0, Map var1, Set var2) {
      if (var0.size() == 5 && "bitmap".equals(var0.getString("type")) && var0.containsKey("height") && var0.containsKey("ascent")) {
         JSONArray var3 = var0.getJSONArray("chars");
         if (var3 != null
            && var3.size() == 1
            && var3.get(0) instanceof String var4
            && var4.codePointCount(0, var4.length()) == 1
            && var4.codePointAt(0) != 0
            && var0.getIntValue("height") > 0) {
            String var31 = var0.getString("file");
            if (var31 == null) {
               return null;
            }

            String[] var6 = var31.split(":", 2);
            String var7 = "assets/" + (var6.length == 2 ? var6[0] : "minecraft") + "/textures/" + var6[var6.length - 1];
            PackEntry var8 = (PackEntry)var1.get(var7);
            if (var8 != null && !var8.isRemoved() && !var2.contains(var7)) {
               try (
                  InputStream var9 = var8.openStream();
                  MemoryCacheImageInputStream var10 = new MemoryCacheImageInputStream(var9);
               ) {
                  Iterator var11 = ImageIO.getImageReaders(var10);
                  if (!var11.hasNext()) {
                     return null;
                  }

                  ImageReader var12 = (ImageReader)var11.next();

                  try {
                     var12.setInput(var10);
                     if (var12.getWidth(0) <= 256 && var12.getHeight(0) <= 256) {
                        BufferedImage var33 = var12.read(0);
                        if ("png".equalsIgnoreCase(var12.getFormatName())
                           && var33.getColorModel().getColorSpace().isCS_sRGB()
                           && var33.getColorModel().getNumColorComponents() == 3
                           && var33.getColorModel().hasAlpha()
                           && !var33.isAlphaPremultiplied()
                           && Arrays.stream(var33.getColorModel().getComponentSize()).allMatch(var0x -> var0x == 16)) {
                           BufferedImage var14 = new BufferedImage(var33.getWidth(), var33.getHeight(), 2);
                           int[] var15 = new int[4];

                           for (int var16 = 0; var16 < var33.getHeight(); var16++) {
                              for (int var17 = 0; var17 < var33.getWidth(); var17++) {
                                 var33.getRaster().getPixel(var17, var16, var15);
                                 var14.setRGB(var17, var16, var15[3] >>> 8 << 24 | var15[0] >>> 8 << 16 | var15[1] >>> 8 << 8 | var15[2] >>> 8);
                              }
                           }

                           var33 = var14;
                        }

                        return var33.getColorModel().getColorSpace().isCS_sRGB()
                              && !Arrays.stream(var33.getColorModel().getComponentSize()).anyMatch(var0x -> var0x != 8)
                           ? new AtlasEntry(var0, var4, var33)
                           : null;
                     } else {
                        return null;
                     }
                  } finally {
                     var12.dispose();
                  }
               } catch (IOException | RuntimeException var30) {
                  return null;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private static int packAtlasBatch(List var0, JSONArray var1, Map var2) {
      LinkedHashMap var3 = new LinkedHashMap();

      for (AtlasEntry var5 : var0) {
         var3.computeIfAbsent(
               new AtlasRect(var5.bus.getWidth(), var5.bus.getHeight(), var5.buq.getIntValue("height"), var5.buq.getIntValue("ascent")),
               var0x -> new ArrayList<>()
            )
            .add(var5);
      }

      int var24 = 0;

      for (Entry var6 : var3.entrySet()) {
         AtlasRect var7 = (AtlasRect)var6.getKey();
         int var8 = 1024 / var7.but;
         int var9 = 1024 / var7.buu;
         int var10 = var8 * var9;

         for (int var11 = 0; var11 < ((List)var6.getValue()).size(); var11 += var10) {
            List var12 = ((List)var6.getValue()).subList(var11, Math.min(var11 + var10, ((List)var6.getValue()).size()));
            if (var12.size() == 1) {
               var1.add(((AtlasEntry)var12.getFirst()).buq);
            } else {
               int var13 = Math.max(Math.min((int)Math.ceil(Math.sqrt(var12.size())), var8), (var12.size() + var9 - 1) / var9);
               int var14 = (var12.size() + var13 - 1) / var13;
               BufferedImage var15 = new BufferedImage(var13 * var7.but, var14 * var7.buu, 2);
               JSONArray var16 = new JSONArray();

               for (int var17 = 0; var17 < var14; var17++) {
                  StringBuilder var18 = new StringBuilder();

                  for (int var19 = 0; var19 < var13; var19++) {
                     int var20 = var17 * var13 + var19;
                     if (var20 >= var12.size()) {
                        var18.append('\u0000');
                     } else {
                        AtlasEntry var21 = (AtlasEntry)var12.get(var20);
                        var18.append(var21.bur);
                        var15.setRGB(
                           var19 * var7.but, var17 * var7.buu, var7.but, var7.buu, var21.bus.getRGB(0, 0, var7.but, var7.buu, null, 0, var7.but), 0, var7.but
                        );
                     }
                  }

                  var16.add(var18.toString());
               }

               ByteArrayOutputStream var26 = new ByteArrayOutputStream();
               if (!ImageIO.write(var15, "PNG", var26)) {
                  throw new IOException("No PNG writer for font image sheet");
               }

               byte[] var27 = var26.toByteArray();

               String var28;
               try {
                  var28 = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var27));
               } catch (NoSuchAlgorithmException var23) {
                  throw new IllegalStateException(var23);
               }

               String var29 = "_iainternal:font_atlas/" + var28 + ".png";
               String var30 = "assets/_iainternal/textures/font_atlas/" + var28 + ".png";
               if (var2.containsKey(var30) && !Arrays.equals(((PackEntry)var2.get(var30)).getBytes(), var27)) {
                  var12.forEach(var1x -> var1.add(var1x.buq));
               } else {
                  var2.put(var30, PackEntry.bytes(var30, var27));
                  JSONObject var22 = new JSONObject(((AtlasEntry)var12.getFirst()).buq);
                  var22.put("file", var29);
                  var22.put("chars", var16);
                  var1.add(var22);
                  var24 += var12.size() - 1;
               }
            }
         }
      }

      var0.clear();
      return var24;
   }
}
