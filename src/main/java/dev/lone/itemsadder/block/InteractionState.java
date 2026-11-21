package dev.lone.itemsadder.block;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

class InteractionState {
   private static final HashMap do_ = new HashMap();
   public long dp;
   public long dq;
   public long dr;
   public long ds;
   public long dt;
   public long du;
   public long dv;
   public long dw;
   public long dx;
   long dy;
   long dz = System.currentTimeMillis();
   private Location dA;
   private Location dB;
   @Nullable
   public BlockFace dC;
   @Nullable
   public Block dD;
   private WeakReference dE = new WeakReference(null);

   void setAnchor(Location var1) {
      this.dA = var1.clone();
      this.dB = null;
   }

   void updateAnchor(Location var1, long var2) {
      if (this.dA == null || this.dA.getWorld() != var1.getWorld()) {
         this.setAnchor(var1);
      } else if (!(squaredDistanceXZ(this.dA, var1) < 64.0)) {
         if (this.dB == null || !(squaredDistanceXZ(this.dB, var1) < 16.0)) {
            this.dB = this.dA;
            this.dA = var1.clone();
            this.dz = var2;
         }
      }
   }

   private static double squaredDistanceXZ(Location var0, Location var1) {
      double var2 = var0.getX() - var1.getX();
      double var4 = var0.getZ() - var1.getZ();
      return var2 * var2 + var4 * var4;
   }

   boolean isExpired(long var1) {
      long var3 = Math.max(this.dz, Math.max(this.dy, Math.max(Math.max(this.dp, this.dt), Math.max(Math.max(this.du, this.dv), Math.max(this.dw, this.dx)))));
      return var1 - var3 >= 120000L;
   }

   @Nullable
   public static InteractionState getState(Player var0) {
      return (InteractionState)do_.get(var0.getUniqueId());
   }

   public static InteractionState createState(Player var0) {
      return do_.put(var0.getUniqueId(), new InteractionState());
   }

   public static void removeState(Player var0) {
      do_.remove(var0.getUniqueId());
   }

   public void setTargetEntity(Entity var1) {
      this.dE = new WeakReference<>(var1);
   }

   @Nullable
   public Entity getTargetEntity() {
      return (Entity)this.dE.get();
   }
}
