package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.nms.NmsRegistry;
import org.bukkit.entity.Entity;

public final class EntityEventSinkHolder {
   private final EntityEventSink bpO = (EntityEventSink)NmsRegistry.method2777(EntityEventSink.class);
   private static EntityEventSinkHolder bpP;

   EntityEventSinkHolder() {
   }

   private static EntityEventSinkHolder getInstance() {
      if (bpP == null) {
         bpP = new EntityEventSinkHolder();
      }

      return bpP;
   }

   public static void dispatch(Entity slime, int size) {
      getInstance().bpO.onEntityEvent(slime, size);
   }
}
