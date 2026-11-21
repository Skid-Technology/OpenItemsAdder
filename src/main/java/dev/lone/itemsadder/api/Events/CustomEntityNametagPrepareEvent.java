package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.api.CustomEntity;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CustomEntityNametagPrepareEvent extends Event {
   private static final HandlerList HANDLERS = new HandlerList();
   private final CustomEntity entity;
   @Nullable
   private final Component originalText;
   @Nullable
   private Component text;

   public CustomEntityNametagPrepareEvent(CustomEntity entity, @Nullable Component originalText, @Nullable Component text) {
      this.entity = Objects.requireNonNull(entity, "entity");
      this.originalText = originalText;
      this.text = text;
   }

   @NotNull
   public CustomEntity getCustomEntity() {
      return this.entity;
   }

   @Nullable
   public Component getOriginalText() {
      return this.originalText;
   }

   @Nullable
   public Component getText() {
      return this.text;
   }

   public void setText(@Nullable Component text) {
      this.text = text;
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
