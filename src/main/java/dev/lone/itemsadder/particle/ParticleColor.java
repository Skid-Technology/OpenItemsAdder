package dev.lone.itemsadder.particle;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.molang.MolangContext;
import dev.lone.itemsadder.molang.MolangExpression;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public final class ParticleColor {
   private static final Pattern aAR = Pattern.compile("[+-]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[eE][+-]?\\d+)?");
   private static final String aAS = "Native particles require a provably opaque tint alpha of 1";
   private final ColorProvider aAT;

   public ParticleColor(Object var1) {
      this.aAT = method2193(var1);
   }

   public int rgb(MolangContext context) {
      return this.aAT.rgb(context);
   }

   private static ColorProvider method2193(Object var0) {
      if (var0 == null) {
         return var0x -> 16777215;
      } else if (var0 instanceof String var5) {
         int var2 = dT(var5);
         return var1x -> var2;
      } else if (var0 instanceof JSONArray var4) {
         return method2194(var4);
      } else if (var0 instanceof List var3) {
         return method2194(new JSONArray(var3));
      } else if (var0 instanceof JSONObject var1) {
         return method2196(var1);
      } else {
         throw new IllegalArgumentException("Particle tint must be a hex string, RGB/RGBA array, gradient object, or null");
      }
   }

   private static ColorProvider method2194(JSONArray var0) {
      if (var0.size() >= 3 && var0.size() <= 4) {
         MolangValue[] var1 = new MolangValue[3];

         for (int var2 = 0; var2 < 3; var2++) {
            var1[var2] = method2195(var0.get(var2));
         }

         if (var0.size() == 4) {
            MolangValue var3 = method2195(var0.get(3));
            if (var3.aAX == null || var3.aAX != 1.0) {
               throw new IllegalArgumentException("Native particles require a provably opaque tint alpha of 1");
            }
         }

         return var1x -> method2199(var1[0].method5(var1x), var1[1].method5(var1x), var1[2].method5(var1x));
      } else {
         throw new IllegalArgumentException("Particle tint array must have 3 or 4 channels");
      }
   }

   private static MolangValue method2195(Object var0) {
      if (var0 instanceof Number var4) {
         double var5 = var4.doubleValue();
         if (!Double.isFinite(var5)) {
            throw new IllegalArgumentException("Particle tint channel must be finite");
         } else {
            return new MolangValue(null, var5);
         }
      } else if (var0 instanceof String var1) {
         if (aAR.matcher(var1.trim()).matches()) {
            double var2 = Double.parseDouble(var1.trim());
            if (!Double.isFinite(var2)) {
               throw new IllegalArgumentException("Particle tint channel must be finite");
            } else {
               return new MolangValue(null, var2);
            }
         } else {
            return new MolangValue(ParticleEffectDefinition.method2074(var1), null);
         }
      } else {
         throw new IllegalArgumentException("Particle tint channels must be numeric or Molang strings");
      }
   }

   private static ColorProvider method2196(JSONObject var0) {
      Object var1 = var0.get("gradient");
      if (var1 == null) {
         throw new IllegalArgumentException("Particle tint gradient is required");
      }

      ArrayList var2 = new ArrayList();
      double var3 = 1.0;
      if (var1 instanceof JSONArray var6) {
         if (var6.isEmpty() || var6.size() > 256) {
            throw new IllegalArgumentException("Particle tint gradient must have 1..256 stops");
         }

         for (int var7 = 0; var7 < var6.size(); var7++) {
            double var8 = var6.size() == 1 ? 0.0 : var7 * 100.0 / (var6.size() - 1);
            var2.add(new ColorStop(var8, method2197(var6.get(var7))));
         }
      } else {
         if (!(var1 instanceof JSONObject var5)) {
            throw new IllegalArgumentException("Particle tint gradient must be an array or time-keyed object");
         }

         if (var5.isEmpty() || var5.size() > 256) {
            throw new IllegalArgumentException("Particle tint gradient must have 1..256 stops");
         }

         ArrayList var15 = new ArrayList();

         for (String var9 : var5.keySet()) {
            double var10;
            try {
               var10 = Double.parseDouble(var9);
            } catch (NumberFormatException var13) {
               throw new IllegalArgumentException("Particle tint gradient time must be numeric: " + var9);
            }

            if (!Double.isFinite(var10)) {
               throw new IllegalArgumentException("Particle tint gradient time must be finite");
            }

            var15.add(new TimedValue(var10, var5.get(var9)));
         }

         var15.sort(Comparator.comparingDouble(TimedValue::nr));
         double var18 = Double.NEGATIVE_INFINITY;

         for (TimedValue var11 : var15) {
            if (var11.aAU <= var18) {
               throw new IllegalArgumentException("Particle tint gradient times must be strictly ordered");
            }

            var18 = var11.aAU;
            var3 = Math.max(var3, var11.aAU);
         }

         for (TimedValue var22 : var15) {
            var2.add(new ColorStop(var22.aAU / var3 * 100.0, method2197(var22.aAV)));
         }
      }

      Object var14 = var0.getOrDefault("interpolant", "v.particle_age / v.particle_lifetime");
      MolangExpression var16 = ParticleEffectDefinition.method2074(var14);
      double var19 = var3;
      return var4 -> {
         double var5x = var16.method2089(var4) / var19 * 100.0;
         int var7x = 0;

         for (int var8x = 0; var8x < var2.size(); var8x++) {
            if (((ColorStop)var2.get(var8x)).aAY <= var5x) {
               var7x = var8x;
            }
         }

         if (var7x + 1 >= var2.size()) {
            return ((ColorStop)var2.get(var7x)).aAZ.rgb(var4);
         }

         ColorStop var14x = (ColorStop)var2.get(var7x);
         ColorStop var9x = (ColorStop)var2.get(var7x + 1);
         double var10x = (var5x - var14x.aAY) / (var9x.aAY - var14x.aAY);
         int var12 = var14x.aAZ.rgb(var4);
         int var13x = var9x.aAZ.rgb(var4);
         return method2200(
            (var12 >>> 16 & 0xFF) + ((var13x >>> 16 & 0xFF) - (var12 >>> 16 & 0xFF)) * var10x,
            (var12 >>> 8 & 0xFF) + ((var13x >>> 8 & 0xFF) - (var12 >>> 8 & 0xFF)) * var10x,
            (var12 & 0xFF) + ((var13x & 0xFF) - (var12 & 0xFF)) * var10x
         );
      };
   }

   private static ColorProvider method2197(Object var0) {
      if (var0 instanceof JSONObject) {
         throw new IllegalArgumentException("Nested particle tint gradients are unsupported");
      } else {
         return method2193(var0);
      }
   }

   private static int dT(String var0) {
      if (var0.startsWith("#") && (var0.length() == 7 || var0.length() == 9)) {
         String var1 = var0.substring(1);
         if (!var1.matches("[0-9a-fA-F]+")) {
            throw new IllegalArgumentException("Invalid particle tint hex: " + var0);
         }

         if (var1.length() == 6) {
            var1 = "ff" + var1;
         }

         String var2 = var1.substring(2, 8) + var1.substring(0, 2);
         int var3 = Integer.parseInt(var2.substring(0, 2), 16);
         int var4 = Integer.parseInt(var2.substring(2, 4), 16);
         int var5 = Integer.parseInt(var2.substring(4, 6), 16);
         int var6 = Integer.parseInt(var2.substring(6, 8), 16);
         if (var6 != 255) {
            throw new IllegalArgumentException("Native particles require a provably opaque tint alpha of 1");
         } else {
            return var3 << 16 | var4 << 8 | var5;
         }
      } else {
         throw new IllegalArgumentException("Particle tint string must be exported #RRGGBB or #AARRGGBB hex");
      }
   }

   private static int method2198(double var0) {
      if (!Double.isFinite(var0)) {
         throw new ArithmeticException("Particle tint expression produced a non-finite value");
      }

      double var2 = Math.max(0.0, Math.min(1.0, var0));
      return (int)Math.floor(var2 * 255.0 + 0.5);
   }

   private static int method2199(double var0, double var2, double var4) {
      return method2198(var0) << 16 | method2198(var2) << 8 | method2198(var4);
   }

   private static int method2200(double var0, double var2, double var4) {
      return method2199(var0 / 255.0, var2 / 255.0, var4 / 255.0);
   }
}
