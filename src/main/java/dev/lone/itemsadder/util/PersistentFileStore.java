package dev.lone.itemsadder.util;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PersistentFileStore {
   public static final int bND = 16384;
   static final int bNE = 4;
   static final int bNF = 1024;
   static final byte[] MAGIC = new byte[]{85, 76, 6, 1};
   static final int MAGIC_LENGTH = MAGIC.length;
   static final int bNI = 35;
   static final int MAX_HEADER_SIZE = MAGIC_LENGTH + 4 + 35840;
   static final int MAX_NAME_LENGTH = 23;
   private RandomAccessFile file;
   public final File storageFile;
   boolean opened;
   RecordIndex index;
   public final Map entries = new ConcurrentHashMap();

   public PersistentFileStore(File var1) {
      this.storageFile = var1;
      if (var1.getParentFile() != null) {
         Files.createDirectories(var1.getParentFile().toPath());
      }

      try {
         this.ensureOpen();
         this.loadIndex();
      } catch (IOException | RuntimeException var3) {
         this.close();
         throw var3;
      }
   }

   @Deprecated
   public Map getEntries() {
      return this.entries;
   }

   @Nullable
   public Object get(String var1) {
      return this.entries.get(var1);
   }

   public boolean contains(String var1) {
      return this.index != null && this.index.getHeader(var1) != null;
   }

   @Nullable
   public Object remove(String var1) {
      return this.entries.remove(var1);
   }

   public String absolutePath() {
      return this.storageFile.getAbsolutePath();
   }

   private void ensureOpen() {
      if (!this.isOpenValid()) {
         this.file = new RandomAccessFile(this.storageFile, "rw");
         this.opened = true;
      }
   }

   private boolean isOpenValid() {
      try {
         return this.file == null ? false : this.file.getFD().valid();
      } catch (IOException var2) {
         return false;
      }
   }

   public void loadIndex() {
      this.index = new RecordIndex();
      if (this.opened) {
         this.ensureOpen();
         if (this.file.length() != 0L) {
            if (this.file.length() < MAGIC_LENGTH + 4) {
               throw new IOException("Truncated CData header. " + this.absolutePath());
            }

            this.file.seek(0L);
            byte[] var1 = new byte[MAGIC_LENGTH];
            this.file.read(var1);
            if (!Arrays.equals(MAGIC, var1)) {
               throw new IOException("Region header not found! " + this.absolutePath());
            }

            int var2 = BinaryIntCodec.readInt(this.file);
            if (var2 >= MAGIC_LENGTH + 4 && var2 <= MAX_HEADER_SIZE && var2 <= this.file.length()) {
               this.index.setVersion(var2);

               while (this.file.getFilePointer() < var2) {
                  String var3 = BinaryIntCodec.readLengthPrefixedString(this.file, 23L);
                  RecordHeader var4 = new RecordHeader(BinaryIntCodec.readInt(this.file), BinaryIntCodec.readInt(this.file));
                  this.index.putHeader(var3, var4);
               }
            } else {
               throw new IOException("Invalid CData header size! '" + var2 + "'. " + this.absolutePath());
            }
         }
      }
   }

   @NotNull
   public Object getOrCompute(String var1, UnaryFunctionD/* @NotNull */ var2) {
      Object var3 = null;

      try {
         var3 = this.readEntry(var1);
      } catch (EOFException var5) {
      }

      if (var3 == null) {
         var3 = var2.call(var1);
         this.entries.put(var1, var3);
      }

      return var3;
   }

   @Nullable
   public Object readEntry(String var1) {
      if (!this.opened) {
         return null;
      }

      this.ensureOpen();
      if (this.file.length() < MAGIC_LENGTH) {
         return null;
      }

      RecordHeader var2 = this.index.getHeader(var1);
      if (var2 == null) {
         return null;
      }

      PackedByteStream var3 = BinaryIntCodec.readRecordBody(this.file, this.index, var2);
      if (var3.size() > 0) {
         try {
            Object var4 = this.method7481(var3.toInputStream(), var2.length);
            this.entries.put(var1, var4);
            return var4;
         } catch (EOFException var5) {
            Log.error("Error reading chunk serialized entry '" + var1 + "'" + this.absolutePath(), var5);
            throw new EOFException("Chunk stream somehow goes outside the file size.");
         } catch (Throwable var6) {
            Log.error("Error reading chunk serialized entry '" + var1 + "'" + this.absolutePath(), var6);
            throw var6;
         }
      } else {
         return null;
      }
   }

   protected abstract Object method7481(InputStream var1, int var2);

   protected abstract OutputStream method7482(PackedByteStream var1, Object var2);

   protected abstract boolean method7483(Object var1);

   public void saveAll(boolean var1, boolean var2) {
      this.ensureOpen();
      RecordIndex var3 = new RecordIndex();
      RecordWriter var4 = var3.createWriter();
      PackedByteStream var5 = new PackedByteStream();
      this.copyUntouchedRecords(this.index, var4, var5);

      for (Entry var7 : this.entries.entrySet()) {
         if (this.method7483(var7.getValue())) {
            this.writeEntry((String)var7.getKey(), var7.getValue(), var4, var5);
         }
      }

      var4.finish();
      Path var18 = this.storageFile.toPath().toAbsolutePath();
      Path var19 = Files.createTempFile(var18.getParent(), ".ia-region-", ".tmp");

      try {
         RandomAccessFile var8 = new RandomAccessFile(var19.toFile(), "rw");

         try {
            var4.writeTo(var8);
            var5.writeTo(var8);
         } catch (Throwable var16) {
            try {
               var8.close();
            } catch (Throwable var15) {
               var16.addSuppressed(var15);
            }

            throw var16;
         }

         var8.close();
         this.close();
         Files.move(var19, var18, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         this.index = var3;
         if (!var1) {
            this.ensureOpen();
         }

         if (var2) {
            this.entries.clear();
         }
      } finally {
         Files.deleteIfExists(var19);
      }
   }

   private void copyUntouchedRecords(RecordIndex var1, RecordWriter var2, PackedByteStream var3) {
      if (this.file.length() >= MAGIC_LENGTH) {
         for (Entry var5 : var1.headers.entrySet()) {
            String var6 = (String)var5.getKey();
            RecordHeader var7 = (RecordHeader)var5.getValue();
            if (!this.entries.containsKey(var6)) {
               PackedByteStream var8 = BinaryIntCodec.readRecordBody(this.file, var1, var7);
               if (var8.size() > 0) {
                  int var9 = var3.size();
                  var8.writeTo(var3);
                  var2.writeEntry(var6, var9, var8.size());
               }
            }
         }
      }
   }

   private void writeEntry(String var1, Object var2, RecordWriter var3, ByteArrayOutputStream var4) {
      PackedByteStream var5 = new PackedByteStream();
      OutputStream var6 = this.method7482(var5, var2);
      var6.close();
      int var7 = var4.size();
      var5.writeTo(var4);
      var3.writeEntry(var1, var7, var5.size());
   }

   public void close() {
      if (this.file != null) {
         try {
            this.file.close();
         } catch (Throwable var2) {
            Log.error("Error closing region file I/O " + this.absolutePath(), var2);
         }
      }
   }
}
