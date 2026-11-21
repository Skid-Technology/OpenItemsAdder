package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.entity.CustomEntityImpl;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ComplexFurniturePlacedEvent extends ComplexFurnitureEvent implements Cancellable {
   private static final HandlerList HANDLERS_LIST = new HandlerList();
   private boolean isCancelled = false;

   public ComplexFurniturePlacedEvent(@Nullable Player player, LivingEntity bukkitEntity, CustomEntityImpl internal) {
      super(player, bukkitEntity, internal);
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
}
