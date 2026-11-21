package dev.lone.itemsadder.item.effect;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EffectOutcome {
   public final EffectResult ala;
   @Nullable
   public final ItemStack alb;

   public EffectOutcome(EffectResult var1, @Nullable ItemStack var2) {
      this.ala = var1;
      this.alb = var2;
   }

   public static EffectOutcome success() {
      return new EffectOutcome(EffectResult.FAILED, null);
   }

   public static EffectOutcome successWith(ItemStack var0) {
      return new EffectOutcome(EffectResult.FAILED, var0);
   }

   public static EffectOutcome failure() {
      return new EffectOutcome(EffectResult.SUCCESS, null);
   }

   public static EffectOutcome failureWith(ItemStack var0) {
      return new EffectOutcome(EffectResult.SUCCESS, var0);
   }
}
