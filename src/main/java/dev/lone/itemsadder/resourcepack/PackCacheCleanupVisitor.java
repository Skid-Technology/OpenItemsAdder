package dev.lone.itemsadder.resourcepack;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;

class PackCacheCleanupVisitor extends SimpleFileVisitor {
   PackCacheCleanupVisitor(PackCacheStore var1) {
   }

   public FileVisitResult deleteEmptyDir(Path var1, IOException var2) {
      PackCacheStore.checkInterrupted();
      if (var2 != null) {
         throw var2;
      }

      try {
         Files.delete(var1);
      } catch (DirectoryNotEmptyException var4) {
      }

      return FileVisitResult.CONTINUE;
   }
}
