package dev.lone.itemsadder.item.conversion;

import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import java.util.List;

public class ItemConversionScanner {
   private final YamlConfig ath;
   private final List ati;

   public ItemConversionScanner(YamlConfig var1, List var2) {
      this.ath = var1;
      this.ati = var2;
   }

   public ItemConversionBatch buildBatch() {
      ItemConversionBatch var1 = new ItemConversionBatch(this.ath);

      for (String var3 : this.ati) {
         ConfigSectionWrapper var4 = this.ath.kc("items." + var3);
         if (var4 != null && var4.method7078("enabled", true)) {
            this.method1977(var1, var3, var4);
            this.method1978(var1, var3, var4);
            this.method1979(var1, var3, var4);
         }
      }

      return var1;
   }

   private void method1977(ItemConversionBatch var1, String var2, ConfigSectionWrapper var3) {
      if (Settings.get().convertFurnitureToItemDisplay) {
         FurnitureToItemDisplayConversion var4 = new FurnitureToItemDisplayConversion(this.ath, var2, var3);

         try {
            if (!var4.needsConversion()) {
               return;
            }

            var1.add(var4);
         } catch (ItemConversionException var6) {
            if (!var6.GR()) {
               ItemConversionException.method333("[Converter] " + var6.getMessage() + " File: " + this.ath.DJ(), var6);
            }
         }
      }
   }

   private void method1978(ItemConversionBatch var1, String var2, ConfigSectionWrapper var3) {
      if (Settings.get().convertResourceToGraphics) {
         ResourceToGraphicsConversion var4 = new ResourceToGraphicsConversion(this.ath, var2, var3);

         try {
            if (!var4.needsConversion()) {
               return;
            }

            var1.add(var4);
         } catch (ItemConversionException var6) {
            if (var6.GR()) {
               ItemConversionException.method334("[Converter] " + var6.getMessage() + " File: " + this.ath.DJ(), var6);
            } else {
               ItemConversionException.method333("[Converter] " + var6.getMessage() + " File: " + this.ath.DJ(), var6);
            }
         }
      }
   }

   private void method1979(ItemConversionBatch var1, String var2, ConfigSectionWrapper var3) {
      if (Settings.get().legacyArmorAppendEquipment) {
         AppendEquipmentArmorConversion var4 = new AppendEquipmentArmorConversion(this.ath, var2, var3);

         try {
            if (!var4.needsConversion()) {
               return;
            }

            var1.add(var4);
         } catch (ItemConversionException var7) {
            if (var7.GR()) {
               ItemConversionException.method334("[Converter] " + var7.getMessage() + " File: " + this.ath.DJ(), var7);
            } else {
               ItemConversionException.method333("[Converter] " + var7.getMessage() + " File: " + this.ath.DJ(), var7);
            }
         }
      } else if (Settings.get().legacyArmorFullConversion) {
         LegacyArmorCompleteConversion var8 = new LegacyArmorCompleteConversion(this.ath, var2, var3);

         try {
            if (!var8.needsConversion()) {
               return;
            }

            var1.add(var8);
         } catch (ItemConversionException var6) {
            if (var6.GR()) {
               ItemConversionException.method334("[Converter] " + var6.getMessage() + " File: " + this.ath.DJ(), var6);
            } else {
               ItemConversionException.method333("[Converter] " + var6.getMessage() + " File: " + this.ath.DJ(), var6);
            }
         }
      }
   }
}
