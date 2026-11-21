package dev.lone.itemsadder.util;

import dev.lone.itemsadder.core.UpdateChecker;
import java.awt.Color;
import java.math.BigDecimal;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class ColorParserListener implements Listener {
   final Plugin plugin;

   public ColorParserListener(Plugin var1) {
      this.plugin = var1;
   }

   @EventHandler
   private void onPluginEnable(PluginEnableEvent var1) {
      if (var1.getPlugin().getName().equals(this.plugin.getName())) {
         Bukkit.getServer()
            .getScheduler()
            .runTaskLaterAsynchronously(
               this.plugin,
               () -> Bukkit.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> new UpdateChecker(this.plugin).method7589()),
               1200 * Utils.randomInt(60, 120)
            );
      }
   }

   public static void removeAllEnchantments(ItemStack var0) {
      for (Enchantment var2 : var0.getEnchantments().keySet()) {
         var0.removeEnchantment(var2);
      }
   }

   public static String dashesToSpaces(String var0) {
      return var0.replace("-", " ");
   }

   public static float parseFloatRange(String var0) {
      if (var0.contains("-")) {
         String[] var1 = var0.split("-");
         return roundFloat(randomFloatBetween(Float.parseFloat(var1[0]), Float.parseFloat(var1[1])), 2);
      } else {
         return Integer.parseInt(var0);
      }
   }

   public static int parseIntRange(String var0) {
      if (var0.contains("-")) {
         String[] var1 = var0.split("-");
         return randomIntBetween(Integer.parseInt(var1[0]), Integer.parseInt(var1[1]));
      } else {
         return Integer.parseInt(var0);
      }
   }

   public static float roundFloat(float var0, int var1) {
      BigDecimal var2 = new BigDecimal(Float.toString(var0));
      var2 = var2.setScale(var1, 4);
      return var2.floatValue();
   }

   public static float randomFloatBetween(float var0, float var1) {
      Random var2 = new Random();
      float var3 = var2.nextFloat() * (var1 - var0) + var0;
      return var2.nextFloat() * (var1 - var0) + var0;
   }

   public static int randomIntBetween(int var0, int var1) {
      Random var2 = new Random();
      return var2.nextInt(var1 - var0 + 1) + var0;
   }

   public static boolean rollPercent(int var0) {
      int var1 = randomIntBetween(1, 100);
      return var1 <= var0;
   }

   public static boolean hasPermissionOrOp(Player var0, String var1) {
      return var0.hasPermission(var1) ? true : var0.isOp();
   }

   public static String sectionToAmpersand(String var0) {
      return var0.replace("§", "&");
   }

   public static String ampersandToSection(String var0) {
      return var0.replace("&", "§");
   }

   public static boolean isNumeric(String var0) {
      return var0.matches("\\d+");
   }

   public static int[] parseHexTriplet(String var0) {
      int[] var1 = new int[3];

      for (int var2 = 0; var2 < 3; var2++) {
         var1[var2] = Integer.parseInt(var0.substring(var2 * 2, var2 * 2 + 2), 16);
      }

      return var1;
   }

   public static int parseReversedHex(String var0) {
      try {
         return Integer.parseInt(new StringBuffer(var0).reverse().toString(), 16);
      } catch (NumberFormatException var2) {
         return 0;
      }
   }

   private static Color parseHexColor(String var0) {
      return new Color(Integer.valueOf(var0.substring(1, 3), 16), Integer.valueOf(var0.substring(3, 5), 16), Integer.valueOf(var0.substring(5, 7), 16));
   }

   public static org.bukkit.Color toBukkitColor(String var0) {
      if (var0.endsWith(".0")) {
         StringBuilder var1 = new StringBuilder(var0.replace(".0", ""));

         while (var1.length() < 7) {
            var1.insert(0, "0");
         }

         var0 = var1.toString();
      }

      if (!var0.startsWith("#")) {
         var0 = "#" + var0;
      }

      Color var2 = parseHexColor(var0);
      return org.bukkit.Color.fromRGB(var2.getRed(), var2.getGreen(), var2.getBlue());
   }

   public static int ceilDivide(float var0, float var1) {
      return (int)Math.ceil(var0 / var1);
   }

   public static ItemStack withDisplayName(ItemStack var0, String var1) {
      ItemMeta var2 = var0.getItemMeta();
      var2.setDisplayName(var1);
      var0.setItemMeta(var2);
      return var0;
   }
}
