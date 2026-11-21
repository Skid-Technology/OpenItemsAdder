package dev.lone.itemsadder.api;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerEntityInteractEvent extends Event implements Cancellable {
   public final Player ang;
   public final Entity anh;
   boolean isCancelled;

   public PlayerEntityInteractEvent(Player var1, Entity var2) {
      this.ang = var1;
      this.anh = var2;
   }

   public boolean isCancelled() {
      return this.isCancelled;
   }

   public void setCancelled(boolean cancelled) {
      this.isCancelled = cancelled;
   }

   @NotNull
   public HandlerList getHandlers() {
      throw new RuntimeException("DUMMY EVENT TRIGGERED!");
   }
}
