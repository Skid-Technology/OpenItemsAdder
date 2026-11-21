package dev.lone.itemsadder.entity;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public final class MatrixTable {
   private final int[] AG;
   private final Matrix4f[] AH;

   public MatrixTable(int[] var1, Matrix4fc[] var2) {
      if (var1.length == var2.length && var1.length >= 2) {
         this.AG = (int[])var1.clone();
         this.AH = new Matrix4f[var2.length];

         for (int var3 = 0; var3 < var2.length; var3++) {
            this.AH[var3] = new Matrix4f(var2[var3]);
         }
      } else {
         throw new IllegalArgumentException("Interpolation endpoints must match");
      }
   }

   public int method8875(int var1) {
      int var2 = 0;
      int var3 = this.AG.length - 1;

      while (var2 < var3) {
         int var4 = var2 + var3 >>> 1;
         if (this.AG[var4 + 1] <= var1) {
            var2 = var4 + 1;
         } else {
            var3 = var4;
         }
      }

      return var1 >= this.AG[0] && var1 < this.AG[this.AG.length - 1] ? var2 : -1;
   }

   public int method8876(int var1) {
      return this.AG[var1];
   }

   public Matrix4fc getMatrix(int var1) {
      return new Matrix4f(this.AH[var1]);
   }

   public int size() {
      return this.AG.length;
   }
}
