package dev.lone.itemsadder.entity;

import org.bukkit.entity.LivingEntity;

public interface ModelPartEntity {
   LivingEntity getLivingEntity();

   boolean isBroken();

   void a_(double var1, double var3, double var5, float var7, float var8);

   void remove();

   boolean isForwardingDamage();
}
