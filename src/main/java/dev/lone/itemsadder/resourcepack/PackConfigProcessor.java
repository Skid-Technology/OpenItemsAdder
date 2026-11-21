package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;

public class PackConfigProcessor {
   private static final Set bIv = Set.of(-45.0, -22.5, 0.0, 22.5, 45.0);
   private static final Set bIw = ConcurrentHashMap.newKeySet();
   private static final Set bIx = ConcurrentHashMap.newKeySet();
   private static final Set bIy = ConcurrentHashMap.newKeySet();
   private static final Set bIz = ConcurrentHashMap.newKeySet();
   private static final Map bIA = new ConcurrentHashMap();
   final String bIB;
   private final Consumer bIC;

   public PackConfigProcessor(String var1) {
      this(var1, Log::warn);
   }

   PackConfigProcessor(String var1, Consumer var2) {
      this.bIB = var1;
      this.bIC = var2;
   }

   public static void resetDedupState() {
      bIw.clear();
      bIx.clear();
      bIy.clear();
      bIz.clear();
      bIA.clear();
   }

   public static void reportMissingTextures(Predicate var0) {
      reportMissingTexturesTo(var0, Log::error);
   }

   static void reportMissingTexturesTo(Predicate var0, Consumer var1) {
      try {
         bIA.entrySet()
            .stream()
            .filter(var1x -> !var0.test((String)var1x.getKey()))
            .sorted(Entry.comparingByKey())
            .map(PackConfigProcessor::formatMissingTextureReport)
            .forEach(var1);
      } finally {
         bIA.clear();
      }
   }

   public PackManifest process(JSONObject var1) {
      boolean var2 = this.repairGeneratedLayer0(var1);
      String var3 = validateStructure(var1);
      if (var3 != null) {
         if (!this.isEmptyPlaceholderModel(var1)) {
            this.replaceWithMissingModel(var1, var3);
            PackManifest var14 = new PackManifest(this.bIB, var1);
            var14.bIL = true;
            return var14;
         }

         var1.clear();
         var1.put("parent", "minecraft:item/air");
         var2 = true;
      }

      if (Settings.get().logNonStandardRotations) {
         this.logNonStandardRotations(var1);
      }

      this.validateDisplayScales(var1);
      PackManifest var4 = new PackManifest(this.bIB, var1);
      var4.bIL = var2;
      if (var1.containsKey("textures")) {
         JSONObject var5 = var1.getJSONObject("textures");

         for (Entry var7 : var5.entrySet()) {
            Object var8 = var7.getValue();
            String var9 = var8 instanceof String var11 ? var11 : (var8 instanceof JSONObject var10 ? var10.getString("sprite") : null);
            if (var9 != null && !PackFileEntry.isSpecialRef(var9)) {
               List var15 = ResourcePackWriter.findInvalidPathChars(var9);
               if (var15 != null) {
                  Log.error(
                     "Texture '"
                        + var9
                        + "' has invalid characters: '"
                        + Utils.joinComma(var15)
                        + "'. Do not use special or UPPERCASE characters. File: "
                        + Utils.shortPathOf(this.getSourcePath())
                  );
               } else {
                  String var16 = ResourcePackGenerator.buildAssetFilePath(var9, PackAssetKind.TEXTURES, "png");
                  if ((!ResourcePackGenerator.isVanillaOrPlainId(var9) || !ContentManager.method8084().field91.hasVanillaTexture(var9))
                     && !ContentManager.method8078(var9)) {
                     File var12 = ContentManager.method8198(var16);
                     if (var12 != null) {
                        var4.addFileEntry(var12, var9);
                     } else {
                        String var13 = Utils.shortPathOf(this.getSourcePath());
                        recordMissingTexture(var9, var13);
                        var4.addSkipped(var9);
                     }
                  } else if (!var9.contains("item/") && !var9.contains("block/")) {
                     var4.addAssetEntry(var16, var9);
                  }
               }
            }
         }
      }

      return var4;
   }

   private boolean repairGeneratedLayer0(JSONObject var1) {
      String var2 = var1.getString("parent");
      if (var1.get("textures") instanceof JSONObject var4
         && !var1.containsKey("elements")
         && !var4.containsKey("layer0")
         && ("item/generated".equals(var2) || "minecraft:item/generated".equals(var2))) {
         Object var5 = var4.get("0");
         if (!(var5 instanceof String) && !(var5 instanceof JSONObject)) {
            return false;
         }

         var4.put("layer0", var5);
         String var6 = Utils.shortPathOf(this.getSourcePath());
         String var7 = "Model: "
            + var6
            + "\nReason: item/generated requires 'layer0' when the model has no elements\nItemsAdder copied texture '0' to 'layer0'.";
         PackWarningSummary.addWarning("Repaired model textures", var7);
         if (bIy.add(PackManifest.normalizeModelId(this.bIB))) {
            this.bIC.accept("Model used texture '0' where item/generated requires 'layer0'; repaired automatically. File: " + var6);
         }

         return true;
      } else {
         return false;
      }
   }

