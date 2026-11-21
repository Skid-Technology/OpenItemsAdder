package dev.lone.itemsadder.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeList {
   private static final LockFreeListNode SENTINEL = new LockFreeListNode();
   private final AtomicReference head = new AtomicReference<>(SENTINEL);

   public boolean add(Object value) {
      LockFreeListNode var2 = new LockFreeListNode();
      var2.value = value;

      LockFreeListNode var3;
      do {
         var3 = (LockFreeListNode)this.head.get();
         var2.next = var3;
         var2.size = var3.size + 1;
      } while (!this.head.compareAndSet(var3, var2));

      return true;
   }

   public int size() {
      return ((LockFreeListNode)this.head.get()).size;
   }

   public List toList() {
      ArrayList var1 = new ArrayList(this.size() + 100);

      LockFreeListNode var2;
      do {
         var2 = (LockFreeListNode)this.head.get();
      } while (!this.head.compareAndSet(var2, SENTINEL));

      while (var2 != SENTINEL) {
         var1.add(var2.value);
         var2 = var2.next;
      }

      return var1;
   }
}
