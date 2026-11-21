package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.Events.ItemsAdderPackCompressedEvent.PackEntry;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class PackFileCache extends AbstractMap {
   private static final AtomicLong bwi = new AtomicLong();
   private static final AtomicLong bwj = new AtomicLong();
   private static final AtomicLong bwk = new AtomicLong();
   private static final AtomicLong bwl = new AtomicLong();
   private static final AtomicLong bwm = new AtomicLong();
   private static final Map bwn = new ConcurrentHashMap();
   private static final long bwo = (Runtime.getRuntime().maxMemory() < 805306368L ? 0L : 64L) * 1024L * 1024L;
   private static final LinkedHashMap bwp = new LinkedHashMap(128, 0.75F, true);
   private static long bwq;
   private final TreeMap bwr = new TreeMap();
   private final boolean bws;

   PackFileCache(boolean var1) {
      this.bws = var1;
   }

   PackFileCache(PackFileCache var1) {
      this.bws = var1.bws;
      this.bwr.putAll(var1.bwr);
   }

   @Override
   public int size() {
      return this.bwr.size();
   }

   @Override
   public boolean containsKey(Object key) {
      return this.bwr.containsKey(key);
   }

   @Override
   public Set keySet() {
      return this.bwr.keySet();
   }

   public JSONObject getJson(Object var1) {
      PackFileData var2 = (PackFileData)this.bwr.get(var1);
      return var2 == null ? null : parseCounted(this.loadBytes(var2));
   }

   public JSONObject putJson(String var1, JSONObject var2) {
      PackFileData var3 = this.bwr.put(var1, this.storeData(JSON.toJSONBytes(var2, new Feature[]{Feature.LargeObject})));
      return var3 == null ? null : parseCounted(this.loadBytes(var3));
   }

   void putBytes(String var1, byte[] var2) {
      this.bwr.put(var1, this.storeData(var2));
   }

   public JSONObject putJsonIfAbsent(String var1, JSONObject var2) {
      if (this.bwr.containsKey(var1)) {
         return this.getJson(var1);
      }

      this.putJson(var1, var2);
      return null;
   }

   @Override
   public void putAll(Map map) {
      if (map instanceof PackFileCache var2) {
         this.bwr.putAll(var2.bwr);
      } else {
         map.forEach(this::putJson);
      }
   }

   void copyEntry(String var1, PackFileCache var2, String var3, boolean var4) {
      PackFileData var5 = (PackFileData)var2.bwr.get(var3);
      if (var5 != null) {
         if (var4) {
            this.bwr.put(var1, var5);
         } else {
            this.bwr.putIfAbsent(var1, var5);
         }
      }
   }

   PackMetadata getMetadata(String var1) {
      PackFileData var2 = (PackFileData)this.bwr.get(var1);
      if (var2 == null) {
         return null;
      } else {
         return var2.bwS == null
            ? PackMetadata.fromModelJson(parseCounted(this.loadBytes(var2)))
            : bwn.computeIfAbsent(var2.bwS, var2x -> PackMetadata.fromModelJson(parseCounted(this.loadBytes(var2))));
      }
   }

   boolean sameContent(String var1, PackFileCache var2, String var3) {
      PackFileData var4 = (PackFileData)this.bwr.get(var1);
      PackFileData var5 = (PackFileData)var2.bwr.get(var3);
      if (var4 == null || var5 == null) {
         return false;
      } else if (var4 == var5) {
         return true;
      } else {
         return var4.bwS != null && var5.bwS != null
            ? var4.bwT == var5.bwT && var4.bwS.equals(var5.bwS)
            : Arrays.equals(this.loadBytes(var4), var2.loadBytes(var5));
      }
   }

   String getDigest(String var1) {
      PackFileData var2 = (PackFileData)this.bwr.get(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("Missing generated JSON: " + var1);
      } else {
         return var2.bwS == null ? sha256Hex(var2.bwR) : var2.bwS;
      }
   }

   PackEntry toPackEntry(String var1) {
      PackFileData var2 = (PackFileData)this.bwr.get(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("Missing generated JSON: " + var1);
      }

      if (var2.bwR != null) {
         return PackEntry.bytes(var1, var2.bwR);
      }

      Path var3 = cacheRoot().resolve(var2.bwS.substring(0, 2)).resolve(var2.bwS + ".json");
      PackFileSource var4 = new PackFileSource(var3, var2.bwS, var2.bwT);
      return PackEntry.stream(var1, var4::openDigesting);
   }

   private static JSONObject parseCounted(byte[] var0) {
      bwl.incrementAndGet();
      bwm.addAndGet(var0.length);
      return JSON.parseObject(var0);
   }

   static TimestampPair getStats() {
      return new TimestampPair(bwl.get(), bwm.get());
   }

   public JSONObject removeJson(Object var1) {
      PackFileData var2 = (PackFileData)this.bwr.remove(var1);
      return var2 == null ? null : parseCounted(this.loadBytes(var2));
   }

   @Override
   public void clear() {
      this.bwr.clear();
   }

   @Override
   public Set entrySet() {
      return new PackFileCacheView(this);
   }

   private PackFileData storeData(byte[] var1) {
      Path var2 = cacheRoot();
      if (this.bws && var2 != null) {
         try {
            PackFileSource var3 = storeModelJson(var1);
            return new PackFileData(null, var3.digest(), var1.length);
         } catch (IOException var4) {
            throw new UncheckedIOException("Cannot cache generated JSON", var4);
         }
      } else {
         return new PackFileData(var1, null, var1.length);
      }
   }

   static PackFileSource storeModelJson(byte[] var0) {
      Path var1 = cacheRoot();
      if (var1 == null) {
         throw new IOException("Model JSON cache is unavailable");
      }

      String var2 = sha256Hex(var0);
      Path var3 = var1.resolve(var2.substring(0, 2)).resolve(var2 + ".json");
      Files.createDirectories(var3.getParent());
      if (Files.isRegularFile(var3)) {
         byte[] var4 = cachedBytes(var3);
         if (var4 == null) {
            var4 = Files.readAllBytes(var3);
            bwj.addAndGet(var4.length);
         }

         if (Arrays.equals(var4, var0)) {
            bwk.incrementAndGet();
            rememberBytes(var3, var4);
            return new PackFileSource(var3, var2, var0.length);
         }
      }

      Path var8 = Files.createTempFile(var3.getParent(), ".model-", ".tmp");

      try {
         Files.write(var8, var0);
         Files.move(var8, var3, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         bwi.addAndGet(var0.length);
         rememberBytes(var3, var0);
      } finally {
         Files.deleteIfExists(var8);
      }

      return new PackFileSource(var3, var2, var0.length);
   }

   private byte[] loadBytes(PackFileData var1) {
      if (var1.bwR != null) {
         return var1.bwR;
      }

      Path var2 = cacheRoot();
      if (var2 == null) {
         throw new IllegalStateException("Model JSON cache is unavailable");
      }

      Path var3 = var2.resolve(var1.bwS.substring(0, 2)).resolve(var1.bwS + ".json");

      try {
         byte[] var4 = cachedBytes(var3);
         if (var4 == null) {
            var4 = Files.readAllBytes(var3);
            bwj.addAndGet(var4.length);
            if (var4.length != var1.bwT || !sha256Hex(var4).equals(var1.bwS)) {
               throw new IOException("Corrupt generated model JSON cache entry: " + var3);
            }

            rememberBytes(var3, var4);
         }

         return var4;
      } catch (IOException var5) {
         throw new UncheckedIOException(var5);
      }
   }

   static CacheTriple resetStats() {
      bwn.clear();
      synchronized (bwp) {
         bwp.clear();
         bwq = 0L;
      }

      return new CacheTriple(bwi.getAndSet(0L), bwj.getAndSet(0L), bwk.getAndSet(0L));
   }

   private static byte[] cachedBytes(Path var0) {
      if (bwo == 0L) {
         return null;
      }

      synchronized (bwp) {
         return (byte[])bwp.get(var0);
      }
   }

   private static void rememberBytes(Path var0, byte[] var1) {
      if (var1.length <= bwo) {
         synchronized (bwp) {
            byte[] var3 = bwp.put(var0, var1);
            if (var3 != null) {
               bwq -= var3.length;
            }

            bwq += var1.length;
            Iterator var4 = bwp.values().iterator();

            while (bwq > bwo && var4.hasNext()) {
               bwq = bwq - ((byte[])var4.next()).length;
               var4.remove();
            }
         }
      }
   }

   private static Path cacheRoot() {
      Main var0 = Main.getInstance();
      return var0 == null ? null : var0.getDataFolder().toPath().resolve("storage/cache/resourcepack-json/v1");
   }

   private static String sha256Hex(byte[] var0) {
      return HexFormat.of().formatHex(sha256Digest().digest(var0));
   }

   private static MessageDigest sha256Digest() {
      try {
         return MessageDigest.getInstance("SHA-256");
      } catch (NoSuchAlgorithmException var1) {
         throw new AssertionError(var1);
      }
   }
}
