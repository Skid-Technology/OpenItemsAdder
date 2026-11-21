package dev.lone.itemsadder.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import org.joml.Matrix4f;

public final class TransformJsonCodec {
   private static final JSONObject Cw;
   public static final String Cx;

   private TransformJsonCodec() {
   }

   public static JsonTransformEntry createEntry(HumanoidEquipmentSet var0, EquipmentSlotRecord var1, String var2, boolean var3, boolean var4, float var5) {
      String var6 = var1.pi();
      String var7 = var6.replace("upper_", "").replace("lower_", "").replace("foot", "leg");
      if (var7.equals("waist")) {
         var7 = "body";
      }

      JSONArray var8 = Cw.getJSONObject("parts").getJSONArray(var7);
      if (var8 == null) {
         throw new IllegalArgumentException("Unknown equipment segment " + var6);
      }

      JSONArray var9 = new JSONArray();
      float var10 = 24.0F;

      for (Object var12 : var8) {
         JSONObject var13 = (JSONObject)var12;
         double[] var14 = readDoubles(var13.getJSONArray("from"));
         double[] var15 = readDoubles(var13.getJSONArray("to"));
         double var16 = var14[1];
         double var18 = var15[1];
         double var20 = var18;
         double var22 = var16;
         double var24 = 0.0;
         if (var6.contains("upper_") || var6.contains("lower_") || var6.endsWith("_foot")) {
            double var26 = var18 - (var18 - var16) * ((Float)var0.pg().get(var7)).floatValue();
            double var28 = var18 - (var18 - var16) * ((Float)var0.pg().get(var7.replace("leg", "foot"))).floatValue();
            if (var6.contains("upper_")) {
               var22 = var26;
            } else {
               var20 = var24 = var6.endsWith("_foot") ? var28 : var26;
               if (var6.contains("lower_") && var1.ph().equals("boots") && var0.pe().equals("vanilla_humanoid_articulated_feet_v1")) {
                  var22 = var28;
               }
            }
         }

         double var67 = Cw.getDoubleValue(var1.ph().equals("legs") ? "innerInflation" : "outerInflation")
            + var13.getDoubleValue("inflationOffset")
            + var1.size();
         var14[1] = var22;
         var15[1] = var20;

         for (int var68 = 0; var68 < 3; var68++) {
            boolean var29 = var68 != 1 || var22 == var16;
            boolean var30 = var68 != 1 || var20 == var18;
            if (var29) {
               var14[var68] -= var67 + var5;
            }

            if (var30) {
               var15[var68] += var67 + var5;
            }

            if (var68 == 1) {
               var14[var68] -= var24;
               var15[var68] -= var24;
            }

            if (var14[var68] >= var15[var68]) {
               throw new IllegalArgumentException("Equipment fit inverts " + var6);
            }

            var10 = Math.max(var10, (float)Math.max(Math.abs(var14[var68]) + 0.1 - var5, Math.abs(var15[var68]) + 0.1 - var5));
         }

         for (int var69 = 0; var69 < (var4 ? 8 : 1); var69++) {
            JSONObject var70 = new JSONObject();

            for (String var31 : var13.getJSONObject("faces").keySet()) {
               if ((!var31.equals("up") || var20 == var18) && (!var31.equals("down") || var22 == var16)) {
                  JSONObject var32 = var13.getJSONObject("faces").getJSONObject(var31);
                  double[] var33 = readDoubles(var32.getJSONArray("uv"));
                  int var34 = var32.getIntValue("rotation");
                  if (!var31.equals("up") && !var31.equals("down")) {
                     double var35 = var34 == 180 ? var33[3] : var33[1];
                     double var37 = var34 == 180 ? var33[1] : var33[3];
                     double var39 = var18 + var67 + var5;
                     double var41 = var18 - var16 + 2.0 * (var67 + var5);
                     double var43 = var20 == var18 ? var35 : var35 + (var37 - var35) * (var39 - var20) / var41;
                     double var45 = var22 == var16 ? var37 : var35 + (var37 - var35) * (var39 - var22) / var41;
                     var33[var34 == 180 ? 3 : 1] = var43;
                     var33[var34 == 180 ? 1 : 3] = var45;
                  }

                  for (int var72 = 0; var72 < 4; var72++) {
                     var33[var72] = var72 % 2 == 0 ? var33[var72] / 4.0 : (var33[var72] + var69 * 32) / (var4 ? 16 : 2);
                  }

                  JSONObject var73 = JSONObject.of("uv", writeDoubles(var33), "texture", "#layer", "rotation", var34);
                  if (var3 || var4) {
                     var73.put("tintindex", var69);
                  }

                  var70.put(var31, var73);
               }
            }

            var9.add(JSONObject.of("from", writeDoubles(var14), "to", writeDoubles(var15), "faces", var70));
         }
      }

      float var47 = var10 / 24.0F;

      for (Object var50 : var9) {
         for (String var17 : new String[]{"from", "to"}) {
            JSONArray var59 = ((JSONObject)var50).getJSONArray(var17);

            for (int var19 = 0; var19 < 3; var19++) {
               var59.set(var19, var59.getDoubleValue(var19) / var47 + 8.0);
            }
         }
      }

      JSONArray var49 = new JSONArray();

      for (Object var53 : var9) {
         JSONObject var55 = (JSONObject)var53;
         JSONObject var57 = new JSONObject();

         for (String var60 : var55.getJSONObject("faces").keySet()) {
            JSONObject var61 = var55.getJSONObject("faces").getJSONObject(var60);
            JSONArray var62 = (JSONArray)var61.getJSONArray("uv").clone();
            if (!var60.equals("up") && !var60.equals("down")) {
               Object var63 = var62.get(1);
               var62.set(1, var62.get(3));
               var62.set(3, var63);
            } else {
               Object var21 = var62.get(0);
               var62.set(0, var62.get(2));
               var62.set(2, var21);
            }

            JSONObject var64 = new JSONObject(var61);
            var64.put("uv", var62);
            var64.put("rotation", (360 - var61.getIntValue("rotation")) % 360);

            String var65 = switch (var60) {
               case "north" -> "south";
               case "south" -> "north";
               case "east" -> "west";
               case "west" -> "east";
               case "up" -> "down";
               default -> "up";
            };
            var57.put(var65, var64);
         }

         var49.add(JSONObject.of("from", var55.getJSONArray("to").clone(), "to", var55.getJSONArray("from").clone(), "faces", var57));
      }

      var9.addAll(var49);
      return new JsonTransformEntry(
         JSONObject.of("textures", JSONObject.of("layer", var2, "particle", "#layer"), "elements", var9, "display", new JSONObject()), var47
      );
   }

