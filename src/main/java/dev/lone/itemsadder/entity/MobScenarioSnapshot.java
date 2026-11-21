package dev.lone.itemsadder.entity;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public class MobScenarioSnapshot {
   private final EntityDescriptor SV;
   private final String SW;
   private final String SX;
   private final int SY;
   private final int SZ;
   private final int Ta;
   private final int Tb;
   private final int Tc;
   private final int Td;
   private final int Te;
   private final boolean Tf;
   private final List Tg;
   private final List Th;
   private final List Ti;
   @Nullable
   private final String Tj;

   public MobScenarioSnapshot(
      EntityDescriptor var1,
      String var2,
      String var3,
      int var4,
      int var5,
      int var6,
      int var7,
      int var8,
      int var9,
      int var10,
      boolean var11,
      List var12,
      List var13,
      List var14,
      @Nullable String var15
   ) {
      this.SV = var1;
      this.SW = var2;
      this.SX = var3;
      this.SY = var4;
      this.SZ = var5;
      this.Ta = var6;
      this.Tb = var7;
      this.Tc = var8;
      this.Td = var9;
      this.Te = var10;
      this.Tf = var11;
      this.Tg = var12;
      this.Th = var13;
      this.Ti = var14;
      this.Tj = var15;
   }

   public EntityDescriptor vn() {
      return this.SV;
   }

   public String vo() {
      return this.SW;
   }

   public String uv() {
      return this.SX;
   }

   public int ut() {
      return this.SY;
   }

   public int vp() {
      return this.SZ;
   }

   public int vq() {
      return this.Ta;
   }

   public int vr() {
      return this.Tb;
   }

   public int vs() {
      return this.Tc;
   }

   public int vt() {
      return this.Td;
   }

   public int vu() {
      return this.Te;
   }

   public boolean method116() {
      return this.Tf;
   }

   public List vw() {
      return this.Tg;
   }

   public List vx() {
      return this.Th;
   }

   public List oy() {
      return this.Ti;
   }

   @Nullable
   public String vy() {
      return this.Tj;
   }
}
