package dev.lone.itemsadder.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import org.bukkit.Bukkit;

public class FileLoggerFactory {
   private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
   private static boolean enabled = true;

   public static boolean canWrite(File var0) {
      if (!enabled) {
         return true;
      }

      if (!var0.exists()) {
         return true;
      }

      if (var0.length() == 0L) {
         return true;
      }

      try {
         String var1 = dateFormat.format(new Date());
         String var2 = var0.getName() + "." + var1;
         File var3 = new File(var0.getParentFile(), var2);
         Files.copy(var0.toPath(), var3.toPath(), StandardCopyOption.REPLACE_EXISTING);
         return true;
      } catch (IOException var4) {
         Bukkit.getLogger().log(Level.WARNING, "Failed to create backup for file: " + var0.getAbsolutePath(), var4);
         return false;
      }
   }

   public static FileLogWriter create(File var0, String var1) {
      if (var0.exists() && var0.length() != 0L) {
         String var2 = dateFormat.format(new Date());
         File var3 = new File(var0.getParentFile(), var0.getName() + ".backup." + var2);
         Files.copy(var0.toPath(), var3.toPath(), StandardCopyOption.REPLACE_EXISTING);
         return new FileLogWriter(var0, var3, var1);
      } else {
         return new FileLogWriter(var0, null, var1);
      }
   }

   public static FileLogWriter createDefault(File var0) {
      return create(var0, null);
   }

   public static void setEnabled(boolean var0) {
      enabled = var0;
   }

   public static boolean isEnabled() {
      return enabled;
   }
}
