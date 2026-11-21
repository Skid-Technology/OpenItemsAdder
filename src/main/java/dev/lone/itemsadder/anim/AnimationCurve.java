package dev.lone.itemsadder.anim;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.molang.MolangContext;
import dev.lone.itemsadder.molang.MolangExpression;
import dev.lone.itemsadder.particle.ParticleEffectDefinition;
import java.util.Arrays;
import java.util.Comparator;

public final class AnimationCurve {
   final MolangExpression awy;
   final MolangExpression awz;
   final String awA;
   final double[] awB;
   final BezierNode[] awC;
   private static final BezierNode awD = new BezierNode(0.0, 0.0, 0.0, 0.0, 0.0);

   AnimationCurve(JSONObject var1) {
      this.awA = var1.getString("type");
      this.awy = ParticleEffectDefinition.method2074(var1.getOrDefault("input", 0));
      this.awz = ParticleEffectDefinition.method2074(var1.getOrDefault("horizontal_range", 1));
      if ("bezier_chain".equals(this.awA)) {
         JSONObject var10 = var1.getJSONObject("nodes");
         if (var10 != null && !var10.isEmpty() && var10.size() <= 256) {
            this.awC = new BezierNode[var10.size()];
            int var11 = 0;

            for (String var5 : var10.keySet()) {
               double var6;
               try {
                  var6 = Double.parseDouble(var5);
               } catch (NumberFormatException var9) {
                  throw ParticleEffectDefinition.bj("curve chain time");
               }

               if (!Double.isFinite(var6)) {
                  throw ParticleEffectDefinition.bj("curve chain time");
               }

               JSONObject var8 = var10.getJSONObject(var5);
               if (var8 == null) {
                  throw ParticleEffectDefinition.bj("curve chain node");
               }

               this.awC[var11++] = new BezierNode(
                  var6,
                  method2079(var8, "left_value", "value"),
                  method2079(var8, "right_value", "value"),
                  method2079(var8, "left_slope", "slope"),
                  method2079(var8, "right_slope", "slope")
               );
            }

            Arrays.sort(this.awC, Comparator.comparingDouble(BezierNode::nr));

            for (int var12 = 1; var12 < this.awC.length; var12++) {
               if (this.awC[var12 - 1].awE == this.awC[var12].awE) {
                  throw ParticleEffectDefinition.bj("duplicate curve chain time");
               }
            }

            this.awB = new double[0];
         } else {
            throw ParticleEffectDefinition.bj("curve chain nodes");
         }
      } else {
         this.awC = null;
         JSONArray var2 = var1.getJSONArray("nodes");
         if (var2 != null && var2.size() >= 2 && var2.size() <= 256) {
            this.awB = new double[var2.size()];

            for (int var3 = 0; var3 < this.awB.length; var3++) {
               this.awB[var3] = var2.getDoubleValue(var3);
               if (!Double.isFinite(this.awB[var3])) {
                  throw ParticleEffectDefinition.bj("non-finite curve node");
               }
            }

            if (!"linear".equals(this.awA) && !"bezier".equals(this.awA) && !"catmull_rom".equals(this.awA)) {
               throw ParticleEffectDefinition.bj("curve mode not implemented: " + this.awA);
            } else if ("bezier".equals(this.awA) && this.awB.length != 4 || "catmull_rom".equals(this.awA) && this.awB.length < 4) {
               throw ParticleEffectDefinition.bj("curve control point count");
            }
         } else {
            throw ParticleEffectDefinition.bj("curve nodes");
         }
      }
   }

   double method2078(MolangContext var1) {
      double var2 = this.awy.method2089(var1);
      double var4 = this.awz.method2089(var1);
      double var6 = var4 == 0.0 ? 0.0 : var2 / var4;
      if (this.awC == null) {
         if ("bezier".equals(this.awA)) {
            double var27 = 1.0 - var6;
            return var27 * var27 * var27 * this.awB[0]
               + 3.0 * var27 * var27 * var6 * this.awB[1]
               + 3.0 * var27 * var6 * var6 * this.awB[2]
               + var6 * var6 * var6 * this.awB[3];
         }

         double var26 = var6 * ("linear".equals(this.awA) ? this.awB.length - 1 : this.awB.length - 3);
         int var28 = (int)Math.floor(var26);
         double var29 = var26 - var28;
         if ("linear".equals(this.awA)) {
            if (var28 >= 0 && var28 + 1 < this.awB.length) {
               return this.awB[var28] + (this.awB[var28 + 1] - this.awB[var28]) * var29;
            } else {
               throw ParticleEffectDefinition.bj("linear curve input outside reference domain");
            }
         } else if (var28 >= 0 && var28 + 3 < this.awB.length) {
            double var30 = this.awB[var28];
            double var31 = this.awB[var28 + 1];
            double var17 = this.awB[var28 + 2];
            double var19 = this.awB[var28 + 3];
            double var21 = (var17 - var30) * 0.5;
            double var23 = (var19 - var31) * 0.5;
            return (2.0 * var31 - 2.0 * var17 + var21 + var23) * var29 * var29 * var29
               + (-3.0 * var31 + 3.0 * var17 - 2.0 * var21 - var23) * var29 * var29
               + var21 * var29
               + var31;
         } else {
            throw ParticleEffectDefinition.bj("catmull curve input outside reference domain");
         }
      } else {
         var6 = var2;
         int var8 = 0;

         while (var8 < this.awC.length && this.awC[var8].awE <= var6) {
            var8++;
         }

         BezierNode var9 = var8 == 0 ? awD : this.awC[var8 - 1];
         if (var8 == this.awC.length) {
            throw ParticleEffectDefinition.bj("bezier chain input at or after last node");
         }

         BezierNode var10 = this.awC[var8];
         double var11 = var10.awE - var9.awE;
         if (var11 == 0.0) {
            throw ParticleEffectDefinition.bj("bezier chain zero interval");
         }

         double var13 = (var6 - var9.awE) / var11;
         double var15 = 1.0 - var13;
         return var15 * var15 * var15 * var9.awG
            + 3.0 * var15 * var15 * var13 * (var9.awG + var9.awI / 3.0)
            + 3.0 * var15 * var13 * var13 * (var10.awF - var10.awH / 3.0)
            + var13 * var13 * var13 * var10.awF;
      }
   }

   private static double method2079(JSONObject var0, String var1, String var2) {
      double var3 = var0.getDoubleValue(var1);
      if (var3 == 0.0) {
         var3 = var0.getDoubleValue(var2);
      }

      if (!Double.isFinite(var3)) {
         throw ParticleEffectDefinition.bj("non-finite curve chain value");
      } else {
         return var3;
      }
   }
}
