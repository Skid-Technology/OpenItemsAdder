package dev.lone.itemsadder.molang;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class MolangExpression {
   private final MolangEvalNode axe;
   private final Set axf;
   private final double axg;

   private MolangExpression(MolangEvalNode var1, Set var2) {
      this.axe = var1;
      this.axf = Collections.unmodifiableSet(new HashSet(var2));
      this.axg = 0.0;
   }

   private MolangExpression(double var1) {
      this.axe = null;
      this.axf = Set.of();
      this.axg = var1;
   }

   public Set Ih() {
      return this.axf;
   }

   public double method2089(MolangContext var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Molang context is required");
      } else if (this.axe == null) {
         return this.axg;
      } else {
         MolangEvalState var2 = new MolangEvalState();
         double var3 = this.axe.eval(var1, var2);
         if (var2.axm == 2 || var2.axm == 3) {
            throw new IllegalStateException("break/continue outside loop");
         } else if (!Double.isFinite(var3)) {
            throw new ArithmeticException("Molang expression produced a non-finite value");
         } else {
            return var3;
         }
      }
   }
}
