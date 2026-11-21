package dev.lone.itemsadder.entity;

import java.util.Arrays;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;

@Generated
public abstract class MatcherNodeG extends MatcherBuilderBase {
   @Generated
   private EntityTypePairRule[] ZD;
   @Generated
   private EntityTypePairRule[] ZE;

   @Generated
   public MatcherNodeG withPairs(@Nullable EntityTypePairRule[] var1) {
      this.ZD = var1;
      return this.ye();
   }

   @Generated
   public MatcherNodeG withPairsDup2(@Nullable EntityTypePairRule[] var1) {
      this.ZE = var1;
      return this.ye();
   }

   @Generated
   protected abstract MatcherNodeG ye();

   @Generated
   public abstract EntityMatcherRootG getRoot();

   @Generated
   @Override
   public String toString() {
      return "HurtByTargetGoal.HurtByTargetGoalBuilder(super="
         + super.toString()
         + ", ignoreDamageFromEntities="
         + Arrays.deepToString(this.ZD)
         + ", alertOthersBukkitTypes="
         + Arrays.deepToString(this.ZE)
         + ")";
   }
}
