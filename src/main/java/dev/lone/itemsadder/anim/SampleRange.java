package dev.lone.itemsadder.anim;

import dev.lone.itemsadder.util.DoubleVector4;
import java.util.List;

class SampleRange {
   private final int Qd;
   private final int Qe;
   private final int Qf;
   private final int Qg;
   private final List Qh;

   private SampleRange(int var1, int var2, int var3, int var4, List var5) {
      this.Qd = var1;
      this.Qe = var2;
      this.Qf = var3;
      this.Qg = var4;
      this.Qh = var5;
   }

   boolean method71(DoubleVector4 var1) {
      return this.Qd == (int)Math.floor(var1.x)
         && this.Qe == (int)Math.floor(var1.z)
         && this.Qf <= (int)Math.floor(var1.y - var1.w)
         && this.Qg >= (int)Math.floor(var1.y);
   }

   public int method1() {
      return this.Qd;
   }

   public int method3() {
      return this.Qe;
   }

   public int uc() {
      return this.Qf;
   }

   public int ud() {
      return this.Qg;
   }

   public List ue() {
      return this.Qh;
   }
}
