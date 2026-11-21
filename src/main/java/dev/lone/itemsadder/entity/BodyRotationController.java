package dev.lone.itemsadder.entity;

import org.bukkit.entity.LivingEntity;

public interface BodyRotationController extends LivingEntityMatcher {
   boolean iq();

   float Nn();

   float No();

   @Override
   default boolean matches(LivingEntity var1) {
      return true;
   }
}
