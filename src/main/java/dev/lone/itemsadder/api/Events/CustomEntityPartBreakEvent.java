package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.api.CustomEntity;
import java.util.UUID;
import org.bukkit.damage.DamageSource;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CustomEntityPartBreakEvent extends Event {
   private static final HandlerList HANDLERS = new HandlerList();
   private final CustomEntity entity;
   private final UUID partId;
   private final String bone;
   @Nullable
   private final DamageSource damageSource;

   public CustomEntityPartBreakEvent(CustomEntity entity, UUID partId, String bone, @Nullable DamageSource damageSource) {
      this.entity = entity;
      this.partId = partId;
      this.bone = bone;
      this.damageSource = damageSource;
   }

   public CustomEntity getCustomEntity() {
      return this.entity;
   }

   public UUID getPartId() {
      return this.partId;
   }

   public String getBone() {
      return this.bone;
   }

   @Nullable
   public DamageSource getDamageSource() {
      return this.damageSource;
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
