package dev.lone.itemsadder.item.conversion;

import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.YamlConfig;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

public class ResourceToGraphicsConversion extends AbstractItemConversion {
   private ConfigSectionWrapper atk;

   public ResourceToGraphicsConversion(YamlConfig var1, String var2, ConfigSectionWrapper var3) {
      super(var1, var2, var3);
   }

   @Override
   public String GK() {
      return "resource_to_graphics";
   }

   @Override
   public boolean needsConversion() {
      if (!this.atc.jI("resource")) {
         return false;
      }

      this.atk = this.atc.kc("resource");
      if (this.atk == null) {
         throw new ItemConversionException("Item '" + this.atb + "' has null resource section", true);
      }

      if (!this.atk.hA("custom_model_data") && !this.atk.hA("model_id")) {
         boolean var1 = this.atk.method7078("generate", false);
         boolean var2 = this.atk.hA("model_path");
         if (!var1 && !var2) {
            throw new ItemConversionException("Item '" + this.atb + "' has neither 'generate' nor 'model_path', needs manual conversion", true);
         }

         if (var1) {
            boolean var3 = this.atk.hA("texture");
            List var4 = this.atk.jZ("textures");
            if (!var3 && var4.isEmpty()) {
               if (this.atc.hA("specific_properties.armor")) {
                  throw new ItemConversionException("Item '" + this.atb + "' has 'generate' but no texture/textures, skipping legacy armor.", true, true);
               }

               throw new ItemConversionException("Item '" + this.atb + "' has 'generate' but no texture/textures", true);
            }

            if (!var3 && var4.size() > 1 && !this.atc.hA("specific_properties.block")) {
               throw new ItemConversionException("Item '" + this.atb + "' has multiple textures but is not a block, needs manual conversion", true);
            }
         }

         return true;
      } else {
         throw new ItemConversionException("Item '" + this.atb + "' has 'custom_model_data' or 'model_id', needs manual conversion", true);
      }
   }

   @Override
   public void convert() {
      try {
         boolean var1 = this.atc.jI("specific_properties.block") || this.atc.jI("behaviours.block");
         Material var2 = this.atk.method7105("material", null);
         if (var2 != null) {
            if (var1) {
               if (var2 != Material.PAPER) {
                  this.atc.method7074("material", var2.toString());
               }
            } else {
               this.atc.method7074("material", var2.toString());
            }
         }

         if (var2 == Material.TRIDENT) {
            throw new ItemConversionException("Item '" + this.atb + "' is a trident, needs manual conversion to properly set throwing texture", true, true);
         }

         if (this.atc.hA("display_name")) {
            this.atc.method7074("name", this.atc.jO("display_name"));
            this.atc.method7074("display_name", null);
         }

         if (this.atc.jI("enabled") && this.atc.kj("enabled")) {
            this.atc.method7074("enabled", null);
         }

         ConfigurationSection var3 = this.atc.kg("graphics");
         if (this.atk.method7078("generate", false)) {
            String var4 = method1974(this.atk);
            if (var2 == Material.BOW) {
               ConfigurationSection var5 = var3.createSection("textures");
               var5.set("normal", var4);
               var5.set("pulling_0", var4 + "_0");
               var5.set("pulling_1", var4 + "_1");
               var5.set("pulling_2", var4 + "_2");
            } else if (var2 == Material.CROSSBOW) {
               ConfigurationSection var9 = var3.createSection("textures");
               var9.set("normal", var4);
               var9.set("pulling_0", var4 + "_0");
               var9.set("pulling_1", var4 + "_1");
               var9.set("pulling_2", var4 + "_2");
               var9.set("arrow", var4 + "_charged");
               var9.set("rocket", var4 + "_firework");
            } else if (var2 == Material.SHIELD) {
               ConfigurationSection var10 = var3.createSection("textures");
               var10.set("normal", var4);
               var10.set("blocking", var4 + "_blocking");
            } else if (var2 == Material.FISHING_ROD) {
               ConfigurationSection var11 = var3.createSection("textures");
               var11.set("normal", var4);
               var11.set("cast", var4 + "_cast");
            } else if (this.atk.hA("texture")) {
               var3.set("texture", var4);
            } else {
               List var12 = this.atk.jZ("textures");
               if (var12.size() == 1) {
                  var3.set("texture", var12.getFirst());
               } else {
                  if (!var1) {
                     throw new ItemConversionException("Item '" + this.atb + "' has multiple textures but is not a block, needs manual conversion", true);
                  }

                  ConfigurationSection var6 = var3.createSection("textures");
                  var6.set("down", var12.get(0));
                  var6.set("east", var12.get(1));
                  var6.set("north", var12.get(2));
                  var6.set("south", var12.get(3));
                  var6.set("up", var12.get(4));
                  var6.set("west", var12.get(5));
               }
            }
         } else {
            String var8 = this.atk.jO("model_path");
            if (var2 == Material.BOW) {
               ConfigurationSection var13 = var3.createSection("models");
               var13.set("normal", var8);
               var13.set("pulling_0", var8 + "_0");
               var13.set("pulling_1", var8 + "_1");
               var13.set("pulling_2", var8 + "_2");
            } else if (var2 == Material.CROSSBOW) {
               ConfigurationSection var14 = var3.createSection("models");
               var14.set("normal", var8);
               var14.set("pulling_0", var8 + "_0");
               var14.set("pulling_1", var8 + "_1");
               var14.set("pulling_2", var8 + "_2");
               var14.set("arrow", var8 + "_charged");
               var14.set("rocket", var8 + "_firework");
            } else if (var2 == Material.SHIELD) {
               ConfigurationSection var15 = var3.createSection("models");
               var15.set("normal", var8);
               var15.set("blocking", var8 + "_blocking");
            } else if (var2 == Material.FISHING_ROD) {
               ConfigurationSection var16 = var3.createSection("models");
               var16.set("normal", var8);
               var16.set("cast", var8 + "_cast");
            } else {
               var3.set("model", var8);
            }
         }

         if (this.atk.jI("icon")) {
            var3.set("icon", this.atk.jO("icon"));
         }

         this.atc.method7074("resource", null);
         this.GY();
      } catch (Exception var7) {
         throw new ItemConversionException("Failed to apply resource_to_graphics conversion for item '" + this.atb + "'", var7);
      }
   }

   @Nullable
   private static String method1974(ConfigSectionWrapper var0) {
      if (var0.hA("texture")) {
         return var0.jO("texture");
      }

      List var1 = var0.jZ("textures");
      return var1.size() != 1 ? null : (String)var1.getFirst();
   }

   private void GY() {
      LinkedHashMap var1 = new LinkedHashMap();

      for (String var3 : this.atc.bJ(false)) {
         var1.put(var3, this.atc.kf(var3));
      }

      for (String var6 : new ArrayList(this.atc.bJ(false))) {
         this.atc.method7074(var6, null);
      }

      if (var1.containsKey("name")) {
         this.atc.method7074("name", var1.remove("name"));
      }

      if (var1.containsKey("material")) {
         this.atc.method7074("material", var1.remove("material"));
      }

      if (var1.containsKey("graphics")) {
         this.atc.method7074("graphics", var1.remove("graphics"));
      }

      for (Entry var7 : var1.entrySet()) {
         this.atc.method7074((String)var7.getKey(), var7.getValue());
      }
   }

   @Override
   public String GN() {
      return "Convert 'resource' to 'graphics' for item '" + this.atb + "'";
   }
}
