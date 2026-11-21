package dev.lone.itemsadder.item;

import dev.lone.itemsadder.util.LongPair;
import dev.lone.itemsadder.util.RateLimiter;

public class ThrowInputState {
   private final boolean afO;
   private final boolean afP;
   private final boolean afQ;
   private final boolean afR;
   private final boolean afS;
   private final boolean afT;
   private final boolean afU;
   private final long afV;
   private final LongPair afW;

   public ThrowInputState(boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      this(var1, var2, var3, var4, var5, var6, false, 0L, null);
   }

   public ThrowInputState(boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, long var8, LongPair var10) {
      this.afO = var1;
      this.afP = var2;
      this.afQ = var3;
      this.afR = var4;
      this.afS = var5;
      this.afT = var6;
      this.afU = var7;
      this.afV = var8;
      this.afW = var10;
   }

   public static ThrowInputState copyWithFlags(
      ThrowInputState var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6, long var7
   ) {
      boolean var9 = var0 != null && var0.afU;
      long var10 = var0 == null ? 0L : var0.afV;
      LongPair var12 = var0 == null ? null : var0.afW;
      if (var6 && (var0 == null || !var0.afT)) {
         if (var9 && RateLimiter.isWithinWindow(var10, var7)) {
            var12 = new LongPair(var10, var7);
            var9 = false;
         } else {
            var9 = true;
            var10 = var7;
         }
      }

      return new ThrowInputState(var1, var2, var3, var4, var5, var6, var9, var10, var12);
   }

   public boolean iq() {
      return this.afO || this.afP || this.afQ || this.afR;
   }

   public boolean kL() {
      return this.afO;
   }

   public boolean kM() {
      return this.afP;
   }

   public boolean kN() {
      return this.afQ;
   }

   public boolean kO() {
      return this.afR;
   }

   public boolean Bi() {
      return this.afS;
   }

   public boolean Bk() {
      return this.afT;
   }

   public boolean Bl() {
      return this.afU;
   }

   public long Bm() {
      return this.afV;
   }

   public LongPair Bn() {
      return this.afW;
   }
}
