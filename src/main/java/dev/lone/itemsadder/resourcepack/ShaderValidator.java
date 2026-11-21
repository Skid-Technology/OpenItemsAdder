package dev.lone.itemsadder.resourcepack;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent.PackEntry;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

final class ShaderValidator {
   private static final int bEr = 1048576;
   private static final int bEs = 32;
   private static final String bEt = "3.4.1";
   private static final String bEu = "dev.lone.itemsadder.shaderbridge.NativeGlslangBridge";
   private static final String bEv = "itemsadder-shader-validator";
   private static final AtomicBoolean bEw = new AtomicBoolean();
   private static final AtomicBoolean bEx = new AtomicBoolean();

   private ShaderValidator() {
   }

   static ShaderReport validateShaders(Map var0) {
      return validateWithReader(var0, NativeShaderValidator.INSTANCE, ShaderValidator::resolveIncludeSource);
   }

   static ShaderReport validateWithBackend(Map var0, ShaderValidatorBackend var1) {
      return validateWithReader(var0, var1, (var0x, var1x) -> null);
   }

   static ShaderReport validateWithReader(Map var0, ShaderValidatorBackend var1, ShaderSourceReader var2) {
      return analyzeShaders(var0, var1, var2, (var0x, var1x) -> null);
   }

   static ShaderReport analyzeShaders(Map var0, ShaderValidatorBackend var1, ShaderSourceReader var2, BiFunction var3) {
      ShaderProgramAnalyzer var4 = new ShaderProgramAnalyzer(var0, var1, var2, var3);
      var4.analyzeAll();
      return new ShaderReport(List.copyOf(var4.bEJ));
   }

   private static String resolveIncludeSource(Version var0, String var1) {
      if (var0 != null && var0 != Version.UNKNOWN) {
         try {
            ContentManager var2 = ContentManager.method8084();
            return var2 != null && var2.field91 != null ? var2.field91.readText(var1, var0) : null;
         } catch (Throwable var3) {
            return null;
         }
      } else {
         return null;
      }
   }

   static void validateAndLog(Map var0) {
      validateAndLogWith(var0, (var0x, var1) -> null);
   }

   static void validateAndLogWith(Map var0, BiFunction var1) {
      try {
         ShaderReport var2 = analyzeShaders(var0, NativeShaderValidator.INSTANCE, ShaderValidator::resolveIncludeSource, var1);

         for (ShaderIssue var4 : var2.diagnostics()) {
            if (!var4.code().equals("SHADER_COMPILER_UNAVAILABLE") || bEw.compareAndSet(false, true)) {
               switch (var4.severity()) {
                  case ERROR:
                     Log.error(var4.formatReport());
                     break;
                  case WARNING:
                     Log.warn(var4.formatReport());
                     break;
                  case INFO:
                     Log.log(var4.formatReport());
               }

               if (var4.severity() == ShaderSeverity.ERROR) {
                  PackWarningSummary.addWarning("Shader validation", var4.formatReport());
               }
            }
         }
      } catch (Throwable var5) {
         if (bEx.compareAndSet(false, true)) {
            Log.warn("[WARNING] SHADER_VALIDATOR_FAILED\nShader validation was skipped: " + rootCauseMessage(var5));
         }
      }
   }

