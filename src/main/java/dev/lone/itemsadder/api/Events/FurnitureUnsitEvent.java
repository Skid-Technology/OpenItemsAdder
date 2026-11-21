package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.item.FurnitureItemEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class FurnitureUnsitEvent extends FurnitureEvent {
   private static final HandlerList HANDLERS_LIST = new HandlerList();

   public FurnitureUnsitEvent(Player player, Entity bukkitEntity, FurnitureItemEntity internal) {
      super(player, bukkitEntity, internal);
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
