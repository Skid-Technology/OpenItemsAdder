package dev.lone.itemsadder.resourcepack;

import beer.devs.fastnbt.nms.Version;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemSpec;
import dev.lone.itemsadder.util.JsonHelper;
import dev.lone.itemsadder.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Material;

public final class OverlayPackGenerator {
   public void addOverlayEntry(HashMap var1, OverlayVersionRange var2, String var3, JSONObject var4) {
      Material var5 = Material.matchMaterial(var3);
      if (var5 != null && var5.isItem()) {
         JSONObject var6 = new JSONObject();
         JSONObject var7 = new JSONObject().fluentPut("type", "range_dispatch").fluentPut("property", "custom_model_data").fluentPut("index", 0);
         MaterialJsonAsset var8 = this.loadFallbackModel(var2, var3);
         if (var8 != null) {
            var7.put("fallback", this.buildFallbackModel(var3, var8));
            JSONArray var9 = new JSONArray();
            var7.put("entries", var9);
            var6.put("model", var7);
            this.applyOverrides(var2, var3, var4, var8, var9);
            var6.put("oversized_in_gui", true);
            var1.computeIfAbsent(var2, var0 -> new HashMap<>()).put(var3, VersionedPackEntry.fromJson(var6));
         }
      }
   }

   private MaterialJsonAsset loadFallbackModel(OverlayVersionRange var1, String var2) {
      if (var2.equals("crossbow")) {
         MaterialJsonAsset var3 = MaterialJsonAsset.forVersion(var2, Version.v1_21_4);
         if (!var3.hasError()) {
            return var3;
         }
      } else if (var2.equals("bow")) {
         MaterialJsonAsset var4 = MaterialJsonAsset.forVersion(var2, Version.v1_21_4);
         if (!var4.hasError()) {
            return var4;
         }
      } else {
         MaterialJsonAsset var5 = MaterialJsonAsset.forVersion(var2, var1.baseVersion);
         if (!var5.hasError()) {
            return var5;
         }

         var5 = MaterialJsonAsset.forVersion(var2, VanillaAssetManager.getTargetVersion());
         if (!var5.hasError()) {
            return var5;
         }
      }

      Log.error("Failed to load fallback model for material " + var2 + ". Skipping 1.21.4+ compatibility file creation.");
      return null;
   }

   private JSONObject buildFallbackModel(String var1, MaterialJsonAsset var2) {
      JSONObject var3 = var2.asObject().getJSONObject("model");
      if (var1.contains("leather_")) {
         var3.fluentPut("type", "model")
            .fluentPut("model", "item/" + var1)
            .put("tints", new JSONArray().fluentAdd(new JSONObject().fluentPut("type", "dye").fluentPut("default", -6265536)));
      }

      return var3;
   }

   private void applyOverrides(OverlayVersionRange var1, String var2, JSONObject var3, MaterialJsonAsset var4, JSONArray var5) {
      JSONArray var6 = var3.getJSONArray("overrides");
      if (var6 != null) {
         HashMap var7 = new HashMap();
         HashMap var8 = new HashMap();

         for (Object var10 : var6) {
            JSONObject var11 = (JSONObject)var10;
            JSONObject var12 = var11.getJSONObject("predicate");
            if (var12 != null && var12.containsKey("custom_model_data")) {
               int var13 = var12.getIntValue("custom_model_data");
               if (var13 >= 0) {
                  JSONObject var14 = this.getOrCreateThreshold(var13, var8, var5, var4);
                  JSONObject var15 = var14.getJSONObject("model");
                  String var16 = var11.getString("model");
                  boolean var17 = this.applyTypeSpecific(var1, var2, var12, var16, var14, var15);
                  if (var13 > 0 && !var17) {
                     this.applyLeatherTints(var2, var13, var16, var14, var15);
                  }

                  if (ContentManager.method8084().ar.containsKey(var16)) {
                     var7.put(var16, var14);
                  }
               }
            }
         }

         for (Entry var19 : var7.entrySet()) {
            String var20 = (String)var19.getKey();
            JSONObject var21 = ((JSONObject)var19.getValue()).getJSONObject("model");
            CustomItemDefinition var22 = (CustomItemDefinition)ContentManager.method8084().ar.get(var20);
            ItemSpec var23 = var22.anZ;
            if (var23 != null) {
               if (var23.Em() != null) {
                  Log.devLog("--- model has graphics.icon, using as special. Icon: " + var23.Du());
                  this.setDisplayContextSelect(var21, var23.Em());
                  continue;
               }
            } else {
               String var24 = var22.Dv();
               if (var24 != null && !var24.isEmpty()) {
                  this.setDisplayContextSelect(var21, var24);
                  Log.devLog("--- model has icon, using as special. Icon: " + var24);
                  continue;
               }
            }

            if ("minecraft:display_context".equals(var21.getString("property"))) {
               this.setDisplayContextSelect(var21, var20);
            }
         }
      }
   }

