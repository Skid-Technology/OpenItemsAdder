package dev.lone.itemsadder.anim;

import org.jetbrains.annotations.Nullable;

public class TimedKeyPayload {
   private final long FC;
   @Nullable
   private final String FD;
   private final boolean FE;

   private TimedKeyPayload(long var1, @Nullable String var3, boolean var4) {
      this.FC = var1;
      this.FD = var3;
      this.FE = var4;
   }

   public long jN() {
      return this.FC;
   }

   @Nullable
   public String rl() {
      return this.FD;
   }

   public boolean rm() {
      return this.FE;
   }
}
