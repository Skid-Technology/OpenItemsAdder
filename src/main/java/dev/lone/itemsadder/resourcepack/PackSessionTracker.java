package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.util.Log;
import java.util.WeakHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PackSessionTracker {
   public long lastWindowStart;
   public int requestCount;
   boolean limited;

   public static PackSessionTracker forPlayer(WeakHashMap var0, Player var1) {
      return var0.computeIfAbsent(var1.getUniqueId(), var0x -> new PackSessionTracker());
   }

   public static void registerQuitListener(WeakHashMap var0) {
      Bukkit.getPluginManager().registerEvents(new PackSessionQuitListener(var0), Main.getInstance());
   }

   public boolean recordRequest(@Nullable Runnable var1) {
      Log.devLog("limited: " + this.limited);
      Log.devLog("Count: " + this.requestCount);
      Log.devLog("time: " + (System.currentTimeMillis() - this.lastWindowStart));
      if (this.limited) {
         if (System.currentTimeMillis() - this.lastWindowStart > 20000L) {
            this.lastWindowStart = System.currentTimeMillis();
            this.requestCount = 0;
            this.limited = false;
         }

         return true;
      } else {
         if (System.currentTimeMillis() - this.lastWindowStart > 5000L) {
            this.lastWindowStart = System.currentTimeMillis();
            this.requestCount = 0;
            this.limited = false;
         }

         this.requestCount++;
         boolean var2 = this.requestCount >= 20;
         if (var2 && !this.limited) {
            this.limited = true;
            if (var1 != null) {
               var1.run();
            }
         }

         return var2;
      }
   }
}
