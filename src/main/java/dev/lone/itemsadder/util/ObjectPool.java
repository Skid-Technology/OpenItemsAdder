package dev.lone.itemsadder.util;

import dev.lone.itemsadder.Main;
import java.util.LinkedList;
import java.util.Queue;
import org.bukkit.Bukkit;

public class ObjectPool {
   volatile int pendingUses;
   volatile int averageUses;
   Queue window = new LinkedList();

   public ObjectPool() {
      Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
         this.window.add(this.pendingUses);
         if (this.window.size() > 4) {
            int var1 = 0;

            for (Integer var3 : this.window) {
               var1 += var3;
            }

            this.averageUses = var1 / this.window.size();
            this.window.remove();
         }

         this.pendingUses = 0;
      }, 20L, 20L);
   }

   public void recordUse() {
      this.pendingUses++;
   }
}
