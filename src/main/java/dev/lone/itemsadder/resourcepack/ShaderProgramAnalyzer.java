package dev.lone.itemsadder.resourcepack;

import beer.devs.fastnbt.nms.Version;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent.PackEntry;
import dev.lone.itemsadder.util.Utils;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

final class ShaderProgramAnalyzer {
   private final Map bEF;
   private final ShaderValidatorBackend bEG;
   private final ShaderSourceReader bEH;
   private final BiFunction bEI;
   private final LinkedHashSet bEJ = new LinkedHashSet();
   private final Map bEK = new HashMap();
   private final Map bEL = new HashMap();

   private ShaderProgramAnalyzer(Map var1, ShaderValidatorBackend var2, ShaderSourceReader var3, BiFunction var4) {
      this.bEF = var1;
      this.bEG = var2;
      this.bEH = var3;
      this.bEI = var4;
   }

   private void analyzeAll() {
      List var1 = this.bEF
         .entrySet()
         .stream()
         .filter(var0 -> !((PackEntry)var0.getValue()).isRemoved())
         .map(Entry::getKey)
         .filter(ShaderValidator::isShaderAssetPath)
         .distinct()
         .sorted()
         .toList();
      if (!var1.isEmpty()) {
         for (String var3 : var1) {
            this.analyzePath(var3);
         }

         JSONObject var4 = this.readPackMeta();
         HashSet var5 = new HashSet();
         this.analyzeOverlays(var4, var5);
      }
   }

   private void analyzePath(String var1) {
      if (var1.startsWith("/") || var1.contains("\\") || ShaderValidator.containsParentTraversal(var1)) {
         this.addIssue(
            ShaderSeverity.ERROR,
            "SHADER_PATH_INVALID",
            ShaderValidator.classifyShaderFile(var1),
            var1,
            var1,
            0,
            0,
            "Shader resource path contains traversal, a backslash, or a leading slash.",
            List.of(var1)
         );
      }

      String var2 = ShaderValidator.toShaderId(var1);
      if (!ShaderValidator.isValidShaderId(var2, false)) {
         this.addIssue(
            ShaderSeverity.ERROR,
            var1.equals(var1.toLowerCase(Locale.ROOT)) ? "SHADER_RESOURCE_LOCATION_INVALID" : "SHADER_PATH_CASE_MISMATCH",
            ShaderValidator.classifyShaderFile(var1),
            var1,
            var1,
            0,
            0,
            "Shader path is not a valid lowercase resource location: " + var2,
            List.of(var1)
         );
      }

      String var3 = this.readSource(var1);
      if (var3 != null) {
         if (var3.isEmpty()) {
            this.addIssue(
               ShaderSeverity.ERROR, "SHADER_FILE_EMPTY", ShaderValidator.classifyShaderFile(var1), var1, var1, 1, 1, "Shader file is empty.", List.of(var1)
            );
         } else {
            if (var1.endsWith(".json")) {
               this.validateJsonProgram(var1, var3);
            } else {
               ShaderFileKind var4 = ShaderValidator.classifyShaderFile(var1);
               ShaderProgramSpec var5 = ShaderValidator.analyzeSource(var3, var1, var4);
               this.bEL.put(var1, var5);
               var5.diagnostics().forEach(this::addRemappedIssue);
               if ((var4 == ShaderFileKind.VERTEX || var4 == ShaderFileKind.FRAGMENT) && !var5.hasMain()) {
                  this.addIssue(ShaderSeverity.ERROR, "SHADER_MAIN_MISSING", var4, var1, var1, 1, 1, "Shader stage does not declare main().", List.of(var1));
               }
            }
         }
      }
   }

