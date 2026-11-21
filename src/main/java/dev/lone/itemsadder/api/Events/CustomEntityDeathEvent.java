package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.entity.CustomEntityImpl;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomEntityDeathEvent extends Event {
   protected final LivingEntity entity;
   public final CustomEntityImpl internal;
   private static final HandlerList HANDLERS_LIST = new HandlerList();

   public CustomEntityDeathEvent(LivingEntity entity, CustomEntityImpl renderer) {
      this.entity = entity;
      this.internal = renderer;
   }

   @NotNull
   public HandlerList getHandlers() {
      return HANDLERS_LIST;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return HANDLERS_LIST;
   }

   public String getNamespacedID() {
      return this.internal.getNamespacedId();
   }

   public LivingEntity getEntity() {
      return this.entity;
   }

   @Nullable
   public Player getKiller() {
      return this.entity.getKiller();
   }
}
