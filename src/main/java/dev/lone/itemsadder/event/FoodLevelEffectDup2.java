package dev.lone.itemsadder.event;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.NamespacedElementWithEffects;
import dev.lone.itemsadder.util.Log;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelEffectDup2 implements PlayerEventEffect, Cloneable {
   public final float aci;

   public FoodLevelEffectDup2(float var1) {
      this.aci = var1;
   }

   public FoodLevelEffectDup2 zZ() {
      return new FoodLevelEffectDup2(this.aci);
   }

   @Override
   public void method322(NamespacedElementWithEffects var1, Player var2, FoodLevelChangeEvent var3) {
      if (var2.getGameMode() != GameMode.CREATIVE) {
         if (var2.getFoodLevel() < var3.getFoodLevel()) {
            int var4 = var3.getFoodLevel() - var2.getFoodLevel();
            float var5 = var1.zP();
            float var6 = var5 + this.aci * var4;
            if (var6 > var1.zS()) {
               var6 = var1.zS();
            }

            var1.method499(var6);
            if (Settings.get().isLoggingEnabled("logging.huds.triggers")) {
               Log.log("IncrementBasedOnFoodLevel " + this.aci * var4);
            }

            ContentManager.method8084().aH.az(var2);
         }
      }
   }
}
