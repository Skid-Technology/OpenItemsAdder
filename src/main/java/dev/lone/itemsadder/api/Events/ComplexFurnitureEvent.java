package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.api.CustomComplexFurniture;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import lombok.Generated;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

abstract class ComplexFurnitureEvent extends Event {
   @Nullable
   private final Player player;
   @Nullable
   final LivingEntity bukkitEntity;
   final CustomEntityImpl internal;

   public ComplexFurnitureEvent(@Nullable Player player, @Nullable LivingEntity bukkitEntity, CustomEntityImpl internal) {
      this.player = player;
      this.bukkitEntity = bukkitEntity;
      this.internal = internal;
   }

   @Nullable
   public CustomComplexFurniture getFurniture() {
      return CustomComplexFurniture.byAlreadySpawned(this.bukkitEntity);
   }

   @Nullable
   public String getNamespacedID() {
      return this.internal == null ? null : this.internal.getNamespacedId();
   }

   @Nullable
   public Player getPlayer() {
      return this.player;
   }

   @Nullable
   @Generated
   public LivingEntity getBukkitEntity() {
      return this.bukkitEntity;
   }
}
