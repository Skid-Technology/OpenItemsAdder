package dev.lone.itemsadder.block;

import dev.lone.itemsadder.util.BoxBounds;
import dev.lone.itemsadder.util.IntVector3;
import lombok.Generated;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class BlockDisplayEntry {
   private volatile boolean cV;
   final ItemDisplay mr;
   @Nullable
   String namespacedId;
   int ms;
   long mt;
   private boolean mu = false;
   public float mv;
   public long mw;
   int mx = 3;
   IntVector3 my;
   private volatile BoxBounds mz = BoxBounds.fromCorners(new Vector(0, 0, 0), new Vector(0, 0, 0));
   private final Location mA = new Location(null, 0.0, 0.0, 0.0);

   BlockDisplayEntry(ItemDisplay var1, Location var2, @Nullable String var3, int var4) {
      this.mr = var1;
      this.my = new IntVector3(var2.getBlockX(), var2.getBlockY(), var2.getBlockZ());
      this.namespacedId = var3;
      this.ms = var4;
      this.mt = System.currentTimeMillis();
   }

   public void setActiveFlag(boolean var1) {
      this.mu = var1;
   }

   public boolean shouldUpdateForBlock(ChunkLocalBlockDup2 var1, boolean var2, boolean var3) {
      return this.resolveUpdate(this.isStateChanged(var1), var2, var3);
   }

   public boolean shouldUpdateForState(byte var1, String var2, boolean var3, boolean var4) {
      return this.resolveUpdate(this.differsFromState(var1, var2), var3, var4);
   }

   boolean isStateChanged(ChunkLocalBlockDup2 var1) {
      return this.differsFromState(var1.age(), var1.namespacedId());
   }

   boolean isStateIdChanged(ChunkLocalBlockDup2 var1) {
      return this.ms != var1.age();
   }

   boolean isNamespaceChanged(ChunkLocalBlockDup2 var1) {
      return !var1.namespacedId().equals(this.namespacedId);
   }

   private boolean differsFromState(byte var1, String var2) {
      return this.ms != var1 || !var2.equals(this.namespacedId);
   }

   boolean resolveUpdate(boolean var1, boolean var2, boolean var3) {
      boolean var4 = this.mu && !var2 || !this.mu && var2;
      return !var1 && var3 && var2 ? false : var1 || var4;
   }

   public void refreshTimestamp() {
      this.mt = System.currentTimeMillis();
   }

   public boolean advanceCooldown() {
      if (this.mx - 1 > 0) {
         this.mx--;
         return false;
      } else {
         this.mx = 3;
         return true;
      }
   }

   public World getWorld() {
      return this.mr.getWorld();
   }

   public Chunk getChunk() {
      return this.mr.getChunk();
   }

   public void setLocation(Location var1) {
      this.my.x = var1.getBlockX();
      this.my.y = var1.getBlockY();
      this.my.z = var1.getBlockZ();
      this.mA.setWorld(var1.getWorld());
      this.mA.setX(var1.getX());
      this.mA.setY(var1.getY());
      this.mA.setZ(var1.getZ());
   }

   public Location getLocation() {
      return this.mA;
   }

   public Block getBlock() {
      return this.mA.getBlock();
   }

   @Generated
   public boolean isDirty() {
      return this.cV;
   }

   @Generated
   public void setDirty(boolean var1) {
      this.cV = var1;
   }

   @Generated
   public void setActiveFlagDup2(boolean var1) {
      this.mu = var1;
   }

   @Generated
   public boolean isActiveFlag() {
      return this.mu;
   }

   @Generated
   public void setBounds(BoxBounds var1) {
      this.mz = var1;
   }

   @Generated
   public BoxBounds getBounds() {
      return this.mz;
   }
}
