package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.item.FurnitureItemEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Deprecated
public class FurniturePlaceEvent extends PlayerEvent implements Cancellable {
   private static final HandlerList HANDLERS_LIST = new HandlerList();
   final FurnitureItemEntity internal;
   private boolean isCancelled;

   public FurniturePlaceEvent(Player player, FurnitureItemEntity internal) {
      super(player);
      this.internal = internal;
      this.isCancelled = false;
   }

   public boolean isCancelled() {
      return this.isCancelled;
   }

   public void setCancelled(boolean cancelled) {
      this.isCancelled = cancelled;
   }

   @Nullable
   public String getNamespacedID() {
      return this.internal.aeT.aok;
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
