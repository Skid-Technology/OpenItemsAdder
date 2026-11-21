package dev.lone.itemsadder.util;

import dev.lone.itemsadder.item.EquipmentSlotType;
import org.bukkit.Material;

// $VF: synthetic class
public class IntTableConstantsP {
   static {
      try {
         SWITCH_MAP_B[Material.LEATHER_CHESTPLATE.ordinal()] = 1;
      } catch (NoSuchFieldError var7) {
      }

      try {
         SWITCH_MAP_B[Material.LEATHER_LEGGINGS.ordinal()] = 2;
      } catch (NoSuchFieldError var6) {
      }

      try {
         SWITCH_MAP_B[Material.LEATHER_BOOTS.ordinal()] = 3;
      } catch (NoSuchFieldError var5) {
      }

      SWITCH_MAP_A = new int[EquipmentSlotType.values().length];

      try {
         SWITCH_MAP_A[EquipmentSlotType.HEAD.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         SWITCH_MAP_A[EquipmentSlotType.CHEST.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         SWITCH_MAP_A[EquipmentSlotType.LEGS.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         SWITCH_MAP_A[EquipmentSlotType.FEET.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }
   }
}
