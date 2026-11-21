package dev.lone.itemsadder.util;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.euclidean.threed.CardanEulerSingularityException;
import org.apache.commons.math3.geometry.euclidean.threed.NotARotationMatrixException;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

public class Rotation3D implements Serializable {
   public static final Rotation3D IDENTITY = new Rotation3D(1.0, 0.0, 0.0, 0.0, false);
   public static final double TOLERANCE = 1.0E-6;
   private static final long serialVersionUID = -2153622329907944313L;
   double q0;
   double q1;
   double q2;
   double q3;

   public Rotation3D(double var1, double var3, double var5, double var7, boolean var9) {
      if (var9) {
         double var10 = 1.0 / FastMath.sqrt(var1 * var1 + var3 * var3 + var5 * var5 + var7 * var7);
         var1 *= var10;
         var3 *= var10;
         var5 *= var10;
         var7 *= var10;
      }

      this.q0 = var1;
      this.q1 = var3;
      this.q2 = var5;
      this.q3 = var7;
   }

   @Deprecated
   public Rotation3D(Vector3D var1, double var2) {
      this(var1, var2, RotationConvention.VECTOR_OPERATOR);
   }

   public Rotation3D(Vector3D var1, double var2, RotationConvention var4) {
      double var5 = var1.getNorm();
      if (var5 == 0.0) {
         throw new MathIllegalArgumentException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_AXIS, new Object[0]);
      }

