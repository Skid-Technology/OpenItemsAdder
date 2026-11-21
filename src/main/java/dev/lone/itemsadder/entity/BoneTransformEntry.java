package dev.lone.itemsadder.entity;

import org.joml.Matrix4f;

class BoneTransformEntry {
   private final TransformBuilder DG;
   private final Matrix4f DH;
   private final Matrix4f DI;
   private final boolean DJ;

   private BoneTransformEntry(TransformBuilder var1, Matrix4f var2, Matrix4f var3, boolean var4) {
      this.DG = var1;
      this.DH = var2;
      this.DI = var3;
      this.DJ = var4;
   }

   public TransformBuilder transform() {
      return this.DG;
   }

   public Matrix4f localMatrix() {
      return this.DH;
   }

   public Matrix4f worldMatrix() {
      return this.DI;
   }

   public boolean visible() {
      return this.DJ;
   }
}
