package dev.lone.itemsadder.api;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public class PlayerEquipEvent extends Event {
   public final Player anl;
   public final Entity entity;
   public final boolean anm;
   public final EquipmentSlot ann;

   PlayerEquipEvent(Player var1, Entity var2, boolean var3, EquipmentSlot var4) {
      this.anl = var1;
      this.entity = var2;
      this.anm = var3;
      this.ann = var4;
   }

   public static PlayerEquipEvent method9988(Player var0, Entity var1, boolean var2, EquipmentSlot var3) {
      return new PlayerEquipEvent(var0, var1, var2, var3);
   }

   @NotNull
   public HandlerList getHandlers() {
      throw new RuntimeException("DUMMY EVENT TRIGGERED!");
   }
}
