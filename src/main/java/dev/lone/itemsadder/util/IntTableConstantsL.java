package dev.lone.itemsadder.util;

import dev.lone.itemsadder.core.EffectChainMode;
import dev.lone.itemsadder.item.ItemEventType;

// $VF: synthetic class
public class IntTableConstantsL {
   static {
      try {
         SWITCH_MAP_B[EffectChainMode.STOP_IF_LAST_SUCCESS.ordinal()] = 1;
      } catch (NoSuchFieldError var18) {
      }

      try {
         SWITCH_MAP_B[EffectChainMode.STOP_IF_LAST_FAIL.ordinal()] = 2;
      } catch (NoSuchFieldError var17) {
      }

      try {
         SWITCH_MAP_B[EffectChainMode.SKIP_IF_LAST_SUCCESS.ordinal()] = 3;
      } catch (NoSuchFieldError var16) {
      }

      try {
         SWITCH_MAP_B[EffectChainMode.SKIP_IF_LAST_FAIL.ordinal()] = 4;
      } catch (NoSuchFieldError var15) {
      }

      try {
         SWITCH_MAP_B[EffectChainMode.STOP_IF_ANY_SUCCESS.ordinal()] = 5;
      } catch (NoSuchFieldError var14) {
      }

      try {
         SWITCH_MAP_B[EffectChainMode.STOP_IF_ANY_FAIL.ordinal()] = 6;
      } catch (NoSuchFieldError var13) {
      }

      try {
         SWITCH_MAP_B[EffectChainMode.SKIP_IF_ANY_SUCCESS.ordinal()] = 7;
      } catch (NoSuchFieldError var12) {
      }

      try {
         SWITCH_MAP_B[EffectChainMode.SKIP_IF_ANY_FAIL.ordinal()] = 8;
      } catch (NoSuchFieldError var11) {
      }

      try {
         SWITCH_MAP_B[EffectChainMode.STOP_IF_SUCCESS.ordinal()] = 9;
      } catch (NoSuchFieldError var10) {
      }

      try {
         SWITCH_MAP_B[EffectChainMode.STOP_IF_FAIL.ordinal()] = 10;
      } catch (NoSuchFieldError var9) {
      }

      try {
         SWITCH_MAP_B[EffectChainMode.SKIP_IF_SUCCESS.ordinal()] = 11;
      } catch (NoSuchFieldError var8) {
      }

      try {
         SWITCH_MAP_B[EffectChainMode.SKIP_IF_FAIL.ordinal()] = 12;
      } catch (NoSuchFieldError var7) {
      }

      SWITCH_MAP_A = new int[ItemEventType.values().length];

      try {
         SWITCH_MAP_A[ItemEventType.WEAR.ordinal()] = 1;
      } catch (NoSuchFieldError var6) {
      }

      try {
         SWITCH_MAP_A[ItemEventType.UNWEAR.ordinal()] = 2;
      } catch (NoSuchFieldError var5) {
      }

      try {
         SWITCH_MAP_A[ItemEventType.HELD.ordinal()] = 3;
      } catch (NoSuchFieldError var4) {
      }

      try {
         SWITCH_MAP_A[ItemEventType.UNHELD.ordinal()] = 4;
      } catch (NoSuchFieldError var3) {
      }

      try {
         SWITCH_MAP_A[ItemEventType.HELD_OFFHAND.ordinal()] = 5;
      } catch (NoSuchFieldError var2) {
      }

      try {
         SWITCH_MAP_A[ItemEventType.UNHELD_OFFHAND.ordinal()] = 6;
      } catch (NoSuchFieldError var1) {
      }
   }
}
