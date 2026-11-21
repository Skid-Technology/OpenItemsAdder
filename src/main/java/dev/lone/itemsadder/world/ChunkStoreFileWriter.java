package dev.lone.itemsadder.world;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.serial.SerialByteSlice;
import dev.lone.itemsadder.util.serial.SerialRecordE;
import dev.lone.itemsadder.util.serial.StreamSerialWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.bukkit.Chunk;

public class ChunkStoreFileWriter {
   private static final int BUFFER_SIZE = 8192;
   private SerialRecordE chunkIndex;
   private File cacheFile;
   private final AtomicBoolean dirty = new AtomicBoolean();
   private final Lock lock;
   private final File regionFolder;

   public ChunkStoreFileWriter(File var1) {
      this.regionFolder = var1;
      this.lock = new ReentrantReadWriteLock().writeLock();
   }

   void loadCache() {
      if (Settings.get().unregBlockstatesRestoreVanilla) {
         this.cacheFile = new File(this.regionFolder, "fixed_glitched_blocks.pregion");
         if (this.cacheFile.exists()) {
            try {
               if (this.cacheFile.exists() && this.cacheFile.length() / 1024L >= 256L) {
                  if (this.cacheFile.delete()) {
                     Log.log("Cleaned up fixed_glitched_blocks.nbt file (size > 512KB)");
                  } else {
                     Log.error("Failed to clean up fixed_glitched_blocks.nbt file (size > 512KB)");
                  }
               }

               GZIPInputStream var1 = new GZIPInputStream(new FileInputStream(this.cacheFile));

               try (var1) {
                  this.chunkIndex = SerialRecordE.create();
                  ChunkFileStore.readRecord(var1, this.chunkIndex);
               }
            } catch (Exception var7) {
               if (this.cacheFile.delete()) {
                  Log.warn("Error loading world '" + this.cacheFile.getName() + "' file. Regenerated: " + this.cacheFile.getAbsoluteFile());
               } else {
                  Log.error(
                     "Error loading file, also failed to delete it. Stop the server and try to delete the file manually: " + this.cacheFile.getAbsolutePath(),
                     var7
                  );
               }
            }
         } else {
            this.chunkIndex = SerialRecordE.create();
         }
      }
   }

   void saveCache() {
      if (Settings.get().unregBlockstatesRestoreVanilla) {
         if (this.dirty.get()) {
            try {
               this.lock.lock();
               SchedulerUtils.submitAsync(
                  () -> {
                     try {
                        if (!this.cacheFile.exists()) {
                           Files.createDirectories(this.cacheFile.getParentFile().toPath());

                           try {
                              Files.createFile(this.cacheFile.toPath());
                           } catch (FileAlreadyExistsException var4) {
                           }
                        }

                        GZIPOutputStream var1 = new GZIPOutputStream(new FileOutputStream(this.cacheFile));
                        SerialByteSlice var2 = SerialByteSlice.create(8192);
                        StreamSerialWriter var3x = new StreamSerialWriter(var2, var1);
                        SerialRecordE.TYPE.write(var3x, this.chunkIndex);
                        SerialByteSlice.writeAllTo(var1, var2);
                        var1.finish();
                        var1.close();
                     } catch (IOException var5) {
                        Log.error(
                           "Error saving 'already fixed glitched blocks' chunks cache, please try to delete the file: " + this.cacheFile.getAbsolutePath(),
                           var5
                        );
                     }
                  }
               );
            } finally {
               this.lock.unlock();
            }
         }
      }
   }

   public void markChunkFixed(Chunk var1) {
      if (this.chunkIndex != null) {
         this.chunkIndex.addChunk(CustomBlockChunkStoreBase.chunkKeyOf(var1));
         this.dirty.set(true);
      }
   }

   public boolean isChunkFixed(Chunk var1) {
      return this.chunkIndex == null ? false : this.chunkIndex.getChunks().contains(CustomBlockChunkStoreBase.chunkKeyOf(var1));
   }
}
