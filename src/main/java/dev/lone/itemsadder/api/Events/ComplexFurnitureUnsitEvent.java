package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.entity.CustomEntityImpl;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ComplexFurnitureUnsitEvent extends ComplexFurnitureEvent {
   private static final HandlerList HANDLERS_LIST = new HandlerList();

   public ComplexFurnitureUnsitEvent(Player player, @Nullable LivingEntity bukkitEntity, CustomEntityImpl internal) {
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
