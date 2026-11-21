package dev.lone.itemsadder.item.conversion;

import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import lombok.Generated;
import org.bukkit.Material;

public class LegacyArmorCompleteConversion extends AbstractItemConversion {
   private String asY;
   private String asZ;
   private Material db;

   public LegacyArmorCompleteConversion(YamlConfig var1, String var2, ConfigSectionWrapper var3) {
      super(var1, var2, var3);
   }

   @Override
   public String GK() {
      return "legacy_armor_complete";
   }

   @Override
   public boolean needsConversion() {
      if (!this.atc.jI("specific_properties.armor.custom_armor")) {
         return false;
      }

      this.asY = this.atc.jO("specific_properties.armor.slot");
      if (this.asY == null) {
         throw new ItemConversionException("Item '" + this.atb + "' has null slot", true);
      }

      this.asY = this.asY.toUpperCase();
      this.asZ = this.atc.jO("specific_properties.armor.custom_armor");
      if (this.ata.contains("equipments." + this.asZ)) {
         throw new ItemConversionException("Item '" + this.atb + "' equipment already exists", true);
      }

      if (!this.ata.contains("armors_rendering." + this.asZ)) {
         throw new ItemConversionException("Item '" + this.atb + "' armors_rendering not in same file", true);
      }

      switch (this.asY) {
         case "HEAD":
            this.db = (Material)BukkitRegistryLookup.enumOrDefault(Settings.get().legacyArmorFullConversionMaterial + "_HELMET", Material.class, null);
            break;
         case "CHEST":
            this.db = (Material)BukkitRegistryLookup.enumOrDefault(Settings.get().legacyArmorFullConversionMaterial + "_CHESTPLATE", Material.class, null);
            break;
         case "LEGS":
            this.db = (Material)BukkitRegistryLookup.enumOrDefault(Settings.get().legacyArmorFullConversionMaterial + "_LEGGINGS", Material.class, null);
            break;
         case "FEET":
            this.db = (Material)BukkitRegistryLookup.enumOrDefault(Settings.get().legacyArmorFullConversionMaterial + "_BOOTS", Material.class, null);
            break;
         default:
            throw new ItemConversionException("Item '" + this.atb + "' has unknown armor slot '" + this.asY + "'", false);
      }

      if (this.db == null) {
         throw new ItemConversionException("Item '" + this.atb + "' could not determine material for slot '" + this.asY + "'", false);
      } else {
         return true;
      }
   }

   @Override
   public void convert() {
      try {
         this.atc.method7074("resource.material", this.db.toString());
         this.ata.method7048("equipments." + this.asZ + ".layer_1", this.ata.getString("armors_rendering." + this.asZ + ".layer_1"));
         this.ata.method7048("equipments." + this.asZ + ".layer_2", this.ata.getString("armors_rendering." + this.asZ + ".layer_2"));
         this.atc.method7074("equipment.id", this.asZ);
         this.atc.method7074("equipment.slot", this.asY);
         if (this.atc.jI("attribute_modifiers." + this.asY.toLowerCase() + ".armor")) {
            for (String var2 : this.atc.jK("attribute_modifiers." + this.asY.toLowerCase())) {
               this.atc.method7074("equipment.slot_attribute_modifiers." + var2, this.atc.kf("attribute_modifiers." + this.asY.toLowerCase() + "." + var2));
            }
         }

         if (this.atc.jK("attribute_modifiers").size() == 1) {
            this.atc.method7074("attribute_modifiers", null);
         }

         this.atc.method7074("specific_properties.armor", null);
         if (this.atc.jK("specific_properties").isEmpty()) {
            this.atc.method7074("specific_properties", null);
         }

         this.ata.method7048("armors_rendering." + this.asZ, null);
      } catch (Exception var3) {
         throw new ItemConversionException("Failed to apply legacy_armor conversion for item '" + this.atb + "'", var3);
      }
   }

   @Override
   public String GN() {
      return "Convert legacy armor '" + this.atb + "' to new equipment format";
   }

   @Generated
   public String GO() {
      return this.asZ;
   }
}
