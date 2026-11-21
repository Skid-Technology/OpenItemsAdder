package dev.lone.itemsadder.util;

import org.bukkit.util.Vector;
import org.joml.Vector3f;

public class IntVector3 {
   public int x;
   public int y;
   public int z;

   public IntVector3(int var1, int var2, int var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public Vector toVector() {
      return new Vector(this.x, this.y, this.z);
   }

   @Override
   public String toString() {
      return "VecF{x=" + this.x + ", y=" + this.y + ", z=" + this.z + "}";
   }

   public IntVector3 copy() {
      return new IntVector3(this.x, this.y, this.z);
   }

   public boolean isZero() {
      return this.x == 0 && this.y == 0 && this.z == 0;
   }

   public Vector3f toVector3f() {
      return new Vector3f(this.x, this.y, this.z);
   }
}
