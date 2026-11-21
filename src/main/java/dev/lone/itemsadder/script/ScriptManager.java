package dev.lone.itemsadder.script;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.CustomEntity;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.scriptinginternal.EntityScript;
import dev.lone.itemsadder.api.scriptinginternal.EntityUtils;
import dev.lone.itemsadder.api.scriptinginternal.ItemScript;
import dev.lone.itemsadder.api.scriptinginternal.ItemsUtils;
import dev.lone.itemsadder.api.scriptinginternal.PlayerUtils;
import dev.lone.itemsadder.api.scriptinginternal.ScriptingUtils;
import dev.lone.itemsadder.api.scriptinginternal.WorldUtils;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.NullableBox;
import dev.lone.itemsadder.util.RegexReplacement;
import ia.sh.customblockdata.CustomBlockData;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class ScriptManager {
   private static final String aCO = "iascript_generated";
   private static final String aCP = "Plugin $plugin, Event $event, Player $player, CustomStack $customStack, ItemStack $itemStack";
   private static final String aCQ = "Plugin $plugin, Event $event, Player $player, CustomEntity $customEntity";
   private static final Set aCR = Set.of(
      ScriptingUtils.class.getName(), PlayerUtils.class.getName(), WorldUtils.class.getName(), ItemsUtils.class.getName(), EntityUtils.class.getName()
   );
   private static final Set aCS = Set.of(
      "itemsadder.m",
      "java.lang.reflect",
      "java.lang.invoke",
      "java.io",
      "java.nio.file",
      "java.net",
      "java.util.concurrent",
      "java.util.function",
      "java.lang.management",
      "java.security",
      "javax.security",
      "javax.script",
      "javax.tools",
      "java.sql",
      "javax.sql",
      "java.rmi",
      "javax.naming",
      "sun.reflect",
      "sun.misc",
      "sun.security",
      "jdk.internal",
      "jdk.nashorn",
      "jdk.jshell",
      "jdk.jfr",
      "jdk.vm",
      "com.sun.tools",
      "com.sun.jdi",
      "com.sun.management",
      "com.sun.net",
      "java.awt",
      "javax.swing",
      "java.applet",
      "org.apache.bcel",
      "org.objectweb",
      "net.bytebuddy",
      "javassist",
      "org.reflections",
      "org.codehaus.groovy",
      "org.mozilla.javascript",
      "org.python",
      "clojure",
      "bsh",
      "groovy",
      "scala.tools"
   );
   List aCT = List.of(
      "org.bukkit.Bukkit",
      "org.bukkit.event.Event",
      "org.bukkit.plugin.Plugin",
      "org.bukkit.entity.Player",
      "org.bukkit.Location",
      "org.bukkit.inventory.ItemStack",
      "dev.lone.itemsadder.api.CustomStack",
      "dev.lone.itemsadder.api.CustomEntity"
   );
   List aCU = List.of(
      "dev.lone.itemsadder.api.FontImages.FontImageWrapper",
      "dev.lone.itemsadder.api.FontImages.PlayerCustomHudWrapper",
      "dev.lone.itemsadder.api.FontImages.PlayerHudsHolderWrapper",
      "dev.lone.itemsadder.api.FontImages.PlayerHudWrapper",
      "dev.lone.itemsadder.api.FontImages.PlayerQuantityHudWrapper",
      "dev.lone.itemsadder.api.FontImages.TexturedInventoryWrapper",
      "dev.lone.itemsadder.api.CustomBlock",
      "dev.lone.itemsadder.api.CustomCrop",
      "dev.lone.itemsadder.api.CustomEntity",
      "dev.lone.itemsadder.api.CustomFire",
      "dev.lone.itemsadder.api.CustomFurniture",
      "dev.lone.itemsadder.api.CustomPlayer",
      "dev.lone.itemsadder.api.CustomStack",
      "dev.lone.itemsadder.api.ItemsAdder",
      "java.util.ArrayList",
      "java.util.HashMap",
      "java.util.List",
      "java.util.Map"
   );
   private static final List aCV = List.of(
      new RegexReplacement(
         Pattern.compile("^cancelEvent\\(\\);?.*$"),
         var0 -> "try {\n    if ($event instanceof org.bukkit.event.Cancellable cancellableEvent)\n        cancellableEvent.setCancelled(true);\n} catch (Exception ignored) {}\n"
      ),
      new RegexReplacement(Pattern.compile("^let\\s+(\\w+)\\s*=\\s*(.*);?$"), var0 -> {
         String var1 = "var " + var0.group(1) + " = " + var0.group(2);
         return !var1.endsWith("}") && !var1.endsWith("{") ? var1 + ";" : var1;
      }),
      new RegexReplacement(Pattern.compile("^const\\s+(\\w+)\\s*=\\s*(.*);?$"), var0 -> {
         String var1 = "final var " + var0.group(1) + " = " + var0.group(2);
         return !var1.endsWith("}") && !var1.endsWith("{") ? var1 + ";" : var1;
      }),
      new RegexReplacement(Pattern.compile("^undefined\\s*;?$"), var0 -> "null")
   );
   private final YamlConfig aCW;
   private boolean aCX = false;

   public ScriptManager() {
      this.aCW = new YamlConfig(Main.getInstance(), true, "storage/cache/compiled_scripts/cached_files.yml", false, false);
   }

   public void Kz() {
      if (!this.aCX) {
         CustomBlockData.registerListener(Main.getInstance());
         this.aCX = true;
      }
   }

   private Optional dX(String var1) {
      Matcher var2 = Pattern.compile("^\\s*package\\s+([a-zA-Z_][a-zA-Z0-9_.]*);", 8).matcher(var1);
      return var2.find() ? Optional.of(var2.group(1)) : Optional.empty();
   }

   public ScriptCompilation method2290(File var1, String var2, String var3, FileTime var4) {
      ScriptCompilation var5 = new ScriptCompilation();
      CompiledScript var6 = new CompiledScript();
      var5.iY = var6;
      ClassLoader var7 = Main.class.getClassLoader();
      String var8 = this.dX(var2).orElse("iascript_generated") + "." + var3;
      Path var9 = new File(Main.getInstance().getDataFolder(), "storage/cache/compiled_scripts").toPath();

      try {
         Files.createDirectories(var9);
      } catch (IOException var56) {
         var5.aCY.add("Error creating cache directory: " + var56.getMessage());
         return var5;
      }

      String var10 = var8.replace(".", "/");
      Path var11 = var9.resolve(var10 + ".class");

      try {
         ConfigSectionWrapper var12 = this.aCW.kc(var10);
         if (var12 != null
            && Files.exists(var11)
            && Files.getLastModifiedTime(var11).toMillis() == var4.toMillis()
            && var12.method7081("last_modified", 0L) == var4.toMillis()
            && var12.bf("itemsadder_version", "").equals(Main.getInstance().getVersion())) {
            HashMap var69 = new HashMap();
            var69.put(var8, Files.readAllBytes(var11));
            Path var70 = var11.getParent();
            String var71 = var11.getFileName().toString().replace(".class", "");

            try (DirectoryStream var73 = Files.newDirectoryStream(var70, var71 + "$*.class")) {
               for (Path var79 : var73) {
                  String var81 = this.dX(var2).orElse("iascript_generated") + "." + var79.getFileName().toString().replace(".class", "");
                  var69.put(var81, Files.readAllBytes(var79));
               }
            } catch (IOException var63) {
               var5.aCY.add("Error reading cached sub-classes: " + var63.getMessage());
               var5.aCZ.add(var63);
            }

            MemoryClassLoader var74 = new MemoryClassLoader(var69, var7);
            Class var77 = var74.loadClass(var8);
            var6.aDd = var77;
            var6.aDc = var2;
            method2300("Loaded cached class: " + var77.getName() + " from " + var11);
            return var5;
         }
      } catch (ClassNotFoundException var64) {
         var5.aCY.add("Cached class not found: " + var64.getMessage());
         var5.aCZ.add(var64);
      } catch (IOException var65) {
         var5.aCY.add("Error reading cached class: " + var65.getMessage());
      } catch (Throwable var66) {
         var5.aCY.add("Unexpected error while checking cached class. Recompiling it. Error: " + var66.getMessage());
         var5.aCZ.add(var66);

         try {
            if (Files.exists(var11)) {
               Files.delete(var11);
               method2300("Deleted cached file: " + var11);
            }
         } catch (IOException var54) {
            var5.aCY.add("Error deleting cached file: " + var54.getMessage());
            var5.aCZ.add(var54);
         }
      }

      if (Settings.get().logScriptCompilation) {
         this.log(" ");
         this.log("Compiling script: " + var3);
      }

      HashSet var68 = new HashSet(this.method2298(Bukkit.class));
      var2 = this.eb(var2);

      String var13;
      try {
         var13 = this.method2295(var2, var3);
      } catch (IllegalArgumentException var53) {
         var5.aCY.add("Preprocessing error: " + var53.getMessage());
         return var5;
      }

      String[] var14 = var13.split("\n");
      String[] var15 = var2.split("\n");

      for (String var19 : var15) {
         String var20 = var19.trim();
         if (!var20.isEmpty() && (var20.startsWith("import \"") && var20.endsWith("\"") || var20.startsWith("import \"") && var20.endsWith("\";"))) {
            String var21 = var20.substring(8, var20.length() - (var20.endsWith("\";") ? 2 : 1));
            method2300("Found explicit import: " + var21);

            try {
               var68.addAll(this.method2299(var21));
            } catch (ClassNotFoundException var52) {
               var5.aCY.add("Class not found: " + var21);
            }
         }
      }

      try {
         JavaCompiler var72 = ToolProvider.getSystemJavaCompiler();
         if (Settings.get().logScriptCompilation) {
            Log.log("[Scripts] JavaCompiler implementation: " + var72.getClass().getName());
         }

         DiagnosticCollector var75 = new DiagnosticCollector();
         StandardJavaFileManager var78 = var72.getStandardFileManager(var75, null, null);
         MemoryJavaFileManager var80 = new MemoryJavaFileManager(var78);
         StringJavaSource var82 = new StringJavaSource(var8, var13);
         if (Settings.get().logScriptCompilation) {
            Log.log("[Scripts] MemoryFileManager class: " + var80.getClass().getName());
            Log.log("[Scripts] Creating JavaFileObject for: " + var8);
            Log.log("[Scripts] Class name in Java source: " + dY(var13));
            Log.log("[Scripts] JavaFileObject className: " + var8);
            Log.log("[Scripts] Submitting JavaFileObject for: " + var82.getName());
         }

         var68.add(System.getProperty("java.class.path"));
         List var83 = new ArrayList();
         int var22 = 2;

         for (byte var23 = 0; var23 < var22; var22++) {
            DiagnosticCollector var24 = new DiagnosticCollector();
            List var25 = List.of(
               "-g",
               "-sourcepath",
               var1.getParentFile().getAbsolutePath(),
               "-classpath",
               String.join(File.pathSeparator, var68),
               "-proc:none",
               "-Xlint:-options"
            );
            CompilationTask var26 = var72.getTask(
               Settings.get().logScriptCompilation ? new PrintWriter(System.out) : new PrintWriter(Writer.nullWriter()),
               var80,
               var24,
               var25,
               null,
               List.of(var82)
            );
            boolean var27 = var26.call();
            if (var27) {
               break;
            }

            List var28 = List.copyOf(var24.getDiagnostics());
            List var29 = var28.stream().map(var0 -> var0.getMessage(Locale.ENGLISH)).collect(Collectors.toList());
            if (var29.equals(var83)) {
               break;
            }

            var83 = var29;
            Set var30 = this.method2297(var28);
            var68.addAll(var30);
         }

         List var84 = List.of(
            "-g", "-sourcepath", var1.getParentFile().getAbsolutePath(), "-classpath", String.join(File.pathSeparator, var68), "-proc:none", "-Xlint:-options"
         );

         try {
            List var85 = new JavaSourceCompiler(var72, var80, var84, var82, var2, aCS).KM();
            if (!var85.isEmpty()) {
               var5.aCY.addAll(var85);
               var5.aDb = true;
               return var5;
            }
         } catch (Exception var60) {
            throw new RuntimeException(var60);
         }

         CompilationTask var86 = var72.getTask(
            Settings.get().logScriptCompilation ? new PrintWriter(System.out) : new PrintWriter(Writer.nullWriter()), var80, var75, var84, null, List.of(var82)
         );
         if (Settings.get().logScriptCompilation) {
            Log.log("[Scripts] Compiling JavaFileObject: " + var82.getName());
         }

         Boolean var87 = var86.call();
         if (var87 != null && var87) {
            if (Settings.get().logScriptCompilation) {
               Log.log("[Scripts] Task call success: " + var87);
               Log.log("[Scripts] Compiled class keys: " + var80.Kx().keySet());
            }

            try {
               var6.aDc = var13;
               Map var89 = var80.Kx();
               if (Settings.get().logScriptCompilation) {
                  Log.log("[Scripts] Compiled class keys: " + var89.keySet());
               }

               MemoryClassLoader var91 = new MemoryClassLoader(var89, var7);

               try {
                  var6.aDd = var91.loadClass(var8);
                  if (Settings.get().logScriptCompilation) {
                     Log.log("[Scripts] Loaded class: " + var6.aDd.getName());
                  }
               } catch (Throwable var58) {
                  var5.aCY.add("Failed to load class: " + var8 + " - " + var58.getClass().getName() + ": " + var58.getMessage());
                  var5.aCZ.add(var58);
                  return var5;
               }

               try {
                  for (Entry var94 : var89.entrySet()) {
                     String var31 = ((String)var94.getKey()).replace(".", "/");
                     byte[] var32 = (byte[])var94.getValue();
                     Path var33 = var9.resolve(var31 + ".class");
                     Files.createDirectories(var33.getParent());
                     Files.write(var33, var32);
                     Files.setLastModifiedTime(var33, var4);
                     ConfigSectionWrapper var34 = this.aCW.kd(var31);
                     var34.method7074("file_path", var33.toString());
                     var34.method7074("last_modified", var4.toMillis());
                     var34.method7074("class_name", var8);
                     var34.method7074("itemsadder_version", Main.getInstance().getVersion());
                     this.aCW.save();
                  }

                  method2300("Compiled class: " + var8 + " to " + var11);
               } catch (IOException var57) {
                  var5.aCY.add("Error writing compiled classes to disk: " + var57.getMessage());
               }
            } catch (Exception var59) {
               var5.aCY.add("Class loading error: " + var59.getClass() + " - " + var59.getMessage());
            }

            return var5;
         } else {
            for (Diagnostic var90 : List.copyOf(var75.getDiagnostics())) {
               if ((var90.getLineNumber() != -1L || !var90.getMessage(Locale.ENGLISH).contains("uses or overrides a deprecated API"))
                  && (var90.getLineNumber() != -1L || !var90.getMessage(Locale.ENGLISH).contains("Recompile with -Xlint:deprecation"))
                  && (var90.getLineNumber() != -1L || !var90.getMessage(Locale.ENGLISH).contains("Some messages have been simplified"))
                  && (var90.getLineNumber() != -1L || !var90.getMessage(Locale.ENGLISH).contains("Some messages have been suppressed"))) {
                  var5.aCY.add("Error at line " + var90.getLineNumber() + ": " + var90.getMessage(Locale.ENGLISH));
                  int var92 = (int)var90.getLineNumber();
                  if (var92 - 1 >= 0) {
                     var5.aCY.add("Code: " + var14[var92 - 1]);
                  }
               }
            }

            return var5;
         }
      } finally {
         if (Settings.get().logScriptCompilation) {
            List var36 = var5.aCY;
            Log.log(
               "[Scripts] Generated Java code for: '"
                  + var3
                  + "'\n"
                  + Arrays.stream(var13.split("\n"))
                     .map(
                        var2x -> {
                           int var3x = Arrays.asList(var13.split("\n")).indexOf(var2x) + 1;
                           String var4x = var36.stream().anyMatch(var1xx -> var1xx.contains("Error at line " + var3x + ":"))
                              ? ChatColor.RED + " <- Error" + ChatColor.RESET
                              : "";
                           Object var5x = var2x.stripTrailing();
                           if (!var4x.equals("")) {
                              var5x = ChatColor.YELLOW + var5x;
                           }

                           return var5x + " // " + var3x + var4x;
                        }
                     )
                     .collect(Collectors.joining("\n"))
            );
         }
      }
   }

   private static String dY(String var0) {
      Matcher var1 = Pattern.compile("public class (\\w+)").matcher(var0);
      return var1.find() ? var1.group(1) : "<not found>";
   }

   private StringBuilder method2291(String[] var1) {
      StringBuilder var2 = new StringBuilder();
      boolean var3 = false;

      for (String var7 : var1) {
         String var8 = var7.trim();
         Matcher var9 = Pattern.compile("^delay\\(([^,]+),\\s*\\(\\)\\s*=>\\s*\\{(.*?)\\}\\);\\s*$").matcher(var8);
         if (var9.matches()) {
            var2.append("_runDelayed(").append(var9.group(1).trim()).append(", () -> {\n").append(var9.group(2).trim()).append("\n});\n");
         } else {
            Matcher var10 = Pattern.compile("^delay\\(([^,]+),\\s*\\(\\)\\s*=>\\s*(.+?)\\);\\s*$").matcher(var8);
            if (var10.matches()) {
               String var11 = var10.group(2).trim();
               if (!var11.endsWith(";")) {
                  var11 = var11 + ";";
               }

               var2.append("_runDelayed(").append(var10.group(1).trim()).append(", () -> {\n").append(var11).append("\n});\n");
            } else {
               Matcher var12 = Pattern.compile("^delay\\(([^,]+),\\s*\\(\\)\\s*=>\\s*\\{\\s*$").matcher(var8);
               if (var12.matches()) {
                  var3 = true;
                  var2.append("_runDelayed(").append(var12.group(1).trim()).append(", () -> {\n");
               } else if (var3 && var8.matches("^\\}\\);\\s*$")) {
                  var3 = false;
                  var2.append("});\n");
               } else {
                  var2.append(var7).append("\n");
               }
            }
         }
      }

      return var2;
   }

   private StringBuilder method2292(String[] var1) {
      StringBuilder var2 = new StringBuilder();
      boolean var3 = false;
      int var4 = 0;

      for (String var8 : var1) {
         String var9 = var8.trim();
         Matcher var10 = Pattern.compile("^([a-zA-Z_][a-zA-Z0-9_<>]*)\\s+(\\w+)\\s*\\(([^)]*)\\)\\s*\\{\\s*$").matcher(var9);
         if (var10.matches() && !var3) {
            var3 = true;
            var4 = 1;
            String var11 = var10.group(1);
            String var12 = var10.group(2);
            String var13 = var10.group(3);
            var2.append("public static ").append(var11).append(" ").append(var12).append("(").append(var13).append(") {\n");
         } else if (var3) {
            if (var9.contains("{")) {
               var4++;
            }

            if (var9.contains("}")) {
               var4--;
            }

            var2.append(var8).append("\n");
            if (var4 == 0) {
               var3 = false;
            }
         } else {
            var2.append(var8).append("\n");
         }
      }

      return var2;
   }

   public CompilationResult method2293(CompiledScript var1, Plugin var2, Event var3, Player var4, CustomStack var5, ItemStack var6) {
      long var7 = System.currentTimeMillis();
      if (Settings.get().logScriptExecution) {
         this.log("Looking for main method in: " + var1.aDd.getName());
         this.log("Compiled class loader: " + var1.aDd.getClassLoader());
      }

      try {
         if (ItemScript.class.isAssignableFrom(var1.aDd)) {
            ItemScript var22 = (ItemScript)var1.aDd.getDeclaredConstructor().newInstance();
            var22.handleEvent(var2, var3, var4, var5, var6);
            return new CompilationResult(System.currentTimeMillis() - var7);
         }
      } catch (Throwable var21) {
         return new CompilationResult(System.currentTimeMillis() - var7, var21);
      }

      for (Method var12 : var1.aDd.getDeclaredMethods()) {
         if (Settings.get().logScriptExecution) {
            this.log("Method: " + var12.getName() + " with params: " + Arrays.toString(var12.getParameterTypes()));
         }

         if (var12.getName().equals("main") && var12.getParameterCount() == 5) {
            if (var12.getParameterTypes()[0].equals(Plugin.class)
               && var12.getParameterTypes()[1].equals(Event.class)
               && var12.getParameterTypes()[2].equals(Player.class)
               && var12.getParameterTypes()[3].equals(CustomStack.class)
               && var12.getParameterTypes()[4].equals(ItemStack.class)) {
               var12.setAccessible(true);

               try {
                  var12.invoke(null, var2, var3, var4, var5, var6);
               } catch (InvocationTargetException var20) {
                  Throwable var14 = var20.getCause();
                  if (var14 != null) {
                     String var15 = var1.aDd.getName();
                     String var16 = null;
                     StackTraceElement[] var17 = var14.getStackTrace();
                     StackTraceElement var18 = var17[0];
                     if (var18.getClassName().equals(var15)) {
                        int var19 = var18.getLineNumber();
                        if (var19 > 0 && var19 <= var1.aDc.split("\n").length) {
                           var16 = var1.aDc.split("\n")[var19 - 1];
                           var16 = var16.trim();
                        }
                     }

                     if (var16 != null) {
                        String[] var24 = new String[]{var14.getMessage(), "  at line " + var17[0].getLineNumber() + ": " + var16};
                        return new CompilationResult(System.currentTimeMillis() - var7, var24);
                     }

                     return new CompilationResult(System.currentTimeMillis() - var7, var14);
                  }

                  return new CompilationResult(System.currentTimeMillis() - var7, var20);
               }
            }

            return new CompilationResult(System.currentTimeMillis() - var7);
         }
      }

      return new CompilationResult(System.currentTimeMillis() - var7, new RuntimeException("No main method found in class: " + var1.aDd.getName()));
   }

   public CompilationResult method2294(CompiledScript var1, Plugin var2, Event var3, @Nullable Player var4, @Nullable CustomEntity var5) {
      long var6 = System.currentTimeMillis();
      if (Settings.get().logScriptExecution) {
         this.log("Looking for main method in: " + var1.aDd.getName());
         this.log("Compiled class loader: " + var1.aDd.getClassLoader());
      }

      try {
         if (EntityScript.class.isAssignableFrom(var1.aDd)) {
            EntityScript var21 = (EntityScript)var1.aDd.getDeclaredConstructor().newInstance();
            var21.handleEvent(var2, var3, var4, var5);
            return new CompilationResult(System.currentTimeMillis() - var6);
         }
      } catch (Throwable var20) {
         return new CompilationResult(System.currentTimeMillis() - var6, var20);
      }

      for (Method var11 : var1.aDd.getDeclaredMethods()) {
         if (Settings.get().logScriptExecution) {
            this.log("Method: " + var11.getName() + " with params: " + Arrays.toString(var11.getParameterTypes()));
         }

         if (var11.getName().equals("main")
            && var11.getParameterCount() == 4
            && var11.getParameterTypes()[0].equals(Plugin.class)
            && var11.getParameterTypes()[1].equals(Event.class)
            && var11.getParameterTypes()[2].equals(Player.class)
            && var11.getParameterTypes()[3].equals(CustomEntity.class)) {
            var11.setAccessible(true);

            try {
               var11.invoke(null, var2, var3, var4, var5);
            } catch (InvocationTargetException var19) {
               Throwable var13 = var19.getCause();
               if (var13 != null) {
                  String var14 = var1.aDd.getName();
                  String var15 = null;
                  StackTraceElement[] var16 = var13.getStackTrace();
                  StackTraceElement var17 = var16[0];
                  if (var17.getClassName().equals(var14)) {
                     int var18 = var17.getLineNumber();
                     if (var18 > 0 && var18 <= var1.aDc.split("\n").length) {
                        var15 = var1.aDc.split("\n")[var18 - 1];
                        var15 = var15.trim();
                     }
                  }

                  if (var15 != null) {
                     String[] var23 = new String[]{var13.getMessage(), "  at line " + var16[0].getLineNumber() + ": " + var15};
                     return new CompilationResult(System.currentTimeMillis() - var6, var23);
                  }

                  return new CompilationResult(System.currentTimeMillis() - var6, var13);
               }

               return new CompilationResult(System.currentTimeMillis() - var6, var19);
            }

            return new CompilationResult(System.currentTimeMillis() - var6);
         }
      }

      return new CompilationResult(
         System.currentTimeMillis() - var6, new RuntimeException("No main custom entity method found in class: " + var1.aDd.getName())
      );
   }

   private String method2295(String var1, String var2) {
      if ((var1.contains("extends EntityScript") || var1.contains("extends ItemScript")) && var1.contains("class ")) {
         if (Settings.get().logScriptCompilation) {
            Log.log("Detected new script format with main method and class declaration. Adjusting script structure.");
         }

         if (!var1.contains("public class ")) {
            throw new IllegalArgumentException("Script must contain a public class declaration. Found: " + var1);
         }

         StringBuilder var20 = new StringBuilder(var1);
         if (!var20.toString().contains("package ")) {
            String[] var21 = var2.split("\\.");
            String var22 = String.join(".", Arrays.copyOf(var21, var21.length - 1));
            var20.insert(0, "package iascript_generated." + var22 + ";\n\n");
         }

         return var20.toString();
      } else {
         ScriptTargetType var3 = ScriptTargetType.eg(var1);
         if (var3 == ScriptTargetType.BOTH) {
            throw new IllegalArgumentException("Script contains $customItem and $customEntity! Decide one of them or create two different scripts.");
         }

         HashSet var4 = new HashSet();
         StringBuilder var5 = new StringBuilder();
         StringBuilder var6 = new StringBuilder();
         String[] var7 = var1.split("\n");
         boolean var8 = false;
         int var9 = 0;

         for (String var13 : var7) {
            String var14 = var13.trim();
            if (!var14.isEmpty()) {
               if (var14.startsWith("import \"") && var14.endsWith("\";")) {
                  String var33 = var14.substring(8, var14.length() - 2);
                  var4.add(var33);
               } else {
                  if (var14.contains("ChatColor.") && !var4.toString().contains("org.bukkit.ChatColor")) {
                     var4.add("org.bukkit.ChatColor");
                  }

                  if (var14.contains("Material.") && !var4.toString().contains("org.bukkit.Material")) {
                     var4.add("org.bukkit.Material");
                  }

                  for (String var16 : this.aCU) {
                     String var17 = var16.substring(var16.lastIndexOf(46) + 1);
                     if (var14.contains(var17) && !var4.toString().contains(var16)) {
                        var4.add(var16);
                     }
                  }

                  boolean var32 = var14.matches("^(public\\s+)?(class|record|interface|enum)\\s+\\w+.*");
                  if (!var32 && !var8) {
                     var6.append(var13).append("\n");
                  } else {
                     var5.append(var13).append("\n");

                     for (char var19 : var13.toCharArray()) {
                        if (var19 == '{') {
                           var9++;
                        } else if (var19 == '}') {
                           var9--;
                        }
                     }

                     var8 = var9 > 0;
                  }
               }
            }
         }

         var6 = this.method2291(var6.toString().split("\n"));
         var6 = this.method2292(var6.toString().split("\n"));
         NullableBox var26 = NullableBox.of(var6);
         String var27 = this.method2296(var26);
         var6 = (StringBuilder)var26.value;
         var4.addAll(this.aCT);
         ArrayList var28 = new ArrayList(aCR);
         StringBuilder var29 = new StringBuilder(var4.stream().map(var0 -> "import " + var0 + ";").collect(Collectors.joining("\n")));

         for (String var34 : var28) {
            var29.append("\nimport static ").append(var34).append(".*").append(";");
         }

         String var31;
         if (var3 == ScriptTargetType.ITEM) {
            var31 = "Plugin $plugin, Event $event, Player $player, CustomStack $customStack, ItemStack $itemStack";
         } else if (var3 == ScriptTargetType.CUSTOM_ENTITY) {
            var31 = "Plugin $plugin, Event $event, Player $player, CustomEntity $customEntity";
         } else {
            var31 = "Plugin $plugin, Event $event, Player $player, CustomStack $customStack, ItemStack $itemStack";
         }

         String var35 = "package iascript_generated;\n"
            + var29
            + "\n\npublic class "
            + var2
            + " {\n\n"
            + this.ea(var5.toString())
            + "\n    public static void main("
            + var31
            + ") {\n"
            + this.ea(var6.toString())
            + "    }\n\n"
            + this.ea(var27)
            + "}";
         return this.ea(var35);
      }
   }

   private String method2296(NullableBox var1) {
      String[] var2 = ((StringBuilder)var1.value).toString().split("\n");
      StringBuilder var3 = new StringBuilder();
      StringBuilder var4 = new StringBuilder();
      boolean var5 = false;
      int var6 = 0;

      for (String var10 : var2) {
         String var11 = var10.trim();
         if (!var5) {
            if (var11.matches("^(public|private|protected|static|final|\\s)*\\s*\\w+\\s+\\w+\\s*\\(.*\\)\\s*\\{?\\s*$")) {
               var5 = true;
               var6 = this.dZ(var10);
               var4.append(var10).append("\n");
            } else {
               var3.append(var10).append("\n");
            }
         } else {
            var6 += this.dZ(var10);
            var4.append(var10).append("\n");
            if (var6 <= 0) {
               var5 = false;
               var4.append("\n");
            }
         }
      }

      var1.value = var3;
      return var4.toString();
   }

   private int dZ(String var1) {
      int var2 = 0;

      for (char var6 : var1.toCharArray()) {
         if (var6 == '{') {
            var2++;
         }

         if (var6 == '}') {
            var2--;
         }
      }

      return var2;
   }

   private String ea(String var1) {
      try {
         AtomicInteger var2 = new AtomicInteger();
         return Arrays.stream(var1.split("\n")).map(var1x -> {
            if (var1x.trim().startsWith("}")) {
               var2.getAndDecrement();
            }

            String var2x = "    ".repeat(var2.get()) + var1x.trim();
            if (var1x.trim().endsWith("{")) {
               var2.getAndIncrement();
            }

            return var2x;
         }).collect(Collectors.joining("\n"));
      } catch (Exception var3) {
         this.method2301("Error indenting code: " + var3.getMessage(), var3);
         return var1;
      }
   }

   public String eb(String var1) {
      return Arrays.stream(var1.split("\n")).map(this::ec).collect(Collectors.joining("\n"));
   }

   private String ec(String var1) {
      String var2 = var1.trim();

      for (RegexReplacement var4 : aCV) {
         Matcher var5 = var4.pattern.matcher(var2);
         if (var5.matches()) {
            return (String)var4.replacer.apply(var5);
         }
      }

      return (CharSequence)var1;
   }

   private Set method2297(List var1) {
      HashSet var2 = new HashSet();

      for (Diagnostic var4 : var1) {
         String var5 = var4.getMessage(Locale.ENGLISH);
         String var6 = ed(var5);
         if (var6 != null && var6.contains(".")) {
            try {
               Class var7 = Class.forName(var6);
               CodeSource var8 = var7.getProtectionDomain().getCodeSource();
               if (var8 != null) {
                  File var9 = new File(var8.getLocation().toURI());
                  var2.add(var9.getAbsolutePath());
                  method2300("Resolved missing class: " + var6 + " from " + var9.getName());
               }
            } catch (Throwable var10) {
               Log.error("[Scripts] Could not resolve class: " + var6);
            }
         }
      }

      return var2;
   }

   @Nullable
   private static String ed(String var0) {
      var0 = var0.replace("\n", " ").replace("\r", " ");
      String var1 = null;
      if (var0.contains("class file for")) {
         int var2 = var0.indexOf("class file for") + "class file for".length();
         int var3 = var0.indexOf("not found", var2);
         if (var3 > var2) {
            var1 = var0.substring(var2, var3).trim();
         }
      } else if (var0.contains("cannot access")) {
         int var5 = var0.indexOf("cannot access") + "cannot access".length();
         int var6 = var0.indexOf(" ", var5 + 1);
         if (var6 == -1) {
            var6 = var0.length();
         }

         var1 = var0.substring(var5, var6).trim();
      }

      return var1;
   }

   private Set method2298(Class var1) {
      HashSet var2 = new HashSet();

      try {
         CodeSource var3 = var1.getProtectionDomain().getCodeSource();
         if (var3 != null) {
            File var4 = new File(var3.getLocation().toURI());
            var2.add(var4.getAbsolutePath());
         }
      } catch (Exception var10) {
      }

      for (Plugin var6 : Bukkit.getPluginManager().getPlugins()) {
         try {
            CodeSource var7 = var6.getClass().getProtectionDomain().getCodeSource();
            if (var7 != null) {
               File var8 = new File(var7.getLocation().toURI());
               var2.add(var8.getAbsolutePath());
            }
         } catch (Exception var9) {
         }
      }

      return var2;
   }

   private Set method2299(String var1) {
      Class var2 = Class.forName(var1);
      return this.method2298(var2);
   }

   private void log(String msg) {
      Log.log(msg);
   }

   public static void method2300(String var0) {
   }

   private void method2301(String var1, Exception var2) {
   }
}
