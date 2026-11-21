package dev.lone.itemsadder.resourcepack;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class PackCacheStore {
   private static final Object bGv = new Object();
   private static final int bGw = 1229017906;
   private static final Pattern bGx = Pattern.compile("[a-f0-9]{64}");
   private static final Pattern bGy = Pattern.compile("[0-9]+(?:[._-][0-9A-Za-z]+)*");
   private static final Map bGz = new HashMap();
   private static final long bGA = ProcessHandle.current().pid();
   private static final long bGB = ProcessHandle.current().info().startInstant().map(var0 -> var0.toEpochMilli()).orElse(0L);
   private final Path bGC;
   private final Consumer bGD;
   private final Map bGE = new ConcurrentHashMap();
   private final Map bGF = new HashMap();
   private final Map bGG = new HashMap();
   private final Set bGH = ConcurrentHashMap.newKeySet();
   private final Set bGI = new HashSet();
   private final byte[] bGJ = new byte[32768];
   private boolean bGK;
   private boolean bGL;

   public PackCacheStore(Path var1, Consumer var2) {
      try {
         this.bGC = var1.toFile().getCanonicalFile().toPath();
      } catch (IOException var4) {
         throw new UncheckedIOException(var4);
      }

      this.bGD = Objects.requireNonNull(var2);
   }

   public boolean ensureVersion(String var1, BooleanSupplier var2, PackFileAction var3) {
      requireVersion(var1);
      Files.createDirectories(this.bGC);
      if (!Files.exists(this.bGC.resolve(".sha256"), LinkOption.NOFOLLOW_LINKS) && !Files.isWritable(this.bGC)) {
         if (!this.bGL) {
            this.bGD.accept("Vanilla cache is read-only; using the legacy files: " + this.bGC);
         }

         this.bGL = true;
         return var2.getAsBoolean();
      } else {
         return (Boolean)this.withLock(
            () -> {
               if (this.bGE.containsKey(var1)) {
                  this.bGG.clear();
                  this.bGI.clear();
               }

               if (!this.bGK) {
                  for (String var5 : this.listVersions()) {
                     if (!var5.equals(var1)
                        && (
                           !Files.isRegularFile(this.indexPath(var5), LinkOption.NOFOLLOW_LINKS)
                              || Files.exists(this.pendingIndexPath(var5), LinkOption.NOFOLLOW_LINKS)
                              || !scanDirectory(this.bGC.resolve(var5)).isEmpty()
                              || Files.isRegularFile(this.stagingPath(var5).resolve("assets-list.txt"), LinkOption.NOFOLLOW_LINKS)
                        )) {
                        this.syncVersion(var5, false);
                     }
                  }

                  this.bGK = true;
               }

               if (this.syncVersion(var1, false) && var2.getAsBoolean()) {
                  return true;
               }

               if (var3 == null) {
                  return false;
               }

               Path var7 = this.stagingPath(var1);
               this.quarantine(var1);
               this.ensureDirectory(var7);

               try {
                  var3.run(var7);
               } catch (IOException | RuntimeException | Error var6) {
                  this.quarantine(var1);
                  throw var6;
               }

               this.bGG.clear();
               return this.syncVersion(var1, true) && var2.getAsBoolean();
            }
         );
      }
   }

   public Path resolveAssetPath(String var1, String var2) {
      try {
         requireVersion(var1);
         Map var3 = (Map)this.bGE.get(var1);
         PackCacheFile var4 = var3 == null ? null : (PackCacheFile)var3.get(var2);
         if (var4 != null) {
            return this.bGH.contains(var4.hash()) ? null : this.objectPath(var4.hash());
         }

         Path var5 = resolveSafe(this.bGC.resolve(var1), var2);
         if (var3 == null && (Files.exists(this.indexPath(var1)) || Files.exists(this.backupIndexPath(var1)))) {
            var3 = (Map)this.withLock(() -> {
               Map var2x = this.readIndex(var1);
               this.markReaders(var2x.values());
               this.bGE.put(var1, this.internEntries(var2x));
               return var2x;
            });
         }

         var4 = var3 == null ? null : (PackCacheFile)var3.get(var2);
         if (var4 != null) {
            return this.bGH.contains(var4.hash()) ? null : this.objectPath(var4.hash());
         }

         for (Path var6 = var5.getParent(); var6 != null && !var6.equals(this.bGC); var6 = var6.getParent()) {
            if (Files.isSymbolicLink(var6)) {
               throw new IOException("Symbolic link in vanilla cache: " + var6);
            }
         }

         if (!Files.isRegularFile(var5, LinkOption.NOFOLLOW_LINKS)) {
            return null;
         } else {
            return !Files.isWritable(this.bGC) ? var5 : (Path)this.withLock(() -> {
               this.syncVersion(var1, false);
               PackCacheFile var3x = (PackCacheFile)this.bGE.getOrDefault(var1, Map.of()).get(var2);
               return var3x != null && !this.bGH.contains(var3x.hash()) ? this.objectPath(var3x.hash()) : null;
            });
         }
      } catch (IOException var7) {
         throw new UncheckedIOException(var7);
      }
   }

   public Set listAssets(String var1) {
      this.resolveAssetPath(var1, "assets-list.txt");
      Map var2 = (Map)this.bGE.get(var1);
      if (var2 != null) {
         return var2.keySet();
      }

      try {
         return Set.copyOf(scanDirectory(this.bGC.resolve(var1)).keySet());
      } catch (IOException var4) {
         throw new UncheckedIOException(var4);
      }
   }

   public String getHash(String var1, String var2) {
      PackCacheFile var3 = (PackCacheFile)this.bGE.getOrDefault(var1, Map.of()).get(var2);
      return var3 != null && !this.bGH.contains(var3.hash()) ? var3.hash() : null;
   }

   public byte[] readAsset(String var1, String var2) {
      PackCacheFile var3 = (PackCacheFile)this.bGE.getOrDefault(var1, Map.of()).get(var2);
      if (var3 == null) {
         return null;
      }

      if (var3.size() > 268435456L) {
         throw new IOException("Vanilla asset exceeds read limit: " + var2);
      }

      byte[] var4;
      try (InputStream var5 = Files.newInputStream(this.objectPath(var3.hash()), LinkOption.NOFOLLOW_LINKS)) {
         var4 = var5.readNBytes((int)var3.size() + 1);
      }

      if (var4.length == var3.size() && HexFormat.of().formatHex(sha256Digest().digest(var4)).equals(var3.hash())) {
         return var4;
      } else {
         throw new IOException("Corrupt vanilla asset: " + var1 + "/" + var2);
      }
   }

   public void repairAsset(String var1, String var2, PackFileAction var3, PackValidator var4) {
      requireVersion(var1);
      requireSafePath(var2);
      this.withLock(() -> {
         Map var5 = this.readIndex(var1);
         PackCacheFile var6 = (PackCacheFile)var5.get(var2);
         this.bGE.put(var1, this.internEntries(var5));
         if (var6 != null) {
            try {
               byte[] var19 = this.readAsset(var1, var2);
               var4.check(var19);
               return null;
            } catch (IOException var18) {
               checkInterrupted();
            }
         }

         this.quarantine(var1);
         Path var7 = this.stagingPath(var1);
         this.ensureDirectory(var7);

         try {
            var3.run(var7);
            checkInterrupted();
            Path var8 = resolveSafe(var7, var2);
            long var9 = Files.size(var8);
            if (var9 > 268435456L) {
               throw new IOException("Repair exceeds asset read limit");
            }

            byte[] var11;
            try (InputStream var12 = Files.newInputStream(var8, LinkOption.NOFOLLOW_LINKS)) {
               var11 = var12.readNBytes((int)var9 + 1);
            }

            if (var11.length != var9) {
               throw new IOException("Repair changed during read");
            }

            var4.check(var11);
            String var20 = HexFormat.of().formatHex(sha256Digest().digest(var11));
            HashMap var13 = new HashMap(var5);
            var13.put(var2, new PackCacheFile(var2, var20, var9));
            this.markPending(var1);
            this.writeIndexFile(this.backupIndexPath(var1), var13);
            this.writeIndexFile(this.indexPath(var1), var13);
            this.markReaders(var13.values());
            this.bGG.remove(var20);
            this.publishObject(var8, var20);
            this.bGE.put(var1, this.internEntries(var13));
            this.removeOriginals(var7, scanDirectory(var7), var13);
            Files.deleteIfExists(this.pendingIndexPath(var1));
            return null;
         } catch (IOException | RuntimeException var17) {
            if (!Files.exists(this.pendingIndexPath(var1), LinkOption.NOFOLLOW_LINKS)) {
               this.quarantine(var1);
            }

            throw var17;
         }
      });
   }

   private Object withLock(PackCacheTask var1) {
      synchronized (bGv) {
         this.ensureDirectory(this.bGC.resolve(".sha256"));
         this.ensureDirectory(this.bGC.resolve(".sha256/indexes"));
         this.ensureDirectory(this.bGC.resolve(".sha256/objects"));

         try (
            FileChannel var3 = FileChannel.open(this.bGC.resolve(".sha256/lock"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            FileLock var4 = acquireLock(var3);
         ) {
            return var1.run();
         }
      }
   }

   private static FileLock acquireLock(FileChannel var0) {
      while (true) {
         checkInterrupted();

         try {
            return var0.lock();
         } catch (OverlappingFileLockException var2) {
            LockSupport.parkNanos(10000000L);
         }
      }
   }

   private boolean syncVersion(String var1, boolean var2) {
      Map var3 = this.readIndex(var1);
      HashMap var4 = new HashMap(var3);
      HashMap var5 = new HashMap();
      Path var6 = this.bGC.resolve(var1);
      Map var7 = scanDirectory(var6);
      Map var8 = !var2
            && !Files.exists(this.pendingIndexPath(var1), LinkOption.NOFOLLOW_LINKS)
            && !Files.isRegularFile(this.stagingPath(var1).resolve("assets-list.txt"), LinkOption.NOFOLLOW_LINKS)
         ? Map.of()
         : scanDirectory(this.stagingPath(var1));
      if (!var3.isEmpty() && var7.isEmpty() && var8.isEmpty() && !Files.exists(this.pendingIndexPath(var1), LinkOption.NOFOLLOW_LINKS)) {
         if (!Files.isRegularFile(this.backupIndexPath(var1), LinkOption.NOFOLLOW_LINKS)) {
            this.writeIndexFile(this.backupIndexPath(var1), var3);
         }

         this.markReaders(var3.values());
         this.bGE.put(var1, this.internEntries(var3));
         return true;
      } else {
         for (Entry var10 : var7.entrySet()) {
            Path var11 = resolveSafe(var6, (String)var10.getKey());
            String var12 = this.hashFile(var11);
            PackCacheFile var13 = (PackCacheFile)var3.get(var10.getKey());
            if (var13 == null || var13.hash().equals(var12)) {
               var5.putIfAbsent(var12, var11);
               var4.put((String)var10.getKey(), new PackCacheFile((String)var10.getKey(), var12, ((BasicFileAttributes)var10.getValue()).size()));
            }
         }

         for (Entry var16 : var8.entrySet()) {
            Path var18 = resolveSafe(this.stagingPath(var1), (String)var16.getKey());
            String var20 = this.hashFile(var18);
            var5.putIfAbsent(var20, var18);
            var4.put((String)var16.getKey(), new PackCacheFile((String)var16.getKey(), var20, ((BasicFileAttributes)var16.getValue()).size()));
         }

         boolean var15 = true;
         if (!var4.isEmpty()) {
            this.markPending(var1);
            if (!var4.equals(var3) || !Files.isRegularFile(this.backupIndexPath(var1), LinkOption.NOFOLLOW_LINKS)) {
               this.writeIndexFile(this.backupIndexPath(var1), var4);
               this.writeIndexFile(this.indexPath(var1), var4);
            }

            this.markReaders(var4.values());
            HashSet var17 = new HashSet();

            for (PackCacheFile var21 : var4.values()) {
               if (!this.isObjectValid(var21.hash())) {
                  Path var22 = (Path)var5.get(var21.hash());
                  if (var22 == null && !var17.contains(var21.hash())) {
                     var22 = this.findObjectSource(var21.hash());
                  }

                  if (var22 != null) {
                     this.publishObject(var22, var21.hash());
                  } else {
                     var17.add(var21.hash());
                     var15 = false;
                  }
               }
            }

            this.bGE.put(var1, this.internEntries(var4));
            if (var15) {
               this.removeOriginals(var6, var7, var4);
               this.removeOriginals(this.stagingPath(var1), var8, var4);
               Files.deleteIfExists(this.pendingIndexPath(var1));
            }
         }

         return var15;
      }
   }

   private void quarantine(String var1) {
      Path var2 = this.stagingPath(var1);
      if (Files.exists(var2, LinkOption.NOFOLLOW_LINKS)) {
         Path var3 = this.bGC.resolve(".sha256/recovery/download-" + UUID.randomUUID()).resolve(var1);
         this.ensureDirectory(var3.getParent());
         atomicMove(var2, var3);
      }
   }

   private Map readIndex(String var1) {
      Path var2 = this.indexPath(var1);
      Path var3 = this.backupIndexPath(var1);
      IOException var4 = null;
      if (Files.exists(var2, LinkOption.NOFOLLOW_LINKS)) {
         try {
            return readIndexFile(var2);
         } catch (IOException var6) {
            var4 = var6;
         }
      }

      if (Files.exists(var3, LinkOption.NOFOLLOW_LINKS)) {
         Map var5 = readIndexFile(var3);
         if (var4 != null) {
            this.deleteFile(var2);
         }

         this.writeIndexFile(var2, var5);
         this.bGD.accept("Recovered vanilla asset manifest from backup: " + var1);
         return var5;
      } else if (var4 != null) {
         throw new IOException(
            "Vanilla asset manifest and its backup are unavailable for "
               + var1
               + "; original objects are preserved; restore the manifest before garbage collection",
            var4
         );
      } else {
         return Map.of();
      }
   }

   private void publishObject(Path var1, String var2) {
      if (!this.isObjectValid(var2)) {
         Path var3 = this.objectPath(var2);
         this.ensureDirectory(var3.getParent());
         if (Files.exists(var3, LinkOption.NOFOLLOW_LINKS)) {
            this.backupFile(var3, true);
         }

         try {
            atomicMove(var1, var3);
         } catch (FileSystemException var5) {
            this.copyObject(var1, var3, var2);
         }

         this.bGG.remove(var2);
         if (!this.isObjectValid(var2)) {
            throw new IOException("Published asset failed verification: " + var3);
         }
      }
   }

   private void copyObject(Path var1, Path var2, String var3) {
      Path var4 = Files.createTempFile(var2.getParent(), ".object-", ".tmp");

      try {
         BasicFileAttributes var5 = Files.readAttributes(var1, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
         MessageDigest var6 = sha256Digest();
         DigestInputStream var7 = new DigestInputStream(Files.newInputStream(var1), var6);

         int var9;
         try (OutputStream var8 = Files.newOutputStream(var4)) {
            while ((var9 = var7.read(this.bGJ)) != -1) {
               checkInterrupted();
               var8.write(this.bGJ, 0, var9);
            }
         } catch (Throwable var24) {
            try {
               var7.close();
            } catch (Throwable var20) {
               var24.addSuppressed(var20);
            }

            throw var24;
         }

         var7.close();
         if (!HexFormat.of().formatHex(var6.digest()).equals(var3)
            || !sameAttributes(var5, Files.readAttributes(var1, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS))) {
            throw new IOException("Asset changed during import: " + var1);
         }

         try (FileChannel var26 = FileChannel.open(var4, StandardOpenOption.WRITE)) {
            var26.force(true);
         }

         atomicMove(var4, var2);
      } finally {
         Files.deleteIfExists(var4);
      }
   }

   private Path findObjectSource(String var1) {
      for (String var3 : this.listVersions()) {
         for (Path var5 : List.of(this.bGC.resolve(var3), this.stagingPath(var3))) {
            for (String var7 : scanDirectory(var5).keySet()) {
               Path var8 = resolveSafe(var5, var7);
               if (this.hashFile(var8).equals(var1)) {
                  return var8;
               }
            }
         }
      }

      return null;
   }

   private void removeOriginals(Path var1, Map var2, Map var3) {
      int var4 = 0;

      for (String var6 : var2.keySet()) {
         checkInterrupted();
         Path var7 = resolveSafe(var1, var6);
         PackCacheFile var8 = (PackCacheFile)var3.get(var6);
         if (var8 != null && Files.exists(var7, LinkOption.NOFOLLOW_LINKS)) {
            try {
               if (Files.mismatch(var7, this.objectPath(var8.hash())) == -1L) {
                  Files.delete(var7);
               } else {
                  this.deleteFile(var7);
               }
            } catch (FileSystemException var10) {
               var4++;
            }
         }
      }

      if (var4 > 0) {
         this.bGD.accept("Vanilla manifests committed; could not remove " + var4 + " original files in " + var1);
      }

      if (Files.isDirectory(var1, LinkOption.NOFOLLOW_LINKS)) {
         Files.walkFileTree(var1, new PackCacheCleanupVisitor(this));
      }
   }

   private void deleteFile(Path var1) {
      this.backupFile(var1, false);
   }

   private void backupFile(Path var1, boolean var2) {
      if (!Files.isRegularFile(var1, LinkOption.NOFOLLOW_LINKS)) {
         throw new IOException("Unsafe cache file: " + var1);
      }

      Path var3 = this.bGC.resolve(".sha256/recovery").resolve(UUID.randomUUID().toString()).resolve(this.bGC.relativize(var1));
      this.ensureDirectory(var3.getParent());
      if (var2) {
         try {
            Files.copy(var1, var3);
         } catch (AccessDeniedException var5) {
            atomicMove(var1, var3);
         }
      } else {
         atomicMove(var1, var3);
      }
   }

   private boolean isObjectValid(String var1) {
      Boolean var2 = (Boolean)this.bGG.get(var1);
      if (var2 != null) {
         return var2;
      }

      Path var3 = this.objectPath(var1);
      this.ensureDirectory(var3.getParent());

      boolean var4;
      try {
         BasicFileAttributes var5 = Files.readAttributes(var3, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
         var4 = var5.isRegularFile() && this.hashFileChecked(var3, var5).equals(var1);
      } catch (InterruptedIOException var6) {
         throw var6;
      } catch (IOException var7) {
         var4 = false;
      }

      this.bGG.put(var1, var4);
      if (var4) {
         this.bGH.remove(var1);
      } else {
         this.bGH.add(var1);
      }

      return var4;
   }

   private String hashFile(Path var1) {
      return this.hashFileChecked(var1, Files.readAttributes(var1, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS));
   }

   private String hashFileChecked(Path var1, BasicFileAttributes var2) {
      MessageDigest var3 = sha256Digest();
      DigestInputStream var4 = new DigestInputStream(Files.newInputStream(var1), var3);

      try {
         long var5 = var2.size();

         while (var5 > 0L) {
            checkInterrupted();
            int var7 = var4.read(this.bGJ, 0, (int)Math.min(this.bGJ.length, var5));
            if (var7 < 0) {
               throw new EOFException("Asset shrank while hashing: " + var1);
            }

            var5 -= var7;
         }
      } catch (Throwable var9) {
         try {
            var4.close();
         } catch (Throwable var8) {
            var9.addSuppressed(var8);
         }

         throw var9;
      }

      var4.close();
      if (!sameAttributes(var2, Files.readAttributes(var1, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS))) {
         throw new IOException("Asset changed while hashing: " + var1);
      } else {
         return HexFormat.of().formatHex(var3.digest());
      }
   }

   private static void checkInterrupted() {
      if (Thread.currentThread().isInterrupted()) {
         throw new InterruptedIOException("Interrupted importing vanilla assets");
      }
   }

   private static boolean sameAttributes(BasicFileAttributes var0, BasicFileAttributes var1) {
      return var1 != null
         && var0.size() == var1.size()
         && var0.lastModifiedTime().equals(var1.lastModifiedTime())
         && Objects.equals(var0.fileKey(), var1.fileKey());
   }

   private void markReaders(Collection var1) {
      Set var2 = bGz.computeIfAbsent(this.bGC, var0 -> new HashSet());
      TreeMap var3 = new TreeMap();

      for (PackCacheFile var5 : var1) {
         if (!var2.contains(var5.hash())) {
            var3.put(var5.hash(), new PackCacheFile(var5.hash(), var5.hash(), 0L));
         }
      }

      if (!var3.isEmpty()) {
         Path var6 = this.bGC.resolve(".sha256/readers/" + bGA + "-" + bGB + "-" + UUID.randomUUID() + ".idx");
         this.writeIndexFile(var6, var3);
         var2.addAll(var3.keySet());
      }
   }

   public void cleanupReaders() {
      this.withLock(() -> {
         if (!Files.exists(this.bGC.resolve(".sha256/gc.pending"))) {
            Path var1 = this.bGC.resolve(".sha256/readers");
            if (Files.isDirectory(var1)) {
               try (DirectoryStream var2 = Files.newDirectoryStream(var1, "*.idx")) {
                  for (Path var4 : var2) {
                     checkInterrupted();

                     try {
                        if (!isReaderAlive(var4)) {
                           Files.delete(var4);
                        }
                     } catch (IllegalArgumentException var7) {
                     }
                  }
               }
            }
         }

         return null;
      });
      if (Files.exists(this.bGC.resolve(".sha256/gc.pending"))) {
         this.gc();
      }

      synchronized (bGv) {
         this.bGF.clear();
         this.bGG.clear();
         this.bGI.clear();
      }
   }

   private static boolean isReaderAlive(Path var0) {
      String[] var1 = var0.getFileName().toString().replace(".idx", "").split("-", 3);
      if (var1.length < 2) {
         throw new IllegalArgumentException("Invalid reader identity");
      }

      long var2 = Long.parseLong(var1[0]);
      long var4 = Long.parseLong(var1[1]);
      Optional var6 = ProcessHandle.of(var2).filter(ProcessHandle::isAlive);
      return var6.isPresent() && (var4 == 0L || ((ProcessHandle)var6.get()).info().startInstant().map(var2x -> var2x.toEpochMilli() == var4).orElse(true));
   }

   private void markGcPending() {
      try {
         Files.createFile(this.bGC.resolve(".sha256/gc.pending"));
      } catch (FileAlreadyExistsException var2) {
      }
   }

   public int gc() {
      return !Files.isDirectory(this.bGC.resolve(".sha256"), LinkOption.NOFOLLOW_LINKS) ? 0 : (Integer)this.withLock(() -> {
         HashSet var1 = new HashSet();

         for (String var3 : this.listVersions()) {
            checkInterrupted();
            if (!Files.isRegularFile(this.indexPath(var3), LinkOption.NOFOLLOW_LINKS)) {
               return 0;
            }

            try {
               Map var4 = readIndexFile(this.indexPath(var3));

               for (PackCacheFile var6 : var4.values()) {
                  var1.add(var6.hash());
               }

               if (Files.exists(this.backupIndexPath(var3))) {
                  try {
                     for (PackCacheFile var24 : readIndexFile(this.backupIndexPath(var3)).values()) {
                        var1.add(var24.hash());
                     }
                  } catch (IOException var15) {
                     this.deleteFile(this.backupIndexPath(var3));
                     this.writeIndexFile(this.backupIndexPath(var3), var4);
                     this.bGD.accept("Recovered vanilla asset backup from the valid primary: " + var3);
                  }
               } else {
                  this.writeIndexFile(this.backupIndexPath(var3), var4);
               }
            } catch (IOException var16) {
               return 0;
            }
         }

         boolean var17 = false;
         Path var18 = this.bGC.resolve(".sha256/readers");
         if (Files.isDirectory(var18, LinkOption.NOFOLLOW_LINKS)) {
            try (DirectoryStream var19 = Files.newDirectoryStream(var18, "*.idx")) {
               for (Path var25 : var19) {
                  try {
                     if (isReaderAlive(var25)) {
                        for (PackCacheFile var28 : readIndexFile(var25).values()) {
                           if (var1.add(var28.hash())) {
                              var17 = true;
                           }
                        }
                     } else {
                        Files.delete(var25);
                     }
                  } catch (IOException | IllegalArgumentException var13) {
                     return 0;
                  }
               }
            }
         }

         int var20 = 0;
         Path var23 = this.bGC.resolve(".sha256/objects");

         try (Stream var26 = Files.walk(var23)) {
            Iterator var27 = var26.iterator();

            while (var27.hasNext()) {
               checkInterrupted();
               Path var29 = (Path)var27.next();
               if (Files.isRegularFile(var29, LinkOption.NOFOLLOW_LINKS)) {
                  String var9 = var29.getFileName().toString();
                  if (bGx.matcher(var9).matches() && !var1.contains(var9) || var9.matches("\\.object-[0-9]+\\.tmp")) {
                     Files.delete(var29);
                     var20++;
                  }
               }
            }
         }

         if (var17) {
            this.markGcPending();
         } else {
            Files.deleteIfExists(this.bGC.resolve(".sha256/gc.pending"));
         }

         return var20;
      });
   }

   private List listVersions() {
      TreeSet var1 = new TreeSet();

      try (Stream var2 = Files.list(this.bGC)) {
         var2.filter(var0 -> Files.isDirectory(var0, LinkOption.NOFOLLOW_LINKS))
            .map(var0 -> var0.getFileName().toString())
            .filter(var0 -> bGy.matcher(var0).matches())
            .forEach(var1::add);
      }

      try (DirectoryStream var10 = Files.newDirectoryStream(this.bGC.resolve(".sha256/indexes"))) {
         for (Path var4 : var10) {
            String var5 = var4.getFileName().toString().replaceFirst("\\.idx(?:\\.backup|\\.pending)?$", "");
            if (bGy.matcher(var5).matches()) {
               var1.add(var5);
            }
         }
      }

      return List.copyOf(var1);
   }

   private static Map scanDirectory(Path var0) {
      TreeMap var1 = new TreeMap();
      if (!Files.isDirectory(var0, LinkOption.NOFOLLOW_LINKS)) {
         return var1;
      }

      Files.walkFileTree(var0, new PackCacheScanVisitor(var0, var1));
      return var1;
   }

   private Path objectPath(String var1) {
      return this.bGC.resolve(".sha256/objects").resolve(var1.substring(0, 2)).resolve(var1);
   }

   private Path indexPath(String var1) {
      return this.bGC.resolve(".sha256/indexes").resolve(var1 + ".idx");
   }

   private Path backupIndexPath(String var1) {
      return this.bGC.resolve(".sha256/indexes").resolve(var1 + ".idx.backup");
   }

   private Path pendingIndexPath(String var1) {
      return this.bGC.resolve(".sha256/indexes").resolve(var1 + ".idx.pending");
   }

   private void markPending(String var1) {
      try {
         Files.createFile(this.pendingIndexPath(var1));
      } catch (FileAlreadyExistsException var3) {
      }
   }

   private Path stagingPath(String var1) {
      return this.bGC.resolve(".sha256/staging").resolve(var1);
   }

   private Map internEntries(Map var1) {
      HashMap var2 = new HashMap(var1.size());

      for (PackCacheFile var4 : var1.values()) {
         PackCacheFile var5 = this.bGF.computeIfAbsent(var4, var0 -> (PackCacheFile)var0);
         var2.put(var5.path(), var5);
      }

      return Map.copyOf(var2);
   }

   private static Map readIndexFile(Path var0) {
      if (!Files.exists(var0, LinkOption.NOFOLLOW_LINKS)) {
         return Map.of();
      }

      if (!Files.isRegularFile(var0, LinkOption.NOFOLLOW_LINKS)) {
         throw new IOException("Unsafe vanilla asset index: " + var0);
      }

      long var1 = Files.size(var0);
      if (var1 >= 40L && var1 <= 268435456L) {
         byte[] var3 = new byte[(int)var1];

         try (DataInputStream var4 = new DataInputStream(Files.newInputStream(var0))) {
            var4.readFully(var3);
            if (var4.read() != -1) {
               throw new IOException("Asset index changed while reading");
            }
         } catch (IOException var19) {
            throw new PackCacheException(var0, var19);
         }

         MessageDigest var20 = sha256Digest();
         var20.update(var3, 0, var3.length - 32);
         if (!MessageDigest.isEqual(Arrays.copyOfRange(var3, var3.length - 32, var3.length), var20.digest())) {
            throw new PackCacheException(var0, new IOException("Vanilla asset index checksum mismatch"));
         }

         try (DataInputStream var5 = new DataInputStream(new ByteArrayInputStream(var3, 0, var3.length - 32))) {
            int var6 = var5.readInt();
            if (var6 != 1229017906 && var6 != 1229017905) {
               throw new IOException("Unknown vanilla asset index format: " + var0);
            }

            int var7 = var5.readInt();
            if (var7 < 0 || var7 > 1000000) {
               throw new IOException("Invalid asset index length");
            }

            HashMap var8 = new HashMap();

            for (int var9 = 0; var9 < var7; var9++) {
               String var10 = var5.readUTF();
               String var11 = var5.readUTF();
               long var12 = var5.readLong();
               requireSafePath(var10);
               if (!isSha256Hex(var11) || var12 < 0L || var8.put(var10, new PackCacheFile(var10, var11, var12)) != null) {
                  throw new IOException("Invalid vanilla asset index entry");
               }
            }

            if (var5.read() != -1) {
               throw new IOException("Trailing vanilla asset index data");
            } else {
               return var8;
            }
         } catch (IOException | RuntimeException var17) {
            throw new PackCacheException(var0, var17);
         }
      } else {
         throw new PackCacheException(var0, new IOException("Invalid asset index length"));
      }
   }

   private static boolean isSha256Hex(String var0) {
      if (var0.length() != 64) {
         return false;
      }

      for (int var1 = 0; var1 < var0.length(); var1++) {
         char var2 = var0.charAt(var1);
         if ((var2 < '0' || var2 > '9') && (var2 < 'a' || var2 > 'f')) {
            return false;
         }
      }

      return true;
   }

   private void writeIndexFile(Path var1, Map var2) {
      this.ensureDirectory(var1.getParent());
      Path var3 = Files.createTempFile(var1.getParent(), ".index-", ".tmp");

      try {
         MessageDigest var4 = sha256Digest();

         try (DataOutputStream var5 = new DataOutputStream(new BufferedOutputStream(new DigestOutputStream(Files.newOutputStream(var3), var4)))) {
            var5.writeInt(1229017906);
            var5.writeInt(var2.size());

            for (PackCacheFile var7 : var2.values()) {
               var5.writeUTF(var7.path());
               var5.writeUTF(var7.hash());
               var5.writeLong(var7.size());
            }
         }

         Files.write(var3, var4.digest(), StandardOpenOption.APPEND);

         try (FileChannel var19 = FileChannel.open(var3, StandardOpenOption.WRITE)) {
            var19.force(true);
         }

         if (var1.getParent().equals(this.bGC.resolve(".sha256/indexes"))) {
            this.markGcPending();
         }

         atomicMove(var3, var1);
      } finally {
         Files.deleteIfExists(var3);
      }
   }

   private static Path resolveSafe(Path var0, String var1) {
      requireSafePath(var1);
      return var0.resolve(var1);
   }

   private static void requireSafePath(String var0) {
      if (!var0.isEmpty()
         && var0.indexOf(92) < 0
         && var0.indexOf(58) < 0
         && var0.indexOf(0) < 0
         && !var0.startsWith("/")
         && !var0.endsWith("/")
         && !var0.contains("//")) {
         int var1 = 0;

         while (var1 < var0.length()) {
            int var2 = var0.indexOf(47, var1);
            if (var2 < 0) {
               var2 = var0.length();
            }

            int var3 = var2 - var1;
            if (var0.charAt(var1) == '.' && (var3 == 1 || var3 == 2 && var0.charAt(var1 + 1) == '.')) {
               throw new IOException("Unsafe vanilla asset path: " + var0);
            }

            var1 = var2 + 1;
         }
      } else {
         throw new IOException("Unsafe vanilla asset path: " + var0);
      }
   }

   private static void requireVersion(String var0) {
      if (!bGy.matcher(var0).matches()) {
         throw new IOException("Invalid Minecraft version: " + var0);
      }
   }

   private void ensureDirectory(Path var1) {
      if (!this.bGI.contains(var1)) {
         for (Path var2 = var1; var2 != null && !var2.equals(this.bGC) && var2.startsWith(this.bGC); var2 = var2.getParent()) {
            if (Files.isSymbolicLink(var2)) {
               throw new IOException("Symbolic link inside vanilla cache: " + var2);
            }
         }

         Files.createDirectories(var1);
         if (!Files.isDirectory(var1, LinkOption.NOFOLLOW_LINKS)) {
            throw new IOException("Unsafe vanilla cache directory: " + var1);
         }

         this.bGI.add(var1);
      }
   }

   private static void atomicMove(Path var0, Path var1) {
      Files.move(var0, var1, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
   }

   private static MessageDigest sha256Digest() {
      try {
         return MessageDigest.getInstance("SHA-256");
      } catch (NoSuchAlgorithmException var1) {
         throw new AssertionError(var1);
      }
   }
}
