package dev.lone.itemsadder.config;

import dev.lone.itemsadder.item.CustomItemDataEntry;
import dev.lone.itemsadder.resourcepack.AssetPathResolver;
import dev.lone.itemsadder.util.FileListHelper;
import dev.lone.itemsadder.util.Log;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;

public final class ConfigMerger {
   private ConfigMerger() {
   }

   static void method9229(Collection var0, Map var1, List var2, List var3) {
      for (YamlConfig var5 : var0) {
         if (var5.contains("equipments")) {
            String var6 = var5.getString("info.namespace", null);
            if (var6 != null) {
               ConfigurationSection var7 = var5.kb("equipments");
               assert var7 != null;

               for (String var9 : var7.getKeys(false)) {
                  if (!var7.getString(var9 + ".renderer", "armor").equals("armor")) {
                     Log.warn(
                        "For now only 'armor' renderer is supported for equipments. More will be implemented in the future. Skipping equipment '"
                           + var9
                           + "'. File: "
                           + var5.DJ()
                     );
                  } else {
                     NamespacedKey var10 = new NamespacedKey(var6, var9);
                     String var11 = YamlConfig.method7038(
                        var7.getString(var9 + ".layer_1"),
                        false,
                        () -> Log.error("Missing 'layer_1' property for equipment '" + var9 + "'. File: " + var5.DJ())
                     );
                     if (var11 != null) {
                        String var12 = YamlConfig.method7038(
                           var7.getString(var9 + ".layer_2"),
                           false,
                           () -> Log.error("Missing 'layer_2' property for equipment '" + var9 + "'. File: " + var5.DJ())
                        );
                        if (var12 != null) {
                           if (FileListHelper.findAssetFile(var2, var3, AssetPathResolver.textureAssetPath(var6, var11)) == null) {
                              Log.error("Can't find layer_1 '" + var11 + "' texture file for equipment '" + var9 + "'. File: " + var5.DJ());
                           } else if (FileListHelper.findAssetFile(var2, var3, AssetPathResolver.textureAssetPath(var6, var12)) == null) {
                              Log.error("Can't find layer_2 '" + var12 + "' texture file for equipment '" + var9 + "'. File: " + var5.DJ());
                           } else {
                              if (!var11.contains(":")) {
                                 var11 = var6 + ":" + var11;
                              }

                              if (!var12.contains(":")) {
                                 var12 = var6 + ":" + var12;
                              }

                              var1.put(var10, new CustomItemDataEntry(var10, var11, var12));
                              Log.log(
                                 ChatColor.AQUA + "[Items] Registered equipment '" + var9 + "' with layers: " + var11 + ", " + var12 + ". File: " + var5.DJ()
                              );
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
