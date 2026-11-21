package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.nms.NmsRegistry;
import dev.lone.itemsadder.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.LivingEntity;

public class BodyRotationControllerFactory {
   static Constructor aON;

   public static BodyRotationController create(LivingEntity var0, float var1) {
      if (aON == null) {
         aON = NmsRegistry.method2779(BodyRotationController.class, false, LivingEntity.class, float.class);
         if (aON == null) {
            throw new RuntimeException("Failed to get constructor for the body controller.");
         }
      }

      try {
         return (BodyRotationController)aON.newInstance(var0, var1);
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException var3) {
         Log.error("Error finding constructor for IBodyRotController", var3);
         return null;
      }
   }
}
