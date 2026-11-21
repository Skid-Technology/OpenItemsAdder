package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeLoader {
   public static boolean method10102(ConfigSectionWrapper var0) {
      return var0.hA("equipment");
   }

   public static void method10103(CustomItemDefinition var0, ConfigSectionWrapper var1, YamlConfig var2, String var3, String var4) {
      method10105(var0, var1, var2, var3, var4, null);
   }

   static void method10104(CustomItemDefinition var0, ConfigSectionWrapper var1, YamlConfig var2, String var3, String var4, NamespacedKey var5) {
      method10105(var0, var1, var2, var3, var4, var5);
   }

   private static void method10105(
      CustomItemDefinition var0, ConfigSectionWrapper var1, YamlConfig var2, String var3, String var4, @Nullable NamespacedKey var5
   ) {
      if (!Version.isAtLeast(Version.v1_21_3)) {
         throw new IllegalArgumentException("Error: 'equipment' is only available in 1.21.2+. File: " + var2.DJ());
      }

      ConfigSectionWrapper var6 = var1.kc("equipment");
      if (var6 == null && var5 == null) {
         throw new IllegalArgumentException("Error: 'equipment' must be a section. File: " + var2.DJ());
      }

      EquipmentSlot var7 = method10106(var0.getType(), var6, var5);
      String var8 = var6 == null ? null : var6.jO("slot");
      if (var5 != null && var8 != null && YamlConfig.method7037(var8, var0x -> {}) != EquipmentSlot.CHEST) {
         throw new IllegalArgumentException("Error: 'elytra' requires 'equipment.slot: CHEST'. File: " + var2.DJ());
      }

      if (var7 != null) {
         String var9 = var6 == null ? null : var6.jO("id");
         NamespacedKey var10 = YamlConfig.method7036(var9, var3, true, var0x -> {
            throw new IllegalArgumentException("Invalid 'equipment.id' value '" + var0x + "'.");
         });
         if (var5 != null && var9 != null && var10 == null) {
            throw new IllegalArgumentException("Error: 'elytra' has invalid explicit 'equipment.id: " + var9 + "'. File: " + var2.DJ());
         }

         if (var5 != null && var10 != null && !var5.equals(var10)) {
            throw new IllegalArgumentException("Error: 'elytra' requires 'equipment.id: " + var5 + "'. File: " + var2.DJ());
         }

         NamespacedKey var11 = var5 != null ? var5 : var10;
         if (var11 != null && var5 == null && ContentManager.method8084().method8207(var11) == null) {
            throw new IllegalArgumentException("Error: Unknown 'equipment.id' value '" + var11 + "'. File: " + var2.DJ());
         }

         NamespacedKey var12 = YamlConfig.method7036(var6 == null ? null : var6.jO("camera_overlay_id"), var3, true, var0x -> {
            throw new IllegalArgumentException("Invalid 'equipment.camera_overlay_id' value '" + var0x + "'.");
         });
         String var13 = YamlConfig.method7038(var6 == null ? null : var6.jO("sound"), true, () -> {
            throw new IllegalArgumentException("Invalid 'equipment.sound'.");
         });
         List var14 = YamlConfig.method7039(var6 == null ? List.of() : var6.jZ("allowed_entities"), EntityType.class, true, true, var0x -> {
            throw new IllegalArgumentException("Invalid 'equipment.allowed_entities' value '" + var0x + "'.");
         });
         if (var5 != null && var6 != null && var6.hA("allowed_entities") && (var14 == null || !var14.contains(EntityType.PLAYER))) {
            throw new IllegalArgumentException("Error: 'elytra' requires 'equipment.allowed_entities' to include PLAYER. File: " + var2.DJ());
         }

         Boolean var15 = var6 == null ? null : var6.method7104("glider", null);
         if (var5 != null && var6 != null && var6.hA("glider") && !Boolean.TRUE.equals(var15)) {
            throw new IllegalArgumentException("Error: 'elytra' requires 'equipment.glider: true'. File: " + var2.DJ());
         }

         var0.method8796(
            var11,
            var7,
            var13,
            var12,
            var14,
            var6 == null ? null : var6.method7104("dispensable", null),
            var6 == null ? null : var6.method7104("swappable", null),
            var6 == null ? null : var6.method7104("damage_on_hurt", null),
            method10107(var5 != null, var15)
         );
         ConfigurationSection var16 = var6 == null ? null : var6.kb("slot_attribute_modifiers");
         if (var16 != null) {
            for (String var18 : var16.getKeys(false)) {
               ItemDefinitionLoader.method10120(var0, var7.name().toLowerCase(), var18, var16);
            }
         }
      }
   }

   static EquipmentSlot method10106(Material var0, @Nullable ConfigSectionWrapper var1, @Nullable NamespacedKey var2) {
      if (var2 != null) {
         return EquipmentSlot.CHEST;
      } else {
         EquipmentSlot var3 = ItemStackUtils.ak(var0);
         String var4 = var1 == null ? null : var1.bc("slot", var3 == null ? null : var3.name());
         EquipmentSlot var5 = YamlConfig.jF(var4);
         if (var5 == null) {
            throw new IllegalArgumentException("Invalid equipment slot '" + var4 + "': set 'equipment.slot' or a valid armor material.");
         } else {
            return var5;
         }
      }
   }

   @Nullable
   static Boolean method10107(boolean var0, @Nullable Boolean var1) {
      return var0 ? Boolean.TRUE : var1;
   }
}