   private static ShaderProgramSpec analyzeSource(String var0, String var1, ShaderFileKind var2) {
      if (var0 == null) {
         return new ShaderProgramSpec(List.of(), List.of(), false, 0, 0, Set.of(), List.of());
      }

      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      ArrayDeque var6 = new ArrayDeque();
      boolean var7 = false;
      boolean var8 = false;
      char var9 = 0;
      boolean var10 = false;
      boolean var11 = true;
      boolean var12 = false;
      int var13 = 0;
      int var14 = 0;
      HashSet var15 = new HashSet();
      int var16 = 1;
      int var17 = 1;
      int var18 = 0;
      int var19 = 0;
      int var20 = 0;

      while (var20 < var0.length()) {
         char var21 = var0.charAt(var20);
         char var22 = var20 + 1 < var0.length() ? var0.charAt(var20 + 1) : '\u0000';
         if (var21 == '\n') {
            if (var9 != 0) {
               var3.add(errorIssue("SHADER_STRING_UNTERMINATED", var2, var1, var19, 1, "Unterminated string literal."));
               var9 = 0;
               var10 = false;
            }

            var7 = false;
            var11 = true;
            var16++;
            var17 = 1;
            var20++;
         } else if (var7) {
            var20++;
            var17++;
         } else if (var8) {
            if (var21 == '*' && var22 == '/') {
               var8 = false;
               var20 += 2;
               var17 += 2;
            } else {
               var20++;
               var17++;
            }
         } else if (var9 != 0) {
            if (var10) {
               var10 = false;
            } else if (var21 == '\\') {
               var10 = true;
            } else if (var21 == var9) {
               var9 = 0;
            }

            var20++;
            var17++;
         } else if (var21 == '/' && var22 == '/') {
            var7 = true;
            var20 += 2;
            var17 += 2;
         } else if (var21 == '/' && var22 == '*') {
            var8 = true;
            var18 = var16;
            var20 += 2;
            var17 += 2;
         } else if (var21 == '"' || var21 == '\'') {
            var9 = var21;
            var19 = var16;
            var12 = true;
            var11 = false;
            var20++;
            var17++;
         } else if (var21 == '#' && var11) {
            int var34 = var0.indexOf(10, var20);
            if (var34 < 0) {
               var34 = var0.length();
            }

            String var35 = var0.substring(var20 + 1, var34);
            int var25 = skipWhitespace(var35, 0);
            int var26 = var25;

            while (var26 < var35.length() && isIdentifierPart(var35.charAt(var26))) {
               var26++;
            }

            String var27 = var35.substring(var25, var26);
            if (var27.equals("version")) {
               var13++;
               var15.add(var16);
               var14 = Math.max(var14, parseVersionNumber(var35, var26));
               if (var13 > 1) {
                  var3.add(errorIssue("SHADER_VERSION_DUPLICATE", var2, var1, var16, var17, "Shader declares #version more than once."));
               }

               if (var12) {
                  var3.add(errorIssue("SHADER_VERSION_MISPLACED", var2, var1, var16, var17, "#version must precede all shader code and directives."));
               }
            } else {
               var12 = true;
               if (var27.equals("moj_import") || var27.equals("include")) {
                  ShaderRange var28 = parseIncludeDirective(var35.substring(var26), var16, var17);
                  var4.add(var28);
                  if (!var28.valid()) {
                     var3.add(errorIssue("SHADER_IMPORT_MALFORMED", var2, var1, var16, var17, "Malformed shader include directive."));
                  }
               }
            }

            var11 = false;
            var20++;
            var17++;
         } else if (Character.isWhitespace(var21)) {
            var20++;
            var17++;
         } else {
            var11 = false;
            var12 = true;
            if (isIdentifierStart(var21)) {
               int var33 = var20;
               int var24 = var17;

               while (var20 < var0.length() && isIdentifierPart(var0.charAt(var20))) {
                  var20++;
                  var17++;
               }

               var5.add(new ShaderLocation(var0.substring(var33, var20), var16, var24, true));
            } else {
               if (var21 == '(' || var21 == ')' || var21 == '[' || var21 == ']' || var21 == '{' || var21 == '}' || var21 == ';' || var21 == ',') {
                  var5.add(new ShaderLocation(String.valueOf(var21), var16, var17, false));
               }

               if (var21 == '(' || var21 == '[' || var21 == '{') {
                  var6.addLast(new ShaderToken(var21, var16, var17));
               } else if (var21 == ')' || var21 == ']' || var21 == '}') {
                  int var23 = var21 == ')' ? 40 : (var21 == ']' ? 91 : 123);
                  if (!var6.isEmpty() && ((ShaderToken)var6.getLast()).value() == var23) {
                     var6.removeLast();
                  } else {
                     var3.add(errorIssue("SHADER_DELIMITER_UNMATCHED", var2, var1, var16, var17, "Unmatched '" + var21 + "'."));
                  }
               }

               var20++;
               var17++;
            }
         }
      }

      if (var8) {
         var3.add(errorIssue("SHADER_COMMENT_UNTERMINATED", var2, var1, var18, 1, "Unterminated block comment."));
      }

      if (var9 != 0) {
         var3.add(errorIssue("SHADER_STRING_UNTERMINATED", var2, var1, var19, 1, "Unterminated string literal."));
      }

      for (ShaderToken var31 : var6) {
         var3.add(errorIssue("SHADER_DELIMITER_UNMATCHED", var2, var1, var31.line(), var31.column(), "Unmatched '" + var31.value() + "'."));
      }

      boolean var30 = false;

      for (int var32 = 0; var32 + 2 < var5.size(); var32++) {
         if (((ShaderLocation)var5.get(var32)).value().equals("void")
            && ((ShaderLocation)var5.get(var32 + 1)).value().equals("main")
            && ((ShaderLocation)var5.get(var32 + 2)).value().equals("(")) {
            var30 = true;
            break;
         }
      }

      return new ShaderProgramSpec(List.copyOf(var3), List.copyOf(var4), var30, var13, var14, Set.copyOf(var15), List.copyOf(var5));
   }

