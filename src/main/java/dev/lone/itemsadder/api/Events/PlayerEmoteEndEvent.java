package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.api.Events.PlayerEmoteEndEvent.Cause;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerEmoteEndEvent extends PlayerEvent {
   private static final HandlerList HANDLERS_LIST = new HandlerList();
   private final String emoteName;
   private final Cause cause;

   public PlayerEmoteEndEvent(@NotNull Player who, String emoteName, Cause cause) {
      super(who);
      this.emoteName = emoteName;
      this.cause = cause;
   }

   public String getEmoteName() {
      return this.emoteName;
   }

   public Cause getCause() {
      return this.cause;
   }

   @NotNull
   public HandlerList getHandlers() {
      return HANDLERS_LIST;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return HANDLERS_LIST;
   }
}
