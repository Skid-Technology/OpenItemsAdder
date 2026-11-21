package dev.lone.itemsadder.resourcepack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.lone.itemsadder.util.BinaryPackCodec;
import dev.lone.itemsadder.util.ImageOptimizeStatus;
import dev.lone.itemsadder.util.IntTableUtils;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.zip.CRC32;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

final class AtlasImageEncoder {
   static CachedAtlasEntry optimizePng(byte[] var0, JsonObject var1, PackStatsTuple var2) {
      if (var0.length >= 20 && ByteBuffer.wrap(var0, 0, 8).getLong() == -8552249625308161526L) {
         boolean var3 = false;
         int var4 = 8;

         while (var4 < var0.length) {
            if (var4 > var0.length - 12) {
               throw new IOException("Truncated PNG chunk");
            }

            int var27 = ByteBuffer.wrap(var0, var4, 4).getInt();
            if (var27 < 0 || var27 > var0.length - var4 - 12) {
               throw new IOException("Invalid PNG chunk length");
            }

            String var28 = new String(var0, var4 + 4, 4, StandardCharsets.US_ASCII);
            CRC32 var29 = new CRC32();
            var29.update(var0, var4 + 4, var27 + 4);
            if ((int)var29.getValue() != ByteBuffer.wrap(var0, var4 + var27 + 8, 4).getInt()) {
               if (BinaryPackCodec.protect(var0).VS() == ImageOptimizeStatus.ALREADY_PROCESSED) {
                  return null;
               }

               throw new IOException("PNG CRC mismatch");
            }

            if (Set.of("iCCP", "cICP", "mDCV", "cLLI", "CgBI", "acTL").contains(var28)) {
               return null;
            }

            if ((var0[var4 + 4] & 32) == 0 && !Set.of("IHDR", "PLTE", "IDAT", "IEND").contains(var28)) {
               return null;
            }

            if (var28.equals("tRNS") && var2.Xx() != 3) {
               return null;
            }

            if (var28.equals("IEND")) {
               if (var27 != 0 || var4 + 12 != var0.length) {
                  throw new IOException("Invalid PNG end chunk");
               }

               var3 = true;
            }

            var4 += var27 + 12;
         }

         if (!var3) {
            throw new IOException("Missing PNG end chunk");
         }

         if ((var2.Xx() != 3 || !Set.of(1, 2, 4, 8).contains(var2.HS())) && (var2.Xx() != 2 && var2.Xx() != 6 || var2.HS() != 8)) {
            return null;
         }

         if (var2.Xw() > Math.min(67108864L, Runtime.getRuntime().maxMemory() / 32L)) {
            throw new IOException("Image exceeds sequential preprocessing memory budget; increase generation heap or keep the original");
         }

         if (!Set.of("animation", "texture").containsAll(var1.keySet())) {
            return null;
         }

         JsonObject var26 = var1.deepCopy();
         JsonObject var5 = var26.has("texture") ? var26.getAsJsonObject("texture") : new JsonObject();
         if (!Set.of("blur", "clamp", "mipmap_strategy", "alpha_cutoff_bias").containsAll(var5.keySet())) {
            return null;
         }

         String var6 = var5.has("mipmap_strategy") ? var5.get("mipmap_strategy").getAsString() : "auto";
         if (!Set.of("auto", "cutout", "strict_cutout").contains(var6)) {
            return null;
         }

         PackCountStats var7 = null;
         if (var26.has("animation")) {
            JsonObject var8 = var26.getAsJsonObject("animation");
            if (!Set.of("width", "height", "frametime", "interpolate", "frames").containsAll(var8.keySet())) {
               return null;
            }

            if (var8.has("frames")) {
               for (JsonElement var10 : var8.getAsJsonArray("frames")) {
                  if (var10.isJsonObject() && !Set.of("index", "time").containsAll(var10.getAsJsonObject().keySet())) {
                     return null;
                  }
               }
            }

            var7 = ResourcePackLoader.parseAnimation(var8, var2);
            if (var7.Xp().isEmpty()) {
               return null;
            }
         }

         if (var7 == null && var2.Xw() < 1048576L) {
            return null;
         }

         BufferedImage var30 = ImageIO.read(new ByteArrayInputStream(var0));
         if (var30 != null
            && !var30.isAlphaPremultiplied()
            && (
               var30.getColorModel() instanceof IndexColorModel
                  || var30.getColorModel().getColorSpace().isCS_sRGB()
                     && var30.getColorModel().getNumColorComponents() == 3
                     && Arrays.stream(var30.getColorModel().getComponentSize()).allMatch(var0x -> var0x == 8)
            )) {
            int var32 = var30.getWidth();
            int var33 = var30.getHeight();
            if (var32 == var2.method4() && var33 == var2.JK()) {
               int[] var11 = var30.getRGB(0, 0, var32, var33, null, 0, var32);
               var30.flush();
               Object var31 = null;
               boolean var12 = false;

               for (int var16 : var11) {
                  if (var16 >>> 24 == 0) {
                     var12 = true;
                     break;
                  }
               }

               boolean var34 = var12 || !var6.equals("auto");
               if (var34) {
                  IntTableUtils.fillTransparentPixels(var11, var32, var33);
               }

               if (Thread.currentThread().isInterrupted()) {
                  throw new InterruptedIOException("Texture preprocessing interrupted");
               }

               int var35 = var7 == null ? 1 : var7.method132(var2);
               ArrayList var36 = new ArrayList();
               int[] var37 = new int[var35];
               Arrays.fill(var37, -1);
               if (var7 != null) {
                  LinkedHashMap var17 = new LinkedHashMap();

                  for (int var18 = 0; var18 < var35; var18++) {
                     if (var37[var18] < 0) {
                        String var19 = hashFramePixels(var11, var32, var7.method4(), var7.JK(), var18);
                        List var20 = var17.computeIfAbsent(var19, var0x -> new ArrayList());
                        int var21 = -1;

                        for (int var23 : var20) {
                           if (framesIdentical(var11, var32, var7.method4(), var7.JK(), var18, var23)) {
                              var21 = var23;
                              break;
                           }
                        }

                        if (var21 >= 0) {
                           var37[var18] = var37[var21];
                        } else {
                           var37[var18] = var36.size();
                           var36.add(var18);
                           var20.add(var18);
                        }
                     }
                  }
               } else {
                  var36.add(0);
               }

               boolean var38 = var7 != null && var36.size() < var35;
               if (var38 || var34 && var2.Xw() >= 1048576L) {
                  int var39 = var38 ? var7.method4() : var32;
                  int var40 = var38 ? Math.multiplyExact(var36.size(), var7.JK()) : var33;
                  if (var40 > 32768) {
                     return null;
                  }

                  BufferedImage var41 = buildIndexedImage(var11, var39, var40, var38 ? var7 : null, var32, var36);
                  if (var41 == null) {
                     var41 = new BufferedImage(var39, var40, 2);
                  }

                  if (!var38) {
                     if (!(var41.getColorModel() instanceof IndexColorModel)) {
                        var41.setRGB(0, 0, var32, var33, var11, 0, var32);
                     }
                  } else {
                     for (int var42 = 0; var42 < var36.size(); var42++) {
                        int var45 = (Integer)var36.get(var42);
                        int var47 = var45 % (var32 / var7.method4()) * var7.method4();
                        int var24 = var45 / (var32 / var7.method4()) * var7.JK();
                        if (!(var41.getColorModel() instanceof IndexColorModel)) {
                           for (int var25 = 0; var25 < var7.JK(); var25++) {
                              var41.setRGB(0, var42 * var7.JK() + var25, var39, 1, var11, (var24 + var25) * var32 + var47, var32);
                           }
                        }
                     }

                     JsonObject var43 = var26.getAsJsonObject("animation");
                     var43.addProperty("width", var7.method4());
                     var43.addProperty("height", var7.JK());
                     JsonArray var46 = new JsonArray();

                     for (int var48 = 0; var48 < var7.Xp().size(); var48++) {
                        JsonObject var50 = new JsonObject();
                        var50.addProperty("index", var37[var7.Xp().get(var48)]);
                        var50.addProperty("time", (Number)var7.Xq().get(var48));
                        var46.add(var50);
                     }

                     var43.add("frames", var46);
                     PackCountStats var49 = ResourcePackLoader.parseAnimation(var43, new PackStatsTuple(var39, var40, 8, 6));
                     if (!var49.Xq().equals(var7.Xq()) || var49.Xo() != var7.Xo()) {
                        throw new IOException("Animation timeline changed");
                     }

                     for (int var51 = 0; var51 < var7.Xp().size(); var51++) {
                        if (!frameMatchesImage(var11, var32, var7, (Integer)var7.Xp().get(var51), var41, (Integer)var49.Xp().get(var51))) {
                           throw new IOException("Animation frame RGBA changed");
                        }
                     }
                  }

                  var5.addProperty("mipmap_strategy", "mean");
                  var26.add("texture", var5);
                  ByteArrayOutputStream var44 = new ByteArrayOutputStream();
                  writePng(var41, var44);
                  var41.flush();
                  ResourcePackLoader.validateTextureMetadata(var26, new PackStatsTuple(var39, var40, 8, 6));
                  return new CachedAtlasEntry(var44.toByteArray(), var26, var35, var36.size(), var2.Xw(), (long)var39 * var40, var34);
               } else {
                  return null;
               }
            } else {
               throw new IOException("PNG header/decoder dimensions differ");
            }
         } else {
            return null;
         }
      } else {
         throw new IOException("Invalid PNG signature");
      }
   }

