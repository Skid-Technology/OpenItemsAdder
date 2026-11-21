package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;

public class ModelDefinitionBundle {
   private final ModelDefinition bHz;
   private final JSONObject bHA;
   private final Map bHB;
   @Nullable
   private final JSONObject bHC;
   private final Map bHD;
   private final List bHE;

   public ModelDefinitionBundle(ModelDefinition var1, Map var2, @Nullable JSONObject var3, List var4, JSONObject var5, Map var6) {
      this.bHz = var1;
      this.bHA = Objects.requireNonNull(var5, "itemJson cannot be null");
      this.bHB = var6;
      this.bHC = var3;
      this.bHD = Objects.requireNonNull(var2, "statusModelsJsons cannot be null");
      this.bHE = Objects.requireNonNull(var4, "usedModelsTextures cannot be null");
   }

   @Generated
   public ModelDefinition baseModel() {
      return this.bHz;
   }

   @Generated
   public JSONObject itemJson() {
      return this.bHA;
   }

   @Generated
   public Map furnitureFiles() {
      return this.bHB;
   }

   @Nullable
   @Generated
   public JSONObject baseLayerJson() {
      return this.bHC;
   }

   @Generated
   public Map statusModels() {
      return this.bHD;
   }

   @Generated
   public List usedTextures() {
      return this.bHE;
   }
}
