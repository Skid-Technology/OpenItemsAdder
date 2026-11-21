package dev.lone.itemsadder.api.FontImages;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.PlayerEffectState;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PlayerHudsHolderWrapper {
   private final Player player;
   private final PlayerEffectState internal;

   public PlayerHudsHolderWrapper(Player player) {
      this.player = player;
      this.internal = (PlayerEffectState)ContentManager.method8084().aH.acB.get(player);
   }

   @Nullable
   public PlayerEffectState getInternal() {
      return this.internal;
   }

   public boolean exists() {
      return this.internal != null;
   }

   public Player getPlayer() {
      return this.player;
   }

   public void recalculateOffsets() {
      this.internal.recalculateOffsets();
   }

   public void sendUpdate() {
      this.internal.An();
   }
}
