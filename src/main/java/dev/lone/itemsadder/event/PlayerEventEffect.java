package dev.lone.itemsadder.event;

import dev.lone.itemsadder.core.NamespacedElementWithEffects;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public interface PlayerEventEffect {
   default void method322(NamespacedElementWithEffects var1, Player var2, FoodLevelChangeEvent var3) {
   }

   PlayerEventEffect zY();

   default void method382(NamespacedElementWithEffects var1, Player var2, PlayerRespawnEvent var3) {
   }
}
