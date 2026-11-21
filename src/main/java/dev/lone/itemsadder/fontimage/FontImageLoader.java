package dev.lone.itemsadder.fontimage;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.gui.GuiEntryBuilder;
import dev.lone.itemsadder.gui.GuiEntryRecord;
import dev.lone.itemsadder.resourcepack.PackBuildWorker;
import dev.lone.itemsadder.resourcepack.ResourcePackWriter;
import dev.lone.itemsadder.util.BossBarManager;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import dev.lone.itemsadder.world.ChunkBlockHookHolder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.Nullable;

public class FontImageLoader {
   private static final Map asx = new HashMap();
   private static final Map asy = new HashMap();
   private static volatile Set asz = Set.of();

   public static boolean method1929(NamespacedKey var0) {
      return asz.contains(var0.toString());
   }

   @Nullable
   public static NamespacedKey method1930(@Nullable NamespacedKey var0) {
      return var0 != null && method1929(var0) ? null : var0;
   }

   public static boolean method1931(@Nullable Float var0) {
      return var0 != null && Float.isFinite(var0) && var0 > 0.0F;
   }

   public static File Gj() {
      return new File(Main.getInstance().getDataFolder(), "storage" + File.separator + "cache" + File.separator + "tmp_ogg");
   }

   public static YamlConfig Gk() {
      File var0 = Gj();
      YamlConfig var1 = new YamlConfig(Main.getInstance(), true, "storage/cache/tmp_ogg/info.yml", false, false);
      if (!var0.exists() && !var0.mkdirs()) {
         Log.error("Failed to create tmp folder for ogg sounds: " + var0.getAbsolutePath());
      }

      return var1;
   }

   public static ConfigSectionWrapper method1932(YamlConfig var0, File var1) {
      return var0.kd(Utils.sha256Hex(var1.getAbsolutePath()));
   }

   public static ConfigSectionWrapper method1933(File var0) {
      return Gk().kd(Utils.sha256Hex(var0.getAbsolutePath()));
   }

   @Nullable
   public static Float method1934(YamlConfig var0, File var1) {
      ConfigSectionWrapper var2 = method1932(var0, var1);
      if (var1.isFile()
         && var2.method7081("duration_last_modified", var2.method7081("last_modified", -1L)) == var1.lastModified()
         && var2.method7081("duration_source_size", var1.length()) == var1.length()) {
         Float var3 = var2.method7101("duration", null);
         return method1931(var3) ? var3 : null;
      } else {
         return null;
      }
   }

