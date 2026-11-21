package dev.lone.itemsadder.Core.OtherPlugins.enchants;

import dev.lone.itemsadder.Core.OtherPlugins.enchants.AeWrapper.PendingEnchantException;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.advancedplugins.ae.api.AEAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

class EnchantPollTask extends BukkitRunnable {
   private int ticks;

   EnchantPollTask(BooleanSupplier var1, PendingEnchantException var2, Consumer var3) {
      this.val$current = var1;
      this.val$pending = var2;
      this.val$completion = var3;
   }

   public void run() {
      if (!this.val$current.getAsBoolean()) {
         this.cancel();
      } else {
         boolean var1 = false;
         RuntimeException var2 = null;

         try {
            Plugin var3 = Bukkit.getPluginManager().getPlugin("AdvancedEnchantments");
            if (var3 == null || !var3.isEnabled()) {
               throw new IllegalStateException("AdvancedEnchantments was disabled while waiting for '" + this.val$pending.enchant + "'");
            }

            if (AEAPI.isAnEnchantment(this.val$pending.enchant)) {
               var1 = true;
            } else if (++this.ticks >= 1200) {
               var1 = true;
               var2 = new IllegalStateException(
                  "AdvancedEnchantments did not register '"
                     + this.val$pending.enchant
                     + "' within 1200 ticks. Check the enchant name and AdvancedEnchantments startup errors, then run /iareload."
               );
            }
         } catch (RuntimeException | LinkageError var4) {
            var1 = true;
            var2 = var4;
         }

         if (var1) {
            this.cancel();
            this.val$completion.accept(var2);
         }
      }
   }
}
