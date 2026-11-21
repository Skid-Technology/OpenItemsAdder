package dev.lone.itemsadder.entity;

import java.util.UUID;

public class BoneState {
   private final UUID Dj;
   private final String Dk;
   private final float Dl;
   private final float Dm;
   private final float Dn;
   private final float Do;
   private final float Dp;
   private final float Dq;
   private final Float Dr;
   private final float Ds;

   public BoneState(UUID var1, String var2, float var3, float var4, float var5, float var6, float var7, float var8, Float var9, float var10) {
      this.Dj = var1;
      this.Dk = var2;
      this.Dl = var3;
      this.Dm = var4;
      this.Dn = var5;
      this.Do = var6;
      this.Dp = var7;
      this.Dq = var8;
      this.Dr = var9;
      this.Ds = var10;
   }

   public UUID id() {
      return this.Dj;
   }

   public String bone() {
      return this.Dk;
   }

   public float x() {
      return this.Dl;
   }

   public float y() {
      return this.Dm;
   }

   public float z() {
      return this.Dn;
   }

   public float rotX() {
      return this.Do;
   }

   public float rotY() {
      return this.Dp;
   }

   public float rotZ() {
      return this.Dq;
   }

   public Float health() {
      return this.Dr;
   }

   public float scale() {
      return this.Ds;
   }
}
