package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.JsonHelper;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PackJsonMerger {
   public static JSONObject buildMinecraftSounds() {
      Map var0 = loadAllSoundFiles();
      JSONObject var1 = var0.computeIfAbsent("minecraft", var0x -> new JSONObject());
      addBlockSoundOverrides(var1);
      return var1;
   }

   public static Map loadAllSoundFiles() {
      return loadSoundFilesFrom(ContentManager.method8203());
   }

   static Map loadSoundFilesFrom(List var0) {
      LinkedHashMap var1 = new LinkedHashMap();

      for (File var3 : var0) {
         File var4 = new File(var3, "sounds.json");
         if (var4.exists()) {
            try {
               JSONObject var5 = JsonHelper.readJsonFile(var4);
               if (!isValidSoundsFile(var5)) {
                  Log.error("Error opening a custom sounds file. Make sure it's valid or delete it. File: " + Utils.shortPathOf(var4.getAbsolutePath()));
               } else {
                  JSONObject var6 = var1.computeIfAbsent(var3.getName(), var0x -> new JSONObject());
                  JsonHelper.mergeObject(var6, var5);
               }
            } catch (JSONException var7) {
               Log.error("Malformed JSON file. File: " + Utils.shortPathOf(var4.getAbsolutePath()));
               Log.error(var7.getMessage());
            } catch (Throwable var8) {
               Log.error("Error opening a custom sounds file. Make sure it's valid or delete it. File: " + Utils.shortPathOf(var4.getAbsolutePath()), var8);
            }
         }
      }

      return var1;
   }

   public static void addBlockSoundOverrides(JSONObject var0) {
      if (Settings.get().customBlockSounds) {
         JsonAssetWithCallbacks var1 = new JsonAssetWithCallbacks("internal/custom_blocks_sounds.json");
         if (!var1.hasError()) {
            for (Entry var3 : var1.asObject().entrySet()) {
               String var4 = (String)var3.getKey();
               if (var4.endsWith(".step")) {
                  if (Settings.get().customBlockSoundsStep) {
                     putBlockSound(var0, var3, var4);
                  }
               } else if (Settings.get().customBlockSoundsNonStep) {
                  putBlockSound(var0, var3, var4);
               }
            }
         }
      }
   }

   private static boolean isValidSoundsFile(JSONObject var0) {
      String var1 = "Refer to the wiki: https://a.devs.beer/ia-wiki-sounds";
      boolean var2 = true;

      for (Entry var4 : var0.entrySet()) {
         if (!(var4.getValue() instanceof JSONObject)) {
            Log.error("Custom sound '" + (String)var4.getKey() + "' has invalid value. " + var1);
            var2 = false;
         } else {
            JSONObject var5 = (JSONObject)var4.getValue();
            if (!var5.containsKey("sounds")) {
               Log.error("Custom sound '" + (String)var4.getKey() + "' has no 'sounds' property. " + var1);
               var2 = false;
            }
         }
      }

      return var2;
   }

   private static void putBlockSound(JSONObject var0, Entry var1, String var2) {
      if (var2.startsWith("block.")) {
         if (var0.containsKey(var2)) {
            JSONObject var3 = (JSONObject)var0.get(var2);
            putSound(var0, "internal." + var2, var3);
         }

         putSound(var0, var2, var1.getValue());
      } else {
         putSound(var0, var2, var1.getValue());
      }
   }

   private static void putSound(JSONObject var0, String var1, Object var2) {
      var0.put(var1, var2);
   }
}
