package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.util.PluginLibraryManager;
import ia.sh.libby.Library;
import ia.sh.libby.Library.Builder;
import ia.sh.libby.classloader.IsolatedClassLoader;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

enum NativeShaderValidator implements ShaderValidatorBackend {
   INSTANCE;

   private volatile Method method;
   private volatile String unavailable;

   @Override
   public ShaderValidationResult validate(List var1) {
      Method var2 = this.method;
      if (var2 == null && this.unavailable == null) {
         synchronized (this) {
            if (this.method == null && this.unavailable == null) {
               try {
                  this.method = loadBridgeMethod();
               } catch (Throwable var8) {
                  this.unavailable = ShaderValidator.rootCauseMessage(var8);
               }
            }

            var2 = this.method;
         }
      }

      if (var2 == null) {
         return ShaderValidationResult.unavailable(this.unavailable == null ? "unknown native loader error" : this.unavailable);
      }

      try {
         String[] var3 = var1.stream().map(var0 -> var0.stage() == ShaderFileKind.VERTEX ? "vertex" : "fragment").toArray(String[]::new);
         String[] var4 = var1.stream().map(ShaderSource::source).toArray(String[]::new);
         String[] var5 = (String[])var2.invoke(null, var3, var4);
         ArrayList var6 = new ArrayList();

         for (byte var7 = 0; var7 + 3 < var5.length; var7 += 4) {
            var6.add(new ShaderEntry(var5[var7], Integer.parseInt(var5[var7 + 1]), Boolean.parseBoolean(var5[var7 + 2]), var5[var7 + 3]));
         }

         return new ShaderValidationResult(true, null, List.copyOf(var6));
      } catch (InvocationTargetException var10) {
         return ShaderValidationResult.unavailable(ShaderValidator.rootCauseMessage(var10.getTargetException()));
      } catch (Throwable var11) {
         return ShaderValidationResult.unavailable(ShaderValidator.rootCauseMessage(var11));
      }
   }

   private static Method loadBridgeMethod() {
      String var0 = nativeClassifier();
      PluginLibraryManager var1 = new PluginLibraryManager(Main.getInstance());

      for (String var3 : List.of("lwjgl", "lwjgl-shaderc")) {
         var1.loadLibrary(libraryFor(var3, null), false);
         var1.loadLibrary(libraryFor(var3, var0), false);
      }

      IsolatedClassLoader var10 = var1.getIsolatedClassLoaderById("itemsadder-shader-validator");
      if (var10 == null) {
         throw new IllegalStateException("Libby did not create the isolated shader compiler loader");
      }

      String var11 = "/" + "dev.lone.itemsadder.shaderbridge.NativeGlslangBridge".replace('.', '/') + ".class";

      Class var4;
      try (InputStream var5 = ShaderValidator.class.getResourceAsStream(var11)) {
         if (var5 == null) {
            throw new IllegalStateException("missing native shader bridge class");
         }

         var4 = var10.defineClass("dev.lone.itemsadder.shaderbridge.NativeGlslangBridge", var5);
      }

      return var4.getMethod("validate", String[].class, String[].class);
   }

   private static Library libraryFor(String var0, String var1) {
      Builder var2 = Library.builder()
         .groupId("org.lwjgl")
         .artifactId(var0)
         .version("3.4.1")
         .resolveTransitiveDependencies(false)
         .loaderId("itemsadder-shader-validator")
         .isolatedLoad(true);
      if (var1 != null) {
         var2.classifier(var1);
      }

      return var2.build();
   }

   private static String nativeClassifier() {
      String var0 = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
      String var1 = System.getProperty("os.arch", "").toLowerCase(Locale.ROOT);
      boolean var2 = var1.equals("aarch64") || var1.equals("arm64");
      boolean var3 = var1.equals("amd64") || var1.equals("x86_64") || var1.equals("x64");
      if (!var2 && !var3) {
         throw new IllegalStateException("unsupported shader compiler architecture: " + var1);
      } else if (var0.contains("linux")) {
         return var2 ? "natives-linux-arm64" : "natives-linux";
      } else if (var0.contains("win")) {
         return var2 ? "natives-windows-arm64" : "natives-windows";
      } else if (!var0.contains("mac") && !var0.contains("darwin")) {
         throw new IllegalStateException("unsupported shader compiler operating system: " + var0);
      } else {
         return var2 ? "natives-macos-arm64" : "natives-macos";
      }
   }
}
