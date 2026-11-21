package dev.lone.itemsadder.util;

class LockFreeListNode {
   private volatile LockFreeListNode next;
   private int size;
   private Object value;

   private LockFreeListNode() {
   }
}
