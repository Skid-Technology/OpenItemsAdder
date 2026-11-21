package dev.lone.itemsadder.util;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class FileVersionReader {
   public final int minorVersion;
   public final int majorVersion;

   public FileVersionReader(File var1) {
      try (InputStream var2 = Files.newInputStream(var1.toPath())) {
         if (var2.read() == 137 && var2.read() == 80 && var2.read() == 78) {
            var2.skip(15L);
            this.majorVersion = readVersionNumber(var2);
            var2.skip(2L);
            this.minorVersion = readVersionNumber(var2);
         } else {
            this.majorVersion = 0;
            this.minorVersion = 0;
         }
      }
   }

   private static int readVersionNumber(InputStream var0) {
      int var1 = 0;
      byte var2 = 8;

      for (int var3 = 0; var3 < 2; var3++) {
         var1 |= var0.read() << var2;
         var2 -= 8;
      }

      return var1;
   }
}
