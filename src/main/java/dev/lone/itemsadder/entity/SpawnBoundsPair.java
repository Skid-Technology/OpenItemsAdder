package dev.lone.itemsadder.entity;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public class SpawnBoundsPair {
   @Nullable
   private final IntTriple qv;
   @Nullable
   private final IntTriple qw;

   public SpawnBoundsPair(@Nullable IntTriple var1, @Nullable IntTriple var2) {
      this.qv = var1;
      this.qw = var2;
   }

   public boolean contains(Location var1) {
      if (this.qv != null) {
         if (this.qv.qx != null && var1.getBlockX() < this.qv.qx) {
            return false;
         }

         if (this.qv.qy != null && var1.getBlockY() < this.qv.qy) {
            return false;
         }

         if (this.qv.qz != null && var1.getBlockZ() < this.qv.qz) {
            return false;
         }
      }

      if (this.qw != null) {
         if (this.qw.qx != null && var1.getBlockX() > this.qw.qx) {
            return false;
         }

         if (this.qw.qy != null && var1.getBlockY() > this.qw.qy) {
            return false;
         }

         if (this.qw.qz != null && var1.getBlockZ() > this.qw.qz) {
            return false;
         }
      }

      return true;
   }

   @Nullable
   public IntTriple min() {
      return this.qv;
   }

   @Nullable
   public IntTriple max() {
      return this.qw;
   }
}
