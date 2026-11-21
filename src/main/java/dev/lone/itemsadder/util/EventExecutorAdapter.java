package dev.lone.itemsadder.util;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class EventExecutorAdapter implements EventExecutor {
   private final Class eventType;
   private final EventHandlerCallback callback;

   public EventExecutorAdapter(Class var1, EventHandlerCallback var2) {
      this.eventType = var1;
      this.callback = var2;
   }

   public void execute(@NotNull Listener listener, @NotNull Event event) {
      if (this.eventType.isInstance(event)) {
         this.callback.handle((Event)this.eventType.cast(event));
      }
   }
}
