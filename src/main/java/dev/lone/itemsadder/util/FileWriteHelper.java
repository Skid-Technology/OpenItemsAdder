package dev.lone.itemsadder.util;

import java.io.File;
import java.util.function.Consumer;

public class FileWriteHelper {
   public static void deleteThen(File var0, Consumer var1) {
      if (var0.exists()) {
         if (!var0.delete()) {
            Log.error("Failed to delete file, probably no permission. " + var0.getAbsolutePath());
            return;
         }

         var1.accept(var0);
      }
   }
}
