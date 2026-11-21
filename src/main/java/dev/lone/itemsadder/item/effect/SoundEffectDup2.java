package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class SoundEffectDup2 extends ItemEffect {
   final String aju;
   final float ajv;

   public SoundEffectDup2(String var1, float var2) {
      this.aju = var1;
      this.ajv = var2;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      float var5 = ContentManager.method8084().aG.method1429(var4, this.aju, 0.0F);
      float var6 = var5 + this.ajv;
      ContentManager.method8084().aH.method876(var4, this.aju, var6);
      if (Settings.get().isLoggingEnabled("logging.huds.triggers")) {
         Log.log("IncrementPlayerStatValue " + this.aju + ": " + this.ajv);
      }

      return var5 != var6 ? EffectOutcome.failure() : EffectOutcome.success();
   }
}
