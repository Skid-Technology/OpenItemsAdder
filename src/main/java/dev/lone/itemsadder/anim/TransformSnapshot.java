package dev.lone.itemsadder.anim;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class TransformSnapshot {
   private final int aRt;
   private final Matrix4f aRu;
   private final long aRv;
   @Nullable
   private final Matrix4f aRw;

   public TransformSnapshot(int var1, Matrix4f var2, long var3, @Nullable Matrix4f var5) {
      var2 = new Matrix4f(var2);
      var5 = var5 == null ? null : new Matrix4f(var5);
      this.aRt = var1;
      this.aRu = var2;
      this.aRv = var3;
      this.aRw = var5;
   }

   public Matrix4f NR() {
      return new Matrix4f(this.aRu);
   }

   @Nullable
   public Matrix4f NS() {
      return this.aRw == null ? null : new Matrix4f(this.aRw);
   }

   public int jI() {
      return this.aRt;
   }

   public long jK() {
      return this.aRv;
   }
}
