package dev.lone.itemsadder.metrics;

import dev.lone.itemsadder.entity.CustomEntityManager;
import java.util.Arrays;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

public final class LongAdderStats {
   final LongAdder pn = new LongAdder();
   final LongAdder po = new LongAdder();
   final LongAdder field104 = new LongAdder();
   final LongAdder pq = new LongAdder();
   final LongAdder pr = new LongAdder();
   final LongAdder ps = new LongAdder();
   final LongAdder pt = new LongAdder();
   final LongAccumulator pu = new LongAccumulator(Long::max, 0L);
   final LongAccumulator pv = new LongAccumulator(Long::max, 0L);
   final long[] pw = new long[2048];
   final long[] px = new long[2048];
   int py;
   int pz;
   long pA;

   private LongAdderStats() {
   }

   void method8367(long var1) {
      if (this.pA != 0L && this.pz < this.px.length) {
         this.px[this.pz++] = var1 - this.pA;
      }

      this.pA = var1;
   }

   void method8368(long var1, long var3, long var5) {
      this.pn.increment();
      this.po.add(var1);
      this.pu.accumulate(var1);
      if (var3 >= 0L) {
         this.field104.add(var3);
      }

      if (var5 >= 0L) {
         this.pq.add(var5);
      }

      if (this.py < this.pw.length) {
         this.pw[this.py++] = var1;
      }
   }

   LongStatsSnapshot gd() {
      long[] var1 = Arrays.copyOf(this.pw, this.py);
      Arrays.sort(var1);
      return new LongStatsSnapshot(
         this.pn.sum(),
         this.po.sum(),
         this.pu.get(),
         CustomEntityManager.percentileOf(var1, 0.5),
         CustomEntityManager.percentileOf(var1, 0.95),
         CustomEntityManager.percentileOf(var1, 0.99),
         this.field104.sum(),
         this.pq.sum(),
         this.pr.sum(),
         this.ps.sum(),
         this.pt.sum()
      );
   }
}
