package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.RayTraceTarget;
import dev.lone.itemsadder.util.RayTraceWalker;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class ScaledCountEffect extends ItemEffect {
   public final float aiR;
   public final int aiS;

   public ScaledCountEffect(float var1, int var2) {
      this.aiR = var1;
      this.aiS = var2;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      RayTraceWalker var5 = new RayTraceWalker(var4, this.aiS);
      if (var5.matchesTarget(RayTraceTarget.ENTITY_AND_BLOCK)) {
         if (var5.hasHitBlock()) {
            return EffectOutcome.success();
         }

         if (var5.hasHitEntity() && var5.getHitEntity() instanceof LivingEntity && BlockFaceUtils.simulateDamage(var4, var5.getHitEntity())) {
            ((LivingEntity)var5.getHitEntity()).damage(this.aiR);
            return EffectOutcome.failure();
         }
      }

      return EffectOutcome.success();
   }
}
