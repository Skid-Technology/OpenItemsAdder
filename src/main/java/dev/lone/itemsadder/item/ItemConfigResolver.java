package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.Version;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.NamespacedId;
import dev.lone.itemsadder.resourcepack.ResourcePackWriter;
import dev.lone.itemsadder.util.CollectionUtils;
import dev.lone.itemsadder.util.ColorParser;
import dev.lone.itemsadder.util.IntTableConstantsT;
import dev.lone.itemsadder.util.Log;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ItemConfigResolver {
   public static boolean method10108(CustomItemDefinition var0, ConfigSectionWrapper var1, YamlConfig var2, String var3, String var4) {
      if (var1.hA("resource")) {
         method10111(var0, var1, var2, var3, var4);
      } else {
         if (var1.hA("graphics")) {
            return method10109(var0, var1, var2, var3, var4);
         }

         if (var1.hA("item_model")) {
            if (!Version.isAtLeast(Version.v1_21_4)) {
               Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: 'item_model' is only available in 1.21.4+. File: " + var2.DJ());
               return false;
            }

            String var5 = var1.jO("item_model");
            assert var5 != null;
            String[] var6 = var5.split(":");
            String var7 = var6.length == 2 ? var6[0] : var3;
            String var8 = "assets/" + var7 + "/items/" + var6[var6.length - 1] + ".json";
            File var9 = ContentManager.method8198(var8);
            if (var9 == null || !var9.exists()) {
               Log.error(
                  "[Items] '" + var3 + ":" + var4 + "'. Error: 'item_model' path '" + var8 + "' does not exist in any contents folder. File: " + var2.DJ()
               );
               return false;
            }

            var0.cC(var1.jO("item_model"));
         }
      }

      if (var0.anZ == null) {
         ContentManager.method8084().ar.put(var0.method8734(false, true), var0);
      }

      return true;
   }

   private static boolean method10109(CustomItemDefinition var0, ConfigSectionWrapper var1, YamlConfig var2, String var3, String var4) {
      if (var1.jI("graphics.model") && var1.jI("graphics.texture")) {
         Log.warn("[Items] '" + var3 + ":" + var4 + "'. Warning: 'graphics.model' and 'graphics.texture' are set at the same time. File: " + var2.DJ());
         return false;
      }

      Boolean var5 = var1.method7104("graphics.hand_animation_on_swap", null);
      Boolean var6 = var1.method7104("graphics.oversized_in_gui", null);
      String var7 = var1.bc("graphics.icon", null);
      if (var7 != null) {
         var7 = NamespacedId.method385(var7, var3);
      }

      Integer var8 = null;
      if (var1.jI("graphics.color")) {
         String var9 = var1.bc("graphics.color", null);
         if (var9 == null) {
            var9 = "WHITE";
         }

         var8 = ColorParser.parse(var9);
         if (var8 == null) {
            Log.error("[Items] '" + var0.getNamespacedID() + "'. Invalid 'color' '" + var9 + "'. File: " + var2.DJ());
         }
      }

      Integer var16 = null;
      if (var1.jI("graphics.color2")) {
         String var10 = var1.bc("graphics.color2", null);
         if (var10 == null) {
            var10 = "WHITE";
         }

         var16 = ColorParser.parse(var10);
         if (var16 == null) {
            Log.error("[Items] '" + var0.getNamespacedID() + "'. Invalid 'color2' '" + var10 + "'. File: " + var2.DJ());
         }
      }

      if (var16 != null && var8 == null) {
         var8 = var16;
         var16 = null;
      }

      if (var1.jI("graphics.model")) {
         String var17 = var1.jO("graphics.model");
         assert var17 != null;
         var17 = NamespacedId.method385(var17, var3);
         if (var17 == null) {
            Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: 'graphics.models' key '" + var17 + "' is invalid. File: " + var2.DJ());
            return false;
         }

         if (!ResourcePackWriter.checkPathValid(var17)) {
            Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: Invalid 'graphics.models' path '" + var17 + "'. File: " + var2.DJ());
            return false;
         }

         if (!var17.startsWith("minecraft:")) {
            if (ContentManager.method8200(var17) == null) {
               Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: Model '" + var17 + "' does not exist in any contents folder. File: " + var2.DJ());
               return false;
            }
         } else if (!ContentManager.method8084().field91.hasVanillaModel(var17)) {
            Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: Vanilla model '" + var17 + "' does not exist. File: " + var2.DJ());
            return false;
         }

         ItemSpecWithAttributes var11 = new ItemSpecWithAttributes(var0.getNamespacedID(), var0.getType(), var17, var7, var5, var6, var8, var16);
         var0.method8746(var11);
      } else if (var1.jI("graphics.models")) {
         HashMap var19 = new HashMap();
         ConfigSectionWrapper var21 = var1.kc("graphics.models");
         assert var21 != null;

         for (String var13 : var21.bJ(false)) {
            String var14 = var21.jO(var13);
            assert var14 != null;
            var14 = NamespacedId.method385(var14, var3);
            if (var14 == null) {
               Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: 'graphics.models' key '" + var14 + "' is invalid. File: " + var2.DJ());
            } else if (!ResourcePackWriter.checkPathValid(var14)) {
               Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: Invalid 'graphics.models' path '" + var14 + "'. File: " + var2.DJ());
            } else if (!ResourcePackWriter.isValidIdentifier(var13)) {
               Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: Invalid 'graphics.models' key '" + var13 + "'. File: " + var2.DJ());
            } else {
               if (!var14.startsWith("minecraft:")) {
                  if (ContentManager.method8200(var14) == null) {
                     Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: Model '" + var14 + "' does not exist in any contents folder. File: " + var2.DJ());
                     continue;
                  }
               } else if (!ContentManager.method8084().field91.hasVanillaModel(var14)) {
                  Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: Vanilla model '" + var14 + "' does not exist. File: " + var2.DJ());
                  continue;
               }

               var19.put(var13, var14);
            }
         }

         if (var19.isEmpty()) {
            return false;
         }

         ItemSpecWithAttributes var27 = new ItemSpecWithAttributes(var0.getNamespacedID(), var0.getType(), var19, var7, var5, var6, var8, var16);
         var0.method8746(var27);
      } else if (var1.jI("graphics.texture") || var1.jI("graphics.textures")) {
         HashMap var20 = new HashMap();
         if (var1.jI("graphics.texture")) {
            String var23 = var1.jO("graphics.texture");
            if (var23 != null) {
               var23 = NamespacedId.method385(var23, var3);
               if (var23 == null) {
                  Log.error(
                     "[Items] '" + var3 + ":" + var4 + "'. Error: 'graphics.texture' '" + var1.jO("graphics.texture") + "' is invalid. File: " + var2.DJ()
                  );
                  return false;
               }

               var23 = dl(var23);
               if (var0 instanceof CustomBlockDefinition) {
                  var20.put("all", var23);
               } else {
                  var20.put("normal", var23);
               }

               if (!var23.startsWith("minecraft:")) {
                  if (ContentManager.method8199(var23) == null) {
                     Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: Texture '" + var23 + "' does not exist in any contents folder. File: " + var2.DJ());
                     return false;
                  }
               } else if (!ContentManager.method8084().field91.hasVanillaTexture(var23)) {
                  Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: Vanilla texture '" + var23 + "' does not exist. File: " + var2.DJ());
                  return false;
               }
            }
         } else if (var1.jI("graphics.textures")) {
            if (!var1.kK("graphics.textures")) {
               Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: 'graphics.textures' is malformed. File: " + var2.DJ());
               return false;
            }

            ConfigSectionWrapper var22 = var1.kc("graphics.textures");
            assert var22 != null;

            for (String var30 : var22.bJ(false)) {
               String var32 = var22.jO(var30);
               assert var32 != null;
               var32 = NamespacedId.method385(var32, var3);
               if (var32 == null) {
                  Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: 'graphics.textures' key '" + var30 + "' is invalid. File: " + var2.DJ());
               } else if (!ResourcePackWriter.checkPathValid(var32)) {
                  Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: Invalid 'graphics.textures' path '" + var32 + "'. File: " + var2.DJ());
               } else if (!ResourcePackWriter.isValidIdentifier(var30)) {
                  Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: Invalid 'graphics.textures' key '" + var30 + "'. File: " + var2.DJ());
               } else {
                  if (!var32.startsWith("minecraft:")) {
                     if (ContentManager.method8199(var32) == null) {
                        Log.error(
                           "[Items] '" + var3 + ":" + var4 + "'. Error: Texture '" + var32 + "' does not exist in any contents folder. File: " + var2.DJ()
                        );
                        continue;
                     }
                  } else if (!ContentManager.method8084().field91.hasVanillaTexture(var32)) {
                     Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: Vanilla texture '" + var32 + "' does not exist. File: " + var2.DJ());
                     continue;
                  }

                  var32 = dl(var32);
                  var20.put(var30, var32);
               }
            }
         }

         if (var20.isEmpty()) {
            return false;
         }

         String var26 = var1.jO("graphics.parent");
         if (var26 == null) {
            var26 = method10112(var0, var20);
         } else {
            try {
               var26 = method10110(var26, var3);
            } catch (IllegalArgumentException var15) {
               Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: Invalid 'graphics.parent' '" + var26 + "'. File: " + var2.DJ());
               Log.error(var15.getMessage());
               return false;
            }
         }

         ItemSpecWithTexture var29 = new ItemSpecWithTexture(var0.getNamespacedID(), var0.getType(), var26, var20, var7, var5, var6, var8, var16);
         var0.method8746(var29);
      }

      return true;
   }

   private static String dl(String var0) {
      return var0.replace(".png", "");
   }

   public static String method10110(String var0, String var1) {
      String[] var2 = var0.split(":");
      if (var2.length == 2) {
         if (ContentManager.method8077(var0)) {
            return var0;
         }

         if (var2[0].equals("minecraft")) {
            if (ContentManager.method8084().field91.hasVanillaModel(var0)) {
               return var0;
            }

            throw new IllegalArgumentException("Unknown vanilla model id: " + var0);
         }

         if (ContentManager.method8200(var0) == null) {
            throw new IllegalArgumentException("Unknown model id: " + var0);
         }
      }

      NamespacedId var3 = NamespacedId.method386(var0, var1);
      if (var3 == null) {
         throw new IllegalArgumentException("Invalid model id: " + var0);
      }

      if (ContentManager.method8200(var3.toString()) == null) {
         var3 = NamespacedId.method386(var0, "minecraft");
         if (var3 == null) {
            throw new IllegalArgumentException("Invalid model id: " + var0);
         } else if (!ContentManager.method8084().field91.hasVanillaModel(var3.toString())) {
            throw new IllegalArgumentException("Unknown vanilla model id: " + var3.toString());
         } else {
            return var3.toString();
         }
      } else {
         return var3.toString();
      }
   }

   private static void method10111(CustomItemDefinition var0, ConfigSectionWrapper var1, YamlConfig var2, String var3, String var4) {
      if (!var1.kj("resource.generate")) {
         var0.aV(false);
         String var5 = var1.jO("resource.model_path");
         if (var5 == null) {
            var0.cE("minecraft:item/barrier");
            Log.error("[Items] '" + var0.getNamespacedID() + "'. Error: no 'model_path' set with 'generate: false'. File: " + var2.DJ());
         } else if (ResourcePackWriter.checkPathValid(var5)) {
            var0.cE(var5);
            String var6 = var1.jO("resource.icon");
            if (var6 != null) {
               if (Version.isOlderThan(Version.v1_21_4)) {
                  Log.warn("[Items] '" + var0.getNamespacedID() + "'. Warning: 'resource.icon' is only available in 1.21.4+ clients. File: " + var2.DJ());
               }

               var0.cF(var6);
            }
         }

         if (Settings.get().isLoggingEnabled("logging.graphics.loading")) {
            Log.log("      generate = false, model_path: " + var0.nC());
         }
      } else {
         var0.aV(true);
         if (var1.jI("resource.texture") && var1.kf("resource.texture") instanceof String var7) {
            var0.method8757(CollectionUtils.newArrayList(var7));
         } else {
            var0.method8757(var1.jZ("resource.textures"));
         }

         if (!(var0 instanceof ColoredItemDefinition) && (var0.getTextures() == null || var0.getTextures().isEmpty())) {
            Log.error("[Items] '" + var3 + ":" + var4 + "'. No 'texture' set with 'generate: true'. File: " + var2.DJ());
            var0.method8757(CollectionUtils.asFixedList("missing_texture_check_server_logs"));
         }

         var0.cL(var1.bc("resource.parent", null));
         if (Settings.get().isLoggingEnabled("logging.graphics.loading")) {
            Log.log("      parent: " + var0.nC());
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   static String method10112(CustomItemDefinition var0, Map var1) {
      if (var0 instanceof CustomBlockDefinition var3) {
         return switch (IntTableConstantsT.SWITCH_MAP[var3.aps.ordinal()]) {
            case 1, 2, 3, 4 -> var1.size() > 1 ? "block/cube" : "block/cube_all";
            case 5 -> "block/base/block_tile";
            default -> "item/generated";
         };
      } else {
         JSONObject var2 = ContentManager.method8084().field91.getItemModelJson(var0.getType());
         return var2 != null && var2.containsKey("parent") ? var2.getString("parent") : "item/generated";
      }
   }
}
