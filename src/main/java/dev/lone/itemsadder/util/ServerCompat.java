package dev.lone.itemsadder.util;

import beer.devs.fastnbt.nms.Version;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class ServerCompat {
   public static boolean nativeAdventure = false;
   public static final String NMS_ENTITY_PACKAGE = "net.minecraft.world.entity.";
   public static boolean viaVersionPresent;
   @Nullable
   private static Boolean paperCache = null;
   @Nullable
   private static Boolean coreMissingCache = null;

   public static boolean isCoreMissing() {
      if (coreMissingCache != null) {
         return coreMissingCache;
      }

      coreMissingCache = !classExists("dev.lone.itemsadder.Core.Core");
      return coreMissingCache;
   }

   public static boolean isDevMode() {
      return !isCoreMissing();
   }

   public static void init() {
      EnvironmentDetector.init();
   }

   public static int getPackVersion() {
      return packVersionOf(Version.get());
   }

   public static int packVersionOf(Version var0) {
      return var0.getPackVersion();
   }

   public static int getFallbackLimit() {
      return 250;
   }

   @Nullable
   public static Class classForName(String var0) {
      try {
         return Class.forName(var0);
      } catch (Throwable var2) {
         return null;
      }
   }

   public static MethodHandle findConstructorHandle(Class var0, Class... var1) {
      try {
         MethodType var2 = MethodType.methodType(void.class, var1);
         Lookup var3 = MethodHandles.lookup();
         return MethodHandles.privateLookupIn(var0, var3).findConstructor(var0, var2);
      } catch (Throwable var4) {
         throw new RuntimeException("Failed to find constructor.", var4);
      }
   }

   public static VarHandle findVarHandle(Class var0, String var1, Class var2) {
      try {
         return MethodHandles.privateLookupIn(var0, MethodHandles.lookup()).findVarHandle(var0, var1, var2);
      } catch (ReflectiveOperationException var4) {
         throw new IllegalStateException("Cannot access " + var0.getName() + "." + var1 + " as " + var2.getName(), var4);
      }
   }

   public static boolean setFieldByType(Class var0, Class var1, Object var2, Object var3) {
      for (Field var7 : var0.getDeclaredFields()) {
         if (var7.getType() == var1) {
            var7.setAccessible(true);
            var7.set(var2, var3);
            return true;
         }
      }

      return false;
   }

   @Nullable
   public static Field findFieldByType(Class var0, Class var1) {
      for (Field var5 : var0.getDeclaredFields()) {
         if (var5.getType() == var1) {
            var5.setAccessible(true);
            return var5;
         }
      }

      return null;
   }

   @Nullable
   public static Field findFieldOrNull(Class var0, String var1) {
      try {
         Field var2 = var0.getDeclaredField(var1);
         var2.setAccessible(true);
         return var2;
      } catch (NoSuchFieldException var4) {
         return null;
      }
   }

   @Nullable
   public static Field findFieldOrThrow(Class var0, String var1) {
      Field var2 = var0.getDeclaredField(var1);
      var2.setAccessible(true);
      return var2;
   }

   @Nullable
   public static Object getFieldValueByTypeIndex(Class var0, Class var1, int var2) {
      return getFieldValueOn(var0, var1, null, var2);
   }

   @Nullable
   public static Object getFieldValueOn(Class var0, Class var1, Object var2, int var3) {
      Field var4 = findFieldByTypeIndex(var0, var1, var3);
      return var4 == null ? null : readField(var4, var2);
   }

   @Nullable
   public static Field findFieldByTypeIndex(Class var0, Class var1, int var2) {
      Field[] var3 = var0.getDeclaredFields();
      int var4 = 0;

      for (Field var8 : var3) {
         if (var8.getType() == var1) {
            if (var4 == var2) {
               var8.setAccessible(true);
               return var8;
            }

            var4++;
         }
      }

      return null;
   }

   @Nullable
   public static Field findFieldByIndex(Class var0, int var1) {
      Field[] var2 = var0.getDeclaredFields();
      if (var1 > var2.length - 1) {
         return null;
      }

      var2[var1].setAccessible(true);
      return var2[var1];
   }

   @Nullable
   public static Method findMethodByReturnType(Class var0, Class var1, int var2) {
      Method[] var3 = var0.getDeclaredMethods();
      int var4 = 0;

      for (Method var8 : var3) {
         if (var8.getReturnType() == var1) {
            if (var4 == var2) {
               var8.setAccessible(true);
               return var8;
            }

            var4++;
         }
      }

      return null;
   }

   public static Object getFieldValue(Class var0, String var1, Object var2) {
      Field var3 = findFieldOrThrow(var0, var1);

      try {
         return var3.get(var2);
      } catch (IllegalAccessException var5) {
         throw new RuntimeException("Failed to read field " + var3.getName(), var5);
      }
   }

   public static Object readField(Field var0, Object var1) {
      try {
         return var0.get(var1);
      } catch (IllegalAccessException var3) {
         throw new RuntimeException("Failed to read field " + var0.getName(), var3);
      }
   }

   public static void writeField(Field var0, Object var1, Object var2) {
      try {
         var0.setAccessible(true);
         var0.set(var1, var2);
      } catch (IllegalAccessException var4) {
         throw new RuntimeException("Failed to write field " + var0.getName(), var4);
      }
   }

   public static boolean calledFromPluginLifecycle(Plugin var0) {
      StackTraceElement[] var1 = Thread.currentThread().getStackTrace();

      for (StackTraceElement var5 : var1) {
         if (var5.getClassName().equals(var0.getClass().getName()) && (var5.getMethodName().equals("onLoad") || var5.getMethodName().equals("onEnable"))) {
            return true;
         }
      }

      return false;
   }

   public static boolean classExists(String var0) {
      try {
         Class.forName(var0);
         return true;
      } catch (Throwable var2) {
         return false;
      }
   }

   public static boolean isPaper() {
      if (paperCache != null) {
         return paperCache;
      } else {
         String var0 = Bukkit.getServer().getName();
         if (!var0.contains("Paper") && !var0.contains("Purpur")) {
            paperCache = classExists("com.destroystokyo.paper.event.player.PlayerSetSpawnEvent$Cause")
               || classExists("com.destroystokyo.paper.utils.PaperPluginLogger")
               || classExists("io.papermc.paper.ServerBuildInfo")
               || classExists("io.papermc.paper.text.PaperComponents");
            return paperCache;
         } else {
            paperCache = true;
            return paperCache;
         }
      }
   }

   public static boolean isAbstractClass(String var0) {
      try {
         Class var1 = Class.forName(var0);
         return Modifier.isAbstract(var1.getModifiers());
      } catch (Throwable var2) {
         return false;
      }
   }

   public static JarLocation locateClassJar(String var0) {
      try {
         return Class.forName(var0).getClassLoader().equals(Bukkit.class.getClassLoader()) ? JarLocation.SERVER_JAR : JarLocation.ANOTHER_JAR;
      } catch (ClassNotFoundException var2) {
         return JarLocation.NOT_FOUND;
      }
   }

   public static Method findDeclaredMethod(Class var0, String var1, Class... var2) {
      try {
         Method var3 = var0.getDeclaredMethod(var1, var2);
         var3.setAccessible(true);
         return var3;
      } catch (NoSuchMethodException var4) {
         return null;
      }
   }

   public static Method findMethodByAccess(Class var0, Class var1, AccessLevel var2, Class... var3) {
      for (Method var7 : var1.getDeclaredMethods()) {
         switch (var2) {
            case PUBLIC:
               if (!Modifier.isPublic(var7.getModifiers())) {
                  continue;
               }
               break;
            case PROTECTED:
               if (!Modifier.isProtected(var7.getModifiers())) {
                  continue;
               }
               break;
            case PRIVATE:
               if (!Modifier.isPrivate(var7.getModifiers())) {
                  continue;
               }
               break;
            case PACKAGE_PRIVATE:
               if (Modifier.isPublic(var7.getModifiers()) || Modifier.isProtected(var7.getModifiers()) || Modifier.isPrivate(var7.getModifiers())) {
                  continue;
               }
         }

         if (var7.getReturnType() == var0) {
            Class[] var8 = var7.getParameterTypes();
            if (var8.length == var3.length) {
               boolean var9 = true;

               for (int var10 = 0; var10 < var8.length; var10++) {
                  if (var8[var10] != var3[var10]) {
                     var9 = false;
                     break;
                  }
               }

               if (var9) {
                  var7.setAccessible(true);
                  return var7;
               }
            }
         }
      }

      return null;
   }
}