   public static void cD() {
      PackBuildWorker var0 = PackBuildWorker.getInstance();
      boolean var1 = var0 != null && var0.isAvailable();
      LinkedHashSet var2 = new LinkedHashSet();
      YamlConfig var3 = null;
      boolean var4 = false;

      try {
         asx.clear();
         asy.clear();
         ContentManager var5 = ContentManager.method8084();

         for (YamlConfig var7 : var5.method8092()) {
            String var8 = var7.getString("info.namespace");
            var5.as.add(var8);

            YamlConfig var9;
            try {
               var9 = var7.method7046('~');
            } catch (IOException | InvalidConfigurationException var46) {
               Log.error("[Items] Failed to load sounds config file: " + var7.GQ(), var46);
               continue;
            }

            List var10 = var9.getStringList("sounds");
            if (var10.isEmpty()) {
               if (Settings.get().isLoggingEnabled("logging.sounds.loading_empty")) {
                  Log.log(ChatColor.YELLOW + "    No sound found in file " + var7.GQ());
               }
            } else {
               BossBarManager.setTitleKeepProgress("Sounds '" + var8 + "'...");

               for (String var12 : var10) {
                  ConfigSectionWrapper var13 = var7.kc("sounds." + var12);
                  assert var13 != null;
                  if (!var13.method7078("enabled", true)) {
                     if (Settings.get().isLoggingEnabled("logging.sounds.loading_disabled")) {
                        Log.log("    Ignored disabled sound: " + var12 + ", namespace: " + var8);
                     }
                  } else {
                     if (Settings.get().isLoggingEnabled("logging.sounds.loading")) {
                        Log.log("    Loading sound: " + var12);
                     }

                     if (!ResourcePackWriter.isValidNamespace(var12)) {
                        Log.error("[Items] Non [a-z0-9_.-] character in sound '" + var8 + ":" + var12 + "' in yml file: " + var7.DJ());
                        ResourcePackWriter.printAllowedCharacters();
                     } else {
                        String var14 = var13.bc("settings.subtitle", null);
                        Boolean var15 = var13.method7104("settings.replace", null);
                        Float var16 = var13.method7101("settings.volume", null);
                        Float var17 = var13.method7101("settings.pitch", null);
                        Integer var18 = var13.method7077("settings.weight", null);
                        Boolean var19 = var13.method7104("settings.stream", null);
                        Integer var20 = var13.method7077("settings.attenuation_distance", null);
                        Boolean var21 = var13.method7104("settings.preload", null);
                        Float var22 = var13.method7101("settings.duration_seconds", null);
                        Integer var23 = var13.method7077("settings.bitrate_quality", Settings.get().compressSounds ? Settings.get().soundsQualityPercent : null);
                        Boolean var24 = var13.method7104("settings.mono", null);
                        Boolean var25 = null;
                        if (var13.jI("jukebox")) {
                           var25 = var13.method7078("jukebox.enabled", true);
                        }

                        String var26 = var13.bc("jukebox.description", null);
                        Integer var27 = var13.method7076("jukebox.comparator_output", 15);
                        GuiEntryBuilder var28 = new GuiEntryBuilder(var8, var12).dq(var14).method1942(var15).method1945(var25).dr(var26).method1946(var27);

                        for (String var31 : var13.bJ(false)) {
                           if (var31.startsWith("variant")) {
                              ConfigSectionWrapper var32 = var13.kc(var31);
                              assert var32 != null;
                              String var33 = var32.bc("path", null);
                              if (var33 == null) {
                                 Log.error("[Items] No path found for sound '" + var8 + ":" + var12 + "' in yml file: " + var7.DJ());
                              } else if (!ResourcePackWriter.hasValidPathChars(var33)) {
                                 Log.error("[Items] Non [a-z0-9_.-] character in sound '" + var8 + ":" + var12 + "' in yml file: " + var7.DJ());
                                 ResourcePackWriter.printAllowedCharacters();
                              } else {
                                 Float var34 = var32.method7101("volume", var16);
                                 Float var35 = var32.method7101("pitch", var17);
                                 Integer var36 = var32.method7077("weight", var18);
                                 Boolean var37 = var32.method7104("stream", var19);
                                 Integer var38 = var32.method7077("attenuation_distance", var20);
                                 Boolean var39 = var32.method7104("preload", var21);
                                 Float var40 = var32.method7101("duration_seconds", var22);
                                 Integer var41 = var32.method7077("bitrate_quality", var23);
                                 Boolean var42 = var32.method7104("mono", var24);
                                 method1938(var7, var8, var12, var28, var33, var34, var35, var36, var37, var38, var39, var40, var41, var42);
                              }
                           }
                        }

                        if (var13.kf("path") instanceof String var48) {
                           method1935(var7, var8, var12, var16, var17, var18, var19, var20, var21, var22, var23, var24, var28, var48);
                        }

                        List var49 = var13.jZ("paths");
                        if (!var49.isEmpty()) {
                           for (String var54 : var49) {
                              method1935(var7, var8, var12, var16, var17, var18, var19, var20, var21, var22, var23, var24, var28, var54);
                           }
                        }

                        if (var28.asI != null && !var28.asI.isEmpty()) {
                           if (Boolean.TRUE.equals(var25)) {
                              FontImageSettingsBuilder var52 = (FontImageSettingsBuilder)var28.asI.getFirst();
                              if (!method1931(var52.asT)) {
                                 if (var3 == null) {
                                    var3 = Gk();
                                 }

                                 var4 |= method1936(var52, var8, var12, var7, var3, var1 ? var0 : null);
                              }
                           }

                           method1940("Loaded sound: " + var12 + ". File: " + var7.DJ());
                           GuiEntryRecord var53 = var28.Gv();
                           if (Boolean.TRUE.equals(var53.Gr()) && !var53.Gm()) {
                              var2.add(var53.cV());
                           }

                           asy.computeIfAbsent(var8, var0x -> new ArrayList<>()).add(var53);
                        } else {
                           Log.error("[Items] Failed to find any variant for sound '" + var8 + ":" + var12 + "' in yml file: " + var7.DJ());
                           if (Boolean.TRUE.equals(var25)) {
                              var2.add(var8 + ":" + var12);
                           }
                        }
                     }
                  }
               }
            }
         }

         asz = Set.copyOf(var2);
         if (!var2.isEmpty()) {
            Log.warn(
               "[Sounds] "
                  + var2.size()
                  + " jukebox song(s) disabled because audio or a valid duration is missing: "
                  + String.join(", ", var2.stream().limit(10L).toList())
                  + (var2.size() > 10 ? ", ..." : "")
                  + ". Ordinary sounds remain available. FFmpeg: "
                  + (var0 == null ? "unavailable" : var0.getStatus())
                  + ". Check the audio files and duration_seconds, or run /iazip when FFmpeg is ready."
            );
            if (var0 != null && !var1) {
               var0.requestNotice();
            }
         }

         Gl().forEach((var0x, var1x) -> ChunkBlockHookHolder.getInstance().hook.injectSoundsIntoRegistry(var1x.stream().filter(GuiEntryRecord::Gm).toList()));
      } finally {
         if (var4 && var3 != null) {
            var3.save();
         }

         asx.clear();
      }
   }

