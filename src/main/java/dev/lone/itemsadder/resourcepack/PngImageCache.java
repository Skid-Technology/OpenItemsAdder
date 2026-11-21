package dev.lone.itemsadder.resourcepack;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;

public final class PngImageCache {
   private static final int MAX_PNG_BYTES = 4194304;
   private static final int MAX_DIMENSION = 4096;
   private static final long MAX_PIXELS = 16777216L;
   private final Map cache = new HashMap();

   public synchronized PngImage decodePng(byte[] var1) {
      if (var1 != null && var1.length != 0 && var1.length <= 4194304) {
         try (MemoryCacheImageInputStream var2 = new MemoryCacheImageInputStream(new ByteArrayInputStream(var1))) {
            Iterator var3 = ImageIO.getImageReaders(var2);
            if (!var3.hasNext()) {
               throw invalidPng("invalid PNG");
            }

            ImageReader var4 = (ImageReader)var3.next();

            BufferedImage var5;
            try {
               var4.setInput(var2, true, true);
               if (!"png".equalsIgnoreCase(var4.getFormatName())) {
                  throw invalidPng("texture is not PNG");
               }

               int var6 = var4.getWidth(0);
               int var7 = var4.getHeight(0);
               if (var6 < 1 || var7 < 1 || var6 > 4096 || var7 > 4096 || (long)var6 * var7 > 16777216L) {
                  throw invalidPng("PNG dimensions out of range");
               }

               var5 = var4.read(0);
            } finally {
               var4.dispose();
            }

            if (var5 == null) {
               throw invalidPng("invalid PNG");
            }

            int var26 = var5.getWidth();
            int var27 = var5.getHeight();
            byte[] var8 = new byte[Math.multiplyExact(Math.multiplyExact(var26, var27), 4)];
            int var9 = 0;

            for (int var10 = 0; var10 < var27; var10++) {
               for (int var11 = 0; var11 < var26; var11++) {
                  int var12 = var5.getRGB(var11, var10);
                  var8[var9++] = (byte)(var12 >>> 16);
                  var8[var9++] = (byte)(var12 >>> 8);
                  var8[var9++] = (byte)var12;
                  var8[var9++] = (byte)(var12 >>> 24);
               }
            }

            String var31 = pixelHash(var26, var27, var8);
            PngImage var32 = (PngImage)this.cache.get(var31);
            if (var32 != null) {
               return var32;
            }

            BufferedImage var33 = new BufferedImage(var26, var27, 2);
            var33.setRGB(0, 0, var26, var27, var5.getRGB(0, 0, var26, var27, null, 0, var26), 0, var26);
            ByteArrayOutputStream var13 = new ByteArrayOutputStream();
            if (!ImageIO.write(var33, "png", var13)) {
               throw invalidPng("PNG writer unavailable");
            }

            PngImage var14 = new PngImage(var31, var26, var27, var13.toByteArray());
            this.cache.put(var31, var14);
            return var14;
         } catch (IllegalArgumentException var24) {
            throw var24;
         } catch (Exception var25) {
            throw invalidPng("invalid PNG");
         }
      } else {
         throw invalidPng("invalid PNG length");
      }
   }

   private static String pixelHash(int var0, int var1, byte[] var2) {
      try {
         MessageDigest var3 = MessageDigest.getInstance("SHA-256");
         var3.update(ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN).putInt(var0).putInt(var1).array());
         return HexFormat.of().formatHex(var3.digest(var2));
      } catch (Exception var4) {
         throw new AssertionError(var4);
      }
   }

   private static IllegalArgumentException invalidPng(String var0) {
      return new IllegalArgumentException("Invalid particle texture: " + var0);
   }
}
