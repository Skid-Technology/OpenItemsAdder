package dev.lone.itemsadder.internal;

import dev.lone.itemsadder.block.BlockRealizationMode;
import org.bukkit.Material;

// $VF: synthetic class
public class SwitchMap79 {
   static {
      try {
         bIf[BlockRealizationMode.REAL.ordinal()] = 1;
      } catch (NoSuchFieldError var13) {
      }

      try {
         bIf[BlockRealizationMode.REAL_TRANSPARENT.ordinal()] = 2;
      } catch (NoSuchFieldError var12) {
      }

      try {
         bIf[BlockRealizationMode.REAL_NOTE.ordinal()] = 3;
      } catch (NoSuchFieldError var11) {
      }

      try {
         bIf[BlockRealizationMode.REAL_WIRE.ordinal()] = 4;
      } catch (NoSuchFieldError var10) {
      }

      try {
         bIf[BlockRealizationMode.ENTITY.ordinal()] = 5;
      } catch (NoSuchFieldError var9) {
      }

      try {
         bIf[BlockRealizationMode.TILE.ordinal()] = 6;
      } catch (NoSuchFieldError var8) {
      }

      try {
         bIf[BlockRealizationMode.FIRE.ordinal()] = 7;
      } catch (NoSuchFieldError var7) {
      }

      bIe = new int[Material.values().length];

      try {
         bIe[Material.BROWN_MUSHROOM_BLOCK.ordinal()] = 1;
      } catch (NoSuchFieldError var6) {
      }

      try {
         bIe[Material.RED_MUSHROOM_BLOCK.ordinal()] = 2;
      } catch (NoSuchFieldError var5) {
      }

      try {
         bIe[Material.MUSHROOM_STEM.ordinal()] = 3;
      } catch (NoSuchFieldError var4) {
      }

      try {
         bIe[Material.CHORUS_PLANT.ordinal()] = 4;
      } catch (NoSuchFieldError var3) {
      }

      try {
         bIe[Material.NOTE_BLOCK.ordinal()] = 5;
      } catch (NoSuchFieldError var2) {
      }

      try {
         bIe[Material.TRIPWIRE.ordinal()] = 6;
      } catch (NoSuchFieldError var1) {
      }
   }
}
