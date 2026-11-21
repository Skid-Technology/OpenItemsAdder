package dev.lone.itemsadder.anim;

import com.alibaba.fastjson2.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;

public final class AnimationDataCompiler {
   public static final int Uf = 256;
   public static final int Ug = 16777216;
   private static final byte[] Uh = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10};

   private AnimationDataCompiler() {
   }

   public static Map method9221(Path var0, Path var1) {
      if (Files.size(var0) > 67108864L) {
         throw bj("document too large");
      } else {
         return method9222(AnimationBinaryCodec.method9161(Files.readAllBytes(var0)), var1);
      }
   }

   public static Map method9222(AnimationBinaryPayload var0, Path var1) {
      JSONObject var2 = var0.vH().getJSONObject("textures");
      JSONObject var3 = var0.vH().getJSONObject("textureMeta");
      LinkedHashMap var4 = new LinkedHashMap();
      if (var2 == null) {
         return var4;
      }

      for (String var6 : var2.keySet()) {
         JSONObject var7 = var3 == null ? null : var3.getJSONObject(var6);
         Path var8 = var1.resolve(var6 + ".png");
         if (!Files.exists(var8)) {
            Files.write(var8, method9225(var6, var2.getString(var6), var7), StandardOpenOption.CREATE_NEW);
         }

         var4.put(var6 + ".png", var8);
         if (var7 != null) {
            Path var9 = var1.resolve(var6 + ".png.mcmeta");
            if (!Files.exists(var9)) {
               Files.writeString(var9, AnimationBinaryCodec.method9185(var7), StandardOpenOption.CREATE_NEW);
            }

            var4.put(var6 + ".png.mcmeta", var9);
         }
      }

      return var4;
   }

   public static void method9223(JSONObject var0, JSONObject var1) {
      if (var0.size() > 256) {
         throw bj("too many textures");
      }

      for (Entry var3 : var1.entrySet()) {
         if (!var0.containsKey((String)var3.getKey()) || !(var3.getValue() instanceof JSONObject)) {
            throw bj("texture metadata must reference an embedded PNG and be an object");
         }
      }

      long var22 = 0L;

      for (Entry var5 : var0.entrySet()) {
         if (!(var5.getValue() instanceof String var6)) {
            throw bj("texture must be Base64 text");
         }

         var22 += var6.length();
         if (var22 > 16777216L) {
            throw bj("embedded textures exceed metadata budget");
         }

         byte[] var23 = method9225((String)var5.getKey(), var6, var1.getJSONObject((String)var5.getKey()));

         try (MemoryCacheImageInputStream var8 = new MemoryCacheImageInputStream(new ByteArrayInputStream(var23))) {
            Iterator var9 = ImageIO.getImageReaders(var8);
            if (!var9.hasNext()) {
               throw bj("invalid PNG");
            }

            ImageReader var10 = (ImageReader)var9.next();

            try {
               var10.setInput(var8);
               int var11 = var10.getWidth(0);
               int var12 = var10.getHeight(0);
               if (var11 <= 0 || var12 <= 0 || var11 > 16384 || var12 > 16384 || (long)var11 * var12 > 16777216L) {
                  throw bj("PNG dimensions exceed bounds");
               }

               var10.read(0).flush();
            } finally {
               var10.dispose();
            }
         } catch (IOException var21) {
            throw new IllegalArgumentException("Invalid IAD2 PNG", var21);
         }
      }
   }

   public static byte[] method9224(String var0, String var1) {
      return method9225(var0, var1, null);
   }

   public static byte[] method9225(String var0, String var1, JSONObject var2) {
      if (!var0.matches("[0-9a-f]{64}")) {
         throw bj("invalid texture digest");
      }

      if (var1.length() > 16777216) {
         throw bj("texture exceeds metadata budget");
      }

      byte[] var3;
      try {
         var3 = Base64.getDecoder().decode(var1);
      } catch (IllegalArgumentException var6) {
         throw bj("invalid texture Base64");
      }

      if (!Base64.getEncoder().encodeToString(var3).equals(var1)) {
         throw bj("noncanonical texture Base64");
      }

      if (var3.length < Uh.length) {
         throw bj("texture is not a PNG");
      }

      for (int var4 = 0; var4 < Uh.length; var4++) {
         if (var3[var4] != Uh[var4]) {
            throw bj("texture is not a PNG");
         }
      }

      try {
         MessageDigest var7 = MessageDigest.getInstance("SHA-256");
         var7.update(var3);
         if (var2 != null) {
            var7.update((byte)0);
            var7.update(AnimationBinaryCodec.method9185(var2).getBytes(StandardCharsets.UTF_8));
         }

         if (!HexFormat.of().formatHex(var7.digest()).equals(var0)) {
            throw bj("texture digest mismatch");
         } else {
            return var3;
         }
      } catch (NoSuchAlgorithmException var5) {
         throw new AssertionError(var5);
      }
   }

   private static IllegalArgumentException bj(String var0) {
      return new IllegalArgumentException("Invalid IAD2: " + var0);
   }
}
