package dev.lone.itemsadder.util;

import com.google.common.primitives.Longs;
import java.io.File;
import java.nio.file.Files;

public class FileHelper {
   public static void storeModifiedTime(File var0, String var1) {
      try {
         long var2 = safeLastModified(var0);
         Files.setAttribute(var0.toPath(), "user:" + var1, Longs.toByteArray(var2));
         if (!var0.setLastModified(var2)) {
            throw new RuntimeException("Failed to set file lastModified attribute. Check your FTP permissions.");
         }
      } catch (Throwable var4) {
         Log.error("Can't set file file attribute " + var1 + ": " + var0.getAbsolutePath(), var4);
      }
   }

   public static boolean isModifiedTimeUnchanged(File var0, String var1) {
      try {
         long var2 = Longs.fromByteArray((byte[])Files.getAttribute(var0.toPath(), "user:" + var1));
         return var2 == var0.lastModified();
      } catch (Throwable var4) {
         return false;
      }
   }

   private static long safeLastModified(File var0) {
      long var1 = var0.lastModified();
      if (var1 < 0L) {
         var1 = System.currentTimeMillis();
      }

      return var1;
   }
}
