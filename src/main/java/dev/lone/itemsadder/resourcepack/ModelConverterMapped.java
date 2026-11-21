package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemSpec;
import dev.lone.itemsadder.item.ItemSpecWithTexture;
import dev.lone.itemsadder.util.JsonHelper;
import dev.lone.itemsadder.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.block.BlockFace;

public class ModelConverterMapped extends ModelConverterBase {
   private final Map bHx = new EnumMap(BlockFace.class);

   public ModelConverterMapped(ItemSpec var1) {
      super(var1);
   }

   @Override
   void collectModels() {
      this.bHk = new HashMap();
      if (this.bHi instanceof ItemSpecWithTexture var1) {
         ModelJsonAsset var13 = ModelJsonAsset.forMaterial(this.bHi.Ep());
         if (var13.hasError()) {
            throw new IllegalArgumentException("Original model of material " + this.bHi.Ep() + " has errors", var13.getError());
         }

         JSONObject var15 = var13.copyAsObject();
         var15.put("parent", var1.Fc());
         JSONObject var17 = var15.getJSONObject("textures");
         if (var17 == null) {
            var17 = new JSONObject();
            var15.put("textures", var17);
         }

         Map var19 = var1.Fd();
         if (!var19.containsKey("layer0") && !var19.containsKey("normal")) {
            var17.remove("layer0");
         }

         if (!var19.containsKey("layer1")) {
            var17.remove("layer1");
         }

         for (Entry var7 : var19.entrySet()) {
            String var8 = (String)var7.getKey();
            String var9 = (String)var7.getValue();
            var8 = var8.toLowerCase().trim();
            if (var8.equals("normal")) {
               var8 = "layer0";
            }

            var17.put(var8, var9);
         }

         if (!var19.containsKey("particle")) {
            String var21 = (String)var19.values().stream().findFirst().orElse(null);
            if (var21 != null) {
               var17.put("particle", var21);
            }
         }

         this.bHj = new ModelDefinition(this.bHi.Ek(), this.bHi.Ej(), var15);
      } else {
         String var12 = this.bHi.Ej();
         File var3 = ContentManager.method8198(var12);
         JSONObject var4 = var3 == null && ContentManager.method8084().field91.hasVanillaModel(this.bHi.Ek())
            ? ContentManager.method8084().field91.getModelJson(this.bHi.Ek())
            : null;
         if (var4 == null && (var3 == null || !var3.exists())) {
            Log.devLog("Model asset file not found: " + var12);
            throw new IllegalArgumentException("Model asset file not found: " + var12);
         }

         try {
            JSONObject var5 = var4 != null ? var4 : JsonHelper.readJsonFile(var3);
            this.bHj = new ModelDefinition(this.bHi.Ek(), this.bHi.Ej(), var5);
         } catch (IOException var10) {
            throw new IllegalArgumentException(var10);
         }
      }

      ModelDefinition var11 = this.bHj;
      this.bHj = ModelTransformUtils.applyDefaultFurnitureTransform(this.bHi, var11);
      if (ModelTransformUtils.furnitureModelId((CustomItemDefinition)ContentManager.method8084().field92.get(this.bHi.Eo()), BlockFace.NORTH) != null) {
         for (BlockFace var20 : new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.DOWN}) {
            ModelDefinition var22 = ModelTransformUtils.applyFurnitureTransform(this.bHi, var11, var20);
            this.bHx.put(var20, var22);
            if (var22 != var11) {
               this.bHk.put("furniture_" + var20.name(), var22);
            }
         }
      }

      if (this.bHi.DP() != null && this.bHi.DP().EQ() != null) {
         this.bHk
            .put("broken", new ModelDefinition(this.bHi.cV("broken"), this.bHi.cU("broken"), ModelJsonUtils.copyWithLayer0(this.bHj.bHq, this.bHi.DP().EQ())));
      }
   }

   @Override
   void buildMaterialJson() {
      if (this.bHi.DP() != null) {
         ModelDefinition var4 = (ModelDefinition)this.bHk.get("broken");
         JSONObject var5 = ModelJsonUtils.brokenConditionModel(this.bHj.bHo, var4 == null ? this.bHj.bHo : var4.bHo);
         if (this.bHi.Em() != null) {
            this.wrapInDisplayContextSelect(var5.getJSONObject("model"), this.bHi.Em());
         }

         this.applyItemProperties(var5);
         this.bHm = var5;
         this.buildFurnitureVariants();
      } else {
         MaterialJsonAsset var1 = new MaterialJsonAsset(this.bHi.Ep());
         JSONObject var2 = var1.copyAsObject();
         JSONObject var3 = var2.getJSONObject("model");
         if (!var3.getString("type").equals("minecraft:model")) {
            throw new IllegalArgumentException("Unsupported material " + this.bHi.Ep() + " for now. Use another one or suggest it on GitHub.");
         }

         var3.put("model", this.bHj.bHo);
         if (this.bHi.Em() != null) {
            this.wrapInDisplayContextSelect(var3, this.bHi.Em());
         }

         this.applyItemProperties(var2);
         this.bHm = var2;
         this.buildFurnitureVariants();
      }
   }

   private void buildFurnitureVariants() {
      if (!this.bHx.isEmpty()) {
         this.bHn = new HashMap();

         for (Entry var2 : this.bHx.entrySet()) {
            ModelDefinition var3 = (ModelDefinition)var2.getValue();
            JSONObject var4 = JsonHelper.copyObject(this.bHm);
            replaceModelReference(var4, this.bHj.bHo, var3.bHo);
            String[] var5 = this.bHi.Eo().split(":", 2);
            String var6 = "assets/"
               + var5[0]
               + "/items/ia_auto/furniture/"
               + var5[1]
               + "_"
               + ((BlockFace)var2.getKey()).name().toLowerCase(Locale.ROOT)
               + ".json";
            this.bHn.put(var6, var4);
         }
      }
   }

   private static void replaceModelReference(Object var0, String var1, String var2) {
      if (var0 instanceof JSONObject var3) {
         for (String var6 : var3.keySet()) {
            Object var7 = var3.get(var6);
            if (var1.equals(var7)) {
               var3.put(var6, var2);
            } else {
               replaceModelReference(var7, var1, var2);
            }
         }
      } else if (var0 instanceof JSONArray) {
         for (Object var9 : (JSONArray)var0) {
            replaceModelReference(var9, var1, var2);
         }
      }
   }
}
