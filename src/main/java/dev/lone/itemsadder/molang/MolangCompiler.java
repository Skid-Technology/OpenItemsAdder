package dev.lone.itemsadder.molang;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

final class MolangCompiler {
   final String axh;
   final List axi = new ArrayList();
   final Set axj = new HashSet();
   int auY;
   int axk;
   int IM;
   int axl;

   MolangCompiler(String var1) {
      this.axh = var1;
      this.method2092();
   }

   MolangEvalNode Ik() {
      MolangEvalNode var1 = this.bm(false);
      this.method2107(TokenKind.END, "end of expression");
      return var1;
   }

   private void method2092() {
      int var1 = 0;

      while (var1 < this.axh.length()) {
         char var2 = this.axh.charAt(var1);
         if (Character.isWhitespace(var2)) {
            var1++;
         } else {
            int var3 = var1;
            if (Character.isDigit(var2) || var2 == '.' && var1 + 1 < this.axh.length() && Character.isDigit(this.axh.charAt(var1 + 1))) {
               boolean var11 = false;
               if (var2 == '.') {
                  var11 = true;
                  var1++;
               }

               while (var1 < this.axh.length() && Character.isDigit(this.axh.charAt(var1))) {
                  var1++;
               }

               if (!var11 && var1 < this.axh.length() && this.axh.charAt(var1) == '.') {
                  var1++;

                  while (var1 < this.axh.length() && Character.isDigit(this.axh.charAt(var1))) {
                     var1++;
                  }
               }

               if (var1 < this.axh.length() && (this.axh.charAt(var1) == 'e' || this.axh.charAt(var1) == 'E')) {
                  int var12 = var1++;
                  if (var1 < this.axh.length() && (this.axh.charAt(var1) == '+' || this.axh.charAt(var1) == '-')) {
                     var1++;
                  }

                  int var13 = var1;

                  while (var1 < this.axh.length() && Character.isDigit(this.axh.charAt(var1))) {
                     var1++;
                  }

                  if (var13 == var1) {
                     var1 = var12;
                  }
               }

               if (var1 < this.axh.length() && this.axh.charAt(var1) == 'f') {
                  var1++;
               }

               this.axi.add(new MolangToken(this.axh.substring(var3, var1), TokenKind.NUMBER, var3));
            } else if (!Character.isLetter(var2) && var2 != '_') {
               if (var2 == '\'') {
                  var1++;
                  StringBuilder var10 = new StringBuilder();

                  while (var1 < this.axh.length() && this.axh.charAt(var1) != '\'') {
                     var10.append(this.axh.charAt(var1++));
                  }

                  if (var1 == this.axh.length()) {
                     throw MolangParser.bj("unterminated string");
                  }

                  var1++;
                  this.axi.add(new MolangToken(var10.toString(), TokenKind.STRING, var3));
               } else {
                  String var4 = null;
                  String[] var5 = new String[]{"==", "!=", "<=", ">=", "&&", "||", "??", "+=", "-=", "*=", "/="};
                  int var6 = var5.length;
                  int var7 = 0;

                  while (true) {
                     if (var7 < var6) {
                        String var8 = var5[var7];
                        if (!this.axh.startsWith(var8, var1)) {
                           var7++;
                           continue;
                        }

                        var4 = var8;
                     }

                     if (var4 == null && "+-*/!?<>=(),;{}:".indexOf(var2) >= 0) {
                        var4 = String.valueOf(var2);
                     }

                     if (var4 == null) {
                        throw MolangParser.bj("unsupported character at " + var1);
                     }

                     this.axi.add(new MolangToken(var4, TokenKind.OP, var1));
                     var1 += var4.length();
                     break;
                  }
               }
            } else {
               var1++;

               while (
                  var1 < this.axh.length()
                     && (Character.isLetterOrDigit(this.axh.charAt(var1)) || this.axh.charAt(var1) == '_' || this.axh.charAt(var1) == '.')
               ) {
                  var1++;
               }

               this.axi.add(new MolangToken(this.axh.substring(var3, var1), TokenKind.IDENT, var3));
            }
         }
      }

      this.axi.add(new MolangToken("", TokenKind.END, this.axh.length()));
   }

