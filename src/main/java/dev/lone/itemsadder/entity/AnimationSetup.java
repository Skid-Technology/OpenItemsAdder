package dev.lone.itemsadder.entity;

import org.jetbrains.annotations.Nullable;

public class AnimationSetup {
   private final String Cn;
   private final int Co;
   private final String Cp;
   @Nullable
   private final PoseSample Cq;
   @Nullable
   private final PoseSample Cr;
   @Nullable
   private final PoseSample Cs;
   @Nullable
   private final PoseSampleList Ct;
   @Nullable
   private final PoseSampleList Cu;
   @Nullable
   private final PoseFrame Cv;

   public AnimationSetup(
      String var1,
      int var2,
      String var3,
      @Nullable PoseSample var4,
      @Nullable PoseSample var5,
      @Nullable PoseSample var6,
      @Nullable PoseSampleList var7,
      @Nullable PoseSampleList var8
   ) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, null);
   }

   public AnimationSetup(
      String var1,
      int var2,
      String var3,
      @Nullable PoseSample var4,
      @Nullable PoseSample var5,
      @Nullable PoseSample var6,
      @Nullable PoseSampleList var7,
      @Nullable PoseSampleList var8,
      @Nullable PoseFrame var9
   ) {
      this.Cn = var1;
      this.Co = var2;
      this.Cp = var3;
      this.Cq = var4;
      this.Cr = var5;
      this.Cs = var6;
      this.Ct = var7;
      this.Cu = var8;
      this.Cv = var9;
   }

   public String oU() {
      return this.Cn;
   }

   public int oV() {
      return this.Co;
   }

   public String oW() {
      return this.Cp;
   }

   @Nullable
   public PoseSample oX() {
      return this.Cq;
   }

   @Nullable
   public PoseSample oY() {
      return this.Cr;
   }

   @Nullable
   public PoseSample oZ() {
      return this.Cs;
   }

   @Nullable
   public PoseSampleList pa() {
      return this.Ct;
   }

   @Nullable
   public PoseSampleList pb() {
      return this.Cu;
   }

   @Nullable
   public PoseFrame pc() {
      return this.Cv;
   }
}