   public static String validateStructure(JSONObject var0) {
      Object var1 = var0.get("textures");
      if (var1 != null && !(var1 instanceof JSONObject)) {
         return "'textures' must be a JSON object";
      }

      if (var1 instanceof JSONObject var2) {
         for (Object var5 : var2.values()) {
            if (!(var5 instanceof String)
               && (
                  !(var5 instanceof JSONObject var3)
                     || !(var3.get("sprite") instanceof String)
                     || var3.containsKey("force_translucent") && !(var3.get("force_translucent") instanceof Boolean)
               )) {
               return "texture values must be strings or material objects with a string 'sprite' and optional boolean 'force_translucent'";
            }
         }
      }

      Object var12 = var0.get("elements");
      if (var12 == null) {
         return null;
      } else if (!(var12 instanceof JSONArray var13)) {
         return "'elements' must be a JSON array";
      } else {
         for (int var14 = 0; var14 < var13.size(); var14++) {
            if (!(var13.get(var14) instanceof JSONObject var15)) {
               return "element " + var14 + " must be a JSON object";
            }

            if (!isVector3(var15.get("from"))) {
               return "element " + var14 + " has a missing or invalid 'from' vector";
            }

            if (!isVector3(var15.get("to"))) {
               return "element " + var14 + " has a missing or invalid 'to' vector";
            }

            Object var16 = var15.get("faces");
            if (var16 != null && !(var16 instanceof JSONObject)) {
               return "element " + var14 + " has invalid 'faces'";
            }

            if (var16 instanceof JSONObject var7) {
               for (Entry var9 : var7.entrySet()) {
                  if (!(var9.getValue() instanceof JSONObject var10)) {
                     return "element " + var14 + " has an invalid '" + (String)var9.getKey() + "' face";
                  }

                  if (var10.containsKey("texture") && !(var10.get("texture") instanceof String)) {
                     return "element " + var14 + " face '" + (String)var9.getKey() + "' has an invalid texture reference";
                  }
               }
            }
         }

         return null;
      }
   }

   private static boolean isVector3(Object var0) {
      return var0 instanceof JSONArray var1 && var1.size() == 3
         ? var1.stream().allMatch(var0x -> var0x instanceof Number var1x && Double.isFinite(var1x.doubleValue()))
         : false;
   }

   private boolean isEmptyPlaceholderModel(JSONObject var1) {
      return var1.get("textures") instanceof JSONObject var2
            && var2.isEmpty()
            && var1.get("elements") instanceof JSONArray var3
            && !var3.isEmpty()
            && !var3.stream().anyMatch(var0 -> !(var0 instanceof JSONObject var1x && var1x.isEmpty()))
         ? ResourcePackGenerator.isMetadataDir(this.getSourceFile())
         : false;
   }

   private void replaceWithMissingModel(JSONObject var1, String var2) {
      String var3 = Utils.shortPathOf(this.getSourcePath());
      String var4 = "Model: " + var3 + "\nReason: " + var2 + "\nThe generated pack contains an ItemsAdder missing-texture model instead.";
      PackWarningSummary.addWarning("Malformed models", var4);
      if (bIx.add(PackManifest.normalizeModelId(this.bIB))) {
         this.bIC.accept("Malformed model replaced with the ItemsAdder missing texture: " + var2 + ". File: " + var3);
      }

      var1.clear();
      var1.put("parent", "minecraft:item/generated");
      var1.put("textures", new JSONObject().fluentPut("layer0", "minecraft:item/iamissing").fluentPut("particle", "minecraft:item/iamissing"));
   }

   static void recordMissingTexture(String var0, String var1) {
      bIA.computeIfAbsent(var0, var0x -> ConcurrentHashMap.newKeySet()).add(var1);
   }

   private static String formatMissingTextureReport(Entry var0) {
      ArrayList var1 = new ArrayList((Collection)var0.getValue());
      Collections.sort(var1);
      StringBuilder var2 = new StringBuilder("Texture '")
         .append((String)var0.getKey())
         .append("' not found; referenced by ")
         .append(var1.size())
         .append(" model")
         .append(var1.size() == 1 ? "" : "s")
         .append(":");
      var1.forEach(var1x -> var2.append("\n  - ").append(var1x));
      var2.append("\n  ").append(suggestFix((String)var0.getKey()));
      String var3 = var2.toString();
      PackWarningSummary.addWarning("Missing textures", var3);
      return var3;
   }

