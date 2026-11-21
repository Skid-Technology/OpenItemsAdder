package dev.lone.itemsadder.core;

import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.resourcepack.ResourcePackWriter;
import dev.lone.itemsadder.script.CompiledScript;
import dev.lone.itemsadder.script.ScriptCompilation;
import dev.lone.itemsadder.util.BossBarManager;
import dev.lone.itemsadder.util.Log;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;

public class NamespaceRegistrar {
   static Map asv = new HashMap();

   public static void bf(boolean var0) {
      if (var0) {
         Gi();
      }

      ContentManager var1 = ContentManager.method8084();

      for (YamlConfig var3 : var1.method8092()) {
         String var4 = var3.getString("info.namespace");
         var1.as.add(var4);
         List var5 = var3.getStringList("scripts");
         if (var5.isEmpty()) {
            if (Settings.get().isLoggingEnabled("logging.scripts.loading_empty")) {
               Log.log(ChatColor.YELLOW + "    No script found in file " + var3.GQ());
            }
         } else {
            BossBarManager.setTitleKeepProgress("Scripts '" + var4 + "'...");

            for (String var7 : var5) {
               ConfigSectionWrapper var8 = var3.kc("scripts." + var7);
               assert var8 != null;
               if (!var8.method7078("enabled", true)) {
                  if (Settings.get().isLoggingEnabled("logging.scripts.loading_disabled")) {
                     Log.log("    Ignored disabled script: " + var7 + ", namespace: " + var4);
                  }
               } else {
                  if (Settings.get().isLoggingEnabled("logging.scripts.loading")) {
                     Log.log("    Loading script: " + var7);
                  }

                  if (!ResourcePackWriter.hasValidPathChars(var7)) {
                     Log.error("[Items] Non [a-z0-9_.-] character in script '" + var4 + ":" + var7 + "' in yml file: " + var3.DJ());
                     ResourcePackWriter.printAllowedCharacters();
                  } else {
                     String var9 = var8.jO("path");
                     if (var9 == null) {
                        Log.error("[Items] No path found for script '" + var4 + ":" + var7 + "' in yml file: " + var3.DJ());
                     } else {
                        String[] var10 = new String[]{".java", ".jspp", ".js"};
                        File var11 = null;

                        for (String var15 : var10) {
                           String var16 = var9.endsWith(var15) ? var9 : var9 + var15;
                           var11 = new File(new File(ContentManager.field73, var4), var16);
                           if (var11.exists()) {
                              break;
                           }
                        }

                        if (!var11.exists()) {
                           Log.error("[Items] Script '" + var4 + ":" + var7 + "' not found in yml file: " + var3.DJ());
                        } else {
                           String var26 = var4 + ":" + var7;
                           if (asv.containsKey(var26)) {
                              Log.warn("[Scripts] '" + var26 + "' is already loaded. Skipping (" + var11 + ")");
                           } else {
                              String var27 = null;
                              FileTime var28 = null;

                              try {
                                 var27 = Files.readString(var11.toPath());
                                 var28 = Files.getLastModifiedTime(var11.toPath());
                              } catch (IOException var24) {
                                 var24.printStackTrace();
                              }

                              if (var27 != null) {
                                 String var29 = var26;
                                 if (var11.getName().endsWith(".java")) {
                                    if (var29.contains(":")) {
                                       var29 = var29.substring(var29.indexOf(58) + 1);
                                    }
                                 } else {
                                    var29 = var29.replace(":", "_").replace("-", "_").replace(".", "_");
                                 }

                                 try {
                                    ScriptCompilation var30 = ContentManager.method8084().field90.method2290(var11, var27, var29, var28);
                                    if (var30.vz()) {
                                       asv.put(var26, var30.KA());
                                       Log.log("[Scripts] '" + var26 + "' loaded in " + var30.KD() + "ms");
                                    } else {
                                       StringBuilder var17 = new StringBuilder();

                                       for (String var19 : var30.KB()) {
                                          var17.append(var19).append("\n");
                                       }

                                       Log.warn("Errors compiling script: " + var11 + "\n" + var17);

                                       for (Throwable var32 : var30.KC()) {
                                          if (var32 != null) {
                                             Log.warn("Trace: " + var32.getMessage());

                                             for (StackTraceElement var23 : var32.getStackTrace()) {
                                                Log.warn("    at " + var23);
                                             }
                                          }
                                       }

                                       if (var30.KE()) {
                                          Log.error("[Scripts] '" + var26 + "' is disabled due to serious compilation errors");
                                          var8.method7074("enabled", false);
                                          var3.save();
                                       }
                                    }
                                 } catch (Exception var25) {
                                    Log.warn("Unexpected exception compiling script: " + var11, var25);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static void Gi() {
      asv.clear();
   }

   public static CompiledScript do_(String var0) {
      return (CompiledScript)asv.get(var0);
   }
}
