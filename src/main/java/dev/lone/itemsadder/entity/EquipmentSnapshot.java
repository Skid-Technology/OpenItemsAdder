package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.ColoredItemDefinition;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.item.LeatherColorDefinition;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.jetbrains.annotations.Nullable;

public class EquipmentSnapshot {
   private final Map CT;
   public static final EquipmentSnapshot CU = new EquipmentSnapshot(Map.of());

   public EquipmentSnapshot(Map var1) {
      this.CT = var1;
   }

   public static EquipmentSnapshot capture(EntityEquipment var0, HumanoidEquipmentSet var1) {
      HashMap var2 = new HashMap();

      for (String var4 : HumanoidEquipmentSet.CH) {
         if (!var1.pf().stream().noneMatch(var1x -> var1x.ph().equals(var4))) {
            EquipmentSlot var5 = switch (var4) {
               case "helmet" -> EquipmentSlot.HEAD;
               case "chest" -> EquipmentSlot.CHEST;
               case "legs" -> EquipmentSlot.LEGS;
               case "boots" -> EquipmentSlot.FEET;
               default -> throw new IllegalArgumentException(var4);
            };
            EquipmentItemRecord var8 = toRecord(var0.getItem(var5), var5);
            if (var8 != null) {
               var2.put(var4, var8);
            }
         }
      }

      return var2.isEmpty() ? CU : new EquipmentSnapshot(Map.copyOf(var2));
   }

   @Nullable
   private static EquipmentItemRecord toRecord(@Nullable ItemStack var0, EquipmentSlot var1) {
      if (var0 != null && !var0.getType().isAir()) {
         ItemMeta var2 = var0.getItemMeta();
         if (var2 == null) {
            return null;
         }

         String var3 = ItemOperationsService.getRegistryBridge().method3441(var0, var1);
         if (var3 == null) {
            return null;
         }

         Integer var4 = ItemOperationsService.getRegistryBridge().am(var0);
         CustomItemDefinition var5 = ContentManager.method8084().method8149(var0);
         LeatherColorDefinition var6 = ColoredItemDefinition.method8842(var5);
         if (var6 != null) {
            var3 = var6.getNamespace() + ":ia_equipment_legacy/" + var6.getId();
            var4 = null;
         }

         String var7 = null;
         String var8 = null;
         if (var2 instanceof ArmorMeta var9 && var9.hasTrim()) {
            ArmorTrim var10 = var9.getTrim();
            var7 = var10.getPattern().getKey().toString();
            var8 = var10.getMaterial().getKey().toString();
         }

         boolean var11 = var2.hasEnchantmentGlintOverride() ? Boolean.TRUE.equals(var2.getEnchantmentGlintOverride()) : var2.hasEnchants();
         return new EquipmentItemRecord(var3, var4, var7, var8, var11);
      } else {
         return null;
      }
   }

   public Map slots() {
      return this.CT;
   }
}
