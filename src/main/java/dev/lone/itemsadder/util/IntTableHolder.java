package dev.lone.itemsadder.util;

import dev.lone.itemsadder.entity.AnimationState;

// $VF: synthetic class
public class IntTableHolder {
   static {
      try {
         SWITCH_MAP[AnimationState.OTHER.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         SWITCH_MAP[AnimationState.ATTACK.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         SWITCH_MAP[AnimationState.DEATH.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
