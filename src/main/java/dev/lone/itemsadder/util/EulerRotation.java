package dev.lone.itemsadder.util;

import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;

public class EulerRotation extends Rotation3D {
   private EulerRotation rotX;
   private EulerRotation rotY;
   private EulerRotation rotZ;
   private final Vector3D axisX;
   private final Vector3D axisY;
   private final Vector3D axisZ;
   private final RotationConvention convention;

   public EulerRotation(RotationOrder var1, RotationConvention var2, double var3, double var5, double var7) {
      super(1.0, 0.0, 0.0, 0.0, false);
      this.axisX = var1.getA1();
      this.axisY = var1.getA2();
      this.axisZ = var1.getA3();
      this.convention = var2;
      this.rotX = new EulerRotation(1.0, 0.0, 0.0, 0.0, false);
      this.rotY = new EulerRotation(1.0, 0.0, 0.0, 0.0, false);
      this.rotZ = new EulerRotation(1.0, 0.0, 0.0, 0.0, false);
      this.setAngles(var3, var5, var7);
   }

   private EulerRotation(double var1, double var3, double var5, double var7, boolean var9) {
      super(var1, var3, var5, var7, var9);
      this.axisX = null;
      this.axisY = null;
      this.axisZ = null;
      this.convention = RotationConvention.VECTOR_OPERATOR;
   }

   public void setAngles(double var1, double var3, double var5) {
      this.rotX.setFromAxisAngle(this.axisX, var1, this.convention);
      this.rotY.setFromAxisAngle(this.axisY, var3, this.convention);
      this.rotZ.setFromAxisAngle(this.axisZ, var5, this.convention);
      this.rotY.composeInPlace(this.rotZ, this.convention);
      this.rotX.composeInPlace(this.rotZ, this.convention);
      this.q0 = this.rotZ.q0;
      this.q1 = this.rotZ.q1;
      this.q2 = this.rotZ.q2;
      this.q3 = this.rotZ.q3;
   }

   private void setFromAxisAngle(Vector3D var1, double var2, RotationConvention var4) {
      double var5 = var1.getNorm();
      if (var5 == 0.0) {
         this.q0 = 1.0;
         this.q1 = 0.0;
         this.q2 = 0.0;
         this.q3 = 0.0;
      } else {
         double var7 = var4 == RotationConvention.VECTOR_OPERATOR ? -0.5 * var2 : 0.5 * var2;
         double var9 = FastMath.sin(var7) / var5;
         this.q0 = FastMath.cos(var7);
         this.q1 = var9 * var1.getX();
         this.q2 = var9 * var1.getY();
         this.q3 = var9 * var1.getZ();
      }
   }
}
