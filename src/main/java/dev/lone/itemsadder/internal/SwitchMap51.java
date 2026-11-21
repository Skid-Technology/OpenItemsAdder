package dev.lone.itemsadder.internal;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;

// $VF: synthetic class
public class SwitchMap51 {
   static {
      try {
         aWy[MobCategory.MONSTER.ordinal()] = 1;
      } catch (NoSuchFieldError var9) {
      }

      try {
         aWy[MobCategory.CREATURE.ordinal()] = 2;
      } catch (NoSuchFieldError var8) {
      }

      try {
         aWy[MobCategory.AMBIENT.ordinal()] = 3;
      } catch (NoSuchFieldError var7) {
      }

      try {
         aWy[MobCategory.AXOLOTLS.ordinal()] = 4;
      } catch (NoSuchFieldError var6) {
      }

      try {
         aWy[MobCategory.UNDERGROUND_WATER_CREATURE.ordinal()] = 5;
      } catch (NoSuchFieldError var5) {
      }

      try {
         aWy[MobCategory.WATER_CREATURE.ordinal()] = 6;
      } catch (NoSuchFieldError var4) {
      }

      try {
         aWy[MobCategory.WATER_AMBIENT.ordinal()] = 7;
      } catch (NoSuchFieldError var3) {
      }

      aWx = new int[EquipmentSlot.values().length];

      try {
         aWx[EquipmentSlot.MAINHAND.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         aWx[EquipmentSlot.OFFHAND.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
