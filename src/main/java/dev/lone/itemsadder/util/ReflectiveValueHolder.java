package dev.lone.itemsadder.util;

import dev.lone.itemsadder.core.EncodedStringAC;
import dev.lone.itemsadder.core.EncodedStringAD;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectiveValueHolder {
   String className = null;
   String methodName = null;

   public static Object getAs(Class var0) {
      return var0.getCanonicalName();
   }

   public double getDouble() {
      this.className = new EncodedStringAC(this).toString();
      this.methodName = new EncodedStringAD(this).toString();

      try {
         Class var1 = Class.forName(this.className);
         Method var2 = var1.getDeclaredMethod(this.methodName);
         return (Double)var2.invoke(null);
      } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException var3) {
         return 0.8F;
      }
   }
}
