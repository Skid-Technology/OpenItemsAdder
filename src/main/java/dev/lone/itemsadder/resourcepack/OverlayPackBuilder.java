package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

public final class OverlayPackBuilder {
   private static final Set bvS = Set.of("model", "range_dispatch", "select", "condition", "composite", "empty", "special", "bundle/selected_item");
   private static final Set bvT = Set.of("type", "property", "index", "scale", "entries", "fallback");
   private static final int bvU = 8388608;

   private OverlayPackBuilder() {
   }

   public static OverlayPackFile buildOwnedOverlay(JSONObject var0, JSONObject var1, Set var2) {
      try {
         JSONObject var3 = requireTopModel(var0, "external");
         JSONObject var4 = requireTopModel(var1, "generated");
         validateModel(var3, "external.model", 0);
         checkRootMetadata(var0, var1, false);
         if (!"range_dispatch".equals(modelType(var4, "generated.model"))) {
            throw invalid("generated.model must be a range_dispatch");
         }

         for (String var6 : var4.keySet()) {
            if (!bvT.contains(var6)) {
               throw invalid("generated.model has unsupported root field '" + var6 + "'");
            }
         }

         PackElementSpec var17 = parseSpec(var4, "generated.model");
         requireCustomModelData(var17, "generated.model");
         if (var17.bwa != 0 || Float.compare(var17.bwb, 1.0F) != 0) {
            throw invalid("generated.model must use custom_model_data index 0 and scale 1");
         }

         if (var2 == null) {
            throw invalid("ownedIds is null");
         }

         TreeSet var18 = new TreeSet();

         for (Integer var8 : var2) {
            if (var8 == null || var8 < 0) {
               throw invalid("ownedIds contains a null or negative ID");
            }

            if (var8.intValue() != var8.doubleValue()) {
               throw invalid("owned ID " + var8 + " is not exactly representable as a float");
            }

            var18.add(var8);
         }

         if (var18.isEmpty()) {
            return toPackFile(var0);
         }

         LinkedHashMap var19 = new LinkedHashMap();
         int var20 = 0;

         for (int var10 : var18) {
            while (var20 < var17.bwd.size() && ((PackScaleEntry)var17.bwd.get(var20)).bvV < var10) {
               var20++;
            }

            if (var20 == var17.bwd.size() || ((PackScaleEntry)var17.bwd.get(var20)).bvV != var10) {
               throw invalid("generated.model has no exact entry for owned ID " + var10);
            }

            var19.put(var10, ((PackScaleEntry)var17.bwd.get(var20)).bvX);
         }

         checkOwnedConflicts(var3, var19, "external.model", 0);
         JSONObject var21 = new JSONObject(var0);
         var21.remove("model");

         for (Entry var11 : var1.entrySet()) {
            if (!"model".equals(var11.getKey())) {
               var21.put((String)var11.getKey(), var11.getValue());
            }
         }

         JSONObject var23 = var3;
         TreeMap var24 = new TreeMap();

         for (Entry var13 : var19.entrySet()) {
            float var14 = ((Integer)var13.getKey()).intValue();
            var24.put(var14, (JSONObject)var13.getValue());
            var24.putIfAbsent(Math.nextUp(var14), var23);
         }

         return mergeRangeDispatch(var21, var23, var24);
      } catch (InvalidPackException var15) {
         return errorFile(var15.getMessage());
      } catch (RuntimeException var16) {
         return errorFile("invalid JSON graph: " + var16.getMessage());
      }
   }

