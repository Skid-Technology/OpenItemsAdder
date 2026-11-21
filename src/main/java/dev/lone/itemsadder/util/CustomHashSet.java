package dev.lone.itemsadder.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;

public class CustomHashSet extends AbstractSet implements Cloneable, Set {
   private transient HashMap map;

   public CustomHashSet() {
      this.map = new HashMap();
   }

   public CustomHashSet(Collection var1) {
      this.map = new HashMap(Math.max((int)(var1.size() / 0.75F) + 1, 16));
      this.addAll(var1);
   }

   public CustomHashSet(int var1, float var2) {
      this.map = new HashMap(var1, var2);
   }

   public CustomHashSet(int var1) {
      this.map = new HashMap(var1);
   }

   @Override
   public Iterator iterator() {
      return this.map.values().iterator();
   }

   @Override
   public int size() {
      return this.map.size();
   }

   @Override
   public boolean isEmpty() {
      return this.map.isEmpty();
   }

   @Override
   public boolean contains(Object o) {
      return this.map.containsKey(o.hashCode());
   }

   @Override
   public boolean add(Object e) {
      return this.map.put(e.hashCode(), e) == null;
   }

   @Override
   public boolean remove(Object o) {
      return this.map.remove(o.hashCode()) != null;
   }

   @Override
   public void clear() {
      this.map.clear();
   }

   @Override
   public Object clone() {
      try {
         CustomHashSet var1 = (CustomHashSet)super.clone();
         var1.map = (HashMap)this.map.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new InternalError(var2);
      }
   }

   @Override
   public Spliterator spliterator() {
      throw new RuntimeException("Not implemented");
   }

   @Override
   public Object[] toArray() {
      throw new RuntimeException("Not implemented");
   }

   @Override
   public Object[] toArray(Object[] a) {
      throw new RuntimeException("Not implemented");
   }
}
