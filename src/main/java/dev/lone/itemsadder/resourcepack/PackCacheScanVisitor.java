package dev.lone.itemsadder.resourcepack;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

class PackCacheScanVisitor extends SimpleFileVisitor {
   PackCacheScanVisitor(Path var1, Map var2) {
      this.bGM = var1;
      this.bGN = var2;
   }

   public FileVisitResult visitRegularFile(Path var1, BasicFileAttributes var2) {
      PackCacheStore.checkInterrupted();
      if (var2.isSymbolicLink()) {
         throw new IOException("Symbolic link in vanilla cache: " + var1);
      }

      if (var2.isRegularFile()) {
         String var3 = this.bGM.relativize(var1).toString().replace(File.separatorChar, '/');
         PackCacheStore.resolveSafe(this.bGM, var3);
         this.bGN.put(var3, var2);
      }

      return FileVisitResult.CONTINUE;
   }
}
