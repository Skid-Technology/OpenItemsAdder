package dev.lone.itemsadder.script;

import dev.lone.itemsadder.util.MarkerAnnotation;
import java.util.HashMap;
import java.util.Map;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject.Kind;

public class MemoryJavaFileManager extends ForwardingJavaFileManager {
   private final Map aCL = new HashMap();

   protected MemoryJavaFileManager(JavaFileManager var1) {
      super(var1);
   }

   @MarkerAnnotation
   @Override
   public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) {
      InMemoryClassFile var5 = new InMemoryClassFile(className, kind);
      this.aCL.put(className, var5);
      return var5;
   }

   public Map Kx() {
      HashMap var1 = new HashMap();

      for (InMemoryClassFile var3 : this.aCL.values()) {
         var1.put(var3.Ky(), var3.getBytes());
      }

      return var1;
   }
}
