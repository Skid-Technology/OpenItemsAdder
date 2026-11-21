package dev.lone.itemsadder.util;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class SyntheticEntityDamageEvent extends EntityDamageByEntityEvent {
   public SyntheticEntityDamageEvent(Entity var1, Entity var2, DamageCause var3, double var4) {
      super(var1, var2, var3, var4);
   }
}