   void setDisplayContextSelect(JSONObject var1, String var2) {
      JSONObject var3 = JsonHelper.copyObject(var1);
      if (var3.containsKey("fallback")) {
         var3 = var3.getJSONObject("fallback");
      }

      var1.clear();
      var1.fluentPut("type", "select")
         .fluentPut("property", "display_context")
         .fluentPut(
            "cases",
            new JSONArray()
               .fluentAdd(
                  new JSONObject()
                     .fluentPut("model", new JSONObject().fluentPut("type", "model").fluentPut("model", var2))
                     .fluentPut("when", new JSONArray().fluentAdd("gui").fluentAdd("ground").fluentAdd("fixed"))
               )
         )
         .fluentPut("fallback", var3);
   }

   private JSONObject getOrCreateThreshold(int var1, Map var2, JSONArray var3, MaterialJsonAsset var4) {
      JSONObject var5 = (JSONObject)var2.get(var1);
      if (var5 != null) {
         return var5;
      }

      JSONObject var6 = new JSONObject().fluentPut("threshold", var1);
      JSONObject var7 = var4.copyAsObject().getJSONObject("model").clone();
      if (var7.containsKey("on_true")) {
         var7.getJSONObject("on_true").computeIfPresent("entries", (var0, var1x) -> {
            ((JSONArray)var1x).clear();
            return var1x;
         });
      }

      var6.put("model", var7);
      var3.add(var6);
      var2.put(var1, var6);
      return var6;
   }

   private boolean applyTypeSpecific(OverlayVersionRange var1, String var2, JSONObject var3, String var4, JSONObject var5, JSONObject var6) {
      return switch (var2) {
         case "shield" -> this.applyShield(var1, var3, var4, var6);
         case "bow" -> this.applyBow(var3, var4, var6);
         case "crossbow" -> this.applyCrossbow(var3, var4, var6);
         case "fishing_rod" -> this.applyFishingRod(var3, var4, var6);
         case "trident" -> this.applyTrident(var3, var4, var6);
         default -> false;
      };
   }

   private void applyLeatherTints(String var1, int var2, String var3, JSONObject var4, JSONObject var5) {
      if ("player_head".equals(var1)) {
         var5.put("base", var3);
      } else {
         var4.fluentPut("model", var5);
         var5.fluentPut("type", "model").fluentPut("model", var3);
         if (var1.contains("leather_")) {
            try {
               int var6 = ContentManager.method8084().method8206(var2);
               var5.fluentPut("tints", new JSONArray().fluentAdd(new JSONObject().fluentPut("type", "dye").fluentPut("default", var6)));
            } catch (Exception var7) {
            }
         }
      }
   }

   private boolean applyShield(OverlayVersionRange var1, JSONObject var2, String var3, JSONObject var4) {
      if (var2.containsKey("blocking") && var2.getInteger("blocking") != 0) {
         if (var2.getInteger("blocking") != 1) {
            Log.error("Invalid blocking value for shield model override: " + var2.getInteger("blocking"));
            return true;
         }

         JSONObject var6 = var4.getJSONObject("on_true");
         var6.put("type", "model");
         var6.put("model", var3);
         if (var1.baseVersion.ordinal() >= Version.v26_1_1.ordinal()) {
            var6.fluentPut(
               "transformation",
               new JSONObject()
                  .fluentPut("left_rotation", new JSONArray().fluentAdd(0.0).fluentAdd(0.0).fluentAdd(0.0).fluentAdd(1.0))
                  .fluentPut("right_rotation", new JSONArray().fluentAdd(0.0).fluentAdd(0.0).fluentAdd(0.0).fluentAdd(1.0))
                  .fluentPut("scale", new JSONArray().fluentAdd(1.0).fluentAdd(-1.0).fluentAdd(-1.0))
                  .fluentPut("translation", new JSONArray().fluentAdd(0.0).fluentAdd(0.0).fluentAdd(0.0))
            );
         }
      } else {
         JSONObject var5 = var4.getJSONObject("on_false");
         var5.put("type", "model");
         var5.put("model", var3);
         if (var1.baseVersion.ordinal() >= Version.v26_1_1.ordinal()) {
            var5.fluentPut(
               "transformation",
               new JSONObject()
                  .fluentPut("left_rotation", new JSONArray().fluentAdd(0.0).fluentAdd(0.0).fluentAdd(0.0).fluentAdd(1.0))
                  .fluentPut("right_rotation", new JSONArray().fluentAdd(0.0).fluentAdd(0.0).fluentAdd(0.0).fluentAdd(1.0))
                  .fluentPut("scale", new JSONArray().fluentAdd(1.0).fluentAdd(-1.0).fluentAdd(-1.0))
                  .fluentPut("translation", new JSONArray().fluentAdd(0.0).fluentAdd(0.0).fluentAdd(0.0))
            );
         }
      }

      return true;
   }

