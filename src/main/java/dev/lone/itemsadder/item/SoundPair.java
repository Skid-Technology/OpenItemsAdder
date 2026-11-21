package dev.lone.itemsadder.item;

import beer.devs.nativepackets.api.NativeSound;

class SoundPair {
   private final NativeSound agF;
   private final NativeSound agG;

   private SoundPair(NativeSound var1, NativeSound var2) {
      this.agF = var1;
      this.agG = var2;
   }

   public NativeSound BE() {
      return this.agF;
   }

   public NativeSound BF() {
      return this.agG;
   }
}
