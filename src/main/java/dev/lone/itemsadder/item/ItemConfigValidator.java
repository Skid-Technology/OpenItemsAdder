package dev.lone.itemsadder.item;

import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.util.Log;

public class ItemConfigValidator {
   public static boolean method10069(ConfigSectionWrapper var0) {
      return var0.hA("durability");
   }

   public static boolean method10070(CustomItemDefinition var0, ConfigSectionWrapper var1, YamlConfig var2, String var3, String var4) {
      boolean var5 = var1.method7078("durability.disappear_when_broken", true);
      boolean var6 = var1.method7078("durability.unbreakable", false);
      boolean var7 = var1.jI("durability.durability") || var1.jI("durability.custom_durability");
      boolean var8 = var1.jI("durability.max_durability") || var1.jI("durability.max_custom_durability");
      if (!var7 && !var8) {
         var0.method8795(null, null, var5, var6);
      } else {
         int var9 = var1.method7076("durability.max_durability", var1.method7076("durability.max_custom_durability", var0.getType().getMaxDurability()));
         int var10 = var1.method7076("durability.custom_durability", var1.method7076("durability.durability", var9));
         if (var8 && !var7) {
            var10 = var9;
         }

         if (var7 && !var8) {
            var9 = ItemStackUtils.method7233(var0);
         }

         if (var9 <= 0) {
            Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: 'max_durability' must be > 0. File: " + var2.DJ());
            return false;
         }

         if (var10 < 0) {
            Log.error("[Items] '" + var3 + ":" + var4 + "' - Error: 'durability' must >= 0. File: " + var2.DJ());
            return false;
         }

         if (var10 > var9) {
            Log.error("[Items] '" + var3 + ":" + var4 + "' - Error: 'durability' must be <= 'max_durability'. File: " + var2.DJ());
            return false;
         }

         var0.method8795(var10, var9, var5, var6);
      }

      return true;
   }
}
