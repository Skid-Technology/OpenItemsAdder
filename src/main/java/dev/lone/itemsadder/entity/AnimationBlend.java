package dev.lone.itemsadder.entity;

import java.util.Objects;

public class AnimationBlend {
   private final ModelDefinitionData Bu;
   private final ModelDefinitionData Bv;
   private final ModelDefinitionData Bw;
   private final float Bx;
   private final float By;
   private final float Bz;

   public AnimationBlend(ModelDefinitionData var1, ModelDefinitionData var2, ModelDefinitionData var3, float var4, float var5, float var6) {
      Objects.requireNonNull(var1);
      Objects.requireNonNull(var2);
      Objects.requireNonNull(var3);
      if (Float.isFinite(var4) && Float.isFinite(var5) && Float.isFinite(var6) && !(var4 < 0.0F) && !(var5 <= var4) && !(var6 <= 0.0F) && !(var6 > 1.0F)) {
         this.Bu = var1;
         this.Bv = var2;
         this.Bw = var3;
         this.Bx = var4;
         this.By = var5;
         this.Bz = var6;
      } else {
         throw new IllegalArgumentException("Invalid Flight Blend tuning");
      }
   }

   public ModelDefinitionData method65() {
      return this.Bu;
   }

   public ModelDefinitionData op() {
      return this.Bv;
   }

   public ModelDefinitionData oq() {
      return this.Bw;
   }

   public float or() {
      return this.Bx;
   }

   public float os() {
      return this.By;
   }

   public float ot() {
      return this.Bz;
   }
}
