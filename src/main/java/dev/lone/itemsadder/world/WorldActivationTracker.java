package dev.lone.itemsadder.world;

import dev.lone.itemsadder.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldActivationTracker {
   public List worldPatterns;
   boolean allWorlds;
   List enabledWorldIds = Collections.synchronizedList(new ArrayList());
   List disabledWorldIds = Collections.synchronizedList(new ArrayList());

   public WorldActivationTracker(List var1) {
      this.worldPatterns = new ArrayList(var1);
      if (this.worldPatterns.contains("*")) {
         this.allWorlds = true;
         this.worldPatterns.removeIf(var0 -> var0.equals("*"));
      } else {
         this.allWorlds = false;
      }

      Bukkit.getWorlds().forEach(var1x -> this.worldPatterns.forEach(var2 -> {
         if (var2.startsWith("!")) {
            if (FilenameUtils.wildcardMatch(var1x.getName(), var2.substring(1))) {
               this.disabledWorldIds.add(var1x.getUID());
            }
         } else if (FilenameUtils.wildcardMatch(var1x.getName(), var2)) {
            this.enabledWorldIds.add(var1x.getUID());
         }
      }));

      for (World var3 : Bukkit.getWorlds()) {
         this.warnIfUnlisted(var3);
      }
   }

   private void warnIfUnlisted(World var1) {
      if (!this.isWorldActive(var1) && var1.getWorldFolder().toPath().resolve("itemsadder").toFile().exists()) {
         Log.warn(" ");
         Log.warn("[!] The world '" + var1.getName() + "' contains ItemsAdder data, but it's not in the worlds list in config.yml of ItemsAdder.");
         Log.warn("[!] The data might get lost because custom blocks events won't get triggered anymore.");
         Log.warn("[!] Make sure to know what you're doing or delete the 'itemsadder' folder from the world folder if you don't need it anymore.");
         Log.warn(" ");
      }
   }

   public boolean isWorldActive(World var1) {
      if (this.allWorlds && this.disabledWorldIds.isEmpty()) {
         return true;
      }

      for (UUID var3 : this.disabledWorldIds) {
         if (var3 != null && var3.equals(var1.getUID())) {
            return false;
         }
      }

      for (UUID var5 : this.enabledWorldIds) {
         if (var5 != null && var5.equals(var1.getUID())) {
            return true;
         }
      }

      return this.allWorlds;
   }

   public void updateWorldState(World var1) {
      if (!this.isWorldActive(var1)) {
         this.enabledWorldIds.remove(var1.getUID());
         this.disabledWorldIds.add(var1.getUID());
      } else {
         this.enabledWorldIds.add(var1.getUID());
         this.disabledWorldIds.remove(var1.getUID());
         this.warnIfUnlisted(var1);
      }
   }

   public void forgetWorld(World var1) {
      this.enabledWorldIds.remove(var1.getUID());
      this.disabledWorldIds.remove(var1.getUID());
   }
}
