package dev.lone.itemsadder.item.conversion;

import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.internal.SwitchMap2;
import org.bukkit.entity.EntityType;

public class FurnitureToItemDisplayConversion extends AbstractItemConversion {
   private EntityType vw;

   public FurnitureToItemDisplayConversion(YamlConfig var1, String var2, ConfigSectionWrapper var3) {
      super(var1, var2, var3);
   }

   @Override
   public String GK() {
      return "furniture_to_item_display";
   }

   @Override
   public boolean needsConversion() {
      if (!this.atc.jI("behaviours.furniture")) {
         return false;
      } else {
         this.vw = this.atc.method7109("behaviours.furniture.entity", EntityType.ARMOR_STAND, null);
         if (this.vw == null) {
            throw new ItemConversionException("Item '" + this.atb + "' has null entity type", true);
         } else if ("item_display".equals(this.atc.jO("behaviours.furniture.entity"))) {
            throw new ItemConversionException("Item '" + this.atb + "' is already item_display", true);
         } else {
            return true;
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void convert() {
      try {
         this.atc.method7074("behaviours.furniture.entity", "item_display");

         boolean var1 = switch (SwitchMap2.atj[this.vw.ordinal()]) {
            case 1 -> {
               this.atc.method7074("behaviours.furniture.display_transformation.source_entity", "item_frame");
               yield true;
            }
            case 2 -> {
               if (this.atc.method7078("behaviours.furniture.small", false)) {
                  this.atc.method7074("behaviours.furniture.display_transformation.source_entity", "small_armor_stand");
                  this.atc.method7074("behaviours.furniture.small", null);
               } else {
                  this.atc.method7074("behaviours.furniture.display_transformation.source_entity", "armor_stand");
               }

               yield true;
            }
            default -> false;
         };
         if (var1 && this.atc.jI("behaviours.furniture.hitbox.width_offset")) {
            this.atc.method7074("behaviours.furniture.hitbox.width_offset", -this.atc.jQ("behaviours.furniture.hitbox.width_offset"));
         }
      } catch (Exception var2) {
         throw new ItemConversionException("Failed to apply furniture_to_item_display conversion for item '" + this.atb + "'", var2);
      }
   }

   @Override
   public String GN() {
      return "Convert furniture entity to item_display for item '" + this.atb + "'";
   }
}
