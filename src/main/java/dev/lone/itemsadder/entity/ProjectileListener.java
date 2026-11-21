package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.EntityEquipmentChangeEvent;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.EventRegistrar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;

public class ProjectileListener implements IaListener {
   private final Plugin aHG;

   public ProjectileListener(Plugin var1) {
      this.aHG = var1;
      EventRegistrar.registerIfAbsent(this, var1);
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
   void onPickup(PlayerPickupItemEvent var1) {
      if (var1.getItem().hasMetadata("IAProjectileNamespacedID")) {
         CustomItemDefinition var2 = ContentManager.method8084()
            .method8154(((MetadataValue)var1.getItem().getMetadata("IAProjectileNamespacedID").get(0)).asString());
         if (var2 != null) {
            var1.getItem().setItemStack(var2.Ds());
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   void onProjectileHit(ProjectileHitEvent var1) {
      if (var1.getHitEntity() != null) {
         if (var1.getEntity().doesBounce()) {
            if (var1.getHitEntity() instanceof ArmorStand) {
               if (!((ArmorStand)var1.getHitEntity()).isVisible() && !var1.getHitEntity().isInvulnerable()) {
                  if (!var1.getEntity().hasMetadata("Bounce")) {
                     var1.getEntity().setMetadata("Bounce", new FixedMetadataValue(Main.getInstance(), 1));
                  } else {
                     var1.getEntity()
                        .setMetadata(
                           "Bounce", new FixedMetadataValue(Main.getInstance(), ((MetadataValue)var1.getEntity().getMetadata("Bounce").get(0)).asInt() + 1)
                        );
                  }

                  if (((MetadataValue)var1.getEntity().getMetadata("Bounce").get(0)).asInt() > 5) {
                     ProjectileTypeHelper.method3227(var1.getEntity());
                  }
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   void onShootBow(EntityShootBowEvent var1) {
      if (var1.getEntity().getType() == EntityType.PLAYER) {
         Entity var2 = var1.getProjectile();
         var2.setMetadata("playerName", new FixedMetadataValue(this.aHG, var1.getEntity().getName()));
         EventRegistrar.callEvent(
            new EntityEquipmentChangeEvent(
               var1.getEntity(), var1.getBow(), var1.getConsumable(), var1.getProjectile(), var1.getHand(), var1.getForce(), var1.shouldConsumeItem()
            )
         );
      }
   }

   @EventHandler(ignoreCancelled = true)
   void onProjectileLaunch(ProjectileLaunchEvent var1) {
      ProjectileSource var2 = var1.getEntity().getShooter();
      if (var2 instanceof Entity) {
         if (((Entity)var2).getType() == EntityType.PLAYER) {
            Projectile var3 = var1.getEntity();
            var3.setMetadata("playerName", new FixedMetadataValue(this.aHG, ((Entity)var2).getName()));
         }
      }
   }
}
