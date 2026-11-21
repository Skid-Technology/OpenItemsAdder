package dev.lone.itemsadder.util;

import java.util.Iterator;
import java.util.Map.Entry;

final class CustomHashMapEntryIterator extends CustomHashMapIteratorBase implements Iterator {
   CustomHashMapEntryIterator(CustomHashMap var1) {
      super(var1);
   }

   public final Entry nextEntry() {
      return this.nextNode();
   }
}
