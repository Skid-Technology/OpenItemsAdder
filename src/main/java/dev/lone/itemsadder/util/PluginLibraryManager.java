package dev.lone.itemsadder.util;

import dev.lone.itemsadder.Main;
import ia.sh.libby.Library;
import ia.sh.libby.LibraryManager;
import ia.sh.libby.classloader.URLClassLoaderHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PluginLibraryManager extends LibraryManager {
   private static volatile boolean JOLT_LOADED;
   private final Function remapper;
   private final URLClassLoaderHelper classLoaderHelper;
   private final ClassLoader pluginClassLoader;
   private final YamlConfiguration libraryConfig;
   private final String configKey;

   public PluginLibraryManager(Main var1) {
      this(var1, "libraries-libby");
   }

   public PluginLibraryManager(Main var1, LibraryOptions var2) {
      this(var1, "libraries-libby", var2);
   }

   public PluginLibraryManager(Main var1, String var2) {
      this(var1, var2, LibraryOptions.DEFAULT);
   }

   public PluginLibraryManager(Main var1, String var2, LibraryOptions var3) {
      super(new LibbyLogAdapter(var1), var1.getDataFolder().toPath(), "lib");
      this.pluginClassLoader = resolveLibraryClassLoader(var1);
      this.classLoaderHelper = new URLClassLoaderHelper((URLClassLoader)this.pluginClassLoader, this);
      this.libraryConfig = loadLibraryConfig(var1);
      this.configKey = var2;

      Function var4;
      try {
         Class var5 = Class.forName("org.bukkit.plugin.java.LibraryLoader");
         Field var6 = var5.getDeclaredField("REMAPPER");
         var4 = (Function)var6.get(null);
      } catch (Exception var7) {
         var4 = null;
      }

      this.remapper = var4;
      if (var3.mavenLocal) {
         this.addMavenLocal();
      }

      if (var3.mavenCentral) {
         this.addRepository("https://maven-central.storage-download.googleapis.com/maven2/");
         this.addMavenCentral();
      }

      if (var3.aliyunMirror) {
         this.addRepository("https://maven.aliyun.com/repository/central");
      }

      if (var3.excludedLibraries != null) {
         var3.excludedLibraries.stream().filter(var0 -> var0 != null && !var0.isEmpty()).forEach(var1x -> this.addRepository(var1x));
      }
   }

   private static URLClassLoader resolveLibraryClassLoader(Plugin var0) {
      ClassLoader var1 = var0.getClass().getClassLoader();
      if (!ServerCompat.isPaper()) {
         return (URLClassLoader)var1;
      }

      try {
         Field var2 = var1.getClass().getDeclaredField("libraryLoader");
         var2.setAccessible(true);
         Object var3 = var2.get(var1);
         if (var3 instanceof URLClassLoader) {
            return (URLClassLoader)var3;
         } else {
            throw new IllegalStateException("Plugin library classloader is unavailable");
         }
      } catch (Exception var4) {
         var0.getLogger().log(Level.WARNING, "Failed to access the isolated library classloader, falling back to the plugin classloader.", var4);
         return (URLClassLoader)var1;
      }
   }

   @Override
   protected void addToClasspath(@NotNull Path file) {
      this.classLoaderHelper.addToClasspath(file);
   }

   private Path remapIfNeeded(Path var1) {
      return this.remapper != null ? (Path)((List)this.remapper.apply(Collections.singletonList(var1))).get(0) : var1;
   }

   private void remapJar(Path var1, Path var2) {
      Path var3 = Files.createTempFile("tmp-", ".jar");

      try (
         JarFile var4 = new JarFile(var1.toFile());
         JarOutputStream var5 = new JarOutputStream(Files.newOutputStream(var3));
      ) {
         Enumeration var6 = var4.entries();

         while (var6.hasMoreElements()) {
            JarEntry var7 = (JarEntry)var6.nextElement();
            if (!"META-INF/MANIFEST.MF".equals(var7.getName())) {
               try (InputStream var8 = var4.getInputStream(var7)) {
                  var5.putNextEntry(new JarEntry(var7.getName()));
                  byte[] var9 = new byte[1024];

                  int var10;
                  while ((var10 = var8.read(var9)) != -1) {
                     var5.write(var9, 0, var10);
                  }

                  var5.closeEntry();
               }
            }
         }

         Manifest var17 = var4.getManifest();
         if (var17 == null) {
            var17 = new Manifest();
         }

         var17.getMainAttributes().putValue("paperweight-mappings-namespace", "spigot");
         var5.putNextEntry(new JarEntry("META-INF/MANIFEST.MF"));
         var17.write(var5);
         var5.closeEntry();
      }

      Files.move(var3, var2, StandardCopyOption.REPLACE_EXISTING);
   }

   public void loadLibrary(@NotNull Library var1, boolean var2) {
      this.logger.info("Loading library " + var1.getArtifactId() + (var2 ? " (remapped)" : "") + "...");
      Path var3 = this.downloadLibrary(Objects.requireNonNull(var1, "library"));
      if (var1.resolveTransitiveDependencies()) {
         this.resolveTransitiveLibraries(var1);
      }

      if (var2) {
         try {
            Path var4 = var3.getParent().resolve(FilenameUtils.getBaseName(var3.getFileName().toString()) + "-modified.jar");
            this.remapJar(var3, var4);
            var3 = this.remapIfNeeded(var4);
         } catch (IOException var5) {
            throw new RuntimeException("Failed to remap library " + var1.getArtifactId(), var5);
         }
      }

      if (var1.isIsolatedLoad()) {
         this.addToIsolatedClasspath(var1, var3);
      } else {
         this.addToClasspath(var3);
      }
   }

   public void loadFromConfig() {
      this.loadAll(this.libraryConfig.getStringList(this.configKey));
   }

   public void addAndLoad(Set var1) {
      List var2 = this.libraryConfig.getStringList(this.configKey);
      var2.addAll(var1);
      this.loadAll(var2);
   }

   public void loadLibraries(Set var1) {
      this.loadAll(var1);
   }

   public void loadJoltNative() {
      String var1 = "6.1.1";
      Library var2 = Library.builder()
         .groupId("com.github.stephengold")
         .artifactId("jolt-jni-Windows64")
         .version(var1)
         .resolveTransitiveDependencies(false)
         .build();
      this.loadLibrary(var2, false);
      String[] var3 = joltNativeFor(System.getProperty("os.name", ""), System.getProperty("os.arch", ""));
      String var4 = "jolt-jni-" + var3[0];
      Library var5 = Library.builder()
         .groupId("com.github.stephengold")
         .artifactId(var4)
         .version(var1)
         .classifier("ReleaseSp")
         .resolveTransitiveDependencies(false)
         .build();
      Path var6 = this.downloadLibrary(var5);
      String var7 = var3[1] + "/com/github/stephengold/" + (var3[2].equals("dll") ? "joltjni.dll" : "libjoltjni." + var3[2]);
      Path var8 = Files.createTempFile("itemsadder-jolt-", "." + var3[2]);
      var8.toFile().deleteOnExit();
      boolean var9 = false;

      try {
         JarFile var10 = new JarFile(var6.toFile());

         try {
            JarEntry var11 = var10.getJarEntry(var7);
            if (var11 == null) {
               throw new IOException("Jolt native library missing from " + var6 + ": " + var7);
            }

            try (InputStream var12 = var10.getInputStream(var11)) {
               Files.copy(var12, var8, StandardCopyOption.REPLACE_EXISTING);
            }
         } catch (Throwable var19) {
            try {
               var10.close();
            } catch (Throwable var16) {
               var19.addSuppressed(var16);
            }

            throw var19;
         }

         var10.close();
         Class var21 = Class.forName("com.github.stephengold.joltjni.NativeLibraryLoader", true, this.pluginClassLoader);
         if (!(Boolean)var21.getMethod("loadLibrary", String.class).invoke(null, var8.toAbsolutePath().toString())) {
            throw new UnsatisfiedLinkError("Failed to load Jolt native library from " + var8);
         }

         var9 = true;
         Class var22 = Class.forName("com.github.stephengold.joltjni.Jolt", true, this.pluginClassLoader);
         String var23 = (String)var22.getMethod("versionString").invoke(null);
         var22.getMethod("registerDefaultAllocator").invoke(null);
         if (!(Boolean)var22.getMethod("newFactory").invoke(null)) {
            throw new IllegalStateException("Jolt factory creation failed");
         }

         var22.getMethod("registerTypes").invoke(null);
         this.logger.info("Loaded Jolt JNI " + var23);
         JOLT_LOADED = true;
      } catch (Exception | LinkageError var20) {
         if (!var9) {
            try {
               Files.deleteIfExists(var8);
            } catch (IOException var15) {
               var20.addSuppressed(var15);
            }
         }

         throw var20;
      }
   }

   public static boolean isJoltLoaded() {
      return JOLT_LOADED;
   }

   static String[] joltNativeFor(String var0, String var1) {
      String var2 = var0.toLowerCase(Locale.ROOT);
      String var3 = var1.toLowerCase(Locale.ROOT);
      boolean var4 = var3.equals("aarch64") || var3.equals("arm64");
      boolean var5 = var3.equals("amd64") || var3.equals("x86_64") || var3.equals("x64");
      if (var2.contains("linux")) {
         if (var4) {
            return new String[]{"Linux_ARM64", "linux/aarch64", "so"};
         }

         if (var5) {
            return new String[]{"Linux64", "linux/x86-64", "so"};
         }

         if (var3.equals("arm") || var3.equals("armhf") || var3.equals("armv7l")) {
            return new String[]{"Linux_ARM32hf", "linux/armhf", "so"};
         }
      }

      if (var2.contains("mac") || var2.contains("darwin")) {
         if (var4) {
            return new String[]{"MacOSX_ARM64", "osx/aarch64", "dylib"};
         }

         if (var5) {
            return new String[]{"MacOSX64", "osx/x86-64", "dylib"};
         }
      }

      if (var2.contains("win")) {
         if (var4) {
            return new String[]{"Windows_ARM64", "windows/aarch64", "dll"};
         }

         if (var5) {
            return new String[]{"Windows64", "windows/x86-64", "dll"};
         }
      }

      throw new UnsupportedOperationException("Unsupported Jolt platform: " + var0 + " / " + var1);
   }

   private void loadAll(Iterable var1) {
      for (String var3 : var1) {
         String[] var4 = var3.split(":");
         if (var4.length != 3) {
            this.logger.warn("Invalid library format: " + var3 + ". Expected format: groupId:artifactId:version");
         } else {
            boolean var5 = false;
            int var6 = 1;

            while (true) {
               if (var6 < var4.length) {
                  if (!var4[var6].contains("--remap")) {
                     var6++;
                     continue;
                  }

                  var5 = true;
               }

               var3 = var3.split(" ")[0];
               var4 = var3.split(":");
               Library var11 = Library.builder()
                  .groupId(var4[0])
                  .artifactId(var4[1])
                  .version(var4[2])
                  .resolveTransitiveDependencies(true)
                  .loaderId("itemsadder-libby-libs")
                  .isolatedLoad(false)
                  .build();

               try {
                  this.loadLibrary(var11, var5);
                  break;
               } catch (Exception | LinkageError var8) {
                  throw new RuntimeException("Failed to load library " + var3, var8);
               }
            }
         }
      }
   }

   @NotNull
   private static YamlConfiguration loadLibraryConfig(Plugin var0) {
      File var1;
      try {
         Method var2 = JavaPlugin.class.getDeclaredMethod("getFile");
         var2.setAccessible(true);
         var1 = (File)var2.invoke(var0);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException var10) {
         throw new RuntimeException("Error accessing plugin JAR file.", var10);
      }

      try (JarFile var3 = new JarFile(var1)) {
         JarEntry var4 = var3.getJarEntry("plugin.yml");

         String var14;
         try (InputStream var5 = var3.getInputStream(var4)) {
            var14 = new BufferedReader(new InputStreamReader(var5, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
         }

         YamlConfiguration var15 = new YamlConfiguration();
         var15.loadFromString(var14);
         return var15;
      } catch (InvalidConfigurationException | IOException var13) {
         throw new RuntimeException("plugin.yml not found in the jar file.");
      }
   }
}
