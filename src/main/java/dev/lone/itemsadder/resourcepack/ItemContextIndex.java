package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

final class ItemContextIndex {
   private static final String bxq = "minecraft:item/iamissing";
   private final PackFileCache bxr;
   private final Set bxs;
   private final Set bxt;
   private final Set bxu;
   private final Map bxv;
   private final Map bxw;

   private ItemContextIndex(PackFileCache var1, Set var2, Set var3, Set var4, Map var5) {
      this.bxr = new PackFileCache(var1);
      this.bxs = Collections.unmodifiableSet(new TreeSet(var2));
      this.bxt = Collections.unmodifiableSet(new TreeSet(var3));
      this.bxu = Collections.unmodifiableSet(new TreeSet(var4));
      TreeMap var6 = new TreeMap();
      var5.forEach((var1x, var2x) -> var6.put(var1x, Collections.unmodifiableSet(new TreeSet(var2x))));
      this.bxv = Collections.unmodifiableMap(var6);
      TreeMap var7 = new TreeMap();

      for (String var9 : var5.keySet()) {
         var7.putIfAbsent(var9, ItemContextBuilder.itemContextAssetPath(var9));
      }

      for (String var15 : var4) {
         PackMetadata var10 = var1.getMetadata(var15);
         if (var10 != null && var10.concreteTexture()) {
            var7.put(var15, ItemContextBuilder.itemContextAssetPath(var15));
         }
      }

      boolean var14;
      do {
         var14 = false;

         for (String var17 : var4) {
            if (!var7.containsKey(var17)) {
               PackMetadata var11 = var1.getMetadata(var17);
               String var12 = var11 == null ? null : var11.parent();
               if (var12 != null && !var12.isBlank() && var7.containsKey(ItemContextBuilder.normalizeId(var12)) || referencesAnyOf(var11, var7.keySet())) {
                  var7.put(var17, ItemContextBuilder.itemContextAssetPath(var17));
                  var14 = true;
               }
            }
         }
      } while (var14);

      this.bxw = Collections.unmodifiableMap(var7);
   }

   Set itemPaths() {
      return this.bxs;
   }

   Set sourcePaths() {
      return this.bxt;
   }

   Set modelPaths() {
      return this.bxu;
   }

   Map resolvedNames() {
      return this.bxw;
   }

   Map toSortedMap() {
      TreeMap var1 = new TreeMap();
      this.forEachResolved(var1::put);
      return var1;
   }

   void forEachResolved(BiConsumer var1) {
      for (String var3 : this.bxs) {
         PackMetadata var4 = this.bxr.getMetadata(var3);
         if (var4 != null) {
            String var5 = var4.parent();
            String var6 = var5 != null && !var5.isBlank() ? ItemContextBuilder.normalizeId(var5) : null;
            boolean var7 = this.bxw.containsKey(var3);
            boolean var8 = var6 != null && this.bxw.containsKey(var6);
            boolean var9 = referencesAnyOf(var4, this.bxw.keySet());
            if (var7 || var8 || var9) {
               JSONObject var10 = this.bxr.getJson(var3);
               JSONObject var11 = deepCopyJson(var10);
               if (var8) {
                  var11.put("parent", this.bxw.get(var6));
               }

               if (var9) {
                  this.rewriteModelReferences(var11);
               }

               Set var12 = (Set)this.bxv.get(var3);
               if (var12 != null) {
                  JSONObject var13 = var11.getJSONObject("textures");
                  if (var13 == null) {
                     var13 = new JSONObject();
                     var11.put("textures", var13);
                  }

                  for (String var15 : var12) {
                     var13.put(var15, "minecraft:item/iamissing");
                  }
               }

               var1.accept(var7 ? (String)this.bxw.get(var3) : var3, var11);
            }
         }
      }
   }

   private static JSONObject deepCopyJson(JSONObject var0) {
      JSONObject var1 = new JSONObject();
      var0.forEach((var1x, var2) -> var1.put(var1x, copyJsonValue(var2)));
      return var1;
   }

   private static Object copyJsonValue(Object var0) {
      if (var0 instanceof JSONObject var3) {
         return deepCopyJson(var3);
      } else if (var0 instanceof JSONArray var1) {
         JSONArray var2 = new JSONArray();
         var1.forEach(var1x -> var2.add(copyJsonValue(var1x)));
         return var2;
      } else {
         return var0;
      }
   }

   boolean rewriteModelReferences(Object var1) {
      if (!(var1 instanceof JSONObject var2)) {
         if (var1 instanceof JSONArray var9) {
            boolean var10 = false;

            for (Object var12 : var9) {
               if (this.rewriteModelReferences(var12)) {
                  var10 = true;
               }
            }

            return var10;
         } else {
            return false;
         }
      } else {
         boolean var3 = false;

         for (Entry var5 : var2.entrySet()) {
            Object var6 = var5.getValue();
            if ("model".equals(var5.getKey()) && var6 instanceof String var7) {
               String var8 = (String)this.bxw.get(ItemContextBuilder.normalizeId(var7));
               if (var8 != null) {
                  var5.setValue(var8);
                  var3 = true;
               }
            } else if (this.rewriteModelReferences(var6)) {
               var3 = true;
            }
         }

         return var3;
      }
   }

   private static boolean referencesAnyOf(PackMetadata var0, Set var1) {
      return var0 != null && var0.modelReferences().stream().anyMatch(var1x -> var1.contains(ItemContextBuilder.normalizeId(var1x)));
   }

   private static boolean referencesModelIn(Object var0, Set var1) {
      if (var0 instanceof JSONObject var2) {
         for (Entry var5 : var2.entrySet()) {
            Object var6 = var5.getValue();
            if ("model".equals(var5.getKey()) && var6 instanceof String var7 && var1.contains(ItemContextBuilder.normalizeId(var7))) {
               return true;
            }

            if (referencesModelIn(var6, var1)) {
               return true;
            }
         }
      } else if (var0 instanceof JSONArray) {
         for (Object var9 : (JSONArray)var0) {
            if (referencesModelIn(var9, var1)) {
               return true;
            }
         }
      }

      return false;
   }

   void remapModelPaths(Object var1) {
      TreeMap var2 = new TreeMap();
      this.bxw.forEach((var1x, var2x) -> var2.put(var2x, var1x));
      remapModelRefs(var1, var2);
   }

   private static void remapModelRefs(Object var0, Map var1) {
      if (var0 instanceof JSONObject var2) {
         for (Entry var5 : var2.entrySet()) {
            Object var6 = var5.getValue();
            if (("model".equals(var5.getKey()) || "parent".equals(var5.getKey())) && var6 instanceof String var7) {
               var5.setValue(var1.getOrDefault(ItemContextBuilder.normalizeId(var7), var7));
            } else {
               remapModelRefs(var6, var1);
            }
         }
      } else if (var0 instanceof JSONArray var3) {
         var3.forEach(var1x -> remapModelRefs(var1x, var1));
      }
   }
}