   private void validateJsonProgram(String var1, String var2) {
      try {
         JSONObject var3 = JSON.parseObject(var2);
         if (var3 == null) {
            throw new IllegalArgumentException("expected a JSON object");
         }

         if (ShaderValidator.isCoreShaderProgram(var1)) {
            this.checkProgramStage(var1, var3, "vertex");
            this.checkProgramStage(var1, var3, "fragment");
         }

         if (ShaderValidator.isPostShaderProgram(var1)) {
            Object var4 = var3.get("passes");
            if (!(var4 instanceof JSONArray)) {
               this.addIssue(
                  ShaderSeverity.ERROR,
                  "SHADER_POST_PASSES_INVALID",
                  ShaderFileKind.PROGRAM,
                  var1,
                  var1,
                  1,
                  1,
                  "Post-processing JSON must contain a passes array.",
                  List.of(var1)
               );
               return;
            }

            for (Object var7 : (JSONArray)var4) {
               if (!(var7 instanceof JSONObject var8 && var8.get("name") instanceof String var9 && ShaderValidator.isValidShaderId(var9, true))) {
                  this.addIssue(
                     ShaderSeverity.ERROR,
                     "SHADER_RESOURCE_LOCATION_INVALID",
                     ShaderFileKind.PROGRAM,
                     var1,
                     var1,
                     1,
                     1,
                     "Post-processing pass has an invalid program resource location.",
                     List.of(var1)
                  );
               }
            }
         }
      } catch (Throwable var11) {
         this.addIssue(
            ShaderSeverity.ERROR, "SHADER_JSON_MALFORMED", ShaderFileKind.PROGRAM, var1, var1, 1, 1, ShaderValidator.rootCauseMessage(var11), List.of(var1)
         );
      }
   }

   private void checkProgramStage(String var1, JSONObject var2, String var3) {
      Object var4 = var2.get(var3);
      if (var4 == null) {
         this.addIssue(
            ShaderSeverity.ERROR,
            "SHADER_PROGRAM_STAGE_MISSING",
            ShaderFileKind.PROGRAM,
            var1,
            var1,
            1,
            1,
            "Shader program JSON is missing its " + var3 + " stage.",
            List.of(var1)
         );
      } else {
         if (!(var4 instanceof String var5 && ShaderValidator.isValidShaderId(var5, true))) {
            this.addIssue(
               ShaderSeverity.ERROR,
               "SHADER_RESOURCE_LOCATION_INVALID",
               ShaderFileKind.PROGRAM,
               var1,
               var1,
               1,
               1,
               "Invalid " + var3 + " shader resource location.",
               List.of(var1)
            );
         }
      }
   }

   private JSONObject readPackMeta() {
      PackEntry var1 = (PackEntry)this.bEF.get("pack.mcmeta");
      if (var1 != null && !var1.isRemoved()) {
         try {
            return JSON.parseObject(ShaderValidator.readPackEntryText(var1));
         } catch (Throwable var3) {
            this.addIssue(
               ShaderSeverity.ERROR,
               "SHADER_PACK_META_MALFORMED",
               ShaderFileKind.NONE,
               "pack.mcmeta",
               "pack.mcmeta",
               1,
               1,
               ShaderValidator.rootCauseMessage(var3),
               List.of()
            );
            return null;
         }
      } else {
         return null;
      }
   }

   private void analyzeOverlays(JSONObject var1, Set var2) {
      boolean var3 = false;
      if (var1 != null) {
         Function var4 = ResourcePackGenerator.createOverlayResolver(this.bEF, var1);

         for (Version var6 : VanillaAssetManager.ip) {
            try {
               this.analyzeVersion(new ShaderVersionMap(var6, (Map)var4.apply(var6)), var2);
               var3 = true;
            } catch (Throwable var8) {
               this.addIssue(
                  ShaderSeverity.ERROR,
                  "SHADER_OVERLAY_INVALID",
                  ShaderFileKind.NONE,
                  "pack.mcmeta",
                  "pack.mcmeta",
                  1,
                  1,
                  ShaderValidator.rootCauseMessage(var8),
                  List.of()
               );
               break;
            }
         }
      }

      if (!var3) {
         LinkedHashMap var9 = new LinkedHashMap();
         this.bEF.values().stream().filter(var0 -> !var0.isRemoved()).forEach(var1x -> {
            String var2x = Utils.normalizeSlashes(var1x.getPath());
            int var3x = var2x.indexOf("assets/");
            if (var3x >= 0) {
               var9.put(var2x.substring(var3x), var2x);
            }
         });
         this.analyzeVersion(new ShaderVersionMap(Version.UNKNOWN, var9), var2);
      }
   }

