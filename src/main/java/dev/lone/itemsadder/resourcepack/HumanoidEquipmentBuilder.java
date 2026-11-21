package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.entity.EquipmentItemRecord;
import dev.lone.itemsadder.entity.EquipmentSlotRecord;
import dev.lone.itemsadder.entity.HumanoidEquipmentSet;
import dev.lone.itemsadder.entity.JsonTransformEntry;
import dev.lone.itemsadder.entity.TransformJsonCodec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public final class HumanoidEquipmentBuilder {
   private static final String buZ = "_iainternal";
   private static final String bva = "humanoid_equipment/";
   private static final JSONObject bvb = JSONObject.of("type", "minecraft:empty");

   private HumanoidEquipmentBuilder() {
   }

   public static String buildSlotKey(HumanoidEquipmentSet var0, EquipmentSlotRecord var1) {
      StringBuilder var2 = new StringBuilder(TransformJsonCodec.Cx).append('\n').append(var0.pe()).append('\n');
      var0.pg()
         .entrySet()
         .stream()
         .sorted(Entry.comparingByKey())
         .forEach(
            var1x -> var2.append((String)var1x.getKey()).append('=').append(Integer.toHexString(Float.floatToIntBits((Float)var1x.getValue()))).append('\n')
         );
      var2.append(var1.ph()).append('\n').append(var1.pi()).append('\n').append(Integer.toHexString(Float.floatToIntBits(var1.size())));
      return "_iainternal:humanoid_equipment/" + sha256Hex(var2.toString());
   }

   public static PackBuildPlan buildPlan(String var0, Map var1, List var2) {
      PackDefinition var3 = PackImageUtils.parseDefinition(var0, var1);
      LinkedHashMap var4 = new LinkedHashMap();
      LinkedHashMap var5 = new LinkedHashMap();
      JSONArray var6 = new JSONArray();
      LinkedHashSet var7 = new LinkedHashSet();
      LinkedHashMap var8 = new LinkedHashMap();
      LinkedHashMap var9 = new LinkedHashMap();

      for (HumanoidEquipmentSet var11 : var2) {
         for (EquipmentSlotRecord var13 : var11.pf()) {
            String var14 = buildSlotKey(var11, var13);
            var8.putIfAbsent(var14, var11);
            var9.putIfAbsent(var14, var13);
         }
      }

      for (String var29 : var8.keySet()) {
         HumanoidEquipmentSet var30 = (HumanoidEquipmentSet)var8.get(var29);
         EquipmentSlotRecord var31 = (EquipmentSlotRecord)var9.get(var29);
         JSONArray var32 = new JSONArray();
         String var15 = var31.ph().equals("legs") ? "humanoid_leggings" : "humanoid";

         for (Entry var17 : var3.equipment().entrySet().stream().sorted(Entry.comparingByKey()).toList()) {
            List var18 = (List)((PackPlan)var17.getValue()).layers().get(var15);
            if (var18 != null && !var18.isEmpty()) {
               JSONArray var19 = new JSONArray();

               for (int var20 = 0; var20 < var18.size(); var20++) {
                  PackOverrideEntry var21 = (PackOverrideEntry)var18.get(var20);
                  String var22 = equipmentTextureId("base", var21.texture());
                  copyTexturePair(var1, var4, var21.texture(), var22);
                  addSingleSprite(var6, var7, var22);
                  String var23 = equipmentModelId(var29, (String)var17.getKey() + "/base/" + var20);
                  JsonTransformEntry var24 = TransformJsonCodec.createEntry(var30, var31, var22, var21.dyeable(), false, var20 * 0.005F);
                  putModelJson(var4, var23, var24);
                  recordGeometryScale(var5, var29, var24.lZ());
                  JSONObject var25 = modelRef(var23);
                  if (var21.dyeable()) {
                     JSONObject var26 = modelRef(var23);
                     var26.put("tints", new JSONArray().fluentAdd(customModelDataIndex(0, -1)));
                     JSONObject var27 = var21.undyed() == null ? bvb : modelRef(var23);
                     if (var21.undyed() != null) {
                        var27.put("tints", new JSONArray().fluentAdd(JSONObject.of("type", "minecraft:constant", "value", var21.undyed())));
                     }

                     var25 = JSONObject.of(
                        "type", "minecraft:condition", "property", "minecraft:custom_model_data", "index", 0, "on_true", var26, "on_false", var27
                     );
                  }

                  var19.add(var25);
               }

               var32.add(JSONObject.of("when", var17.getKey(), "model", JSONObject.of("type", "minecraft:composite", "models", var19)));
            }
         }

         if (!var32.isEmpty()) {
            JSONArray var33 = new JSONArray()
               .fluentAdd(JSONObject.of("type", "minecraft:select", "property", "minecraft:custom_model_data", "index", 0, "cases", var32, "fallback", bvb));
            JSONArray var34 = buildTrimCases(var0, var1, var3, var30, var31, var29, var15, var4, var6, var7, var5);
            if (!var34.isEmpty()) {
               var33.add(JSONObject.of("type", "minecraft:select", "property", "minecraft:custom_model_data", "index", 1, "cases", var34, "fallback", bvb));
            }

            var4.put(assetJsonPath("items", var29), JSON.toJSONBytes(JSONObject.of("model", JSONObject.of("type", "minecraft:composite", "models", var33))));
         }
      }

      return new PackBuildPlan(Map.copyOf(var4), var6, var3, Map.copyOf(var5));
   }

   private static JSONArray buildTrimCases(
      String var0,
      Map var1,
      PackDefinition var2,
      HumanoidEquipmentSet var3,
      EquipmentSlotRecord var4,
      String var5,
      String var6,
      Map var7,
      JSONArray var8,
      Set var9,
      Map var10
   ) {
      LinkedHashMap var11 = new LinkedHashMap();

      for (String var13 : var2.equipment().keySet().stream().sorted().toList()) {
         for (String var15 : var2.patterns().keySet().stream().sorted().toList()) {
            for (String var17 : var2.materials().keySet().stream().sorted().toList()) {
               EquipmentItemRecord var18 = new EquipmentItemRecord(var13, null, var15, var17, false);
               PackTemplateGroup var19 = var2.resolveTemplate(var18, var6);
               if (var19 != null) {
                  String var20 = (var19.colors() == null ? "direct/" : "packed/") + var19.texture();
                  var11.putIfAbsent(var20, var19);
               }
            }
         }
      }

      JSONArray var24 = new JSONArray();

      for (Entry var26 : var11.entrySet()) {
         String var27 = ((PackTemplateGroup)var26.getValue()).texture();
         boolean var28 = ((PackTemplateGroup)var26.getValue()).colors() != null;
         String var29 = equipmentTextureId(var28 ? "masks" : "direct", var27);
         if (var28) {
            String var30 = PackImageUtils.texturePath(var27);
            String var32 = resolveTrimBasePalette(var0, var1, var27);
            if (!var7.containsKey(texturePath(var29))) {
               var7.put(texturePath(var29), PackImageUtils.encodeTrimMasks(PackImageUtils.loadTexture(var1, var30), PackImageUtils.loadTexture(var1, var32)));
            }
         } else {
            copyTexturePair(var1, var7, var27, var29);
         }

         addSingleSprite(var8, var9, var29);
         String var31 = equipmentModelId(var5, "trim/" + (String)var26.getKey());
         int var33 = var2.equipment().values().stream().mapToInt(var1x -> var1x.layers().getOrDefault(var6, List.of()).size()).max().orElse(1);
         JsonTransformEntry var34 = TransformJsonCodec.createEntry(var3, var4, var29, var28, var28, Math.max(0.01F, (var33 - 1) * 0.005F + 0.005F));
         putModelJson(var7, var31, var34);
         recordGeometryScale(var10, var5, var34.lZ());
         JSONObject var21 = modelRef(var31);
         if (var28) {
            JSONArray var22 = new JSONArray();

            for (int var23 = 1; var23 <= 8; var23++) {
               var22.add(customModelDataIndex(var23, 16777215));
            }

            var21.put("tints", var22);
         }

         var24.add(JSONObject.of("when", var26.getKey(), "model", var21));
      }

      return var24;
   }

   private static String resolveTrimBasePalette(String var0, Map var1, String var2) {
      if (var0.equals("26.3")) {
         String var11 = PackImageUtils.texturePath(var2) + ".mcmeta";
         byte[] var12 = (byte[])var1.get(var11);
         if (var12 == null) {
            throw new IOException("Missing trim palette metadata: " + var11);
         } else {
            JSONObject var13 = JSON.parseObject(var12).getJSONObject("palette");
            if (var13 != null && var13.getString("base_palette") != null) {
               return PackImageUtils.texturePath(PackImageUtils.withSubfolder(var13.getString("base_palette"), "palettes/"));
            } else {
               throw new IOException("Missing trim base palette: " + var11);
            }
         }
      } else {
         byte[] var3 = (byte[])var1.get("assets/minecraft/atlases/armor_trims.json");
         if (var3 == null) {
            throw new IOException("Missing armor trim atlas");
         }

         String[] var4 = var2.split(":", 2);

         for (Object var6 : JSON.parseObject(var3).getJSONArray("sources")) {
            JSONObject var7 = (JSONObject)var6;
            if ("paletted_permutations".equals(var7.getString("type")) || "minecraft:paletted_permutations".equals(var7.getString("type"))) {
               JSONArray var8 = var7.getJSONArray("textures");
               if (var8 != null) {
                  for (Object var10 : var8) {
                     if (var4[1].equals(var10) || var2.equals(var10)) {
                        return PackImageUtils.texturePath(var7.getString("palette_key"));
                     }
                  }
               }
            }
         }

         throw new IOException("Trim texture lacks base palette: " + var2);
      }
   }

   private static JSONObject modelRef(String var0) {
      return JSONObject.of("type", "minecraft:model", "model", var0);
   }

   private static JSONObject customModelDataIndex(int var0, int var1) {
      return JSONObject.of("type", "minecraft:custom_model_data", "index", var0, "default", var1);
   }

   private static void putModelJson(Map var0, String var1, JsonTransformEntry var2) {
      var0.put(assetJsonPath("models", var1), JSON.toJSONBytes(var2.pd()));
   }

   private static void recordGeometryScale(Map var0, String var1, float var2) {
      Float var3 = var0.putIfAbsent(var1, var2);
      if (var3 != null && Math.abs(var3 - var2) > 1.0E-6F) {
         throw new IOException("Equipment geometry scale mismatch for " + var1);
      }
   }

   private static String equipmentModelId(String var0, String var1) {
      return "_iainternal:humanoid_equipment/models/" + var0.substring(var0.lastIndexOf(47) + 1) + "/" + sha256Hex(var1);
   }

   private static String equipmentTextureId(String var0, String var1) {
      return "_iainternal:humanoid_equipment/" + var0 + "/" + sha256Hex(var1);
   }

   private static void copyTexturePair(Map var0, Map var1, String var2, String var3) {
      String var4 = PackImageUtils.texturePath(var2);
      byte[] var5 = (byte[])var0.get(var4);
      if (var5 == null) {
         throw new IOException("Missing equipment texture: " + var4);
      }

      var1.putIfAbsent(texturePath(var3), var5);
      byte[] var6 = (byte[])var0.get(var4 + ".mcmeta");
      if (var6 != null) {
         var1.putIfAbsent(texturePath(var3) + ".mcmeta", var6);
      }
   }

   private static void addSingleSprite(JSONArray var0, Set var1, String var2) {
      if (var1.add(var2)) {
         var0.add(JSONObject.of("type", "single", "resource", var2, "sprite", var2));
      }
   }

   private static String texturePath(String var0) {
      String[] var1 = var0.split(":", 2);
      return "assets/" + var1[0] + "/textures/" + var1[1] + ".png";
   }

   private static String assetJsonPath(String var0, String var1) {
      String[] var2 = var1.split(":", 2);
      return "assets/" + var2[0] + "/" + var0 + "/" + var2[1] + ".json";
   }

   private static String sha256Hex(String var0) {
      try {
         byte[] var1 = MessageDigest.getInstance("SHA-256").digest(var0.getBytes(StandardCharsets.UTF_8));
         StringBuilder var2 = new StringBuilder(var1.length * 2);

         for (byte var6 : var1) {
            var2.append(Character.forDigit(var6 >>> 4 & 15, 16)).append(Character.forDigit(var6 & 15, 16));
         }

         return var2.toString();
      } catch (NoSuchAlgorithmException var7) {
         throw new AssertionError(var7);
      }
   }
}