   private static ShaderBlockMaps extractBlockMaps(ShaderSource var0) {
      List var1 = analyzeSource(var0.source(), var0.resourcePath(), var0.stage()).tokens();
      LinkedHashMap var2 = new LinkedHashMap();
      LinkedHashMap var3 = new LinkedHashMap();

      for (int var4 = 0; var4 < var1.size(); var4++) {
         if (((ShaderLocation)var1.get(var4)).value().equals("uniform")) {
            int var5 = var4 + 1;

            while (var5 < var1.size() && !((ShaderLocation)var1.get(var5)).value().equals("{") && !((ShaderLocation)var1.get(var5)).value().equals(";")) {
               var5++;
            }

            if (var5 >= var1.size()) {
               break;
            }

            if (((ShaderLocation)var1.get(var5)).value().equals(";")) {
               collectUniformNames(var1, var4 + 1, var5, var2);
               var4 = var5;
            } else {
               int var6 = 1;

               int var7;
               for (var7 = var5 + 1; var7 < var1.size() && var6 > 0; var7++) {
                  if (((ShaderLocation)var1.get(var7)).value().equals("{")) {
                     var6++;
                  } else if (((ShaderLocation)var1.get(var7)).value().equals("}")) {
                     var6--;
                  }
               }

               collectUniformNames(var1, var5 + 1, Math.max(var5 + 1, var7 - 1), var3);
               var4 = var7 - 1;
            }
         }
      }

      return new ShaderBlockMaps(Map.copyOf(var2), Map.copyOf(var3));
   }

   private static void collectUniformNames(List var0, int var1, int var2, Map var3) {
      for (int var4 = var1 + 1; var4 <= var2 && var4 < var0.size(); var4++) {
         String var5 = ((ShaderLocation)var0.get(var4)).value();
         ShaderLocation var6 = (ShaderLocation)var0.get(var4 - 1);
         if (var6.identifier() && (var5.equals("[") || var5.equals(",") || var5.equals(";"))) {
            var3.putIfAbsent(var6.value(), var6);
         }
      }
   }

   private static ShaderRange parseIncludeDirective(String var0, int var1, int var2) {
      int var3 = skipWhitespace(var0, 0);
      if (var3 >= var0.length()) {
         return new ShaderRange(var1, var2, "", false, false);
      }

      char var4 = var0.charAt(var3);
      int var5 = var4 == '<' ? 62 : (var4 == '"' ? 34 : 0);
      if (var5 == 0) {
         return new ShaderRange(var1, var2, "", false, false);
      }

      int var6 = var0.indexOf(var5, var3 + 1);
      if (var6 < 0) {
         return new ShaderRange(var1, var2, "", var4 == '"', false);
      }

      String var7 = var0.substring(var3 + 1, var6).trim();
      String var8 = var0.substring(var6 + 1).trim();
      boolean var9 = !var7.isEmpty() && (var8.isEmpty() || var8.startsWith("//"));
      return new ShaderRange(var1, var2, var7, var4 == '"', var9);
   }

   private static ShaderIssue errorIssue(String var0, ShaderFileKind var1, String var2, int var3, int var4, String var5) {
      return new ShaderIssue(ShaderSeverity.ERROR, var0, var1, var2, var2, var3, var4, var5, List.of(var2));
   }

