package dev.lone.itemsadder.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class ScrollableState {
   static final int aKw = 8;
   static final int aKx = 5;
   final int aKy;
   private final int[] aKz;
   private final Map aKA = new HashMap();
   volatile int aKB;
   private int aIw;
   private boolean aKC;

   ScrollableState(int var1) {
      this(var1, 5);
   }

   ScrollableState(int var1, int var2) {
      if (var2 >= 1 && var2 <= 6) {
         this.aKy = var2;
         this.aKz = new int[var2 * 9];
         this.ce(var1);
         Arrays.fill(this.aKz, -1);
      } else {
         throw new IllegalArgumentException("rows must be between 1 and 6");
      }
   }

   synchronized void ce(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("size must not be negative");
      }

      this.aKB = Math.max(0, cf(var1) - this.aKy);
      this.aIw = Math.min(this.aIw, this.aKB);
   }

   synchronized int Me() {
      return this.aIw;
   }

   static int cf(int var0) {
      return var0 == 0 ? 0 : (var0 - 1) / 8 + 1;
   }

   synchronized boolean Mf() {
      return this.aKC;
   }

   synchronized void Mg() {
      this.aKC = !this.aKC;
   }

   synchronized boolean cg(int var1) {
      int var2 = Math.max(0, Math.min(this.aKB, var1));
      if (this.aIw == var2) {
         return false;
      }

      this.aIw = var2;
      return true;
   }

   synchronized void ch(int var1) {
      this.aKz[var1] = -1;
   }

   synchronized void method2588(int var1, long var2) {
      this.aKA.put(var1, var2);
   }

   synchronized boolean Mh() {
      return !this.aKA.isEmpty();
   }

   synchronized boolean ci(int var1) {
      Long var2 = (Long)this.aKA.remove(var1);
      if (var2 == null) {
         return false;
      }

      for (int var3 = 0; var3 < this.aKz.length; var3++) {
         if ((var2 & 1L << var3) != 0L) {
            this.aKz[var3] = -1;
         }
      }

      return true;
   }

   synchronized boolean method2589(int var1, int var2) {
      if (var1 >= 0 && var1 < this.aKz.length && var2 >= -1 && var2 <= 2) {
         int var3 = this.aKz[var1];
         this.aKz[var1] = var2;
         if (var2 != -1 && var3 != var2) {
            int var4 = var3 == -1 ? (var2 == 0 ? 1 : (var2 == 2 ? -1 : 0)) : ((var2 - var3 + 3) % 3 == 1 ? 1 : -1);
            return this.cg(this.aIw + var4 * (this.aKC ? this.aKy : 1));
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   synchronized float cj(int var1) {
      return method2590(var1, this.aIw, this.aKB, this.aKy * 18);
   }

   static int method2590(int var0, int var1, int var2, int var3) {
      return var2 <= 0 ? -100 : Math.round((var3 - 10.0F) * var1 / var2) - var0 * 18;
   }

   static int method2591(int var0, boolean var1) {
      return var0 * (var1 ? 5 : 1);
   }

   static int method2592(int var0, int var1, int var2, int var3) {
      if (var3 > 0 && var0 >= 0 && var0 < var1) {
         int var4 = Math.round((var1 * 18.0F - 10.0F) * var2 / var3);
         if ((var0 + 1) * 18 <= var4) {
            return -1;
         } else {
            return var0 * 18 >= var4 + 10 ? 1 : 0;
         }
      } else {
         return 0;
      }
   }
}