   private static BufferedImage buildIndexedImage(int[] var0, int var1, int var2, PackCountStats var3, int var4, List var5) {
      int[] var6 = new int[512];
      byte[] var7 = new byte[512];
      boolean[] var8 = new boolean[512];
      byte[] var9 = new byte[256];
      byte[] var10 = new byte[256];
      byte[] var11 = new byte[256];
      byte[] var12 = new byte[256];
      int var13 = 0;

      for (int var14 = 0; var14 < var2; var14++) {
         for (int var15 = 0; var15 < var1; var15++) {
            int var16 = sampleFramePixel(var0, var15, var14, var3, var4, var5);
            int var17 = hashSlot(var16, var6.length);

            while (var8[var17] && var6[var17] != var16) {
               var17 = var17 + 1 & var6.length - 1;
            }

            if (!var8[var17]) {
               if (var13 == 256) {
                  return null;
               }

               var8[var17] = true;
               var6[var17] = var16;
               var7[var17] = (byte)var13;
               var12[var13] = (byte)(var16 >>> 24);
               var9[var13] = (byte)(var16 >>> 16);
               var10[var13] = (byte)(var16 >>> 8);
               var11[var13++] = (byte)var16;
            }
         }
      }

      IndexColorModel var22 = new IndexColorModel(8, var13, var9, var10, var11, var12);
      WritableRaster var23 = Raster.createInterleavedRaster(0, var1, var2, 1, null);
      byte[] var24 = ((DataBufferByte)var23.getDataBuffer()).getData();
      int var25 = 0;
      int var18 = 0;

      while (var25 < var2) {
         for (int var19 = 0; var19 < var1; var18++) {
            int var20 = sampleFramePixel(var0, var19, var25, var3, var4, var5);
            int var21 = hashSlot(var20, var6.length);

            while (var6[var21] != var20) {
               var21 = var21 + 1 & var6.length - 1;
            }

            var24[var18] = var7[var21];
            var19++;
         }

         var25++;
      }

      return new BufferedImage(var22, var23, false, null);
   }