   private static ShaderDiagnostic parseDiagnostic(String var0) {
      ShaderSeverity var1 = null;
      String var2 = var0;
      if (var0.regionMatches(true, 0, "ERROR:", 0, 6)) {
         var1 = ShaderSeverity.ERROR;
         var2 = var0.substring(6).trim();
      } else if (var0.regionMatches(true, 0, "WARNING:", 0, 8)) {
         var1 = ShaderSeverity.WARNING;
         var2 = var0.substring(8).trim();
      }

      int var3 = 0;
      int var4 = 0;
      int var5 = var2.indexOf(58);
      if (var5 >= 0) {
         int var6 = var2.indexOf(58, var5 + 1);
         if (var6 >= 0) {
            int var7 = parseIntOrZero(var2.substring(var5 + 1, var6).trim());
            if (var7 > 0) {
               var3 = var7;
               int var8 = var2.indexOf(58, var6 + 1);
               if (var8 >= 0) {
                  int var9 = parseIntOrZero(var2.substring(var6 + 1, var8).trim());
                  if (var9 > 0) {
                     var4 = var9;
                     var2 = var2.substring(var8 + 1).trim();
                  } else {
                     var2 = var2.substring(var6 + 1).trim();
                  }
               } else {
                  var2 = var2.substring(var6 + 1).trim();
               }
            }
         }
      }

      return new ShaderDiagnostic(var1, var3, var4, var2);
   }

   private static int parseIntOrZero(String var0) {
      try {
         return Integer.parseInt(var0);
      } catch (NumberFormatException var2) {
         return 0;
      }
   }

   private static String mapFailureCode(String var0) {
      return switch (var0) {
         case "preprocess" -> "SHADER_PREPROCESS_FAILED";
         case "link" -> "SHADER_LINK_FAILED";
         default -> "SHADER_COMPILATION_FAILED";
      };
   }

   private static String buildShaderAssetPath(String var0, String var1, String var2) {
      if (var1 != null && !var1.isBlank()) {
         String var3 = extractNamespace(var0);
         String var4 = var1;
         int var5 = var1.indexOf(58);
         if (var5 >= 0) {
            var3 = var1.substring(0, var5);
            var4 = var1.substring(var5 + 1);
         }

         if (!var4.startsWith("core/")) {
            var4 = "core/" + var4;
         }

         if (!var4.endsWith(var2)) {
            var4 = var4 + var2;
         }

         return "assets/" + var3 + "/shaders/" + var4;
      } else {
         return null;
      }
   }

   private static boolean isValidShaderId(String var0, boolean var1) {
      if (var0 != null && !var0.isBlank() && !var0.contains("\\") && !containsParentTraversal(var0)) {
         int var2 = var0.indexOf(58);
         return var2 < 0
            ? var1 && isValidIdSegment(var0, true)
            : var2 > 0 && var2 == var0.lastIndexOf(58) && isValidIdSegment(var0.substring(0, var2), false) && isValidIdSegment(var0.substring(var2 + 1), true);
      } else {
         return false;
      }
   }

