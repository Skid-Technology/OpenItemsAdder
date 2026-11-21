package dev.lone.itemsadder.entity;

import org.bukkit.entity.LivingEntity;

public interface LivingEntityMatcher {
   boolean matches(LivingEntity var1);

   boolean matchesDup2(LivingEntity var1);
}
