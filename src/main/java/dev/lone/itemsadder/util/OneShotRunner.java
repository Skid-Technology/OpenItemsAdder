package dev.lone.itemsadder.util;

import java.util.Objects;
import java.util.function.Function;

public final class OneShotRunner {
   private boolean cancelled;
   private boolean scheduled;
   private boolean running;
   private Runnable task;
   private Thread runnerThread;

   synchronized boolean tryStart(Runnable var1, Function var2) {
      if (!this.cancelled && !this.scheduled) {
         this.scheduled = true;

         try {
            this.task = Objects.requireNonNull((Runnable)var2.apply(() -> {
               synchronized (this) {
                  if (this.cancelled || !this.scheduled) {
                     return;
                  }

                  this.running = true;
                  this.runnerThread = Thread.currentThread();
               }

               try {
                  var1.run();
               } finally {
                  this.reset();
               }
            }));
            return true;
         } catch (RuntimeException | Error var4) {
            this.reset();
            throw var4;
         }
      } else {
         return false;
      }
   }

   boolean runIfNotCancelled(Runnable var1) {
      synchronized (this) {
         if (this.cancelled) {
            return false;
         }
      }

      var1.run();
      return true;
   }

   synchronized boolean isCancelled() {
      return this.cancelled;
   }

   boolean cancel() {
      Runnable var1;
      boolean var2;
      synchronized (this) {
         this.cancelled = true;
         var1 = this.task;
         if (this.runnerThread != null) {
            this.runnerThread.interrupt();
         }

         if (this.scheduled && !this.running) {
            this.scheduled = false;
            this.task = null;
         }

         var2 = !this.running;
      }

      if (var1 != null) {
         var1.run();
      }

      return var2;
   }

   private synchronized void reset() {
      this.scheduled = false;
      this.running = false;
      this.task = null;
      this.runnerThread = null;
      this.notifyAll();
   }
}
