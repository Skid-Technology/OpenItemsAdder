package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class InteractionEffect extends ItemEffect {
   public final int ajk;
   public final boolean ajl;
   public final boolean ajm;

   public InteractionEffect(int var1, boolean var2, boolean var3) {
      this.ajk = var1;
      this.ajl = var2;
      this.ajm = var3;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      World var5 = var4.getWorld();
      return var5.createExplosion(var4.getLocation(), this.ajk, this.ajl, this.ajm, var4) ? EffectOutcome.failure() : EffectOutcome.success();
   }

   @Override
   public EffectOutcome applyToBlock(Event var1, Block var2, CustomItemDefinition var3, ItemStack var4, Player var5) {
      World var6 = var5.getWorld();
      if (var2 == null) {
         return EffectOutcome.success();
      } else {
         return var6.createExplosion(var2.getLocation(), this.ajk, this.ajl, this.ajm, var5) ? EffectOutcome.failure() : EffectOutcome.success();
      }
   }

   @Override
   public EffectOutcome applyOnProjectileHit(ProjectileHitEvent var1, CustomItemDefinition var2, ItemStack var3) {
      World var4 = var1.getEntity().getWorld();
      return var4.createExplosion(var1.getEntity().getLocation(), this.ajk, this.ajl, this.ajm, (Entity)var1.getEntity().getShooter())
         ? EffectOutcome.failure()
         : EffectOutcome.success();
   }

   @Override
   public EffectOutcome applyOnInteractEntity(PlayerInteractEntityEvent var1, CustomItemDefinition var2, ItemStack var3) {
      World var4 = var1.getRightClicked().getWorld();
      return var4.createExplosion(var1.getRightClicked().getLocation(), this.ajk, this.ajl, this.ajm, var1.getPlayer())
         ? EffectOutcome.failure()
         : EffectOutcome.success();
   }

   @Override
   public EffectOutcome applyOnDamage(EntityDamageByEntityEvent var1, CustomItemDefinition var2, ItemStack var3) {
      World var4 = var1.getEntity().getWorld();
      return var4.createExplosion(var1.getEntity().getLocation(), this.ajk, this.ajl, this.ajm) ? EffectOutcome.failure() : EffectOutcome.success();
   }
}
