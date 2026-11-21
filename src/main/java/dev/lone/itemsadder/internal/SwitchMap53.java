package dev.lone.itemsadder.internal;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;

// $VF: synthetic class
public class SwitchMap53 {
   static {
      try {
         aWC[MobCategory.MONSTER.ordinal()] = 1;
      } catch (NoSuchFieldError var9) {
      }

      try {
         aWC[MobCategory.CREATURE.ordinal()] = 2;
      } catch (NoSuchFieldError var8) {
      }

      try {
         aWC[MobCategory.AMBIENT.ordinal()] = 3;
      } catch (NoSuchFieldError var7) {
      }

      try {
         aWC[MobCategory.AXOLOTLS.ordinal()] = 4;
      } catch (NoSuchFieldError var6) {
      }

      try {
         aWC[MobCategory.UNDERGROUND_WATER_CREATURE.ordinal()] = 5;
      } catch (NoSuchFieldError var5) {
      }

      try {
         aWC[MobCategory.WATER_CREATURE.ordinal()] = 6;
      } catch (NoSuchFieldError var4) {
      }

      try {
         aWC[MobCategory.WATER_AMBIENT.ordinal()] = 7;
      } catch (NoSuchFieldError var3) {
      }

      aWB = new int[EquipmentSlot.values().length];

      try {
         aWB[EquipmentSlot.MAINHAND.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         aWB[EquipmentSlot.OFFHAND.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
