package dev.lone.itemsadder.item;

public class TimedStep {
   private final int aqN;
   private final int aqO;
   private final float aqP;

   public TimedStep(int var1) {
      this(var1, var1, 100.0F);
   }

   public TimedStep(int var1, int var2, float var3) {
      this.aqN = var1;
      this.aqO = var2;
      this.aqP = var3;
   }

   public int Fn() {
      return this.aqN;
   }

   public int Fo() {
      return this.aqO;
   }

   public float gF() {
      return this.aqP;
   }
}
