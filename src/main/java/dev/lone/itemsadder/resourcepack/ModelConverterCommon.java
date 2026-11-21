package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.ItemSpec;
import dev.lone.itemsadder.item.ItemSpecWithAttributes;
import dev.lone.itemsadder.item.ItemSpecWithTexture;
import dev.lone.itemsadder.util.JsonHelper;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public abstract class ModelConverterCommon extends ModelConverterBase {
   public ModelConverterCommon(ItemSpec var1) {
      super(var1);
   }

   @Override
   void collectModels() {
      this.bHk = new HashMap();
      if (this.bHi instanceof ItemSpecWithTexture var1) {
         this.collectTextureModels(var1);
      } else if (this.bHi instanceof ItemSpecWithAttributes var2) {
         this.collectAttributeModels(var2);
      }
   }

   void collectTextureModels(ItemSpecWithTexture var1) {
      ModelJsonAsset var2 = ModelJsonAsset.forMaterial(this.bHi.Ep());
      if (var2.hasError()) {
         throw new IllegalArgumentException("Original model of material '" + this.bHi.Ep() + "' has errors", var2.getError());
      }

      Map var3 = var1.Fd();
      var3.forEach((var2x, var3x) -> {
         var2x = var2x.toLowerCase().trim();
         JSONObject var4 = var2.copyAsObject();
         JSONObject var5 = var4.getJSONObject("textures");
         var5.put("layer0", var3x);
         ModelDefinition var6 = new ModelDefinition(this.bHi.cV(var2x), this.bHi.cU(var2x), var4);
         if (var2x.equals("normal")) {
            this.bHj = var6;
         } else {
            this.bHk.put(var2x, var6);
         }
      });
   }

   void collectAttributeModels(ItemSpecWithAttributes var1) {
      Map var2 = var1.Fb();
      var2.forEach((var1x, var2x) -> {
         var1x = var1x.toLowerCase().trim();
         String var3 = this.bHi.cU(var1x);
         String var4 = this.bHi.cV(var1x);
         JSONObject var5 = null;
         File var6 = ContentManager.method8198(var3);
         if (var6 != null && var6.exists()) {
            Log.devLog("Using model asset file: " + var6.getAbsolutePath());

            try {
               var5 = JsonHelper.readJsonFile(var6);
            } catch (IOException var8) {
               throw new IllegalArgumentException("Failed to load JSON model: " + Utils.shortPath(var6), var8);
            }
         } else {
            if (!var4.startsWith("minecraft:")) {
               Log.devLog("Model asset file not found: " + var3);
               throw new IllegalArgumentException("Model asset file not found: " + var3);
            }

            if (!ContentManager.method8084().field91.hasVanillaModel(var4)) {
               throw new IllegalArgumentException("Model '" + var4 + "' is not a valid vanilla model. Item: " + this.bHi.Eo());
            }
         }

         ModelDefinition var7 = new ModelDefinition(var4, var3, var5);
         if (var1x.equals("normal")) {
            this.bHj = var7;
         } else {
            this.bHk.put(var1x, var7);
         }
      });
   }

   @Nullable
   String getModelId(String var1) {
      return this.getModelIdOrDefault(var1, null);
   }

   @Nullable
   String getModelIdOrDefault(String var1, @Nullable String var2) {
      return this.bHk
         .entrySet()
         .stream()
         .filter(var1x -> ((String)var1x.getKey()).equals(var1))
         .map(var0 -> ((ModelDefinition)var0.getValue()).bHo)
         .findFirst()
         .orElse(var2);
   }
}
