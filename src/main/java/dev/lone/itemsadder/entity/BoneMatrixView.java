package dev.lone.itemsadder.entity;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public class BoneMatrixView {
   private final BoneTransformTable DK;
   private final int DL;
   private final Matrix4fc DM;

   public BoneMatrixView(BoneTransformTable var1, int var2, Matrix4fc var3) {
      this.DK = var1;
      this.DL = var2;
      this.DM = var3;
   }

   public Matrix4fc getMatrix(int var1) {
      return this.DK.DF[this.DL][var1].DI;
   }

   public Matrix4f getMatrixInto(int var1, Matrix4f var2) {
      return var2.set(this.DM).mul(this.getMatrix(var1));
   }

   public BoneTransformTable table() {
      return this.DK;
   }

   public int boneIndex() {
      return this.DL;
   }

   public Matrix4fc matrix() {
      return this.DM;
   }
}
