package dev.lone.itemsadder.block;

import org.bukkit.Chunk;
import org.bukkit.Location;

public class ChunkLocalBlock {
   private final int field7;
   private final int jk;
   private final int jl;
   private final String jm;
   private final byte jn;

   public ChunkLocalBlock(int var1, int var2, int var3, String var4, byte var5) {
      this.field7 = var1;
      this.jk = var2;
      this.jl = var3;
      this.jm = var4;
      this.jn = var5;
   }

   public Location toWorldLocation(Chunk var1) {
      return new Location(var1.getWorld(), var1.getX() * 16 + this.field7, this.jk, var1.getZ() * 16 + this.jl);
   }

   public int relX() {
      return this.field7;
   }

   public int y() {
      return this.jk;
   }

   public int relZ() {
      return this.jl;
   }

   public String namespacedId() {
      return this.jm;
   }

   public byte age() {
      return this.jn;
   }
}