   private static OverlayPackFile mergeRangeDispatch(JSONObject var0, JSONObject var1, TreeMap var2) {
      byte[] var3 = JSON.toJSONBytes(var0);
      byte[] var4 = (var0.isEmpty() ? "" : ",")
         .concat("\"model\":{\"type\":\"range_dispatch\",\"property\":\"custom_model_data\",\"index\":0,\"scale\":1,\"fallback\":")
         .getBytes(StandardCharsets.UTF_8);
      byte[] var5 = ",\"entries\":[".getBytes(StandardCharsets.UTF_8);
      IdentityHashMap var6 = new IdentityHashMap();
      byte[] var7 = JSON.toJSONBytes(var1);
      var6.put(var1, var7);
      ArrayList var8 = new ArrayList(var2.size());
      ArrayList var9 = new ArrayList(var2.size());
      long var10 = var3.length - 1L + var4.length + var7.length + var5.length + 3L;

      for (Entry var13 : var2.entrySet()) {
         byte[] var14 = ((var8.isEmpty() ? "" : ",") + "{\"threshold\":" + var13.getKey() + ",\"model\":").getBytes(StandardCharsets.UTF_8);
         byte[] var15 = (byte[])var6.computeIfAbsent((JSONObject)var13.getValue(), JSON::toJSONBytes);
         var10 += var14.length + var15.length + 1;
         if (var10 > 8388608L) {
            throw invalid("merged definition exceeds the 8 MiB safety limit");
         }

         var8.add(var14);
         var9.add(var15);
      }

      if (var10 > 8388608L) {
         throw invalid("merged definition exceeds the 8 MiB safety limit");
      }

      ByteBuffer var16 = ByteBuffer.allocate((int)var10);
      var16.put(var3, 0, var3.length - 1);
      var16.put(var4);
      var16.put(var7);
      var16.put(var5);

      for (int var17 = 0; var17 < var8.size(); var17++) {
         var16.put((byte[])var8.get(var17));
         var16.put((byte[])var9.get(var17));
         var16.put((byte)125);
      }

      var16.put((byte)93).put((byte)125).put((byte)125);
      return new OverlayPackFile(var16.array(), null);
   }

   public static OverlayPackFile mergeDefinitions(JSONObject var0, JSONObject var1) {
      try {
         if (var0 != null && var1 != null) {
            JSONObject var2 = requireTopModel(var0, "lower");
            JSONObject var3 = requireTopModel(var1, "higher");
            validateModel(var2, "lower.model", 0);
            validateModel(var3, "higher.model", 0);
            if (Objects.equals(var0, var1)) {
               return toPackFile(var0);
            }

            checkRootMetadata(var0, var1, true);
            String var4 = modelType(var2, "lower.model");
            String var5 = modelType(var3, "higher.model");
            if (!var4.equals(var5)) {
               throw invalid("top-level model types differ");
            }

            JSONObject var6 = switch (var4) {
               case "range_dispatch" -> mergeRangeDispatchModels(var2, var3);
               case "select" -> mergeSelectModels(var2, var3);
               default -> throw invalid("unsupported top-level model type '" + var4 + "'");
            };
            JSONObject var11 = new JSONObject(var1);
            var11.put("model", var6);
            return toPackFile(var11);
         } else {
            throw invalid("definitions must not be null");
         }
      } catch (InvalidPackException var9) {
         return errorFile(var9.getMessage());
      } catch (RuntimeException var10) {
         return errorFile("invalid JSON graph: " + var10.getMessage());
      }
   }

   private static JSONObject mergeRangeDispatchModels(JSONObject var0, JSONObject var1) {
      PackElementSpec var2 = parseSpec(var0, "lower.model");
      PackElementSpec var3 = parseSpec(var1, "higher.model");
      requireCustomModelData(var2, "lower.model");
      requireCustomModelData(var3, "higher.model");
      if (var2.bwa == var3.bwa && Float.compare(var2.bwb, var3.bwb) == 0 && sameNamespacedId(var2.bvZ, var3.bvZ)) {
         if (!Objects.equals(var2.bwc, var3.bwc)) {
            throw invalid("range dispatcher fallbacks differ");
         }

         checkSharedFields(var0, var1, Set.of("type", "property", "index", "scale", "entries", "fallback"), "range dispatcher");
         TreeMap var4 = new TreeMap(Comparator.naturalOrder());
         var2.bwd.forEach(var1x -> var4.put(var1x.bvV, var1x.bvW));
         var3.bwd.forEach(var1x -> var4.putIfAbsent(var1x.bvV, var1x.bvW));
         JSONArray var5 = new JSONArray();
         JSONObject var6 = var2.bwc;

         for (Entry var8 : var4.entrySet()) {
            JSONObject var9 = modelForThreshold(var2, (Float)var8.getKey());
            JSONObject var10 = modelForThreshold(var3, (Float)var8.getKey());
            boolean var11 = !Objects.equals(var9, var2.bwc);
            boolean var12 = !Objects.equals(var10, var3.bwc);
            if (var11 && var12 && !Objects.equals(var9, var10)) {
               throw invalid("range dispatchers overlap at threshold " + var8.getValue());
            }

            JSONObject var13 = var11 ? var9 : (var12 ? var10 : var2.bwc);
            if (!Objects.equals(var6, var13)) {
               var5.add(new JSONObject().fluentPut("threshold", var8.getValue()).fluentPut("model", var13));
               var6 = var13;
            }
         }

         JSONObject var14 = new JSONObject(var0);
         var14.put("entries", var5);
         return var14;
      } else {
         throw invalid("range dispatcher property, index or scale differs");
      }
   }

