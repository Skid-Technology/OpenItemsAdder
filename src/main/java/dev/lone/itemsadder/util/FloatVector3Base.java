package dev.lone.itemsadder.util;

import org.bukkit.util.Vector;

public class FloatVector3Base {
   public float x;
   public float y;
   public float z;

   public FloatVector3Base(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public Vector toBukkitVector() {
      return new Vector(this.x, this.y, this.z);
   }

   @Override
   public String toString() {
      return "VecF{x=" + this.x + ", y=" + this.y + ", z=" + this.z + "}";
   }

   public FloatVector3Base copy() {
      return new FloatVector3Base(this.x, this.y, this.z);
   }

   @Override
   public int hashCode() {
      int var1 = 7;
      var1 = 79 * var1 + (int)(Double.doubleToLongBits(this.x) ^ Double.doubleToLongBits(this.x) >>> 32);
      var1 = 79 * var1 + (int)(Double.doubleToLongBits(this.y) ^ Double.doubleToLongBits(this.y) >>> 32);
      return 79 * var1 + (int)(Double.doubleToLongBits(this.z) ^ Double.doubleToLongBits(this.z) >>> 32);
   }
}
