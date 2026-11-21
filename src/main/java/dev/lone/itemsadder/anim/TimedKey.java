package dev.lone.itemsadder.anim;

import org.jetbrains.annotations.Nullable;

public class TimedKey {
   private final long FA;
   @Nullable
   private final TimedKeyPayload FB;

   private TimedKey(long var1, @Nullable TimedKeyPayload var3) {
      this.FA = var1;
      this.FB = var3;
   }

   public long jN() {
      return this.FA;
   }

   @Nullable
   public TimedKeyPayload rk() {
      return this.FB;
   }
}
