package dev.lone.itemsadder.util;

public final class HashSeeds {
   private static final long[] SEEDS = new long[]{7640891575438662147L};

   private HashSeeds() {
   }

   static long seed() {
      return SEEDS[0] ^ 7640891576956012809L;
   }
}
