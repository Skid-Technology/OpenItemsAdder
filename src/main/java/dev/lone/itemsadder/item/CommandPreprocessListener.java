package dev.lone.itemsadder.item;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CommandPreprocessListener implements Listener {
   public void register(Plugin plugin) {
      EventRegistrar.registerIfAbsent(this, plugin);
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method2402(PlayerCommandPreprocessEvent var1) {
      String var2 = var1.getMessage();
      Pattern var3 = Pattern.compile("/(?:[^:]+:enchant|enchant)(.*)");
      if (var3.matcher(var2).matches()) {
         Player var4 = var1.getPlayer();
         SchedulerUtils.runLaterSync(() -> {
            ItemStack var1x = var4.getInventory().getItemInMainHand();
            CustomItemDefinition var2x = ContentManager.method8084().method8149(var1x);
            if (var2x != null) {
               boolean var3x = false;
               if (var2x.isBlockAllEnchants()) {
                  if (ItemStackUtils.aD(var1x)) {
                     var3x = true;
                  }
               } else if (var2x.Dw() != null) {
                  for (Enchantment var5 : var2x.Dw()) {
                     if (ItemStackUtils.method7220(var1x, var5)) {
                        var3x = true;
                     }
                  }
               }

               if (var3x) {
                  Log.errorToSender(var4, ChatColor.RED + "You can't add this enchantment to this custom item.");
               }
            }
         }, 5L);
      }
   }
}
