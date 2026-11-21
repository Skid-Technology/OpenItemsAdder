package dev.lone.itemsadder.event;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.NamespacedElementWithEffects;
import dev.lone.itemsadder.util.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnEffect implements PlayerEventEffect {
   public final float acj;

   public RespawnEffect(float var1) {
      this.acj = var1;
   }

   public RespawnEffect Aa() {
      return new RespawnEffect(this.acj);
   }

   @Override
   public void method382(NamespacedElementWithEffects var1, Player var2, PlayerRespawnEvent var3) {
      float var4 = var1.zP();
      float var5 = var4 + this.acj;
      if (var5 > var1.zS()) {
         var5 = var1.zS();
      }

      var1.method499(var5);
      if (Settings.get().isLoggingEnabled("logging.huds.triggers")) {
         Log.log("IncrementOnPlayerRespawn " + var5);
      }

      ContentManager.method8084().aH.az(var2);
   }
}
