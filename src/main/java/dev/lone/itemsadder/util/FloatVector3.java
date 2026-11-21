package dev.lone.itemsadder.util;

import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

public class FloatVector3 extends FloatVector3Base {
   private boolean hashCached;
   private int cachedHash;

   public FloatVector3(float var1, float var2, float var3) {
      super(var1, var2, var3);
   }

   FloatVector3(float var1, float var2, float var3, int var4) {
      super(var1, var2, var3);
      this.cachedHash = var4;
      this.hashCached = true;
   }

   public boolean isZero() {
      return this.x == 0.0F && this.y == 0.0F && this.z == 0.0F;
   }

   public double distance(@NotNull FloatVector3 var1) {
      return Math.sqrt(NumberConversions.square(this.x - var1.x) + NumberConversions.square(this.y - var1.y) + NumberConversions.square(this.z - var1.z));
   }

   public double distanceXZ(@NotNull FloatVector3 var1) {
      return Math.sqrt(NumberConversions.square(this.x - var1.x) + NumberConversions.square(this.z - var1.z));
   }

   @Override
   public String toString() {
      return "ImmutableVecF{x=" + this.x + ", y=" + this.y + ", z=" + this.z + "}";
   }

   public FloatVector3 copy() {
      return new FloatVector3(this.x, this.y, this.z, this.cachedHash);
   }

   @Override
   public int hashCode() {
      if (this.hashCached) {
         return this.cachedHash;
      }

      this.cachedHash = 7;
      this.cachedHash = 79 * this.cachedHash + (int)(Double.doubleToLongBits(this.x) ^ Double.doubleToLongBits(this.x) >>> 32);
      this.cachedHash = 79 * this.cachedHash + (int)(Double.doubleToLongBits(this.y) ^ Double.doubleToLongBits(this.y) >>> 32);
      this.cachedHash = 79 * this.cachedHash + (int)(Double.doubleToLongBits(this.z) ^ Double.doubleToLongBits(this.z) >>> 32);
      this.hashCached = true;
      return this.cachedHash;
   }

   public boolean isEqualTo(FloatVector3 var1) {
      if (this == var1) {
         return true;
      } else {
         return this.hashCached && var1.hashCached ? this.cachedHash == var1.cachedHash : this.hashCode() == var1.hashCode();
      }
   }
}