   private static boolean isValidIdSegment(String var0, boolean var1) {
      if (!var0.isEmpty() && !var0.startsWith("/") && !var0.endsWith("/")) {
         for (int var2 = 0; var2 < var0.length(); var2++) {
            char var3 = var0.charAt(var2);
            if ((var3 < 'a' || var3 > 'z') && (var3 < '0' || var3 > '9') && var3 != '_' && var3 != '-' && var3 != '.' && (!var1 || var3 != '/')) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static String normalizePath(String var0) {
      ArrayDeque var1 = new ArrayDeque();

      for (String var5 : var0.split("/")) {
         if (!var5.isEmpty() && !var5.equals(".")) {
            if (var5.equals("..")) {
               if (var1.isEmpty()) {
                  return null;
               }

               var1.removeLast();
            } else {
               var1.addLast(var5);
            }
         }
      }

      return String.join("/", var1);
   }

   private static boolean containsParentTraversal(String var0) {
      return Arrays.stream(var0.replace('\\', '/').split("/", -1)).anyMatch(".."::equals);
   }

   private static void appendStage(StringBuilder var0, List var1, String var2, String var3, int var4, Deque var5) {
      var0.append(var2).append('\n');
      var1.add(new ShaderStage(var3, var4, List.copyOf(var5)));
   }

   static String readPackEntryText(PackEntry var0) {
      try (InputStream var1 = var0.openStream()) {
         byte[] var2 = var1.readNBytes(1048577);
         if (var2.length > 1048576) {
            throw new IOException("shader file exceeds 1048576 bytes");
         } else {
            return new String(var2, StandardCharsets.UTF_8);
         }
      }
   }

   static boolean isShaderAssetPath(String var0) {
      int var1 = var0.indexOf("assets/");
      if (var1 < 0) {
         return false;
      }

      String var2 = var0.substring(var1);
      int var3 = var2.indexOf("/shaders/");
      return var2.startsWith("assets/") && var3 >= 0
         ? var2.endsWith(".vsh") || var2.endsWith(".fsh") || var2.endsWith(".glsl") || var2.endsWith(".json") || var2.contains("/shaders/include/")
         : false;
   }

   private static boolean isCoreShaderProgram(String var0) {
      return var0.endsWith(".json") && var0.contains("/shaders/core/");
   }

   private static boolean isPostShaderProgram(String var0) {
      return var0.endsWith(".json") && var0.contains("/shaders/post/");
   }

   private static ShaderFileKind classifyShaderFile(String var0) {
      if (var0 == null) {
         return ShaderFileKind.NONE;
      } else if (var0.endsWith(".vsh")) {
         return ShaderFileKind.VERTEX;
      } else if (var0.endsWith(".fsh")) {
         return ShaderFileKind.FRAGMENT;
      } else if (var0.endsWith(".glsl") || var0.contains("/shaders/include/")) {
         return ShaderFileKind.INCLUDE;
      } else {
         return var0.endsWith(".json") ? ShaderFileKind.PROGRAM : ShaderFileKind.NONE;
      }
   }

   private static String extractNamespace(String var0) {
      int var1 = var0.indexOf("assets/");
      if (var1 < 0) {
         return "minecraft";
      }

      int var2 = var1 + "assets/".length();
      int var3 = var0.indexOf(47, var2);
      return var3 < 0 ? "minecraft" : var0.substring(var2, var3);
   }

   private static String toShaderId(String var0) {
      String var1 = extractNamespace(var0);
      int var2 = var0.indexOf("/shaders/");
      return var2 < 0 ? var0 : var1 + ":" + var0.substring(var2 + "/shaders/".length());
   }

   private static int skipWhitespace(String var0, int var1) {
      while (var1 < var0.length() && Character.isWhitespace(var0.charAt(var1))) {
         var1++;
      }

      return var1;
   }

   private static int parseVersionNumber(String var0, int var1) {
      int var2 = skipWhitespace(var0, var1);
      int var3 = var2;

      while (var3 < var0.length() && Character.isDigit(var0.charAt(var3))) {
         var3++;
      }

      return var2 == var3 ? 0 : parseIntOrZero(var0.substring(var2, var3));
   }

   private static String replaceVersionNumber(String var0, int var1) {
      int var2 = var0.indexOf(35);
      int var3 = skipWhitespace(var0, var2 + 1) + "version".length();
      var3 = skipWhitespace(var0, var3);
      int var4 = var3;

      while (var4 < var0.length() && Character.isDigit(var0.charAt(var4))) {
         var4++;
      }

      return var3 == var4 ? var0 : var0.substring(0, var3) + var1 + var0.substring(var4);
   }

   private static boolean isIdentifierStart(char var0) {
      return var0 == '_' || Character.isLetter(var0);
   }

   private static boolean isIdentifierPart(char var0) {
      return var0 == '_' || Character.isLetterOrDigit(var0);
   }

   static String rootCauseMessage(Throwable var0) {
      Throwable var1 = var0;

      while (var1.getCause() != null && var1.getCause() != var1) {
         var1 = var1.getCause();
      }

      String var2 = var1.getMessage();
      return var2 != null && !var2.isBlank() ? var2 : var1.getClass().getSimpleName();
   }
}
