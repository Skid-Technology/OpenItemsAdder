package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.Version;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.util.JsonHelper;
import dev.lone.itemsadder.util.Log;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import org.apache.commons.io.FileUtils;

public class ItemNbtLoader {
   public static boolean hasNbtComponents(ConfigSectionWrapper var0) {
      return var0.hA("components_nbt_file") || var0.hA("nbt");
   }

   public static void loadNbtComponents(CustomItemDefinition var0, ConfigSectionWrapper var1, YamlConfig var2) {
      if (var1.jI("nbt") && var1.jI("components_nbt_file")) {
         Log.warn("[Items] '" + var0.getNamespacedID() + "'. Both 'nbt' and 'components_nbt_file' are set. 'nbt' will be ignored. File: " + var2.DJ());
      }

      String var3 = var1.jO("components_nbt_file");
      if (var3 != null) {
         File var4 = new File(var2.getFile().getParent(), var3);
         if (var4.exists()) {
            String var5 = null;

            try {
               var5 = FileUtils.readFileToString(var4, StandardCharsets.UTF_8);
            } catch (IOException var10) {
               Log.error("[Items] '" + var0.getNamespacedID() + "'. Error reading 'components_nbt_file' property. File: " + var2.DJ());
               Log.error("Error: " + var10.getMessage());
            }

            if (var5 != null) {
               try {
                  JsonHelper.validateJson(var5);
               } catch (ExecutionException var9) {
                  Log.error("[Items] '" + var0.getNamespacedID() + "'. Error parsing 'components_nbt_file' property. File: " + var2.DJ());
                  Log.error("Error: " + var9.getMessage());
               }

               try {
                  var0.cD(normalizeFoodComponent(var0, var5, var2));
               } catch (Exception var8) {
                  Log.error("[Items] '" + var0.getNamespacedID() + "'. Error injecting 'components_nbt_file' property. File: " + var2.DJ());
                  Log.error("Error: " + var8.getMessage());
               }
            }
         } else {
            Log.error("[Items] '" + var0.getNamespacedID() + "'. Error: 'components_nbt_file' not found. File: " + var2.DJ());
         }
      } else {
         String var11 = var1.bc("nbt", null);
         if (var11 != null) {
            try {
               var0.cD(normalizeFoodComponent(var0, var11, var2));
            } catch (Exception var7) {
               Log.error("[Items] '" + var0.getNamespacedID() + "'. Error injecting 'nbt' property. File: " + var2.DJ());
               Log.error("Error: " + var7.getMessage());
            }
         }
      }
   }

   private static String normalizeFoodComponent(CustomItemDefinition var0, String var1, YamlConfig var2) {
      ItemFeatureSpec var3 = var0.DY();
      int var4 = var3 != null ? var3.Ew() : 0;
      float var5 = var3 != null ? var3.Ex() : 0.0F;
      boolean var6 = Version.isNewerThan(Version.v1_21_1);
      String var7 = applyFoodDefaults(var1, var4, var5, var6);
      if (!var7.equals(var1)) {
         Log.warn(
            "[Items] '"
               + var0.getNamespacedID()
               + "'. Incomplete or version-incompatible 'food'/'consumable' component. Using nutrition="
               + var4
               + " and saturation="
               + var5
               + (var6 ? "." : "; unsupported 1.21.2+ consumable properties were ignored.")
               + " File: "
               + var2.DJ()
         );
      }

      return var7;
   }

   static String applyFoodDefaults(String var0, int var1, float var2, boolean var3) {
      try {
         JSONObject var4 = JSON.parseObject(var0);
         JSONObject var5 = var4.getJSONObject("components");
         if (var5 == null) {
            return var0;
         }

         JSONObject var6 = var5.getJSONObject("minecraft:food");
         if (var6 == null) {
            var6 = var5.getJSONObject("food");
         }

         boolean var7 = false;
         if (!var3) {
            JSONObject var8 = var5.getJSONObject("minecraft:consumable");
            String var9 = "minecraft:consumable";
            if (var8 == null) {
               var8 = var5.getJSONObject("consumable");
               var9 = "consumable";
            }

            if (var8 != null) {
               if (var6 == null) {
                  var6 = new JSONObject();
                  var5.put("minecraft:food", var6);
               }

               if (!var6.containsKey("eat_seconds") && var8.containsKey("consume_seconds")) {
                  var6.put("eat_seconds", var8.get("consume_seconds"));
               }

               var5.remove(var9);
               var7 = true;
            }
         }

         if (var6 == null) {
            return var7 ? JSON.toJSONString(var4) : var0;
         }

         if (!var6.containsKey("nutrition")) {
            var6.put("nutrition", var1);
            var7 = true;
         }

         if (!var6.containsKey("saturation")) {
            var6.put("saturation", var2);
            var7 = true;
         }

         return var7 ? JSON.toJSONString(var4) : var0;
      } catch (RuntimeException var10) {
         return var0;
      }
   }
}
