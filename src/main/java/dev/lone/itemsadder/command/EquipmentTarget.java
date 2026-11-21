package dev.lone.itemsadder.command;

import org.bukkit.inventory.EquipmentSlot;

enum EquipmentTarget {
   MAIN_HAND,
   OFF_HAND,
   HEAD,
   CHEST,
   LEGS,
   FEET,
   ALL;

   public EquipmentSlot Lq() {
      return switch (this) {
         case MAIN_HAND -> EquipmentSlot.HAND;
         case OFF_HAND -> EquipmentSlot.OFF_HAND;
         case HEAD -> EquipmentSlot.HEAD;
         case CHEST -> EquipmentSlot.CHEST;
         case LEGS -> EquipmentSlot.LEGS;
         case FEET -> EquipmentSlot.FEET;
         default -> null;
      };
   }

   public boolean isSingle() {
      return this != ALL;
   }
}
