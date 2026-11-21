package dev.lone.itemsadder.util;

import java.util.AbstractCollection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

final class CustomHashMapValues extends AbstractCollection {
   CustomHashMapValues(CustomHashMap var1) {
      this.bOJ = var1;
   }

   @Override
   public final int size() {
      return this.bOJ.XN;
   }

   @Override
   public final void clear() {
      this.bOJ.clear();
   }

   @Override
   public final Iterator iterator() {
      return new CustomHashMapValueIterator(this.bOJ);
   }

   @Override
   public final boolean contains(Object o) {
      return this.bOJ.containsValue(o);
   }

   @Override
   public final Spliterator spliterator() {
      return new CustomHashMapValueSpliterator(this.bOJ, 0, -1, 0, 0);
   }

   @Override
   public Object[] toArray() {
      return this.bOJ.method295(new Object[this.bOJ.XN]);
   }

   @Override
   public Object[] toArray(Object[] a) {
      return this.bOJ.method295(this.bOJ.method293(a));
   }

   @Override
   public final void forEach(Consumer action) {
      if (action == null) {
         throw new NullPointerException();
      }

      if (this.bOJ.XN > 0) {
         CustomHashMapNode[] var2 = this.bOJ.bOm;
         if (this.bOJ.bOm != null) {
            int var3 = this.bOJ.bOo;
            CustomHashMapNode[] var4 = var2;
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; var6++) {
               for (CustomHashMapNode var7 = var4[var6]; var7 != null; var7 = var7.bOu) {
                  action.accept(var7.auj);
               }
            }

            if (this.bOJ.bOo != var3) {
               throw new ConcurrentModificationException();
            }
         }
      }
   }
}