   private MolangEvalNode bm(boolean var1) {
      ArrayList var2 = new ArrayList();

      while (!this.method2108(TokenKind.END) && (!var1 || !this.dL("}"))) {
         if (!this.dI(";")) {
            var2.add(this.Im());
            if (!this.dI(";")) {
               if (var1 && this.dL("}")) {
                  break;
               }

               if (!this.method2108(TokenKind.END)) {
                  throw this.dM("expected ';'");
               }
            }
         }
      }

      if (var1) {
         this.dK("}");
      }

      return this.method2106((var1x, var2x) -> {
         double var3 = 0.0;

         for (MolangEvalNode var6 : var2) {
            var3 = var6.eval(var1x, var2x);
            if (var2x.axm != 0) {
               break;
            }
         }

         return var3;
      });
   }

   private MolangEvalNode Im() {
      if (this.dJ("return")) {
         MolangEvalNode var4 = this.In();
         return this.method2106((var1x, var2x) -> {
            double var3x = var4.eval(var1x, var2x);
            var2x.axm = 1;
            return var3x;
         });
      }

      if (this.dJ("break")) {
         if (this.axl == 0) {
            throw this.dM("break outside loop");
         } else {
            return this.method2106((var0, var1x) -> {
               var1x.axm = 2;
               return 0.0;
            });
         }
      } else if (this.dJ("continue")) {
         if (this.axl == 0) {
            throw this.dM("continue outside loop");
         } else {
            return this.method2106((var0, var1x) -> {
               var1x.axm = 3;
               return 0.0;
            });
         }
      } else if (this.method2108(TokenKind.IDENT) && this.bQ(1).axr.equals("=")) {
         String var1 = this.IB().axr;
         this.IB();
         String var2 = dN(var1);
         if (!var2.matches("(?:variable|temp)\\.[a-z0-9_]+")) {
            throw this.dM("only simple variable/temp assignment is supported");
         }

         MolangEvalNode var3 = this.In();
         return this.method2106((var2x, var3x) -> {
            var2x.method2090(var2, var3.eval(var2x, var3x));
            return 0.0;
         });
      } else if (!this.dL("+=") && !this.dL("-=") && !this.dL("*=") && !this.dL("/=")) {
         return this.In();
      } else {
         throw this.dM("compound assignment is unsupported by pinned Molang");
      }
   }

   private MolangEvalNode In() {
      return this.method2093();
   }

   private MolangEvalNode method2093() {
      MolangEvalNode var1 = this.Ip();
      if (!this.dI("?")) {
         return var1;
      }

      this.Iy();

      try {
         MolangEvalNode var2 = this.In();
         MolangEvalNode var3 = (var0, var1x) -> 0.0;
         if (this.dI(":")) {
            var3 = this.In();
         }

         MolangEvalNode var4 = var3;
         return this.method2106((var3x, var4x) -> method2105(var1.eval(var3x, var4x)) ? var2.eval(var3x, var4x) : var4.eval(var3x, var4x));
      } finally {
         this.Iz();
      }
   }

   private MolangEvalNode Ip() {
      MolangEvalNode var1 = this.Iq();

      while (this.dI("||")) {
         MolangEvalNode var2 = var1;
         MolangEvalNode var3 = this.Iq();
         var1 = this.method2106((var2x, var3x) -> !method2105(var2.eval(var2x, var3x)) && !method2105(var3.eval(var2x, var3x)) ? 0.0 : 1.0);
      }

      return var1;
   }

   private MolangEvalNode Iq() {
      MolangEvalNode var1 = this.Ir();

      while (this.dI("&&")) {
         MolangEvalNode var2 = var1;
         MolangEvalNode var3 = this.Ir();
         var1 = this.method2106((var2x, var3x) -> method2105(var2.eval(var2x, var3x)) && method2105(var3.eval(var2x, var3x)) ? 1.0 : 0.0);
      }

      return var1;
   }

   private MolangEvalNode Ir() {
      MolangEvalNode var1 = this.Is();

      while (this.dL("==") || this.dL("!=")) {
         String var2 = this.IB().axr;
         MolangEvalNode var3 = var1;
         MolangEvalNode var4 = this.Is();
         var1 = this.method2106((var3x, var4x) -> {
            double var5 = var3.eval(var3x, var4x);
            double var7 = var4.eval(var3x, var4x);
            boolean var9 = var5 == var7;
            return (var2.equals("==") ? !var9 : var9) ? 0.0 : 1.0;
         });
      }

      return var1;
   }