   private void analyzeVersion(ShaderVersionMap var1, Set var2) {
      HashMap var3 = new HashMap();
      var1.paths().keySet().forEach(var1x -> var3.putIfAbsent(var1x.toLowerCase(Locale.ROOT), var1x));
      LinkedHashMap var4 = new LinkedHashMap();

      for (Entry var6 : var1.paths().entrySet()) {
         ShaderFileKind var7 = ShaderValidator.classifyShaderFile((String)var6.getKey());
         if (var7 == ShaderFileKind.VERTEX || var7 == ShaderFileKind.FRAGMENT) {
            var4.put((String)var6.getKey(), this.resolveSource(var7, (String)var6.getKey(), var1, var3, new ArrayDeque(), new HashSet(), 0));
         }
      }

      Set var11 = this.collectProgramPairs(var1, var3);
      HashSet var12 = new HashSet();

      for (ShaderPair var8 : var11) {
         ShaderSource var9 = var4.computeIfAbsent(
            var8.vertex(), var3x -> this.resolveSource(ShaderFileKind.VERTEX, var3x, var1, var3, new ArrayDeque(), new HashSet(), 0)
         );
         ShaderSource var10 = var4.computeIfAbsent(
            var8.fragment(), var3x -> this.resolveSource(ShaderFileKind.FRAGMENT, var3x, var1, var3, new ArrayDeque(), new HashSet(), 0)
         );
         if (var9 != null && var10 != null && var9.complete() && var10.complete()) {
            var12.add(var8.vertex());
            var12.add(var8.fragment());
            if (this.isAnalyzable(var1, var8.vertex()) && this.isAnalyzable(var1, var8.fragment())) {
               this.validateGroup(var1, List.of(var9, var10), var2);
            }
         }
      }

      for (Entry var15 : var4.entrySet()) {
         if (!var12.contains(var15.getKey()) && ((ShaderSource)var15.getValue()).complete() && this.isAnalyzable(var1, (String)var15.getKey())) {
            this.validateGroup(var1, List.of((ShaderSource)var15.getValue()), var2);
         }
      }

      if (var1.version() != Version.UNKNOWN && var1.version().getPackVersion() < Version.v1_21_3.getPackVersion()) {
         this.checkLegacyProgramJsons(var1);
      }
   }

   private boolean isAnalyzable(ShaderVersionMap var1, String var2) {
      return var1.version() == Version.UNKNOWN
         || var2.startsWith("assets/minecraft/shaders/core/")
            && (var2.endsWith(".vsh") || var2.endsWith(".fsh"))
            && this.readVanillaSource(var1.version(), var2) != null;
   }

   private void checkLegacyProgramJsons(ShaderVersionMap var1) {
      HashSet var2 = new HashSet();

      for (Entry var4 : var1.paths().entrySet()) {
         if (ShaderValidator.isCoreShaderProgram((String)var4.getKey())) {
            String var5 = this.readSource((String)var4.getValue());
            if (var5 != null) {
               try {
                  JSONObject var6 = JSON.parseObject(var5);
                  var2.add(ShaderValidator.buildShaderAssetPath((String)var4.getKey(), var6.getString("vertex"), ".vsh"));
                  var2.add(ShaderValidator.buildShaderAssetPath((String)var4.getKey(), var6.getString("fragment"), ".fsh"));
               } catch (Throwable var8) {
               }
            }
         }
      }

      var2.remove(null);

      for (String var11 : var1.paths().keySet().stream().filter(var0 -> var0.endsWith(".vsh") || var0.endsWith(".fsh")).collect(Collectors.toSet())) {
         String var12 = ShaderValidator.extractNamespace(var11);
         if (!"minecraft".equals(var12)) {
            String var7 = var11.substring(0, var11.length() - 4) + ".json";
            if (!var1.paths().containsKey(var7) && !var2.contains(var11)) {
               this.addIssue(
                  ShaderSeverity.ERROR,
                  "SHADER_PROGRAM_JSON_MISSING",
                  ShaderValidator.classifyShaderFile(var11),
                  var11,
                  (String)var1.paths().get(var11),
                  0,
                  0,
                  "Legacy clients require a shader program JSON for this custom shader.",
                  List.of(var11)
               );
            }
         }
      }
   }

