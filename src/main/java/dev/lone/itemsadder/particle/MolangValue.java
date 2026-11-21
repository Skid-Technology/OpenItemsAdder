package dev.lone.itemsadder.particle;

import dev.lone.itemsadder.molang.MolangContext;
import dev.lone.itemsadder.molang.MolangExpression;

class MolangValue {
   private final MolangExpression aAW;
   private final Double aAX;

   private MolangValue(MolangExpression var1, Double var2) {
      this.aAW = var1;
      this.aAX = var2;
   }

   double method5(MolangContext var1) {
      return this.aAX != null ? this.aAX : this.aAW.method2089(var1);
   }

   public MolangExpression JL() {
      return this.aAW;
   }

   public Double JM() {
      return this.aAX;
   }
}
