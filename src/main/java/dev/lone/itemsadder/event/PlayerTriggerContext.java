package dev.lone.itemsadder.event;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerTriggerContext {
   private final Player bkD;
   private final Location bkE;
   private final Entity bkF;
   @NotNull
   private final Location bkG;
   public long bkH;
   @Nullable
   private Float lU;
   @Nullable
   private Float lV;

   public PlayerTriggerContext(Player var1, Location var2, Entity var3, @NotNull Location var4) {
      this.bkD = var1;
      this.bkE = var2;
      this.bkF = var3;
      this.bkG = var4;
      this.bkH = System.currentTimeMillis();
   }

   public Player Ph() {
      return this.bkD;
   }

   public Location Pi() {
      return this.bkE;
   }

   public Entity Pj() {
      return this.bkF;
   }

   @NotNull
   public Location Pk() {
      return this.bkG;
   }

   public float method5206(float var1) {
      return this.lU != null ? this.lU : var1;
   }

   public float method5207(float var1) {
      return this.lV != null ? this.lV : var1;
   }

   public void method5208(float var1) {
      this.lU = var1;
   }

   public void method5209(float var1) {
      this.lV = var1;
   }
}
