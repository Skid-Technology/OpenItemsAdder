package dev.lone.itemsadder.entity;

import org.jetbrains.annotations.Nullable;

public class IntTriple {
   @Nullable
   public Integer qx;
   @Nullable
   public Integer qy;
   @Nullable
   public Integer qz;

   public IntTriple(@Nullable Integer var1, @Nullable Integer var2, @Nullable Integer var3) {
      this.qx = var1;
      this.qy = var2;
      this.qz = var3;
   }

   @Override
   public String toString() {
      return "Side{x=" + this.qx + ", y=" + this.qy + ", z=" + this.qz + "}";
   }

   public IntTriple gR() {
      return new IntTriple(this.qx, this.qy, this.qz);
   }
}
