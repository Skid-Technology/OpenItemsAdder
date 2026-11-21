package dev.lone.itemsadder.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.JSONWriter.Feature;
import com.google.gson.JsonSyntaxException;
import com.jayway.jsonpath.DocumentContext;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class JsonHelper {
   private static final Set PRIMITIVE_TYPES = Set.of(
      int.class,
      long.class,
      short.class,
      float.class,
      double.class,
      byte.class,
      boolean.class,
      char.class,
      Integer.class,
      Long.class,
      Short.class,
      Float.class,
      Double.class,
      Byte.class,
      Boolean.class,
      Character.class
   );

   public static FloatVector3 parseScaledVector(JSONArray var0) {
      return new FloatVector3(var0.getFloat(0) / 16.0F, var0.getFloat(1) / 16.0F, var0.getFloat(2) / 16.0F);
   }

   public static double getDoubleOr(JSONObject var0, String var1, double var2) {
      return !var0.containsKey(var1) ? var2 : var0.getDouble(var1);
   }

   public static FloatVector3 parseVector(JSONArray var0) {
      return new FloatVector3(var0.getFloat(0), var0.getFloat(1), var0.getFloat(2));
   }

   public static boolean getBool(JSONObject var0, String var1) {
      return getBoolOr(var0, var1, false);
   }

   public static boolean getBoolOr(JSONObject var0, String var1, boolean var2) {
      return !var0.containsKey(var1) ? var2 : var0.getBoolean(var1);
   }

   public static String getStringOr(JSONObject var0, String var1, String var2) {
      return !var0.containsKey(var1) ? var2 : var0.getString(var1);
   }

   public static String[] toStringArray(JSONArray var0) {
      String[] var1 = new String[var0 != null ? var0.size() : 0];
      if (var0 != null) {
         int var2 = 0;

         for (Object var4 : var0) {
            var1[var2++] = (String)var4;
         }
      }

      return var1;
   }

   public static JSONObject readJsonFile(File var0) {
      try (BufferedReader var1 = new BufferedReader(new InputStreamReader(Files.newInputStream(var0.toPath()), StandardCharsets.UTF_8))) {
         return JSONReader.of(var1).readJSONObject();
      }
   }

   public static JSONObject parseJson(String var0) {
      return JSON.parseObject(var0);
   }

   public static void validateJson(String var0) {
      try {
         GsonHelper.PRETTY_GSON.fromJson(var0, Object.class);
      } catch (JsonSyntaxException var14) {
         JsonSyntaxException var1 = var14;
         String var2 = var14.getMessage();
         if (var2 != null && var2.contains("line")) {
            String[] var3 = var2.split("\n");
            String var4 = var3[0];

            try {
               int var5 = var4.indexOf("line ");
               int var6 = var4.indexOf(" column ");
               if (var5 != -1 && var6 != -1) {
                  int var7 = Integer.parseInt(var4.substring(var5 + 5, var4.indexOf(" ", var5 + 5)));
                  int var8 = Integer.parseInt(var4.substring(var6 + 8, var4.indexOf(" ", var6 + 8)));
                  StringBuilder var9 = new StringBuilder("Error at line ").append(var7).append(", column ").append(var8);
                  if (var7 > 1) {
                     var9.append("\n...");
                  }

                  var9.append("\n");
                  String[] var10 = var0.split("\\r?\\n");

                  for (int var11 = var7 - 1; var11 < Math.min(var10.length, var7 + 5); var11++) {
                     String var12 = var10[var11];
                     var9.append(var11 + 1).append(": ").append(var12).append("\n");
                  }

                  if (var7 + 5 < var10.length) {
                     var9.append("...\n");
                  }

                  throw new ExecutionException(var9.toString(), var1);
               }
            } catch (NumberFormatException var13) {
               throw new ExecutionException("Failed to parse line/column information: " + var4, var13);
            }
         }

         throw new ExecutionException("Unexpected JSON error: " + var2, var14);
      }
   }

   public static String toLargeJsonString(JSONObject var0) {
      JSONWriter var1 = JSONWriter.of(new Feature[]{Feature.LargeObject});

      String var2;
      try {
         var1.setRootObject(var0);
         var1.write(var0);
         var2 = var1.toString();
      } catch (Throwable var5) {
         if (var1 != null) {
            try {
               var1.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (var1 != null) {
         var1.close();
      }

      return var2;
   }

   public static String toJsonString(JSONObject var0) {
      return JSON.toJSONString(var0);
   }

   public static String mapToJsonString(Map var0) {
      return JSON.toJSONString(new JSONObject(var0));
   }

   public static Map parseMap(String var0) {
      return (Map)parseJson(var0).toJavaObject(Map.class, new com.alibaba.fastjson2.JSONReader.Feature[0]);
   }

   public static JSONObject getObjectOr(JSONObject var0, String var1, JSONObject var2) {
      return var0.containsKey(var1) ? var0.getJSONObject(var1) : var2;
   }

   public static JSONArray copyArray(JSONArray var0) {
      return (JSONArray)JSON.copy(var0, new Feature[0]);
   }

   public static JSONObject copyObject(JSONObject var0) {
      return (JSONObject)JSON.copy(var0, new Feature[0]);
   }

   public static void mergeObject(JSONObject var0, JSONObject var1) {
      var0.putAll(var1);
   }

   public static DocumentContext toDocumentContext(JSONObject var0) {
      return JsonPathHolder.PARSE_CONTEXT.parse(var0.toString());
   }

   public static boolean isPrimitiveValue(Object var0) {
      return var0 != JSONObject.class && var0 != JSONArray.class ? PRIMITIVE_TYPES.contains(var0.getClass()) : false;
   }

   public static String toFormattedJsonString(JSONObject var0, boolean var1, boolean var2) {
      String var3 = var2 ? JSON.toJSONString(var0, new Feature[]{Feature.MapSortField, Feature.PrettyFormat}) : JSON.toJSONString(var0);
      return var1 ? CustomTexture.escapeForJs(var3) : var3;
   }

   public static void writeJsonFile(File var0, JSONObject var1) {
      try {
         Files.writeString(var0.toPath(), toJsonString(var1), StandardCharsets.UTF_8);
      } catch (IOException var3) {
         throw new RuntimeException("Failed to write JSON to file: " + var0.getAbsolutePath(), var3);
      }
   }
}
