package dev.lone.itemsadder.entity;

import java.util.Objects;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public final class TransformBuilder {
   private final Vector3f yr = new Vector3f();
   private final Quaternionf ys = new Quaternionf();
   private final Vector3f yt = new Vector3f(1.0F);
   private final Quaternionf yu = new Quaternionf();
   private final TransformScratch yv;
   private Matrix4fc yw;

   public TransformBuilder() {
      this(new TransformScratch());
   }

   public TransformBuilder(TransformScratch var1) {
      this.yv = Objects.requireNonNull(var1, "workspace");
   }

   public TransformBuilder copyOf(TransformBuilder var1) {
      this.yr.set(var1.yr);
      this.ys.set(var1.ys);
      this.yt.set(var1.yt);
      this.yu.set(var1.yu);
      this.yw = var1.yw;
      return this;
   }

   public TransformBuilder method8665(TransformBuilder var1, TransformDecomposition var2, float var3) {
      if (var3 <= 0.0F) {
         return this.copyOf(var1);
      }

      if (var3 >= 1.0F) {
         var2.applyTo(this);
         return this;
      }

      if (var3 > 0.0F && var1.method8670(var2)) {
         return this.copyOf(var1);
      }

      this.yr.set(var1.yr).lerp(var2.yg, var3);
      this.ys.set(var1.ys).slerp(var2.yh, var3).normalize();
      this.yt.set(var1.yt).lerp(var2.yi, var3);
      this.yu.set(var1.yu).slerp(var2.yj, var3).normalize();
      this.yw = null;
      return this;
   }

   public TransformBuilder method8666(TransformBuilder var1, TransformBuilder var2, float var3) {
      if (var3 <= 0.0F) {
         return this.copyOf(var1);
      }

      if (var3 >= 1.0F) {
         return this.copyOf(var2);
      }

      if (var3 > 0.0F && var1.method8669(var2)) {
         return this.copyOf(var1);
      }

      this.yr.set(var1.yr).lerp(var2.yr, var3);
      this.ys.set(var1.ys).slerp(var2.ys, var3).normalize();
      this.yt.set(var1.yt).lerp(var2.yt, var3);
      this.yu.set(var1.yu).slerp(var2.yu, var3).normalize();
      this.yw = null;
      return this;
   }

   public TransformBuilder method8667(TransformBuilder var1, int var2, float var3) {
      if (var3 <= 0.0F || var2 == 0) {
         return this;
      }

      if (var3 <= 1.0F && this.method8669(var1)) {
         return this;
      }

      if (var3 == 1.0F && this.isIdentity() && var1.isIdentity()) {
         return var2 == 7 ? this.copyOf(var1) : this.method8671(var1.yr, var1.ys, var1.yt, var1.yu, var2);
      }

      if ((var2 & 1) != 0) {
         this.yr.lerp(var1.yr, var3);
      }

      if ((var2 & 2) != 0) {
         this.ys.slerp(var1.ys, var3).normalize();
         this.yu.slerp(var1.yu, var3).normalize();
      }

      if ((var2 & 4) != 0) {
         this.yt.lerp(var1.yt, var3);
      }

      this.yw = null;
      return this;
   }

   public TransformBuilder method8668(TransformDecomposition var1, int var2, float var3) {
      if (var3 <= 0.0F || var2 == 0) {
         return this;
      }

      if (var3 <= 1.0F && this.method8670(var1)) {
         return this;
      }

      if (var3 == 1.0F && this.isIdentity() && var1.yg.isFinite() && var1.yh.isFinite() && var1.yi.isFinite() && var1.yj.isFinite()) {
         if (var2 == 7) {
            var1.applyTo(this);
            return this;
         } else {
            return this.method8671(var1.yg, var1.yh, var1.yi, var1.yj, var2);
         }
      } else {
         if ((var2 & 1) != 0) {
            this.yr.lerp(var1.yg, var3);
         }

         if ((var2 & 2) != 0) {
            this.ys.slerp(var1.yh, var3).normalize();
            this.yu.slerp(var1.yj, var3).normalize();
         }

         if ((var2 & 4) != 0) {
            this.yt.lerp(var1.yi, var3);
         }

         this.yw = null;
         return this;
      }
   }

   private boolean method8669(TransformBuilder var1) {
      return this.yw != null
         && this.yw == var1.yw
         && this.isIdentity()
         && this.yr.equals(var1.yr)
         && this.ys.equals(var1.ys)
         && this.yt.equals(var1.yt)
         && this.yu.equals(var1.yu);
   }

   private boolean method8670(TransformDecomposition var1) {
      return this.yw == var1.yf
         && this.isIdentity()
         && this.yr.equals(var1.yg)
         && this.ys.equals(var1.yh)
         && this.yt.equals(var1.yi)
         && this.yu.equals(var1.yj);
   }

   private TransformBuilder method8671(Vector3f var1, Quaternionf var2, Vector3f var3, Quaternionf var4, int var5) {
      if ((var5 & 1) != 0) {
         this.yr.set(var1);
      }

      if ((var5 & 2) != 0) {
         this.ys.set(var2).normalize();
         this.yu.set(var4).normalize();
      }

      if ((var5 & 4) != 0) {
         this.yt.set(var3);
      }

      this.yw = null;
      return this;
   }

   public TransformBuilder method8672(TransformDecomposition var1, TransformBuilder var2, int var3, float var4) {
      if (!(var4 <= 0.0F) && var3 != 0) {
         if ((var3 & 1) != 0) {
            this.yr.add(this.yv.yz.set(var2.yr).sub(var1.yg).mul(var4));
         }

         if ((var3 & 2) != 0) {
            this.yv.yx.set(var1.yh).conjugate().mul(var2.ys).normalize();
            this.ys.mul(this.yv.field113.identity().slerp(this.yv.yx, var4)).normalize();
            this.yv.yx.set(var1.yj).conjugate().mul(var2.yu).normalize();
            this.yu.mul(this.yv.field113.identity().slerp(this.yv.yx, var4)).normalize();
         }

         if ((var3 & 4) != 0) {
            this.yv
               .yz
               .set(
                  1.0F + (method8684(var2.yt.x, var1.yi.x) - 1.0F) * var4,
                  1.0F + (method8684(var2.yt.y, var1.yi.y) - 1.0F) * var4,
                  1.0F + (method8684(var2.yt.z, var1.yi.z) - 1.0F) * var4
               );
            this.yt.mul(this.yv.yz);
         }

         this.yw = null;
         return this;
      } else {
         return this;
      }
   }

   public TransformBuilder method8673(TransformDecomposition var1, TransformBuilder[] var2, int[] var3, float[] var4) {
      var1.applyTo(this);
      this.method8674(var1.yh, this.yv.yA);
      this.yv.yB.set(this.yv.yA);
      this.method8674(var1.yj, this.yv.yD);
      this.yv.yE.set(this.yv.yD);

      for (int var5 = 0; var5 < var2.length; var5++) {
         float var6 = var4[var5];
         int var7 = var3[var5];
         if (!(var6 <= 0.0F) && var7 != 0) {
            TransformBuilder var8 = var2[var5];
            if ((var7 & 1) != 0) {
               this.yr.add(this.yv.yz.set(var8.yr).sub(var1.yg).mul(var6));
            }

            if ((var7 & 2) != 0) {
               this.method8674(var8.ys, this.yv.yC);
               this.yv
                  .yA
                  .add(
                     method8683(this.yv.yC.x - this.yv.yB.x) * var6,
                     method8683(this.yv.yC.y - this.yv.yB.y) * var6,
                     method8683(this.yv.yC.z - this.yv.yB.z) * var6
                  );
               this.method8674(var8.yu, this.yv.yC);
               this.yv
                  .yD
                  .add(
                     method8683(this.yv.yC.x - this.yv.yE.x) * var6,
                     method8683(this.yv.yC.y - this.yv.yE.y) * var6,
                     method8683(this.yv.yC.z - this.yv.yE.z) * var6
                  );
            }

            if ((var7 & 4) != 0) {
               this.yt
                  .mul(
                     1.0F + (method8684(var8.yt.x, var1.yi.x) - 1.0F) * var6,
                     1.0F + (method8684(var8.yt.y, var1.yi.y) - 1.0F) * var6,
                     1.0F + (method8684(var8.yt.z, var1.yi.z) - 1.0F) * var6
                  );
            }
         }
      }

      this.ys.rotationZYX(this.yv.yA.z, this.yv.yA.y, this.yv.yA.x).normalize();
      this.yu.rotationZYX(this.yv.yD.z, this.yv.yD.y, this.yv.yD.x).normalize();
      this.yw = null;
      return this;
   }

   private void method8674(Quaternionfc var1, Vector3f var2) {
      var1.get(this.yv.yF);
      float var3 = this.yv.yF.m02();
      var2.y = (float)Math.asin(-Math.max(-1.0F, Math.min(1.0F, var3)));
      if (Math.abs(var3) < 0.9999999F) {
         var2.x = (float)Math.atan2(this.yv.yF.m12(), this.yv.yF.m22());
         var2.z = (float)Math.atan2(this.yv.yF.m01(), this.yv.yF.m00());
      } else {
         var2.x = 0.0F;
         var2.z = (float)Math.atan2(-this.yv.yF.m10(), this.yv.yF.m11());
      }
   }

   public TransformBuilder method8675(float var1, float var2, float var3) {
      if (var1 == 0.0F && var2 == 0.0F && var3 == 0.0F) {
         return this;
      }

      this.ys.mul(this.yv.yx.rotationZYX(var3, var2, var1)).normalize();
      this.yw = null;
      return this;
   }

   public TransformBuilder method8676(float var1, float var2, float var3) {
      if (var1 == 0.0F && var2 == 0.0F && var3 == 0.0F) {
         return this;
      }

      this.method8674(this.ys, this.yv.yC);
      this.ys.rotationZYX(this.yv.yC.z + var3, this.yv.yC.y + var2, this.yv.yC.x + var1).normalize();
      this.yw = null;
      return this;
   }

   public Matrix4f method8677(Matrix4f var1, float var2, float var3, float var4) {
      this.method8674(this.ys, this.yv.yC);
      this.yv.yx.rotationZYX(this.yv.yC.z + var4, this.yv.yC.y + var3, this.yv.yC.x + var2).normalize();
      return var1.translation(this.yr).rotate(this.yv.yx).scale(this.yt).rotate(this.yu);
   }

   public Matrix4f method8678(Matrix4f var1, TransformDecomposition var2, Quaternionfc var3, float var4, float var5, float var6) {
      this.method8674(this.ys, this.yv.yC);
      this.yv.yx.rotationZYX(this.yv.yC.z + var6, this.yv.yC.y + var5, this.yv.yC.x + var4).normalize();
      this.method8681(var2, var3);
      return var1.translation(this.yr).rotate(this.yv.yx).scale(this.yt).rotate(this.yu);
   }

   public TransformBuilder method8679(TransformDecomposition var1, Quaternionfc var2, float var3, float var4, float var5) {
      this.method8674(this.ys, this.yv.yC);
      this.yv.yx.rotationZYX(this.yv.yC.z + var5, this.yv.yC.y + var4, this.yv.yC.x + var3).normalize();
      this.method8681(var1, var2);
      this.ys.set(this.yv.yx);
      this.yw = null;
      return this;
   }

   public TransformBuilder method8680(float var1) {
      if (var1 == 0.0F) {
         return this;
      }

      this.ys.premul(this.yv.yx.rotationZ(var1)).normalize();
      this.yw = null;
      return this;
   }

   private void method8681(TransformDecomposition var1, Quaternionfc var2) {
      this.yv.yx.mul(this.yv.field113.set(var1.yh).conjugate());
      this.yv.yx.premul(var2).mul(this.yv.field113.set(var2).conjugate()).mul(var1.yh).normalize();
   }

   public TransformBuilder method8682(Vector3fc var1, Quaternionfc var2) {
      this.yr.set(var1);
      this.ys.premul(var2).normalize();
      this.yw = null;
      return this;
   }

   private static float method8683(float var0) {
      var0 %= (float) (Math.PI * 2);
      if (var0 >= Math.PI) {
         var0 -= (float) (Math.PI * 2);
      }

      if (var0 < -Math.PI) {
         var0 += (float) (Math.PI * 2);
      }

      return var0;
   }

   private static float method8684(float var0, float var1) {
      return Math.abs(var1) < 1.0E-7F ? 1.0F : var0 / var1;
   }

   public TransformBuilder method8685(TransformBuilder var1, TransformBuilder var2, int var3, TransformDecomposition var4, int var5, float var6) {
      this.copyOf(var1);
      if (var6 == 1.0F && this.isIdentity() && var2.isIdentity()) {
         return this.method8668(var4, var5, 1.0F);
      }

      int var7 = var3 | var5;
      if ((var7 & 1) != 0) {
         this.yr.set((var3 & 1) != 0 ? var2.yr : var1.yr).lerp((var5 & 1) != 0 ? var4.yg : var1.yr, var6);
      }

      if ((var7 & 2) != 0) {
         this.ys.set((var3 & 2) != 0 ? var2.ys : var1.ys).slerp((var5 & 2) != 0 ? var4.yh : var1.ys, var6).normalize();
         this.yu.set((var3 & 2) != 0 ? var2.yu : var1.yu).slerp((var5 & 2) != 0 ? var4.yj : var1.yu, var6).normalize();
      }

      if ((var7 & 4) != 0) {
         this.yt.set((var3 & 4) != 0 ? var2.yt : var1.yt).lerp((var5 & 4) != 0 ? var4.yi : var1.yt, var6);
      }

      if (var7 != 0) {
         this.yw = null;
      }

      return this;
   }

   public TransformBuilder method8686(TransformBuilder var1, TransformBuilder var2, int var3, TransformBuilder var4, int var5, float var6) {
      this.copyOf(var1);
      if (var6 == 1.0F && this.isIdentity() && var2.isIdentity()) {
         return this.method8667(var4, var5, 1.0F);
      }

      int var7 = var3 | var5;
      if ((var7 & 1) != 0) {
         this.yr.set((var3 & 1) != 0 ? var2.yr : var1.yr).lerp((var5 & 1) != 0 ? var4.yr : var1.yr, var6);
      }

      if ((var7 & 2) != 0) {
         this.ys.set((var3 & 2) != 0 ? var2.ys : var1.ys).slerp((var5 & 2) != 0 ? var4.ys : var1.ys, var6).normalize();
         this.yu.set((var3 & 2) != 0 ? var2.yu : var1.yu).slerp((var5 & 2) != 0 ? var4.yu : var1.yu, var6).normalize();
      }

      if ((var7 & 4) != 0) {
         this.yt.set((var3 & 4) != 0 ? var2.yt : var1.yt).lerp((var5 & 4) != 0 ? var4.yt : var1.yt, var6);
      }

      if (var7 != 0) {
         this.yw = null;
      }

      return this;
   }

   public Matrix4f method8687(Matrix4f var1) {
      return this.yw != null ? var1.set(this.yw) : var1.translation(this.yr).rotate(this.ys).scale(this.yt).rotate(this.yu);
   }

   public boolean isIdentity() {
      return this.yr.isFinite() && this.ys.isFinite() && this.yt.isFinite() && this.yu.isFinite();
   }
}
