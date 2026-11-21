package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EntityUtils;
import java.util.Collection;
import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

public class ProjectileSpreadEffect extends ItemEffect {
   public final float aiT;
   public final int aiU;
   public final List aiV;

   public ProjectileSpreadEffect(float var1, int var2, List var3) {
      this.aiT = var1;
      this.aiU = var2;
      this.aiV = var3;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      boolean var5 = false;

      for (Entity var8 : BlockFaceUtils.entitiesNear(var4.getLocation(), this.aiU)) {
         if (var8 instanceof LivingEntity
            && var8 != var4
            && (this.aiV.isEmpty() || this.aiV.contains(TargetCategory.fromEntity(var8)))
            && EntityUtils.isWithinRange(var4, (LivingEntity)var8, this.aiU, this.aiT)) {
            var5 = true;
         }
      }

      return var5 ? EffectOutcome.failure() : EffectOutcome.success();
   }

   @Override
   public EffectOutcome applyOnProjectileHit(ProjectileHitEvent var1, CustomItemDefinition var2, ItemStack var3) {
      boolean var4 = false;
      Collection var5;
      if (var1.getHitEntity() != null) {
         var5 = BlockFaceUtils.entitiesNear(var1.getHitEntity().getLocation(), this.aiU);
      } else {
         var5 = BlockFaceUtils.entitiesNear(var1.getHitBlock().getLocation(), this.aiU);
      }

      for (Entity var7 : var5) {
         if (var7 instanceof LivingEntity
            && (this.aiV.isEmpty() || this.aiV.contains(TargetCategory.fromEntity(var7)))
            && EntityUtils.isProjectileHitting(var1.getEntity(), (LivingEntity)var7, this.aiU, this.aiT)) {
            var4 = true;
         }
      }

      return var4 ? EffectOutcome.failure() : EffectOutcome.success();
   }
}
