package dev.lone.itemsadder.util;

import dev.lone.itemsadder.block.BlockRealizationMode;

// $VF: synthetic class
public class IntTableConstantsT {
   static {
      try {
         SWITCH_MAP[BlockRealizationMode.REAL.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         SWITCH_MAP[BlockRealizationMode.REAL_TRANSPARENT.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      try {
         SWITCH_MAP[BlockRealizationMode.REAL_NOTE.ordinal()] = 3;
      } catch (NoSuchFieldError var3) {
      }

      try {
         SWITCH_MAP[BlockRealizationMode.ENTITY.ordinal()] = 4;
      } catch (NoSuchFieldError var2) {
      }

      try {
         SWITCH_MAP[BlockRealizationMode.TILE.ordinal()] = 5;
      } catch (NoSuchFieldError var1) {
      }
   }
}
