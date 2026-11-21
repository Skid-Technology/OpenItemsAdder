package dev.lone.itemsadder.util;

import dev.lone.itemsadder.item.ItemKind;
import org.bukkit.Instrument;

// $VF: synthetic class
public class IntTableConstantsR {
   static {
      try {
         SWITCH_MAP_B[Instrument.PIANO.ordinal()] = 1;
      } catch (NoSuchFieldError var8) {
      }

      try {
         SWITCH_MAP_B[Instrument.BASS_DRUM.ordinal()] = 2;
      } catch (NoSuchFieldError var7) {
      }

      try {
         SWITCH_MAP_B[Instrument.SNARE_DRUM.ordinal()] = 3;
      } catch (NoSuchFieldError var6) {
      }

      try {
         SWITCH_MAP_B[Instrument.STICKS.ordinal()] = 4;
      } catch (NoSuchFieldError var5) {
      }

      try {
         SWITCH_MAP_B[Instrument.BASS_GUITAR.ordinal()] = 5;
      } catch (NoSuchFieldError var4) {
      }

      SWITCH_MAP_A = new int[ItemKind.values().length];

      try {
         SWITCH_MAP_A[ItemKind.BLOCK.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         SWITCH_MAP_A[ItemKind.ITEM.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         SWITCH_MAP_A[ItemKind.VANILLA.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
