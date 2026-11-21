package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import org.jetbrains.annotations.Nullable;

public class ModelDefinition {
   public final String bHo;
   public final String bHp;
   @Nullable
   public final JSONObject bHq;

   public ModelDefinition(String var1, String var2, @Nullable JSONObject var3) {
      this.bHo = var1;
      this.bHp = var2;
      this.bHq = var3;
   }

   public boolean isVanillaReference() {
      return this.bHo.startsWith("minecraft:") && this.bHq == null;
   }
}