   public static Matrix4f computeMatrix(EquipmentSlotRecord var0, float var1) {
      Vector3fRecord var2 = var0.translation();
      Vector3fRecord var3 = var0.rotation();
      Vector3fRecord var4 = var0.scale();
      return new Matrix4f()
         .translation(var2.x() / 16.0F, var2.y() / 16.0F, var2.z() / 16.0F)
         .rotateZYX((float)Math.toRadians(var3.z()), (float)Math.toRadians(var3.y()), (float)Math.toRadians(var3.x()))
         .scale(var4.x() * var1, var4.y() * var1, var4.z() * var1);
   }

   private static double[] readDoubles(JSONArray var0) {
      double[] var1 = new double[var0.size()];

      for (int var2 = 0; var2 < var1.length; var2++) {
         var1[var2] = var0.getDoubleValue(var2);
      }

      return var1;
   }

   private static JSONArray writeDoubles(double[] var0) {
      JSONArray var1 = new JSONArray();

      for (double var5 : var0) {
         var1.add(var5);
      }

      return var1;
   }

   static {
      try (InputStream var0 = TransformJsonCodec.class.getResourceAsStream("/entities/humanoid-equipment-v1.json")) {
         if (var0 == null) {
            throw new IOException("Missing humanoid equipment descriptor");
         }

         byte[] var1 = var0.readAllBytes();
         Cw = JSON.parseObject(var1);
         Cx = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var1));
         if (Cw.getIntValue("version") != 1) {
            throw new IOException("Unsupported humanoid equipment descriptor");
         }
      } catch (IOException | NoSuchAlgorithmException var5) {
         throw new ExceptionInInitializerError(var5);
      }
   }
}
