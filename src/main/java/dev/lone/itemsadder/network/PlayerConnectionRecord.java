package dev.lone.itemsadder.network;

import dev.lone.itemsadder.entity.RotationPolicy;
import org.bukkit.entity.Player;

class PlayerConnectionRecord {
   private final ConnectionRecord blI;
   private final Player blJ;
   private final RotationPolicy blK;

   private PlayerConnectionRecord(ConnectionRecord var1, Player var2, RotationPolicy var3) {
      this.blI = var1;
      this.blJ = var2;
      this.blK = var3;
   }

   public ConnectionRecord kQ() {
      return this.blI;
   }

   public Player dJ() {
      return this.blJ;
   }

   public RotationPolicy kV() {
      return this.blK;
   }
}
