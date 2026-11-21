package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.entity.CustomEntityImpl;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.jetbrains.annotations.NotNull;

public class ComplexFurnitureInteractEvent extends ComplexFurnitureEvent implements Cancellable {
   private static final HandlerList HANDLERS_LIST = new HandlerList();
   @NotNull
   private final Action action;
   private boolean isCancelled = false;

   public ComplexFurnitureInteractEvent(Player player, LivingEntity bukkitEntity, @NotNull Action action, CustomEntityImpl internal) {
      super(player, bukkitEntity, internal);
      this.action = action;
   }

   @Deprecated
   public ComplexFurnitureInteractEvent(Player player, LivingEntity bukkitEntity, CustomEntityImpl internal) {
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
