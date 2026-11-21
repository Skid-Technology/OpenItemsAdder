package dev.lone.itemsadder.world;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.Arrays;

public final class PackedBlockStore {
   private final Long2IntOpenHashMap primaryIndex;
   private final Long2IntOpenHashMap secondaryIndex;
   final long[] slotKeys;
   final int[] slotStates;
   final int[] slotGeneration;
   final int[] slotPriorities;
   final boolean[] slotActive;
   final boolean[] slotScheduled;
   final boolean[] slotVisited;
   final byte[] slotLayers;
   final long[] heapKeys;
   final int[] heapValues;
   final byte[] heapLayers;
   final double[] heapDistances;
   final int[] heapIds;
   final int[] pendingRemovals;
   final LongOpenHashSet primaryKeys = new LongOpenHashSet();
   final LongOpenHashSet secondaryKeys = new LongOpenHashSet();
   private final int[] freeSlots;
   private int freeCount;
   private int generation;
   int heapSize;
   int removalCount;
   int queuedCount;

   PackedBlockStore(int var1) {
      this(var1, var1);
   }

   PackedBlockStore(int var1, int var2) {
      if (var1 >= 1 && var2 >= 1 && var2 <= var1) {
         this.primaryIndex = createIndex(var2);
         this.secondaryIndex = createIndex(var2);
         this.slotKeys = new long[var1];
         this.slotStates = new int[var1];
         Arrays.fill(this.slotStates, -1);
         this.slotGeneration = new int[var1];
         this.slotPriorities = new int[var1];
         this.slotActive = new boolean[var1];
         this.slotScheduled = new boolean[var1];
         this.slotVisited = new boolean[var1];
         this.slotLayers = new byte[var1];
         this.heapKeys = new long[var2];
         this.heapValues = new int[var2];
         this.heapLayers = new byte[var2];
         this.heapDistances = new double[var2];
         this.heapIds = new int[var2];
         this.pendingRemovals = new int[var1];
         this.freeSlots = new int[var1];

         for (int var3 = 0; var3 < var1; var3++) {
            this.freeSlots[var3] = var1 - var3 - 1;
         }

         this.freeCount = var1;
      } else {
         throw new IllegalArgumentException("ENTITY block viewer capacity must be positive.");
      }
   }

   void advanceGeneration() {
      if (++this.generation == 0) {
         Arrays.fill(this.slotGeneration, 0);
         this.generation = 1;
      }

      this.heapSize = 0;
   }

   void offerEntryDefault(long var1, int var3, double var4) {
      this.offerEntry(var1, var3, (byte)0, var4);
   }

   void offerEntry(long var1, int var3, byte var4, double var5) {
      if (this.heapSize < this.heapKeys.length) {
         int var7 = this.heapSize++;
         this.setHeapSlot(var7, var1, var3, var4, var5);
         this.siftUp(var7);
      } else if (!(var5 >= this.heapDistances[0])) {
         this.setHeapSlot(0, var1, var3, var4, var5);
         this.siftDown();
      }
   }

   private void setHeapSlot(int var1, long var2, int var4, byte var5, double var6) {
      this.heapKeys[var1] = var2;
      this.heapValues[var1] = var4;
      this.heapLayers[var1] = var5;
      this.heapDistances[var1] = var6;
   }

   private void siftUp(int var1) {
      while (var1 > 0) {
         int var2 = var1 - 1 >>> 1;
         if (this.heapDistances[var2] >= this.heapDistances[var1]) {
            return;
         }

         this.swapHeapSlots(var2, var1);
         var1 = var2;
      }
   }

   private void siftDown() {
      int var1 = 0;

      while (true) {
         int var2 = var1 * 2 + 1;
         if (var2 >= this.heapSize) {
            return;
         }

         int var3 = var2 + 1;
         int var4 = var3 < this.heapSize && this.heapDistances[var3] > this.heapDistances[var2] ? var3 : var2;
         if (this.heapDistances[var1] >= this.heapDistances[var4]) {
            return;
         }

         this.swapHeapSlots(var1, var4);
         var1 = var4;
      }
   }

   private void swapHeapSlots(int var1, int var2) {
      long var3 = this.heapKeys[var1];
      int var5 = this.heapValues[var1];
      byte var6 = this.heapLayers[var1];
      double var7 = this.heapDistances[var1];
      this.setHeapSlot(var1, this.heapKeys[var2], this.heapValues[var2], this.heapLayers[var2], this.heapDistances[var2]);
      this.setHeapSlot(var2, var3, var5, var6, var7);
   }