   private Set collectProgramPairs(ShaderVersionMap var1, Map var2) {
      LinkedHashSet var3 = new LinkedHashSet();

      for (String var5 : var1.paths().keySet()) {
         if (var5.endsWith(".vsh")) {
            String var6 = var5.substring(0, var5.length() - 4) + ".fsh";
            if (var1.paths().containsKey(var6)) {
               var3.add(new ShaderPair(var5, var6));
            }
         }
      }

      if (var1.version() != Version.UNKNOWN) {
         for (String var13 : var1.paths().keySet()) {
            if (var13.startsWith("assets/minecraft/shaders/core/") && (var13.endsWith(".vsh") || var13.endsWith(".fsh"))) {
               String var15 = var13.substring(0, var13.length() - 4);
               String var7 = var15 + ".vsh";
               String var8 = var15 + ".fsh";
               if (this.readVanillaSource(var1.version(), var7) != null && this.readVanillaSource(var1.version(), var8) != null) {
                  var3.add(new ShaderPair(var7, var8));
               }
            }
         }
      }

      if (var1.version() != Version.UNKNOWN && var1.version().getPackVersion() >= Version.v1_21_3.getPackVersion()) {
         return var3;
      }

      for (Entry var14 : var1.paths().entrySet()) {
         if (ShaderValidator.isCoreShaderProgram((String)var14.getKey())) {
            String var16 = this.readSource((String)var14.getValue());
            if (var16 != null) {
               try {
                  JSONObject var17 = JSON.parseObject(var16);
                  String var18 = ShaderValidator.buildShaderAssetPath((String)var14.getKey(), var17.getString("vertex"), ".vsh");
                  String var9 = ShaderValidator.buildShaderAssetPath((String)var14.getKey(), var17.getString("fragment"), ".fsh");
                  var18 = this.resolveImportPath(var18, var1, var2, ShaderFileKind.VERTEX, (String)var14.getKey());
                  var9 = this.resolveImportPath(var9, var1, var2, ShaderFileKind.FRAGMENT, (String)var14.getKey());
                  if (var18 != null && var9 != null) {
                     var3.add(new ShaderPair(var18, var9));
                  }
               } catch (Throwable var10) {
               }
            }
         }
      }

      return var3;
   }

   private void validateGroup(ShaderVersionMap var1, List var2, Set var3) {
      ArrayList var4 = new ArrayList();
      var4.add(var1.version().name());
      var4.addAll(var2.stream().map(ShaderSource::resourcePath).toList());
      if (var3.add(var4)) {
         List var5 = var2;
         if (var1.version() == Version.v26_3) {
            var5 = new ArrayList(var2.size());

            for (ShaderSource var7 : var2) {
               var5.add(
                  new ShaderSource(
                     var7.stage(),
                     var7.resourcePath(),
                     var7.source().replace("gl_VertexIndex", "gl_VertexID"),
                     var7.lines(),
                     var7.complete(),
                     var7.glslVersion()
                  )
               );
            }
         }

         ShaderValidationResult var9 = this.bEG.validate(var5);
         if (var2.size() == 2 && var9.messages().stream().noneMatch(var0 -> var0.kind().equals("link") && !var0.success())) {
            this.checkUniformBlocks(var1, var2);
         }

         if (!var9.available()) {
            this.addIssue(
               ShaderSeverity.WARNING,
               "SHADER_COMPILER_UNAVAILABLE",
               ShaderFileKind.NONE,
               null,
               null,
               0,
               0,
               "Native GLSL compiler unavailable; static validation completed. " + var9.unavailableReason(),
               List.of()
            );
         } else {
            for (ShaderEntry var8 : var9.messages()) {
               this.reportEntry(var2, var8);
            }
         }
      }
   }

   private void checkUniformBlocks(ShaderVersionMap var1, List var2) {
      ShaderBlockMaps var3 = ShaderValidator.extractBlockMaps((ShaderSource)var2.get(0));
      ShaderBlockMaps var4 = ShaderValidator.extractBlockMaps((ShaderSource)var2.get(1));
      this.reportUniformConflicts(var1, (ShaderSource)var2.get(0), var3.defaults(), (ShaderSource)var2.get(1), var4.blocks());
      this.reportUniformConflicts(var1, (ShaderSource)var2.get(1), var4.defaults(), (ShaderSource)var2.get(0), var3.blocks());
   }

