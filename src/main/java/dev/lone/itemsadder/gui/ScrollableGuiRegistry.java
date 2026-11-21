package dev.lone.itemsadder.gui;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.block.CustomBlockStateSettings;
import dev.lone.itemsadder.core.NamespacedIdentityImpl;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public final class ScrollableGuiRegistry {
   private final Map bEj;
   private Map bEk;

   private ScrollableGuiRegistry(Map var1) {
      this.bEj = Map.copyOf(var1);
   }

   public synchronized List XS() {
      if (this.bEk == null) {
         this.bEk = new LinkedHashMap();

         for (int var1 = -9; var1 <= 17; var1++) {
            this.hX("_iainternal:item/gui/scroll_track/offset_" + var1);
         }

         this.hX("_iainternal:item/gui/scroll_track/track");
         this.bEj.values().forEach(var1x -> {
            for (int var2x = -9; var2x <= 17; var2x++) {
               this.hX("_iainternal:item/" + var1x.getKey() + "/offset_" + var2x);
            }

            this.hX("_iainternal:item/" + var1x.getKey() + "/track");
         });

         for (String var2 : List.of("up_enabled", "up_disabled", "down_enabled", "down_disabled")) {
            this.hX("_iainternal:item/gui/scroll_track/" + var2);
         }

         CustomBlockStateSettings.getInstance().saveIdCache();
      }

      return this.bEk
         .entrySet()
         .stream()
         .map(
            var0 -> new NamespacedIdentityImpl(
               (Integer)var0.getValue(), (String)var0.getKey(), "_iainternal", ((String)var0.getKey()).substring(((String)var0.getKey()).lastIndexOf(47) + 1)
            )
         )
         .toList();
   }

   private void hX(String var1) {
      int var2 = CustomBlockStateSettings.getInstance().allocateStateId(Material.PAPER, "_iainternal", var1.substring("_iainternal:item/".length()));
      if (var2 < 0) {
         throw new IllegalStateException("No custom model data available for " + var1);
      }

      this.bEk.put(var1, var2);
   }

   public Set XQ() {
      return Stream.concat(
            this.bEj
               .entrySet()
               .stream()
               .filter(var0 -> ((ScrollableGuiKeySet)var0.getKey()).bEn != null)
               .flatMap(
                  var0 -> IntStream.rangeClosed(-9, 17)
                     .mapToObj(var1 -> new NamespacedKey("_iainternal", ((NamespacedKey)var0.getValue()).getKey() + "/fast_offset_" + var1))
               ),
            Stream.concat(
               Stream.concat(
                  this.bEj
                     .values()
                     .stream()
                     .flatMap(
                        var0 -> Stream.concat(
                           IntStream.rangeClosed(-9, 17).mapToObj(var1 -> new NamespacedKey("_iainternal", var0.getKey() + "/offset_" + var1)),
                           Stream.of(new NamespacedKey("_iainternal", var0.getKey() + "/track"))
                        )
                     ),
                  Stream.of("up_enabled", "up_disabled", "down_enabled", "down_disabled")
                     .map(var0 -> new NamespacedKey("_iainternal", "gui/scroll_track/" + var0))
               ),
               IntStream.rangeClosed(-9, 17).mapToObj(var0 -> new NamespacedKey("_iainternal", "gui/scroll_track/fast_offset_" + var0))
            )
         )
         .collect(Collectors.toUnmodifiableSet());
   }

   public Map XR() {
      LinkedHashMap var1 = new LinkedHashMap();
      JSONArray var2 = new JSONArray();

      for (int var3 = -9; var3 <= 17; var3++) {
         String var4 = "offset_" + var3;
         String var5 = "_iainternal:item/gui/scroll_track/" + var4;
         String var6 = "_iainternal:gui/scroll_track/" + var4;
         var2.add(JSONObject.of("threshold", var3, "model", ScrollableGuiAssets.fR(var5)));
         var1.put("ia_overlay_1_21_4_plus/assets/_iainternal/models/item/gui/scroll_track/" + var4 + ".json", JSON.toJSONBytes(ScrollableGuiAssets.hV(var6)));
         var1.put("assets/_iainternal/textures/gui/scroll_track/" + var4 + ".png", ScrollableGuiAssets.method6627(var3));
      }

      var2.add(JSONObject.of("threshold", 18, "model", ScrollableGuiAssets.fR("_iainternal:item/gui/scroll_track/track")));
      var1.put(
         "ia_overlay_1_21_4_plus/assets/_iainternal/models/item/gui/scroll_track/track.json",
         JSON.toJSONBytes(ScrollableGuiAssets.hV("_iainternal:gui/scroll_track/track"))
      );
      var1.put("assets/_iainternal/textures/gui/scroll_track/track.png", ScrollableGuiAssets.method6627((Integer)null));

      for (int var7 = -9; var7 <= 17; var7++) {
         String var10 = "fast_offset_" + var7;
         var1.put(
            "ia_overlay_1_21_4_plus/assets/_iainternal/models/item/gui/scroll_track/" + var10 + ".json",
            JSON.toJSONBytes(ScrollableGuiAssets.hV("_iainternal:gui/scroll_track/" + var10))
         );
         var1.put("assets/_iainternal/textures/gui/scroll_track/" + var10 + ".png", ScrollableGuiAssets.method6629(ScrollableGuiAssets.dj(var7)));
      }

      var1.put(
         "ia_overlay_1_21_4_plus/assets/_iainternal/items/gui/scroll_track.json",
         JSON.toJSONBytes(
            JSONObject.of(
               "oversized_in_gui",
               true,
               "model",
               ScrollableGuiAssets.ab(
                  JSONObject.of(
                     "type",
                     "minecraft:range_dispatch",
                     "property",
                     "minecraft:custom_model_data",
                     "index",
                     0,
                     "entries",
                     var2,
                     "fallback",
                     ScrollableGuiAssets.fR("_iainternal:item/gui/scroll_track/track")
                  )
               )
            )
         )
      );
      var1.put(
         "ia_overlay_1_21_4_plus/assets/_iainternal/items/gui/empty_slot.json",
         JSON.toJSONBytes(JSONObject.of("model", JSONObject.of("type", "minecraft:empty")))
      );
      var1.put(
         "ia_overlay_1_21_4_plus/assets/_iainternal/scrollable_gui_v1.json",
         JSON.toJSONBytes(
            JSONObject.of(
               "version",
               2,
               "custom_models",
               Stream.concat(this.bEj.values().stream().map(NamespacedKey::toString), Stream.of("_iainternal:gui/empty_slot")).sorted().toArray(String[]::new)
            )
         )
      );
      this.bEj.forEach((var1x, var2x) -> ScrollableGuiAssets.method6620(var1, var1x, var2x));

      for (String var11 : new ArrayList(var1.keySet())) {
         if (var11.startsWith("ia_overlay_1_21_4_plus/assets/_iainternal/models/item/gui/scroll_track/")
            || var11.startsWith("ia_overlay_1_21_4_plus/assets/_iainternal/models/item/gui/scroll_track/custom/")) {
            var1.put(var11.substring(var11.indexOf("assets/")), (byte[])var1.get(var11));
         }
      }

      for (String var12 : List.of("up_enabled", "up_disabled", "down_enabled", "down_disabled")) {
         var1.put("assets/_iainternal/textures/gui/scroll_track/" + var12 + ".png", ScrollableGuiAssets.method6629(ScrollableGuiAssets.hW(var12)));
         var1.put(
            "assets/_iainternal/models/item/gui/scroll_track/" + var12 + ".json",
            JSON.toJSONBytes(ScrollableGuiAssets.hV("_iainternal:gui/scroll_track/" + var12))
         );
      }

      var1.put(
         "assets/_iainternal/scrollable_gui_legacy_v1.json", JSON.toJSONBytes(JSONObject.of("version", 1, "models", this.bEk == null ? Map.of() : this.bEk))
      );
      return Map.copyOf(var1);
   }
}
