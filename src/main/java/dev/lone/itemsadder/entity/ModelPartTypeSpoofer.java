package dev.lone.itemsadder.entity;

import java.lang.reflect.Field;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public final class ModelPartTypeSpoofer {
   private static final ClassValue aVQ = new EntityTypeFieldLocator();

   private ModelPartTypeSpoofer() {
   }

   public static LivingEntity spoof(LivingEntity var0) {
      if (var0.getType() == EntityType.UNKNOWN) {
         return var0;
      }

      try {
         ((Field)aVQ.get(var0.getClass())).set(var0, EntityType.UNKNOWN);
         return var0;
      } catch (IllegalAccessException var2) {
         throw new IllegalStateException("Cannot initialize Bukkit model part type", var2);
      }
   }
}
