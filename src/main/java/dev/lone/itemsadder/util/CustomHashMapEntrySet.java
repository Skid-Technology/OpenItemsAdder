package dev.lone.itemsadder.util;

import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

final class CustomHashMapEntrySet extends AbstractSet {
   CustomHashMapEntrySet(CustomHashMap var1) {
      this.bOB = var1;
   }

   @Override
   public final int size() {
      return this.bOB.XN;
   }

   @Override
   public final void clear() {
      this.bOB.clear();
   }

   @Override
   public final Iterator iterator() {
      return new CustomHashMapKeyIterator(this.bOB);
   }

   @Override
   public final boolean contains(Object o) {
      return this.bOB.containsKey(o);
   }

   @Override
   public final boolean remove(Object key) {
      return this.bOB.removeNode(CustomHashMap.hash(key), key, null, false, true) != null;
   }

   @Override
   public final Spliterator spliterator() {
      return new CustomHashMapEntrySpliterator(this.bOB, 0, -1, 0, 0);
   }

   @Override
   public Object[] toArray() {
      return this.bOB.method294(new Object[this.bOB.XN]);
   }

   @Override
   public Object[] toArray(Object[] a) {
      return this.bOB.method294(this.bOB.method293(a));
   }

   @Override
   public final void forEach(Consumer action) {
      if (action == null) {
         throw new NullPointerException();
      }

      if (this.bOB.XN > 0) {
         CustomHashMapNode[] var2 = this.bOB.bOm;
         if (this.bOB.bOm != null) {
            int var3 = this.bOB.bOo;
            CustomHashMapNode[] var4 = var2;
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; var6++) {
               for (CustomHashMapNode var7 = var4[var6]; var7 != null; var7 = var7.bOu) {
                  action.accept(var7.bOD);
               }
            }

            if (this.bOB.bOo != var3) {
               throw new ConcurrentModificationException();
            }
         }
      }
   }
}
