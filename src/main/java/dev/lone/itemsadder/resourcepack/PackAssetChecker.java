package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent.PackEntry;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

final class PackAssetChecker {
   private PackAssetChecker() {
   }

   static boolean method6640(File var0, String var1) {
      if (!Settings.bI() || !var1.contains("minecraft/shaders/core/") || !var1.contains("rendertype_text") && !ia(var1).equals("text")) {
         if (Settings.bJ() && var1.contains("minecraft/shaders/core/") && var1.contains("rendertype_armor_cutout_no_cull")) {
            Log.warn("Found shader overlapping an ItemsAdder Armors shader: " + Utils.shortPath(var0) + " -> " + Utils.normalizePluginPath(var1));
            Log.warn("Disable emotes to edit these shaders manually.");
            return true;
         } else {
            return false;
         }
      } else {
         Log.warn("Found shader overlapping an ItemsAdder Texts shader: " + Utils.shortPath(var0) + " -> " + Utils.normalizePluginPath(var1));
         Log.warn("Disable text-effects to edit these shaders manually.");
         return true;
      }
   }

   static void method6641(Map var0) {
      if (Settings.get().autoFixPre1212Shaders) {
         for (PackEntry var2 : List.copyOf(var0.values())) {
            String var3 = Utils.normalizeSlashes(var2.getPath());
            if (!var2.isRemoved() && hZ(var3)) {
               try {
                  JSONObject var4 = JSON.parseObject(ShaderValidator.readPackEntryText(var2));
                  boolean var5 = method6642(var4, "vertex") | method6642(var4, "fragment");
                  if (var5) {
                     Log.log("[Pack] pre-1.21.2 shader JSON found. Adding 1.21.2+ compatibility. File: " + Utils.shortPathOf(var3));
                     String var6 = "ia_overlay_1_21_2_plus/" + var3;
                     var0.putIfAbsent(var6, PackEntry.bytes(var6, var4.toJSONString(new Feature[0]).getBytes(StandardCharsets.UTF_8)));
                  }
               } catch (Throwable var7) {
                  Log.warn("Malformed shader JSON. File: " + Utils.shortPathOf(var3) + ". " + ShaderValidator.rootCauseMessage(var7));
               }
            }
         }
      }
   }

   private static boolean method6642(JSONObject var0, String var1) {
      if (var0.get(var1) instanceof String var3 && !var3.startsWith("core/") && !var3.startsWith("minecraft:core/")) {
         var0.put(var1, "core/" + var3);
         return true;
      } else {
         return false;
      }
   }

   private static boolean hZ(String var0) {
      return var0.endsWith(".json") && var0.contains("assets/minecraft/shaders/core/");
   }

   private static String ia(String var0) {
      int var1 = var0.lastIndexOf(47);
      int var2 = var0.lastIndexOf(46);
      int var3 = var1 + 1;
      return var2 > var3 ? var0.substring(var3, var2) : var0.substring(var3);
   }
}