   private static JSONObject mergeSelectModels(JSONObject var0, JSONObject var1) {
      String var2 = requireProperty(var0, "lower.model");
      String var3 = requireProperty(var1, "higher.model");
      int var4 = requireIndex(var0, "lower.model");
      int var5 = requireIndex(var1, "higher.model");
      if (isCustomModelData(var2) && isCustomModelData(var3) && var4 == var5) {
         JSONObject var6 = optionalModelObject(var0, "fallback", "lower.model");
         JSONObject var7 = optionalModelObject(var1, "fallback", "higher.model");
         if (!Objects.equals(var6, var7)) {
            throw invalid("select fallbacks differ");
         }

         checkSharedFields(var0, var1, Set.of("type", "property", "index", "cases", "fallback"), "select dispatcher");
         OverlayPackDefinition var8 = parseSelect(var0, "lower.model");
         OverlayPackDefinition var9 = parseSelect(var1, "higher.model");
         LinkedHashMap var10 = new LinkedHashMap(var8.bwh);

         for (Entry var12 : var9.bwh.entrySet()) {
            JSONObject var13 = (JSONObject)var10.get(var12.getKey());
            if (var13 != null && !Objects.equals(var13, var12.getValue())) {
               throw invalid("select dispatchers overlap at case '" + (String)var12.getKey() + "'");
            }

            var10.putIfAbsent((String)var12.getKey(), (JSONObject)var12.getValue());
         }

         JSONArray var14 = new JSONArray();
         var10.forEach((var1x, var2x) -> var14.add(new JSONObject().fluentPut("when", var1x).fluentPut("model", var2x)));
         JSONObject var15 = new JSONObject(var0);
         var15.put("cases", var14);
         return var15;
      } else {
         throw invalid("select models must use the same custom_model_data index");
      }
   }

   private static void checkOwnedConflicts(JSONObject var0, Map var1, String var2, int var3) {
      if (var3 > 128) {
         throw invalid(var2 + " is nested too deeply");
      }

      switch (modelType(var0, var2)) {
         case "range_dispatch":
            PackElementSpec var12 = parseRangeDispatch(var0, var2, var3, false);
            if (isCustomModelData(var12.bvZ) && var12.bwa == 0) {
               for (Entry var16 : var1.entrySet()) {
                  float var17 = ((Integer)var16.getKey()).intValue() * var12.bwb;
                  if (!var12.bwd.stream().noneMatch(var1x -> var1x.bvV == var17)) {
                     JSONObject var10 = modelForOwnedId(var12, ((Integer)var16.getKey()).intValue());
                     if (var10 != null && !Objects.equals(var10, var12.bwc) && !Objects.equals(var10, var16.getValue())) {
                        throw invalid(var2 + " custom_model_data overlaps owned ID " + var16.getKey());
                     }
                  }
               }
            }

            if (var12.bwc != null) {
               checkOwnedConflicts(var12.bwc, var1, var2 + ".fallback", var3 + 1);
            }

            for (int var15 = 0; var15 < var12.bwd.size(); var15++) {
               checkOwnedConflicts(((PackScaleEntry)var12.bwd.get(var15)).bvX, var1, var2 + ".entries[" + var15 + "].model", var3 + 1);
            }
            break;
         case "select":
            JSONObject var11 = var0.getJSONObject("fallback");
            if (var11 != null) {
               checkOwnedConflicts(var11, var1, var2 + ".fallback", var3 + 1);
            }

            int var13 = 0;

            for (Object var9 : var0.getJSONArray("cases")) {
               checkOwnedConflicts(((JSONObject)var9).getJSONObject("model"), var1, var2 + ".cases[" + var13++ + "].model", var3 + 1);
            }
            break;
         case "condition":
            checkOwnedConflicts(var0.getJSONObject("on_true"), var1, var2 + ".on_true", var3 + 1);
            checkOwnedConflicts(var0.getJSONObject("on_false"), var1, var2 + ".on_false", var3 + 1);
            break;
         case "composite":
            JSONArray var6 = var0.getJSONArray("models");

            for (int var7 = 0; var7 < var6.size(); var7++) {
               checkOwnedConflicts(var6.getJSONObject(var7), var1, var2 + ".models[" + var7 + "]", var3 + 1);
            }
      }
   }

