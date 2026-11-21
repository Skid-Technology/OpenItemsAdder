package dev.lone.itemsadder.item;

import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.resourcepack.ArmorTextureBuilder;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

public class ArmorItemHelper {
   public static boolean isLegacyArmorConfig(ConfigSectionWrapper var0) {
      return var0.hA("specific_properties.armor") || var0.hA("equipment.legacy_armor_rendering_id");
   }

   public static ArmorConversionResult createLegacyArmor(Material var0, ConfigSectionWrapper var1, YamlConfig var2, String var3, String var4) {
      Material var6 = var0;
      var0 = resolveLegacyArmorMaterial(var0, var1);
      if (var6 != null && var6 != var0) {
         Log.warn(
            "[Items] '"
               + var3
               + ":"
               + var4
               + "'. Warning: Trying to create a custom legacy armor using an incompatible material. '"
               + var6
               + "' is not a leather armor material. File: "
               + var2.DJ()
         );
      }

      boolean var7 = false;
      Color var8 = Utils.parseBukkitColor(var1.bc("specific_properties.armor.color", null));
      ColoredItemDefinition var5 = new ColoredItemDefinition(var2, var3, var4, var0, var8, var1.method7078("specific_properties.armor.can_wear", true));
      String var9 = var1.bc("specific_properties.armor.custom_armor", var1.bc("equipment.legacy_armor_rendering_id", null));
      if (var9 != null) {
         if (!var9.contains(":")) {
            var9 = var3 + ":" + var9;
         }

         LeatherColorDefinition var10 = (LeatherColorDefinition)ContentManager.method8084().ae.get(var9);
         if (var10 != null) {
            if (Settings.get().logArmorsLoading) {
               Log.log(ChatColor.GREEN + "Set custom_armor '" + var9 + "' for item '" + var5.getNamespacedID() + "'. File: " + var5.DJ());
            }

            var5.method8843(var10);
         } else {
            Log.error("[Items] '" + var5.getNamespacedID() + "'. Error: Unknown custom_armor '" + var9 + "'. File: " + var5.DJ());
         }
      }

      if (var5.De() == null && var8 != null) {
         int var14 = var8.asRGB();
         HashMap var11 = (HashMap)ContentManager.method8084().field98.get(var5.anu);
         if (var11 != null && var11.containsKey(var14)) {
            if (Settings.get().logArmorsLoading) {
               Log.warn(
                  ChatColor.YELLOW
                     + "Armor piece "
                     + var5.anu
                     + " "
                     + ((CustomItemDefinition)var11.get(var14)).getNamespacedID()
                     + " is using the same color as "
                     + var5.getNamespacedID()
                     + ". Trying to generate a new similar color..."
               );
            }

            Integer var12 = ArmorTextureBuilder.findFreeRgbKey(var14, var11);
            if (var12 == null) {
               if (Settings.get().logArmorsLoading) {
                  Log.error(
                     ChatColor.RED
                        + "Failed to find a proper color for armor piece "
                        + var5.anu
                        + " "
                        + var5.getNamespacedID()
                        + ". Decide another color. File: "
                        + var2.DJ()
                  );
               }
            } else {
               if (Settings.get().logArmorsLoading) {
                  Log.error(ChatColor.GREEN + "Fixed color for armor '" + var5.getNamespacedID() + "'. File: " + var2.DJ());
               }

               var5.setColor(Utils.intToBukkitColor(var12));
               var1.method7074("specific_properties.armor.color", Utils.intToHex(var12));
               ContentManager.method8084().field98.computeIfAbsent(var5.anu, var0x -> new HashMap<>()).put(var12, var5);
               var7 = true;
            }
         } else {
            ContentManager.method8084().field98.computeIfAbsent(var5.anu, var0x -> new HashMap<>()).put(var14, var5);
         }
      }

      return new ArmorConversionResult(var5, var7, var0);
   }

   static Material resolveLegacyArmorMaterial(Material var0, ConfigSectionWrapper var1) {
      if (var0 != null && ItemStackUtils.al(var0)) {
         return var0;
      } else {
         String var2 = var1.jI("equipment.slot") ? EquipmentSlotType.cR(var1.bc("equipment.slot", null)) : var1.bc("specific_properties.armor.slot", null);
         Material var3 = ColoredItemDefinition.method8844(EquipmentSlotType.cP(var2));
         if (var3 == null) {
            throw new IllegalArgumentException(
               "Invalid legacy armor slot '" + var2 + "': set a valid armor material or 'equipment.slot' (HEAD, CHEST, LEGS, FEET)."
            );
         } else {
            return var3;
         }
      }
   }
}
