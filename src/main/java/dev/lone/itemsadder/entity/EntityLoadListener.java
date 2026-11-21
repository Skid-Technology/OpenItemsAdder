package dev.lone.itemsadder.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;

public class EntityLoadListener implements Listener {
   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onEntitiesLoad(EntitiesLoadEvent var1) {
      for (Entity var3 : var1.getEntities()) {
         CustomEntityEventListener.method9235(var3);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   private void onEntitiesUnload(EntitiesUnloadEvent var1) {
      for (Entity var3 : var1.getEntities()) {
         if (var3 instanceof LivingEntity && !(var3 instanceof Player) && CustomEntityManager.getInstance().isCustomEntityLoaded(var3)) {
            CustomEntityManager.getInstance().removeEntityByBukkit(var3);
         }
      }
   }
}
