package dev.lone.itemsadder.internal;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;

// $VF: synthetic class
public class SwitchMap50 {
   static {
      try {
         aWw[MobCategory.MONSTER.ordinal()] = 1;
      } catch (NoSuchFieldError var9) {
      }

      try {
         aWw[MobCategory.CREATURE.ordinal()] = 2;
      } catch (NoSuchFieldError var8) {
      }

      try {
         aWw[MobCategory.AMBIENT.ordinal()] = 3;
      } catch (NoSuchFieldError var7) {
      }

      try {
         aWw[MobCategory.AXOLOTLS.ordinal()] = 4;
      } catch (NoSuchFieldError var6) {
      }

      try {
         aWw[MobCategory.UNDERGROUND_WATER_CREATURE.ordinal()] = 5;
      } catch (NoSuchFieldError var5) {
      }

      try {
         aWw[MobCategory.WATER_CREATURE.ordinal()] = 6;
      } catch (NoSuchFieldError var4) {
      }

      try {
         aWw[MobCategory.WATER_AMBIENT.ordinal()] = 7;
      } catch (NoSuchFieldError var3) {
      }

      aWv = new int[EquipmentSlot.values().length];

      try {
         aWv[EquipmentSlot.MAINHAND.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         aWv[EquipmentSlot.OFFHAND.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
