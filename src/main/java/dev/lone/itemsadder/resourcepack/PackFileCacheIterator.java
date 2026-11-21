package dev.lone.itemsadder.resourcepack;

import java.util.Iterator;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;

class PackFileCacheIterator implements Iterator {
   PackFileCacheIterator(PackFileCacheView var1, Iterator var2) {
      this.bwu = var2;
      this.bwv = var1;
   }

   @Override
   public boolean hasNext() {
      return this.bwu.hasNext();
   }

   public Entry nextEntry() {
      String var1 = (String)this.bwu.next();
      return new SimpleImmutableEntry<>(var1, this.bwv.bwt.getJson(var1));
   }

   @Override
   public void remove() {
      this.bwu.remove();
   }
}
