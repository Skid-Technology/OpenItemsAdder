package dev.lone.itemsadder.item;

import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.util.IntTableConstantsP;
import lombok.Generated;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.Nullable;

public class ColoredItemDefinition extends CustomItemDefinition {
   public final EquipmentSlotType anu;
   final boolean anv;
   @Nullable
   private LeatherColorDefinition anw;

   @Nullable
   public static LeatherColorDefinition method8842(CustomItemDefinition var0) {
      return var0 instanceof ColoredItemDefinition var1 ? var1.De() : null;
   }

   public ColoredItemDefinition(YamlConfig var1, String var2, String var3, Material var4, @Nullable Color var5, boolean var6) {
      super(var1, var2, var3, var4);
      this.anu = method8845(var4);
      this.anv = var6;
      this.setColor(var5);
   }

   public void setColor(Color color) {
      if (color != null) {
         LeatherArmorMeta var2 = (LeatherArmorMeta)this.getItemMeta();
         var2.setColor(color);
         var2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_DYE});
         this.setItemMeta(var2);
      }
   }

   public int getColor() {
      return ((LeatherArmorMeta)this.getItemMeta()).getColor().asRGB();
   }

   public void method8843(LeatherColorDefinition var1) {
      this.anw = var1;
      this.setColor(var1.apE);
   }

   @Nullable
   public LeatherColorDefinition De() {
      return this.anw;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Nullable
   public static Material method8844(@Nullable EquipmentSlotType var0) {
      if (var0 != null) {
         switch (IntTableConstantsP.SWITCH_MAP_A[var0.ordinal()]) {
            case 1:
               return Material.LEATHER_HELMET;
            case 2:
               return Material.LEATHER_CHESTPLATE;
            case 3:
               return Material.LEATHER_LEGGINGS;
            case 4:
               return Material.LEATHER_BOOTS;
         }
      }

      return null;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static EquipmentSlotType method8845(Material var0) {
      switch (IntTableConstantsP.SWITCH_MAP_B[var0.ordinal()]) {
         case 1:
            return EquipmentSlotType.CHEST;
         case 2:
            return EquipmentSlotType.LEGS;
         case 3:
            return EquipmentSlotType.FEET;
         default:
            return EquipmentSlotType.HEAD;
      }
   }

   @Generated
   public boolean Df() {
      return this.anv;
   }
}