   private boolean applyBow(JSONObject var1, String var2, JSONObject var3) {
      JSONObject var4 = var3.getJSONObject("on_true");
      JSONArray var5 = var4.getJSONArray("entries");
      if (var1.containsKey("pulling") && var1.getInteger("pulling") == 1 && var1.containsKey("pull")) {
         var5.add(
            new JSONObject()
               .fluentPut("threshold", var1.getFloat("pull"))
               .fluentPut("model", new JSONObject().fluentPut("type", "model").fluentPut("model", var2))
         );
      } else {
         if (!var1.containsKey("pulling") || var1.getInteger("pulling") != 1) {
            var3.getJSONObject("on_false").put("model", var2);
         }

         var3.getJSONObject("on_true").getJSONObject("fallback").put("model", var2);
      }

      return true;
   }

   private boolean applyCrossbow(JSONObject var1, String var2, JSONObject var3) {
      JSONObject var4 = var3.getJSONObject("on_true");
      JSONArray var5 = var4.getJSONArray("entries");
      if (var1.containsKey("pulling") && var1.getInteger("pulling") == 1 && var1.containsKey("pull")) {
         var5.add(
            new JSONObject()
               .fluentPut("threshold", var1.getFloat("pull"))
               .fluentPut("model", new JSONObject().fluentPut("type", "model").fluentPut("model", var2))
         );
      } else {
         JSONObject var6 = var3.getJSONObject("on_false");
         JSONArray var7 = var6.getJSONArray("cases");
         if (var1.containsKey("charged") && var1.getInteger("charged") == 1) {
            boolean var8 = var1.containsKey("firework") && var1.getInteger("firework") == 1;
            String var9 = var8 ? "rocket" : "arrow";

            for (Object var11 : var7) {
               JSONObject var12 = (JSONObject)var11;
               if (var12.getString("when").equals(var9)) {
                  var12.put("model", new JSONObject().fluentPut("type", "model").fluentPut("model", var2));
                  break;
               }
            }
         } else if (!var1.containsKey("pulling")) {
            var6.put("model", var2);
            var6.put("fallback", new JSONObject().fluentPut("type", "model").fluentPut("model", var2));
            var4.getJSONObject("fallback").put("model", var2);
         }
      }

      return true;
   }

   private boolean applyFishingRod(JSONObject var1, String var2, JSONObject var3) {
      var3.put("type", "minecraft:condition");
      JSONObject var4 = var3.getJSONObject("on_true");
      JSONObject var5 = var3.getJSONObject("on_false");
      if (var1.containsKey("cast") && var1.getInteger("cast") == 1) {
         var4.fluentPut("model", var2).fluentPut("type", "model");
      } else {
         var5.fluentPut("model", var2).fluentPut("type", "model");
      }

      return true;
   }

   boolean applyTrident(JSONObject var1, String var2, JSONObject var3) {
      JSONObject var4 = var3.getJSONObject("fallback");
      JSONObject var5;
      if (var1.containsKey("throwing") && var1.getInteger("throwing") != 0) {
         if (var1.getInteger("throwing") != 1) {
            Log.error("Invalid throwing value for trident model override: " + var1.getInteger("throwing"));
            return true;
         }

         var5 = var4.getJSONObject("on_true");
      } else {
         var5 = var4.getJSONObject("on_false");
      }

      var5.remove("base");
      var5.put("type", "model");
      var5.put("model", var2);
      return true;
   }
}
