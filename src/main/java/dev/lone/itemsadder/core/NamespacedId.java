package dev.lone.itemsadder.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NamespacedId {
   public String dk;
   public String dl;

   public NamespacedId(String var1, String var2) {
      this.dk = var1;
      this.dl = var2;
   }

   public String getNamespacedID() {
      return this.dk + ":" + this.dl;
   }

   public String getNamespace() {
      return this.dk;
   }

   public void method383(String var1) {
      this.dk = var1;
   }

   public String getId() {
      return this.dl;
   }

   public void method384(String var1) {
      this.dl = var1;
   }

   @Override
   public String toString() {
      return this.dk + ":" + this.dl;
   }

   @Nullable
   public static String method385(String var0, @NotNull String var1) {
      NamespacedId var2 = method386(var0, var1);
      return var2 == null ? null : var2.toString();
   }

   @Nullable
   public static NamespacedId method386(String var0, @NotNull String var1) {
      String[] var2 = var0.split(":", 3);
      if (var2.length > 2) {
         return null;
      } else {
         String var3 = var2.length == 2 ? var2[1] : "";
         if (var2.length == 1) {
            String var5 = var2[0];
            return !var5.isEmpty() && method388(var5) ? new NamespacedId(var1, var5) : null;
         } else if (var2.length == 2 && !method388(var3)) {
            return null;
         } else {
            String var4 = var2[0];
            if (var4.isEmpty()) {
               return new NamespacedId(var1, var3);
            } else {
               return !method387(var4) ? null : new NamespacedId(var4, var3);
            }
         }
      }
   }

   private static boolean method387(String var0) {
      int var1 = var0.length();
      if (var1 == 0) {
         return false;
      }

      for (int var2 = 0; var2 < var1; var2++) {
         if (!method390(var0.charAt(var2))) {
            return false;
         }
      }

      return true;
   }

   private static boolean method388(String var0) {
      int var1 = var0.length();
      if (var1 == 0) {
         return false;
      }

      for (int var2 = 0; var2 < var1; var2++) {
         if (!method389(var0.charAt(var2))) {
            return false;
         }
      }

      return true;
   }

   private static boolean method389(char var0) {
      return method390(var0) || var0 == '/';
   }

   private static boolean method390(char var0) {
      return var0 >= 'a' && var0 <= 'z' || var0 >= '0' && var0 <= '9' || var0 == '.' || var0 == '_' || var0 == '-';
   }
}