   int acquireSlot(long var1, int var3, int var4) {
      return this.acquireSlotWithLayer(var1, var3, (byte)0, var4);
   }

   int acquireSlotWithLayer(long var1, int var3, byte var4, int var5) {
      Long2IntOpenHashMap var6 = this.indexFor(var4);
      int var7 = var6.get(var1);
      if (var7 < 0) {
         if (this.freeCount == 0) {
            return -1;
         }

         var7 = this.freeSlots[--this.freeCount];
         var6.put(var1, var7);
         this.slotKeys[var7] = var1;
         this.slotActive[var7] = true;
         if (this.slotScheduled[var7]) {
            this.queuedCount--;
         }
      }

      this.assignSlot(var7, var3, var4, var5);
      return var7;
   }

   void assignSlot(int var1, int var2, byte var3, int var4) {
      this.slotLayers[var1] = var3;
      this.slotStates[var1] = var2;
      this.slotVisited[var1] = false;
      this.slotGeneration[var1] = this.generation;
      this.slotPriorities[var1] = var4;
   }

   boolean isSlotCurrent(int var1) {
      return this.slotGeneration[var1] == this.generation;
   }

   void releaseSlot(int var1, int var2) {
      this.removeKeyMapping(var1);
      this.slotActive[var1] = false;
      this.slotVisited[var1] = false;
      this.slotPriorities[var1] = var2;
      if (this.slotScheduled[var1]) {
         this.queuedCount++;
      }

      this.freeSlots[this.freeCount++] = var1;
   }

   int replaceSlot(int var1, long var2, int var4, int var5) {
      return this.replaceSlotWithLayer(var1, var2, var4, (byte)0, var5);
   }

   int replaceSlotWithLayer(int var1, long var2, int var4, byte var5, int var6) {
      int var7 = this.slotStates[var1];
      this.removeKeyMapping(var1);
      this.indexFor(var5).put(var2, var1);
      this.slotKeys[var1] = var2;
      this.slotLayers[var1] = var5;
      this.slotStates[var1] = var4;
      this.slotVisited[var1] = false;
      this.slotGeneration[var1] = this.generation;
      this.slotPriorities[var1] = var6;
      return var7;
   }

   boolean markVisited(int var1) {
      if (this.slotVisited[var1]) {
         return false;
      }

      this.slotVisited[var1] = true;
      this.slotGeneration[var1] = this.generation;
      return true;
   }

   private void removeKeyMapping(int var1) {
      long var2 = this.slotKeys[var1];
      Long2IntOpenHashMap var4 = this.indexFor(this.slotLayers[var1]);
      if (var4.get(var2) == var1) {
         var4.remove(var2);
      }
   }

   int slotOf(long var1) {
      return this.slotOfWithLayer(var1, (byte)0);
   }

   int slotOfWithLayer(long var1, byte var3) {
      return this.indexFor(var3).get(var1);
   }

   void unlinkSlot(int var1, int var2) {
      if (this.slotScheduled[var1]) {
         this.slotScheduled[var1] = false;
         this.slotStates[var1] = -1;
         if (!this.slotActive[var1]) {
            this.queuedCount--;
         }

         this.pendingRemovals[this.removalCount++] = var2;
      }
   }

   boolean containsKey(long var1) {
      return this.containsKeyWithLayer(var1, (byte)0);
   }

   boolean containsKeyWithLayer(long var1, byte var3) {
      return this.slotOfWithLayer(var1, var3) >= 0;
   }

   int capacity() {
      return this.slotKeys.length;
   }

   int size() {
      return this.primaryIndex.size() + this.secondaryIndex.size();
   }

   void clear() {
      this.primaryIndex.clear();
      this.secondaryIndex.clear();
      Arrays.fill(this.slotActive, false);
      Arrays.fill(this.slotScheduled, false);
      Arrays.fill(this.slotVisited, false);
      Arrays.fill(this.slotStates, -1);

      for (int var1 = 0; var1 < this.freeSlots.length; var1++) {
         this.freeSlots[var1] = this.freeSlots.length - var1 - 1;
      }

      this.freeCount = this.freeSlots.length;
      this.heapSize = 0;
      this.removalCount = 0;
      this.queuedCount = 0;
      this.primaryKeys.clear();
      this.secondaryKeys.clear();
   }

   private Long2IntOpenHashMap indexFor(byte var1) {
      return var1 == 0 ? this.primaryIndex : this.secondaryIndex;
   }

   private static Long2IntOpenHashMap createIndex(int var0) {
      Long2IntOpenHashMap var1 = new Long2IntOpenHashMap(var0);
      var1.defaultReturnValue(-1);
      return var1;
   }
}
