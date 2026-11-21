package dev.lone.itemsadder.lang;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.fontimage.EmojiReplacer;
import dev.lone.itemsadder.util.LocalizedMessages;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

public final class LocaleLoader {
   private LocaleLoader() {
   }

   static void method6955(YamlConfig var0, LocalizedMessages var1) {
      ConfigurationSection var2 = var0.kb("dictionary");
      if (var2 == null) {
         if (Settings.get().isLoggingEnabled("logging.translations.dictionaries_loading")) {
            Log.log(ChatColor.YELLOW + "    No dictionary found in file " + var0.DJ());
         }
      } else if (!var0.contains("info.dictionary-lang")) {
         Log.log(ChatColor.RED + "Error: No 'info.lang' property specified in file: " + var0.DJ());
      } else {
         String var3 = var0.getString("info.dictionary-lang");
         if (var3.equals("en")) {
            for (String var5 : var2.getKeys(true)) {
               if (var2.get(var5) instanceof String) {
                  var1.putDefault(var5, var2.getString(var5));
               }
            }
         } else if (var3.equals(Settings.get().dictionariesLang)) {
            for (String var7 : var2.getKeys(true)) {
               if (var2.get(var7) instanceof String) {
                  var1.putOverride(var7, var2.getString(var7));
               }
            }
         } else if (Settings.get().isLoggingEnabled("logging.translations.dictionaries_loading")) {
            Log.log(ChatColor.YELLOW + "    Dictionary lang '" + var3 + "' not supported in file " + var0.DJ());
         }
      }
   }

   static void method6956(YamlConfig var0, MinecraftLocales var1) {
      ConfigurationSection var2 = var0.kb("minecraft_lang_overwrite");
      if (var2 == null) {
         if (Settings.get().isLoggingEnabled("logging.translations.minecraft_lang_overwrite_loading")) {
            Log.log(ChatColor.YELLOW + "    No 'minecraft_lang_overwrite' found in file " + var0.GQ());
         }
      } else {
         try {
            var2 = YamlConfig.method7045(var0.getFile(), '*').getConfigurationSection("minecraft_lang_overwrite");
         } catch (IOException | InvalidConfigurationException var13) {
            Log.error("Error loading Minecraft language overwrites from " + var0.GQ(), var13);
            return;
         }

         if (var2 != null) {
            for (String var4 : var2.getKeys(false)) {
               ConfigurationSection var5 = var2.getConfigurationSection(var4);
               if (var5 != null && var5.getBoolean("enabled", true)) {
                  ConfigurationSection var6 = var5.getConfigurationSection("entries");
                  if (var6 != null) {
                     Map var7 = method6957(var6);
                     List var8;
                     if (var5.getStringList("languages").contains("ALL")) {
                        var8 = MinecraftLocales.aut;
                     } else {
                        var8 = var5.getStringList("languages");
                     }

                     for (String var10 : var8) {
                        for (Entry var12 : var7.entrySet()) {
                           var1.method2037(var10, (String)var12.getKey(), Utils.toSectionCodes(EmojiReplacer.replaceFontImages((String)var12.getValue())));
                        }
                     }
                  }
               }
            }
         }
      }
   }

   static Map method6957(ConfigurationSection var0) {
      LinkedHashMap var1 = new LinkedHashMap();

      for (String var3 : var0.getKeys(true)) {
         if (var0.get(var3) instanceof String var4) {
            var1.put(var3.replace('*', '.'), var4);
         }
      }

      return var1;
   }
}
