package dev.lone.itemsadder.entity;

public class MobAttributeEntry {
   private final String wQ;
   private final double wR;
   private final int wS;

   public MobAttributeEntry(String var1, double var2, int var4) {
      this.wQ = var1;
      this.wR = var2;
      this.wS = var4;
   }

   public String name() {
      return this.wQ;
   }

   public double amount() {
      return this.wR;
   }

   public int operation() {
      return this.wS;
   }
}
