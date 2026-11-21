package dev.lone.itemsadder.block;

import dev.lone.itemsadder.util.StringListMatcher;
import lombok.Generated;

@Generated
public class ExplosiveBlockSpecBuilder {
   @Generated
   private StringListMatcher arz;
   @Generated
   private int aiU;
   @Generated
   private double arB;
   @Generated
   private boolean arC;
   @Generated
   private boolean arD;
   @Generated
   private int arE;
   @Generated
   private int arF;

   @Generated
   ExplosiveBlockSpecBuilder() {
   }

   @Generated
   public ExplosiveBlockSpecBuilder withItemsMatcher(StringListMatcher var1) {
      this.arz = var1;
      return this;
   }

   @Generated
   public ExplosiveBlockSpecBuilder withRadius(int var1) {
      this.aiU = var1;
      return this;
   }

   @Generated
   public ExplosiveBlockSpecBuilder withDamageMultiplier(double var1) {
      this.arB = var1;
      return this;
   }

   @Generated
   public ExplosiveBlockSpecBuilder withWaterproof(boolean var1) {
      this.arC = var1;
      return this;
   }

   @Generated
   public ExplosiveBlockSpecBuilder withIncendiary(boolean var1) {
      this.arD = var1;
      return this;
   }

   @Generated
   public ExplosiveBlockSpecBuilder withFuseTicks(int var1) {
      this.arE = var1;
      return this;
   }

   @Generated
   public ExplosiveBlockSpecBuilder withTriggers(int var1) {
      this.arF = var1;
      return this;
   }

   @Generated
   public ExplosiveBlockSpec build() {
      return new ExplosiveBlockSpec(this.arz, this.aiU, this.arB, this.arC, this.arD, this.arE, this.arF);
   }

   @Generated
   @Override
   public String toString() {
      return "PrimeableSettings.PrimeableSettingsBuilder(itemsMatcher="
         + this.arz
         + ", radius="
         + this.aiU
         + ", damageMultiplier="
         + this.arB
         + ", waterproof="
         + this.arC
         + ", incendiary="
         + this.arD
         + ", fuseTicks="
         + this.arE
         + ", triggers="
         + this.arF
         + ")";
   }
}
