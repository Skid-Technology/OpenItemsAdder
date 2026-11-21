package dev.lone.itemsadder.config;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.conversion.ItemConversionBatch;
import dev.lone.itemsadder.item.conversion.ItemConversionException;
import dev.lone.itemsadder.item.conversion.ItemConversionScanner;
import dev.lone.itemsadder.util.FileLogWriter;
import dev.lone.itemsadder.util.FileLoggerFactory;
import dev.lone.itemsadder.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.bukkit.ChatColor;

public class ConfigConversionBackup {
   public static void Ge() {
      String var0 = "conversion_backup_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

      for (YamlConfig var2 : ContentManager.method8084().method8092()) {
         List var3 = var2.getStringList("items");
         if (var3.isEmpty()) {
            if (Settings.get().isLoggingEnabled("logging.config_files.legacy_empty_files")) {
               Log.log(ChatColor.YELLOW + "    No item found in file " + var2.GQ());
            }
         } else {
            method1911(var2, var3, var0);
         }
      }
   }

   private static void method1911(YamlConfig var0, List var1, String var2) {
      FileLogWriter var3 = null;

      try {
         ItemConversionScanner var4 = new ItemConversionScanner(var0, var1);
         ItemConversionBatch var5 = var4.buildBatch();
         if (var5.isEmpty()) {
            return;
         }

         var3 = FileLoggerFactory.create(var0.getFile(), var2);
         var5.GT();
         var0.save();
         var3.commit();
         Log.log(ChatColor.GREEN + "[Converter] Successfully converted " + var5.size() + " item(s) in file: " + var0.DJ());
      } catch (ItemConversionException var6) {
         Log.error("[Converter] Conversion failed, rolling back. File: " + var0.DJ(), var6);
         method1912(var0, var3);
      } catch (Exception var7) {
         Log.error("[Converter] Unexpected error during conversion, rolling back. File: " + var0.DJ(), var7);
         method1912(var0, var3);
      }
   }

   private static void method1912(YamlConfig var0, FileLogWriter var1) {
      if (var1 != null) {
         try {
            var1.rollback();
            var0.reload();
            Log.warn("[Converter] Rolled back changes for file: " + var0.DJ());
         } catch (Exception var3) {
            Log.error("[Converter] Failed to rollback file: " + var0.DJ(), var3);
         }
      }
   }
}