      double var7 = var4 == RotationConvention.VECTOR_OPERATOR ? -0.5 * var2 : 0.5 * var2;
      double var9 = FastMath.sin(var7) / var5;
      this.q0 = FastMath.cos(var7);
      this.q1 = var9 * var1.getX();
      this.q2 = var9 * var1.getY();
      this.q3 = var9 * var1.getZ();
   }

   public Rotation3D(double[][] var1, double var2) {
      if (var1.length == 3 && var1[0].length == 3 && var1[1].length == 3 && var1[2].length == 3) {
         double[][] var4 = this.orthogonalizeMatrix(var1, var2);
         double var5 = var4[0][0] * (var4[1][1] * var4[2][2] - var4[2][1] * var4[1][2])
            - var4[1][0] * (var4[0][1] * var4[2][2] - var4[2][1] * var4[0][2])
            + var4[2][0] * (var4[0][1] * var4[1][2] - var4[1][1] * var4[0][2]);
         if (var5 < 0.0) {
            throw new NotARotationMatrixException(LocalizedFormats.CLOSEST_ORTHOGONAL_MATRIX_HAS_NEGATIVE_DETERMINANT, new Object[]{var5});
         }

         double[] var7 = matrixToQuaternion(var4);
         this.q0 = var7[0];
         this.q1 = var7[1];
         this.q2 = var7[2];
         this.q3 = var7[3];
      } else {
         throw new NotARotationMatrixException(LocalizedFormats.ROTATION_MATRIX_DIMENSIONS, new Object[]{var1.length, var1[0].length});
      }
   }

   public Rotation3D(Vector3D var1, Vector3D var2, Vector3D var3, Vector3D var4) {
      Vector3D var5 = var1.crossProduct(var2).normalize();
      var2 = var5.crossProduct(var1).normalize();
      var1 = var1.normalize();
      Vector3D var6 = var3.crossProduct(var4).normalize();
      var4 = var6.crossProduct(var3).normalize();
      var3 = var3.normalize();
      double[][] var7 = new double[][]{
         {
               MathArrays.linearCombination(var1.getX(), var3.getX(), var2.getX(), var4.getX(), var5.getX(), var6.getX()),
               MathArrays.linearCombination(var1.getY(), var3.getX(), var2.getY(), var4.getX(), var5.getY(), var6.getX()),
               MathArrays.linearCombination(var1.getZ(), var3.getX(), var2.getZ(), var4.getX(), var5.getZ(), var6.getX())
         },
         {
               MathArrays.linearCombination(var1.getX(), var3.getY(), var2.getX(), var4.getY(), var5.getX(), var6.getY()),
               MathArrays.linearCombination(var1.getY(), var3.getY(), var2.getY(), var4.getY(), var5.getY(), var6.getY()),
               MathArrays.linearCombination(var1.getZ(), var3.getY(), var2.getZ(), var4.getY(), var5.getZ(), var6.getY())
         },
         {
               MathArrays.linearCombination(var1.getX(), var3.getZ(), var2.getX(), var4.getZ(), var5.getX(), var6.getZ()),
               MathArrays.linearCombination(var1.getY(), var3.getZ(), var2.getY(), var4.getZ(), var5.getY(), var6.getZ()),
               MathArrays.linearCombination(var1.getZ(), var3.getZ(), var2.getZ(), var4.getZ(), var5.getZ(), var6.getZ())
         }
      };
      double[] var8 = matrixToQuaternion(var7);
      this.q0 = var8[0];
      this.q1 = var8[1];
      this.q2 = var8[2];
      this.q3 = var8[3];
   }

   public Rotation3D(Vector3D var1, Vector3D var2) {
      double var3 = var1.getNorm() * var2.getNorm();
      if (var3 == 0.0) {
         throw new MathArithmeticException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR, new Object[0]);
      }

      double var5 = var1.dotProduct(var2);
      if (var5 < -0.999999999999998 * var3) {
         Vector3D var7 = var1.orthogonal();
         this.q0 = 0.0;
         this.q1 = -var7.getX();
         this.q2 = -var7.getY();
         this.q3 = -var7.getZ();
      } else {
         this.q0 = FastMath.sqrt(0.5 * (1.0 + var5 / var3));
         double var10 = 1.0 / (2.0 * this.q0 * var3);
         Vector3D var9 = var2.crossProduct(var1);
         this.q1 = var10 * var9.getX();
         this.q2 = var10 * var9.getY();
         this.q3 = var10 * var9.getZ();
      }
   }

   @Deprecated
   public Rotation3D(RotationOrder var1, double var2, double var4, double var6) {
      this(var1, RotationConvention.VECTOR_OPERATOR, var2, var4, var6);
   }

   public Rotation3D(RotationOrder var1, RotationConvention var2, double var3, double var5, double var7) {
      Rotation3D var9 = new Rotation3D(var1.getA1(), var3, var2);
      Rotation3D var10 = new Rotation3D(var1.getA2(), var5, var2);
      Rotation3D var11 = new Rotation3D(var1.getA3(), var7, var2);
      var10.composeInPlace(var11, var2);
      var9.composeInPlace(var11, var2);
      this.q0 = var11.q0;
      this.q1 = var11.q1;
      this.q2 = var11.q2;
      this.q3 = var11.q3;
   }

   private static double[] matrixToQuaternion(double[][] var0) {
      double[] var1 = new double[4];
      double var2 = var0[0][0] + var0[1][1] + var0[2][2];
      if (var2 > -0.19) {
         var1[0] = 0.5 * FastMath.sqrt(var2 + 1.0);
         double var4 = 0.25 / var1[0];
         var1[1] = var4 * (var0[1][2] - var0[2][1]);
         var1[2] = var4 * (var0[2][0] - var0[0][2]);
         var1[3] = var4 * (var0[0][1] - var0[1][0]);
      } else {
         var2 = var0[0][0] - var0[1][1] - var0[2][2];
         if (var2 > -0.19) {
            var1[1] = 0.5 * FastMath.sqrt(var2 + 1.0);
            double var9 = 0.25 / var1[1];
            var1[0] = var9 * (var0[1][2] - var0[2][1]);
            var1[2] = var9 * (var0[0][1] + var0[1][0]);
            var1[3] = var9 * (var0[0][2] + var0[2][0]);
         } else {
            var2 = var0[1][1] - var0[0][0] - var0[2][2];
            if (var2 > -0.19) {
               var1[2] = 0.5 * FastMath.sqrt(var2 + 1.0);
               double var10 = 0.25 / var1[2];
               var1[0] = var10 * (var0[2][0] - var0[0][2]);
               var1[1] = var10 * (var0[0][1] + var0[1][0]);
               var1[3] = var10 * (var0[2][1] + var0[1][2]);
            } else {
               var2 = var0[2][2] - var0[0][0] - var0[1][1];
               var1[3] = 0.5 * FastMath.sqrt(var2 + 1.0);
               double var11 = 0.25 / var1[3];
               var1[0] = var11 * (var0[0][1] - var0[1][0]);
               var1[1] = var11 * (var0[0][2] + var0[2][0]);
               var1[2] = var11 * (var0[2][1] + var0[1][2]);
            }
         }
      }

      return var1;
   }

   public static Rotation3D slerp(Rotation3D var0, Rotation3D var1, double var2) {
      double var4 = var0.q0 * var1.q0 + var0.q1 * var1.q1 + var0.q2 * var1.q2 + var0.q3 * var1.q3;
      if (var4 < 0.0) {
         var1 = new Rotation3D(-var1.q0, -var1.q1, -var1.q2, -var1.q3, false);
         var4 = -var4;
      }

      if (var4 > 0.999999) {
         return new Rotation3D(
            var0.q0 + var2 * (var1.q0 - var0.q0),
            var0.q1 + var2 * (var1.q1 - var0.q1),
            var0.q2 + var2 * (var1.q2 - var0.q2),
            var0.q3 + var2 * (var1.q3 - var0.q3),
            true
         );
      }

      double var6 = FastMath.acos(var4);
      double var8 = FastMath.sqrt(1.0 - var4 * var4);
      double var10 = 1.0 / var8;
      double var12 = FastMath.sin((1.0 - var2) * var6) * var10;
      double var14 = FastMath.sin(var2 * var6) * var10;
      return new Rotation3D(
         var12 * var0.getQ0() + var14 * var1.getQ0(),
         var12 * var0.getQ1() + var14 * var1.getQ1(),
         var12 * var0.getQ2() + var14 * var1.getQ2(),
         var12 * var0.getQ3() + var14 * var1.getQ3(),
         false
      );
   }

   public Rotation3D revert() {
      return new Rotation3D(-this.q0, this.q1, this.q2, this.q3, false);
   }

   public double getQ0() {
      return this.q0;
   }

   public double getQ1() {
      return this.q1;
   }

   public double getQ2() {
      return this.q2;
   }

   public double getQ3() {
      return this.q3;
   }

   @Deprecated
   public Vector3D getAxis() {
      return this.getAxisWith(RotationConvention.VECTOR_OPERATOR);
   }

   public Vector3D getAxisWith(RotationConvention var1) {
      double var2 = this.q1 * this.q1 + this.q2 * this.q2 + this.q3 * this.q3;
      if (var2 == 0.0) {
         return var1 == RotationConvention.VECTOR_OPERATOR ? Vector3D.PLUS_I : Vector3D.MINUS_I;
      } else {
         double var4 = var1 == RotationConvention.VECTOR_OPERATOR ? 1.0 : -1.0;
         if (this.q0 < 0.0) {
            double var8 = var4 / FastMath.sqrt(var2);
            return new Vector3D(this.q1 * var8, this.q2 * var8, this.q3 * var8);
         } else {
            double var6 = -var4 / FastMath.sqrt(var2);
            return new Vector3D(this.q1 * var6, this.q2 * var6, this.q3 * var6);
         }
      }
   }

   public double getAngle() {
      if (!(this.q0 < -0.1) && !(this.q0 > 0.1)) {
         return this.q0 < 0.0 ? 2.0 * FastMath.acos(-this.q0) : 2.0 * FastMath.acos(this.q0);
      } else {
         return 2.0 * MathUtils.asinFast(FastMath.sqrt(this.q1 * this.q1 + this.q2 * this.q2 + this.q3 * this.q3));
      }
   }

   @Deprecated
   public double[] getAngles(RotationOrder var1) {
      return this.computeAngles(var1, RotationConvention.VECTOR_OPERATOR);
   }

   public double[] getAnglesInDegrees(RotationOrder var1, RotationConvention var2) {
      double[] var3 = this.computeAngles(var1, var2);
      var3[0] = Math.toDegrees(var3[0]);
      var3[1] = Math.toDegrees(var3[1]);
      var3[2] = Math.toDegrees(var3[2]);
      return var3;
   }

   public double[] computeAngles(RotationOrder var1, RotationConvention var2) {
      if (var2 == RotationConvention.VECTOR_OPERATOR) {
         if (var1 == RotationOrder.XYZ) {
            Vector3D var27 = this.applyTo(Vector3D.PLUS_K);
            Vector3D var50 = this.applyInverseTo(Vector3D.PLUS_I);
            if (!(var50.getZ() < -0.9999999999) && !(var50.getZ() > 0.9999999999)) {
               return new double[]{
                  MathUtils.atan2Fast(-var27.getY(), var27.getZ()), MathUtils.asinFast(var50.getZ()), MathUtils.atan2Fast(-var50.getY(), var50.getX())
               };
            } else {
               throw new CardanEulerSingularityException(true);
            }
         } else if (var1 == RotationOrder.XZY) {
            Vector3D var26 = this.applyTo(Vector3D.PLUS_J);
            Vector3D var49 = this.applyInverseTo(Vector3D.PLUS_I);
            if (!(var49.getY() < -0.9999999999) && !(var49.getY() > 0.9999999999)) {
               return new double[]{
                  MathUtils.atan2Fast(var26.getZ(), var26.getY()), -MathUtils.asinFast(var49.getY()), MathUtils.atan2Fast(var49.getZ(), var49.getX())
               };
            } else {
               throw new CardanEulerSingularityException(true);
            }
         } else {
            if (var1 == RotationOrder.YXZ) {
               Vector3D var25 = this.applyTo(Vector3D.PLUS_K);
               Vector3D var48 = this.applyInverseTo(Vector3D.PLUS_J);
               return new double[]{
                  MathUtils.atan2Fast(var25.getX(), var25.getZ()), -MathUtils.asinFast(var48.getZ()), MathUtils.atan2Fast(var48.getX(), var48.getY())
               };
            }

            if (var1 == RotationOrder.YZX) {
               Vector3D var24 = this.applyTo(Vector3D.PLUS_I);
               Vector3D var47 = this.applyInverseTo(Vector3D.PLUS_J);
               if (!(var47.getX() < -0.9999999999) && !(var47.getX() > 0.9999999999)) {
                  return new double[]{
                     MathUtils.atan2Fast(-var24.getZ(), var24.getX()), MathUtils.asinFast(var47.getX()), MathUtils.atan2Fast(-var47.getZ(), var47.getY())
                  };
               } else {
                  throw new CardanEulerSingularityException(true);
               }
            } else if (var1 == RotationOrder.ZXY) {
               Vector3D var23 = this.applyTo(Vector3D.PLUS_J);
               Vector3D var46 = this.applyInverseTo(Vector3D.PLUS_K);
               if (!(var46.getY() < -0.9999999999) && !(var46.getY() > 0.9999999999)) {
                  return new double[]{
                     MathUtils.atan2Fast(-var23.getX(), var23.getY()), MathUtils.asinFast(var46.getY()), MathUtils.atan2Fast(-var46.getX(), var46.getZ())
                  };
               } else {
                  throw new CardanEulerSingularityException(true);
               }
            } else if (var1 == RotationOrder.ZYX) {
               Vector3D var22 = this.applyTo(Vector3D.PLUS_I);
               Vector3D var45 = this.applyInverseTo(Vector3D.PLUS_K);
               if (!(var45.getX() < -0.9999999999) && !(var45.getX() > 0.9999999999)) {
                  return new double[]{
                     MathUtils.atan2Fast(var22.getY(), var22.getX()), -MathUtils.asinFast(var45.getX()), MathUtils.atan2Fast(var45.getY(), var45.getZ())
                  };
               } else {
                  throw new CardanEulerSingularityException(true);
               }
            } else if (var1 == RotationOrder.XYX) {
               Vector3D var21 = this.applyTo(Vector3D.PLUS_I);
               Vector3D var44 = this.applyInverseTo(Vector3D.PLUS_I);
               if (!(var44.getX() < -0.9999999999) && !(var44.getX() > 0.9999999999)) {
                  return new double[]{
                     MathUtils.atan2Fast(var21.getY(), -var21.getZ()), FastMath.acos(var44.getX()), MathUtils.atan2Fast(var44.getY(), var44.getZ())
                  };
               } else {
                  throw new CardanEulerSingularityException(false);
               }
            } else if (var1 == RotationOrder.XZX) {
               Vector3D var20 = this.applyTo(Vector3D.PLUS_I);
               Vector3D var43 = this.applyInverseTo(Vector3D.PLUS_I);
               if (!(var43.getX() < -0.9999999999) && !(var43.getX() > 0.9999999999)) {
                  return new double[]{
                     MathUtils.atan2Fast(var20.getZ(), var20.getY()), FastMath.acos(var43.getX()), MathUtils.atan2Fast(var43.getZ(), -var43.getY())
                  };
               } else {
                  throw new CardanEulerSingularityException(false);
               }
            } else if (var1 == RotationOrder.YXY) {
               Vector3D var19 = this.applyTo(Vector3D.PLUS_J);
               Vector3D var42 = this.applyInverseTo(Vector3D.PLUS_J);
               if (!(var42.getY() < -0.9999999999) && !(var42.getY() > 0.9999999999)) {
                  return new double[]{
                     MathUtils.atan2Fast(var19.getX(), var19.getZ()), FastMath.acos(var42.getY()), MathUtils.atan2Fast(var42.getX(), -var42.getZ())
                  };
               } else {
                  throw new CardanEulerSingularityException(false);
               }
            } else if (var1 == RotationOrder.YZY) {
               Vector3D var18 = this.applyTo(Vector3D.PLUS_J);
               Vector3D var41 = this.applyInverseTo(Vector3D.PLUS_J);
               if (!(var41.getY() < -0.9999999999) && !(var41.getY() > 0.9999999999)) {
                  return new double[]{
                     MathUtils.atan2Fast(var18.getZ(), -var18.getX()), FastMath.acos(var41.getY()), MathUtils.atan2Fast(var41.getZ(), var41.getX())
                  };
               } else {
                  throw new CardanEulerSingularityException(false);
               }
            } else if (var1 == RotationOrder.ZXZ) {
               Vector3D var17 = this.applyTo(Vector3D.PLUS_K);
               Vector3D var40 = this.applyInverseTo(Vector3D.PLUS_K);
               if (!(var40.getZ() < -0.9999999999) && !(var40.getZ() > 0.9999999999)) {
                  return new double[]{
                     MathUtils.atan2Fast(var17.getX(), -var17.getY()), FastMath.acos(var40.getZ()), MathUtils.atan2Fast(var40.getX(), var40.getY())
                  };
               } else {
                  throw new CardanEulerSingularityException(false);
               }
            } else {
               Vector3D var16 = this.applyTo(Vector3D.PLUS_K);
               Vector3D var39 = this.applyInverseTo(Vector3D.PLUS_K);
               if (!(var39.getZ() < -0.9999999999) && !(var39.getZ() > 0.9999999999)) {
                  return new double[]{
                     MathUtils.atan2Fast(var16.getY(), var16.getX()), FastMath.acos(var39.getZ()), MathUtils.atan2Fast(var39.getY(), -var39.getX())
                  };
               } else {
                  throw new CardanEulerSingularityException(false);
               }
            }
         }
      } else if (var1 == RotationOrder.XYZ) {
         Vector3D var15 = this.applyTo(Vector3D.PLUS_I);
         Vector3D var38 = this.applyInverseTo(Vector3D.PLUS_K);
         if (!(var38.getX() < -0.9999999999) && !(var38.getX() > 0.9999999999)) {
            return new double[]{
               MathUtils.atan2Fast(-var38.getY(), var38.getZ()), MathUtils.asinFast(var38.getX()), MathUtils.atan2Fast(-var15.getY(), var15.getX())
            };
         } else {
            throw new CardanEulerSingularityException(true);
         }
      } else if (var1 == RotationOrder.XZY) {
         Vector3D var14 = this.applyTo(Vector3D.PLUS_I);
         Vector3D var37 = this.applyInverseTo(Vector3D.PLUS_J);
         if (!(var37.getX() < -0.9999999999) && !(var37.getX() > 0.9999999999)) {
            return new double[]{
               MathUtils.atan2Fast(var37.getZ(), var37.getY()), -MathUtils.asinFast(var37.getX()), MathUtils.atan2Fast(var14.getZ(), var14.getX())
            };
         } else {
            throw new CardanEulerSingularityException(true);
         }
      } else if (var1 == RotationOrder.YXZ) {
         Vector3D var13 = this.applyTo(Vector3D.PLUS_J);
         Vector3D var36 = this.applyInverseTo(Vector3D.PLUS_K);
         if (!(var36.getY() < -0.9999999999) && !(var36.getY() > 0.9999999999)) {
            return new double[]{
               MathUtils.atan2Fast(var36.getX(), var36.getZ()), -MathUtils.asinFast(var36.getY()), MathUtils.atan2Fast(var13.getX(), var13.getY())
            };
         } else {
            throw new CardanEulerSingularityException(true);
         }
      } else if (var1 == RotationOrder.YZX) {
         Vector3D var12 = this.applyTo(Vector3D.PLUS_J);
         Vector3D var35 = this.applyInverseTo(Vector3D.PLUS_I);
         if (!(var35.getY() < -0.9999999999) && !(var35.getY() > 0.9999999999)) {
            return new double[]{
               MathUtils.atan2Fast(-var35.getZ(), var35.getX()), MathUtils.asinFast(var35.getY()), MathUtils.atan2Fast(-var12.getZ(), var12.getY())
            };
         } else {
            throw new CardanEulerSingularityException(true);
         }
      } else if (var1 == RotationOrder.ZXY) {
         Vector3D var11 = this.applyTo(Vector3D.PLUS_K);
         Vector3D var34 = this.applyInverseTo(Vector3D.PLUS_J);
         if (!(var34.getZ() < -0.9999999999) && !(var34.getZ() > 0.9999999999)) {
            return new double[]{
               MathUtils.atan2Fast(-var34.getX(), var34.getY()), MathUtils.asinFast(var34.getZ()), MathUtils.atan2Fast(-var11.getX(), var11.getZ())
            };
         } else {
            throw new CardanEulerSingularityException(true);
         }
      } else if (var1 == RotationOrder.ZYX) {
         Vector3D var10 = this.applyTo(Vector3D.PLUS_K);
         Vector3D var33 = this.applyInverseTo(Vector3D.PLUS_I);
         if (!(var33.getZ() < -0.9999999999) && !(var33.getZ() > 0.9999999999)) {
            return new double[]{
               MathUtils.atan2Fast(var33.getY(), var33.getX()), -MathUtils.asinFast(var33.getZ()), MathUtils.atan2Fast(var10.getY(), var10.getZ())
            };
         } else {
            throw new CardanEulerSingularityException(true);
         }
      } else if (var1 == RotationOrder.XYX) {
         Vector3D var9 = this.applyTo(Vector3D.PLUS_I);
         Vector3D var32 = this.applyInverseTo(Vector3D.PLUS_I);
         if (!(var32.getX() < -0.9999999999) && !(var32.getX() > 0.9999999999)) {
            return new double[]{MathUtils.atan2Fast(var32.getY(), -var32.getZ()), FastMath.acos(var32.getX()), MathUtils.atan2Fast(var9.getY(), var9.getZ())};
         } else {
            throw new CardanEulerSingularityException(false);
         }
      } else if (var1 == RotationOrder.XZX) {
         Vector3D var8 = this.applyTo(Vector3D.PLUS_I);
         Vector3D var31 = this.applyInverseTo(Vector3D.PLUS_I);
         if (!(var31.getX() < -0.9999999999) && !(var31.getX() > 0.9999999999)) {
            return new double[]{MathUtils.atan2Fast(var31.getZ(), var31.getY()), FastMath.acos(var31.getX()), MathUtils.atan2Fast(var8.getZ(), -var8.getY())};
         } else {
            throw new CardanEulerSingularityException(false);
         }
      } else if (var1 == RotationOrder.YXY) {
         Vector3D var7 = this.applyTo(Vector3D.PLUS_J);
         Vector3D var30 = this.applyInverseTo(Vector3D.PLUS_J);
         if (!(var30.getY() < -0.9999999999) && !(var30.getY() > 0.9999999999)) {
            return new double[]{MathUtils.atan2Fast(var30.getX(), var30.getZ()), FastMath.acos(var30.getY()), MathUtils.atan2Fast(var7.getX(), -var7.getZ())};
         } else {
            throw new CardanEulerSingularityException(false);
         }
      } else if (var1 == RotationOrder.YZY) {
         Vector3D var6 = this.applyTo(Vector3D.PLUS_J);
         Vector3D var29 = this.applyInverseTo(Vector3D.PLUS_J);
         if (!(var29.getY() < -0.9999999999) && !(var29.getY() > 0.9999999999)) {
            return new double[]{MathUtils.atan2Fast(var29.getZ(), -var29.getX()), FastMath.acos(var29.getY()), MathUtils.atan2Fast(var6.getZ(), var6.getX())};
         } else {
            throw new CardanEulerSingularityException(false);
         }
      } else if (var1 == RotationOrder.ZXZ) {
         Vector3D var5 = this.applyTo(Vector3D.PLUS_K);
         Vector3D var28 = this.applyInverseTo(Vector3D.PLUS_K);
         if (!(var28.getZ() < -0.9999999999) && !(var28.getZ() > 0.9999999999)) {
            return new double[]{MathUtils.atan2Fast(var28.getX(), -var28.getY()), FastMath.acos(var28.getZ()), MathUtils.atan2Fast(var5.getX(), var5.getY())};
         } else {
            throw new CardanEulerSingularityException(false);
         }
      } else {
         Vector3D var3 = this.applyTo(Vector3D.PLUS_K);
         Vector3D var4 = this.applyInverseTo(Vector3D.PLUS_K);
         if (!(var4.getZ() < -0.9999999999) && !(var4.getZ() > 0.9999999999)) {
            return new double[]{MathUtils.atan2Fast(var4.getY(), var4.getX()), FastMath.acos(var4.getZ()), MathUtils.atan2Fast(var3.getY(), -var3.getX())};
         } else {
            throw new CardanEulerSingularityException(false);
         }
      }
   }

   public double[][] getMatrix() {
      double var1 = this.q0 * this.q0;
      double var3 = this.q0 * this.q1;
      double var5 = this.q0 * this.q2;
      double var7 = this.q0 * this.q3;
      double var9 = this.q1 * this.q1;
      double var11 = this.q1 * this.q2;
      double var13 = this.q1 * this.q3;
      double var15 = this.q2 * this.q2;
      double var17 = this.q2 * this.q3;
      double var19 = this.q3 * this.q3;
      double[][] var21 = new double[][]{new double[3], new double[3], new double[3]};
      var21[0][0] = 2.0 * (var1 + var9) - 1.0;
      var21[1][0] = 2.0 * (var11 - var7);
      var21[2][0] = 2.0 * (var13 + var5);
      var21[0][1] = 2.0 * (var11 + var7);
      var21[1][1] = 2.0 * (var1 + var15) - 1.0;
      var21[2][1] = 2.0 * (var17 - var3);
      var21[0][2] = 2.0 * (var13 - var5);
      var21[1][2] = 2.0 * (var17 + var3);
      var21[2][2] = 2.0 * (var1 + var19) - 1.0;
      return var21;
   }

   public Vector3D applyTo(Vector3D var1) {
      double var2 = var1.getX();
      double var4 = var1.getY();
      double var6 = var1.getZ();
      double var8 = this.q1 * var2 + this.q2 * var4 + this.q3 * var6;
      return new Vector3D(
         2.0 * (this.q0 * (var2 * this.q0 - (this.q2 * var6 - this.q3 * var4)) + var8 * this.q1) - var2,
         2.0 * (this.q0 * (var4 * this.q0 - (this.q3 * var2 - this.q1 * var6)) + var8 * this.q2) - var4,
         2.0 * (this.q0 * (var6 * this.q0 - (this.q1 * var4 - this.q2 * var2)) + var8 * this.q3) - var6
      );
   }

   public void applyToArray(double[] var1, double[] var2) {
      double var3 = var1[0];
      double var5 = var1[1];
      double var7 = var1[2];
      double var9 = this.q1 * var3 + this.q2 * var5 + this.q3 * var7;
      var2[0] = 2.0 * (this.q0 * (var3 * this.q0 - (this.q2 * var7 - this.q3 * var5)) + var9 * this.q1) - var3;
      var2[1] = 2.0 * (this.q0 * (var5 * this.q0 - (this.q3 * var3 - this.q1 * var7)) + var9 * this.q2) - var5;
      var2[2] = 2.0 * (this.q0 * (var7 * this.q0 - (this.q1 * var5 - this.q2 * var3)) + var9 * this.q3) - var7;
   }

   public Vector3D applyInverseTo(Vector3D var1) {
      double var2 = var1.getX();
      double var4 = var1.getY();
      double var6 = var1.getZ();
      double var8 = this.q1 * var2 + this.q2 * var4 + this.q3 * var6;
      double var10 = -this.q0;
      return new Vector3D(
         2.0 * (var10 * (var2 * var10 - (this.q2 * var6 - this.q3 * var4)) + var8 * this.q1) - var2,
         2.0 * (var10 * (var4 * var10 - (this.q3 * var2 - this.q1 * var6)) + var8 * this.q2) - var4,
         2.0 * (var10 * (var6 * var10 - (this.q1 * var4 - this.q2 * var2)) + var8 * this.q3) - var6
      );
   }

   public void applyInverseToArray(double[] var1, double[] var2) {
      double var3 = var1[0];
      double var5 = var1[1];
      double var7 = var1[2];
      double var9 = this.q1 * var3 + this.q2 * var5 + this.q3 * var7;
      double var11 = -this.q0;
      var2[0] = 2.0 * (var11 * (var3 * var11 - (this.q2 * var7 - this.q3 * var5)) + var9 * this.q1) - var3;
      var2[1] = 2.0 * (var11 * (var5 * var11 - (this.q3 * var3 - this.q1 * var7)) + var9 * this.q2) - var5;
      var2[2] = 2.0 * (var11 * (var7 * var11 - (this.q1 * var5 - this.q2 * var3)) + var9 * this.q3) - var7;
   }

   public Rotation3D compose(Rotation3D var1) {
      return this.composeWith(var1, RotationConvention.VECTOR_OPERATOR);
   }

   public Rotation3D composeWith(Rotation3D var1, RotationConvention var2) {
      return var2 == RotationConvention.VECTOR_OPERATOR ? this.composeInternal(var1) : var1.composeInternal(this);
   }

   private Rotation3D composeInternal(Rotation3D var1) {
      return new Rotation3D(
         var1.q0 * this.q0 - (var1.q1 * this.q1 + var1.q2 * this.q2 + var1.q3 * this.q3),
         var1.q1 * this.q0 + var1.q0 * this.q1 + (var1.q2 * this.q3 - var1.q3 * this.q2),
         var1.q2 * this.q0 + var1.q0 * this.q2 + (var1.q3 * this.q1 - var1.q1 * this.q3),
         var1.q3 * this.q0 + var1.q0 * this.q3 + (var1.q1 * this.q2 - var1.q2 * this.q1),
         false
      );
   }

   public void setFromRotation(Rotation3D var1) {
      this.composeInPlace(var1, RotationConvention.VECTOR_OPERATOR);
   }

   public void composeInPlace(Rotation3D var1, RotationConvention var2) {
      if (var2 == RotationConvention.VECTOR_OPERATOR) {
         this.setQuaternionFrom(var1);
      } else {
         var1.setQuaternionFrom(this);
      }
   }

   private void setQuaternionFrom(Rotation3D var1) {
      double var2 = var1.q0 * this.q0 - (var1.q1 * this.q1 + var1.q2 * this.q2 + var1.q3 * this.q3);
      double var4 = var1.q1 * this.q0 + var1.q0 * this.q1 + (var1.q2 * this.q3 - var1.q3 * this.q2);
      double var6 = var1.q2 * this.q0 + var1.q0 * this.q2 + (var1.q3 * this.q1 - var1.q1 * this.q3);
      double var8 = var1.q3 * this.q0 + var1.q0 * this.q3 + (var1.q1 * this.q2 - var1.q2 * this.q1);
      var1.q0 = var2;
      var1.q1 = var4;
      var1.q2 = var6;
      var1.q3 = var8;
   }

   public Rotation3D composeInverse(Rotation3D var1) {
      return this.composeInverseWith(var1, RotationConvention.VECTOR_OPERATOR);
   }

   public Rotation3D composeInverseWith(Rotation3D var1, RotationConvention var2) {
      return var2 == RotationConvention.VECTOR_OPERATOR ? this.composeInverseInternal(var1) : var1.composeInternal(this.revert());
   }

   private Rotation3D composeInverseInternal(Rotation3D var1) {
      return new Rotation3D(
         -var1.q0 * this.q0 - (var1.q1 * this.q1 + var1.q2 * this.q2 + var1.q3 * this.q3),
         -var1.q1 * this.q0 + var1.q0 * this.q1 + (var1.q2 * this.q3 - var1.q3 * this.q2),
         -var1.q2 * this.q0 + var1.q0 * this.q2 + (var1.q3 * this.q1 - var1.q1 * this.q3),
         -var1.q3 * this.q0 + var1.q0 * this.q3 + (var1.q1 * this.q2 - var1.q2 * this.q1),
         false
      );
   }

   private double[][] orthogonalizeMatrix(double[][] var1, double var2) {
      double[] var4 = var1[0];
      double[] var5 = var1[1];
      double[] var6 = var1[2];
      double var7 = var4[0];
      double var9 = var4[1];
      double var11 = var4[2];
      double var13 = var5[0];
      double var15 = var5[1];
      double var17 = var5[2];
      double var19 = var6[0];
      double var21 = var6[1];
      double var23 = var6[2];
      double var25 = 0.0;
      double[][] var27 = new double[3][3];
      double[] var28 = var27[0];
      double[] var29 = var27[1];
      double[] var30 = var27[2];
      int var31 = 0;

      while (++var31 < 11) {
         double var32 = var4[0] * var7 + var5[0] * var13 + var6[0] * var19;
         double var34 = var4[1] * var7 + var5[1] * var13 + var6[1] * var19;
         double var36 = var4[2] * var7 + var5[2] * var13 + var6[2] * var19;
         double var38 = var4[0] * var9 + var5[0] * var15 + var6[0] * var21;
         double var40 = var4[1] * var9 + var5[1] * var15 + var6[1] * var21;
         double var42 = var4[2] * var9 + var5[2] * var15 + var6[2] * var21;
         double var44 = var4[0] * var11 + var5[0] * var17 + var6[0] * var23;
         double var46 = var4[1] * var11 + var5[1] * var17 + var6[1] * var23;
         double var48 = var4[2] * var11 + var5[2] * var17 + var6[2] * var23;
         var28[0] = var7 - 0.5 * (var7 * var32 + var9 * var34 + var11 * var36 - var4[0]);
         var28[1] = var9 - 0.5 * (var7 * var38 + var9 * var40 + var11 * var42 - var4[1]);
         var28[2] = var11 - 0.5 * (var7 * var44 + var9 * var46 + var11 * var48 - var4[2]);
         var29[0] = var13 - 0.5 * (var13 * var32 + var15 * var34 + var17 * var36 - var5[0]);
         var29[1] = var15 - 0.5 * (var13 * var38 + var15 * var40 + var17 * var42 - var5[1]);
         var29[2] = var17 - 0.5 * (var13 * var44 + var15 * var46 + var17 * var48 - var5[2]);
         var30[0] = var19 - 0.5 * (var19 * var32 + var21 * var34 + var23 * var36 - var6[0]);
         var30[1] = var21 - 0.5 * (var19 * var38 + var21 * var40 + var23 * var42 - var6[1]);
         var30[2] = var23 - 0.5 * (var19 * var44 + var21 * var46 + var23 * var48 - var6[2]);
         double var50 = var28[0] - var4[0];
         double var52 = var28[1] - var4[1];
         double var54 = var28[2] - var4[2];
         double var56 = var29[0] - var5[0];
         double var58 = var29[1] - var5[1];
         double var60 = var29[2] - var5[2];
         double var62 = var30[0] - var6[0];
         double var64 = var30[1] - var6[1];
         double var66 = var30[2] - var6[2];
         double var68 = var50 * var50
            + var52 * var52
            + var54 * var54
            + var56 * var56
            + var58 * var58
            + var60 * var60
            + var62 * var62
            + var64 * var64
            + var66 * var66;
         if (FastMath.abs(var68 - var25) <= var2) {
            return var27;
         }

         var7 = var28[0];
         var9 = var28[1];
         var11 = var28[2];
         var13 = var29[0];
         var15 = var29[1];
         var17 = var29[2];
         var19 = var30[0];
         var21 = var30[1];
         var23 = var30[2];
         var25 = var68;
      }

      throw new NotARotationMatrixException(LocalizedFormats.UNABLE_TO_ORTHOGONOLIZE_MATRIX, new Object[]{var31 - 1});
   }

   public static double distance(Rotation3D var0, Rotation3D var1) {
      return var0.composeInverseInternal(var1).getAngle();
   }

   public Rotation3D copy() {
      return new Rotation3D(this.q0, this.q1, this.q2, this.q3, false);
   }

   public void assignFromCopy(Rotation3D var1) {
      if (this != var1) {
         this.q0 = var1.q0;
         this.q1 = var1.q1;
         this.q2 = var1.q2;
         this.q3 = var1.q3;
      } else {
         this.q0 = 1.0;
         this.q1 = 0.0;
         this.q2 = 0.0;
         this.q3 = 0.0;
      }
   }
}
