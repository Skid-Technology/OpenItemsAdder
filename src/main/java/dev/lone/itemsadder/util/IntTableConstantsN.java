package dev.lone.itemsadder.util;

import org.bukkit.event.player.PlayerFishEvent.State;

// $VF: synthetic class
public class IntTableConstantsN {
   static {
      try {
         SWITCH_MAP[State.FISHING.ordinal()] = 1;
      } catch (NoSuchFieldError var7) {
      }

      try {
         SWITCH_MAP[State.CAUGHT_ENTITY.ordinal()] = 2;
      } catch (NoSuchFieldError var6) {
      }

      try {
         SWITCH_MAP[State.CAUGHT_FISH.ordinal()] = 3;
      } catch (NoSuchFieldError var5) {
      }

      try {
         SWITCH_MAP[State.FAILED_ATTEMPT.ordinal()] = 4;
      } catch (NoSuchFieldError var4) {
      }

      try {
         SWITCH_MAP[State.REEL_IN.ordinal()] = 5;
      } catch (NoSuchFieldError var3) {
      }

      try {
         SWITCH_MAP[State.BITE.ordinal()] = 6;
      } catch (NoSuchFieldError var2) {
      }

      try {
         SWITCH_MAP[State.IN_GROUND.ordinal()] = 7;
      } catch (NoSuchFieldError var1) {
      }
   }
}
