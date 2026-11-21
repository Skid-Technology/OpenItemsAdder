package dev.lone.itemsadder.nms;

import beer.devs.fastnbt.nms.Version;
import com.google.gson.JsonObject;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.Log;
import java.io.File;
import java.io.IOException;
import javax.annotation.Nullable;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

public final class NmsWorldService {
   final NmsWorldAdapter aVH = (NmsWorldAdapter)NmsRegistry.method2777(NmsWorldAdapter.class);
   static NmsWorldService aVI;

   NmsWorldService() {
   }

   private static NmsWorldService method3190() {
      if (aVI == null) {
         aVI = new NmsWorldService();
      }

      return aVI;
   }

   public static NmsWorldAdapter Om() {
      return method3190().aVH;
   }

   @Deprecated
   public static boolean method3191(String var0, String var1, int var2, int var3, int var4) {
      boolean var5 = false;
      World var6 = (World)Bukkit.getServer().getWorlds().get(0);
      File var7 = new File(var6.getWorldFolder(), "datapacks/ia_" + var0 + "/data/" + var0 + "/worldgen/biome".replace("/", File.separator));
      File var8 = new File(var6.getWorldFolder(), ("datapacks/ia_" + var0 + "/").replace("/", File.separator));
      if (!var7.exists()) {
         var7.mkdirs();
         String var9 = "{\n  \"pack\": {\n    \"pack_format\": 6,\n    \"description\": \"ItemsAdder - colored fluids\"\n  }\n}";

         try {
            FileUtils.write(new File(var8, "pack.mcmeta"), var9);
         } catch (IOException var12) {
            Log.error("Failed to write pack.mcmeta of " + var0 + " datapack");
            var12.printStackTrace();
         }
      }

      JsonObject var15 = (JsonObject)ContentManager.field66
         .fromJson(
            "{\n"
               + (Version.isAtLeast(Version.v1_21_3) ? "  \"carvers\": [],\n" : "\"carvers\": {},")
               + "  \"downfall\": 0.4,\n  \"effects\": {\n    \"fog_color\": 12638463,\n    \"mood_sound\": {\n      \"block_search_extent\": 8,\n      \"offset\": 2.0,\n      \"sound\": \"minecraft:ambient.cave\",\n      \"tick_delay\": 6000\n    },\n    \"sky_color\": 7907327,\n    \"water_color\": 4159204,\n    \"water_fog_color\": 329011\n  },\n  \"features\": [\n    [],\n    [],\n    [],\n    [],\n    [],\n    [],\n    [],\n    [],\n    [],\n    [],\n    []\n  ],\n  \"has_precipitation\": true,\n  \"spawn_costs\": {},\n  \"spawners\": {\n    \"ambient\": [],\n    \"axolotls\": [],\n    \"creature\": [],\n    \"misc\": [],\n    \"monster\": [],\n    \"underground_water_creature\": [],\n    \"water_ambient\": [],\n    \"water_creature\": []\n  },\n  \"temperature\": 0.8\n}",
            JsonObject.class
         );
      if (var2 != -1) {
         var15.getAsJsonObject("effects").addProperty("water_color", var2);
      }

      if (var2 != -1) {
         var15.getAsJsonObject("effects").addProperty("water_fog_color", var2);
      }

      if (var3 != -1) {
         var15.getAsJsonObject("effects").addProperty("foliage_color", var3);
      }

      if (var4 != -1) {
         var15.getAsJsonObject("effects").addProperty("grass_color", var4);
      }

      File var10 = new File(var7, var1 + ".json");

      try {
         String var11 = ContentManager.field66.toJson(var15);
         if (var10.exists() && ((JsonObject)ContentManager.field66.fromJson(FileUtils.readFileToString(var10), JsonObject.class)).equals(var15)) {
            var5 = false;
         } else {
            FileUtils.write(var10, var11);
            var5 = true;
         }
      } catch (IOException var13) {
         Log.error("Failed to write datapack for: " + var0 + ":" + var1, var13);
         var5 = false;
      }

      return var5;
   }

   @Nullable
   public static Biome bi(Block var0) {
      try {
         return var0.getBiome();
      } catch (NullPointerException var2) {
         return null;
      }
   }

   public static Biome method3192(Block var0, Biome var1) {
      Biome var2 = bi(var0);
      return var2 == null ? var1 : var2;
   }
}
