package dev.lone.itemsadder.item.effect;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EventRegistrar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public final class ElytraBoostListener implements IaListener {
   private final GeneralItemEffectListener alX;

   public ElytraBoostListener(GeneralItemEffectListener var1) {
      this.alX = var1;
      EventRegistrar.registerIfAbsent(this, Main.getInstance());
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onElytraBoost(PlayerElytraBoostEvent var1) {
      EffectFlags var2 = this.alX.resolveFlags(var1.getPlayer(), var1.getItemStack(), var1);
      if (var2.CH()) {
         if (!var2.CI()) {
            var1.setCancelled(true);
         } else {
            var1.setShouldConsume(var2.CJ());
         }
      }
   }
}
