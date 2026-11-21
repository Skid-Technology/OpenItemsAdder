package dev.lone.itemsadder.event;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.NamespacedElementWithEffects;
import dev.lone.itemsadder.util.Log;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelEffect implements PlayerEventEffect, Cloneable {
   public final float ach;

   public FoodLevelEffect(float var1) {
      this.ach = var1;
   }

   public FoodLevelEffect zX() {
      return new FoodLevelEffect(this.ach);
   }

   @Override
   public void method322(NamespacedElementWithEffects var1, Player var2, FoodLevelChangeEvent var3) {
      if (var2.getGameMode() != GameMode.CREATIVE) {
         if (var3.getFoodLevel() < var2.getFoodLevel()) {
            int var4 = var2.getFoodLevel() - var3.getFoodLevel();
            float var5 = var1.zP();
            if (var5 - this.ach * var4 < var1.zR()) {
               if (var5 > 0.0F) {
                  var1.method499(var1.zR());
                  if (Settings.get().isLoggingEnabled("logging.huds.triggers")) {
                     Log.log("DecrementBasedOnFoodLevel " + this.ach);
                  }
               }
            } else {
               var1.method499(var5 - this.ach * var4);
               if (Settings.get().isLoggingEnabled("logging.huds.triggers")) {
                  Log.log("DecrementBasedOnFoodLevel " + this.ach * var4);
               }
            }

            ContentManager.method8084().aH.az(var2);
         }
      }
   }
}
