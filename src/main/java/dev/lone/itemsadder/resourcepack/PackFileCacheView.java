package dev.lone.itemsadder.resourcepack;

import java.util.AbstractSet;
import java.util.Iterator;

class PackFileCacheView extends AbstractSet {
   PackFileCacheView(PackFileCache var1) {
      this.bwt = var1;
   }

   @Override
   public int size() {
      return this.bwt.bwr.size();
   }

   @Override
   public Iterator iterator() {
      Iterator var1 = this.bwt.bwr.keySet().iterator();
      return new PackFileCacheIterator(this, var1);
   }
}
