package dev.lone.itemsadder.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.lone.itemsadder.config.PackVersionInfo;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PluginResourceIndex {
   private static final List STAGE_ORDER = Arrays.asList("alpha", "beta", "rc", "release", "hotfix");
   private final JavaPlugin plugin;
   private final int resourceId;
   private final String currentVersion;

   public PluginResourceIndex(JavaPlugin var1, int var2, @NotNull String var3) {
      this.plugin = var1;
      this.resourceId = var2;
      this.currentVersion = var3;
   }

   public void checkForUpdates(Consumer var1, Consumer var2) {
      Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
         try (
            InputStream var3 = new URL("https://api.polymart.org/v1/getResourceUpdates?resource_id=" + this.resourceId).openStream();
            InputStreamReader var4 = new InputStreamReader(var3);
         ) {
            JsonObject var5 = JsonParser.parseReader(var4).getAsJsonObject();
            JsonObject var6 = var5.getAsJsonObject("response");
            if (!var6.get("success").getAsBoolean()) {
               this.plugin.getLogger().warning("Update check failed: API returned error.");
            } else {
               JsonArray var7 = var6.getAsJsonArray("updates");
               if (var7 != null && !var7.isEmpty()) {
                  JsonObject var8 = var7.get(0).getAsJsonObject();
                  int var9 = var8.get("id").getAsInt();
                  String var10 = var8.get("version").getAsString();
                  String var11 = var8.get("title").getAsString();
                  long var12 = var8.get("time").getAsLong();
                  int var14 = 0;

                  for (JsonElement var17 : var7) {
                     JsonObject var18 = var17.getAsJsonObject();
                     String var19 = var18.get("version").getAsString();
                     if (var19.equals(this.currentVersion)) {
                        int var20 = var18.get("id").getAsInt();
                        if (var20 != var9) {
                           PackVersionInfo var15 = new PackVersionInfo(var10, var11, var12, var9, var14);
                           var1.accept(var15);
                        } else {
                           PackVersionInfo var26 = new PackVersionInfo(var10, var11, var12, var9, 0);
                           var2.accept(var26);
                        }

                        return;
                     }

                     var14++;
                  }

                  PackVersionInfo var27 = new PackVersionInfo(var10, var11, var12, var9, var14);
                  if (this.compareVersions(this.currentVersion, var10) < 0) {
                     var1.accept(var27);
                  } else {
                     var2.accept(var27);
                  }
               } else {
                  this.plugin.getLogger().warning("Update check failed: No updates found.");
               }
            }
         } catch (Exception var25) {
            this.plugin.getLogger().warning("Update check error: " + var25.getMessage());
         }
      });
   }

   private int compareVersions(String var1, String var2) {
      ResourceEntry var3 = this.parseEntry(var1);
      ResourceEntry var4 = this.parseEntry(var2);
      int var5 = this.compareNumericVersion(var3.version, var4.version);
      if (var5 != 0) {
         return var5;
      }

      int var6 = Integer.compare(STAGE_ORDER.indexOf(var3.stage), STAGE_ORDER.indexOf(var4.stage));
      return var6 != 0 ? var6 : Integer.compare(var3.build, var4.build);
   }

   private ResourceEntry parseEntry(String var1) {
      String[] var2 = var1.split("-", 3);
      String var3 = var2[0];
      String var4 = "release";
      int var5 = 0;
      if (var2.length >= 2) {
         var4 = this.normalizeStage(var2[1]);
      }

      if (var2.length == 3) {
         var5 = this.parseNumber(var2[2]);
      }

      return new ResourceEntry(var3, var4, var5);
   }

   private int compareNumericVersion(String var1, String var2) {
      String[] var3 = var1.split("\\.");
      String[] var4 = var2.split("\\.");
      int var5 = Math.max(var3.length, var4.length);

      for (int var6 = 0; var6 < var5; var6++) {
         int var7 = var6 < var3.length ? this.parseNumber(var3[var6]) : 0;
         int var8 = var6 < var4.length ? this.parseNumber(var4[var6]) : 0;
         if (var7 != var8) {
            return Integer.compare(var7, var8);
         }
      }

      return 0;
   }

   private int parseNumber(String var1) {
      try {
         return Integer.parseInt(var1.replaceAll("[^0-9]", ""));
      } catch (Exception var3) {
         return 0;
      }
   }

   private String normalizeStage(String var1) {
      var1 = var1.toLowerCase();
      if (var1.contains("alpha")) {
         return "alpha";
      } else if (var1.contains("beta")) {
         return "beta";
      } else if (var1.contains("rc")) {
         return "rc";
      } else if (var1.contains("release")) {
         return "release";
      } else {
         return var1.contains("hotfix") ? "hotfix" : "release";
      }
   }
}
