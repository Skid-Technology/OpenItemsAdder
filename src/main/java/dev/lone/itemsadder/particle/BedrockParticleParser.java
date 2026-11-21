package dev.lone.itemsadder.particle;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import dev.lone.itemsadder.molang.MolangExpression;
import dev.lone.itemsadder.molang.MolangParser;
import dev.lone.itemsadder.molang.NoUvContext;
import dev.lone.itemsadder.resourcepack.PngImage;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;

public final class BedrockParticleParser {
   private BedrockParticleParser() {
   }

   public static List method2135(JSONObject var0, PngImage var1, Map var2) {
      JSONObject var3 = var0.getJSONObject("particle_effect").getJSONObject("components");
      JSONObject var4 = var3.getJSONObject("minecraft:particle_appearance_billboard");
      JSONObject var5 = var4 == null ? null : var4.getJSONObject("uv");
      JSONObject var6 = var5 == null ? null : var5.getJSONObject("flipbook");
      double var7 = var5 == null ? 1.0 : method2140(var5.getOrDefault("texture_width", 1));
      double var9 = var5 == null ? 1.0 : method2140(var5.getOrDefault("texture_height", 1));
      if (!(var7 <= 0.0) && !(var9 <= 0.0)) {
         double[] var11 = method2139(var6 == null ? (var5 == null ? null : var5.get("uv")) : var6.get("base_UV"), 0.0, 0.0);
         double[] var12 = method2139(var6 == null ? (var5 == null ? null : var5.get("uv_size")) : var6.get("size_UV"), var7, var9);
         double[] var13 = method2139(var6 == null ? null : var6.get("step_UV"), 0.0, 0.0);
         double var14 = var6 == null ? 1.0 : method2140(var6.getOrDefault("max_frame", 1));
         if (var14 == Math.rint(var14) && !(var14 < 1.0) && !(var14 > 256.0)) {
            ArrayList var16 = new ArrayList();
            String var17 = "itemsadder:item/iaparticles/" + var1.digest();

            for (int var18 = 0; var18 < (int)var14; var18++) {
               double var19 = (var11[0] + var18 * var13[0]) / var7 * 16.0;
               double var21 = (var11[1] + var18 * var13[1]) / var9 * 16.0;
               double var23 = var19 + var12[0] / var7 * 16.0;
               double var25 = var21 + var12[1] / var9 * 16.0;
               if (!Double.isFinite(var19) || !Double.isFinite(var21) || !Double.isFinite(var23) || !Double.isFinite(var25)) {
                  throw bj("UV frame must be finite");
               }

               JSONArray var27 = new JSONArray();
               List var28 = method2136(var19, var23);
               List var29 = method2136(var21, var25);

               for (int var30 = 1; var30 < var28.size(); var30++) {
                  for (int var31 = 1; var31 < var29.size(); var31++) {
                     double var32 = (Double)var28.get(var30 - 1);
                     double var34 = (Double)var28.get(var30);
                     double var36 = (Double)var29.get(var31 - 1);
                     double var38 = (Double)var29.get(var31);
                     double var40 = method2137(var19 + (var23 - var19) * var32);
                     double var42 = method2137(var19 + (var23 - var19) * var34);
                     double var44 = method2137(var21 + (var25 - var21) * var36);
                     double var46 = method2137(var21 + (var25 - var21) * var38);
                     var27.add(
                        JSONObject.of(
                           "from",
                           JSONArray.of(16.0 * var32, 16.0 * (1.0 - var38), 8),
                           "to",
                           JSONArray.of(16.0 * var34, 16.0 * (1.0 - var36), 8),
                           "shade",
                           false,
                           "faces",
                           JSONObject.of("north", method2138(var42, var44, var40, var46), "south", method2138(var40, var44, var42, var46))
                        )
                     );
                  }
               }

               JSONObject var48 = JSONObject.of("ambientocclusion", false, "textures", JSONObject.of("particle", var17, "0", var17), "elements", var27);
               String var49 = "item/iaparticles/" + dO(var48.toJSONString(new Feature[0]));
               String var50 = "itemsadder:" + var49;
               var2.putIfAbsent("assets/itemsadder/models/" + var49 + ".json", var48);
               var2.putIfAbsent(
                  "assets/itemsadder/items/" + var49 + ".json",
                  JSONObject.of(
                     "model",
                     JSONObject.of(
                        "type",
                        "minecraft:model",
                        "model",
                        var50,
                        "tints",
                        JSONArray.of(JSONObject.of("type", "minecraft:custom_model_data", "index", 0, "default", 16777215))
                     )
                  )
               );
               var16.add(var50);
            }

            return List.copyOf(var16);
         } else {
            throw bj("flipbook must have 1..256 static frames");
         }
      } else {
         throw bj("texture dimensions must be positive");
      }
   }

   private static List method2136(double var0, double var2) {
      ArrayList var4 = new ArrayList(4);
      var4.add(0.0);
      if (var0 != var2) {
         for (double var8 : new double[]{0.0, 16.0}) {
            double var10 = (var8 - var0) / (var2 - var0);
            if (var10 > 0.0 && var10 < 1.0) {
               var4.add(var10);
            }
         }
      }

      var4.add(1.0);
      var4.sort(Double::compare);
      return var4;
   }

   private static double method2137(double var0) {
      return Math.max(0.0, Math.min(16.0, var0));
   }

   private static JSONObject method2138(double var0, double var2, double var4, double var6) {
      return JSONObject.of("uv", JSONArray.of(new Object[]{var0, var2, var4, var6}), "texture", "#0", "tintindex", 0);
   }

   private static double[] method2139(Object var0, double var1, double var3) {
      if (var0 == null) {
         return new double[]{var1, var3};
      } else if (var0 instanceof JSONArray var5 && var5.size() == 2) {
         return new double[]{method2140(var5.get(0)), method2140(var5.get(1))};
      } else {
         throw bj("UV must be a pair");
      }
   }

   private static double method2140(Object var0) {
      MolangExpression var1 = MolangParser.method2088(var0);
      if (!var1.Ih().isEmpty()) {
         throw bj("UV/model frame definitions must be constant");
      } else {
         return var1.method2089(new NoUvContext());
      }
   }

   private static String dO(String var0) {
      try {
         return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var0.getBytes(StandardCharsets.UTF_8)));
      } catch (NoSuchAlgorithmException var2) {
         throw new AssertionError(var2);
      }
   }

   private static IllegalArgumentException bj(String var0) {
      return new IllegalArgumentException("Particle model: " + var0);
   }
}
