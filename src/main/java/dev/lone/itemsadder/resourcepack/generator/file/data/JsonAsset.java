package dev.lone.itemsadder.resourcepack.generator.file.data;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;

public abstract class JsonAsset {
   protected String asL;
   protected Object bIG;
   @Nullable
   Exception bIH = null;

   public boolean hasError() {
      return this.bIH != null;
   }

   @Nullable
   public Exception getError() {
      return this.bIH;
   }

   public JSONArray asArray() {
      return (JSONArray)this.bIG;
   }

   public JSONObject asObject() {
      return (JSONObject)this.bIG;
   }

   public JSONObject getObject(String var1) {
      return this.asObject().getJSONObject(var1);
   }

   public JSONArray getArray(String var1) {
      return this.asObject().getJSONArray(var1);
   }

   public JSONObject copyAsObject() {
      return (JSONObject)JSON.copy(this.bIG, new Feature[0]);
   }

   @Generated
   public String getPath() {
      return this.asL;
   }
}
