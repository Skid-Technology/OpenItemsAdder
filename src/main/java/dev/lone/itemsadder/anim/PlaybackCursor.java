package dev.lone.itemsadder.anim;

import dev.lone.itemsadder.api.AnimationPlaybackOptions;

public final class PlaybackCursor {
   private static final double QD = 1.0E-9;
   final double QE;
   final double QF;
   final double QG;
   final double QH;
   final boolean QI;
   final double QJ;
   final boolean QK;
   double zW;
   private double QL;
   private double QM;
   private int QN;
   private boolean QO = true;
   private boolean oi;
   private boolean QP;
   private boolean field129;
   private boolean QR;
   boolean QS;
   static final PlaybackVisitor QT = (var0, var2, var4, var5) -> {};

   int ul() {
      return this.QN;
   }

   PlaybackCursor(double var1, double var3, double var5, AnimationPlaybackOptions var7) {
      this.QE = var7.start();
      this.QF = var7.end() < 0.0 ? var1 : var7.end();
      if (!(this.QE > var1) && !(this.QF > var1) && !(this.QF < this.QE)) {
         boolean var8 = var7.start() == 0.0 && var7.end() == -1.0 && var5 > var3;
         this.QG = var8 ? var3 : this.QE;
         this.QH = var8 ? var5 : this.QF;
         this.QK = var8;
         this.QI = var7.reversed();
         this.QR = this.QI;
         this.QJ = var7.speed() / 20.0;
         double var9 = this.QH - this.QG;
         if (var9 > 1.0E-9 && this.QJ / var9 > 1024.0) {
            throw new IllegalArgumentException("Playback speed crosses more than 1024 cycles per tick");
         }

         this.zW = this.QI ? this.QF : this.QE;
      } else {
         throw new IllegalArgumentException("Playback range exceeds the clip duration");
      }
   }

   private PlaybackCursor(PlaybackCursor var1) {
      this.QE = var1.QE;
      this.QF = var1.QF;
      this.QG = var1.QG;
      this.QH = var1.QH;
      this.QI = var1.QI;
      this.QJ = var1.QJ;
      this.QK = var1.QK;
      this.zW = var1.zW;
      this.field129 = var1.field129;
      this.QR = var1.QR;
   }

   boolean um() {
      return !this.QR;
   }

   double method9133(int var1, boolean var2) {
      if (var1 >= 0 && var1 <= 8) {
         PlaybackCursor var3 = new PlaybackCursor(this);

         for (int var4 = 0; var4 < var1; var4++) {
            var3.method9136(var2, this.QR);
         }

         return var3.zW;
      } else {
         throw new IllegalArgumentException("Prediction requires 0 to 8 server ticks");
      }
   }

   void method9134(PlaybackVisitor var1) {
      if (this.QO) {
         var1.visit(this.zW, this.zW, true, true);
         this.QO = false;
         this.QS = this.QS | this.QF - this.QE <= 1.0E-9;
      }

      if (this.oi) {
         this.oi = false;
         this.QS = this.QS | (this.QN != 0 || this.field129);
         if (this.QN == 0) {
            var1.visit(this.QL, this.QM, this.QP, true);
            this.QP = false;
         } else {
            if (!this.QR) {
               var1.visit(this.QL, this.QH, false, !this.QK);

               for (int var2 = 1; var2 < this.QN; var2++) {
                  var1.visit(this.QG, this.QH, true, !this.QK);
               }

               var1.visit(this.QG, this.QM, true, true);
            } else {
               var1.visit(this.QL, this.QG, false, true);

               for (int var3 = 1; var3 < this.QN; var3++) {
                  var1.visit(this.QH, this.QG, !this.QK, true);
               }

               var1.visit(this.QH, this.QM, !this.QK, true);
            }
         }
      }
   }

   void method9135(PlaybackVisitor var1) {
      if (this.oi && this.QN != 0) {
         var1.visit(this.QL, this.QR ? this.QG : this.QH, false, this.QR || !this.QK);
         this.QS = true;
         this.QL = this.QR ? this.QH : this.QG;
         this.QM = this.zW;
         this.QN = 0;
         this.QP = !this.QR || !this.QK;
      }
   }

   boolean aq(boolean var1) {
      return this.method9136(var1, this.QI);
   }

   boolean method9136(boolean var1, boolean var2) {
      if (this.field129) {
         return false;
      }

      this.QR = var2;
      this.QL = this.zW;
      this.QN = 0;
      double var3 = this.zW + (var2 ? -this.QJ : this.QJ);
      double var5 = this.QH - this.QG;
      if (var1 && var5 > 1.0E-9 && (!var2 ? var3 >= this.QH - 1.0E-9 : var3 <= this.QG + 1.0E-9)) {
         double var7 = var2 ? this.QG - var3 : var3 - this.QH;
         this.QN = 1 + (int)Math.floor(Math.max(0.0, var7 + 1.0E-9) / var5);
         double var9 = Math.max(0.0, var7) - (this.QN - 1) * var5;
         if (Math.abs(var9) < 1.0E-9) {
            var9 = 0.0;
         }

         this.zW = var2 ? this.QH - var9 : this.QG + var9;
      } else {
         this.zW = Math.max(this.QE, Math.min(this.QF, var3));
         this.field129 = !var2 ? this.zW >= this.QF - 1.0E-9 : this.zW <= this.QE + 1.0E-9;
      }

      this.QM = this.zW;
      this.oi = this.QN != 0 || Math.abs(this.QM - this.QL) > 1.0E-9;
      return true;
   }

   static boolean method9137(double var0, double var2, double var4, boolean var6, boolean var7) {
      return var2 <= var4
         ? (var6 ? var0 >= var2 - 1.0E-9 : var0 > var2 + 1.0E-9) && (var7 ? var0 <= var4 + 1.0E-9 : var0 < var4 - 1.0E-9)
         : (var6 ? var0 <= var2 + 1.0E-9 : var0 < var2 - 1.0E-9) && (var7 ? var0 >= var4 - 1.0E-9 : var0 > var4 + 1.0E-9);
   }
}
