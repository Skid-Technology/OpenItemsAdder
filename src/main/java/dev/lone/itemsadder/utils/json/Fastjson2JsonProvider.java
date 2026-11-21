package dev.lone.itemsadder.utils.json;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.spi.json.AbstractJsonProvider;
import dev.lone.itemsadder.util.JsonHelper;
import dev.lone.itemsadder.util.MarkerAnnotation;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

@MarkerAnnotation
public class Fastjson2JsonProvider extends AbstractJsonProvider {
   public Object unwrap(Object o) {
      return o;
   }

   public Object parse(String json) {
      return JSON.parse(json);
   }

   public Object parse(InputStream jsonStream, String charset) {
      try {
         JSONReader var3 = JSONReader.of(new InputStreamReader(jsonStream, charset));
         JSONObject var4 = var3.readJSONObject();
         var3.close();
         return var4;
      } catch (Throwable var5) {
         throw new JsonPathException(var5);
      }
   }

   public String toJson(Object obj) {
      return JSON.toJSONString(obj);
   }

   public Object createArray() {
      return new JSONArray();
   }

   public Object createMap() {
      return new JSONObject();
   }

   public boolean isArray(Object obj) {
      return obj instanceof JSONObject || obj instanceof List;
   }

   public Object getArrayIndex(Object obj, int idx) {
      return this.toJsonArray(obj).get(idx);
   }

   public void setArrayIndex(Object array, int index, Object newValue) {
      if (!this.isArray(array)) {
         throw new UnsupportedOperationException();
      }

      JSONArray var4 = this.toJsonArray(array);
      if (index == var4.size()) {
         var4.add(newValue);
      } else {
         var4.set(index, newValue);
      }
   }

   public Object getMapValue(Object obj, String key) {
      JSONObject var3 = this.toJsonObject(obj);
      Object var4 = var3.get(key);
      return !var3.containsKey(key) ? UNDEFINED : this.unwrap(var4);
   }

   public void setProperty(Object obj, Object key, Object value) {
      if (this.isMap(obj)) {
         this.toJsonObject(obj).put(key.toString(), value);
      } else {
         JSONArray var4 = this.toJsonArray(obj);
         int var5;
         if (key != null) {
            var5 = key instanceof Integer ? (Integer)key : Integer.parseInt(key.toString());
         } else {
            var5 = var4.size();
         }

         if (var5 == var4.size()) {
            var4.add(value);
         } else {
            var4.set(var5, value);
         }
      }
   }

   public void removeProperty(Object obj, Object key) {
      if (this.isMap(obj)) {
         this.toJsonObject(obj).remove(key.toString());
      } else {
         JSONArray var3 = this.toJsonArray(obj);
         int var4 = key instanceof Integer ? (Integer)key : Integer.parseInt(key.toString());
         var3.remove(var4);
      }
   }

   public boolean isMap(Object obj) {
      return obj instanceof JSONObject;
   }

   public Collection getPropertyKeys(Object obj) {
      ArrayList var2 = new ArrayList();

      for (Entry var4 : this.toJsonObject(obj).entrySet()) {
         var2.add((String)var4.getKey());
      }

      return var2;
   }

   public int length(Object obj) {
      if (this.isArray(obj)) {
         return this.toJsonArray(obj).size();
      } else if (this.isMap(obj)) {
         return this.toJsonObject(obj).entrySet().size();
      } else if (JsonHelper.isPrimitiveValue(obj)) {
         return obj.toString().length();
      } else {
         throw new JsonPathException("length operation can not applied to " + (obj != null ? obj.getClass().getName() : "null"));
      }
   }

   public Iterable toIterable(Object obj) {
      JSONArray var2 = this.toJsonArray(obj);
      ArrayList var3 = new ArrayList(var2.size());

      for (Object var5 : var2) {
         var3.add(this.unwrap(var5));
      }

      return var3;
   }

   private JSONArray toJsonArray(Object o) {
      return (JSONArray)o;
   }

   private JSONObject toJsonObject(Object o) {
      return (JSONObject)o;
   }
}
