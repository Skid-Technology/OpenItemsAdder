package dev.lone.itemsadder.item.conversion;

import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.util.Log;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class ItemConversionBatch {
   private final YamlConfig atf;
   private final List atg = new ArrayList();

   public ItemConversionBatch(YamlConfig var1) {
      this.atf = var1;
   }

   public void add(ItemConversion var1) {
      this.atg.add(var1);
   }

   public boolean isEmpty() {
      return this.atg.isEmpty();
   }

   public int size() {
      return this.atg.size();
   }

   public void GT() {
      for (ItemConversion var2 : this.atg) {
         try {
            var2.convert();
            this.method1976(var2);
         } catch (ItemConversionException var4) {
            throw new ItemConversionException("Failed to apply conversion '" + var2.GK() + "' for item '" + var2.GP() + "': " + var4.getMessage(), var4);
         }
      }

      this.GU();
      this.GV();
   }

   private void method1976(ItemConversion var1) {
      switch (var1.GK()) {
         case "resource_to_graphics":
            Log.devLog("Converted 'resource' to 'graphics' for item '" + var1.GP() + "'. File: " + this.atf.DJ());
            break;
         case "furniture_to_item_display":
            Log.devLog("Converted furniture to item_display for item '" + var1.GP() + "'. File: " + this.atf.DJ());
            break;
         case "legacy_armor_complete":
            Log.log(ChatColor.AQUA + "[Converter] Converted legacy armor '" + var1.GP() + "' to new equipment format. File: " + this.atf.DJ());
            break;
         case "append_equipment_to_legacy_armor":
            Log.log(ChatColor.AQUA + "[Converter] Append new equipment to legacy armor '" + var1.GP() + "'. File: " + this.atf.DJ());
      }
   }

   private void GU() {
      if (this.atf.contains("equipments_tmp")) {
         this.atf.method7048("equipments", this.atf.kb("equipments_tmp"));
         this.atf.method7048("equipments_tmp", null);
      }

      if (this.atf.contains("armors_rendering") && this.atf.getStringList("armors_rendering").isEmpty()) {
         this.atf.method7048("armors_rendering", null);
      }
   }

   private void GV() {
      try {
         if (this.atf.contains("items")) {
            ConfigurationSection var1 = this.atf.kb("items");
            if (var1 != null) {
               this.atf.method7048("items", null);
               this.atf.method7048("items", var1);
            }
         }
      } catch (Exception var2) {
         Log.error("[Converter] Error moving 'items' key to the end of the file. File: " + this.atf.DJ(), var2);
      }
   }

   @Generated
   public YamlConfig getConfig() {
      return this.atf;
   }
}
