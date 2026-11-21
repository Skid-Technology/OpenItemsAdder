package dev.lone.itemsadder.config;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public final class YamlFileWriter {
   private YamlFileWriter() {
   }

   public static void method7539(Path var0, String var1) {
      Properties var2 = new Properties();
      if (Files.exists(var0)) {
         try (InputStream var3 = Files.newInputStream(var0)) {
            var2.load(var3);
         }
      }

      String var19 = var2.getProperty("current-version");
      if (!var1.equals(var19)) {
         if (var19 != null) {
            var2.setProperty("previous-version", var19);
         }

         var2.setProperty("current-version", var1);
         Files.createDirectories(var0.getParent());
         Path var4 = Files.createTempFile(var0.getParent(), "plugin-version-", ".tmp");

         try {
            try (OutputStream var5 = Files.newOutputStream(var4)) {
               var2.store(var5, "ItemsAdder version history");
            }

            Files.move(var4, var0, StandardCopyOption.REPLACE_EXISTING);
         } finally {
            Files.deleteIfExists(var4);
         }
      }
   }
}
