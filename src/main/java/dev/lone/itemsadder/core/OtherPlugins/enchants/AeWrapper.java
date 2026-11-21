package dev.lone.itemsadder.Core.OtherPlugins.enchants;

import beer.devs.fastnbt.nms.nbt.NItem;
import dev.lone.itemsadder.Core.OtherPlugins.enchants.AeWrapper.PendingEnchantException;
import dev.lone.itemsadder.item.CustomItemDefinition;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.advancedplugins.ae.api.AEAPI;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class AeWrapper {
   @Nullable
   public static String applyEnchant(ItemStack item, String enchant, int level) {
      try {
         NItem var3 = new NItem(item);
         var3.merge(AEAPI.applyEnchant(enchant, level, item));
         var3.save();
         return null;
      } catch (Exception var4) {
         return var4.getMessage();
      }
   }

   public static void applyEnchant(CustomItemDefinition item, String enchant, int level) {
      try {
         item.Dt().merge(AEAPI.applyEnchant(enchant, level, item));
         item.method8769();
      } catch (Throwable var4) {
         throw new RuntimeException(var4);
      }
   }

   public static boolean isCustomEnchant(String enchant) {
      return AEAPI.isAnEnchantment(enchant);
   }

   public static void requireCustomEnchant(String enchant) {
      Plugin var1 = Bukkit.getPluginManager().getPlugin("AdvancedEnchantments");
      if (var1 == null || !var1.isEnabled()) {
         throw new IllegalStateException("AdvancedEnchantments must be enabled to apply enchant '" + enchant + "'");
      }

      if (!AEAPI.isAnEnchantment(enchant)) {
         throw new PendingEnchantException(enchant);
      }
   }

   public static void awaitRegistration(PendingEnchantException pending, Plugin owner, BooleanSupplier current, Consumer completion) {
      new EnchantPollTask(current, pending, completion).runTaskTimer(owner, 1L, 1L);
   }
}
