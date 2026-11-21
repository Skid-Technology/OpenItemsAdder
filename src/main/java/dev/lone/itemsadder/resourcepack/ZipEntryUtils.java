package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.JSONWriter.Feature;
import dev.lone.itemsadder.util.BinaryPackCodec;
import dev.lone.itemsadder.util.BinaryPackResult;
import dev.lone.itemsadder.util.ImageOptimizeStatus;
import dev.lone.itemsadder.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.Random;
import java.util.zip.ZipEntry;
import org.bukkit.Material;

public class ZipEntryUtils {
   private static final byte[] LOCAL_HEADER_SIG = new byte[]{80, 75, 3, 4};
   private static final byte[] CENTRAL_HEADER_SIG = new byte[]{80, 75, 1, 2};
   private static final byte[] END_OF_CENTRAL_SIG = new byte[]{80, 75, 5, 6};
   private static final byte[] CENTRAL_MARKER = new byte[]{5, 57};
   private static final byte[] ZERO_4 = new byte[]{0, 0, 0, 0};
   private static final byte[] FILLER_NAME = buildFillerName(255);
   public static Random random = new Random();
   private static final FileTime EPOCH_TIME = FileTime.fromMillis(0L);

   public static void normalizeEntry(ZipEntry var0) {
      var0.setCrc(0L);
      var0.setLastAccessTime(EPOCH_TIME);
      var0.setCreationTime(EPOCH_TIME);
      var0.setLastModifiedTime(EPOCH_TIME);
      if (var0.getName().toLowerCase().endsWith(".ogg")) {
         var0.setSize(randomBetween(9000, 15000));
      } else {
         var0.setSize(1337L);
      }
   }

   public static int randomBetween(int var0, int var1) {
      return var1 == var0 ? var1 : random.nextInt(var1 - var0 + 1) + var0;
   }

   private static byte[] buildFillerName(int var0) {
      byte[] var1 = new byte[var0];

      for (int var2 = 0; var2 < var0; var2++) {
         var1[var2] = 92;
      }

      if (var0 > 4) {
         var1[0] = 46;
         var1[1] = 46;
         var1[2] = 47;
      }

      return var1;
   }

   public static void scrubZipMetadata(File var0) {
      try (RandomAccessFile var1 = new RandomAccessFile(var0, "rw")) {
         try {
            byte[] var2 = new byte[8192];
            int var5 = 0;

            while (var5 < var1.length()) {
               int var6 = var1.read(var2);
               var5 += var6;
               long var3 = var1.getFilePointer() - var6;

               for (int var7 = 0; var7 < var6 - 6; var7++) {
                  if (var2[var7] == LOCAL_HEADER_SIG[0]
                     && var2[var7 + 1] == LOCAL_HEADER_SIG[1]
                     && var2[var7 + 2] == LOCAL_HEADER_SIG[2]
                     && var2[var7 + 3] == LOCAL_HEADER_SIG[3]) {
                     if (var7 + 30 < var6) {
                        int var8 = var2[var7 + 4] & 255 | (var2[var7 + 5] & 255) << 8;
                        if (var8 >= 10 && var8 <= 63) {
                           int var9 = var2[var7 + 8] & 255 | (var2[var7 + 9] & 255) << 8;
                           if (var9 == 8 || var9 == 0) {
                              int var10 = var2[var7 + 26] & 255 | (var2[var7 + 27] & 255) << 8;
                              if (var10 != 0 && var10 <= 260 && var7 + 30 + var10 <= var6) {
                                 var1.seek(var3 + var7 + 18L);
                                 var1.write(ZERO_4);
                                 var1.seek(var3 + var7 + 22L);
                                 var1.write(ZERO_4);
                                 var1.seek(var3 + var7 + 30L);
                                 int var11 = Math.min(FILLER_NAME.length, var10);
                                 var1.write(FILLER_NAME, 0, var11);

                                 for (int var12 = var11; var12 < var10; var12++) {
                                    var1.write(0);
                                 }

                                 var7 += 98;
                              }
                           }
                        }
                     }
                  } else if (var2[var7] == CENTRAL_HEADER_SIG[0]
                     && var2[var7 + 1] == CENTRAL_HEADER_SIG[1]
                     && var2[var7 + 2] == CENTRAL_HEADER_SIG[2]
                     && var2[var7 + 3] == CENTRAL_HEADER_SIG[3]) {
                     var1.seek(var3 + var7 + 34L);
                     var1.write(CENTRAL_MARKER);
                     var7 += 34;
                  }
               }

               if (var3 + var6 - 4L < var1.length()) {
                  var1.seek(var3 + var6 - 4L);
               }
            }
         } catch (Throwable var14) {
            Log.error("Failed to protect zip file.", var14);
         }
      } catch (IOException var16) {
         Log.error("Failed to close zip file after protection.", var16);
      }
   }

