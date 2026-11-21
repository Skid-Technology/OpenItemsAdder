package dev.lone.itemsadder.anim;

import dev.lone.itemsadder.util.DoubleQuaternion;
import dev.lone.itemsadder.util.DoubleVector3;

public class BoneKeyframe {
   private final String Ec;
   private final DoubleVector3 Ed;
   private final DoubleQuaternion Ee;
   private final DoubleVector3 Ef;
   private final Double Eg;
   private final Double Eh;

   public BoneKeyframe(String var1, DoubleVector3 var2, DoubleQuaternion var3, DoubleVector3 var4, Double var5, Double var6) {
      this.Ec = var1;
      this.Ed = var2;
      this.Ee = var3;
      this.Ef = var4;
      this.Eg = var5;
      this.Eh = var6;
   }

   public String pU() {
      return this.Ec;
   }

   public DoubleVector3 pV() {
      return this.Ed;
   }

   public DoubleQuaternion pW() {
      return this.Ee;
   }

   public DoubleVector3 pX() {
      return this.Ef;
   }

   public Double pY() {
      return this.Eg;
   }

   public Double pZ() {
      return this.Eh;
   }
}
