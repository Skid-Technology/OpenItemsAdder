package dev.lone.itemsadder.entity;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class TransformDecomposition {
   private final Matrix4f yf;
   private final Vector3f yg;
   private final Quaternionf yh;
   private final Vector3f yi;
   private final Quaternionf yj;
   private static final TransformKeyframe[] yk = getKeyframes();

   private TransformDecomposition(Matrix4fc var1, Vector3f var2, Quaternionf var3, Vector3f var4, Quaternionf var5) {
      this.yf = new Matrix4f(var1);
      this.yg = var2;
      this.yh = var3;
      this.yi = var4;
      this.yj = var5;
   }

   public static TransformDecomposition decompose(Matrix4fc var0) {
      if (!var0.isFinite()) {
         throw new IllegalArgumentException("Non-finite affine matrix");
      }

      if (method8657(var0)) {
         return new TransformDecomposition(var0, new Vector3f(var0.m30(), var0.m31(), var0.m32()), new Quaternionf(), new Vector3f(), new Quaternionf());
      }

      TransformDecomposition var1 = method8658(var0);
      if (var1 != null) {
         return var1;
      }

      double[][] var2 = new double[][]{{var0.m00(), var0.m10(), var0.m20()}, {var0.m01(), var0.m11(), var0.m21()}, {var0.m02(), var0.m12(), var0.m22()}};
      SingularValueDecomposition var3 = new SingularValueDecomposition(MatrixUtils.createRealMatrix(var2));
      RealMatrix var4 = var3.getU();
      RealMatrix var5 = var3.getV();
      double[] var6 = var3.getSingularValues();
      double var7 = method8660(var4) < 0.0 ? -1.0 : 1.0;
      double var9 = method8660(var5) < 0.0 ? -1.0 : 1.0;
      if (var7 < 0.0) {
         method8661(var4, 2, -1.0);
      }

      if (var9 < 0.0) {
         method8661(var5, 2, -1.0);
      }

      var6[2] *= var7 * var9;
      Quaternionf var11 = method8662(var4, false);
      Quaternionf var12 = method8662(var5, true);
      normalize(var11);
      normalize(var12);
      Vector3f var13 = new Vector3f(var0.m30(), var0.m31(), var0.m32());
      Vector3f var14 = new Vector3f((float)var6[0], (float)var6[1], (float)var6[2]);
      if (var13.isFinite() && var11.isFinite() && var14.isFinite() && var12.isFinite()) {
         return new TransformDecomposition(var0, var13, var11, var14, var12);
      } else {
         throw new IllegalArgumentException("Non-finite affine decomposition");
      }
   }

   public static TransformDecomposition decomposeInto(Matrix4fc var0, TransformDecomposition var1) {
      TransformDecomposition var2 = decompose(var0);
      if (method8655(var2.yj) && method8655(var1.yj)) {
         return var2;
      }

      float var3 = Math.max(Math.abs(var2.yi.x), Math.max(Math.abs(var2.yi.y), Math.abs(var2.yi.z)));
      float var4 = var3 * 1.0E-6F;
      if (Math.abs(var2.yi.x - var2.yi.y) <= var4 && Math.abs(var2.yi.y - var2.yi.z) <= var4) {
         var2.yh.mul(var2.yj).mul(new Quaternionf(var1.yj).conjugate());
         var2.yj.set(var1.yj);
         return var2;
      }

      Quaternionf var5 = new Quaternionf();
      Quaternionf var6 = new Quaternionf();
      Quaternionf var7 = new Quaternionf();
      Quaternionf var8 = new Quaternionf();
      Quaternionf var9 = new Quaternionf();
      Quaternionf var10 = new Quaternionf();
      Quaternionf var11 = new Quaternionf();
      Vector3f var12 = new Vector3f();
      Vector3f var13 = new Vector3f();
      double var14 = Double.NEGATIVE_INFINITY;
      double var16 = Math.max(var3, Math.max(Math.abs(var1.yi.x), Math.max(Math.abs(var1.yi.y), Math.abs(var1.yi.z))));

      for (TransformKeyframe var21 : yk) {
         var7.set(var2.yh).mul(var21.yo);
         var8.set(var21.yp).conjugate().mul(var2.yj);
         var12.set(var2.yi.get(var21.yl), var2.yi.get(var21.ym), var2.yi.get(var21.yn)).mul(var21.yq);

         for (int var22 = 0; var22 < 3; var22++) {
            int var23 = (var22 + 1) % 3;
            int var24 = (var22 + 2) % 3;
            if (!(Math.abs(Math.abs(var12.get(var23)) - Math.abs(var12.get(var24))) > var4)) {
               var9.set(var22 == 0 ? 1.0F : 0.0F, var22 == 1 ? 1.0F : 0.0F, var22 == 2 ? 1.0F : 0.0F, 0.0F);
               float var25 = angleBetween(var7, var1.yh);
               float var26 = angleBetween(var8, var1.yj);
               float var27 = angleBetween(var7.mul(var9, var10), var1.yh);
               boolean var28 = Math.copySign(1.0F, var12.get(var23)) == Math.copySign(1.0F, var12.get(var24));
               float var29 = angleBetween((var28 ? var11.set(var9).conjugate() : var11.set(var9)).mul(var8), var1.yj);
               float var30 = (float)Math.atan2(2.0F * (var25 * var27 + var26 * var29), var25 * var25 + var26 * var26 - var27 * var27 - var29 * var29);
               var9.rotationAxis(var30, var22 == 0 ? 1.0F : 0.0F, var22 == 1 ? 1.0F : 0.0F, var22 == 2 ? 1.0F : 0.0F);
               var7.mul(var9);
               var8.premul(var28 ? var9.conjugate() : var9);
               break;
            }
         }

         float var32 = angleBetween(var7, var1.yh);
         float var33 = angleBetween(var8, var1.yj);
         double var34 = ((double)var12.x - var1.yi.x) / var16;
         double var35 = ((double)var12.y - var1.yi.y) / var16;
         double var36 = ((double)var12.z - var1.yi.z) / var16;
         double var37 = var32 * var32 + var33 * var33 - var34 * var34 - var35 * var35 - var36 * var36;
         if (var37 > var14) {
            var14 = var37;
            var5.set(var7);
            var6.set(var8);
            var13.set(var12);
         }
      }

      var2.yh.set(var5);
      var2.yj.set(var6);
      var2.yi.set(var13);
      return var2;
   }

   private static float angleBetween(Quaternionf var0, Quaternionf var1) {
      return var0.x * var1.x + var0.y * var1.y + var0.z * var1.z + var0.w * var1.w;
   }

   private static boolean method8655(Quaternionf var0) {
      return var0.x == 0.0F && var0.y == 0.0F && var0.z == 0.0F;
   }

   private static TransformKeyframe[] getKeyframes() {
      TransformKeyframe[] var0 = new TransformKeyframe[96];
      int var1 = 0;

      for (int[] var5 : new int[][]{{0, 1, 2}, {0, 2, 1}, {1, 0, 2}, {1, 2, 0}, {2, 0, 1}, {2, 1, 0}}) {
         int var6 = ((var5[0] > var5[1] ? 1 : 0) + (var5[0] > var5[2] ? 1 : 0) + (var5[1] > var5[2] ? 1 : 0)) % 2 == 0 ? 1 : -1;

         for (int var10 : new int[]{-1, 1}) {
            for (int var14 : new int[]{-1, 1}) {
               for (int var18 : new int[]{-1, 1}) {
                  for (int var22 : new int[]{-1, 1}) {
                     int var23 = var6 * var10 * var14;
                     int var24 = var6 * var18 * var22;
                     var0[var1++] = new TransformKeyframe(
                        var5[0],
                        var5[1],
                        var5[2],
                        method8656(var5, var10, var14, var23),
                        method8656(var5, var18, var22, var24),
                        new Vector3f(var10 * var18, var14 * var22, var23 * var24)
                     );
                  }
               }
            }
         }
      }

      return var0;
   }

   private static Quaternionf method8656(int[] var0, int var1, int var2, int var3) {
      return new Matrix3f(
            var0[0] == 0 ? var1 : 0.0F,
            var0[0] == 1 ? var1 : 0.0F,
            var0[0] == 2 ? var1 : 0.0F,
            var0[1] == 0 ? var2 : 0.0F,
            var0[1] == 1 ? var2 : 0.0F,
            var0[1] == 2 ? var2 : 0.0F,
            var0[2] == 0 ? var3 : 0.0F,
            var0[2] == 1 ? var3 : 0.0F,
            var0[2] == 2 ? var3 : 0.0F
         )
         .getNormalizedRotation(new Quaternionf());
   }

   public static boolean method8657(Matrix4fc var0) {
      return var0.m00() == 0.0F
         && var0.m01() == 0.0F
         && var0.m02() == 0.0F
         && var0.m10() == 0.0F
         && var0.m11() == 0.0F
         && var0.m12() == 0.0F
         && var0.m20() == 0.0F
         && var0.m21() == 0.0F
         && var0.m22() == 0.0F;
   }

   private static TransformDecomposition method8658(Matrix4fc var0) {
      Vector3f var1 = new Vector3f(var0.m00(), var0.m01(), var0.m02());
      Vector3f var2 = new Vector3f(var0.m10(), var0.m11(), var0.m12());
      Vector3f var3 = new Vector3f(var0.m20(), var0.m21(), var0.m22());
      float var4 = var1.length();
      float var5 = var2.length();
      float var6 = var3.length();
      if (Float.isFinite(var4) && Float.isFinite(var5) && Float.isFinite(var6) && !(var4 < 1.0E-7F) && !(var5 < 1.0E-7F) && !(var6 < 1.0E-7F)) {
         var1.div(var4);
         var2.div(var5);
         var3.div(var6);
         if (!(Math.abs(var1.dot(var2)) > 1.0E-5F) && !(Math.abs(var1.dot(var3)) > 1.0E-5F) && !(Math.abs(var2.dot(var3)) > 1.0E-5F)) {
            if (var1.cross(var2, new Vector3f()).dot(var3) < 0.0F) {
               var3.negate();
               var6 = -var6;
            }

            Quaternionf var7 = new Matrix3f(var1.x, var1.y, var1.z, var2.x, var2.y, var2.z, var3.x, var3.y, var3.z).getNormalizedRotation(new Quaternionf());
            normalize(var7);
            return new TransformDecomposition(var0, new Vector3f(var0.m30(), var0.m31(), var0.m32()), var7, new Vector3f(var4, var5, var6), new Quaternionf());
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public void applyTo(TransformBuilder var1) {
      var1.yr.set(this.yg);
      var1.ys.set(this.yh);
      var1.yt.set(this.yi);
      var1.yu.set(this.yj);
      var1.yw = this.yf;
   }

   public Vector3f getTranslation() {
      return this.yg;
   }

   public Quaternionf getRotation() {
      return this.yh;
   }

   public Vector3f getScale() {
      return this.yi;
   }

   public Quaternionf mA() {
      return this.yj;
   }

   private static double method8660(RealMatrix var0) {
      double var1 = var0.getEntry(0, 0);
      double var3 = var0.getEntry(0, 1);
      double var5 = var0.getEntry(0, 2);
      double var7 = var0.getEntry(1, 0);
      double var9 = var0.getEntry(1, 1);
      double var11 = var0.getEntry(1, 2);
      double var13 = var0.getEntry(2, 0);
      double var15 = var0.getEntry(2, 1);
      double var17 = var0.getEntry(2, 2);
      return var1 * (var9 * var17 - var11 * var15) - var3 * (var7 * var17 - var11 * var13) + var5 * (var7 * var15 - var9 * var13);
   }

   private static void method8661(RealMatrix var0, int var1, double var2) {
      for (int var4 = 0; var4 < 3; var4++) {
         var0.multiplyEntry(var4, var1, var2);
      }
   }

   private static Quaternionf method8662(RealMatrix var0, boolean var1) {
      float[][] var2 = new float[3][3];

      for (int var3 = 0; var3 < 3; var3++) {
         for (int var4 = 0; var4 < 3; var4++) {
            var2[var3][var4] = (float)var0.getEntry(var1 ? var4 : var3, var1 ? var3 : var4);
         }
      }

      return new Matrix3f(var2[0][0], var2[1][0], var2[2][0], var2[0][1], var2[1][1], var2[2][1], var2[0][2], var2[1][2], var2[2][2])
         .getNormalizedRotation(new Quaternionf());
   }

   private static void normalize(Quaternionf var0) {
      if (var0.w < 0.0F) {
         var0.set(-var0.x, -var0.y, -var0.z, -var0.w);
      }
   }
}
