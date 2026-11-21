package dev.lone.itemsadder.entity;

import org.bukkit.entity.LivingEntity;

public interface LivingEntityPredicate {
   boolean test(LivingEntity var1, LivingEntity var2);
}