   private static int sampleFramePixel(int[] var0, int var1, int var2, PackCountStats var3, int var4, List var5) {
      if (var3 == null) {
         return var0[var2 * var4 + var1];
      }

      int var6 = (Integer)var5.get(var2 / var3.JK());
      int var7 = var6 % (var4 / var3.method4()) * var3.method4();
      int var8 = var6 / (var4 / var3.method4()) * var3.JK();
      return var0[(var8 + var2 % var3.JK()) * var4 + var7 + var1];
   }

   private static int hashSlot(int var0, int var1) {
      return (var0 ^ var0 >>> 16) * 73244475 & var1 - 1;
   }

   private static void writePng(BufferedImage var0, OutputStream var1) {
      Iterator var2 = ImageIO.getImageWritersByFormatName("png");
      if (!var2.hasNext()) {
         throw new IOException("No PNG encoder");
      }

      ImageWriter var3 = (ImageWriter)var2.next();

      try (ImageOutputStream var4 = ImageIO.createImageOutputStream(var1)) {
         ImageWriteParam var5 = var3.getDefaultWriteParam();
         if (var5.canWriteCompressed()) {
            var5.setCompressionMode(2);
            var5.setCompressionQuality(0.0F);
         }

         var3.setOutput(var4);
         var3.write(null, new IIOImage(var0, null, null), var5);
      } finally {
         var3.dispose();
      }
   }

