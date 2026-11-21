package dev.lone.itemsadder.block;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Chunk;
import org.bukkit.scheduler.BukkitRunnable;

class BlockInteractionTask extends BukkitRunnable {
   BlockInteractionTask(BlockInteractionListener var1, AtomicInteger var2, List var3) {
      this.nm = var2;
      this.field51 = var3;
      this.no = var1;
   }

   public void run() {
      if (this.no.isActive() && this.nm.get() < this.field51.size()) {
         Chunk var1 = (Chunk)this.field51.get(this.nm.getAndIncrement());
         this.no.mZ.preloadChunk(var1);
      } else {
         this.cancel();
      }
   }
}
