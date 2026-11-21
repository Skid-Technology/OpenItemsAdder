package dev.lone.itemsadder.anim;

import java.util.LinkedHashSet;

public final class EntityLifecycleTracker {
   private EntityLifecycleState tD = EntityLifecycleState.NEW;
   private long tE;
   private final LinkedHashSet tF = new LinkedHashSet();

   void jS() {
      if (this.tD != EntityLifecycleState.NEW) {
         throw new IllegalStateException("Custom entity lifecycle is already " + this.tD);
      }

      this.tD = EntityLifecycleState.ACTIVE;
   }

   boolean jT() {
      return this.tD == EntityLifecycleState.NEW || this.tD == EntityLifecycleState.ACTIVE;
   }

   boolean hz() {
      if (this.tD != EntityLifecycleState.DISPOSING && this.tD != EntityLifecycleState.DISPOSED) {
         this.tD = EntityLifecycleState.DISPOSING;
         this.tE++;
         return true;
      } else {
         return false;
      }
   }

   void jU() {
      this.tF.clear();
      this.tD = EntityLifecycleState.DISPOSED;
   }

   boolean method8549(int var1) {
      return this.jT() && this.tF.add(var1);
   }

   int[] jV() {
      return this.tF.stream().mapToInt(Integer::intValue).toArray();
   }

   void jW() {
      this.tF.clear();
   }

   EntityLifecycleState jX() {
      return this.tD;
   }

   long jY() {
      return this.tE;
   }
}
