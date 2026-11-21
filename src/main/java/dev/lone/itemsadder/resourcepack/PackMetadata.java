package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.util.ListCollector;
import dev.lone.itemsadder.util.StringKey;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

class PackMetadata {
   private final String parent;
   private final Map textures;
   private final boolean hasElements;
   private final Set faceSlots;
   private final List modelReferences;
   private final List modernModelReferences;
   private final boolean concreteTexture;
   private final boolean outOfBoundsUv;

   PackMetadata(String var1, Map var2, boolean var3, Set var4, List var5, List var6, boolean var7, boolean var8) {
      this.parent = var1;
      this.textures = var2;
      this.hasElements = var3;
      this.faceSlots = var4;
      this.modelReferences = var5;
      this.modernModelReferences = var6;
      this.concreteTexture = var7;
      this.outOfBoundsUv = var8;
   }

   static PackMetadata fromModelJson(JSONObject var0) {
      JSONObject var1 = var0.getJSONObject("textures");
      LinkedHashMap var2 = new LinkedHashMap();
      boolean var3 = false;
      if (var1 != null) {
         for (Entry var5 : var1.entrySet()) {
            Object var6 = var5.getValue();
            String var7 = var6 instanceof JSONObject var8 ? var8.getString("sprite") : (var6 == null ? null : String.valueOf(var6));
            var2.put((String)var5.getKey(), new StringKey(var7));
            String var27 = var6 instanceof String var10 ? var10 : (var6 instanceof JSONObject var9 ? var9.getString("sprite") : null);
            var3 |= var27 != null && !var27.startsWith("#");
         }
      }

      LinkedHashSet var22 = new LinkedHashSet();
      boolean var23 = false;
      Object var24 = var0.get("elements");
      if (var24 instanceof JSONArray var25) {
         for (Object var29 : var25) {
            if (var29 instanceof JSONObject var30 && var30.get("faces") instanceof JSONObject var11) {
               for (Object var13 : var11.values()) {
                  if (var13 instanceof JSONObject var14) {
                     String var15 = var14.getString("texture");
                     if (var15 != null && var15.startsWith("#")) {
                        var22.add(var15.substring(1));
                     }

                     if (var14.get("uv") instanceof JSONArray var16 && var16.size() == 4) {
                        for (Object var19 : var16) {
                           if (var19 instanceof Number var17) {
                              double var20 = var17.doubleValue();
                              var23 |= Double.isFinite(var20) && (var20 < 0.0 || var20 > 16.0);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      JSONObject var26 = new JSONObject(var0);
      var26.remove("overrides");
      return new PackMetadata(
         var0.getString("parent"),
         Collections.unmodifiableMap(var2),
         var24 instanceof JSONArray,
         Set.copyOf(var22),
         List.copyOf(ListCollector.collectModelNames(var0)),
         List.copyOf(ListCollector.collectModelNames(var26)),
         var3,
         var23
      );
   }
}
