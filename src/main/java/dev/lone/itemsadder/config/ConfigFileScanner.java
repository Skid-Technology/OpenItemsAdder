package dev.lone.itemsadder.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.resourcepack.ResourcePackWriter;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Nullable;

public final class ConfigFileScanner {
   private final Cache field33 = CacheBuilder.newBuilder().expireAfterWrite(10L, TimeUnit.MINUTES).build();

   Collection method1901() {
      return this.field33.asMap().values();
   }

   Map method1902() {
      return this.field33.asMap();
   }

   void method1903(String var1, String var2, boolean var3, boolean var4) {
      List var5 = this.method1904(var1, var4);
      if (var3) {
         var5.addAll(this.method1904(var2, var4));
      }

      if (var4) {
         this.field33.asMap().entrySet().removeIf(var1x -> !var5.contains(var1x.getKey()));
      }
   }

   private List method1904(String var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      File var4 = new File(var1);
      if (!var4.exists()) {
         return var3;
      }

      try (Stream var5 = Files.walk(var4.toPath(), FileVisitOption.FOLLOW_LINKS)) {
         var5.forEach(var3x -> {
            if (FilenameUtils.getExtension(var3x.toString()).equals("yml")) {
               if (!var3x.toString().contains("conversion_backup")) {
                  boolean var4x = true;
                  String var5x = var3x.toString();
                  var3.add(var5x);
                  if (var2) {
                     YamlConfig var6 = (YamlConfig)this.field33.getIfPresent(var5x);
                     if (var6 != null && var6.lastModified() == var3x.toFile().lastModified()) {
                        var4x = false;
                     }
                  }

                  if (var4x) {
                     if (var2) {
                        this.field33.invalidate(var5x);
                     }

                     YamlConfig var8 = new YamlConfig(Main.getInstance(), false, var5x, false, false, true, false);
                     if (var8.isLoaded() && var8.getBoolean("info.enabled", true)) {
                        if (!method1905(var8.getString("info.namespace"), var8)) {
                           return;
                        }

                        String var7 = Utils.relativePath(var3x.toFile());
                        if (var2) {
                           if (Settings.get().logConfigReloading) {
                              Log.log("Detected file changes: " + var7);
                           }
                        } else if (Settings.get().logConfigLoading) {
                           Log.log("Found file: " + var7);
                        }

                        var8.lastModified();
                        this.field33.put(var5x, var8);
                     }
                  }
               }
            }
         });
         return var3;
      } catch (IOException var10) {
         throw new RuntimeException("Failed to find YML configurations files.", var10);
      }
   }

   static boolean method1905(@Nullable String var0, YamlConfig var1) {
      if (var0 == null) {
         Log.error("Missing namespace in file: " + var1.DJ());
         return false;
      } else {
         List var2 = ResourcePackWriter.findInvalidIdChars(var0);
         if (var2 != null) {
            Log.error("Invalid namespace characters: '" + Utils.joinComma(var2) + "'");
            Log.error("Do not use special or UPPERCASE characters. File: " + var1.DJ());
            ResourcePackWriter.printAllowedCharacters();
            return false;
         } else {
            return true;
         }
      }
   }
}
