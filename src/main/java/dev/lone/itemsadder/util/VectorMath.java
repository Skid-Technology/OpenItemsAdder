package dev.lone.itemsadder.util;

import org.bukkit.util.Vector;

public class VectorMath {
   public static double cosRad(double var0) {
      return Math.cos(var0);
   }

   public static double sinRad(double var0) {
      return Math.sin(var0);
   }

   public static double tanRad(double var0) {
      return Math.tan(var0);
   }

   public static double acosRad(double var0) {
      return Math.acos(var0);
   }

   public static double asinRad(double var0) {
      return Math.asin(var0);
   }

   public static double atanRad(double var0) {
      return Math.atan(var0);
   }

   public static double toRadians(double var0) {
      return Math.toRadians(var0);
   }

   public static double toDegrees(double var0) {
      return Math.toDegrees(var0);
   }

   public static double atanAbsRatio(double var0, double var2) {
      if (var0 < 0.0) {
         var0 *= -1.0;
      }

      if (var2 < 0.0) {
         var2 *= -1.0;
      }

      return var0 != 0.0 && var2 != 0.0 ? atanRad(var2 / var0) : 0.0;
   }

   public static Vector rotateByYawPitch(Vector var0, double var1, double var3) {
      var1 = toRadians(var1);
      var3 = toRadians(var3);
      var0 = rotateAroundX(var0, var3);
      return rotateAroundY(var0, -var1);
   }

   public static Vector rotateAroundX(Vector var0, double var1) {
      double var3 = cosRad(var1) * var0.getY() - sinRad(var1) * var0.getZ();
      double var5 = sinRad(var1) * var0.getY() + cosRad(var1) * var0.getZ();
      return var0.setY(var3).setZ(var5);
   }

   public static Vector rotateAroundY(Vector var0, double var1) {
      double var3 = cosRad(var1) * var0.getX() + sinRad(var1) * var0.getZ();
      double var5 = -sinRad(var1) * var0.getX() + cosRad(var1) * var0.getZ();
      return var0.setX(var3).setZ(var5);
   }

   public static Vector rotateAroundZ(Vector var0, double var1) {
      double var3 = cosRad(var1) * var0.getX() - sinRad(var1) * var0.getY();
      double var5 = sinRad(var1) * var0.getX() + cosRad(var1) * var0.getY();
      return var0.setX(var3).setY(var5);
   }
}
