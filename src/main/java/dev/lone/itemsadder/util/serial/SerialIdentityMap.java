package dev.lone.itemsadder.util.serial;

import java.util.Map.Entry;

final class SerialIdentityMap {
   private static final int bRe = 32;
   private static final int bRf = 4;
   private static final int bRg = 536870912;
   private transient Object[] entries;
   private int size;
   private transient int bOp;

   public SerialIdentityMap() {
      this.grow(32);
   }

   public SerialIdentityMap(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("expectedMaxSize is negative: " + var1);
      }

      this.grow(this.hashIndex(var1));
   }

   private int hashIndex(int var1) {
      int var2 = 3 * var1 / 2;
      int var3;
      if (var2 <= 536870912 && var2 >= 0) {
         var3 = 4;

         while (var3 < var2) {
            var3 <<= 1;
         }
      } else {
         var3 = 536870912;
      }

      return var3;
   }

   private void grow(int var1) {
      this.bOp = var1 * 2 / 3;
      this.entries = new Object[2 * var1];
   }

   private static int identityIndexOf(Object var0, int var1) {
      int var2 = System.identityHashCode(var0);
      return (var2 << 1) - (var2 << 8) & var1 - 1;
   }

   private static int mix(int var0, int var1) {
      return var0 + 2 < var1 ? var0 + 2 : 0;
   }

   public boolean writeIdentity(int var1, Object var2, SerialWriterBase var3, int var4) {
      Object[] var5 = this.entries;
      int var6 = var5.length;

      int var7;
      Object var8;
      for (var7 = identityIndexOf(var2, var6); (var8 = var5[var7]) != null; var7 = mix(var7, var6)) {
         if (var8 == var2) {
            if (var2 instanceof Entry && var2.getClass().getName().startsWith("java.util")) {
               return true;
            }

            var3.cursor = var3.mode.method252((Integer)var5[var7 + 1], var3, var3.mode.method252(WireTypes.makeTag(var4, 6), var3, var3.cursor));
            return false;
         }
      }

      var5[var7] = var2;
      var5[var7 + 1] = var1;
      if (++this.size >= this.bOp) {
         this.rehash(var6);
      }

      return true;
   }

   private void rehash(int var1) {
      int var2 = var1 * 2;
      Object[] var3 = this.entries;
      int var4 = var3.length;
      if (var4 == 1073741824) {
         if (this.bOp == 536870911) {
            throw new IllegalStateException("Capacity exhausted.");
         }

         this.bOp = 536870911;
      } else if (var4 < var2) {
         Object[] var5 = new Object[var2];
         this.bOp = var2 / 3;

         for (byte var6 = 0; var6 < var4; var6 += 2) {
            Object var7 = var3[var6];
            if (var7 != null) {
               Object var8 = var3[var6 + 1];
               var3[var6] = null;
               var3[var6 + 1] = null;
               int var9 = identityIndexOf(var7, var2);

               while (var5[var9] != null) {
                  var9 = mix(var9, var2);
               }

               var5[var9] = var7;
               var5[var9 + 1] = var8;
            }
         }

         this.entries = var5;
      }
   }
}
