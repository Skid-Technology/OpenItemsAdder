package dev.lone.itemsadder.util;

import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BoxBounds {
   public double minX;
   public double minY;
   public double minZ;
   public double maxX;
   public double maxY;
   public double maxZ;

   public BoxBounds(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.minX = var1;
      this.minY = var3;
      this.minZ = var5;
      this.maxX = var7;
      this.maxY = var9;
      this.maxZ = var11;
   }

   @NotNull
   public static BoxBounds fromCorners(@NotNull Vector var0, @NotNull Vector var1) {
      return new BoxBounds(var0.getX(), var0.getY(), var0.getZ(), var1.getX(), var1.getY(), var1.getZ());
   }

   public BoundingBox toBoundingBox() {
      return new BoundingBox(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
   }

   public float getHeight() {
      return (float)(this.maxY - this.minY);
   }

   public float getDepth() {
      return (float)(this.maxZ - this.minZ);
   }

   public BoxBounds expand(double var1) {
      return new BoxBounds(this.minX - var1, this.minY - var1, this.minZ - var1, this.maxX + var1, this.maxY + var1, this.maxZ + var1);
   }

   @Nullable
   public Vector rayIntersection(Vector var1, Vector var2) {
      Vector var3 = new Vector(this.minX, this.minY, this.minZ);
      Vector var4 = new Vector(this.maxX, this.maxY, this.maxZ);
      double var5 = Double.NEGATIVE_INFINITY;
      double var7 = Double.POSITIVE_INFINITY;

      for (int var9 = 0; var9 < 3; var9++) {
         double var10 = var9 == 0 ? var1.getX() : (var9 == 1 ? var1.getY() : var1.getZ());
         double var12 = var9 == 0 ? var2.getX() : (var9 == 1 ? var2.getY() : var2.getZ());
         double var14 = var9 == 0 ? var3.getX() : (var9 == 1 ? var3.getY() : var3.getZ());
         double var16 = var9 == 0 ? var4.getX() : (var9 == 1 ? var4.getY() : var4.getZ());
         if (Math.abs(var12) < 1.0E-6) {
            if (var10 < var14 || var10 > var16) {
               return null;
            }
         } else {
            double var18 = (var14 - var10) / var12;
            double var20 = (var16 - var10) / var12;
            if (var18 > var20) {
               double var22 = var18;
               var18 = var20;
               var20 = var22;
            }

            var5 = Math.max(var5, var18);
            var7 = Math.min(var7, var20);
            if (var5 > var7) {
               return null;
            }
         }
      }

      if (!(var5 < 0.0)) {
         return var1.clone().add(var2.clone().multiply(var5));
      } else {
         return var7 < 0.0 ? null : var1.clone().add(var2.clone().multiply(var7));
      }
   }

   @Nullable
   Vector rayIntersectionWithin(Vector var1, Vector var2, double var3) {
      Vector var5 = this.rayIntersection(var1, var2);
      return var5 != null && var5.distanceSquared(var1) <= var3 ? var5 : null;
   }
}
