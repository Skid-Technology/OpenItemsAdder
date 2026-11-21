package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.item.FurnitureItemEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.jetbrains.annotations.NotNull;

public class FurnitureInteractEvent extends FurnitureEvent implements Cancellable {
   private static final HandlerList HANDLERS_LIST = new HandlerList();
   @NotNull
   private final Action action;
   private boolean isCancelled = false;

   public FurnitureInteractEvent(Player player, Entity bukkitEntity, @NotNull Action action, FurnitureItemEntity internal) {
      super(player, bukkitEntity, internal);
      this.action = action;
   }

   @Deprecated
   public FurnitureInteractEvent(Player player, Entity bukkitEntity, FurnitureItemEntity internal) {
      super(player, bukkitEntity, internal);
      this.action = Action.LEFT_CLICK_AIR;
   }

   public boolean isCancelled() {
      return this.isCancelled;
   }

   public void setCancelled(boolean cancelled) {
      this.isCancelled = cancelled;
   }

   @NotNull
   public HandlerList getHandlers() {
      return HANDLERS_LIST;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return HANDLERS_LIST;
   }

   @NotNull
   public Action getAction() {
      return this.action;
   }
}
