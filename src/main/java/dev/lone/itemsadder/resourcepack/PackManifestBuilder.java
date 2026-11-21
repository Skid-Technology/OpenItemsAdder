package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.util.ImageOptimizeStatus;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.ServerCompat;
import dev.lone.itemsadder.util.Utils;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

final class PackManifestBuilder {
   private final Map entries = new LinkedHashMap();

   void addEntry(String var1, ImageOptimizeStatus var2, String var3) {
      this.entries.putIfAbsent(Utils.normalizeSlashes(var1), new PackManifestEntry(var2, var3));
   }

   void addWriteError(String var1, @Nullable String var2) {
      String var3 = Utils.normalizeSlashes(var1);
      if (var3.toLowerCase(Locale.ROOT).endsWith(".png")) {
         this.entries.put(var3, new PackManifestEntry(ImageOptimizeStatus.WRITE_ERROR, "write error"));
      }
   }

   int countWithStatus(ImageOptimizeStatus var1) {
      return (int)this.entries.values().stream().filter(var1x -> var1x.status() == var1).count();
   }

   int size() {
      return this.entries.size();
   }

   void logSummary() {
      if (ServerCompat.isDevMode()) {
         if (!this.entries.isEmpty()) {
            int var1 = this.countWithStatus(ImageOptimizeStatus.APPLIED);
            int var2 = this.countWithStatus(ImageOptimizeStatus.ALREADY_PROCESSED);
            int var3 = this.countWithStatus(ImageOptimizeStatus.UNSUPPORTED_FORMAT) + this.countWithStatus(ImageOptimizeStatus.NOT_NEEDED);
            int var4 = this.countWithStatus(ImageOptimizeStatus.INVALID_PNG)
               + this.countWithStatus(ImageOptimizeStatus.READ_ERROR)
               + this.countWithStatus(ImageOptimizeStatus.WRITE_ERROR);
            String var5 = "PNG obfuscation: " + var1 + " processed, " + var2 + " already protected, " + var3 + " skipped, " + var4 + " failed.";
            if (var4 == 0) {
               Log.log(ChatColor.AQUA + "### [DEV MODE LOG] " + var5);
            } else {
               Log.warn(ChatColor.AQUA + "### [DEV MODE LOG] " + var5);
            }

            this.logStatusGroup("skipped", false, EnumSet.of(ImageOptimizeStatus.UNSUPPORTED_FORMAT, ImageOptimizeStatus.NOT_NEEDED));
            this.logStatusGroup("failed", true, EnumSet.of(ImageOptimizeStatus.INVALID_PNG, ImageOptimizeStatus.READ_ERROR, ImageOptimizeStatus.WRITE_ERROR));
         }
      }
   }

   private void logStatusGroup(String var1, boolean var2, Set var3) {
      List var4 = this.entries.entrySet().stream().filter(var1x -> var3.contains(((PackManifestEntry)var1x.getValue()).status())).toList();
      if (!var4.isEmpty()) {
         Map var5 = var4.stream().collect(Collectors.groupingBy(var0 -> ((PackManifestEntry)var0.getValue()).reason(), TreeMap::new, Collectors.counting()));
         StringBuilder var6 = new StringBuilder("PNG obfuscation ").append(var1).append(' ').append(var4.size()).append(" files:");
         var5.forEach((var1x, var2x) -> var6.append("\n- ").append(var1x).append(": ").append(var2x));
         var6.append("\nFiles:");
         var4.forEach(var1x -> var6.append("\n- ").append((String)var1x.getKey()));
         if (var2) {
            Log.devWarn(var6.toString());
         } else if (ServerCompat.isDevMode()) {
            Log.log(ChatColor.AQUA + "### [DEV MODE LOG] " + ChatColor.RESET + var6);
         }
      }
   }
}
