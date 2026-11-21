package dev.lone.itemsadder.util;

import dev.lone.itemsadder.entity.AnimationState;
import dev.lone.itemsadder.entity.AnimationVariable;
import dev.lone.itemsadder.entity.AutoRotationMode;

// $VF: synthetic class
public class IntLookupTables {
   static {
      try {
         SWITCH_ANIMATION_VARIABLE[AnimationVariable.AGE.ordinal()] = 1;
      } catch (NoSuchFieldError var13) {
      }

      try {
         SWITCH_ANIMATION_VARIABLE[AnimationVariable.AGGRESSIVE.ordinal()] = 2;
      } catch (NoSuchFieldError var12) {
      }

      try {
         SWITCH_ANIMATION_VARIABLE[AnimationVariable.ATTACK_PROGRESS.ordinal()] = 3;
      } catch (NoSuchFieldError var11) {
      }

      try {
         SWITCH_ANIMATION_VARIABLE[AnimationVariable.NONE.ordinal()] = 4;
      } catch (NoSuchFieldError var10) {
      }

      SWITCH_AUTO_ROTATION_MODE = new int[AutoRotationMode.values().length];

      try {
         SWITCH_AUTO_ROTATION_MODE[AutoRotationMode.MANUAL.ordinal()] = 1;
      } catch (NoSuchFieldError var9) {
      }

      try {
         SWITCH_AUTO_ROTATION_MODE[AutoRotationMode.AUTOMATIC.ordinal()] = 2;
      } catch (NoSuchFieldError var8) {
      }

      try {
         SWITCH_AUTO_ROTATION_MODE[AutoRotationMode.INHERIT.ordinal()] = 3;
      } catch (NoSuchFieldError var7) {
      }

      SWITCH_ANIMATION_STATE = new int[AnimationState.values().length];

      try {
         SWITCH_ANIMATION_STATE[AnimationState.IDLE.ordinal()] = 1;
      } catch (NoSuchFieldError var6) {
      }

      try {
         SWITCH_ANIMATION_STATE[AnimationState.IDLE_ANGRY.ordinal()] = 2;
      } catch (NoSuchFieldError var5) {
      }

      try {
         SWITCH_ANIMATION_STATE[AnimationState.WALK.ordinal()] = 3;
      } catch (NoSuchFieldError var4) {
      }

      try {
         SWITCH_ANIMATION_STATE[AnimationState.WALK_ANGRY.ordinal()] = 4;
      } catch (NoSuchFieldError var3) {
      }

      try {
         SWITCH_ANIMATION_STATE[AnimationState.SNEAK.ordinal()] = 5;
      } catch (NoSuchFieldError var2) {
      }

      try {
         SWITCH_ANIMATION_STATE[AnimationState.SNEAK_WALK.ordinal()] = 6;
      } catch (NoSuchFieldError var1) {
      }
   }
}
