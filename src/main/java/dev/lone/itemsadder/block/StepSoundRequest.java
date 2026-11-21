package dev.lone.itemsadder.block;

import org.bukkit.Location;
import org.bukkit.entity.Player;

class StepSoundRequest {
   private final Player li;
   private final PlayerStepState lj;
   private final BlockDisplayRegistry lk;
   private final Location field5;
   private final Location lm;
   private final double ln;
   private final int field6;
   private final int lp;
   private final boolean lq;
   private final long lr;

   private StepSoundRequest(
      Player var1, PlayerStepState var2, BlockDisplayRegistry var3, Location var4, Location var5, double var6, int var8, int var9, boolean var10, long var11
   ) {
      this.li = var1;
      this.lj = var2;
      this.lk = var3;
      this.field5 = var4;
      this.lm = var5;
      this.ln = var6;
      this.field6 = var8;
      this.lp = var9;
      this.lq = var10;
      this.lr = var11;
   }

   public Player player() {
      return this.li;
   }

   public PlayerStepState state() {
      return this.lj;
   }

   public BlockDisplayRegistry pool() {
      return this.lk;
   }

   public Location location() {
      return this.field5;
   }

   public Location eyeLocation() {
      return this.lm;
   }

   public double distanceFromFloor() {
      return this.ln;
   }

   public int currentTick() {
      return this.field6;
   }

   public int playerIndex() {
      return this.lp;
   }

   public boolean forceUpdate() {
      return this.lq;
   }

   public long lifecycleGeneration() {
      return this.lr;
   }
}