   public static void validateVanillaModel(JSONObject var0) {
      try {
         validateModel(requireTopModel(var0, "vanilla"), "vanilla.model", 0);
      } catch (InvalidPackException var2) {
         throw new IllegalArgumentException(var2.getMessage(), var2);
      }
   }

   private static void validateModel(JSONObject var0, String var1, int var2) {
      if (var2 > 128) {
         throw invalid(var1 + " is nested too deeply");
      }

      switch (modelType(var0, var1)) {
         case "model":
            if (!(var0.get("model") instanceof String)) {
               throw invalid(var1 + ".model must be a string");
            }
            break;
         case "range_dispatch":
            parseSpecDepth(var0, var1, var2);
            break;
         case "select":
            parseSelectDepth(var0, var1, var2);
            break;
         case "condition":
            requireProperty(var0, var1);
            requireModelObject(var0, "on_true", var1, var2);
            requireModelObject(var0, "on_false", var1, var2);
            break;
         case "composite":
            if (!(var0.get("models") instanceof JSONArray var6)) {
               throw invalid(var1 + ".models must be an array");
            }

            for (int var7 = 0; var7 < var6.size(); var7++) {
               if (!(var6.get(var7) instanceof JSONObject var8)) {
                  throw invalid(var1 + ".models[" + var7 + "] must be an object");
               }

               validateModel(var8, var1 + ".models[" + var7 + "]", var2 + 1);
            }
         case "empty":
         case "bundle/selected_item":
            break;
         case "special":
            if (!(var0.get("base") instanceof String)) {
               throw invalid(var1 + ".base must be a string");
            }

            if (!(var0.get("model") instanceof JSONObject)) {
               throw invalid(var1 + ".model must be an opaque object");
            }
            break;
         default:
            throw invalid(var1 + " has an unknown model type");
      }
   }

   private static PackElementSpec parseSpec(JSONObject var0, String var1) {
      return parseSpecDepth(var0, var1, 0);
   }

   private static PackElementSpec parseSpecDepth(JSONObject var0, String var1, int var2) {
      return parseRangeDispatch(var0, var1, var2, true);
   }

   private static PackElementSpec parseRangeDispatch(JSONObject var0, String var1, int var2, boolean var3) {
      String var4 = requireProperty(var0, var1);
      int var5 = requireIndex(var0, var1);
      float var6 = optionalFloat(var0, "scale", 1.0F, var1);
      JSONObject var7 = optionalModelObject(var0, "fallback", var1);
      if (var7 != null && var3) {
         validateModel(var7, var1 + ".fallback", var2 + 1);
      }

      if (!(var0.get("entries") instanceof JSONArray var9)) {
         throw invalid(var1 + ".entries must be an array");
      } else {
         ArrayList var10 = new ArrayList(var9.size());
         float var11 = 0.0F;
         boolean var12 = true;

         for (int var13 = 0; var13 < var9.size(); var13++) {
            if (!(var9.get(var13) instanceof JSONObject var14)) {
               throw invalid(var1 + ".entries[" + var13 + "] must be an object");
            }

            if (!(var14.get("threshold") instanceof Number var16)) {
               throw invalid(var1 + ".entries[" + var13 + "].threshold must be a number");
            }

            float var17 = var16.floatValue();
            if (!Float.isFinite(var17)) {
               throw invalid(var1 + ".entries[" + var13 + "].threshold must be finite");
            }

            if (!var12 && var17 <= var11) {
               throw invalid(var1 + ".entries thresholds must be strictly increasing and unique as floats");
            }

            JSONObject var18 = var3 ? requireModelObject(var14, "model", var1 + ".entries[" + var13 + "]", var2) : var14.getJSONObject("model");
            var10.add(new PackScaleEntry(var17, var16, var18));
            var11 = var17;
            var12 = false;
         }

         return new PackElementSpec(var0, var4, var5, var6, var7, var10);
      }
   }

