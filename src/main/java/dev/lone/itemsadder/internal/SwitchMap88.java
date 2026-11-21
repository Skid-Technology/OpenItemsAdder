package dev.lone.itemsadder.internal;

import org.bukkit.boss.BarStyle;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;

// $VF: synthetic class
public class SwitchMap88 {
   static {
      try {
         ts[EntityType.SPIDER.ordinal()] = 1;
      } catch (NoSuchFieldError var12) {
      }

      try {
         ts[EntityType.CAVE_SPIDER.ordinal()] = 2;
      } catch (NoSuchFieldError var11) {
      }

      try {
         ts[EntityType.SILVERFISH.ordinal()] = 3;
      } catch (NoSuchFieldError var10) {
      }

      try {
         ts[EntityType.ENDERMITE.ordinal()] = 4;
      } catch (NoSuchFieldError var9) {
      }

      tr = new int[BarStyle.values().length];

      try {
         tr[BarStyle.SOLID.ordinal()] = 1;
      } catch (NoSuchFieldError var8) {
      }

      try {
         tr[BarStyle.SEGMENTED_6.ordinal()] = 2;
      } catch (NoSuchFieldError var7) {
      }

      try {
         tr[BarStyle.SEGMENTED_10.ordinal()] = 3;
      } catch (NoSuchFieldError var6) {
      }

      try {
         tr[BarStyle.SEGMENTED_12.ordinal()] = 4;
      } catch (NoSuchFieldError var5) {
      }

      try {
         tr[BarStyle.SEGMENTED_20.ordinal()] = 5;
      } catch (NoSuchFieldError var4) {
      }

      tq = new int[EquipmentSlot.values().length];

      try {
         tq[EquipmentSlot.HAND.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         tq[EquipmentSlot.OFF_HAND.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         tq[EquipmentSlot.HEAD.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
