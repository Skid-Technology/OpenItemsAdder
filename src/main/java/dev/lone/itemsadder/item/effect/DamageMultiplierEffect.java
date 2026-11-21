package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class DamageMultiplierEffect extends ItemEffect {
   public final float aiQ;

   public DamageMultiplierEffect(float var1) {
      this.aiQ = var1;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      return this.applyMultiplier(var4, var4);
   }

   @Override
   public EffectOutcome applyOnDamage(EntityDamageByEntityEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyMultiplier(var1.getDamager(), var1.getEntity());
   }

   @Override
   public EffectOutcome applyOnInteractEntity(PlayerInteractEntityEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyMultiplier(var1.getPlayer(), var1.getRightClicked());
   }

   @Override
   public EffectOutcome applyOnProjectileHit(ProjectileHitEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyMultiplier(var1.getEntity(), var1.getHitEntity());
   }

   private EffectOutcome applyMultiplier(Entity var1, Entity var2) {
      if (var2 == null) {
         return EffectOutcome.success();
      }

      ((LivingEntity)var2).damage(this.aiQ, var1);
      return EffectOutcome.failure();
   }
}