   @Deprecated
   public static byte[] protectFile(File var0) {
      byte[] var1 = Files.readAllBytes(var0.toPath());
      BinaryPackResult var2 = BinaryPackCodec.protect(var1);
      if (var2.VS() != ImageOptimizeStatus.APPLIED && var2.VS() != ImageOptimizeStatus.ALREADY_PROCESSED) {
         throw new IOException(var2.Ue());
      } else {
         return var2.Ud();
      }
   }

   @Deprecated
   public static byte[] obfuscateModelJson(JSONObject var0) {
      JSONObject var1 = new JSONObject();
      if (var0.containsKey("parent")) {
         String var2 = "parent\u200b";
         String var3 = randomMaterialName();
         var1.put(var2, "minecraft:item/" + var3);
      }

      if (var0.containsKey("textures")) {
         String var17 = "textures\u200b";
         JSONObject var20 = new JSONObject();
         var20.put("layer0", "minecraft:item/" + randomMaterialName());
         var1.put(var17, var20);
      }

      for (String var21 : var0.keySet()) {
         Object var4 = var0.get(var21);
         if (var21.equals("parent") && var4 instanceof String var5) {
            String var23 = escapeLetters(var21);
            String var25 = escapeLetters(var5);
            var1.put(var23, var25);
         } else if (var21.equals("textures") && var4 instanceof JSONObject var6) {
            JSONObject var7 = new JSONObject();

            for (String var9 : var6.keySet()) {
               Object var10 = var6.get(var9);
               String var11 = escapeLetters(var9);
               Object var12 = var10 instanceof String var13 ? escapeLetters(var13) : var10;
               var7.put(var11, var12);
            }

            String var24 = escapeLetters(var21);
            var1.put(var24, var7);
            if (Math.random() < 0.3) {
               var1.put(appendDecoy(var21), var7);
            }
         } else {
            var1.put(var21, var4);
         }
      }

      try {
         JSONWriter var19 = JSONWriter.of(new Feature[]{Feature.LargeObject});

         byte[] var22;
         try {
            var19.setRootObject(var1);
            var19.write(var1);
            var22 = var19.toString().replace("\\\\", "\\").getBytes(StandardCharsets.UTF_8);
         } catch (Throwable var15) {
            if (var19 != null) {
               try {
                  var19.close();
               } catch (Throwable var14) {
                  var15.addSuppressed(var14);
               }
            }

            throw var15;
         }

         if (var19 != null) {
            var19.close();
         }

         return var22;
      } catch (Throwable var16) {
         Log.error("Failed to obfuscate JSON file. Returning non-obfuscated JSON as fallback.", var16);
         return var0.toString().getBytes(StandardCharsets.UTF_8);
      }
   }

   private static String escapeLetters(String var0) {
      StringBuilder var1 = new StringBuilder();

      for (char var5 : var0.toCharArray()) {
         if (Character.isLetter(var5)) {
            var1.append(String.format("\\u%04x", Integer.valueOf(var5)));
         } else {
            var1.append(var5);
         }
      }

      return var1.toString();
   }

   private static String appendDecoy(String var0) {
      return var0 + "\u200b";
   }

   private static String randomMaterialName() {
      Material[] var0 = Material.values();
      Material var1 = var0[(int)(Math.random() * var0.length)];
      return var1.name().toLowerCase();
   }

   private static String unescapeUnicode(String var0) {
      StringBuilder var1 = new StringBuilder();
      int var2 = 0;

      while (true) {
         while (true) {
            if (var2 >= var0.length()) {
               return var1.toString();
            }

            if (var0.charAt(var2) != '\\' || var2 + 5 >= var0.length() || var0.charAt(var2 + 1) != 'u') {
               break;
            }

            String var3 = var0.substring(var2 + 2, var2 + 6);

            try {
               int var4 = Integer.parseInt(var3, 16);
               var1.append((char)var4);
               var2 += 6;
            } catch (NumberFormatException var5) {
               break;
            }
         }

         var1.append(var0.charAt(var2));
         var2++;
      }
   }

   public static JSONObject stripDecoys(JSONObject var0) {
      JSONObject var1 = new JSONObject();

      for (String var3 : var0.keySet()) {
         if (!var3.endsWith("\u200b")) {
            Object var4 = var0.get(var3);
            String var5 = unescapeUnicode(var3);
            if (var4 instanceof String var6) {
               String var8 = unescapeUnicode(var6);
               var1.put(var5, var8);
            } else if (var4 instanceof JSONObject var7) {
               var1.put(var5, stripDecoys(var7));
            } else {
               var1.put(var5, var4);
            }
         }
      }

      return var1;
   }
}
