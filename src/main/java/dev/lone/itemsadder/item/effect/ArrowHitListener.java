package dev.lone.itemsadder.item.effect;

import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

class ArrowHitListener implements Listener {
   private ArrowHitListener() {
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onProjectileHit(ProjectileHitEvent var1) {
      if (var1.getEntity() instanceof Arrow) {
         for (ArrowDisplayState var3 : ArrowDisplayEffect.akm) {
            if (var3.akv == var1.getEntity()) {
               var3.akB = true;
               return;
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   public void onEntityDamageByEntity(EntityDamageByEntityEvent var1) {
      if (var1.getDamager() instanceof Arrow) {
         for (ArrowDisplayState var3 : ArrowDisplayEffect.akm) {
            if (var3.akv == var1.getDamager()) {
               if (var3.akA == 0.0) {
                  var1.setCancelled(true);
               } else {
                  var1.setDamage(var3.akA);
               }

               return;
            }
         }
      }
   }
}