   private static void method1935(
      YamlConfig var0,
      String var1,
      String var2,
      @Nullable Float var3,
      @Nullable Float var4,
      @Nullable Integer var5,
      @Nullable Boolean var6,
      @Nullable Integer var7,
      @Nullable Boolean var8,
      @Nullable Float var9,
      @Nullable Integer var10,
      @Nullable Boolean var11,
      GuiEntryBuilder var12,
      String var13
   ) {
      method1938(var0, var1, var2, var12, var13, var3, var4, var5, var6, var7, var8, var9, var10, var11);
   }

   private static boolean method1936(FontImageSettingsBuilder var0, String var1, String var2, YamlConfig var3, YamlConfig var4, @Nullable PackBuildWorker var5) {
      var0.method1953(null);
      File var6 = var0.asM;
      if (asx.containsKey(var6)) {
         var0.method1953((Float)asx.get(var6));
         return false;
      }

      boolean var7 = false;
      Float var8 = method1934(var4, var6);
      if (var8 == null && var5 != null && !Thread.currentThread().isInterrupted()) {
         try {
            long var9 = var6.lastModified();
            long var11 = var6.length();
            float var13 = (float)var5.probeDuration(var6);
            if (!method1931(var13)) {
               throw new IOException("Invalid duration: " + var13);
            }

            if (var9 != var6.lastModified() || var11 != var6.length()) {
               throw new IOException("Source changed while reading audio duration");
            }

            var8 = var13;
            ConfigSectionWrapper var14 = method1932(var4, var6);
            var14.method7074("duration", var8);
            var14.method7074("duration_last_modified", var9);
            var14.method7074("duration_source_size", var11);
            var14.method7074("path_source", var6.getAbsolutePath());
            var7 = true;
         } catch (InterruptedException var15) {
            Thread.currentThread().interrupt();
         } catch (IOException var16) {
            Log.warn("[Sounds] Failed to read duration of '" + var1 + ":" + var2 + "' (" + var3.DJ() + "): " + var16.getMessage());
         }
      }

      asx.put(var6, var8);
      var0.method1953(var8);
      return var7;
   }

