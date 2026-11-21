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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionGrantEffectDup2 extends ItemEffect {
   public final PotionEffectType akI;
   public final int akJ;
   public final int akK;
   private final boolean akL;
   private final boolean akM;
   private final boolean akN;

   public PotionGrantEffectDup2(PotionEffectType var1, int var2, int var3, boolean var4, boolean var5, boolean var6) {
      this.akI = var1;
      this.akJ = var2;
      this.akK = var3;
      this.akL = var4;
      this.akM = var5;
      this.akN = var6;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      return EffectOutcome.failure();
   }

   @Override
   public EffectOutcome applyOnDamage(EntityDamageByEntityEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.grantPotionTo(var1.getEntity());
   }

   @Override
   public EffectOutcome applyOnInteractEntity(PlayerInteractEntityEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.grantPotionTo(var1.getRightClicked());
   }

   @Override
   public EffectOutcome applyOnProjectileHit(ProjectileHitEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.grantPotionTo(var1.getHitEntity());
   }

   private EffectOutcome grantPotionTo(Entity var1) {
      return var1 instanceof LivingEntity && ((LivingEntity)var1).addPotionEffect(new PotionEffect(this.akI, this.akK, this.akJ, this.akL, this.akM, this.akN))
         ? EffectOutcome.failure()
         : EffectOutcome.success();
   }
}
