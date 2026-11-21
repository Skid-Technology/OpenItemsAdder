package dev.lone.itemsadder.util;

import java.lang.reflect.Method;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemMetaReflection {
   private static final Method HAS_EQUIPPABLE;
   private static final Method GET_EQUIPPABLE;
   private static final Method GET_MODEL;

   private ItemMetaReflection() {
   }

   static void ensureInitialized() {
   }

   static boolean hasEquippable(ItemMeta var0) {
      try {
         return (Boolean)HAS_EQUIPPABLE.invoke(var0);
      } catch (ReflectiveOperationException var2) {
         throw new IllegalStateException(var2);
      }
   }

   static Object getEquippable(ItemMeta var0) {
      try {
         return GET_EQUIPPABLE.invoke(var0);
      } catch (ReflectiveOperationException var2) {
         throw new IllegalStateException(var2);
      }
   }

   static NamespacedKey getEquippableModel(Object var0) {
      try {
         return (NamespacedKey)GET_MODEL.invoke(var0);
      } catch (ReflectiveOperationException var2) {
         throw new IllegalStateException(var2);
      }
   }

   static {
      try {
         HAS_EQUIPPABLE = ItemMeta.class.getMethod("hasEquippable");
         GET_EQUIPPABLE = ItemMeta.class.getMethod("getEquippable");
         Class var0 = GET_EQUIPPABLE.getReturnType();
         GET_MODEL = var0.getMethod("getModel");
      } catch (ReflectiveOperationException var1) {
         throw new IllegalStateException("Missing Bukkit equippable API", var1);
      }
   }
}
