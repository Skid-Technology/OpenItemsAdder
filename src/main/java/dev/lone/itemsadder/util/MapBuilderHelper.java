package dev.lone.itemsadder.util;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.item.LeatherColorDefinition;
import dev.lone.itemsadder.resourcepack.ArmorTextureBuilder;
import java.util.Collection;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;

public final class MapBuilderHelper {
   private MapBuilderHelper() {
   }

   static void buildLookup(Collection var0, HashMap var1, HashMap var2) {
      for (YamlConfig var4 : var0) {
         if (var4.contains("armors_rendering") || var4.contains("legacy_armor_renderings")) {
            boolean var5 = false;
            String var6 = var4.getString("info.namespace", null);
            ConfigurationSection var7 = var4.method7066("armors_rendering", "legacy_armor_renderings");

            for (String var9 : var7.getKeys(false)) {
               Color var10 = Utils.parseBukkitColor(var7.getString(var9 + ".color", null));
               if (var10 == null) {
                  Log.error(ChatColor.RED + "'color' property missing for legacy_armor_rendering '" + var9 + "'. File: " + var4.DJ());
               } else {
                  int var11 = var10.asRGB();
                  if (var1.containsKey(var11)) {
                     Log.warn(
                        ChatColor.YELLOW
                           + "legacy_armor_rendering '"
                           + var9
                           + "' is using the same color as another render '"
                           + ((LeatherColorDefinition)var1.get(var11)).getNamespacedID()
                           + "'. Trying to generate a new similar color..."
                     );
                     Integer var12 = ArmorTextureBuilder.findFreeRgbKey(var11, var1);
                     if (var12 == null) {
                        Log.error(
                           ChatColor.RED + "Failed to find a proper color for legacy_armor_rendering '" + var9 + "'. Decide another color. File: " + var4.DJ()
                        );
                     } else {
                        Log.error(ChatColor.GREEN + "Fixed color for armors_render '" + var9 + "'. ");
                        var11 = var12;
                        var10 = Utils.intToBukkitColor(var11);
                        if (var4.contains("legacy_armor_rendering." + var9)) {
                           var4.method7048("legacy_armor_rendering." + var9 + ".color", Utils.intToHex(var11));
                        } else if (var4.contains("armors_rendering." + var9)) {
                           var4.method7048("armors_rendering." + var9 + ".color", Utils.intToHex(var11));
                        }

                        var5 = true;
                     }
                  }

                  LeatherColorDefinition var13 = new LeatherColorDefinition(
                     var4,
                     var6,
                     var9,
                     var10,
                     var7.getString(var9 + ".layer_1", null),
                     var7.getString(var9 + ".layer_2", null),
                     var7.getString(var9 + ".emissive_1", null),
                     var7.getString(var9 + ".emissive_2", null),
                     var7.getBoolean(var9 + ".use_color", false),
                     var7.getInt(var9 + ".animation.speed", 24),
                     var7.getBoolean(var9 + ".animation.interpolation", false),
                     var7.getInt(var9 + ".emissive_type", 1)
                  );
                  var1.put(var11, var13);
                  var2.put(var13.getNamespacedID(), var13);
                  if (Settings.get().logArmorsLoading) {
                     Log.log(ChatColor.AQUA + "[Items] Registered legacy_armor_rendering '" + var9 + "', color " + var13.apE);
                  }
               }
            }

            if (var5) {
               var4.save();
               var4.reload();
            }
         }
      }
   }
}
