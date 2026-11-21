package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import java.util.Map;

public class ModelConversionResult {
   public final ModelDefinition bHr;
   public final Map bHs;
   public final JSONObject bHt;
   public final JSONObject bHu;
   public final Map bHv;

   ModelConversionResult(ModelDefinition var1, Map var2, JSONObject var3, JSONObject var4, Map var5) {
      this.bHt = var3;
      this.bHu = var4;
      this.bHr = var1;
      this.bHs = var2;
      this.bHv = var5;
   }
}
