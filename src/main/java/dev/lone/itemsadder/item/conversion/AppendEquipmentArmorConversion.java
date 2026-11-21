package dev.lone.itemsadder.item.conversion;

import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.YamlConfig;
import lombok.Generated;
import org.bukkit.Material;

public class AppendEquipmentArmorConversion extends AbstractItemConversion {
   private String asY;
   private String asZ;

   public AppendEquipmentArmorConversion(YamlConfig var1, String var2, ConfigSectionWrapper var3) {
      super(var1, var2, var3);
   }

   @Override
   public String GK() {
      return "append_equipment_to_legacy_armor";
   }

   @Override
   public boolean needsConversion() {
      if (!this.atc.jI("specific_properties.armor.custom_armor")) {
         return false;
      } else {
         this.asY = this.atc.jO("specific_properties.armor.slot");
         if (this.asY == null) {
            throw new ItemConversionException("Item '" + this.atb + "' has null slot", true);
         } else {
            this.asY = this.asY.toUpperCase();
            this.asZ = this.atc.jO("specific_properties.armor.custom_armor");
            if (this.ata.contains("equipments." + this.asZ)) {
               throw new ItemConversionException("Item '" + this.atb + "' equipment already exists", true);
            } else if (!this.ata.contains("armors_rendering." + this.asZ)) {
               throw new ItemConversionException("Item '" + this.atb + "' armors_rendering not in same file", true);
            } else if (!"HEAD".equals(this.asY) && !"CHEST".equals(this.asY) && !"LEGS".equals(this.asY) && !"FEET".equals(this.asY)) {
               throw new ItemConversionException("Item '" + this.atb + "' has unknown armor slot '" + this.asY + "'", false);
            } else {
               return true;
            }
         }
      }
   }

   @Override
   public void convert() {
      try {
         switch (this.asY) {
            case "HEAD":
               this.atc.method7074("resource.material", Material.LEATHER_HELMET.toString());
               break;
            case "CHEST":
               this.atc.method7074("resource.material", Material.LEATHER_CHESTPLATE.toString());
               break;
            case "LEGS":
               this.atc.method7074("resource.material", Material.LEATHER_LEGGINGS.toString());
               break;
            case "FEET":
               this.atc.method7074("resource.material", Material.LEATHER_BOOTS.toString());
         }

         this.ata.method7048("equipments." + this.asZ + ".layer_1", this.ata.getString("armors_rendering." + this.asZ + ".layer_1"));
         this.ata.method7048("equipments." + this.asZ + ".layer_2", this.ata.getString("armors_rendering." + this.asZ + ".layer_2"));
         this.atc.method7074("equipment.id", this.asZ);
         this.atc.method7074("equipment.legacy_armor_rendering_id", this.asZ);
         this.atc.method7074("equipment.slot", this.asY);
         if (this.atc.jI("attribute_modifiers." + this.asY.toLowerCase() + ".armor")) {
            for (String var5 : this.atc.jK("attribute_modifiers." + this.asY.toLowerCase())) {
               this.atc.method7074("equipment.slot_attribute_modifiers." + var5, this.atc.kf("attribute_modifiers." + this.asY.toLowerCase() + "." + var5));
            }
         }

         if (this.atc.jK("attribute_modifiers").size() <= 1) {
            this.atc.method7074("attribute_modifiers", null);
         }

         this.atc.method7074("specific_properties.armor", null);
         if (this.atc.jK("specific_properties").size() <= 1) {
            this.atc.method7074("specific_properties", null);
         }
      } catch (Exception var3) {
         throw new ItemConversionException("Failed to apply append_equipment conversion for item '" + this.atb + "'", var3);
      }
   }

   @Override
   public String GN() {
      return "Append new equipment to legacy armor '" + this.atb + "'";
   }

   @Generated
   public String GO() {
      return this.asZ;
   }
}
