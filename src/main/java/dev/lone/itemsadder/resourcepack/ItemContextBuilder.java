package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;

final class ItemContextBuilder {
   private static final String bxp = "_iainternal:item_context/";

   static ItemContextIndex buildIndex(Map var0, Collection var1, Collection var2) {
      PackFileCache var3;
      if (var0 instanceof PackFileCache var4) {
         var3 = new PackFileCache(var4);
      } else {
         var3 = new PackFileCache(false);
         var0.forEach((var1x, var2x) -> var3.putJson(normalizeId(var1x), var2x));
      }

      Set var12 = collectModelClosure(var1, var3);
      Set var5 = collectModelClosure(var2, var3);
      TreeSet var6 = new TreeSet(var12);
      var6.retainAll(var5);
      TreeMap var7 = new TreeMap();

      for (String var9 : var1) {
         String var10 = normalizeId(var9);
         Set var11 = collectMetadataTextures(var10, var3::getMetadata);
         if (!var11.isEmpty()) {
            var7.put(var10, var11);
         }
      }

      return new ItemContextIndex(var3, var12, var5, var6, var7);
   }

   static String normalizeId(String var0) {
      String var1 = var0.trim();
      if (var1.endsWith(".json")) {
         var1 = var1.substring(0, var1.length() - ".json".length());
      }

      if (!var1.contains(":")) {
         var1 = "minecraft:" + var1;
      }

      return var1;
   }

   static String modelJsonPath(String var0) {
      String var1 = normalizeId(var0);
      int var2 = var1.indexOf(58);
      return "assets/" + var1.substring(0, var2) + "/models/" + var1.substring(var2 + 1) + ".json";
   }

   private static Set collectModelClosure(Collection var0, PackFileCache var1) {
      TreeSet var2 = new TreeSet();
      HashSet var3 = new HashSet();
      HashSet var4 = new HashSet();

      for (String var6 : var0) {
         visitModel(normalizeId(var6), var1, var2, var3, var4);
      }

      return var2;
   }

   private static void visitModel(String var0, PackFileCache var1, Set var2, Set var3, Set var4) {
      if (!var3.contains(var0)) {
         if (!var4.add(var0)) {
            throw new IllegalStateException("Model parent cycle detected at '" + var0 + "'");
         }

         PackMetadata var5 = var1.getMetadata(var0);
         if (var5 != null) {
            var2.add(var0);
            String var6 = var5.parent();
            if (var6 != null && !var6.isBlank()) {
               visitModel(normalizeId(var6), var1, var2, var3, var4);
            }
         }

         var4.remove(var0);
         var3.add(var0);
      }
   }

   static Set collectJsonTextures(String var0, Function var1) {
      ArrayList var2 = new ArrayList();
      HashSet var3 = new HashSet();
      String var4 = normalizeId(var0);

      while (var3.add(var4)) {
         JSONObject var5 = (JSONObject)var1.apply(var4);
         if (var5 == null) {
            break;
         }

         var2.add(var5);
         String var6 = var5.getString("parent");
         if (var6 == null || var6.isBlank()) {
            break;
         }

         var4 = normalizeId(var6);
      }

      JSONArray var17 = null;

      for (JSONObject var7 : var2) {
         if (var7.get("elements") instanceof JSONArray var8) {
            var17 = var8;
            break;
         }
      }

      if (var17 != null && !var17.isEmpty()) {
         TreeMap var19 = new TreeMap();

         for (int var20 = var2.size() - 1; var20 >= 0; var20--) {
            JSONObject var22 = ((JSONObject)var2.get(var20)).getJSONObject("textures");
            if (var22 != null) {
               var22.forEach((var1x, var2x) -> {
                  String var3x = var2x instanceof String var5x ? var5x : (var2x instanceof JSONObject var4x ? var4x.getString("sprite") : null);
                  if (var3x != null) {
                     var19.put(var1x, var3x);
                  }
               });
            }
         }

         TreeSet var21 = new TreeSet();

         for (Object var24 : var17) {
            if (var24 instanceof JSONObject var10 && var10.get("faces") instanceof JSONObject var11) {
               for (Object var13 : var11.values()) {
                  if (var13 instanceof JSONObject var14) {
                     String var15 = var14.getString("texture");
                     if (var15 != null && var15.startsWith("#")) {
                        String var16 = resolveTextureRef(var15.substring(1), var19);
                        if (var16 != null) {
                           var21.add(var16);
                        }
                     }
                  }
               }
            }
         }

         return var21;
      } else {
         return Set.of();
      }
   }

   static Set collectMetadataTextures(String var0, Function var1) {
      ArrayList var2 = new ArrayList();
      HashSet var3 = new HashSet();
      String var4 = normalizeId(var0);

      while (var3.add(var4)) {
         PackMetadata var5 = (PackMetadata)var1.apply(var4);
         if (var5 == null) {
            break;
         }

         var2.add(var5);
         String var6 = var5.parent();
         if (var6 == null || var6.isBlank()) {
            break;
         }

         var4 = normalizeId(var6);
      }

      PackMetadata var11 = null;

      for (PackMetadata var7 : var2) {
         if (var7.hasElements()) {
            var11 = var7;
            break;
         }
      }

      if (var11 != null && !var11.faceSlots().isEmpty()) {
         TreeMap var13 = new TreeMap();

         for (int var14 = var2.size() - 1; var14 >= 0; var14--) {
            ((PackMetadata)var2.get(var14)).textures().forEach((var1x, var2x) -> {
               if (var2x.value() != null) {
                  var13.put(var1x, var2x.value());
               }
            });
         }

         TreeSet var15 = new TreeSet();

         for (String var9 : var11.faceSlots()) {
            String var10 = resolveTextureRef(var9, var13);
            if (var10 != null) {
               var15.add(var10);
            }
         }

         return var15;
      } else {
         return Set.of();
      }
   }

   private static String resolveTextureRef(String var0, Map var1) {
      HashSet var2 = new HashSet();

      while (var2.add(var0)) {
         String var3 = (String)var1.get(var0);
         if (var3 == null) {
            return var0;
         }

         if (!var3.startsWith("#")) {
            return null;
         }

         var0 = var3.substring(1);
      }

      return var0;
   }

   private static String itemContextAssetPath(String var0) {
      String var1 = normalizeId(var0);
      int var2 = var1.indexOf(58);
      return "_iainternal:item_context/" + var1.substring(0, var2) + "/" + var1.substring(var2 + 1);
   }
}
