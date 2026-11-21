package dev.lone.itemsadder.particle;

import java.util.List;

final class ParticleCollision {
   private static final double avc = 1.0E-7;

   private ParticleCollision() {
   }

   static CollisionResult method2064(double var0, double var2, double var4, double var6, double var8, double var10, double var12, List var14) {
      if (Double.isFinite(var0)
         && Double.isFinite(var2)
         && Double.isFinite(var4)
         && Double.isFinite(var6)
         && Double.isFinite(var8)
         && Double.isFinite(var10)
         && Double.isFinite(var12)
         && !(var12 <= 0.0)
         && var14 != null) {
         for (ParticleBounds var16 : var14) {
            if (var16 == null) {
               throw new IllegalArgumentException("Particle collision obstacle cannot be null");
            }
         }

         double var25 = method2065(1, var8, var0, var2, var4, var12, var14);
         var2 += var25;
         double var17;
         double var19;
         if (Math.abs(var6) < Math.abs(var10)) {
            var19 = method2065(2, var10, var0, var2, var4, var12, var14);
            var4 += var19;
            var17 = method2065(0, var6, var0, var2, var4, var12, var14);
         } else {
            var17 = method2065(0, var6, var0, var2, var4, var12, var14);
            var0 += var17;
            var19 = method2065(2, var10, var0, var2, var4, var12, var14);
         }

         byte var21 = 0;
         if (var17 != var6) {
            var21 |= 1;
         }

         if (var25 != var8) {
            var21 |= 2;
         }

         if (var19 != var10) {
            var21 |= 4;
         }

         return new CollisionResult(var17, var25, var19, var21);
      } else {
         throw new IllegalArgumentException("Particle collision inputs must be finite and radius positive");
      }
   }

   private static double method2065(int var0, double var1, double var3, double var5, double var7, double var9, List var11) {
      double var12 = var1;
      if (var12 == 0.0) {
         return 0.0;
      }

      for (ParticleBounds var15 : var11) {
         int var16 = method2067(var0, 0);
         int var17 = method2067(var0, 1);
         if (method2066(method2068(var3, var5, var7, var16), var9, method2069(var15, var16), method2070(var15, var16))
            && method2066(method2068(var3, var5, var7, var17), var9, method2069(var15, var17), method2070(var15, var17))) {
            double var18 = method2069(var15, var0);
            double var20 = method2070(var15, var0);
            double var22 = method2068(var3, var5, var7, var0);
            double var24 = var22 - var9;
            double var26 = var22 + var9;
            if (var12 > 0.0 && var26 <= var18 + 1.0E-7) {
               double var30 = var18 - var26;
               var12 = Math.max(0.0, Math.min(var12, var30));
            } else if (var12 < 0.0 && var24 >= var20 - 1.0E-7) {
               double var28 = var20 - var24;
               var12 = Math.min(0.0, Math.max(var12, var28));
            }
         }
      }

      return var12;
   }

   private static boolean method2066(double var0, double var2, double var4, double var6) {
      return var0 + var2 > var4 + 1.0E-7 && var0 - var2 < var6 - 1.0E-7;
   }

   private static int method2067(int var0, int var1) {
      if (var0 == 0) {
         return var1 == 0 ? 1 : 2;
      } else if (var0 == 1) {
         return var1 == 0 ? 0 : 2;
      } else {
         return var1 == 0 ? 0 : 1;
      }
   }

   private static double method2068(double var0, double var2, double var4, int var6) {
      return switch (var6) {
         case 0 -> var0;
         case 1 -> var2;
         default -> var4;
      };
   }

   private static double method2069(ParticleBounds var0, int var1) {
      return switch (var1) {
         case 0 -> var0.HL();
         case 1 -> var0.HM();
         default -> var0.HN();
      };
   }

   private static double method2070(ParticleBounds var0, int var1) {
      return switch (var1) {
         case 0 -> var0.HO();
         case 1 -> var0.HP();
         default -> var0.HQ();
      };
   }
}
