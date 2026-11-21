package dev.lone.itemsadder.physics;

import org.joml.Matrix4f;

final class BoneBindPose {
   final Matrix4f Hb;
   final Matrix4f Hc = new Matrix4f();
   final float[] Hd;
   int He = -1;

   BoneBindPose(Matrix4f var1, float[] var2) {
      this.Hb = var1;
      this.Hd = var2;
   }
}
