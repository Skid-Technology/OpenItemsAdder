package dev.lone.itemsadder.util;

import dev.lone.itemsadder.nms.NmsRegistry;
import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public final class RaycastUtils {
   private static RaycastUtils INSTANCE;
   private final RaycastStrategy strategy = (RaycastStrategy)NmsRegistry.method2777(RaycastStrategy.class);

   RaycastUtils() {
   }

   private static RaycastUtils getInstance() {
      if (INSTANCE == null) {
         INSTANCE = new RaycastUtils();
      }

      return INSTANCE;
   }

   @Nullable
   public static Vector raycastBetween(Location var0, Location var1) {
      return raycastBetween(var0, var1, true);
   }

   @Nullable
   public static Vector raycastBetween(Location var0, Location var1, boolean var2) {
      return getInstance().strategy.traceVector(var0, var1, var2);
   }

   @Nullable
   public static Vector raycastForward(Location var0, double var1) {
      return raycastForward(var0, var1, true);
   }

   @Nullable
   public static Vector raycastForward(Location var0, double var1, boolean var3) {
      Location var4 = var0.clone();
      var4.add(var0.getDirection().normalize().multiply(var1));
      return raycastBetween(var0, var4, var3);
   }

   public static RaycastResult raycastEntity(Entity bukkitEntity, HashSet nmsBlockTypes) {
      return getInstance().strategy.traceEntity(bukkitEntity, nmsBlockTypes);
   }
}