   private MolangEvalNode Is() {
      MolangEvalNode var1 = this.It();

      while (this.dL("<") || this.dL("<=") || this.dL(">") || this.dL(">=")) {
         String var2 = this.IB().axr;
         MolangEvalNode var3 = var1;
         MolangEvalNode var4 = this.It();
         var1 = this.method2106((var3x, var4x) -> {
            double var5 = var3.eval(var3x, var4x);
            double var7 = var4.eval(var3x, var4x);

            return switch (var2) {
               case "<" -> var5 < var7 ? 1.0 : 0.0;
               case "<=" -> var5 <= var7 ? 1.0 : 0.0;
               case ">" -> var5 > var7 ? 1.0 : 0.0;
               default -> var5 >= var7 ? 1.0 : 0.0;
            };
         });
      }

      return var1;
   }

   private MolangEvalNode It() {
      MolangEvalNode var1 = this.Iu();

      while (this.dL("+") || this.dL("-")) {
         String var2 = this.IB().axr;
         MolangEvalNode var3 = var1;
         MolangEvalNode var4 = this.Iu();
         var1 = this.method2106(
            (var3x, var4x) -> var2.equals("+") ? var3.eval(var3x, var4x) + var4.eval(var3x, var4x) : var3.eval(var3x, var4x) - var4.eval(var3x, var4x)
         );
      }

      return var1;
   }

   private MolangEvalNode Iu() {
      MolangEvalNode var1 = this.Iv();

      while (this.dL("*") || this.dL("/")) {
         String var2 = this.IB().axr;
         MolangEvalNode var3 = var1;
         MolangEvalNode var4 = this.Iv();
         var1 = this.method2106(
            (var3x, var4x) -> var2.equals("*") ? var3.eval(var3x, var4x) * var4.eval(var3x, var4x) : var3.eval(var3x, var4x) / var4.eval(var3x, var4x)
         );
      }

      return var1;
   }

   private MolangEvalNode Iv() {
      if (this.dI("+")) {
         this.Iy();

         try {
            MolangEvalNode var19 = this.Iv();
            return this.method2106((var1x, var2) -> var19.eval(var1x, var2));
         } finally {
            this.Iz();
         }
      } else if (this.dI("-")) {
         this.Iy();

         try {
            MolangEvalNode var18 = this.Iv();
            return this.method2106((var1x, var2) -> -var18.eval(var1x, var2));
         } finally {
            this.Iz();
         }
      } else if (this.dI("!")) {
         this.Iy();

         try {
            MolangEvalNode var1 = this.Iv();
            return this.method2106((var1x, var2) -> var1.eval(var1x, var2) == 0.0 ? 1.0 : 0.0);
         } finally {
            this.Iz();
         }
      } else {
         return this.Iw();
      }
   }

   private MolangEvalNode Iw() {
      if (this.dI("(")) {
         this.Iy();

         try {
            MolangEvalNode var23 = this.In();
            this.dK(")");
            return var23;
         } finally {
            this.Iz();
         }
      } else if (this.dI("{")) {
         this.Iy();

         try {
            return this.bm(true);
         } finally {
            this.Iz();
         }
      } else {
         MolangToken var1 = this.IB();
         if (var1.axs == TokenKind.NUMBER) {
            String var24 = var1.axr.endsWith("f") ? var1.axr.substring(0, var1.axr.length() - 1) : var1.axr;

            double var25;
            try {
               var25 = Double.parseDouble(var24);
            } catch (NumberFormatException var21) {
               throw MolangParser.bj("invalid number at " + var1.axt);
            }

            if (!Double.isFinite(var25)) {
               throw MolangParser.bj("non-finite number");
            } else {
               return this.method2106((var2x, var3x) -> var25);
            }
         } else {
            if (var1.axs == TokenKind.STRING) {
               throw MolangParser.bj("string values are unsupported in numeric expressions");
            }

            if (var1.axs != TokenKind.IDENT) {
               throw MolangParser.bj("expected expression at " + var1.axt);
            }

            if (var1.axr.equals("true")) {
               return this.method2106((var0, var1x) -> 1.0);
            }

            if (var1.axr.equals("false")) {
               return this.method2106((var0, var1x) -> 0.0);
            }

            String var2 = dN(var1.axr);
            if (var2.equals("math.pi")) {
               if (this.dI("(")) {
                  throw this.dM("math.pi is a constant");
               } else {
                  return this.method2106((var0, var1x) -> Math.PI);
               }
            } else if (this.dI("(")) {
               this.Iy();

               try {
                  if (var2.equals("loop")) {
                     return this.Ix();
                  }

                  ArrayList var3 = new ArrayList();
                  if (!this.dI(")")) {
                     do {
                        var3.add(this.In());
                     } while (this.dI(","));

                     this.dK(")");
                  }

                  return this.method2094(var2, var3);
               } finally {
                  this.Iz();
               }
            } else {
               this.axj.add(var2);
               return this.method2106((var1x, var2x) -> var1x.dH(var2));
            }
         }
      }
   }

