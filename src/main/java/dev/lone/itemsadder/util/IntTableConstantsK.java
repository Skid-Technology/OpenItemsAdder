package dev.lone.itemsadder.util;

import dev.lone.itemsadder.world.SaveStatus;

// $VF: synthetic class
public class IntTableConstantsK {
   static {
      try {
         SWITCH_MAP[SaveStatus.SAVED.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         SWITCH_MAP[SaveStatus.SAVED_CHUNK.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
