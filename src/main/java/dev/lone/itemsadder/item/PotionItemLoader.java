package dev.lone.itemsadder.item;

import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class PotionItemLoader {
   public static boolean hasPotionEffects(CustomItemDefinition var0) {
      return var0.getType() == Material.POTION || var0.getType() == Material.SPLASH_POTION || var0.getType() == Material.LINGERING_POTION;
   }

   public static void loadPotionEffects(CustomItemDefinition var0, ConfigSectionWrapper var1, YamlConfig var2) {
      String var3 = var1.bc("specific_properties.potion.type", null);
      PotionType var4 = null;
      if (var3 != null) {
         var4 = YamlConfig.jE(var3);
         if (var4 == null) {
            Log.error("[Items] '" + var0.getNamespacedID() + "'. Unknown 'type: '" + var3 + "' for potion. File: " + var2.DJ());
         }
      }

      String var5 = var1.bc("specific_properties.potion.color", "WHITE");
      int var6 = Utils.colorToRgbInt(var5);
      if (var6 == -1) {
         Log.error("[Items] '" + var0.getNamespacedID() + "'. Missing potion.color '" + var5 + ". File: " + var2.DJ());
      }

      PotionMeta var7 = (PotionMeta)var0.getItemMeta();
      var7.clearCustomEffects();
      var7.setBasePotionType(var4);
      if (var1.jI("specific_properties.potion.effects")) {
         ConfigurationSection var8 = var1.kb("specific_properties.potion.effects");

         for (String var10 : var8.getKeys(false)) {
            String var11 = var8.getString(var10 + ".type", null);
            if (var11 == null) {
               Log.error("[Items] '" + var0.getNamespacedID() + "'. Missing 'type' for custom potion effect. File: " + var2.DJ());
            } else {
               PotionEffectType var12 = null;
               if (!var11.contains(":")) {
                  var12 = YamlConfig.jU(var11);
               } else {
                  String[] var13 = var11.split(":");
                  var12 = PotionEffectType.getByKey(new NamespacedKey(var13[0], var13[1]));
               }

               if (var12 == null) {
                  Log.error("[Items] '" + var0.getNamespacedID() + "'. Unknown 'type' for custom potion effect. File: " + var2.DJ());
               } else {
                  int var19 = var8.getInt(var10 + ".duration", 0);
                  int var14 = var8.getInt(var10 + ".amplifier", 0);
                  boolean var15 = var8.getBoolean(var10 + ".ambient", false);
                  boolean var16 = var8.getBoolean(var10 + ".particles", true);
                  boolean var17 = var8.getBoolean(var10 + ".icon", true);
                  var7.addCustomEffect(new PotionEffect(var12, var19, var14, var15, var16, var17), false);
               }
            }
         }
      } else {
         var7.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ADDITIONAL_TOOLTIP});
      }

      if (var6 != -1) {
         var7.setColor(Color.fromRGB(var6));
      }

      var0.setItemMeta(var7);
      if (var0.getType() == Material.POTION) {
         ItemOperationsService.getRegistryBridge().aj(var0);
      }
   }
}