   private MolangEvalNode Ix() {
      MolangEvalNode var1 = this.In();
      this.dK(",");
      this.axl++;

      MolangEvalNode var2;
      try {
         if (this.dI("{")) {
            this.Iy();

            try {
               var2 = this.bm(true);
            } finally {
               this.Iz();
            }
         } else {
            var2 = this.Im();
         }
      } finally {
         this.axl--;
      }

      this.dK(")");
      return this.method2106((var2x, var3) -> {
         double var4 = var1.eval(var2x, var3);
         double var6 = method2101(var4, 0.0, 1024.0);
         int var8 = (int)Math.ceil(var6);
         if (var8 > 256) {
            throw MolangParser.bj("loop count exceeds 256");
         }

         if (var3.axn + var8 > 256) {
            throw MolangParser.bj("total loop iterations exceed 256");
         }

         var3.axn += var8;
         double var9x = 0.0;

         for (int var11 = 0; var11 < var8; var11++) {
            var9x = var2.eval(var2x, var3);
            if (var3.axm == 1) {
               return var9x;
            }

            if (var3.axm == 2) {
               var3.axm = 0;
               break;
            }

            if (var3.axm == 3) {
               var3.axm = 0;
            }
         }

         return var9x;
      });
   }

   private MolangEvalNode method2094(String var1, List var2) {
      byte var3 = switch (var1) {
         case "math.abs", "math.sin", "math.cos", "math.exp", "math.ln", "math.sqrt", "math.ceil", "math.round", "math.trunc", "math.floor", "math.asin", "math.acos", "math.atan", "math.hermite_blend", "math.min_angle" -> 1;
         case "math.pow", "math.random", "math.mod", "math.min", "math.max", "math.atan2", "math.random_integer" -> 2;
         case "math.clamp", "math.lerp", "math.lerprotate", "math.die_roll", "math.die_roll_integer" -> 3;
         case "query.in_range" -> 3;
         case "query.all", "query.any", "query.approx_eq" -> -1;
         default -> throw this.dM("unsupported function " + var1);
      };
      if (var3 >= 0 && var2.size() != var3) {
         throw this.dM("wrong argument count for " + var1);
      } else if (var3 == -1 && var2.isEmpty()) {
         throw this.dM("wrong argument count for " + var1);
      } else {
         return this.method2106(
            (var3x, var4) -> {
               if (var1.equals("math.random") || var1.equals("math.random_integer")) {
                  double var18 = this.method2095(var2, 0, var3x, var4);
                  double var20 = this.method2095(var2, 1, var3x, var4);
                  double var22 = this.method2100(var3x, var4);
                  return var1.endsWith("integer")
                     ? Math.ceil(var18) + Math.floor(var22 * (Math.floor(var20) - Math.ceil(var18) + 1.0))
                     : var18 + var22 * (var20 - var18);
               }

               if (var1.equals("math.die_roll") || var1.equals("math.die_roll_integer")) {
                  double var17 = method2101(this.method2095(var2, 0, var3x, var4), 0.0, 1.0E9);
                  double var19 = this.method2095(var2, 1, var3x, var4);
                  double var21 = this.method2095(var2, 2, var3x, var4);
                  int var23 = (int)Math.ceil(var17);
                  if (var23 <= 256 && var4.axo + var23 <= 256) {
                     var4.axo += var23;
                     double var24 = 0.0;

                     for (int var14 = 0; var14 < var23; var14++) {
                        double var15 = this.method2100(var3x, var4);
                        var24 += var1.endsWith("integer")
                           ? Math.ceil(var19) + Math.floor(var15 * (Math.floor(var21) - Math.ceil(var19) + 1.0))
                           : var19 + var15 * (var21 - var19);
                     }

                     return var24;
                  } else {
                     throw MolangParser.bj("die_roll count exceeds 256");
                  }
               } else {
                  if (var1.equals("query.all")) {
                     return this.method2096(var2, var3x, var4);
                  }

                  if (var1.equals("query.any")) {
                     return this.method2097(var2, var3x, var4);
                  }

                  if (var1.equals("query.approx_eq")) {
                     return this.method2098(var2, var3x, var4);
                  }

                  double var5 = this.method2095(var2, 0, var3x, var4);
                  double var7 = var2.size() > 1 ? this.method2095(var2, 1, var3x, var4) : 0.0;
                  double var9 = var2.size() > 2 ? this.method2095(var2, 2, var3x, var4) : 0.0;

                  return switch (var1) {
                     case "math.abs" -> Math.abs(var5);
                     case "math.sin" -> Math.sin(var5 * (Math.PI / 180.0));
                     case "math.cos" -> Math.cos(var5 * (Math.PI / 180.0));
                     case "math.exp" -> Math.exp(var5);
                     case "math.ln" -> Math.log(var5);
                     case "math.pow" -> Math.pow(var5, var7);
                     case "math.sqrt" -> Math.sqrt(var5);
                     case "math.ceil" -> Math.ceil(var5);
                     case "math.round" -> Math.floor(var5 + 0.5);
                     case "math.trunc" -> var5 < 0.0 ? Math.ceil(var5) : Math.floor(var5);
                     case "math.floor" -> Math.floor(var5);
                     case "math.mod" -> var5 % var7;
                     case "math.min" -> Math.min(var5, var7);
                     case "math.max" -> Math.max(var5, var7);
                     case "math.clamp" -> method2101(var5, var7, var9);
                     case "math.lerp" -> var5 + (var7 - var5) * var9;
                     case "math.lerprotate" -> method2103(var5, var7, var9);
                     case "math.asin" -> Math.asin(var5) / (Math.PI / 180.0);
                     case "math.acos" -> Math.acos(var5) / (Math.PI / 180.0);
                     case "math.atan" -> Math.atan(var5) / (Math.PI / 180.0);
                     case "math.atan2" -> Math.atan2(var5, var7) / (Math.PI / 180.0);
                     case "math.hermite_blend" -> 3.0 * var5 * var5 - 2.0 * var5 * var5 * var5;
                     case "math.min_angle" -> method2104(var5);
                     case "query.in_range" -> var5 <= var9 && var5 >= var7 ? 1.0 : 0.0;
                     case "query.all" -> this.method2096(var2, var3x, var4);
                     case "query.any" -> this.method2097(var2, var3x, var4);
                     case "query.approx_eq" -> this.method2098(var2, var3x, var4);
                     default -> throw new AssertionError(var1);
                  };
               }
            }
         );
      }
   }

