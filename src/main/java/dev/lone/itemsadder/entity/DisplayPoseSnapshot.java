package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.particle.ParentPose;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class DisplayPoseSnapshot {
   private final UUID auz;
   private final double auA;
   private final double auB;
   private final double auC;
   private final double auD;
   private final double auE;
   private final double auF;
   private final double auG;
   private final Set auH;

   public DisplayPoseSnapshot(UUID var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, Set var16) {
      Objects.requireNonNull(var1);
      new ParentPose(var2, var4, var6, var8, var10, var12, var14);
      var16 = Set.copyOf(var16);
      this.auz = var1;
      this.auA = var2;
      this.auB = var4;
      this.auC = var6;
      this.auD = var8;
      this.auE = var10;
      this.auF = var12;
      this.auG = var14;
      this.auH = var16;
   }

   ParentPose toParentPose(double var1, double var3, double var5) {
      return new ParentPose(this.auA - var1, this.auB - var3, this.auC - var5, this.auD, this.auE, this.auF, this.auG);
   }

   public UUID entityId() {
      return this.auz;
   }

   public double ka() {
      return this.auA;
   }

   public double kb() {
      return this.auB;
   }

   public double kc() {
      return this.auC;
   }

   public double HG() {
      return this.auD;
   }

   public double method131() {
      return this.auE;
   }

   public double HI() {
      return this.auF;
   }

   public double HJ() {
      return this.auG;
   }

   public Set HK() {
      return this.auH;
   }
}
