package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.metrics.LongAdderStats;
import dev.lone.itemsadder.util.LongPairDup2;
import dev.lone.itemsadder.util.TimeWindow;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

final class CustomEntityWorker implements AutoCloseable {
   private static final long qa = 50000000L;
   private final ScheduledExecutorService qb;
   private volatile Thread qc;
   private volatile boolean mT;
   private volatile boolean qd;
   private final Object qe;
   private final CompletableFuture qf;
   private final TimeWindow qg;

   private CustomEntityWorker(CustomEntityManager var1) {
      this.qh = var1;
      this.qb = Executors.newSingleThreadScheduledExecutor(var0 -> {
         Thread var1x = new Thread(var0, "ItemsAdder-CustomEntities-Render");
         var1x.setDaemon(true);
         return var1x;
      });
      this.qd = true;
      this.qe = new Object();
      this.qf = new CompletableFuture();
      this.qg = new TimeWindow(50000000L);
   }

   void start() {
      this.qd = true;
      this.mT = true;
      this.qg.addSample(System.nanoTime());
      this.method1690(0L);
   }

   void submit(Runnable var1) {
      if (this.gx()) {
         if (this.qd) {
            var1.run();
         }
      } else {
         synchronized (this.qe) {
            if (this.qd) {
               this.qh.pj.enqueue(var1);
               LongAdderStats var3 = CustomEntityManager.field100;
               if (var3 != null) {
                  var3.pv.accumulate(this.qh.pj.size());
               }
            }
         }
      }
   }

   boolean gx() {
      return Thread.currentThread() == this.qc;
   }

   private void method1690(long var1) {
      if (this.mT) {
         this.qb.schedule(this::gy, Math.max(0L, var1), TimeUnit.NANOSECONDS);
      }
   }

   private void gy() {
      this.qc = Thread.currentThread();
      if (this.mT) {
         LongPairDup2 var1 = this.qg.snapshot(System.nanoTime());
         this.qh.tickEntities(var1.gr(), var1.gs());
         this.method1690(this.qg.getStart() - System.nanoTime());
      }
   }

   CompletionStage gz() {
      boolean var1 = this.gx();
      synchronized (this.qe) {
         if (!this.qd) {
            return this.qf;
         }

         this.qd = false;
         this.mT = false;
         if (!var1) {
            this.qb.execute(this::gA);
         }
      }

      if (var1) {
         this.gA();
      }

      return this.qf;
   }

   @Override
   public void close() {
      this.gz();
   }

   private void gA() {
      this.qc = Thread.currentThread();

      try {
         this.qh.pj.runAll();
         this.qf.complete(null);
      } catch (Throwable var5) {
         this.qf.completeExceptionally(var5);
      } finally {
         this.qb.shutdown();
      }
   }
}
