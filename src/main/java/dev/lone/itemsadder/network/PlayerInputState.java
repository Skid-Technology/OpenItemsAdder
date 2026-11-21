package dev.lone.itemsadder.network;

import java.util.UUID;

class PlayerInputState {
   private final UUID Vd;
   private final long Ve;
   private final boolean Vf;
   private final boolean Vg;
   private final float Vh;
   private final float Vi;
   private final long Vj;

   private PlayerInputState(UUID var1, long var2, boolean var4, boolean var5, float var6, float var7, long var8) {
      this.Vd = var1;
      this.Ve = var2;
      this.Vf = var4;
      this.Vg = var5;
      this.Vh = var6;
      this.Vi = var7;
      this.Vj = var8;
   }

   public UUID bk() {
      return this.Vd;
   }

   public long bl() {
      return this.Ve;
   }

   public boolean kp() {
      return this.Vf;
   }

   public boolean vR() {
      return this.Vg;
   }

   public float vS() {
      return this.Vh;
   }

   public float vT() {
      return this.Vi;
   }

   public long vU() {
      return this.Vj;
   }
}
