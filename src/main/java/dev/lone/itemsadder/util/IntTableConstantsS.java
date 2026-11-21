package dev.lone.itemsadder.util;

import dev.lone.itemsadder.block.InventoryHookMode;
import org.bukkit.block.BlockFace;

// $VF: synthetic class
public class IntTableConstantsS {
   static {
      try {
         SWITCH_MAP_B[InventoryHookMode.ALL.ordinal()] = 1;
      } catch (NoSuchFieldError var10) {
      }

      try {
         SWITCH_MAP_B[InventoryHookMode.LOG.ordinal()] = 2;
      } catch (NoSuchFieldError var9) {
      }

      try {
         SWITCH_MAP_B[InventoryHookMode.FURNACE.ordinal()] = 3;
      } catch (NoSuchFieldError var8) {
      }

      try {
         SWITCH_MAP_B[InventoryHookMode.DROPPER.ordinal()] = 4;
      } catch (NoSuchFieldError var7) {
      }

      SWITCH_MAP_A = new int[BlockFace.values().length];

      try {
         SWITCH_MAP_A[BlockFace.SOUTH.ordinal()] = 1;
      } catch (NoSuchFieldError var6) {
      }

      try {
         SWITCH_MAP_A[BlockFace.NORTH.ordinal()] = 2;
      } catch (NoSuchFieldError var5) {
      }

      try {
         SWITCH_MAP_A[BlockFace.EAST.ordinal()] = 3;
      } catch (NoSuchFieldError var4) {
      }

      try {
         SWITCH_MAP_A[BlockFace.WEST.ordinal()] = 4;
      } catch (NoSuchFieldError var3) {
      }

      try {
         SWITCH_MAP_A[BlockFace.DOWN.ordinal()] = 5;
      } catch (NoSuchFieldError var2) {
      }

      try {
         SWITCH_MAP_A[BlockFace.UP.ordinal()] = 6;
      } catch (NoSuchFieldError var1) {
      }
   }
}