   private static OverlayPackDefinition parseSelect(JSONObject var0, String var1) {
      return parseSelectDepth(var0, var1, 0);
   }

   private static OverlayPackDefinition parseSelectDepth(JSONObject var0, String var1, int var2) {
      requireProperty(var0, var1);
      requireIndex(var0, var1);
      JSONObject var3 = optionalModelObject(var0, "fallback", var1);
      if (var3 != null) {
         validateModel(var3, var1 + ".fallback", var2 + 1);
      }

      if (!(var0.get("cases") instanceof JSONArray var5)) {
         throw invalid(var1 + ".cases must be an array");
      } else {
         LinkedHashMap var6 = new LinkedHashMap();

         for (int var7 = 0; var7 < var5.size(); var7++) {
            if (!(var5.get(var7) instanceof JSONObject var8)) {
               throw invalid(var1 + ".cases[" + var7 + "] must be an object");
            }

            JSONObject var16 = requireModelObject(var8, "model", var1 + ".cases[" + var7 + "]", var2);
            Object var10 = var8.get("when");
            if (var10 instanceof String var12) {
               addSelectCase(var6, var12, var16, var1);
            } else {
               if (!(var10 instanceof JSONArray var11) || var11.isEmpty()) {
                  throw invalid(var1 + ".cases.when must be a string or non-empty string array");
               }

               for (Object var14 : var11) {
                  if (!(var14 instanceof String var15)) {
                     throw invalid(var1 + ".cases.when must contain only strings");
                  }

                  addSelectCase(var6, var15, var16, var1);
               }
            }
         }

         return new OverlayPackDefinition(var3, var6);
      }
   }

   private static void addSelectCase(Map var0, String var1, JSONObject var2, String var3) {
      if (var0.putIfAbsent(var1, var2) != null) {
         throw invalid(var3 + " has duplicate case '" + var1 + "'");
      }
   }

   private static JSONObject requireModelObject(JSONObject var0, String var1, String var2, int var3) {
      if (var0.get(var1) instanceof JSONObject var5) {
         validateModel(var5, var2 + "." + var1, var3 + 1);
         return var5;
      } else {
         throw invalid(var2 + "." + var1 + " must be an object");
      }
   }

   private static JSONObject optionalModelObject(JSONObject var0, String var1, String var2) {
      Object var3 = var0.get(var1);
      if (var3 == null) {
         return null;
      } else if (var3 instanceof JSONObject var4) {
         return var4;
      } else {
         throw invalid(var2 + "." + var1 + " must be an object");
      }
   }

   private static JSONObject requireTopModel(JSONObject var0, String var1) {
      if (var0 == null) {
         throw invalid(var1 + " definition is null");
      } else if (var0.get("model") instanceof JSONObject var3) {
         return var3;
      } else {
         throw invalid(var1 + ".model must be an object");
      }
   }

   private static String modelType(JSONObject var0, String var1) {
      if (var0.get("type") instanceof String var3) {
         String var4 = stripMinecraftPrefix(var3);
         if (!bvS.contains(var4)) {
            throw invalid(var1 + " has unknown model type '" + var3 + "'");
         } else {
            return var4;
         }
      } else {
         throw invalid(var1 + ".type must be a string");
      }
   }

