package dev.lone.itemsadder.internal;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;

// $VF: synthetic class
public class SwitchMap52 {
   static {
      try {
         aWA[MobCategory.MONSTER.ordinal()] = 1;
      } catch (NoSuchFieldError var9) {
      }

      try {
         aWA[MobCategory.CREATURE.ordinal()] = 2;
      } catch (NoSuchFieldError var8) {
      }

      try {
         aWA[MobCategory.AMBIENT.ordinal()] = 3;
      } catch (NoSuchFieldError var7) {
      }

      try {
         aWA[MobCategory.AXOLOTLS.ordinal()] = 4;
      } catch (NoSuchFieldError var6) {
      }

      try {
         aWA[MobCategory.UNDERGROUND_WATER_CREATURE.ordinal()] = 5;
      } catch (NoSuchFieldError var5) {
      }

      try {
         aWA[MobCategory.WATER_CREATURE.ordinal()] = 6;
      } catch (NoSuchFieldError var4) {
      }

      try {
         aWA[MobCategory.WATER_AMBIENT.ordinal()] = 7;
      } catch (NoSuchFieldError var3) {
      }

      aWz = new int[EquipmentSlot.values().length];

      try {
         aWz[EquipmentSlot.MAINHAND.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         aWz[EquipmentSlot.OFFHAND.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
