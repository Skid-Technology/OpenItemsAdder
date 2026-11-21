package dev.lone.itemsadder.entity;

import java.lang.reflect.Field;

class EntityTypeFieldLocator extends ClassValue {
   protected Field findField(Class var1) {
      for (Class var2 = var1; var2 != null; var2 = var2.getSuperclass()) {
         try {
            Field var3 = var2.getDeclaredField("entityType");
            var3.setAccessible(true);
            return var3;
         } catch (NoSuchFieldException var4) {
         }
      }

      throw new IllegalStateException("Cannot identify Bukkit model part type: " + var1.getName());
   }
}
