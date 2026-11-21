package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class SoundEffect extends ItemEffect {
   final String aiX;
   final float aiY;

   public SoundEffect(String var1, float var2) {
      this.aiX = var1;
      this.aiY = var2;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      float var5 = ContentManager.method8084().aG.method1429(var4, this.aiX, 0.0F);
      var5 -= this.aiY;
      ContentManager.method8084().aH.method876(var4, this.aiX, var5);
      if (Settings.get().isLoggingEnabled("logging.huds.triggers")) {
         Log.log("DecrementPlayerStatValue " + this.aiX + ": " + this.aiY);
      }

      return EffectOutcome.failure();
   }
}
