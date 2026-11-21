package dev.lone.itemsadder.util;

import math.fast.SpeedyMath;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.bukkit.Location;

public final class MathUtils {
   static final float PI_F = (float) Math.PI;
   static final float HALF_PI_F = (float) (Math.PI / 2);
   static final float NEG_HALF_PI_F = (float) (-Math.PI / 2);
   private static final int INT_ABS_MASK = Integer.MAX_VALUE;
   private static final long LONG_ABS_MASK = Long.MAX_VALUE;

   public static double atan2Fast(double var0, double var2) {
      if (var2 == 0.0) {
         if (var0 > 0.0) {
            return (float) (Math.PI / 2);
         } else {
            return var0 == 0.0 ? 0.0 : (float) (-Math.PI / 2);
         }
      } else {
         double var6 = var0 / var2;
         if (Math.abs(var6) < 1.0) {
            double var8 = var6 / (1.0 + 0.28F * var6 * var6);
            if (var2 < 0.0) {
               return var0 < 0.0 ? var8 - (float) Math.PI : var8 + (float) Math.PI;
            } else {
               return var8;
            }
         } else {
            double var4 = (float) (Math.PI / 2) - var6 / (var6 * var6 + 0.28F);
            return var0 < 0.0 ? var4 - (float) Math.PI : var4;
         }
      }
   }

   public static double asinFast(double var0) {
      return SpeedyMath.asin(var0);
   }

   public static float addAnglesWrapped(float var0, float var1) {
      double var2 = var0 + var1;
      if (var2 > (float) Math.PI) {
         var2 -= (float) (Math.PI * 2);
      }

      if (var2 < (float) -Math.PI) {
         var2 += (float) (Math.PI * 2);
      }

      return (float)var2;
   }

   public static double angleDifference(double var0, double var2) {
      if (var2 > var0) {
         return var2 - var0 > (float) Math.PI ? (float)(var0 + (float) (Math.PI * 2) - var2) : var2 - var0;
      } else {
         return var0 - var2 > (float) Math.PI ? (float)(var2 + (float) (Math.PI * 2) - var0) : var0 - var2;
      }
   }

   public static float absFloat(float var0) {
      return Float.intBitsToFloat(2147483647 & Float.floatToRawIntBits(var0));
   }

   public static double absDouble(double var0) {
      return Double.longBitsToDouble(Long.MAX_VALUE & Double.doubleToRawLongBits(var0));
   }

   public static Location lookAt(Location var0, Location var1) {
      Location var2 = var0.clone();
      double var3 = var1.getX() - var2.getX();
      double var5 = var1.getY() - var2.getY();
      double var7 = var1.getZ() - var2.getZ();
      double var9 = Math.sqrt(var3 * var3 + var7 * var7);
      float var11 = (float)Math.toDegrees(Math.atan2(-var3, var7));
      float var12 = (float)Math.toDegrees(-Math.atan2(var5, var9));
      var2.setYaw(var11);
      var2.setPitch(var12);
      return var2;
   }

   public static double slerpAngle(double var0, double var2, double var4) {
      if (var4 <= 0.0) {
         return var0;
      }

      if (var4 >= 1.0) {
         return var2;
      }

      double var6 = angleDifference(var0, var2);
      if (var6 < 0.0) {
         var6 += Math.PI * 2;
      }

      double var8 = Math.sin(var6 / 2.0);
      if (var8 == 0.0) {
         return var0;
      }

      double var10 = Math.sin((1.0 - var4) * var6 / 2.0) / var8;
      double var12 = Math.sin(var4 * var6 / 2.0) / var8;
      return var0 * var10 + var2 * var12;
   }

   public static double normalizeAngle(double var0) {
      double var2 = (float)(var0 % (Math.PI * 2));
      if (var2 >= Math.PI) {
         var2 -= Math.PI * 2;
      }

      if (var2 < -Math.PI) {
         var2 += Math.PI * 2;
      }

      return var2;
   }

   public static double lerpAngle(double var0, double var2, double var4) {
      double var6 = normalizeAngle(var2 - var0);
      return normalizeAngle(var0 + var6 * var4);
   }

   public float angleDegrees(double var1, double var3, double var5, double var7) {
      float var9 = (float)Math.toDegrees(Math.atan2(var7 - var3, var5 - var1));
      if (var9 < 0.0F) {
         var9 += 360.0F;
      }

      return var9;
   }

   public static double lerp(double var0, double var2, double var4) {
      return var0 + var4 * (var2 - var0);
   }

   public static float clamp(float var0, float var1, float var2) {
      return Math.max(var1, Math.min(var2, var0));
   }

   public static void rotatePoint(double[] var0, Rotation3D var1, double var2, double var4, double var6) {
      double var8 = var0[0] - var2;
      double var10 = var0[1] - var4;
      double var12 = var0[2] - var6;
      Vector3D var14 = new Vector3D(var8, var10, var12);
      Vector3D var15 = var1.applyTo(var14);
      var0[0] = var15.getX() + var2;
      var0[1] = var15.getY() + var4;
      var0[2] = var15.getZ() + var6;
   }

   public static void rotateXThenY(double[] var0, double var1, double var3, double var5, double var7, double var9) {
      if (var7 != 0.0) {
         rotateAroundX(var0, var3, var5, var7);
      }

      if (var9 != 0.0) {
         rotateAroundY(var0, var1, var5, var9);
      }
   }

   public static void rotateYThenX(double[] var0, double var1, double var3, double var5, double var7, double var9) {
      if (var9 != 0.0) {
         rotateAroundY(var0, var1, var5, var9);
      }

      if (var7 != 0.0) {
         rotateAroundX(var0, var3, var5, var7);
      }
   }

   public static void rotateAroundY(double[] var0, double var1, double var3, double var5) {
      double var7 = Math.sin(var5);
      double var9 = Math.cos(var5);
      var0[0] -= var1;
      var0[2] -= var3;
      double var11 = var0[0] * var9 - var0[2] * var7;
      double var13 = var0[0] * var7 + var0[2] * var9;
      var0[0] = var11 + var1;
      var0[2] = var13 + var3;
   }

   public static void rotateAroundX(double[] var0, double var1, double var3, double var5) {
      double var7 = Math.sin(var5);
      double var9 = Math.cos(var5);
      var0[1] -= var1;
      var0[2] -= var3;
      double var11 = var0[1] * var9 - var0[2] * var7;
      double var13 = var0[1] * var7 + var0[2] * var9;
      var0[1] = var11 + var1;
      var0[2] = var13 + var3;
   }

   public static boolean approxEquals(double var0, double var2, double var4) {
      return Math.abs(var0 - var2) <= var4;
   }

   public static boolean isOutsideEpsilon3D(double var0, double var2, double var4, double var6, double var8, double var10, double var12) {
      return !approxEquals(var0, var6, var12) || !approxEquals(var2, var8, var12) || !approxEquals(var4, var10, var12);
   }
}
