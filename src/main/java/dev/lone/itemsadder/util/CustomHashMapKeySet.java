package dev.lone.itemsadder.util;

import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Map.Entry;
import java.util.function.Consumer;

final class CustomHashMapKeySet extends AbstractSet {
   CustomHashMapKeySet(CustomHashMap var1) {
      this.bOt = var1;
   }

   @Override
   public final int size() {
      return this.bOt.XN;
   }

   @Override
   public final void clear() {
      this.bOt.clear();
   }

   @Override
   public final Iterator iterator() {
      return new CustomHashMapEntryIterator(this.bOt);
   }

   @Override
   public final boolean contains(Object o) {
      if (!(o instanceof Entry var2)) {
         return false;
      } else {
         Object var3 = var2.getKey();
         CustomHashMapNode var4 = this.bOt.getNode(var3);
         return var4 != null && var4.equals(var2);
      }
   }

   @Override
   public final boolean remove(Object o) {
      if (o instanceof Entry var2) {
         Object var3 = var2.getKey();
         Object var4 = var2.getValue();
         return this.bOt.removeNode(CustomHashMap.hash(var3), var3, var4, true, true) != null;
      } else {
         return false;
      }
   }

   @Override
   public final Spliterator spliterator() {
      return new CustomHashMapKeySpliterator(this.bOt, 0, -1, 0, 0);
   }

   @Override
   public final void forEach(Consumer action) {
      if (action == null) {
         throw new NullPointerException();
      }

      if (this.bOt.XN > 0) {
         CustomHashMapNode[] var2 = this.bOt.bOm;
         if (this.bOt.bOm != null) {
            int var3 = this.bOt.bOo;
            CustomHashMapNode[] var4 = var2;
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; var6++) {
               for (CustomHashMapNode var7 = var4[var6]; var7 != null; var7 = var7.bOu) {
                  action.accept(var7);
               }
            }

            if (this.bOt.bOo != var3) {
               throw new ConcurrentModificationException();
            }
         }
      }
   }
}
