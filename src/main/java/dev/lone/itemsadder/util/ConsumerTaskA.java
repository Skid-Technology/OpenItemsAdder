package dev.lone.itemsadder.util;

import java.util.function.Consumer;
import org.bukkit.scheduler.BukkitRunnable;

class ConsumerTaskA extends BukkitRunnable {
   ConsumerTaskA(Consumer var1) {
      this.consumer = var1;
   }

   public void run() {
      this.consumer.accept(this);
   }
}
