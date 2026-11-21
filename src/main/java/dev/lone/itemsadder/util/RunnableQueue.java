package dev.lone.itemsadder.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class RunnableQueue {
   private final Queue queue = new ConcurrentLinkedQueue();

   void enqueue(Runnable var1) {
      this.queue.add(var1);
   }

   boolean isEmpty() {
      Runnable var1 = (Runnable)this.queue.poll();
      if (var1 == null) {
         return false;
      }

      var1.run();
      return true;
   }

   void runAll() {
      while (this.isEmpty()) {
      }
   }

   int size() {
      return this.queue.size();
   }
}
