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
import org.bukkit.potion.PotionEffectType;

public class PotionRemoveEffectDup2 extends ItemEffect {
   public final PotionEffectType akO;

   public PotionRemoveEffectDup2(PotionEffectType var1) {
      this.akO = var1;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      return EffectOutcome.failure();
   }

   @Override
   public EffectOutcome applyOnDamage(EntityDamageByEntityEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.removePotionFrom(var1.getEntity());
   }

   @Override
   public EffectOutcome applyOnInteractEntity(PlayerInteractEntityEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.removePotionFrom(var1.getRightClicked());
   }

   @Override
   public EffectOutcome applyOnProjectileHit(ProjectileHitEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.removePotionFrom(var1.getHitEntity());
   }

   private EffectOutcome removePotionFrom(Entity var1) {
      if (var1 instanceof LivingEntity var2 && var2.hasPotionEffect(this.akO)) {
         var2.removePotionEffect(this.akO);
         return EffectOutcome.failure();
      } else {
         return EffectOutcome.success();
      }
   }
}