   static String suggestFix(String var0) {
      if (var0.contains(":textures/")) {
         return "Resource locations normally omit 'textures/' after the namespace";
      }

      int var1 = var0.indexOf(58);
      if (var1 <= 0) {
         return "Check the namespace and path for typos";
      }

      if (Main.getInstance() == null) {
         return "Check the namespace and path for typos";
      }

      String var2 = var0.substring(0, var1);
      String var3 = var0.substring(var1 + 1);

      try {
         String var4 = null;
         int var5 = Integer.MAX_VALUE;

         for (File var7 : ContentManager.method8203()) {
            String var8 = var7.getName();
            if (!var8.equals(var2)) {
               String var9 = var8 + ":" + var3;
               String var10 = ResourcePackGenerator.buildAssetFilePath(var9, PackAssetKind.TEXTURES, "png");
               if (ContentManager.method8198(var10) != null) {
                  int var11 = levenshtein(var2, var8);
                  if (var11 < var5) {
                     var4 = var8;
                     var5 = var11;
                  }
               }
            }
         }

         if (var4 != null && var5 <= 3) {
            return "Possible namespace typo: did you mean '" + var4 + ":" + var3 + "'?";
         }
      } catch (RuntimeException var12) {
      }

      return "Check the namespace and path for typos";
   }

   private static int levenshtein(String var0, String var1) {
      int[] var2 = new int[var1.length() + 1];
      int var3 = 0;

      while (var3 < var2.length) {
         var2[var3] = var3++;
      }

      for (int var7 = 1; var7 <= var0.length(); var7++) {
         int var4 = var2[0];
         var2[0] = var7;

         for (int var5 = 1; var5 <= var1.length(); var5++) {
            int var6 = var2[var5];
            var2[var5] = Math.min(Math.min(var2[var5] + 1, var2[var5 - 1] + 1), var4 + (var0.charAt(var7 - 1) == var1.charAt(var5 - 1) ? 0 : 1));
            var4 = var6;
         }
      }

      return var2[var1.length()];
   }

   private void logNonStandardRotations(JSONObject var1) {
      JSONArray var2 = var1.getJSONArray("elements");
      if (var2 != null) {
         LinkedHashSet var3 = new LinkedHashSet();

         for (int var4 = 0; var4 < var2.size(); var4++) {
            JSONObject var5 = var2.getJSONObject(var4).getJSONObject("rotation");
            if (var5 != null && var5.get("angle") instanceof Number var7 && !bIv.contains(var7.doubleValue())) {
               var3.add(var7.doubleValue());
            }
         }

         String var8 = PackManifest.normalizeModelId(this.bIB);
         if (!var3.isEmpty() && bIw.add(var8)) {
            this.bIC
               .accept(
                  "Model uses non-standard element rotation angles "
                     + var3
                     + ". Clients older than Minecraft 1.21.6 only support -45, -22.5, 0, 22.5 and 45 degrees; Minecraft 1.21.6 and newer support 0.5-degree steps. File: "
                     + Utils.shortPathOf(this.getSourcePath())
               );
         }
      }
   }

   private void validateDisplayScales(JSONObject var1) {
      if (var1.get("display") instanceof JSONObject var2) {
         String var13 = Utils.shortPathOf(this.getSourcePath());

         for (Entry var5 : var2.entrySet()) {
            if (var5.getValue() instanceof JSONObject var6 && var6.containsKey("scale")) {
               Object var14 = var6.get("scale");
               String var8 = null;
               if (var14 instanceof JSONArray var9 && var9.size() == 3) {
                  Iterator var10 = var9.iterator();

                  while (var10.hasNext()) {
                     if (!(var10.next() instanceof Number var12) || !Double.isFinite(var12.doubleValue())) {
                        var8 = "expected exactly 3 finite numeric components";
                        break;
                     }

                     if (Math.abs(var12.doubleValue()) > 4.0) {
                        var8 = "component magnitude exceeds 4 (client clamps display scale to [-4, 4])";
                        break;
                     }
                  }
               } else {
                  var8 = "expected exactly 3 finite numeric components";
               }

               if (var8 != null) {
                  String var15 = PackManifest.normalizeModelId(this.bIB) + ":" + (String)var5.getKey();
                  if (bIz.add(var15)) {
                     this.bIC.accept("Invalid display scale " + var14 + " in slot '" + (String)var5.getKey() + "': " + var8 + ". File: " + var13);
                  }
               }
            }
         }
      }
   }

   private String getSourcePath() {
      File var1 = this.getSourceFile();
      return var1 == null ? this.bIB : var1.getPath();
   }

   @Nullable
   private File getSourceFile() {
      String var1 = Utils.normalizeSlashes(this.bIB);
      int var2 = var1.indexOf("assets/");
      if (var2 >= 0 && Main.getInstance() != null) {
         try {
            return ContentManager.method8198(var1.substring(var2));
         } catch (RuntimeException var4) {
            return null;
         }
      } else {
         File var3 = new File(this.bIB);
         return var3.isFile() ? var3 : null;
      }
   }
}
