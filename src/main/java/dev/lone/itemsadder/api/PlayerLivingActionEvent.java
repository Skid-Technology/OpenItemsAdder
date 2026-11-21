package dev.lone.itemsadder.api;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerLivingActionEvent extends Event {
   public final Player anj;
   public final LivingEntity ank;

   public PlayerLivingActionEvent(Player var1, LivingEntity var2) {
      this.anj = var1;
      this.ank = var2;
   }

   @NotNull
   public HandlerList getHandlers() {
      throw new RuntimeException("DUMMY EVENT TRIGGERED!");
   }
}
