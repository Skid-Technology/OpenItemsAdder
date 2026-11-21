package dev.lone.itemsadder.util;

import java.util.Iterator;

final class CustomHashMapKeyIterator extends CustomHashMapIteratorBase implements Iterator {
   CustomHashMapKeyIterator(CustomHashMap var1) {
      super(var1);
   }

   @Override
   public final Object next() {
      return this.nextNode().bOD;
   }
}