   private static String requireProperty(JSONObject var0, String var1) {
      if (var0.get("property") instanceof String var3 && !var3.isEmpty()) {
         return var3;
      } else {
         throw invalid(var1 + ".property must be a non-empty string");
      }
   }

   private static int requireIndex(JSONObject var0, String var1) {
      Object var2 = var0.get("index");
      if (var2 == null) {
         return 0;
      } else if (var2 instanceof Number var3 && var3.doubleValue() == var3.intValue() && var3.intValue() >= 0) {
         return var3.intValue();
      } else {
         throw invalid(var1 + ".index must be a nonnegative integer");
      }
   }

   private static float optionalFloat(JSONObject var0, String var1, float var2, String var3) {
      Object var4 = var0.get(var1);
      if (var4 == null) {
         return var2;
      } else if (var4 instanceof Number var5 && Float.isFinite(var5.floatValue())) {
         return var5.floatValue();
      } else {
         throw invalid(var3 + "." + var1 + " must be a finite number");
      }
   }

   private static void requireCustomModelData(PackElementSpec var0, String var1) {
      if (!isCustomModelData(var0.bvZ)) {
         throw invalid(var1 + " must use custom_model_data");
      }
   }

   private static boolean isCustomModelData(String var0) {
      return "custom_model_data".equals(stripMinecraftPrefix(var0));
   }

   private static boolean sameNamespacedId(String var0, String var1) {
      return stripMinecraftPrefix(var0).equals(stripMinecraftPrefix(var1));
   }

   private static String stripMinecraftPrefix(String var0) {
      return var0.startsWith("minecraft:") ? var0.substring("minecraft:".length()) : var0;
   }

   private static JSONObject modelForOwnedId(PackElementSpec var0, float var1) {
      return modelForThreshold(var0, var1 * var0.bwb);
   }

   private static JSONObject modelForThreshold(PackElementSpec var0, float var1) {
      if (Float.isNaN(var1)) {
         return var0.bwc;
      }

      int var2 = 0;
      int var3 = var0.bwd.size();

      while (var2 < var3) {
         int var4 = var2 + var3 >>> 1;
         if (((PackScaleEntry)var0.bwd.get(var4)).bvV <= var1) {
            var2 = var4 + 1;
         } else {
            var3 = var4;
         }
      }

      return var2 == 0 ? var0.bwc : ((PackScaleEntry)var0.bwd.get(var2 - 1)).bvX;
   }

   private static void checkRootMetadata(JSONObject var0, JSONObject var1, boolean var2) {
      Map var3 = copyExcept(var0, Set.of("model"));
      Map var4 = copyExcept(var1, Set.of("model"));
      if (var2) {
         if (!var4.entrySet().containsAll(var3.entrySet())) {
            throw invalid("root metadata differs from higher-priority definition");
         }
      } else {
         for (Entry var6 : var3.entrySet()) {
            if (var4.containsKey(var6.getKey()) && !Objects.equals(var6.getValue(), var4.get(var6.getKey()))) {
               throw invalid("root metadata conflicts at '" + (String)var6.getKey() + "'");
            }
         }
      }
   }

   private static void checkSharedFields(JSONObject var0, JSONObject var1, Set var2, String var3) {
      if (!copyExcept(var0, var2).equals(copyExcept(var1, var2))) {
         throw invalid(var3 + " fields differ");
      }
   }

   private static Map copyExcept(JSONObject var0, Set var1) {
      LinkedHashMap var2 = new LinkedHashMap();
      var0.forEach((var2x, var3) -> {
         if (!var1.contains(var2x)) {
            var2.put(var2x, var3);
         }
      });
      return var2;
   }

   private static OverlayPackFile toPackFile(JSONObject var0) {
      byte[] var1 = JSON.toJSONBytes(var0);
      if (var1.length > 8388608) {
         throw invalid("merged definition exceeds the 8 MiB safety limit");
      } else {
         return new OverlayPackFile(var1, null);
      }
   }

   private static OverlayPackFile errorFile(String var0) {
      return new OverlayPackFile(null, var0);
   }

   private static InvalidPackException invalid(String var0) {
      return new InvalidPackException(var0);
   }
}
