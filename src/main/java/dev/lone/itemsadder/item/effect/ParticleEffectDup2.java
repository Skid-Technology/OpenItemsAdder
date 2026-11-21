package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.particle.ParticleEmitterManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ParticleEffectDup2 extends ItemEffect {
   public final Particle ajF;
   @Nullable
   public final Object ajG;
   @Nullable
   public final String ajH;
   public final int ajI;

   public ParticleEffectDup2(Particle var1, @Nullable Object var2) {
      this.ajF = var1;
      this.ajG = var2;
      this.ajH = null;
      this.ajI = 60;
   }

   public ParticleEffectDup2(String var1, int var2) {
      if (!ParticleEmitterManager.dP(var1)) {
         throw new IllegalArgumentException("Expected a custom particle ID");
      }

      this.ajH = var1;
      this.ajI = ParticleEmitterManager.method1713(var2);
      this.ajF = null;
      this.ajG = null;
   }

   private void spawnParticleAt(Location var1) {
      if (this.ajH != null) {
         new ParticleEmitterManager().method1714(this.ajH, var1, null, this.ajI);
      } else {
         var1.getWorld().spawnParticle(this.ajF, var1, 40, 0.0, 0.0, 0.0, 0.0, this.ajG);
      }
   }

   @Override
   public EffectOutcome applyToBlock(Event var1, Block var2, CustomItemDefinition var3, ItemStack var4, Player var5) {
      if (this.ajH != null && var2 != null) {
         this.spawnParticleAt(var2.getLocation().add(0.5, 0.5, 0.5));
         return EffectOutcome.failure();
      } else {
         return this.applyEffect(var1, var3, var4, var5);
      }
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      this.spawnParticleAt(var4.getEyeLocation());
      return EffectOutcome.failure();
   }

   @Override
   public EffectOutcome applyOnProjectileHit(ProjectileHitEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var1.getHitEntity() != null) {
         this.spawnParticleAt(var1.getHitEntity().getLocation());
      } else if (var1.getHitBlock() != null) {
         this.spawnParticleAt(var1.getHitBlock().getLocation());
      }

      return EffectOutcome.failure();
   }
}
