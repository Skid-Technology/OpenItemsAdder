package dev.lone.itemsadder.util;

import dev.lone.itemsadder.anim.SkeletonPose;
import java.util.Arrays;

public final class FloatCurveSampler {
   private final float[] angles;
   private final float[] values;
   private int head;

   FloatCurveSampler(int var1) {
      this.angles = new float[var1];
      this.values = new float[var1];
   }

   void reset(float var1, float var2) {
      Arrays.fill(this.angles, SkeletonPose.method9106(var1));
      Arrays.fill(this.values, var2);
      this.head = 0;
   }

   void push(float var1, float var2) {
      this.head = (this.head + 1) % this.angles.length;
      this.angles[this.head] = SkeletonPose.method9106(var1);
      this.values[this.head] = var2;
   }

   float sampleAngle(float var1) {
      int var2 = (int)Math.floor(var1);
      float var3 = var1 - var2;
      float var4 = this.angles[this.wrapIndex(var2)];
      return SkeletonPose.method9106(var4 + SkeletonPose.method9106(this.angles[this.wrapIndex(var2 + 1)] - var4) * var3);
   }

   float sampleFloat(float var1) {
      int var2 = (int)Math.floor(var1);
      float var3 = var1 - var2;
      return SkeletonPose.method9107(this.values[this.wrapIndex(var2)], this.values[this.wrapIndex(var2 + 1)], var3);
   }

   private int wrapIndex(int var1) {
      int var2 = (this.head - Math.min(var1, this.angles.length - 1)) % this.angles.length;
      return var2 < 0 ? var2 + this.angles.length : var2;
   }
}
