package dev.lone.itemsadder.event;

import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.SchedulerUtils;
import org.bukkit.event.Listener;

public interface IaListener extends Listener {
   static boolean method532(IaListener var0) {
      if (var0 != null) {
         var0.unregister();
         return true;
      } else {
         return true;
      }
   }

   default void unregister() {
      EventRegistrar.unregisterListener(this);
      SchedulerUtils.cancelTasksFor(this.getClass());
   }

   default void bW() {
   }
}