   private double method2095(List var1, int var2, MolangContext var3, MolangEvalState var4) {
      return ((MolangEvalNode)var1.get(var2)).eval(var3, var4);
   }

   private double method2096(List var1, MolangContext var2, MolangEvalState var3) {
      double[] var4 = this.method2099(var1, var2, var3);

      for (int var5 = 1; var5 < var4.length; var5++) {
         if (var4[var5] != var4[0]) {
            return 0.0;
         }
      }

      return 1.0;
   }

   private double method2097(List var1, MolangContext var2, MolangEvalState var3) {
      double[] var4 = this.method2099(var1, var2, var3);

      for (int var5 = 1; var5 < var4.length; var5++) {
         if (var4[var5] == var4[0]) {
            return 1.0;
         }
      }

      return 0.0;
   }

   private double method2098(List var1, MolangContext var2, MolangEvalState var3) {
      double[] var4 = this.method2099(var1, var2, var3);

      for (int var5 = 1; var5 < var4.length; var5++) {
         if (Math.abs(var4[0] - var4[var5]) > 1.0E-7) {
            return 0.0;
         }
      }

      return 1.0;
   }

   private double[] method2099(List var1, MolangContext var2, MolangEvalState var3) {
      double[] var4 = new double[var1.size()];

      for (int var5 = 0; var5 < var4.length; var5++) {
         var4[var5] = this.method2095(var1, var5, var2, var3);
      }

      return var4;
   }

