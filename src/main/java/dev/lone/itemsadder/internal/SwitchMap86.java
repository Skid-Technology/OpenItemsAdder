package dev.lone.itemsadder.internal;

import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;

// $VF: synthetic class
public class SwitchMap86 {
   static {
      try {
         bMS[Rotation.FLIPPED.ordinal()] = 1;
      } catch (NoSuchFieldError var24) {
      }

      try {
         bMS[Rotation.FLIPPED_45.ordinal()] = 2;
      } catch (NoSuchFieldError var23) {
      }

      try {
         bMS[Rotation.COUNTER_CLOCKWISE.ordinal()] = 3;
      } catch (NoSuchFieldError var22) {
      }

      try {
         bMS[Rotation.COUNTER_CLOCKWISE_45.ordinal()] = 4;
      } catch (NoSuchFieldError var21) {
      }

      try {
         bMS[Rotation.NONE.ordinal()] = 5;
      } catch (NoSuchFieldError var20) {
      }

      try {
         bMS[Rotation.CLOCKWISE_45.ordinal()] = 6;
      } catch (NoSuchFieldError var19) {
      }

      try {
         bMS[Rotation.CLOCKWISE.ordinal()] = 7;
      } catch (NoSuchFieldError var18) {
      }

      try {
         bMS[Rotation.CLOCKWISE_135.ordinal()] = 8;
      } catch (NoSuchFieldError var17) {
      }

      bMR = new int[BlockFace.values().length];

      try {
         bMR[BlockFace.NORTH.ordinal()] = 1;
      } catch (NoSuchFieldError var16) {
      }

      try {
         bMR[BlockFace.WEST.ordinal()] = 2;
      } catch (NoSuchFieldError var15) {
      }

      try {
         bMR[BlockFace.NORTH_WEST.ordinal()] = 3;
      } catch (NoSuchFieldError var14) {
      }

      try {
         bMR[BlockFace.SOUTH_WEST.ordinal()] = 4;
      } catch (NoSuchFieldError var13) {
      }

      try {
         bMR[BlockFace.EAST.ordinal()] = 5;
      } catch (NoSuchFieldError var12) {
      }

      try {
         bMR[BlockFace.NORTH_EAST.ordinal()] = 6;
      } catch (NoSuchFieldError var11) {
      }

      try {
         bMR[BlockFace.SOUTH_EAST.ordinal()] = 7;
      } catch (NoSuchFieldError var10) {
      }

      try {
         bMR[BlockFace.SOUTH.ordinal()] = 8;
      } catch (NoSuchFieldError var9) {
      }

      try {
         bMR[BlockFace.EAST_NORTH_EAST.ordinal()] = 9;
      } catch (NoSuchFieldError var8) {
      }

      try {
         bMR[BlockFace.WEST_NORTH_WEST.ordinal()] = 10;
      } catch (NoSuchFieldError var7) {
      }

      try {
         bMR[BlockFace.NORTH_NORTH_EAST.ordinal()] = 11;
      } catch (NoSuchFieldError var6) {
      }

      try {
         bMR[BlockFace.NORTH_NORTH_WEST.ordinal()] = 12;
      } catch (NoSuchFieldError var5) {
      }

      try {
         bMR[BlockFace.EAST_SOUTH_EAST.ordinal()] = 13;
      } catch (NoSuchFieldError var4) {
      }

      try {
         bMR[BlockFace.WEST_SOUTH_WEST.ordinal()] = 14;
      } catch (NoSuchFieldError var3) {
      }

      try {
         bMR[BlockFace.SOUTH_SOUTH_EAST.ordinal()] = 15;
      } catch (NoSuchFieldError var2) {
      }

      try {
         bMR[BlockFace.SOUTH_SOUTH_WEST.ordinal()] = 16;
      } catch (NoSuchFieldError var1) {
      }
   }
}
