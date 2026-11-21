package dev.lone.itemsadder.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;

public final class NamedThreadFactory implements ThreadFactory {
   private final String namePrefix;
   private final AtomicInteger threadCount = new AtomicInteger();

   NamedThreadFactory(String var1) {
      this.namePrefix = var1;
   }

   @Override
   public Thread newThread(@NotNull Runnable r) {
      Thread var2 = new Thread(r, this.namePrefix + "-" + this.threadCount.incrementAndGet());
      var2.setDaemon(true);
      return var2;
   }
}
