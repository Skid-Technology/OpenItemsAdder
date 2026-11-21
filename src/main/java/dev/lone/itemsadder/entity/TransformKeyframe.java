package dev.lone.itemsadder.entity;

import org.joml.Quaternionf;
import org.joml.Vector3f;

class TransformKeyframe {
   private final int yl;
   private final int ym;
   private final int yn;
   private final Quaternionf yo;
   private final Quaternionf yp;
   private final Vector3f yq;

   private TransformKeyframe(int var1, int var2, int var3, Quaternionf var4, Quaternionf var5, Vector3f var6) {
      this.yl = var1;
      this.ym = var2;
      this.yn = var3;
      this.yo = var4;
      this.yp = var5;
      this.yq = var6;
   }

   public int startTick() {
      return this.yl;
   }

   public int endTick() {
      return this.ym;
   }

   public int easing() {
      return this.yn;
   }

   public Quaternionf rotationStart() {
      return this.yo;
   }

   public Quaternionf rotationEnd() {
      return this.yp;
   }

   public Vector3f translation() {
      return this.yq;
   }
}
