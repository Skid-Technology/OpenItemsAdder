package dev.lone.itemsadder.util.serial;

public final class VarIntCodec {
   private VarIntCodec() {
   }

   public static int readVarInt32(byte[] var0, int var1, int var2, int var3) {
      if (var2 == 0) {
         throw new NumberFormatException(Base64Helper.encodeRange(var0, var1, var2));
      }

      if (var0[var1] == 45) {
         if (var2 == 1) {
            throw new NumberFormatException(Base64Helper.encodeRange(var0, var1, var2));
         } else {
            return readVarInt32Checked(var0, var1 + 1, var2 - 1, var3, false);
         }
      } else {
         return readVarInt32Checked(var0, var1, var2, var3, true);
      }
   }

   static int readVarInt32Checked(byte[] var0, int var1, int var2, int var3, boolean var4) {
      int var5 = Integer.MIN_VALUE / var3;
      int var6 = 0;
      int var7 = var1;
      int var8 = var1 + var2;

      while (var7 < var8) {
         int var9 = Character.digit(var0[var7++], var3);
         if (var9 == -1) {
            throw new NumberFormatException(Base64Helper.encodeRange(var0, var1, var2));
         }

         if (var5 > var6) {
            throw new NumberFormatException(Base64Helper.encodeRange(var0, var1, var2));
         }

         int var10 = var6 * var3 - var9;
         if (var10 > var6) {
            throw new NumberFormatException(Base64Helper.encodeRange(var0, var1, var2));
         }

         var6 = var10;
      }

      if (var4) {
         var6 = -var6;
         if (var6 < 0) {
            throw new NumberFormatException(Base64Helper.encodeRange(var0, var1, var2));
         }
      }

      return var6;
   }

   public static long readVarInt64(byte[] var0, int var1, int var2, int var3) {
      if (var2 == 0) {
         throw new NumberFormatException(Base64Helper.encodeRange(var0, var1, var2));
      }

      if (var0[var1] == 45) {
         if (var2 == 1) {
            throw new NumberFormatException(Base64Helper.encodeRange(var0, var1, var2));
         } else {
            return readVarInt64Checked(var0, var1 + 1, var2 - 1, var3, false);
         }
      } else {
         return readVarInt64Checked(var0, var1, var2, var3, true);
      }
   }

   static long readVarInt64Checked(byte[] var0, int var1, int var2, int var3, boolean var4) {
      long var5 = Long.MIN_VALUE / var3;
      long var7 = 0L;
      int var9 = var1;
      int var10 = var1 + var2;

      while (var9 < var10) {
         int var11 = Character.digit(var0[var9++], var3);
         if (var11 == -1) {
            throw new NumberFormatException(Base64Helper.encodeRange(var0, var1, var2));
         }

         if (var5 > var7) {
            throw new NumberFormatException(Base64Helper.encodeRange(var0, var1, var2));
         }

         long var12 = var7 * var3 - var11;
         if (var12 > var7) {
            throw new NumberFormatException(Base64Helper.encodeRange(var0, var1, var2));
         }

         var7 = var12;
      }

      if (var4) {
         var7 = -var7;
         if (var7 < 0L) {
            throw new NumberFormatException(Base64Helper.encodeRange(var0, var1, var2));
         }
      }

      return var7;
   }
}
