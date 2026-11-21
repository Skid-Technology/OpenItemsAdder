package dev.lone.itemsadder.util;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicGate {
   private final AtomicInteger state = new AtomicInteger();

   public boolean consumeTask() {
      int var1 = this.state.getAndDecrement();
      if (var1 == WorkerState.RUNNING_GOT_TASKS.ordinal()) {
         return true;
      } else if (var1 == WorkerState.RUNNING_NO_TASKS.ordinal()) {
         return false;
      } else {
         throw new AssertionError();
      }
   }

   public boolean tryOpen() {
      if (this.state.get() == WorkerState.RUNNING_GOT_TASKS.ordinal()) {
         return false;
      }

      int var1 = this.state.getAndSet(WorkerState.RUNNING_GOT_TASKS.ordinal());
      return var1 == WorkerState.WAITING.ordinal();
   }
}
