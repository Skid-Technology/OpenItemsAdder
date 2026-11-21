package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.item.FurnitureItemEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class FurniturePrePlaceEvent extends PlayerEvent implements Cancellable {
   private static final HandlerList HANDLERS_LIST = new HandlerList();
   final FurnitureItemEntity internal;
   private final Location location;
   private boolean isCancelled;

   public FurniturePrePlaceEvent(Player player, FurnitureItemEntity internal, Location location) {
      super(player);
      this.internal = internal;
      this.location = location;
      this.isCancelled = false;
   }

   public boolean isCancelled() {
      return this.isCancelled;
   }

   public void setCancelled(boolean cancelled) {
      this.isCancelled = cancelled;
   }

   public String getNamespacedID() {
      return this.internal.aeT.aok;
   }

   public Location getLocation() {
      return this.location;
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