   private void reportUniformConflicts(ShaderVersionMap var1, ShaderSource var2, Map var3, ShaderSource var4, Map var5) {
      for (String var7 : var3.keySet()) {
         if (var5.containsKey(var7)) {
            boolean var8 = var1.paths().containsKey(var2.resourcePath());
            ShaderSource var9 = var8 ? var2 : var4;
            ShaderLocation var10 = var8 ? (ShaderLocation)var3.get(var7) : (ShaderLocation)var5.get(var7);
            ShaderStage var11 = var10.line() > 0 && var10.line() <= var9.lines().size() ? (ShaderStage)var9.lines().get(var10.line() - 1) : null;
            this.addIssue(
               ShaderSeverity.ERROR,
               "SHADER_UNIFORM_BLOCK_LINK_INCOMPATIBLE",
               var9.stage(),
               var9.resourcePath(),
               var11 == null ? var9.resourcePath() : var11.file(),
               var11 == null ? var10.line() : var11.line(),
               var10.column(),
               "Uniform '"
                  + var7
                  + "' is declared in the default uniform block of "
                  + var2.resourcePath()
                  + " and inside a named uniform block of "
                  + var4.resourcePath()
                  + "; the Minecraft client linker rejects this combination.",
               List.of()
            );
         }
      }
   }

   private void reportEntry(List var1, ShaderEntry var2) {
      String var3 = var2.log();
      if (var3 != null && !var3.isBlank()) {
         if (var2.kind().equals("link")) {
            ArrayList var15 = new ArrayList();

            for (String var19 : var3.replace('\r', '\n').split("\n")) {
               String var20 = var19.trim();
               if (!var20.isEmpty()) {
                  ShaderDiagnostic var21 = ShaderValidator.parseDiagnostic(var20);
                  if (var21.severity() != ShaderSeverity.WARNING && !var21.message().isBlank()) {
                     var15.add(var21.message());
                  }
               }
            }

            this.addIssue(
               ShaderSeverity.ERROR,
               "SHADER_LINK_FAILED",
               ShaderFileKind.PROGRAM,
               null,
               null,
               0,
               0,
               var15.isEmpty() ? "glslang rejected the shader without an info log." : String.join("\n", var15),
               List.of()
            );
         } else {
            for (String var7 : var3.replace('\r', '\n').split("\n")) {
               String var8 = var7.trim();
               if (!var8.isEmpty() && !var8.matches("(?i)^(?:ERROR:\\s*)?[0-9]+ compilation errors?.*$")) {
                  ShaderDiagnostic var9 = ShaderValidator.parseDiagnostic(var8);
                  if (var9.severity() != ShaderSeverity.WARNING) {
                     int var10 = var2.stageIndex();
                     ShaderSource var11 = var10 >= 0 && var10 < var1.size() ? (ShaderSource)var1.get(var10) : null;
                     ShaderStage var12 = var11 != null && var9.line() > 0 && var9.line() <= var11.lines().size()
                        ? (ShaderStage)var11.lines().get(var9.line() - 1)
                        : null;
                     ShaderSeverity var13 = var9.severity() != null ? var9.severity() : (var2.success() ? ShaderSeverity.WARNING : ShaderSeverity.ERROR);
                     this.addIssue(
                        var13,
                        var2.success() ? "SHADER_COMPILER_WARNING" : ShaderValidator.mapFailureCode(var2.kind()),
                        var11 == null ? ShaderFileKind.PROGRAM : var11.stage(),
                        var11 == null ? null : var11.resourcePath(),
                        var12 == null ? (var11 == null ? null : var11.resourcePath()) : var12.file(),
                        var12 == null ? var9.line() : var12.line(),
                        var9.column(),
                        var9.message(),
                        var12 == null ? List.of() : var12.includeStack()
                     );
                  }
               }
            }
         }
      } else {
         if (!var2.success()) {
            ShaderFileKind var4 = var2.stageIndex() >= 0 ? ((ShaderSource)var1.get(var2.stageIndex())).stage() : ShaderFileKind.PROGRAM;
            this.addIssue(
               ShaderSeverity.ERROR,
               ShaderValidator.mapFailureCode(var2.kind()),
               var4,
               var2.stageIndex() >= 0 ? ((ShaderSource)var1.get(var2.stageIndex())).resourcePath() : null,
               null,
               0,
               0,
               "glslang rejected the shader without an info log.",
               List.of()
            );
         }
      }
   }

