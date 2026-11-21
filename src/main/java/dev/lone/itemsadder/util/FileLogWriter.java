package dev.lone.itemsadder.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import org.bukkit.Bukkit;

public class FileLogWriter {
   private final File target;
   private final File backup;
   private final String backupSubfolder;
   private boolean committed = false;

   private FileLogWriter(File var1, File var2, String var3) {
      this.target = var1;
      this.backup = var2;
      this.backupSubfolder = var3;
   }

   public void rollback() {
      if (this.backup != null && this.backup.exists()) {
         Files.copy(this.backup.toPath(), this.target.toPath(), StandardCopyOption.REPLACE_EXISTING);
         Bukkit.getLogger().info("Rolled back file: " + this.target.getName() + " from backup");
      }
   }

   public void commit() {
      if (this.backup != null && this.backup.exists()) {
         try {
            File var1;
            if (this.backupSubfolder != null) {
               File var2 = new File(this.target.getParentFile(), this.backupSubfolder);
               if (!var2.exists()) {
                  var2.mkdirs();
               }

               var1 = new File(var2, this.target.getName());
            } else {
               String var4 = this.target.getName() + ".old." + System.currentTimeMillis();
               var1 = new File(this.target.getParentFile(), var4);
            }

            Files.move(this.backup.toPath(), var1.toPath(), StandardCopyOption.REPLACE_EXISTING);
            this.committed = true;
         } catch (IOException var3) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to move backup file: " + this.backup.getAbsolutePath(), var3);
         }
      } else {
         this.committed = true;
      }
   }

   public void discardBackup() {
      if (this.backup != null && this.backup.exists()) {
         this.backup.delete();
      }

      this.committed = true;
   }

   @Override
   protected void finalize() {
      try {
         if (!this.committed && this.backup != null && this.backup.exists()) {
            this.backup.delete();
         }
      } finally {
         super.finalize();
      }
   }
}
