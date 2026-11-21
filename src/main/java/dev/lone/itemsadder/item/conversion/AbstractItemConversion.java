package dev.lone.itemsadder.item.conversion;

import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.YamlConfig;

public abstract class AbstractItemConversion implements ItemConversion {
   protected final YamlConfig ata;
   protected final String atb;
   protected final ConfigSectionWrapper atc;

   public AbstractItemConversion(YamlConfig var1, String var2, ConfigSectionWrapper var3) {
      this.ata = var1;
      this.atb = var2;
      this.atc = var3;
   }

   @Override
   public String GP() {
      return this.atb;
   }

   protected String GQ() {
      return this.ata.DJ();
   }
}
