package dev.lone.itemsadder.util;

import java.util.ConcurrentModificationException;
import java.util.Spliterator;
import java.util.function.Consumer;

final class CustomHashMapEntrySpliterator extends CustomHashMapSpliteratorBase implements Spliterator {
   CustomHashMapEntrySpliterator(CustomHashMap var1, int var2, int var3, int var4, int var5) {
      super(var1, var2, var3, var4, var5);
   }

   public CustomHashMapEntrySpliterator adl() {
      int var1 = this.adk();
      int var2 = this.bwa;
      int var3 = var2 + var1 >>> 1;
      return var2 < var3 && this.bOv == null ? new CustomHashMapEntrySpliterator(this.bOy, var2, this.bwa = var3, this.bOA >>>= 1, this.bOw) : null;
   }

   @Override
   public void forEachRemaining(Consumer action) {
      if (action == null) {
         throw new NullPointerException();
      }

      CustomHashMap var5 = this.bOy;
      CustomHashMapNode[] var6 = var5.bOm;
      int var3 = this.bOz;
      int var4;
      if (this.bOz < 0) {
         var4 = this.bOw = var5.bOo;
         var3 = this.bOz = var6 == null ? 0 : var6.length;
      } else {
         var4 = this.bOw;
      }

      if (var6 != null && var6.length >= var3) {
         int var2 = this.bwa;
         if (this.bwa >= 0 && (var2 < (this.bwa = var3) || this.bOv != null)) {
            CustomHashMapNode var7 = this.bOv;
            this.bOv = null;

            do {
               if (var7 == null) {
                  var7 = var6[var2++];
               } else {
                  action.accept(var7.bOD);
                  var7 = var7.bOu;
               }
            } while (var7 != null || var2 < var3);

            if (var5.bOo != var4) {
               throw new ConcurrentModificationException();
            }
         }
      }
   }

   @Override
   public boolean tryAdvance(Consumer action) {
      if (action == null) {
         throw new NullPointerException();
      }

      CustomHashMapNode[] var3 = this.bOy.bOm;
      int var2;
      if (var3 != null && var3.length >= (var2 = this.adk()) && this.bwa >= 0) {
         while (this.bOv != null || this.bwa < var2) {
            if (this.bOv != null) {
               Object var4 = this.bOv.bOD;
               this.bOv = this.bOv.bOu;
               action.accept(var4);
               if (this.bOy.bOo != this.bOw) {
                  throw new ConcurrentModificationException();
               }

               return true;
            }

            this.bOv = var3[this.bwa++];
         }
      }

      return false;
   }

   @Override
   public int characteristics() {
      return (this.bOz >= 0 && this.bOA != this.bOy.XN ? 0 : 64) | 1;
   }
}
