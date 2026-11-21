package dev.lone.itemsadder.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import java.util.Map.Entry;

public class GsonHelper {
   public static final Gson COMPACT_GSON = new GsonBuilder().disableHtmlEscaping().create();
   public static final Gson PRETTY_GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

   public static JsonObject parseObject(String var0) {
      return (JsonObject)ContentManager.field66.fromJson(var0, JsonObject.class);
   }

   public static String toJsonString(JsonObject var0, boolean var1, boolean var2) {
      String var3 = var2 ? COMPACT_GSON.toJson(var0) : PRETTY_GSON.toJson(var0);
      return var1 ? CustomTexture.escapeForJs(var3) : var3;
   }

   public static JsonElement copyElement(JsonElement var0) {
      Class var1 = var0.getClass();
      return (JsonElement)ContentManager.field66.fromJson(var0.toString(), var1);
   }

   public static String toCompactJson(JsonObject var0) {
      return ContentManager.field66.toJson(var0);
   }

   public static void mergeInto(JsonObject var0, JsonObject var1) {
      for (Entry var3 : var1.entrySet()) {
         var0.add((String)var3.getKey(), (JsonElement)var3.getValue());
      }
   }

   public static String prettify(String var0) {
      return PRETTY_GSON.toJson(parseObject(var0));
   }
}
