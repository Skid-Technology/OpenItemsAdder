package dev.lone.itemsadder.anim;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public final class AnimationJsonBuilder {
   public static final String TU = "ia_overlay_v2_1_21_6_plus";
   public static final String TV = "ia_overlay_v2_modern_atlas";

   private AnimationJsonBuilder() {
   }

   public static List method9196(AnimationBinaryPayload var0) {
      JSONObject var1 = var0.vH();
      JSONObject var2 = var1.getJSONObject("entity");
      JSONObject var3 = var1.getJSONObject("geometryModels");
      if (var2 != null && var3 != null) {
         String var4 = var2.getString("namespace");
         String var5 = var2.getString("id");
         ArrayList var6 = new ArrayList();

         for (String var8 : List.of("compatible", "single_axis_45", "unrestricted")) {
            JSONObject var9 = var3.getJSONObject(var8);

            for (String var11 : new TreeSet(var9.keySet())) {
               JSONArray var12 = var9.getJSONArray(var11);

               for (int var13 = 0; var13 < var12.size(); var13++) {
                  JSONObject var14 = var12.getJSONObject(var13);
                  String var15 = method9200(var4, var5, var8, null, var11, var13);
                  JSONObject var16 = method9197(var14.getJSONObject("model"));
                  method9199(var6, var8, var15, var16);
                  JSONObject var17 = var14.getJSONObject("variants");

                  for (String var19 : new TreeSet(var17.keySet())) {
                     String var20 = method9200(var4, var5, var8, var19, var11, var13);
                     JSONObject var21 = new JSONObject();
                     var21.put("parent", var4 + ":entity/" + var5 + "/profiles/" + var8 + "/" + var11 + "/" + var13);
                     var21.put("textures", var17.getJSONObject(var19).getJSONObject("textures"));
                     method9199(var6, var8, var20, var21);
                  }

                  JSONObject var24 = var1.getJSONObject("hurtTextures");
                  if (var24 != null) {
                     TreeSet var25 = new TreeSet(var17.keySet());
                     var25.add("default");

                     for (String var27 : var25) {
                        JSONObject var22 = var16.getJSONObject("textures") == null ? new JSONObject() : new JSONObject(var16.getJSONObject("textures"));
                        if (!var27.equals("default")) {
                           var22.putAll(var17.getJSONObject(var27).getJSONObject("textures"));
                        }

                        JSONObject var23 = new JSONObject();
                        var23.put("parent", var4 + ":entity/" + var5 + "/profiles/" + var8 + "/" + var11 + "/" + var13);
                        var23.put("textures", method9198(var22, var4, var24));
                        method9199(var6, var8, method9200(var4, var5, var8, "hurt/" + var27, var11, var13), var23);
                     }
                  }
               }
            }
         }

         return var6;
      } else {
         return List.of();
      }
   }

   private static JSONObject method9197(JSONObject var0) {
      JSONObject var1 = JSONObject.parseObject(var0.toJSONString(new Feature[0]));
      JSONArray var2 = var1.getJSONArray("elements");
      if (var2 != null) {
         for (Object var4 : var2) {
            JSONObject var5 = ((JSONObject)var4).getJSONObject("faces");
            if (var5 != null) {
               for (Object var7 : var5.values()) {
                  ((JSONObject)var7).put("tintindex", 0);
               }
            }
         }
      }

      return var1;
   }

   private static JSONObject method9198(JSONObject var0, String var1, JSONObject var2) {
      String var3 = var1 + ":ia_v2/";
      JSONObject var4 = new JSONObject();

      for (String var6 : var0.keySet()) {
         String var7 = var0.getString(var6);
         if (var7.startsWith("#")) {
            var4.put(var6, var7);
         } else {
            String var8 = var7.startsWith(var3) ? var2.getString(var7.substring(var3.length())) : null;
            var4.put(var6, var8 == null ? var7 : var3 + var8);
         }
      }

      return var4;
   }

   private static void method9199(List var0, String var1, String var2, JSONObject var3) {
      if (var1.equals("compatible")) {
         var0.add(new AnimationSourceEntry(var2, var3));
      } else {
         JSONObject var4 = new JSONObject();
         var4.put("elements", new JSONArray());
         var0.add(new AnimationSourceEntry(var2, var4));
         String var5 = var1.equals("single_axis_45") ? "ia_overlay_v2_1_21_6_plus" : "ia_overlay_v2_modern_atlas";
         var0.add(new AnimationSourceEntry(var5 + "/" + var2, var3));
      }
   }

   private static String method9200(String var0, String var1, String var2, String var3, String var4, int var5) {
      return "assets/" + var0 + "/models/entity/" + var1 + "/profiles/" + var2 + "/" + (var3 == null ? "" : var3 + "/") + var4 + "/" + var5 + ".json";
   }
}
