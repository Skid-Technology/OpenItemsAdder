package dev.lone.itemsadder.util;

import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.resourcepack.ZipEntryUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JsonFileMerger {
   private static void deleteRecursively(File var0) {
      if (var0 != null && var0.exists()) {
         if (var0.isDirectory()) {
            File[] var1 = var0.listFiles();
            if (var1 == null) {
               return;
            }

            for (File var5 : var1) {
               deleteRecursively(var5);
            }
         }

         try {
            Files.delete(var0.toPath());
         } catch (IOException var6) {
            Log.warn("Failed to delete file: " + var0.getAbsolutePath(), var6);
         }
      }
   }

   public static void mergeZip(File var0, File var1) {
      mergeZipAndRemap(var0, var1, false);
   }

   public static void mergeZipAndRemap(File var0, File var1, boolean var2) {
      deleteRecursively(var1);
      if (!var1.exists()) {
         var1.mkdirs();
      }

      HashMap var3 = new HashMap();
      HashMap var4 = new HashMap();
      HashMap var5 = new HashMap();

      try (ZipFile var6 = new ZipFile(var0)) {
         Enumeration var7 = var6.entries();

         while (var7.hasMoreElements()) {
            ZipEntry var8 = (ZipEntry)var7.nextElement();
            File var9 = new File(var1, var8.getName());

            try {
               if (!var9.getCanonicalPath().startsWith(var1.getCanonicalPath())) {
                  Log.warn("Skipping suspicious entry: " + var8.getName());
               } else if (var8.isDirectory()) {
                  var9.mkdirs();
               } else {
                  var9.getParentFile().mkdirs();

                  try (
                     InputStream var10 = var6.getInputStream(var8);
                     FileOutputStream var11 = new FileOutputStream(var9);
                  ) {
                     byte[] var12 = new byte[4096];

                     int var13;
                     while ((var13 = var10.read(var12)) > 0) {
                        var11.write(var12, 0, var13);
                     }
                  }

                  if (var9.getName().endsWith(".json")) {
                     try {
                        JSONObject var35 = JsonHelper.readJsonFile(var9);
                        var35 = ZipEntryUtils.stripDecoys(var35);
                        if (var2) {
                           String var40 = var8.getName();
                           int var44 = var40.indexOf("assets/");
                           if (var44 >= 0 && (var44 == 0 || var40.charAt(var44 - 1) == '/')) {
                              String var48 = var40.substring(0, var44);
                              String var14 = var40.substring(var44 + "assets/".length());
                              int var15 = var14.indexOf(47);
                              if (var15 >= 0) {
                                 String var16 = var14.substring(var15 + 1);
                                 if (var16.startsWith("atlases/")) {
                                    var4.put(var9, var48);
                                 } else if (var16.startsWith("models/")) {
                                    var5.put(var9, var48);
                                 }
                              }
                           }
                        }

                        JsonHelper.writeJsonFile(var9, var35);
                     } catch (Exception var26) {
                        Log.warn("Failed to process JSON file: " + var9.getAbsolutePath() + ". It may be obfuscated or corrupted.", var26);
                     }
                  }
               }
            } catch (IOException var29) {
               Log.warn("Skipping corrupted entry: " + var8.getName(), var29);
            }
         }

         if (var2) {
            HashMap var32 = new HashMap();

            for (Entry var37 : var4.entrySet()) {
               if (!((String)var37.getValue()).isEmpty() && ((File)var37.getKey()).getName().equals("blocks.json")) {
                  JSONObject var41 = JsonHelper.readJsonFile((File)var37.getKey());
                  if (var41.getJSONArray("sources") != null) {
                     for (Object var49 : var41.getJSONArray("sources")) {
                        if (var49 instanceof JSONObject var51 && "single".equals(var51.getString("type"))) {
                           var32.computeIfAbsent(var51.getString("sprite"), var0x -> new HashSet<>()).add(var51.getString("resource"));
                        }
                     }
                  }
               }
            }

            NameRegistry var34 = new NameRegistry();

            for (Entry var42 : var4.entrySet()) {
               JSONObject var46 = JsonHelper.readJsonFile((File)var42.getKey());
               remapAtlasJson(
                  var46,
                  var3.computeIfAbsent((String)var42.getValue(), var0x -> new HashMap()),
                  ((File)var42.getKey()).getName().equals("blocks.json") ? var32 : Map.of(),
                  var34
               );
               JsonHelper.writeJsonFile((File)var42.getKey(), var46);
            }

            for (Entry var43 : var5.entrySet()) {
               JSONObject var47 = JsonHelper.readJsonFile((File)var43.getKey());
               JSONObject var50 = var47.getJSONObject("textures");
               if (var50 != null) {
                  Map var52 = var3.getOrDefault(var43.getValue(), Map.of());
                  Map var53 = var3.getOrDefault("", Map.of());
                  boolean var54 = false;

                  for (Entry var18 : var50.entrySet()) {
                     Object var19 = var18.getValue();
                     String var20 = var19 instanceof JSONObject var22 ? var22.getString("sprite") : (var19 instanceof String var21 ? var21 : null);
                     if (var20 != null) {
                        String var55 = (String)var52.getOrDefault(var20, (String)var53.get(var20));
                        if (var55 != null) {
                           if (var19 instanceof JSONObject var56) {
                              var56.put("sprite", var55);
                           } else {
                              var18.setValue(var55);
                           }

                           var54 = true;
                        }
                     }
                  }

                  if (var54) {
                     JsonHelper.writeJsonFile((File)var43.getKey(), var47);
                  }
               }
            }
         }
      } catch (IOException var31) {
         Log.error("Failed to extract zip file: " + var0.getAbsolutePath(), var31);
      }
   }

   private static void remapAtlasJson(JSONObject var0, Map var1, Map var2, NameRegistry var3) {
      if (var0.getJSONArray("sources") != null) {
         for (Object var5 : var0.getJSONArray("sources")) {
            if (var5 instanceof JSONObject var6 && "single".equals(var6.getString("type"))) {
               String var7 = var6.getString("sprite");
               String var8 = var6.getString("resource");
               if (var7 != null && var7.matches("ia:[0-9]+") && var8 != null) {
                  String var9 = var3.generateObfuscatedName(var8, false, var2.getOrDefault(var7, Set.of()).contains(var8));
                  var1.put(var7, var9);
                  var6.put("sprite", var9);
               }
            }
         }
      }
   }
}
