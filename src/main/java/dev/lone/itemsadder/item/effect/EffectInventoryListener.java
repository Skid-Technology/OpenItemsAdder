package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.util.EventRegistrar;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class EffectInventoryListener implements Listener {
   private final Map alc = new HashMap();

   public void register() {
      EventRegistrar.registerListener(this, Main.getInstance());
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onInventoryClick(InventoryClickEvent var1) {
      if (var1.getWhoClicked() instanceof Player var2) {
         if (var1.getClick() != ClickType.NUMBER_KEY || var1.getAction() != InventoryAction.HOTBAR_SWAP || var1.getSlotType() != SlotType.QUICKBAR) {
            return;
         }

         this.flushQueue(var2);
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onSwapHandItems(PlayerSwapHandItemsEvent var1) {
      this.flushQueue(var1.getPlayer());
   }

   private void flushQueue(Player var1) {
      if (Settings.get().kickOnQuickbarSpam) {
         long var2 = System.currentTimeMillis();
         EffectQueue var4 = this.alc.computeIfAbsent(var1, var0 -> new EffectQueue());

         while (!var4.ald.isEmpty() && var2 - var4.ald.peek() > Settings.get().quickbarSpamWindowMs) {
            var4.ald.poll();
         }

         var4.ald.add(var2);
         if (var4.ald.size() > Settings.get().quickbarSpamClicks) {
            var1.kickPlayer("Kicked for spamming quickbar clicks.");
            this.alc.remove(var1);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   private void onPlayerQuit(PlayerQuitEvent var1) {
      this.alc.remove(var1.getPlayer());
   }
}
