package dev.lone.itemsadder.molang;

import java.util.Locale;

public final class MolangParser {
   private static final int axa = 4096;
   private static final int axb = 512;
   private static final int axc = 64;
   private static final int axd = 256;

   private MolangParser() {
   }

   public static MolangExpression method2088(Object var0) {
      if (var0 instanceof Number var5) {
         double var6 = var5.doubleValue();
         if (!Double.isFinite(var6)) {
            throw bj("non-finite numeric expression");
         } else {
            return new MolangExpression(var6);
         }
      } else if (var0 instanceof String var1) {
         if (var1.length() > 4096) {
            throw bj("expression exceeds 4096 characters");
         }

         String var2 = var1.toLowerCase(Locale.ROOT).trim();
         if (var2.isEmpty()) {
            return new MolangExpression(0.0);
         }

         if (var2.matches("[+-]?(?:[0-9]+(?:\\.[0-9]*)?|\\.[0-9]+)(?:e[+-]?[0-9]+)?f?")) {
            double var7 = Double.parseDouble(var2.endsWith("f") ? var2.substring(0, var2.length() - 1) : var2);
            if (!Double.isFinite(var7)) {
               throw bj("non-finite number");
            } else {
               return new MolangExpression(var7);
            }
         } else {
            MolangCompiler var3 = new MolangCompiler(var2);
            MolangEvalNode var4 = var3.Ik();
            return new MolangExpression(var4, var3.axj);
         }
      } else {
         throw bj("expression must be a number or string");
      }
   }

   private static IllegalArgumentException bj(String var0) {
      return new IllegalArgumentException("Invalid Molang expression: " + var0);
   }
}