   public static void method1937(YamlConfig var0) {
      Path var1 = Gj().toPath().toAbsolutePath().normalize();

      for (String var3 : var0.abO()) {
         ConfigSectionWrapper var4 = var0.kc(var3);
         assert var4 != null;
         String var5 = var4.jO("path_source");
         if (var5 == null) {
            var0.method7048(var3, null);
         } else {
            File var6 = new File(var5);
            if (!var6.exists()) {
               method1940("Deleting cache file " + var3 + " because the original file doesn't exist anymore.");
               String var7 = var4.jO("path_destination");

               try {
                  if (var7 != null) {
                     Path var8 = new File(var7).toPath().toAbsolutePath().normalize();
                     if (var8.startsWith(var1) && !var8.equals(var1) && !var8.equals(var6.toPath().toAbsolutePath().normalize())) {
                        Files.deleteIfExists(var8);
                     }
                  }
               } catch (Exception var9) {
                  Log.error("Failed to delete cache file " + var3 + ". " + var9.getMessage());
               }

               var0.method7048(var3, null);
            }
         }
      }
   }

   private static @Nullable FontImageSettingsBuilder method1938(
      YamlConfig var0,
      String var1,
      String var2,
      GuiEntryBuilder var3,
      String var4,
      @Nullable Float var5,
      @Nullable Float var6,
      @Nullable Integer var7,
      @Nullable Boolean var8,
      @Nullable Integer var9,
      @Nullable Boolean var10,
      @Nullable Float var11,
      @Nullable Integer var12,
      @Nullable Boolean var13
   ) {
      if (!ResourcePackWriter.hasValidPathChars(var4)) {
         Log.error("[Items] Non [a-z0-9_.-] character in sound '" + var1 + ":" + var2 + "' in yml file: " + var0.DJ());
         ResourcePackWriter.printAllowedCharacters();
         return null;
      }

      File var14 = method1939(var2, var1, var4, var0);
      if (var14 == null) {
         return null;
      }

      if (var4.endsWith(".ogg")) {
         var4 = var4.substring(0, var4.length() - 4);
      }

      var4 = var4.substring(var4.indexOf(58) + 1);
      if (var12 != null && (var12 < 1 || var12 > 100)) {
         Log.warn("[Sounds] Invalid bitrate_quality for '" + var1 + ":" + var2 + "'; expected 1..100. Ignoring this option. File: " + var0.DJ());
         var12 = null;
      }

      FontImageSettingsBuilder var15 = new FontImageSettingsBuilder(var4, var14, var5, var6, var7, var8, var9, var10, var11, var12, var13);
      var3.method1944(var15);
      return var15;
   }

   private static File method1939(String var0, String var1, String var2, YamlConfig var3) {
      if (!ResourcePackWriter.hasValidPathChars(var2)) {
         Log.error("[Items] Non [a-z0-9_.-] character in sound '" + var1 + ":" + var0 + "' in yml file: " + var3.DJ());
         ResourcePackWriter.printAllowedCharacters();
         return null;
      }

      if (var2.endsWith(".ogg")) {
         var2 = var2.substring(0, var2.length() - 4);
      }

      String[] var4 = var2.split(":");
      if (var4.length > 1 && !var4[0].equals(var1)) {
         Log.error(
            "[Items] Namespace mismatch in sound '"
               + var1
               + ":"
               + var0
               + "' ('"
               + var2
               + "'). No need to specify anything or use the correct one. File: "
               + var3.DJ()
         );
         return null;
      }

      String var5;
      if (var4.length > 1) {
         var5 = var4[1];
      } else {
         var5 = var4[0];
      }

      File var6 = ContentManager.method8198("assets/" + var1 + "/sounds/" + var5 + ".ogg");
      if (var6 != null && var6.exists()) {
         return var6;
      }

      Log.error("[Items] Sound file not found: " + var2 + ". File: " + var3.DJ());
      return null;
   }

   public static Map Gl() {
      return asy;
   }

   private static void method1940(String var0) {
   }
}
