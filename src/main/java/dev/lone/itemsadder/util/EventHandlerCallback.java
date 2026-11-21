package dev.lone.itemsadder.util;

import org.bukkit.event.Event;

@FunctionalInterface
public interface EventHandlerCallback {
   void handle(Event var1);
}
