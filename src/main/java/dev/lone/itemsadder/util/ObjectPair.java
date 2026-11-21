package dev.lone.itemsadder.util;

public class ObjectPair {
   public final Object first;
   public final Object second;

   public ObjectPair(Object var1, Object var2) {
      this.first = var1;
      this.second = var2;
   }

   public static ObjectPair of(Object var0, Object var1) {
      return new ObjectPair(var0, var1);
   }
}
