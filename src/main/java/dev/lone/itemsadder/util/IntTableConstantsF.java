package dev.lone.itemsadder.util;

import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;

// $VF: synthetic class
public class IntTableConstantsF {
   static {
      try {
         SWITCH_MAP_B[Rotation.CLOCKWISE_45.ordinal()] = 1;
      } catch (NoSuchFieldError var17) {
      }

      try {
         SWITCH_MAP_B[Rotation.CLOCKWISE.ordinal()] = 2;
      } catch (NoSuchFieldError var16) {
      }

      try {
         SWITCH_MAP_B[Rotation.CLOCKWISE_135.ordinal()] = 3;
      } catch (NoSuchFieldError var15) {
      }

      try {
         SWITCH_MAP_B[Rotation.FLIPPED.ordinal()] = 4;
      } catch (NoSuchFieldError var14) {
      }

      try {
         SWITCH_MAP_B[Rotation.FLIPPED_45.ordinal()] = 5;
      } catch (NoSuchFieldError var13) {
      }

      try {
         SWITCH_MAP_B[Rotation.COUNTER_CLOCKWISE.ordinal()] = 6;
      } catch (NoSuchFieldError var12) {
      }

      try {
         SWITCH_MAP_B[Rotation.COUNTER_CLOCKWISE_45.ordinal()] = 7;
      } catch (NoSuchFieldError var11) {
      }

      SWITCH_MAP_A = new int[BlockFace.values().length];

      try {
         SWITCH_MAP_A[BlockFace.NORTH.ordinal()] = 1;
      } catch (NoSuchFieldError var10) {
      }

      try {
         SWITCH_MAP_A[BlockFace.SOUTH.ordinal()] = 2;
      } catch (NoSuchFieldError var9) {
      }

      try {
         SWITCH_MAP_A[BlockFace.EAST.ordinal()] = 3;
      } catch (NoSuchFieldError var8) {
      }

      try {
         SWITCH_MAP_A[BlockFace.WEST.ordinal()] = 4;
      } catch (NoSuchFieldError var7) {
      }

      try {
         SWITCH_MAP_A[BlockFace.UP.ordinal()] = 5;
      } catch (NoSuchFieldError var6) {
      }

      try {
         SWITCH_MAP_A[BlockFace.DOWN.ordinal()] = 6;
      } catch (NoSuchFieldError var5) {
      }

      try {
         SWITCH_MAP_A[BlockFace.NORTH_WEST.ordinal()] = 7;
      } catch (NoSuchFieldError var4) {
      }

      try {
         SWITCH_MAP_A[BlockFace.NORTH_EAST.ordinal()] = 8;
      } catch (NoSuchFieldError var3) {
      }

      try {
         SWITCH_MAP_A[BlockFace.SOUTH_WEST.ordinal()] = 9;
      } catch (NoSuchFieldError var2) {
      }

      try {
         SWITCH_MAP_A[BlockFace.SOUTH_EAST.ordinal()] = 10;
      } catch (NoSuchFieldError var1) {
      }
   }
}
