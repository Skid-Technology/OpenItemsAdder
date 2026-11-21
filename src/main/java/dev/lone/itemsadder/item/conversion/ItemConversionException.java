package dev.lone.itemsadder.item.conversion;

import dev.lone.itemsadder.util.Log;

public class ItemConversionException extends Exception {
   private final boolean atd;
   private final boolean ate;

   public ItemConversionException(String var1) {
      super(var1);
      this.atd = false;
      this.ate = false;
   }

   public ItemConversionException(String var1, boolean var2, boolean var3) {
      super(var1);
      this.atd = var2;
      this.ate = var3;
   }

   public ItemConversionException(String var1, boolean var2) {
      super(var1);
      this.atd = var2;
      this.ate = false;
   }

   public ItemConversionException(String var1, Throwable var2) {
      super(var1, var2);
      this.atd = false;
      this.ate = false;
   }

   public static void method333(String var0, ItemConversionException var1) {
      if (var1.ate) {
         Log.devLogWithTrace(var0, var1);
      } else {
         Log.error(var0, var1);
      }
   }

   public static void method334(String var0, ItemConversionException var1) {
      if (var1.ate) {
         Log.devLog(var0);
      } else {
         Log.warn(var0);
      }
   }

   public boolean GR() {
      return this.atd;
   }

   public boolean GS() {
      return this.ate;
   }
}
