package dev.lone.itemsadder.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerActionEvent extends Event {
   public final Player ani;

   public PlayerActionEvent(Player var1) {
      this.ani = var1;
   }

   @NotNull
   public HandlerList getHandlers() {
      throw new RuntimeException("DUMMY EVENT TRIGGERED!");
   }
}
