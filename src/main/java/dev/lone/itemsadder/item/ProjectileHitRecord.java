package dev.lone.itemsadder.item;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class ProjectileHitRecord {
   private final long RI;
   private final int RJ;
   private final long RK;
   private final String RL;
   private final String RM;
   @Nullable
   private final UUID RN;
   @Nullable
   private final UUID RO;
   private final double RP;
   private final boolean cancelled;

   public ProjectileHitRecord(long var1, int var3, long var4, String var6, String var7, @Nullable UUID var8, @Nullable UUID var9, double var10, boolean var12) {
      this.RI = var1;
      this.RJ = var3;
      this.RK = var4;
      this.RL = var6;
      this.RM = var7;
      this.RN = var8;
      this.RO = var9;
      this.RP = var10;
      this.cancelled = var12;
   }

   public long jN() {
      return this.RI;
   }

   public int ut() {
      return this.RJ;
   }

   public long method115() {
      return this.RK;
   }

   public String uv() {
      return this.RL;
   }

   public String pU() {
      return this.RM;
   }

   @Nullable
   public UUID uF() {
      return this.RN;
   }

   @Nullable
   public UUID ux() {
      return this.RO;
   }

   public double damage() {
      return this.RP;
   }
}
