package dev.lone.itemsadder.resourcepack;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.concurrent.atomic.LongAdder;

final class SolidifiedFrameCache {
   static final String CACHE_VERSION = "solidify_frames_1";
   static final String GENERATOR_VERSION = "26.2/88.0";
   static final String POLICY_TAG = "verified_ITEMS_mip0_no_item_bypass_no_raw_consumers";
   private static final int MAX_CACHE_BYTES = 67108864;
   private static final Object[] LOCKS = new Object[64];
   private final Path cacheDir;
   private final LongAdder hits = new LongAdder();
   private final LongAdder misses = new LongAdder();
   private final LongAdder errors = new LongAdder();
   private final LongAdder inputBytes = new LongAdder();
   private final LongAdder cacheReadBytes = new LongAdder();
   private final LongAdder cacheWriteBytes = new LongAdder();

   SolidifiedFrameCache(Path var1) {
      this.cacheDir = var1.toAbsolutePath().normalize();
   }

   CacheCounters counters() {
      return new CacheCounters(
         this.hits.sum(), this.misses.sum(), this.errors.sum(), this.inputBytes.sum(), this.cacheReadBytes.sum(), this.cacheWriteBytes.sum()
      );
   }

   static String cacheKey(byte[] var0, byte[] var1, String var2, String var3, String var4) {
      MessageDigest var5 = sha256();

      for (String var9 : new String[]{
         var2,
         var3,
         var4,
         Long.toString(Math.min(67108864L, Runtime.getRuntime().maxMemory() / 32L)),
         sha256Hex(var0),
         var1 == null ? "absent" : sha256Hex(var1)
      }) {
         var5.update(var9.getBytes(StandardCharsets.UTF_8));
         var5.update((byte)0);
      }

      return HexFormat.of().formatHex(var5.digest());
   }

   CachedAtlasEntry getOrSolidify(byte[] var1, byte[] var2, JsonObject var3, PackStatsTuple var4) {
      this.inputBytes.add(var1.length + (var2 == null ? 0 : var2.length));
      String var5 = cacheKey(var1, var2, "solidify_frames_1", "26.2/88.0", "verified_ITEMS_mip0_no_item_bypass_no_raw_consumers");
      synchronized (LOCKS[(var5.hashCode() & 2147483647) % LOCKS.length]) {
         checkInterrupted();
         FileChannel var7 = null;

         FileLock var8;
         try {
            Files.createDirectories(this.cacheDir);
            var7 = FileChannel.open(this.cacheDir.resolve(var5 + ".lock"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            var8 = var7.lock();
         } catch (IOException | RuntimeException var20) {
            closeQuietly(var7);
            checkInterrupted();
            this.errors.increment();
            this.misses.increment();
            return AtlasImageEncoder.optimizePng(var1, var3, var4);
         }

         CachedAtlasEntry var11;
         try {
            Path var9 = this.cacheDir.resolve(var5 + ".bin");

            try {
               if (Files.exists(var9)) {
                  CachedAtlasEntry var24 = this.readCacheEntry(var9, var5);
                  this.hits.increment();
                  return var24;
               }
            } catch (IOException | RuntimeException var21) {
               checkInterrupted();
               this.errors.increment();
            }

            this.misses.increment();
            CachedAtlasEntry var10 = AtlasImageEncoder.optimizePng(var1, var3, var4);
            if (var10 == null || var10.png().length < 67108864) {
               try {
                  byte[] var25 = serializeEntry(var5, var10);
                  if (var25.length <= 67108864) {
                     VanillaAssetCache.writeCacheFile(var9, var25);
                     this.cacheWriteBytes.add(var25.length);
                  }
               } catch (IOException | RuntimeException var19) {
                  checkInterrupted();
                  this.errors.increment();
               }

               return var10;
            }

            var11 = var10;
         } finally {
            closeQuietly(var8);
            closeQuietly(var7);
         }

         return var11;
      }
   }

   private CachedAtlasEntry readCacheEntry(Path var1, String var2) {
      if (!Files.isSymbolicLink(var1) && Files.size(var1) <= 67108864L) {
         byte[] var3;
         try (InputStream var4 = Files.newInputStream(var1)) {
            var3 = var4.readNBytes(67108865);
         }

         this.cacheReadBytes.add(var3.length);
         if (var3.length >= 32 && var3.length <= 67108864) {
            MessageDigest var19 = sha256();
            var19.update(var3, 32, var3.length - 32);
            if (!MessageDigest.isEqual(Arrays.copyOf(var3, 32), var19.digest())) {
               throw new IOException("Texture cache checksum mismatch");
            }

            try (DataInputStream var5 = new DataInputStream(new ByteArrayInputStream(var3, 32, var3.length - 32))) {
               if (!var5.readUTF().equals(var2)) {
                  throw new IOException("Texture cache key mismatch");
               }

               CachedAtlasEntry var6 = null;
               if (var5.readBoolean()) {
                  int var7 = var5.readInt();
                  int var8 = var5.readInt();
                  long var9 = var5.readLong();
                  long var11 = var5.readLong();
                  boolean var13 = var5.readBoolean();
                  JsonObject var14 = JsonParser.parseString(new String(readLengthPrefixed(var5), StandardCharsets.UTF_8)).getAsJsonObject();
                  var6 = new CachedAtlasEntry(readLengthPrefixed(var5), var14, var7, var8, var9, var11, var13);
               }

               if (var5.available() != 0) {
                  throw new IOException("Trailing texture cache data");
               } else {
                  return var6;
               }
            }
         } else {
            throw new IOException("Invalid texture cache length");
         }
      } else {
         throw new IOException("Invalid texture cache size/type");
      }
   }

   private static byte[] readLengthPrefixed(DataInputStream var0) {
      int var1 = var0.readInt();
      if (var1 >= 0 && var1 <= var0.available()) {
         return var0.readNBytes(var1);
      } else {
         throw new IOException("Invalid texture cache field length");
      }
   }

   private static byte[] serializeEntry(String var0, CachedAtlasEntry var1) {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      var2.write(new byte[32]);

      try (DataOutputStream var3 = new DataOutputStream(var2)) {
         var3.writeUTF(var0);
         var3.writeBoolean(var1 != null);
         if (var1 != null) {
            var3.writeInt(var1.originalFrames());
            var3.writeInt(var1.uniqueFrames());
            var3.writeLong(var1.originalPixels());
            var3.writeLong(var1.outputPixels());
            var3.writeBoolean(var1.solidified());
            byte[] var4 = var1.metadata().toString().getBytes(StandardCharsets.UTF_8);
            var3.writeInt(var4.length);
            var3.write(var4);
            var3.writeInt(var1.png().length);
            var3.write(var1.png());
         }
      }

      byte[] var8 = var2.toByteArray();
      MessageDigest var9 = sha256();
      var9.update(var8, 32, var8.length - 32);
      System.arraycopy(var9.digest(), 0, var8, 0, 32);
      return var8;
   }

   private static MessageDigest sha256() {
      try {
         return MessageDigest.getInstance("SHA-256");
      } catch (NoSuchAlgorithmException var1) {
         throw new IllegalStateException(var1);
      }
   }

   private static String sha256Hex(byte[] var0) {
      return HexFormat.of().formatHex(sha256().digest(var0));
   }

   private static void checkInterrupted() {
      if (Thread.currentThread().isInterrupted()) {
         throw new InterruptedIOException("Texture cache interrupted");
      }
   }

   private static void closeQuietly(AutoCloseable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (Exception var2) {
         }
      }
   }

   static {
      Arrays.setAll(LOCKS, var0 -> new Object());
   }
}
