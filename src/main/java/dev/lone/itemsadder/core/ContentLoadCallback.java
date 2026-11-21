package dev.lone.itemsadder.core;

import dev.lone.itemsadder.util.BossBarManager;
import java.util.function.Consumer;
import org.bukkit.scheduler.BukkitRunnable;

class ContentLoadCallback implements Consumer {
   final String bF = BossBarManager.getTitle();

   ContentLoadCallback(ContentManager var1) {
   }

   public void method3170(BukkitRunnable var1) {
      if (this.bF.equals(BossBarManager.getTitle())) {
         BossBarManager.clearIfTitle(this.bF);
      }
   }
}
