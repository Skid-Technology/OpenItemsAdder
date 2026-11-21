package dev.lone.itemsadder.loot;

import com.google.common.collect.ImmutableList;
import dev.lone.itemsadder.util.StringListMatcher;
import lombok.Generated;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class LootDropRulesImplA extends LootDropRules {
   @Nullable
   public final ImmutableList kG;
   @Nullable
   public final Integer kH;
   @Nullable
   public final Integer kI;
   @Nullable
   public final Integer kJ;
   @Nullable
   final StringListMatcher kK;

   public boolean method2014(int var1) {
      return this.kH != null && var1 < this.kH ? false : this.kI == null || var1 <= this.kI;
   }

   public boolean method2015(@Nullable String var1) {
      return this.kK == null ? true : this.kK.matchesName(var1);
   }

   public boolean method2016(@Nullable Player var1) {
      return this.kK == null ? true : this.kK.matchesHeldItem(var1);
   }

   @Generated
   protected LootDropRulesImplA(LootDropRulesBuilder var1) {
      super(var1);
      this.kG = var1.kG;
      this.kH = var1.kH;
      this.kI = var1.kI;
      this.kJ = var1.kJ;
      this.kK = var1.kK;
   }

   @Generated
   public static LootDropRulesBuilder dx() {
      return new LootDropRulesBuilderImpl();
   }
}
