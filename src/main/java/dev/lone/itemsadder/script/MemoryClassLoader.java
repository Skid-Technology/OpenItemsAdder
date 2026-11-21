package dev.lone.itemsadder.script;

import dev.lone.itemsadder.util.MarkerAnnotation;
import java.util.HashMap;
import java.util.Map;

public class MemoryClassLoader extends ClassLoader {
   private final Map aCK = new HashMap();

   public MemoryClassLoader(Map var1, ClassLoader var2) {
      super(var2);
      this.aCK.putAll(var1);
   }

   @MarkerAnnotation
   @Override
   protected Class findClass(String name) {
      byte[] var2 = (byte[])this.aCK.get(name);
      if (var2 == null) {
         System.err.println("[ItemsAdder] [MemoryClassLoader] Class not found in memory: " + name);
         System.err.println("[ItemsAdder] [MemoryClassLoader] Available classes: " + this.aCK.keySet());
         return super.findClass(name);
      } else {
         return this.defineClass(name, var2, 0, var2.length);
      }
   }

   @MarkerAnnotation
   @Override
   public Class loadClass(String name, boolean resolve) {
      if (this.aCK.containsKey(name)) {
         byte[] var3 = (byte[])this.aCK.get(name);
         Class var4 = this.defineClass(name, var3, 0, var3.length);
         if (resolve) {
            this.resolveClass(var4);
         }

         return var4;
      } else {
         return super.loadClass(name, resolve);
      }
   }
}
