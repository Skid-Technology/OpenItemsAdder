package dev.lone.itemsadder.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class VersionComparator {
   public static InstalledVersionState compareInstalledVersion(String var0, String var1) {
      Plugin var2 = Bukkit.getPluginManager().getPlugin(var0);
      return var2 == null ? InstalledVersionState.UNKNOWN : compareInstalledVersionStrict(var1, var2.getDescription().getVersion().split("-")[0].split("_")[0]);
   }

   public static InstalledVersionState compareInstalledVersionStrict(String var0, String var1) {
      int var2 = compareVersions(var0, var1);

      return switch (var2) {
         case -1 -> InstalledVersionState.INSTALLED_IS_NEWER;
         case 0 -> InstalledVersionState.INSTALLED_IS_SAME;
         case 1 -> InstalledVersionState.INSTALLED_IS_LOWER;
         default -> InstalledVersionState.UNKNOWN;
      };
   }

   public static int compareVersions(String var0, String var1) {
      int var2 = 0;
      int var3 = 0;
      int var4 = 0;

      for (int var5 = 0; var4 < var0.length() || var5 < var1.length(); var5++) {
         while (var4 < var0.length() && var0.charAt(var4) != '.') {
            var2 = var2 * 10 + (var0.charAt(var4) - '0');
            var4++;
         }

         while (var5 < var1.length() && var1.charAt(var5) != '.') {
            var3 = var3 * 10 + (var1.charAt(var5) - '0');
            var5++;
         }

         if (var2 > var3) {
            return 1;
         }

         if (var3 > var2) {
            return -1;
         }

         var3 = 0;
         var2 = 0;
         var4++;
      }

      return 0;
   }
}
