package dev.lone.itemsadder.api.Events;

import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.item.FurnitureItemEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;

abstract class FurnitureEvent extends PlayerEvent {
   final Entity bukkitEntity;
   final FurnitureItemEntity internal;

   public FurnitureEvent(Player player, Entity bukkitEntity, FurnitureItemEntity internal) {
      super(player);
      this.bukkitEntity = bukkitEntity;
      this.internal = internal;
   }

   @Nullable
   public CustomFurniture getFurniture() {
      return CustomFurniture.byAlreadySpawned(this.bukkitEntity);
   }

   @Nullable
   public String getNamespacedID() {
      return this.internal == null ? null : this.internal.aeT.aok;
   }

   public Entity getBukkitEntity() {
      return this.bukkitEntity;
   }
}
