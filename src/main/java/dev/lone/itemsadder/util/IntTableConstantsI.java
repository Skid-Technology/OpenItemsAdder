package dev.lone.itemsadder.util;

import beer.devs.nativepackets.api.PlayerAction;

// $VF: synthetic class
public class IntTableConstantsI {
   static {
      try {
         SWITCH_MAP[PlayerAction.START_DESTROY_BLOCK.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         SWITCH_MAP[PlayerAction.ABORT_DESTROY_BLOCK.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         SWITCH_MAP[PlayerAction.STOP_DESTROY_BLOCK.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
