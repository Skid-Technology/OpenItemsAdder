package dev.lone.itemsadder.anim;

import dev.lone.itemsadder.entity.MatrixTable;

public final class TimedSequenceCursor {
   static final int Je = -2;
   static final int Jf = -1;
   static final int Jg = 0;
   private MatrixTable Jh;
   private int Ji;
   private long Jj;
   private boolean Jk;
   private int Jl;

   void reset() {
      this.Jh = null;
   }

   int method8999(MatrixTable var1, int var2, long var3, boolean var5, boolean var6) {
      return this.method9000(var1, var2, var3, var5, var6, Integer.MAX_VALUE);
   }

   int method9000(MatrixTable var1, int var2, long var3, boolean var5, boolean var6, int var7) {
      return this.method9001(var1, var2, var3, var5, var6, var7, false);
   }

   int method9001(MatrixTable var1, int var2, long var3, boolean var5, boolean var6, int var7, boolean var8) {
      if (this.Jk != var8) {
         this.reset();
         this.Jk = var8;
      }

      if (!var6 || var1 == null) {
         this.reset();
         return -2;
      }

      if (this.Jk) {
         return this.method9002(var1, var2, var3, var7);
      }

      if (this.Jh != var1 || var3 > this.Jj) {
         this.reset();
         int var10 = var1.method8875(var2);
         if (var10 >= 0 && var1.method8876(var10) == var2 && var1.method8876(var10 + 1) - var2 >= 2 && var1.method8876(var10 + 1) < var7) {
            this.Jh = var1;
            this.Ji = var10;
            this.Jj = var3 + 2L;
            return 0;
         } else {
            return -2;
         }
      } else {
         if (var3 < this.Jj) {
            return -1;
         }

         if (this.Ji == this.Jh.size() - 1) {
            if (!var5) {
               this.Jj = Long.MAX_VALUE;
               return -1;
            }

            this.Ji = 0;
         }

         int var9 = this.Ji + 1;
         if (this.Jh.method8876(var9) >= var7) {
            this.reset();
            return -2;
         } else {
            this.Ji = var9;
            this.Jj = this.Jj + this.method9003(var9);
            return var9;
         }
      }
   }

   private int method9002(MatrixTable var1, int var2, long var3, int var5) {
      if (this.Jh == var1 && var3 <= this.Jj) {
         if (var3 < this.Jj) {
            return -1;
         }

         if (this.Ji == this.Jh.size() - 1) {
            this.Jj = Long.MAX_VALUE;
            return -1;
         }

         if (this.Jh.method8876(this.Ji + 1) >= var5) {
            this.reset();
            return -2;
         }

         this.Ji++;
      } else {
         this.reset();
         int var6 = var1.method8875(var2);
         if (var6 < 0 || var1.method8876(var6 + 1) - var2 < 2 || var1.method8876(var6 + 1) >= var5) {
            return -2;
         }

         this.Jh = var1;
         this.Ji = var6 + 1;
      }

      this.Jl = this.Jh.method8876(this.Ji) - var2;
      if (this.Jl < 1) {
         this.reset();
         return -2;
      } else {
         this.Jj = var3 + this.Jl;
         return this.Ji;
      }
   }

   int method9003(int var1) {
      return this.Jk ? this.Jl : this.Jh.method8876(var1) - this.Jh.method8876(var1 - 1);
   }
}
