package dev.lone.itemsadder.util;

import java.util.Iterator;

final class CustomHashMapValueIterator extends CustomHashMapIteratorBase implements Iterator {
   CustomHashMapValueIterator(CustomHashMap var1) {
      super(var1);
   }

   @Override
   public final Object next() {
      return this.nextNode().auj;
   }
}
