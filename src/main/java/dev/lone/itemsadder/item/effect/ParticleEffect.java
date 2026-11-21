package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.particle.ParticleEmitterManager;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.RayTraceTarget;
import dev.lone.itemsadder.util.RayTraceWalker;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class ParticleEffect extends ItemEffect {
   public final Particle akC;
   @Nullable
   public final Object akD;
   public final int akE;
   @Nullable
   public final String akF;
   public final int akG;

   public ParticleEffect(Particle var1, @Nullable Object var2, int var3) {
      this.akC = var1;
      this.akD = var2;
      this.akE = var3;
      this.akF = null;
      this.akG = 60;
   }

   public ParticleEffect(String var1, int var2, int var3) {
      if (!ParticleEmitterManager.dP(var1)) {
         throw new IllegalArgumentException("Expected a custom particle ID");
      }

      if (var2 >= 1 && var2 <= 128) {
         this.akF = var1;
         this.akG = ParticleEmitterManager.method1713(var3);
         this.akE = var2;
         this.akC = null;
         this.akD = null;
      } else {
         throw new IllegalArgumentException("Custom particle distance must be in 1..128");
      }
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      byte var5 = 10;
      Location var6 = getParticleLocation(var4, var3);
      Location var7 = getParticleLocationAt(var4, this.akE);
      if (this.akF == null) {
         double var14 = var6.distance(var7) / var5;
         int[] var15 = new int[]{0};
         Location var11 = var7;
         Location var12 = var6;
         Collection var13 = BlockFaceUtils.playersWithin(var6, this.akE);
         Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), var8x -> {
            if (var15[0] >= var5) {
               var8x.cancel();
            }

            Location var9x = var12.clone();
            Vector var10x = var11.toVector().subtract(var12.toVector()).normalize();
            Vector var11x = var10x.multiply(var15[0] * var14);
            var9x.add(var11x.getX(), var11x.getY(), var11x.getZ());

            for (Player var13x : var13) {
               var13x.spawnParticle(this.akC, var9x, 1, 0.0, 0.0, 0.0, 0.0, this.akD);
            }

            var15[0]++;
         }, 0L, 0L);
         return EffectOutcome.failure();
      }

      ParticleEmitterManager var8 = new ParticleEmitterManager();
      Vector var9 = var7.toVector().subtract(var6.toVector()).multiply(1.0 / (var5 - 1));

      for (int var10 = 0; var10 < var5; var10++) {
         var8.method1714(this.akF, var6.clone().add(var9.clone().multiply(var10)), null, this.akG);
      }

      return EffectOutcome.failure();
   }

   static Location getParticleLocation(Player var0, ItemStack var1) {
      Location var2 = var0.getEyeLocation();
      return var2.add(
         (CustomItemDefinition.method8780(var1, var0.getInventory().getItemInMainHand()) ? getParticleOffset(var0) : getParticleDirection(var0)).multiply(0.5)
      );
   }

   static Location getParticleLocationAt(Player var0, int var1) {
      RayTraceWalker var2 = new RayTraceWalker(var0, var1);
      return var2.matchesTarget(RayTraceTarget.ENTITY_AND_BLOCK) && var2.hasHitLocation()
         ? var2.getHitLocation()
         : var0.getEyeLocation().add(var0.getLocation().getDirection().multiply(var1));
   }

   private static Vector getParticleOffset(Player var0) {
      Vector var1 = var0.getLocation().getDirection().normalize();
      return new Vector(-var1.getZ(), 0.0, var1.getX()).normalize();
   }

   public static Vector getParticleDirection(Player var0) {
      Vector var1 = var0.getLocation().getDirection().normalize();
      return new Vector(var1.getZ(), 0.0, -var1.getX()).normalize();
   }
}
