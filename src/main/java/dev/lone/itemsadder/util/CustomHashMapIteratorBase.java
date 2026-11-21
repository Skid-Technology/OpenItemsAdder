package dev.lone.itemsadder.util;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

abstract class CustomHashMapIteratorBase {
   CustomHashMapNode bOu;
   CustomHashMapNode bOv;
   int bOw;
   int bwa;

   CustomHashMapIteratorBase(CustomHashMap var1) {
      this.bOx = var1;
      this.bOw = var1.bOo;
      CustomHashMapNode[] var2 = var1.bOm;
      this.bOv = this.bOu = null;
      this.bwa = 0;
      if (var2 != null && var1.XN > 0) {
         while (this.bwa < var2.length && (this.bOu = var2[this.bwa++]) == null) {
         }
      }
   }

   public final boolean hasNext() {
      return this.bOu != null;
   }

   final CustomHashMapNode nextNode() {
      CustomHashMapNode var2 = this.bOu;
      if (this.bOx.bOo != this.bOw) {
         throw new ConcurrentModificationException();
      }

      if (var2 == null) {
         throw new NoSuchElementException();
      }

      if ((this.bOu = (this.bOv = var2).bOu) == null) {
         CustomHashMapNode[] var1 = this.bOx.bOm;
         if (this.bOx.bOm != null) {
            while (this.bwa < var1.length && (this.bOu = var1[this.bwa++]) == null) {
            }
         }
      }

      return var2;
   }

   public final void remove() {
      CustomHashMapNode var1 = this.bOv;
      if (var1 == null) {
         throw new IllegalStateException();
      }

      if (this.bOx.bOo != this.bOw) {
         throw new ConcurrentModificationException();
      }

      this.bOv = null;
      this.bOx.removeNode(var1.bOC, var1.bOD, null, false, false);
      this.bOw = this.bOx.bOo;
   }
}
