package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.api.CustomEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class CustomEntityAnimationSignalEvent extends Event {
   private static final HandlerList HANDLERS = new HandlerList();
   private final CustomEntity entity;
   private final String animation;
   private final String layer;
   private final String signal;
   private final long playbackId;

   public CustomEntityAnimationSignalEvent(CustomEntity entity, String animation, String layer, long playbackId, String signal) {
      this.entity = entity;
      this.animation = animation;
      this.layer = layer;
      this.playbackId = playbackId;
      this.signal = signal;
   }

   public CustomEntity getCustomEntity() {
      return this.entity;
   }

   public String getAnimation() {
      return this.animation;
   }

   public String getLayer() {
      return this.layer;
   }

   public String getSignalId() {
      return this.signal;
   }

   public long getPlaybackId() {
      return this.playbackId;
   }

   @NotNull
   public HandlerList getHandlers() {
      return HANDLERS;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return HANDLERS;
   }
}