   private ShaderSource resolveSource(ShaderFileKind var1, String var2, ShaderVersionMap var3, Map var4, Deque var5, Set var6, int var7) {
      String var8 = (String)var3.paths().get(var2);
      String var9 = var8 == null ? var2 : var8;
      if (var7 > 32) {
         this.addIssue(ShaderSeverity.ERROR, "SHADER_IMPORT_DEPTH_EXCEEDED", var1, var2, var9, 0, 0, "Shader import depth exceeds 32.", List.copyOf(var5));
         return new ShaderSource(var1, var2, "", List.of(), false, 0);
      }

      if (var5.contains(var2)) {
         ArrayList var26 = new ArrayList(var5);
         var26.add(var2);
         this.addIssue(ShaderSeverity.ERROR, "SHADER_IMPORT_CIRCULAR", var1, var2, var9, 0, 0, "Circular shader include detected.", var26);
         return new ShaderSource(var1, var2, "", List.of(), false, 0);
      }

      String var10 = var8 == null ? this.readVanillaSource(var3.version(), var2) : this.readSource(var8);
      if (var10 == null) {
         return new ShaderSource(var1, var2, "", List.of(), false, 0);
      }

      String var11 = var8 == null ? var3.version().name() + ":" + var2 : var8;
      ShaderProgramSpec var12 = this.bEL.computeIfAbsent(var11, var3x -> ShaderValidator.analyzeSource(var10, var9, var1));
      HashMap var13 = new HashMap();
      var12.imports().forEach(var1x -> var13.put(var1x.line(), var1x));
      String[] var14 = var10.split("\n", -1);
      StringBuilder var15 = new StringBuilder();
      ArrayList var16 = new ArrayList();
      boolean var17 = true;
      int var18 = var12.glslVersion();
      int var19 = -1;
      String var20 = null;
      var5.addLast(var2);

      for (int var21 = 0; var21 < var14.length; var21++) {
         if (var12.versionLines().contains(var21 + 1)) {
            if (var1 == ShaderFileKind.INCLUDE) {
               ShaderValidator.appendStage(var15, var16, "", var9, var21 + 1, var5);
               continue;
            }

            if (var19 < 0) {
               var19 = var15.length();
               var20 = var14[var21];
            }
         }

         ShaderRange var22 = (ShaderRange)var13.get(var21 + 1);
         if (var22 != null && var22.valid()) {
            String var23 = this.resolveIncludePath(var2, var22);
            if (var23 == null) {
               var17 = false;
            } else {
               String var24 = this.resolveImportPath(var23, var3, var4, var1, var9);
               if (var24 == null) {
                  var17 = false;
               } else if (var6.contains(var24) && !var5.contains(var24)) {
                  ShaderValidator.appendStage(var15, var16, "", var9, var21 + 1, var5);
               } else {
                  var6.add(var24);
                  ShaderSource var25 = this.resolveSource(ShaderFileKind.INCLUDE, var24, var3, var4, var5, var6, var7 + 1);
                  var15.append(var25.source());
                  var16.addAll(var25.lines());
                  var17 &= var25.complete();
                  var18 = Math.max(var18, var25.glslVersion());
               }
            }
         } else {
            ShaderValidator.appendStage(var15, var16, var14[var21], var9, var21 + 1, var5);
         }
      }

      var5.removeLast();
      if (var19 >= 0 && var18 > var12.glslVersion()) {
         var15.replace(var19, var19 + var20.length(), ShaderValidator.replaceVersionNumber(var20, var18));
      }

      return new ShaderSource(var1, var2, var15.toString(), List.copyOf(var16), var17, var18);
   }

   private String readVanillaSource(Version var1, String var2) {
      String var3 = var1.name() + ":" + var2;
      if (this.bEK.containsKey(var3)) {
         return (String)this.bEK.get(var3);
      }

      String var4 = this.bEH.read(var1, var2);
      this.bEK.put(var3, var4);
      return var4;
   }

