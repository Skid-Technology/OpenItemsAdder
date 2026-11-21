package dev.lone.itemsadder.item;

import java.util.Locale;
import org.bukkit.inventory.EquipmentSlot;

public enum EquipmentSlotType {
   HEAD("head", 1, EquipmentSlot.HEAD),
   CHEST("chest", 2, EquipmentSlot.CHEST),
   LEGS("legs", 3, EquipmentSlot.LEGS),
   FEET("feet", 4, EquipmentSlot.FEET),
   MAINHAND("mainhand", 5, EquipmentSlot.HAND),
   OFFHAND("offhand", 6, EquipmentSlot.OFF_HAND);

   public final String name;
   public final int numVal;
   public final EquipmentSlot bukkitSlot;

   EquipmentSlotType(String var3, int var4, EquipmentSlot var5) {
      this.name = var3;
      this.numVal = var4;
      this.bukkitSlot = var5;
   }

   public static EquipmentSlotType cP(String var0) {
      if (var0 == null) {
         return null;
      }

      var0 = var0.toLowerCase(Locale.ROOT);

      for (EquipmentSlotType var4 : values()) {
         if (var4.name.equals(var0)) {
            return var4;
         }
      }

      return null;
   }

   public static EquipmentSlot cQ(String var0) {
      if (var0 == null) {
         return null;
      }

      var0 = var0.toLowerCase(Locale.ROOT);

      for (EquipmentSlotType var4 : values()) {
         if (var4.name.equals(var0)) {
            return var4.bukkitSlot;
         }
      }

      return null;
   }

   public static String cR(String var0) {
      if (var0 == null) {
         return null;
      }

      return switch (var0) {
         case "HAND" -> MAINHAND.name;
         case "OFF_HAND" -> OFFHAND.name;
         case "HEAD" -> HEAD.name;
         case "CHEST" -> CHEST.name;
         case "LEGS" -> LEGS.name;
         case "FEET" -> FEET.name;
         default -> null;
      };
   }
}