   private static boolean frameMatchesImage(int[] var0, int var1, PackCountStats var2, int var3, BufferedImage var4, int var5) {
      int var6 = var3 % (var1 / var2.method4()) * var2.method4();
      int var7 = var3 / (var1 / var2.method4()) * var2.JK();

      for (int var8 = 0; var8 < var2.JK(); var8++) {
         for (int var9 = 0; var9 < var2.method4(); var9++) {
            if (var0[(var7 + var8) * var1 + var6 + var9] != var4.getRGB(var9, var5 * var2.JK() + var8)) {
               return false;
            }
         }
      }

      return true;
   }

   private static String hashFramePixels(int[] var0, int var1, int var2, int var3, int var4) {
      try {
         MessageDigest var5 = MessageDigest.getInstance("SHA-256");
         int var6 = var4 % (var1 / var2) * var2;
         int var7 = var4 / (var1 / var2) * var3;
         byte[] var8 = new byte[var2 * 4];

         for (int var9 = 0; var9 < var3; var9++) {
            for (int var10 = 0; var10 < var2; var10++) {
               int var11 = var0[(var7 + var9) * var1 + var6 + var10];
               var8[var10 * 4] = (byte)(var11 >>> 24);
               var8[var10 * 4 + 1] = (byte)(var11 >>> 16);
               var8[var10 * 4 + 2] = (byte)(var11 >>> 8);
               var8[var10 * 4 + 3] = (byte)var11;
            }

            var5.update(var8);
         }

         return HexFormat.of().formatHex(var5.digest());
      } catch (NoSuchAlgorithmException var12) {
         throw new AssertionError(var12);
      }
   }

   private static boolean framesIdentical(int[] var0, int var1, int var2, int var3, int var4, int var5) {
      int var6 = var4 % (var1 / var2) * var2;
      int var7 = var4 / (var1 / var2) * var3;
      int var8 = var5 % (var1 / var2) * var2;
      int var9 = var5 / (var1 / var2) * var3;

      for (int var10 = 0; var10 < var3; var10++) {
         if (!Arrays.equals(
            var0, (var7 + var10) * var1 + var6, (var7 + var10) * var1 + var6 + var2, var0, (var9 + var10) * var1 + var8, (var9 + var10) * var1 + var8 + var2
         )) {
            return false;
         }
      }

      return true;
   }
}
