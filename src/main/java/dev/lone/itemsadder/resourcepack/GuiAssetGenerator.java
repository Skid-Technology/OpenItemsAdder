package dev.lone.itemsadder.resourcepack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GuiAssetGenerator {
   private final String modelFolder;
   private static final String INTERNAL_NAMESPACE = "_iainternal";
   private final String itemModelDir;
   private final int layoutOffset;
   private static final double bCP = 0.0;
   private static final double SCALE_OFFSET_X = 0.1;
   private static final double SCALE_OFFSET_Y = 0.05;
   private static final double SCALE_OFFSET_Z = 0.15;
   private static final double TRANSLATION_OFFSET_X = 0.8;
   private static final double TRANSLATION_OFFSET_Y = -1.2;
   private static final double TRANSLATION_OFFSET_Z = 0.4;
   private static final int TEXTURE_WIDTH = 64;
   private static final int TEXTURE_HEIGHT = 32;
   private static final double bCY = 0.25;
   private static final int bCZ = 8;
   private static final int bDa = 12;
   private static final int bDb = 4;
   private static final int bDc = 8;
   private static final int bDd = 8;
   private static final int bDe = 8;
   private static final int bDf = 12;
   private static final int bDg = 4;
   private static final int bDh = 4;
   private static final int bDi = 3;
   private static final int bDj = 96;
   private static final int bDk = 48;
   private static final int bDl = 32;
   private static final int bDm = 64;
   private static final int bDn = 64;
   private static final int bDo = 64;
   private final Gson prettyGson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
   private final Gson compactGson = new GsonBuilder().disableHtmlEscaping().create();
   private final Map generatedFiles = new LinkedHashMap();

   public GuiAssetGenerator(boolean var1) {
      this.modelFolder = var1 ? "player_model_v2" : "player_model";
      this.itemModelDir = "_iainternal\\models\\item\\" + this.modelFolder;
      this.layoutOffset = var1 ? 8 : 0;
   }

   public Map generateAssets() {
      this.writeParentCube();
      this.buildLayerModel("body");
      this.buildLayerModel("head");
      this.buildLayerModel("left_arm");
      this.buildLayerModel("right_arm");
      this.buildLayerModel("left_arm_slim");
      this.buildLayerModel("right_arm_slim");
      this.buildLayerModel("left_leg");
      this.buildLayerModel("right_leg");
      GuiSlotLists var1 = this.buildTorsoSlots();
      GuiSlotLists var2 = this.buildHeadSlots();
      GuiSlotLists var3 = this.buildLimbSlots("right_arm", false);
      GuiSlotLists var4 = this.buildLimbSlots("left_arm", false);
      GuiSlotLists var5 = this.buildLimbSlots("right_arm", true);
      GuiSlotLists var6 = this.buildLimbSlots("left_arm", true);
      GuiSlotLists var7 = this.buildLimbSlots("right_leg", false);
      GuiSlotLists var8 = this.buildLimbSlots("left_leg", false);
      this.writeCompositeModel(var1.bDv, var1.bDw, identityPath("_iainternal\\items\\" + this.modelFolder + "\\torso.json"));
      this.writeCompositeModel(var2.bDv, var2.bDw, identityPath("_iainternal\\items\\" + this.modelFolder + "\\head.json"));
      this.writeCompositeModel(var3.bDv, var3.bDw, identityPath("_iainternal\\items\\" + this.modelFolder + "\\right_arm.json"));
      this.writeCompositeModel(var5.bDv, var5.bDw, identityPath("_iainternal\\items\\" + this.modelFolder + "\\right_arm_slim.json"));
      this.writeCompositeModel(var4.bDv, var4.bDw, identityPath("_iainternal\\items\\" + this.modelFolder + "\\left_arm.json"));
      this.writeCompositeModel(var6.bDv, var6.bDw, identityPath("_iainternal\\items\\" + this.modelFolder + "\\left_arm_slim.json"));
      this.writeCompositeModel(var7.bDv, var7.bDw, identityPath("_iainternal\\items\\" + this.modelFolder + "\\right_leg.json"));
      this.writeCompositeModel(var8.bDv, var8.bDw, identityPath("_iainternal\\items\\" + this.modelFolder + "\\left_leg.json"));
      this.writeCustomDataModel(var1.bDv, var1.bDw, identityPath("_iainternal\\items\\" + this.modelFolder + "\\torso.json"));
      this.writeCustomDataModel(var2.bDv, var2.bDw, identityPath("_iainternal\\items\\" + this.modelFolder + "\\head.json"));
      this.writeCustomDataModel(var3.bDv, var3.bDw, identityPath("_iainternal\\items\\" + this.modelFolder + "\\right_arm.json"));
      this.writeCustomDataModel(var5.bDv, var5.bDw, identityPath("_iainternal\\items\\" + this.modelFolder + "\\right_arm_slim.json"));
      this.writeCustomDataModel(var4.bDv, var4.bDw, identityPath("_iainternal\\items\\" + this.modelFolder + "\\left_arm.json"));
      this.writeCustomDataModel(var6.bDv, var6.bDw, identityPath("_iainternal\\items\\" + this.modelFolder + "\\left_arm_slim.json"));
      this.writeCustomDataModel(var7.bDv, var7.bDw, identityPath("_iainternal\\items\\" + this.modelFolder + "\\right_leg.json"));
      this.writeCustomDataModel(var8.bDv, var8.bDw, identityPath("_iainternal\\items\\" + this.modelFolder + "\\left_leg.json"));
      this.minifyJsonFiles();
      return Collections.unmodifiableMap(this.generatedFiles);
   }

   private static String identityPath(String var0) {
      return var0;
   }

   private List rectangleUv(double var1, double var3, double var5, double var7, boolean var9, boolean var10) {
      double var11 = var1;
      double var13 = var3;
      double var15 = var1 + var5;
      double var17 = var3 + var7;
      if (var9) {
         double var19 = var11;
         var11 = var15;
         var15 = var19;
      }

      if (var10) {
         double var21 = var13;
         var13 = var17;
         var17 = var21;
      }

      return Arrays.asList(var11, var13, var15, var17);
   }

   private GuiTextureRef cubeTextureRef(String var1, String var2, int var3, String var4) {
      String var5 = var2 != null ? var2 + "_cube_" + var3 + var4 : var1 + "_cube_" + var3 + var4;
      GuiTextureRef var6 = new GuiTextureRef();
      var6.bDx = this.itemModelDir + "\\" + var5 + ".json";
      var6.As = "_iainternal:item/" + this.modelFolder + "/" + var5;
      return var6;
   }

   private GuiTextureRef parentCubeRef(String var1, String var2) {
      String var3 = var2 != null ? var2 + "_cube" : var1 + "_cube";
      GuiTextureRef var4 = new GuiTextureRef();
      var4.bDx = this.itemModelDir + "\\" + var3 + ".json";
      var4.As = "_iainternal:item/" + this.modelFolder + "/" + var3;
      return var4;
   }

   private GuiLayers layersFor(String var1, String var2) {
      List var3 = Arrays.asList(90, 0, 0);
      if ("body".equals(var1)) {
         return this.createLayers(Arrays.asList(6.6, -10.0, 23.5), Arrays.asList(0.96, 0.94, 1.0), var3);
      } else if ("head".equals(var1)) {
         return this.createLayers(Arrays.asList(6.6, -9.9, 21.1), Arrays.asList(0.96, 0.96, 0.96), var3);
      } else if ("left_arm".equals(var2)) {
         return this.createLayers(Arrays.asList(6.0, -10.0, 13.5), Arrays.asList(0.96, 0.93, 1.0), var3);
      } else if ("right_arm".equals(var2)) {
         return this.createLayers(Arrays.asList(7.1, -10.0, 13.5), Arrays.asList(0.96, 0.93, 1.0), var3);
      } else if ("left_arm_slim".equals(var2)) {
         return this.createLayers(Arrays.asList(6.5, -10.0, 13.5), Arrays.asList(0.96, 0.93, 1.0), var3);
      } else if ("right_arm_slim".equals(var2)) {
         return this.createLayers(Arrays.asList(6.6, -10.0, 13.5), Arrays.asList(0.96, 0.93, 1.0), var3);
      } else if ("right_leg".equals(var2)) {
         return this.createLayers(Arrays.asList(6.6, -10.0, 12.2), Arrays.asList(0.96, 0.93, 1.0), var3);
      } else {
         return "left_leg".equals(var2)
            ? this.createLayers(Arrays.asList(6.5, -10.0, 12.2), Arrays.asList(0.96, 0.93, 1.0), var3)
            : this.createLayers(Arrays.asList(5.8, -10.0, 14.3), Arrays.asList(1.0, 1.0, 1.0), var3);
      }
   }

   private GuiLayers createLayers(List var1, List var2, List var3) {
      GuiLayers var4 = new GuiLayers();
      var4.bDs = var1;
      var4.bDt = var2;
      var4.bDu = var3;
      return var4;
   }

   private void putGeneratedFile(String var1, JsonElement var2) {
      String var3 = this.prettyGson.toJson(var2);
      this.generatedFiles.put(var1, var3.getBytes(StandardCharsets.UTF_8));
   }

   private JsonObject readGeneratedFile(String var1) {
      byte[] var2 = (byte[])this.generatedFiles.get(var1);
      if (var2 == null) {
         throw new IllegalStateException("File not found in memory: " + var1);
      }

      String var3 = new String(var2, StandardCharsets.UTF_8);
      return JsonParser.parseString(var3).getAsJsonObject();
   }

   private String toInternalPath(String var1) {
      String var2 = "_iainternal:item/" + this.modelFolder + "/";
      if (!var1.startsWith(var2)) {
         throw new IllegalArgumentException("Unexpected modelPath " + var1);
      }

      String var3 = var1.substring(var2.length());
      return this.itemModelDir + "\\" + var3 + ".json";
   }

   private String buildGuiCube(int var1, double var2, double var4, double var6, double var8, double var10, double var12, Map var14, String var15, String var16) {
      JsonObject var17 = new JsonObject();
      String var18 = var16 != null ? var16 : var15;
      var17.addProperty("parent", "_iainternal:item/" + this.modelFolder + "/" + var18 + "_cube");
      double var19 = this.layoutOffset;
      double var21 = this.layoutOffset;
      if (this.layoutOffset != 0) {
         var19 += switch (var18) {
            case "left_arm", "left_arm_slim" -> 0.25;
            case "right_leg" -> -0.1;
            default -> 0.0;
         };

         var21 += switch (var18) {
            case "body" -> 6;
            case "head" -> 4;
            case "left_arm", "left_arm_slim", "right_arm", "right_arm_slim" -> -4;
            default -> -6;
         };
      }

      JsonObject var30 = new JsonObject();
      var30.addProperty("shade", false);
      JsonArray var32 = new JsonArray();
      var32.add(var2 + var19);
      var32.add(var4 + var21);
      var32.add(var6 + this.layoutOffset);
      var30.add("from", var32);
      JsonArray var25 = new JsonArray();
      var25.add(var2 + var8 + 0.0 + var19);
      var25.add(var4 + var10 + 0.0 + var21);
      var25.add(var6 + var12 + 0.0 + this.layoutOffset);
      var30.add("to", var25);
      JsonObject var26 = new JsonObject();

      for (Entry var28 : var14.entrySet()) {
         var26.add((String)var28.getKey(), (JsonElement)var28.getValue());
      }

      var30.add("faces", var26);
      JsonArray var33 = new JsonArray();
      var33.add(var30);
      var17.add("elements", var33);
      GuiTextureRef var34 = this.cubeTextureRef(var15, var16, var1, "");
      this.putGeneratedFile(var34.bDx, var17);
      return var34.As;
   }

   private String buildLayerModel(String var1) {
      GuiLayers var2 = this.layersFor(var1, var1);
      JsonObject var3 = new JsonObject();
      var3.addProperty("parent", "_iainternal:item/" + this.modelFolder + "/parent_cube");
      JsonObject var4 = new JsonObject();
      JsonObject var5 = new JsonObject();
      var5.add("translation", this.toDoubleArray(var2.bDs));
      var5.add("scale", this.toDoubleArray(var2.bDt));
      var5.add("rotation", this.toIntArray(var2.bDu));
      var4.add("thirdperson_righthand", var5);
      var3.add("display", var4);
      GuiTextureRef var6 = this.parentCubeRef(var1, var1);
      this.putGeneratedFile(var6.bDx, var3);
      return var6.As;
   }

   private String writeParentCube() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("parent", "item/generated");
      JsonObject var2 = new JsonObject();
      var2.addProperty("0", "_iainternal:item/player_model/player_model_pixel");
      var2.addProperty("particle", "#0");
      var1.add("textures", var2);
      var1.addProperty("ambientocclusion", false);
      String var3 = this.itemModelDir + "\\parent_cube.json";
      this.putGeneratedFile(var3, var1);
      return "_iainternal:item/" + this.modelFolder + "/parent_cube";
   }

   private String buildPixelLayer(String var1, String var2, String var3) {
      String var4 = this.toInternalPath(var1);
      JsonObject var5 = this.readGeneratedFile(var4);
      String var6 = var5.get("parent").getAsString();
      String var7 = this.itemModelDir + "\\" + var6.substring(var6.lastIndexOf(47) + 1) + ".json";
      JsonObject var8 = this.readGeneratedFile(var7);
      JsonObject var9 = var8.getAsJsonObject("display").getAsJsonObject("thirdperson_righthand");
      List var10 = this.readDoubles(var9.getAsJsonArray("translation"));
      List var11 = this.readDoubles(var9.getAsJsonArray("scale"));
      List var12 = this.readInts(var9.getAsJsonArray("rotation"));
      JsonObject var13 = new JsonObject();
      var13.addProperty("parent", "_iainternal:item/" + this.modelFolder + "/parent_cube");
      JsonObject var14 = new JsonObject();
      JsonObject var15 = new JsonObject();
      var15.add("translation", this.toDoubleArray(Arrays.asList((Double)var10.get(0) + 0.8, (Double)var10.get(1) + -1.2, (Double)var10.get(2) + 0.4)));
      var15.add("scale", this.toDoubleArray(Arrays.asList((Double)var11.get(0) + 0.1, (Double)var11.get(1) + 0.05, (Double)var11.get(2) + 0.15)));
      var15.add("rotation", this.toIntArray(var12));
      var14.add("thirdperson_righthand", var15);
      var13.add("display", var14);
      String var16;
      if (var3 != null) {
         var16 = this.itemModelDir + "\\parent_" + var3 + "_pixel_layer2.json";
      } else {
         var16 = this.itemModelDir + "\\parent_" + var2 + "_pixel_layer2.json";
      }

      this.putGeneratedFile(var16, var13);
      return this.toItemId(var16);
   }

   private String buildPixelLayerForFace(String var1, int var2, int var3, String var4, JsonObject var5, String var6, String var7) {
      String var8 = this.toInternalPath(var1);
      JsonObject var9 = this.readGeneratedFile(var8);
      JsonObject var10 = var9.getAsJsonArray("elements").get(0).getAsJsonObject();
      JsonObject var11 = new JsonObject();
      String var12 = "_iainternal:item/" + this.modelFolder + "/parent_" + (var7 != null ? var7 : var6) + "_pixel_layer2";
      var11.addProperty("parent", var12);
      JsonObject var13 = new JsonObject();
      var13.addProperty("shade", false);
      var13.add("from", var10.get("from").getAsJsonArray().deepCopy());
      var13.add("to", var10.get("to").getAsJsonArray().deepCopy());
      JsonObject var14 = new JsonObject();
      JsonObject var15 = new JsonObject();
      var15.add("uv", var5.get("uv").getAsJsonArray().deepCopy());
      var15.addProperty("texture", "#0");
      var15.addProperty("tintindex", 0);
      var14.add(var4, var15);
      var13.add("faces", var14);
      JsonArray var16 = new JsonArray();
      var16.add(var13);
      var11.add("elements", var16);
      String var17 = var2 + "_" + var3;
      String var18;
      String var19;
      if (var7 != null) {
         var18 = this.itemModelDir + "\\" + var7 + "_pixel_" + var17 + "_layer2.json";
         var19 = "_iainternal:item/" + this.modelFolder + "/" + var7 + "_pixel_" + var17 + "_layer2";
      } else {
         var18 = this.itemModelDir + "\\" + var6 + "_pixel_" + var17 + "_layer2.json";
         var19 = "_iainternal:item/" + this.modelFolder + "/" + var6 + "_pixel_" + var17 + "_layer2";
      }

      this.putGeneratedFile(var18, var11);
      return var19;
   }

   private String toItemId(String var1) {
      String var2 = var1.substring(this.itemModelDir.length() + 1, var1.length() - ".json".length());
      return "_iainternal:item/" + this.modelFolder + "/" + var2;
   }

   private JsonArray toDoubleArray(List var1) {
      JsonArray var2 = new JsonArray();

      for (Double var4 : var1) {
         var2.add(var4);
      }

      return var2;
   }

   private JsonArray toIntArray(List var1) {
      JsonArray var2 = new JsonArray();

      for (Integer var4 : var1) {
         var2.add(var4);
      }

      return var2;
   }

   private List readDoubles(JsonArray var1) {
      ArrayList var2 = new ArrayList(var1.size());

      for (JsonElement var4 : var1) {
         var2.add(var4.getAsDouble());
      }

      return var2;
   }

   private List readInts(JsonArray var1) {
      ArrayList var2 = new ArrayList(var1.size());

      for (JsonElement var4 : var1) {
         var2.add(var4.getAsInt());
      }

      return var2;
   }

   private JsonObject buildFaceUv(List var1, int var2) {
      JsonObject var3 = new JsonObject();
      JsonArray var4 = new JsonArray();

      for (Double var6 : var1) {
         var4.add(var6);
      }

      var3.add("uv", var4);
      var3.addProperty("texture", "#0");
      var3.addProperty("tintindex", var2);
      return var3;
   }

   private GuiSlotLists buildTorsoSlots() {
      byte var1 = 8;
      byte var2 = 12;
      byte var3 = 4;
      double var4 = var1;
      double var6 = var2;
      double var8 = var3;
      double var10 = -var4 / 2.0;
      double var12 = -var6 / 2.0;
      double var14 = -var8 / 2.0;
      ArrayList var16 = new ArrayList();
      ArrayList var17 = new ArrayList();
      int var18 = 0;
      double var19 = var1 * 0.25;
      double var21 = var2 * 0.25;
      double var23 = var3 * 0.25;
      double var25 = var2 * 0.25;
      double var27 = var1 * 0.25;
      double var29 = var3 * 0.25;
      int var31 = 0;

      for (int var32 = 0; var32 < var2; var32++) {
         for (int var33 = 0; var33 < var1; var33++) {
            LinkedHashMap var34 = new LinkedHashMap();
            ArrayList var35 = new ArrayList();
            double var36 = (32 - var1 + (var1 - 1 - var33)) * 0.25;
            double var38 = (32 - var2 + (var2 - 1 - var32)) * 0.25;
            int var40 = var32 * var1 + var33;
            var34.put("north", this.buildFaceUv(this.rectangleUv(var36, var38, var19, var21, false, false), 0));
            var35.add(var40);
            if (var33 == var1 - 1) {
               var36 = (32 - var3 + (var3 - 1 - var31)) * 0.25;
               var38 = (32 - var2 + (var2 - 1 - var32)) * 0.25;
               int var41 = 192 + var32 * var3 + var31;
               var34.put("east", this.buildFaceUv(this.rectangleUv(var36, var38, var23, var25, false, false), var35.size()));
               var35.add(var41);
            }

            if (var33 == 0) {
               var36 = (32 - var3 + (var3 - 1 - var31)) * 0.25;
               var38 = (32 - var2 + (var2 - 1 - var32)) * 0.25;
               int var107 = 192 + var3 * var2 + var32 * var3 + var31;
               var34.put("west", this.buildFaceUv(this.rectangleUv(var36, var38, var23, var25, false, false), var35.size()));
               var35.add(var107);
            }

            if (var32 == var2 - 1) {
               var36 = (32 - var1 + var33) * 0.25;
               var38 = (32 - var3 + var31) * 0.25;
               int var108 = 192 + var3 * var2 * 2 + var31 * var1 + var33;
               var34.put("up", this.buildFaceUv(this.rectangleUv(var36, var38, var27, var29, false, false), var35.size()));
               var35.add(var108);
            }

            if (var32 == 0) {
               var36 = (32 - var1 + var33) * 0.25;
               var38 = (32 - var3 + var31) * 0.25;
               int var109 = 192 + var3 * var2 * 2 + 32 + var31 * var1 + var33;
               var34.put("down", this.buildFaceUv(this.rectangleUv(var36, var38, var27, var29, false, false), var35.size()));
               var35.add(var109);
            }

            String var110 = this.buildGuiCube(var18, var10, var12, var14, var4, var6, var8, var34, "body", null);
            var16.add(var110);
            var17.add(var35);
            var18++;
         }
      }

      var31 = var3 - 1;

      for (int var47 = 0; var47 < var2; var47++) {
         for (int var52 = 0; var52 < var1; var52++) {
            LinkedHashMap var57 = new LinkedHashMap();
            ArrayList var63 = new ArrayList();
            double var72 = (32 - var1 + (var1 - 1 - var52)) * 0.25;
            double var89 = (32 - var2 + (var2 - 1 - var47)) * 0.25;
            int var102 = 96 + var47 * var1 + var52;
            var57.put("south", this.buildFaceUv(this.rectangleUv(var72, var89, var19, var21, false, false), 0));
            var63.add(var102);
            if (var52 == var1 - 1) {
               var72 = (32 - var3 + (var3 - 1 - var31)) * 0.25;
               var89 = (32 - var2 + (var2 - 1 - var47)) * 0.25;
               int var111 = 192 + var47 * var3 + var31;
               var57.put("east", this.buildFaceUv(this.rectangleUv(var72, var89, var23, var25, false, false), var63.size()));
               var63.add(var111);
            }

            if (var52 == 0) {
               var72 = (32 - var3 + (var3 - 1 - var31)) * 0.25;
               var89 = (32 - var2 + (var2 - 1 - var47)) * 0.25;
               int var112 = 192 + var3 * var2 + var47 * var3 + var31;
               var57.put("west", this.buildFaceUv(this.rectangleUv(var72, var89, var23, var25, false, false), var63.size()));
               var63.add(var112);
            }

            if (var47 == var2 - 1) {
               var72 = (32 - var1 + var52) * 0.25;
               var89 = (32 - var3 + var31) * 0.25;
               int var113 = 192 + var3 * var2 * 2 + var31 * var1 + var52;
               var57.put("up", this.buildFaceUv(this.rectangleUv(var72, var89, var27, var29, false, false), var63.size()));
               var63.add(var113);
            }

            if (var47 == 0) {
               var72 = (32 - var1 + var52) * 0.25;
               var89 = (32 - var3 + var31) * 0.25;
               int var114 = 192 + var3 * var2 * 2 + 32 + var31 * var1 + var52;
               var57.put("down", this.buildFaceUv(this.rectangleUv(var72, var89, var27, var29, false, false), var63.size()));
               var63.add(var114);
            }

            String var115 = this.buildGuiCube(var18, var10, var12, var14, var4, var6, var8, var57, "body", null);
            var16.add(var115);
            var17.add(var63);
            var18++;
         }
      }

      int var48 = var1 - 1;

      for (int var53 = 0; var53 < var2; var53++) {
         for (int var43 = 1; var43 < var3 - 1; var43++) {
            LinkedHashMap var58 = new LinkedHashMap();
            ArrayList var64 = new ArrayList();
            double var77 = (32 - var3 + (var3 - 1 - var43)) * 0.25;
            double var94 = (32 - var2 + (var2 - 1 - var53)) * 0.25;
            int var103 = 192 + var53 * var3 + var43;
            var58.put("east", this.buildFaceUv(this.rectangleUv(var77, var94, var23, var25, false, false), 0));
            var64.add(var103);
            if (var53 == var2 - 1) {
               var77 = (32 - var1 + var48) * 0.25;
               var94 = (32 - var3 + var43) * 0.25;
               int var116 = 192 + var3 * var2 * 2 + var43 * var1 + var48;
               var58.put("up", this.buildFaceUv(this.rectangleUv(var77, var94, var27, var29, false, false), var64.size()));
               var64.add(var116);
            }

            if (var53 == 0) {
               var77 = (32 - var1 + var48) * 0.25;
               var94 = (32 - var3 + var43) * 0.25;
               int var117 = 192 + var3 * var2 * 2 + 32 + var43 * var1 + var48;
               var58.put("down", this.buildFaceUv(this.rectangleUv(var77, var94, var27, var29, false, false), var64.size()));
               var64.add(var117);
            }

            String var118 = this.buildGuiCube(var18, var10, var12, var14, var4, var6, var8, var58, "body", null);
            var16.add(var118);
            var17.add(var64);
            var18++;
         }
      }

      byte var49 = 0;

      for (int var54 = 0; var54 < var2; var54++) {
         for (int var44 = 1; var44 < var3 - 1; var44++) {
            LinkedHashMap var59 = new LinkedHashMap();
            ArrayList var65 = new ArrayList();
            double var80 = (32 - var3 + (var3 - 1 - var44)) * 0.25;
            double var97 = (32 - var2 + (var2 - 1 - var54)) * 0.25;
            int var104 = 192 + var3 * var2 + var54 * var3 + var44;
            var59.put("west", this.buildFaceUv(this.rectangleUv(var80, var97, var23, var25, false, false), 0));
            var65.add(var104);
            if (var54 == var2 - 1) {
               var80 = (32 - var1 + var49) * 0.25;
               var97 = (32 - var3 + var44) * 0.25;
               int var119 = 192 + var3 * var2 * 2 + var44 * var1 + var49;
               var59.put("up", this.buildFaceUv(this.rectangleUv(var80, var97, var27, var29, false, false), var65.size()));
               var65.add(var119);
            }

            if (var54 == 0) {
               var80 = (32 - var1 + var49) * 0.25;
               var97 = (32 - var3 + var44) * 0.25;
               int var120 = 192 + var3 * var2 * 2 + 32 + var44 * var1 + var49;
               var59.put("down", this.buildFaceUv(this.rectangleUv(var80, var97, var27, var29, false, false), var65.size()));
               var65.add(var120);
            }

            String var121 = this.buildGuiCube(var18, var10, var12, var14, var4, var6, var8, var59, "body", null);
            var16.add(var121);
            var17.add(var65);
            var18++;
         }
      }

      int var55 = var2 - 1;

      for (int var45 = 1; var45 < var3 - 1; var45++) {
         for (int var50 = 1; var50 < var1 - 1; var50++) {
            LinkedHashMap var60 = new LinkedHashMap();
            ArrayList var66 = new ArrayList();
            double var83 = (32 - var1 + var50) * 0.25;
            double var100 = (32 - var3 + var45) * 0.25;
            int var105 = 192 + var3 * var2 * 2 + var45 * var1 + var50;
            var60.put("up", this.buildFaceUv(this.rectangleUv(var83, var100, var27, var29, false, false), 0));
            var66.add(var105);
            String var122 = this.buildGuiCube(var18, var10, var12, var14, var4, var6, var8, var60, "body", null);
            var16.add(var122);
            var17.add(var66);
            var18++;
         }
      }

      boolean var56 = false;

      for (int var46 = 1; var46 < var3 - 1; var46++) {
         for (int var51 = 1; var51 < var1 - 1; var51++) {
            LinkedHashMap var61 = new LinkedHashMap();
            ArrayList var67 = new ArrayList();
            double var84 = (32 - var1 + var51) * 0.25;
            double var101 = (32 - var3 + var46) * 0.25;
            int var106 = 192 + var3 * var2 * 2 + 32 + var46 * var1 + var51;
            var61.put("down", this.buildFaceUv(this.rectangleUv(var84, var101, var27, var29, false, false), 0));
            var67.add(var106);
            String var123 = this.buildGuiCube(var18, var10, var12, var14, var4, var6, var8, var61, "body", null);
            var16.add(var123);
            var17.add(var67);
            var18++;
         }
      }

      GuiSlotLists var62 = new GuiSlotLists();
      var62.bDv = var16;
      var62.bDw = var17;
      return var62;
   }

   private GuiSlotLists buildHeadSlots() {
      byte var1 = 8;
      byte var2 = 8;
      byte var3 = 8;
      double var4 = var1;
      double var6 = var2;
      double var8 = var3;
      double var10 = -var4 / 2.0;
      double var12 = -var6 / 2.0;
      double var14 = -var8 / 2.0;
      ArrayList var16 = new ArrayList();
      ArrayList var17 = new ArrayList();
      int var18 = 0;
      double var19 = var1 * 0.25;
      double var21 = var2 * 0.25;
      double var23 = var3 * 0.25;
      double var25 = var2 * 0.25;
      double var27 = var1 * 0.25;
      double var29 = var3 * 0.25;
      int var31 = 0;

      for (int var32 = 0; var32 < var2; var32++) {
         for (int var33 = 0; var33 < var1; var33++) {
            LinkedHashMap var34 = new LinkedHashMap();
            ArrayList var35 = new ArrayList();
            double var36 = (32 - var1 + (var1 - 1 - var33)) * 0.25;
            double var38 = (32 - var2 + (var2 - 1 - var32)) * 0.25;
            int var40 = var32 * var1 + var33;
            var34.put("north", this.buildFaceUv(this.rectangleUv(var36, var38, var19, var21, false, false), 0));
            var35.add(var40);
            if (var33 == var1 - 1) {
               var36 = (32 - var3 + (var3 - 1 - var31)) * 0.25;
               var38 = (32 - var2 + (var2 - 1 - var32)) * 0.25;
               int var41 = 128 + var32 * var3 + var31;
               var34.put("east", this.buildFaceUv(this.rectangleUv(var36, var38, var23, var25, false, false), var35.size()));
               var35.add(var41);
            }

            if (var33 == 0) {
               var36 = (32 - var3 + (var3 - 1 - var31)) * 0.25;
               var38 = (32 - var2 + (var2 - 1 - var32)) * 0.25;
               int var107 = 128 + var3 * var2 + var32 * var3 + var31;
               var34.put("west", this.buildFaceUv(this.rectangleUv(var36, var38, var23, var25, false, false), var35.size()));
               var35.add(var107);
            }

            if (var32 == var2 - 1) {
               var36 = (32 - var1 + var33) * 0.25;
               var38 = (32 - var3 + var31) * 0.25;
               int var108 = 128 + var3 * var2 * 2 + var31 * var1 + var33;
               var34.put("up", this.buildFaceUv(this.rectangleUv(var36, var38, var27, var29, false, false), var35.size()));
               var35.add(var108);
            }

            if (var32 == 0) {
               var36 = (32 - var1 + var33) * 0.25;
               var38 = (32 - var3 + var31) * 0.25;
               int var109 = 128 + var3 * var2 * 2 + 64 + var31 * var1 + var33;
               var34.put("down", this.buildFaceUv(this.rectangleUv(var36, var38, var27, var29, true, true), var35.size()));
               var35.add(var109);
            }

            String var110 = this.buildGuiCube(var18, var10, var12, var14, var4, var6, var8, var34, "head", null);
            var16.add(var110);
            var17.add(var35);
            var18++;
         }
      }

      var31 = var3 - 1;

      for (int var47 = 0; var47 < var2; var47++) {
         for (int var52 = 0; var52 < var1; var52++) {
            LinkedHashMap var57 = new LinkedHashMap();
            ArrayList var63 = new ArrayList();
            double var72 = (32 - var1 + (var1 - 1 - var52)) * 0.25;
            double var89 = (32 - var2 + (var2 - 1 - var47)) * 0.25;
            int var102 = 64 + var47 * var1 + var52;
            var57.put("south", this.buildFaceUv(this.rectangleUv(var72, var89, var19, var21, false, false), 0));
            var63.add(var102);
            if (var52 == var1 - 1) {
               var72 = (32 - var3 + (var3 - 1 - var31)) * 0.25;
               var89 = (32 - var2 + (var2 - 1 - var47)) * 0.25;
               int var111 = 128 + var47 * var3 + var31;
               var57.put("east", this.buildFaceUv(this.rectangleUv(var72, var89, var23, var25, false, false), var63.size()));
               var63.add(var111);
            }

            if (var52 == 0) {
               var72 = (32 - var3 + (var3 - 1 - var31)) * 0.25;
               var89 = (32 - var2 + (var2 - 1 - var47)) * 0.25;
               int var112 = 128 + var3 * var2 + var47 * var3 + var31;
               var57.put("west", this.buildFaceUv(this.rectangleUv(var72, var89, var23, var25, false, false), var63.size()));
               var63.add(var112);
            }

            if (var47 == var2 - 1) {
               var72 = (32 - var1 + var52) * 0.25;
               var89 = (32 - var3 + var31) * 0.25;
               int var113 = 128 + var3 * var2 * 2 + var31 * var1 + var52;
               var57.put("up", this.buildFaceUv(this.rectangleUv(var72, var89, var27, var29, false, false), var63.size()));
               var63.add(var113);
            }

            if (var47 == 0) {
               var72 = (32 - var1 + var52) * 0.25;
               var89 = (32 - var3 + var31) * 0.25;
               int var114 = 128 + var3 * var2 * 2 + 64 + var31 * var1 + var52;
               var57.put("down", this.buildFaceUv(this.rectangleUv(var72, var89, var27, var29, true, true), var63.size()));
               var63.add(var114);
            }

            String var115 = this.buildGuiCube(var18, var10, var12, var14, var4, var6, var8, var57, "head", null);
            var16.add(var115);
            var17.add(var63);
            var18++;
         }
      }

      int var48 = var1 - 1;

      for (int var53 = 0; var53 < var2; var53++) {
         for (int var43 = 1; var43 < var3 - 1; var43++) {
            LinkedHashMap var58 = new LinkedHashMap();
            ArrayList var64 = new ArrayList();
            double var77 = (32 - var3 + (var3 - 1 - var43)) * 0.25;
            double var94 = (32 - var2 + (var2 - 1 - var53)) * 0.25;
            int var103 = 128 + var53 * var3 + var43;
            var58.put("east", this.buildFaceUv(this.rectangleUv(var77, var94, var23, var25, false, false), 0));
            var64.add(var103);
            if (var53 == var2 - 1) {
               var77 = (32 - var1 + var48) * 0.25;
               var94 = (32 - var3 + var43) * 0.25;
               int var116 = 128 + var3 * var2 * 2 + var43 * var1 + var48;
               var58.put("up", this.buildFaceUv(this.rectangleUv(var77, var94, var27, var29, false, false), var64.size()));
               var64.add(var116);
            }

            if (var53 == 0) {
               var77 = (32 - var1 + var48) * 0.25;
               var94 = (32 - var3 + var43) * 0.25;
               int var117 = 128 + var3 * var2 * 2 + 64 + var43 * var1 + var48;
               var58.put("down", this.buildFaceUv(this.rectangleUv(var77, var94, var27, var29, true, true), var64.size()));
               var64.add(var117);
            }

            String var118 = this.buildGuiCube(var18, var10, var12, var14, var4, var6, var8, var58, "head", null);
            var16.add(var118);
            var17.add(var64);
            var18++;
         }
      }

      byte var49 = 0;

      for (int var54 = 0; var54 < var2; var54++) {
         for (int var44 = 1; var44 < var3 - 1; var44++) {
            LinkedHashMap var59 = new LinkedHashMap();
            ArrayList var65 = new ArrayList();
            double var80 = (32 - var3 + (var3 - 1 - var44)) * 0.25;
            double var97 = (32 - var2 + (var2 - 1 - var54)) * 0.25;
            int var104 = 128 + var3 * var2 + var54 * var3 + var44;
            var59.put("west", this.buildFaceUv(this.rectangleUv(var80, var97, var23, var25, false, false), 0));
            var65.add(var104);
            if (var54 == var2 - 1) {
               var80 = (32 - var1 + var49) * 0.25;
               var97 = (32 - var3 + var44) * 0.25;
               int var119 = 128 + var3 * var2 * 2 + var44 * var1 + var49;
               var59.put("up", this.buildFaceUv(this.rectangleUv(var80, var97, var27, var29, false, false), var65.size()));
               var65.add(var119);
            }

            if (var54 == 0) {
               var80 = (32 - var1 + var49) * 0.25;
               var97 = (32 - var3 + var44) * 0.25;
               int var120 = 128 + var3 * var2 * 2 + 64 + var44 * var1 + var49;
               var59.put("down", this.buildFaceUv(this.rectangleUv(var80, var97, var27, var29, true, true), var65.size()));
               var65.add(var120);
            }

            String var121 = this.buildGuiCube(var18, var10, var12, var14, var4, var6, var8, var59, "head", null);
            var16.add(var121);
            var17.add(var65);
            var18++;
         }
      }

      int var55 = var2 - 1;

      for (int var45 = 1; var45 < var3 - 1; var45++) {
         for (int var50 = 1; var50 < var1 - 1; var50++) {
            LinkedHashMap var60 = new LinkedHashMap();
            ArrayList var66 = new ArrayList();
            double var83 = (32 - var1 + var50) * 0.25;
            double var100 = (32 - var3 + var45) * 0.25;
            int var105 = 128 + var3 * var2 * 2 + var45 * var1 + var50;
            var60.put("up", this.buildFaceUv(this.rectangleUv(var83, var100, var27, var29, false, false), 0));
            var66.add(var105);
            String var122 = this.buildGuiCube(var18, var10, var12, var14, var4, var6, var8, var60, "head", null);
            var16.add(var122);
            var17.add(var66);
            var18++;
         }
      }

      boolean var56 = false;

      for (int var46 = 1; var46 < var3 - 1; var46++) {
         for (int var51 = 1; var51 < var1 - 1; var51++) {
            LinkedHashMap var61 = new LinkedHashMap();
            ArrayList var67 = new ArrayList();
            double var84 = (32 - var1 + var51) * 0.25;
            double var101 = (32 - var3 + var46) * 0.25;
            int var106 = 128 + var3 * var2 * 2 + 64 + var46 * var1 + var51;
            var61.put("down", this.buildFaceUv(this.rectangleUv(var84, var101, var27, var29, true, true), 0));
            var67.add(var106);
            String var123 = this.buildGuiCube(var18, var10, var12, var14, var4, var6, var8, var61, "head", null);
            var16.add(var123);
            var17.add(var67);
            var18++;
         }
      }

      GuiSlotLists var62 = new GuiSlotLists();
      var62.bDv = var16;
      var62.bDw = var17;
      return var62;
   }

   private GuiSlotLists buildLimbSlots(String var1, boolean var2) {
      byte var3;
      byte var4;
      byte var5;
      if (var1.contains("arm")) {
         var3 = (byte)(var2 ? 3 : 4);
         var4 = 12;
         var5 = 4;
      } else {
         var3 = 4;
         var4 = 12;
         var5 = 4;
      }

      double var6 = var3;
      double var8 = var4;
      double var10 = var5;
      double var12 = -var6 / 2.0;
      double var14 = -var8 / 2.0;
      double var16 = -var10 / 2.0;
      ArrayList var18 = new ArrayList();
      ArrayList var19 = new ArrayList();
      int var20 = 0;
      double var21 = var3 * 0.25;
      double var23 = var4 * 0.25;
      double var25 = var5 * 0.25;
      double var27 = var4 * 0.25;
      double var29 = var3 * 0.25;
      double var31 = var5 * 0.25;
      String var33 = var2 ? var1 + "_slim" : var1;
      int var34 = var3 * var4;
      int var35 = var5 * var4;
      int var36 = var3 * var5;
      int var37 = 0;

      for (int var38 = 0; var38 < var4; var38++) {
         for (int var39 = 0; var39 < var3; var39++) {
            LinkedHashMap var40 = new LinkedHashMap();
            ArrayList var41 = new ArrayList();
            double var42 = (32 - var3 + (var3 - 1 - var39)) * 0.25;
            double var44 = (32 - var4 + (var4 - 1 - var38)) * 0.25;
            int var46 = var38 * var3 + var39;
            var40.put("north", this.buildFaceUv(this.rectangleUv(var42, var44, var21, var23, false, false), 0));
            var41.add(var46);
            if (var39 == var3 - 1) {
               var42 = (32 - var5 + (var5 - 1 - var37)) * 0.25;
               var44 = (32 - var4 + (var4 - 1 - var38)) * 0.25;
               int var47 = var34 * 2 + var38 * var5 + var37;
               var40.put("east", this.buildFaceUv(this.rectangleUv(var42, var44, var25, var27, false, false), var41.size()));
               var41.add(var47);
            }

            if (var39 == 0) {
               var42 = (32 - var5 + (var5 - 1 - var37)) * 0.25;
               var44 = (32 - var4 + (var4 - 1 - var38)) * 0.25;
               int var113 = var34 * 2 + var5 * var4 + var38 * var5 + var37;
               var40.put("west", this.buildFaceUv(this.rectangleUv(var42, var44, var25, var27, false, false), var41.size()));
               var41.add(var113);
            }

            if (var38 == var4 - 1) {
               var42 = (32 - var3 + var39) * 0.25;
               var44 = (32 - var5 + var37) * 0.25;
               int var114 = var34 * 2 + var5 * var4 * 2 + var37 * var3 + var39;
               var40.put("up", this.buildFaceUv(this.rectangleUv(var42, var44, var29, var31, false, false), var41.size()));
               var41.add(var114);
            }

            if (var38 == 0) {
               var42 = (32 - var3 + var39) * 0.25;
               var44 = (32 - var5 + var37) * 0.25;
               int var115 = var34 * 2 + var5 * var4 * 2 + var36 + var37 * var3 + var39;
               var40.put("down", this.buildFaceUv(this.rectangleUv(var42, var44, var29, var31, false, false), var41.size()));
               var41.add(var115);
            }

            String var116 = this.buildGuiCube(var20, var12, var14, var16, var6, var8, var10, var40, "limb", var33);
            var18.add(var116);
            var19.add(var41);
            var20++;
         }
      }

      var37 = var5 - 1;

      for (int var53 = 0; var53 < var4; var53++) {
         for (int var58 = 0; var58 < var3; var58++) {
            LinkedHashMap var63 = new LinkedHashMap();
            ArrayList var69 = new ArrayList();
            double var78 = (32 - var3 + (var3 - 1 - var58)) * 0.25;
            double var95 = (32 - var4 + (var4 - 1 - var53)) * 0.25;
            int var108 = var34 + var53 * var3 + var58;
            var63.put("south", this.buildFaceUv(this.rectangleUv(var78, var95, var21, var23, false, false), 0));
            var69.add(var108);
            if (var58 == var3 - 1) {
               var78 = (32 - var5 + (var5 - 1 - var37)) * 0.25;
               var95 = (32 - var4 + (var4 - 1 - var53)) * 0.25;
               int var117 = var34 * 2 + var53 * var5 + var37;
               var63.put("east", this.buildFaceUv(this.rectangleUv(var78, var95, var25, var27, false, false), var69.size()));
               var69.add(var117);
            }

            if (var58 == 0) {
               var78 = (32 - var5 + (var5 - 1 - var37)) * 0.25;
               var95 = (32 - var4 + (var4 - 1 - var53)) * 0.25;
               int var118 = var34 * 2 + var5 * var4 + var53 * var5 + var37;
               var63.put("west", this.buildFaceUv(this.rectangleUv(var78, var95, var25, var27, false, false), var69.size()));
               var69.add(var118);
            }

            if (var53 == var4 - 1) {
               var78 = (32 - var3 + var58) * 0.25;
               var95 = (32 - var5 + var37) * 0.25;
               int var119 = var34 * 2 + var5 * var4 * 2 + var37 * var3 + var58;
               var63.put("up", this.buildFaceUv(this.rectangleUv(var78, var95, var29, var31, false, false), var69.size()));
               var69.add(var119);
            }

            if (var53 == 0) {
               var78 = (32 - var3 + var58) * 0.25;
               var95 = (32 - var5 + var37) * 0.25;
               int var120 = var34 * 2 + var5 * var4 * 2 + var36 + var37 * var3 + var58;
               var63.put("down", this.buildFaceUv(this.rectangleUv(var78, var95, var29, var31, false, false), var69.size()));
               var69.add(var120);
            }

            String var121 = this.buildGuiCube(var20, var12, var14, var16, var6, var8, var10, var63, "limb", var33);
            var18.add(var121);
            var19.add(var69);
            var20++;
         }
      }

      int var54 = var3 - 1;

      for (int var59 = 0; var59 < var4; var59++) {
         for (int var49 = 1; var49 < var5 - 1; var49++) {
            LinkedHashMap var64 = new LinkedHashMap();
            ArrayList var70 = new ArrayList();
            double var83 = (32 - var5 + (var5 - 1 - var49)) * 0.25;
            double var100 = (32 - var4 + (var4 - 1 - var59)) * 0.25;
            int var109 = var34 * 2 + var59 * var5 + var49;
            var64.put("east", this.buildFaceUv(this.rectangleUv(var83, var100, var25, var27, false, false), 0));
            var70.add(var109);
            if (var59 == var4 - 1) {
               var83 = (32 - var3 + var54) * 0.25;
               var100 = (32 - var5 + var49) * 0.25;
               int var122 = var34 * 2 + var5 * var4 * 2 + var49 * var3 + var54;
               var64.put("up", this.buildFaceUv(this.rectangleUv(var83, var100, var29, var31, false, false), var70.size()));
               var70.add(var122);
            }

            if (var59 == 0) {
               var83 = (32 - var3 + var54) * 0.25;
               var100 = (32 - var5 + var49) * 0.25;
               int var123 = var34 * 2 + var5 * var4 * 2 + var36 + var49 * var3 + var54;
               var64.put("down", this.buildFaceUv(this.rectangleUv(var83, var100, var29, var31, false, false), var70.size()));
               var70.add(var123);
            }

            String var124 = this.buildGuiCube(var20, var12, var14, var16, var6, var8, var10, var64, "limb", var33);
            var18.add(var124);
            var19.add(var70);
            var20++;
         }
      }

      byte var55 = 0;

      for (int var60 = 0; var60 < var4; var60++) {
         for (int var50 = 1; var50 < var5 - 1; var50++) {
            LinkedHashMap var65 = new LinkedHashMap();
            ArrayList var71 = new ArrayList();
            double var86 = (32 - var5 + (var5 - 1 - var50)) * 0.25;
            double var103 = (32 - var4 + (var4 - 1 - var60)) * 0.25;
            int var110 = var34 * 2 + var5 * var4 + var60 * var5 + var50;
            var65.put("west", this.buildFaceUv(this.rectangleUv(var86, var103, var25, var27, false, false), 0));
            var71.add(var110);
            if (var60 == var4 - 1) {
               var86 = (32 - var3 + var55) * 0.25;
               var103 = (32 - var5 + var50) * 0.25;
               int var125 = var34 * 2 + var5 * var4 * 2 + var50 * var3 + var55;
               var65.put("up", this.buildFaceUv(this.rectangleUv(var86, var103, var29, var31, false, false), var71.size()));
               var71.add(var125);
            }

            if (var60 == 0) {
               var86 = (32 - var3 + var55) * 0.25;
               var103 = (32 - var5 + var50) * 0.25;
               int var126 = var34 * 2 + var5 * var4 * 2 + var36 + var50 * var3 + var55;
               var65.put("down", this.buildFaceUv(this.rectangleUv(var86, var103, var29, var31, false, false), var71.size()));
               var71.add(var126);
            }

            String var127 = this.buildGuiCube(var20, var12, var14, var16, var6, var8, var10, var65, "limb", var33);
            var18.add(var127);
            var19.add(var71);
            var20++;
         }
      }

      int var61 = var4 - 1;

      for (int var51 = 1; var51 < var5 - 1; var51++) {
         for (int var56 = 1; var56 < var3 - 1; var56++) {
            LinkedHashMap var66 = new LinkedHashMap();
            ArrayList var72 = new ArrayList();
            double var89 = (32 - var3 + var56) * 0.25;
            double var106 = (32 - var5 + var51) * 0.25;
            int var111 = var34 * 2 + var5 * var4 * 2 + var51 * var3 + var56;
            var66.put("up", this.buildFaceUv(this.rectangleUv(var89, var106, var29, var31, false, false), 0));
            var72.add(var111);
            String var128 = this.buildGuiCube(var20, var12, var14, var16, var6, var8, var10, var66, "limb", var33);
            var18.add(var128);
            var19.add(var72);
            var20++;
         }
      }

      boolean var62 = false;

      for (int var52 = 1; var52 < var5 - 1; var52++) {
         for (int var57 = 1; var57 < var3 - 1; var57++) {
            LinkedHashMap var67 = new LinkedHashMap();
            ArrayList var73 = new ArrayList();
            double var90 = (32 - var3 + var57) * 0.25;
            double var107 = (32 - var5 + var52) * 0.25;
            int var112 = var34 * 2 + var5 * var4 * 2 + var36 + var52 * var3 + var57;
            var67.put("down", this.buildFaceUv(this.rectangleUv(var90, var107, var29, var31, false, false), 0));
            var73.add(var112);
            String var129 = this.buildGuiCube(var20, var12, var14, var16, var6, var8, var10, var67, "limb", var33);
            var18.add(var129);
            var19.add(var73);
            var20++;
         }
      }

      GuiSlotLists var68 = new GuiSlotLists();
      var68.bDv = var18;
      var68.bDw = var19;
      return var68;
   }

   private void writeCompositeModel(List var1, List var2, String var3) {
      JsonObject var4 = new JsonObject();
      JsonObject var5 = new JsonObject();
      var5.addProperty("type", "composite");
      JsonArray var6 = new JsonArray();
      this.appendModelEntries(var1, var2, var6);
      var5.add("models", var6);
      var4.add("model", var5);
      this.putGeneratedFile(var3, var4);
   }

   private void writeCustomDataModel(List var1, List var2, String var3) {
      JsonObject var4 = new JsonObject();
      JsonObject var5 = new JsonObject();
      var5.addProperty("type", "composite");
      JsonArray var6 = new JsonArray();
      int var7 = -1;

      for (List var9 : var2) {
         for (Integer var11 : var9) {
            var7 = Math.max(var7, var11);
         }
      }

      int var29 = var7 + 1;
      this.appendModelEntries(var1, var2, var6);

      for (int var30 = 0; var30 < var1.size(); var30++) {
         String var31 = (String)var1.get(var30);
         List var32 = (List)var2.get(var30);
         String[] var12 = var31.substring(var31.lastIndexOf(47) + 1).split("_");
         String var13;
         String var14;
         if (var31.contains("slim")) {
            var13 = String.join("_", Arrays.asList(var12).subList(0, var12.length - 2)) + "_slim";
            var14 = "limb";
         } else if (!var12[0].equals("right") && !var12[0].equals("left")) {
            var13 = null;
            var14 = var12[0];
         } else {
            var13 = String.join("_", Arrays.asList(var12).subList(0, var12.length - 2));
            var14 = "limb";
         }

         if (var13 != null) {
            var13 = var13.replace("_slim_slim", "_slim");
         }

         this.buildPixelLayer(var31, var14, var13);
         String var15 = this.toInternalPath(var31);
         JsonObject var16 = this.readGeneratedFile(var15);
         JsonObject var17 = var16.getAsJsonArray("elements").get(0).getAsJsonObject().getAsJsonObject("faces");
         ArrayList var18 = new ArrayList();

         for (Entry var20 : var17.entrySet()) {
            var18.add(var20);
         }

         for (int var33 = 0; var33 < var32.size(); var33++) {
            if (var33 < var18.size()) {
               Entry var34 = (Entry)var18.get(var33);
               String var21 = (String)var34.getKey();
               JsonObject var22 = ((JsonElement)var34.getValue()).getAsJsonObject();
               String var23 = this.buildPixelLayerForFace(var31, var30, var33, var21, var22, var14, var13);
               JsonObject var24 = new JsonObject();
               var24.addProperty("type", "condition");
               var24.addProperty("property", "custom_model_data");
               var24.addProperty("index", (Number)var32.get(var33));
               JsonObject var25 = new JsonObject();
               var25.addProperty("type", "model");
               var25.addProperty("model", var23);
               JsonArray var26 = new JsonArray();
               JsonObject var27 = new JsonObject();
               var27.addProperty("type", "custom_model_data");
               var27.addProperty("index", var29 + (Integer)var32.get(var33));
               var27.addProperty("default", 0);
               var26.add(var27);
               var25.add("tints", var26);
               JsonObject var28 = new JsonObject();
               var28.addProperty("type", "empty");
               var24.add("on_true", var25);
               var24.add("on_false", var28);
               var6.add(var24);
            }
         }
      }

      var5.add("models", var6);
      var4.add("model", var5);
      this.putGeneratedFile(var3, var4);
   }

   private void appendModelEntries(List var1, List var2, JsonArray var3) {
      for (int var4 = 0; var4 < var1.size(); var4++) {
         String var5 = (String)var1.get(var4);
         List var6 = (List)var2.get(var4);
         JsonObject var7 = new JsonObject();
         var7.addProperty("type", "model");
         var7.addProperty("model", var5);
         JsonArray var8 = new JsonArray();

         for (Integer var10 : var6) {
            JsonObject var11 = new JsonObject();
            var11.addProperty("type", "custom_model_data");
            var11.addProperty("index", var10);
            var11.addProperty("default", 0);
            var8.add(var11);
         }

         var7.add("tints", var8);
         var3.add(var7);
      }
   }

   private void minifyJsonFiles() {
      LinkedHashMap var1 = new LinkedHashMap(this.generatedFiles);

      for (Entry var3 : var1.entrySet()) {
         String var4 = (String)var3.getKey();
         if (var4.endsWith(".json")) {
            String var5 = new String((byte[])var3.getValue(), StandardCharsets.UTF_8);
            JsonElement var6 = JsonParser.parseString(var5);
            String var7 = this.compactGson.toJson(var6);
            var7 = var7.replace(".0,", ",").replace(".0]", "]").replace(".0}", "}");
            this.generatedFiles.put(var4, var7.getBytes(StandardCharsets.UTF_8));
         }
      }
   }
}
