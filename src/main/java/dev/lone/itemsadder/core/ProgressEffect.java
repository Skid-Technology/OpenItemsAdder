package dev.lone.itemsadder.core;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.util.Log;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class ProgressEffect implements ProgressEffectAction {
   public final int acu;
   public final float acv;
   public final int acw;
   public final int acx;
   protected int acp = 0;

   public ProgressEffect(int var1, float var2, int var3, int var4) {
      this.acu = var1;
      this.acv = var2;
      this.acw = var3;
      this.acx = var4;
   }

   public ProgressEffect Af() {
      return new ProgressEffect(this.acu, this.acv, this.acw, this.acx);
   }

   @Override
   public void method9642(NamespacedElementWithEffects var1, Player var2, int var3) {
      if (this.method9641(var1, var2, var3)) {
         this.method9643(var1, var2, var3);
      }
   }

   boolean method9641(NamespacedElementWithEffects var1, Player var2, int var3) {
      if (var2.getGameMode() == GameMode.CREATIVE) {
         return false;
      }

      if (!var1.method492(var2.getWorld())) {
         return false;
      }

      this.acp += var3;
      if (this.acp < this.acu) {
         return false;
      }

      this.acp = 0;
      return this.acw == -1
         || this.acx == -1
         || var2.getLocation().getBlock().getLightFromSky() >= this.acw && var2.getLocation().getBlock().getLightFromSky() <= this.acx;
   }

   void method9643(NamespacedElementWithEffects var1, Player var2, int var3) {
      float var4 = var1.zP();
      var1.method499(var4 + this.acv);
      ContentManager.method8084().aH.az(var2);
      if (Settings.get().isLoggingEnabled("logging.huds.triggers")) {
         Log.log("GenericOverTime " + this.acv);
      }
   }
}
