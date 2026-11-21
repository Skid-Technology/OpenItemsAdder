package dev.lone.itemsadder.particle;

import dev.lone.itemsadder.anim.AnimationCurve;
import dev.lone.itemsadder.molang.MolangContext;

final class EmitterMolangContext implements MolangContext {
   ParticleData aAn;
   double aAo;
   double aAp;
   double aAq;
   double aAr;
   int aAs;
   final double[] aAt;

   EmitterMolangContext(ParticleSimulation var1) {
      this.aAu = var1;
      this.aAt = new double[4];
   }

   void method2091(ParticleData var1) {
      this.aAn = var1;
      this.aAo = this.aAu.azO;
      this.aAr = this.aAu.azQ;
      this.aAp = var1 == null ? 0.0 : var1.azO;
      this.aAq = var1 == null ? 0.0 : var1.aAv;
      System.arraycopy(this.aAu.azM, 0, this.aAt, 0, 4);
   }

   @Override
   public double dH(String var1) {
      return switch (var1) {
         case "variable.emitter_age" -> {
            double var17 = this.aAo;
            yield var17;
         }
         case "variable.emitter_lifetime" -> {
            double var16 = this.aAr;
            yield var16;
         }
         case "variable.particle_age" -> {
            double var15 = this.aAp;
            yield var15;
         }
         case "variable.particle_lifetime" -> {
            double var14 = this.aAq;
            yield var14;
         }
         case "variable.entity_scale" -> {
            double var13 = 1.0;
            yield var13;
         }
         default -> {
            label110: {
               if (var1.startsWith("variable.emitter_random_") && var1.length() == 25) {
                  int var6 = var1.charAt(24) - '1';
                  if (var6 >= 0 && var6 < 4) {
                     double var12 = this.aAt[var6];
                     yield var12;
                     break label110;
                  }
               }

               if (var1.startsWith("variable.particle_random_") && var1.length() == 26) {
                  int var18 = var1.charAt(25) - '1';
                  if (var18 >= 0 && var18 < 4) {
                     double var11 = this.aAn == null ? 0.0 : this.aAn.aAC[var18];
                     yield var11;
                     break label110;
                  }
               }

               AnimationCurve var19 = (AnimationCurve)this.aAu.azC.awe.get(var1);
               if (var19 != null) {
                  if (++this.aAs > 16) {
                     throw new IllegalStateException("Recursive particle curve");
                  }

                  double var4;
                  try {
                     var4 = var19.method2078(this);
                  } finally {
                     this.aAs--;
                  }

                  yield var4;
               } else {
                  double var10 = this.aAu.azL.getOrDefault(var1, 0.0);
                  yield var10;
               }
            }
         }
      };
   }

   @Override
   public void method2090(String var1, double var2) {
      if (!Double.isFinite(var2)) {
         throw new ArithmeticException("Non-finite particle variable");
      }

      this.aAu.azL.put(var1, var2);
   }

   @Override
   public double Ii() {
      return this.aAu.azE.nextDouble();
   }
}
