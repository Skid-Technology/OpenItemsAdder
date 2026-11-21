package dev.lone.itemsadder.core;

public abstract class ComparisonCondition implements ConditionAction {
   public final int acn;
   public final float aco;
   protected int acp = 0;
   final ComparisonOperator acq;

   public ComparisonCondition(int var1, float var2, ComparisonOperator var3) {
      this.acn = var1;
      this.aco = var2;
      this.acq = var3;
   }
}
