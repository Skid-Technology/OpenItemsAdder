package dev.lone.itemsadder.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BaseItemsAdderEvent extends Event {
   private static final HandlerList Yk = new HandlerList();

   @NotNull
   public HandlerList getHandlers() {
      return Yk;
   }

   public static HandlerList getHandlerList() {
      return Yk;
   }
}
