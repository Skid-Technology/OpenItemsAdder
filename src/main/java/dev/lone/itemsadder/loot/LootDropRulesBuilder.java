package dev.lone.itemsadder.loot;

import com.google.common.collect.ImmutableList;
import dev.lone.itemsadder.util.StringListMatcher;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;

@Generated
public abstract class LootDropRulesBuilder extends AbstractLootConditions {
   @Generated
   private ImmutableList kG;
   @Generated
   private Integer kH;
   @Generated
   private Integer kI;
   @Generated
   private Integer kJ;
   @Generated
   private StringListMatcher kK;

   @Generated
   public LootDropRulesBuilder method2031(@Nullable ImmutableList var1) {
      this.kG = var1;
      return this.dB();
   }

   @Generated
   public LootDropRulesBuilder method2032(@Nullable Integer var1) {
      this.kH = var1;
      return this.dB();
   }

   @Generated
   public LootDropRulesBuilder method2033(@Nullable Integer var1) {
      this.kI = var1;
      return this.dB();
   }

   @Generated
   public LootDropRulesBuilder method2034(@Nullable Integer var1) {
      this.kJ = var1;
      return this.dB();
   }

   @Generated
   public LootDropRulesBuilder method2035(@Nullable StringListMatcher var1) {
      this.kK = var1;
      return this.dB();
   }

   @Generated
   protected abstract LootDropRulesBuilder dB();

   @Generated
   public abstract LootDropRulesImplA dC();

   @Generated
   @Override
   public String toString() {
      return "CropLoot.CropLootBuilder(super="
         + super.toString()
         + ", triggers="
         + this.kG
         + ", ageMin="
         + this.kH
         + ", ageMax="
         + this.kI
         + ", decreaseAge="
         + this.kJ
         + ", itemRulesMatcher="
         + this.kK
         + ")";
   }
}
