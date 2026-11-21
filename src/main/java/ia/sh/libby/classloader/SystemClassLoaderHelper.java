package ia.sh.libby.classloader;

import ia.sh.libby.LibraryManager;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Objects;
import java.util.jar.JarFile;
import org.jetbrains.annotations.NotNull;

public class SystemClassLoaderHelper extends ClassLoaderHelper {
   private MethodHandle appendMethodHandle = null;
   private Instrumentation appendInstrumentation = null;

   public SystemClassLoaderHelper(ClassLoader classLoader, @NotNull LibraryManager libraryManager) {
      super(classLoader);
      Objects.requireNonNull(libraryManager, "libraryManager");

      try {
         Method appendMethod = classLoader.getClass().getDeclaredMethod("appendToClassPathForInstrumentation", String.class);
         this.setMethodAccessible(
            libraryManager,
            appendMethod,
            classLoader.getClass().getName() + "#appendToClassPathForInstrumentation(String)",
            methodHandle -> this.appendMethodHandle = methodHandle,
            instrumentation -> this.appendInstrumentation = instrumentation
         );
      } catch (Exception e) {
         throw new RuntimeException("Couldn't initialize SystemClassLoaderHelper", e);
      }
   }

   @Override
   public void addToClasspath(@NotNull URL url) {
      try {
         if (this.appendInstrumentation != null) {
            this.appendInstrumentation.appendToSystemClassLoaderSearch(new JarFile(url.toURI().getPath()));
         } else {
            this.appendMethodHandle.invokeWithArguments(url.toURI().getPath());
         }
      } catch (Throwable e) {
         throw new RuntimeException(e);
      }
   }
}