   private String resolveImportPath(String var1, ShaderVersionMap var2, Map var3, ShaderFileKind var4, String var5) {
      if (var1 == null) {
         return null;
      }

      if (var2.paths().containsKey(var1)) {
         return var1;
      }

      String var6 = (String)var3.get(var1.toLowerCase(Locale.ROOT));
      if (var6 != null) {
         this.addIssue(
            ShaderSeverity.ERROR,
            "SHADER_PATH_CASE_MISMATCH",
            var4,
            var5,
            var5,
            0,
            0,
            "Resource path case mismatch: requested '" + var1 + "', actual '" + var6 + "'.",
            List.of(var5)
         );
         return null;
      }

      if ("minecraft".equals(ShaderValidator.extractNamespace(var1)) && this.readVanillaSource(var2.version(), var1) != null) {
         return var1;
      }

      boolean var7 = "minecraft".equals(ShaderValidator.extractNamespace(var1));
      this.addIssue(
         var7 ? ShaderSeverity.INFO : ShaderSeverity.ERROR,
         var7 ? "SHADER_VALIDATION_PARTIAL" : "SHADER_IMPORT_MISSING",
         var4,
         var5,
         var5,
         0,
         0,
         var7
            ? "Compilation skipped because "
               + ShaderValidator.toShaderId(var1)
               + " is not available in the vanilla shader cache for "
               + var2.version().getName()
               + "."
            : "Missing shader import or stage: " + var1,
         List.of(var5, var1)
      );
      return null;
   }

   private String resolveIncludePath(String var1, ShaderRange var2) {
      String var3 = var2.target();
      if (ShaderValidator.containsParentTraversal(var3) || var3.startsWith("/") || var3.contains("\\")) {
         this.addIssue(
            ShaderSeverity.ERROR,
            "SHADER_IMPORT_PATH_TRAVERSAL",
            ShaderValidator.classifyShaderFile(var1),
            var1,
            var1,
            var2.line(),
            var2.column(),
            "Import path traversal is not allowed.",
            List.of(var1)
         );
         return null;
      }

      if (var2.relative()) {
         int var7 = var1.lastIndexOf(47);
         return ShaderValidator.normalizePath(var1.substring(0, var7 + 1) + var3);
      }

      String var4 = "minecraft";
      String var5 = var3;
      int var6 = var3.indexOf(58);
      if (var6 >= 0) {
         var4 = var3.substring(0, var6);
         var5 = var3.substring(var6 + 1);
      }

      if (!ShaderValidator.isValidShaderId(var4 + ":" + var5, false)) {
         this.addIssue(
            ShaderSeverity.ERROR,
            "SHADER_RESOURCE_LOCATION_INVALID",
            ShaderValidator.classifyShaderFile(var1),
            var1,
            var1,
            var2.line(),
            var2.column(),
            "Malformed shader include resource location.",
            List.of(var1)
         );
         return null;
      } else {
         return "assets/" + var4 + "/shaders/include/" + var5;
      }
   }

   private String readSource(String var1) {
      if (var1 == null) {
         return null;
      }

      if (this.bEK.containsKey(var1)) {
         return (String)this.bEK.get(var1);
      }

      PackEntry var2 = (PackEntry)this.bEF.get(var1);
      if (var2 == null) {
         return null;
      }

      try {
         String var3 = ShaderValidator.readPackEntryText(var2);
         this.bEK.put(var1, var3);
         return var3;
      } catch (Throwable var4) {
         this.addIssue(
            ShaderSeverity.ERROR,
            "SHADER_FILE_UNREADABLE",
            ShaderValidator.classifyShaderFile(var1),
            var1,
            var1,
            0,
            0,
            ShaderValidator.rootCauseMessage(var4),
            List.of(var1)
         );
         return null;
      }
   }

   private void addIssue(ShaderSeverity var1, String var2, ShaderFileKind var3, String var4, String var5, int var6, int var7, String var8, List var9) {
      this.bEJ.add(new ShaderIssue(var1, var2, var3, var4, this.displayPath(var5), var6, var7, var8, var9));
   }

   private void addRemappedIssue(ShaderIssue var1) {
      this.bEJ
         .add(
            new ShaderIssue(
               var1.severity(),
               var1.code(),
               var1.stage(),
               var1.resourcePath(),
               this.displayPath(var1.originalFile()),
               var1.line(),
               var1.column(),
               var1.message(),
               var1.includeStack()
            )
         );
   }

   private String displayPath(String var1) {
      if (var1 == null) {
         return null;
      }

      PackEntry var2 = (PackEntry)this.bEF.get(var1);
      String var3 = var2 == null ? null : (String)this.bEI.apply(var2, var1);
      return var3 == null ? var1 : var3;
   }
}
