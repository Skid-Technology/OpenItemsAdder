package dev.lone.itemsadder.nms;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public interface GameModeAdapter {
   void method3423(Player var1, float var2);

   default void method3424(Player var1, GameMode var2) {
      this.method3423(var1, var2.getValue());
   }

   default void cv(Player var1) {
      this.method3424(var1, var1.getGameMode());
   }

   void cw(Player var1);

   void method3425(Player var1, boolean var2, boolean var3, float var4);
}
