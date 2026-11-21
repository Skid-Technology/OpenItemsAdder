package dev.lone.itemsadder.util.serial;

import java.util.AbstractList;
import java.util.List;

public final class MappedList extends AbstractList {
   private final List source;
   private final ListMapper mapper;

   public MappedList(List var1, ListMapper var2) {
      this.source = var1;
      this.mapper = var2;
   }

   @Override
   public Object get(int index) {
      return this.mapper.map(this.source.get(index));
   }

   @Override
   public int size() {
      return this.source.size();
   }
}
