package dev.lone.itemsadder.util;

import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class MutableBlockPos {
   private int x;
   private int y;
   private int z;

   public MutableBlockPos() {
   }

   public MutableBlockPos(Block var1) {
      this(var1.getX(), var1.getY(), var1.getZ());
   }

   public MutableBlockPos(Location var1) {
      this(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ());
   }

   public MutableBlockPos(Vector var1) {
      this(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ());
   }

   public MutableBlockPos(short var1) {
      this.x = var1 >>> 8 & 15;
      this.y = var1 & 15;
      this.z = var1 >>> 4 & 15;
   }

   public MutableBlockPos(int var1, int var2, int var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public int getX() {
      return this.x;
   }

   public void setX(int var1) {
      this.x = var1;
   }

   public int getZ() {
      return this.z;
   }

   public void setZ(int var1) {
      this.z = var1;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int var1) {
      this.y = var1;
   }

   public MutableBlockPos add(MutableBlockPos var1) {
      return this.addOffset(var1.x, var1.y, var1.z);
   }

   public MutableBlockPos addOffset(int var1, int var2, int var3) {
      this.x += var1;
      this.y += var2;
      this.z += var3;
      return this;
   }

   public MutableBlockPos subtract(MutableBlockPos var1) {
      this.x = this.x - var1.getX();
      this.y = this.y - var1.getY();
      this.z = this.z - var1.getZ();
      return this;
   }

   public MutableBlockPos multiply(int var1) {
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      return this;
   }

   public static MutableBlockPos min(MutableBlockPos var0, MutableBlockPos var1) {
      return new MutableBlockPos(Math.min(var0.x, var1.x), Math.min(var0.y, var1.y), Math.min(var0.z, var1.z));
   }

   public static MutableBlockPos max(MutableBlockPos var0, MutableBlockPos var1) {
      return new MutableBlockPos(Math.max(var0.x, var1.x), Math.max(var0.y, var1.y), Math.max(var0.z, var1.z));
   }

   public Vector toVector() {
      return new Vector(this.x, this.y, this.z);
   }

   public Location toLocation(World var1) {
      return new Location(var1, this.x, this.y, this.z);
   }

   public Block toBlock(World var1) {
      return var1.getBlockAt(this.x, this.y, this.z);
   }

   public MutableBlockPos copy() {
      return new MutableBlockPos(this.x, this.y, this.z);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else {
         return !(o instanceof MutableBlockPos var2) ? false : this.x == var2.x && this.y == var2.y && this.z == var2.z;
      }
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.x, this.y, this.z);
   }

   @Override
   public String toString() {
      return this.x + "," + this.y + "," + this.z;
   }

   public short toPackedShort() {
      return (short)((this.x & 15) << 8 | (this.z & 15) << 4 | this.y & 15);
   }

   public static MutableBlockPos deserialize(String var0) {
      if (var0.length() < 11) {
         throw new IllegalArgumentException("Failed to deserialize BlockVecWrap " + var0 + ": Wrong string length.");
      }

      String[] var1 = var0.split(",");
      if (var1.length != 3) {
         throw new IllegalArgumentException("Failed to deserialize BlockVecWrap " + var0 + ": Check coordinates count.");
      }

      try {
         int var2 = Integer.parseInt(var1[0].substring(2));
         int var3 = Integer.parseInt(var1[1].substring(2));
         int var4 = Integer.parseInt(var1[2].substring(2));
         return new MutableBlockPos(var2, var3, var4);
      } catch (NumberFormatException var5) {
         throw new IllegalArgumentException("Failed to deserialize BlockVecWrap " + var0 + ": " + var5.getMessage());
      }
   }
}
