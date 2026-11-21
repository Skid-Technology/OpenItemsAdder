package dev.lone.itemsadder.util;

import dev.lone.itemsadder.config.Settings;
import java.util.LinkedHashMap;
import org.bukkit.ChatColor;

public class CounterRegistry {
   LinkedHashMap counters = new LinkedHashMap();
   PersistentCounter current;

   public void startCounter(String var1, boolean var2) {
      if (var2 && this.counters.size() > 0) {
         this.saveCurrent();
      }

      this.current = new PersistentCounter();
      this.counters.put(var1, this.current);
   }

   public void saveCurrent() {
      this.current.save();
   }

   public void startNewCounter(String var1) {
      this.startCounter(var1, true);
   }

   public void saveCounter(String var1) {
      PersistentCounter var2 = (PersistentCounter)this.counters.get(var1);
      var2.save();
   }

   public PersistentCounter getCounter(String var1) {
      return (PersistentCounter)this.counters.get(var1);
   }

   public void logBenchmarks() {
      if (Settings.get().logPerformanceBenchmark) {
         new LinkedHashMap<>(this.counters).forEach((var0, var1) -> {
            long var2 = var1.getSaved();
            String var4 = "";
            if (var2 > 100L) {
               var4 = ChatColor.GOLD.toString();
            }

            if (var2 > 150L) {
               var4 = ChatColor.YELLOW.toString();
            }

            if (var2 > 200L) {
               var4 = ChatColor.DARK_RED.toString();
            }

            if (var2 > 250L) {
               var4 = ChatColor.RED.toString();
            }

            Log.log(var0 + " took " + var4 + var2 + "ms");
         });
      }
   }

   public void clear() {
      this.counters.clear();
   }
}