   private double method2100(MolangContext var1, MolangEvalState var2) {
      if (++var2.axq > 65536) {
         throw MolangParser.bj("random call budget exceeded");
      } else {
         double var3 = var1.Ii();
         if (var3 >= 0.0 && var3 < 1.0) {
            return var3;
         } else {
            throw MolangParser.bj("random source must return [0,1)");
         }
      }
   }

   private static double method2101(double var0, double var2, double var4) {
      if (var0 > var4) {
         var0 = var4;
      }

      if (var0 < var2 || Double.isNaN(var0)) {
         var0 = var2;
      }

      return var0;
   }

   private static double method2102(double var0) {
      return ((var0 + 180.0) % 360.0 + 180.0) % 360.0;
   }

   private static double method2103(double var0, double var2, double var4) {
      var0 = method2102(var0);
      var2 = method2102(var2);
      if (var0 > var2) {
         double var6 = var0;
         var0 = var2;
         var2 = var6;
      }

      double var10 = var2 - var0;
      return var10 > 180.0 ? method2102(var2 + var4 * (360.0 - var10)) : var0 + var4 * var10;
   }

   private static double method2104(double var0) {
      double var2 = Math.max(4.0, 4.0 + Math.floor(-var0 / 360.0 + 0.5)) * 2.0 + 1.0;
      return (var0 + 180.0 * var2) % 360.0 - 180.0;
   }

   private static boolean method2105(double var0) {
      return var0 != 0.0 && !Double.isNaN(var0);
   }

   private MolangEvalNode method2106(MolangNode var1) {
      if (++this.axk > 512) {
         throw this.dM("expression exceeds 512 nodes");
      } else {
         return (var1x, var2) -> {
            if (++var2.axp > 150000) {
               throw MolangParser.bj("evaluation budget exceeded");
            } else {
               return var1.eval(var1x, var2);
            }
         };
      }
   }

   private void Iy() {
      if (++this.IM > 64) {
         throw this.dM("expression nesting exceeds 64");
      }
   }

   private void Iz() {
      this.IM--;
   }

   private boolean dI(String var1) {
      if (this.dL(var1)) {
         this.auY++;
         return true;
      } else {
         return false;
      }
   }

   private boolean dJ(String var1) {
      if (this.method2108(TokenKind.IDENT) && this.dL(var1)) {
         this.auY++;
         return true;
      } else {
         return false;
      }
   }

   private void dK(String var1) {
      if (!this.dI(var1)) {
         throw this.dM("expected '" + var1 + "'");
      }
   }

   private MolangToken method2107(TokenKind var1, String var2) {
      if (!this.method2108(var1)) {
         throw this.dM("expected " + var2);
      } else {
         return this.IB();
      }
   }

   private boolean dL(String var1) {
      return this.IA().axr.equals(var1);
   }

   private boolean method2108(TokenKind var1) {
      return this.IA().axs == var1;
   }

   private MolangToken IA() {
      return (MolangToken)this.axi.get(this.auY);
   }

   private MolangToken bQ(int var1) {
      return (MolangToken)this.axi.get(Math.min(this.auY + var1, this.axi.size() - 1));
   }

   private MolangToken IB() {
      return (MolangToken)this.axi.get(this.auY++);
   }

   private IllegalArgumentException dM(String var1) {
      return MolangParser.bj(var1 + " at " + this.IA().axt);
   }

   private static String dN(String var0) {
      String var1 = var0.toLowerCase(Locale.ROOT);
      if (var1.startsWith("q.")) {
         return "query" + var1.substring(1);
      } else if (var1.startsWith("v.")) {
         return "variable" + var1.substring(1);
      } else if (var1.startsWith("t.")) {
         return "temp" + var1.substring(1);
      } else {
         return var1.startsWith("c.") ? "context" + var1.substring(1) : var1;
      }
   }
}
