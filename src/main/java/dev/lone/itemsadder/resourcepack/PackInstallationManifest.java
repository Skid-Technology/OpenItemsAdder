package dev.lone.itemsadder.resourcepack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public final class PackInstallationManifest {
   private static final long MAX_ARCHIVE_SIZE = 536870912L;
   private static final int MAX_CENTRAL_DIR_SIZE = 67108864;
   private static final String MARKER_PREFIX = "{\"itemsadder_installation\":";
   private static final Pattern MARKER_V1_PATTERN = Pattern.compile(
      "\\{\"itemsadder_installation\":\\{\"v\":1,\"fingerprint\":\"([a-f0-9]{64})\",\"id\":\"([a-f0-9-]{36})\"}}"
   );
   private static final Pattern MARKER_V2_PATTERN = Pattern.compile(
      "\\{\"itemsadder_installation\":\\{\"v\":2,\"fingerprint\":\"([a-f0-9]{64})\",\"keyId\":\"[a-f0-9]{16}\",\"wrappedKey\":\"[A-Za-z0-9_-]{512,683}\",\"iv\":\"[A-Za-z0-9_-]{16}\",\"ciphertext\":\"[A-Za-z0-9_-]{24,12288}\"}}"
   );

   private PackInstallationManifest() {
   }

   public static String buildV1Marker(PackIdentityFile var0) {
      return "{\"itemsadder_installation\":{\"v\":1,\"fingerprint\":\"" + var0.getIdentityHash() + "\",\"id\":\"" + var0.id() + "\"}}";
   }

   public static String readFingerprint(Path var0) {
      String var1 = readSignedBlob(var0).marker();
      return var1 == null ? null : parseFingerprint(var1);
   }

   public static int readMarkerVersion(Path var0) {
      return markerVersion(readSignedBlob(var0).marker());
   }

   private static int markerVersion(String var0) {
      return var0 == null ? 0 : (var0.startsWith("{\"itemsadder_installation\":{\"v\":2,") ? 2 : 1);
   }

   private static String parseFingerprint(String var0) {
      Matcher var1 = MARKER_V1_PATTERN.matcher(var0);
      if (var1.matches()) {
         try {
            UUID.fromString(var1.group(2));
         } catch (IllegalArgumentException var3) {
            throw new IOException("Invalid installation marker");
         }

         return var1.group(1);
      } else {
         var1 = MARKER_V2_PATTERN.matcher(var0);
         if (!var1.matches()) {
            throw new IOException("Invalid installation marker");
         } else {
            return var1.group(1);
         }
      }
   }

   static Path resolveUpdateDir(Path var0, String var1) {
      if (var1 != null && !var1.isBlank()) {
         try {
            return var0.resolve(var1).toAbsolutePath();
         } catch (InvalidPathException var3) {
            throw new IOException("Invalid update folder", var3);
         }
      } else {
         return null;
      }
   }

   static void prepareUpdateDir(Path var0, Path var1) {
      if (Files.isSymbolicLink(var1)) {
         throw new IOException("Unsafe update storage");
      }

      Files.createDirectories(var1);
      if (Files.isSameFile(var0, var1)) {
         throw new IOException("Update directory must be separate");
      }

      if (!Files.isWritable(var1)) {
         throw new AccessDeniedException("Update directory is not writable");
      }
   }

   public static boolean stageUpdate(Path var0, Path var1, Path var2, String var3) {
      String var4 = parseFingerprint(var3);
      SignedPackBlob var5 = readSignedBlob(var0);
      boolean var6 = markerVersion(var5.marker()) == 1 && markerVersion(var3) == 2 && var4.equals(parseFingerprint(var5.marker()));
      if (var5.marker() != null && !var6) {
         return false;
      }

      if (Files.isSymbolicLink(var2)) {
         throw new IOException("Unsafe update storage");
      }

      prepareUpdateDir(var0.getParent(), var1);
      Path var7 = var1.resolve(var0.getFileName());

      try (Stream var8 = Files.list(var1)) {
         for (Path var10 : var8.filter(var0x -> var0x.getFileName().toString().endsWith(".jar")).toList()) {
            if (var10.equals(var7)) {
               SignedPackBlob var11 = readSignedBlob(var10);
               if (var11.marker() != null
                  && markerVersion(var3) == markerVersion(var11.marker())
                  && var4.equals(parseFingerprint(var11.marker()))
                  && matchesSignedBlob(var0, var5, var10, var11)) {
                  return true;
               }
            }

            if (var10.equals(var7) || isItemsAdderPlugin(var10)) {
               throw new IOException("A plugin update is already pending");
            }
         }
      }

      List var20 = var5.entries()
         .stream()
         .filter(
            var2x -> var6
               ? readEntryComment(var5, var2x).equals(var5.marker())
               : var2x.commentLength() == 0 && !var2x.name().endsWith("/") && !var2x.name().startsWith("META-INF/")
         )
         .sorted(Comparator.comparing(var1x -> PackIdentityFile.sha256Hex(var4 + "\n" + var1x.name())))
         .limit(3L)
         .toList();
      if (var20.isEmpty()) {
         throw new IOException("No unused archive comments");
      }

      verifyJarEntries(var0);
      String var21 = sha256File(var0);
      Path var22 = Files.createTempFile(var1, ".", ".tmp");

      try {
         writeWithCommentMarkers(var0, var5, var22, var20, var3);
         SignedPackBlob var23 = readSignedBlob(var22);
         if (var3.equals(var23.marker()) && matchesSignedBlob(var0, var5, var22, var23) && sha256File(var0).equals(var21)) {
            replaceAtomically(var0, var2);
            if (!sha256File(var0).equals(var21)) {
               throw new IOException("Archive changed during preparation");
            }

            Files.createLink(var7, var22);
            PackIdentityFile.syncDirectory(var1);
            return true;
         } else {
            throw new IOException("Archive changed during preparation");
         }
      } finally {
         Files.deleteIfExists(var22);
      }
   }

   private static SignedPackBlob readSignedBlob(Path var0) {
      if (!Files.isRegularFile(var0, LinkOption.NOFOLLOW_LINKS)) {
         throw new IOException("Invalid plugin archive");
      }

      try (FileChannel var1 = FileChannel.open(var0, StandardOpenOption.READ)) {
         long var2 = var1.size();
         if (var2 < 22L || var2 > 536870912L) {
            throw new IOException("Unsupported archive size");
         }

         byte[] var4 = readFully(var1, Math.max(0L, var2 - 65557L), (int)Math.min(var2, 65557L));
         ByteBuffer var5 = ByteBuffer.wrap(var4).order(ByteOrder.LITTLE_ENDIAN);
         int var6 = -1;

         for (int var7 = var4.length - 22; var7 >= 0; var7--) {
            if (var5.getInt(var7) == 101010256 && var7 + 22 + unsignedShort(var5.getShort(var7 + 20)) == var4.length) {
               var6 = var7;
               break;
            }
         }

         if (var6 < 0 || var5.getShort(var6 + 4) != 0 || var5.getShort(var6 + 6) != 0 || var5.getShort(var6 + 8) != var5.getShort(var6 + 10)) {
            throw new IOException("Invalid archive footer");
         }

         int var30 = unsignedShort(var5.getShort(var6 + 10));
         long var8 = Integer.toUnsignedLong(var5.getInt(var6 + 12));
         long var10 = Integer.toUnsignedLong(var5.getInt(var6 + 16));
         long var12 = var2 - var4.length + var6;
         if (var30 == 65535 || var8 > 67108864L || var10 + var8 != var12) {
            throw new IOException("ZIP64 or nonstandard archives are not supported");
         }

         byte[] var14 = readFully(var1, var10, (int)var8);
         ByteBuffer var15 = ByteBuffer.wrap(var14).order(ByteOrder.LITTLE_ENDIAN);
         ArrayList var16 = new ArrayList();
         HashSet var17 = new HashSet();
         int var18 = 0;
         String var19 = null;

         for (int var20 = 0; var20 < var30; var20++) {
            if (var18 > var14.length - 46 || var15.getInt(var18) != 33639248) {
               throw new IOException("Invalid archive directory");
            }

            int var21 = unsignedShort(var15.getShort(var18 + 28));
            int var22 = unsignedShort(var15.getShort(var18 + 30));
            int var23 = unsignedShort(var15.getShort(var18 + 32));
            int var24 = var18 + 46 + var21 + var22;
            int var25 = var24 + var23;
            if (var21 == 0 || var25 > var14.length || var15.getShort(var18 + 34) != 0 || Integer.toUnsignedLong(var15.getInt(var18 + 42)) >= var10) {
               throw new IOException("Invalid archive entry");
            }

            String var26 = new String(var14, var18 + 46, var21, StandardCharsets.UTF_8);
            if (!var17.add(var26)) {
               throw new IOException("Duplicate archive entry");
            }

            String var27 = new String(var14, var24, var23, StandardCharsets.UTF_8);
            if (var27.startsWith("{\"itemsadder_installation\":")) {
               parseFingerprint(var27);
               if (var19 != null && !var19.equals(var27)) {
                  throw new IOException("Conflicting installation markers");
               }

               var19 = var27;
            }

            var16.add(new PackFileDescriptor(var18, var24, var25, var26, var23));
            var18 = var25;
         }

         if (var18 != var14.length) {
            throw new IOException("Invalid archive directory size");
         } else {
            return new SignedPackBlob(var10, var14, Arrays.copyOfRange(var4, var6, var4.length), var16, var19);
         }
      }
   }

   private static void writeWithCommentMarkers(Path var0, SignedPackBlob var1, Path var2, List var3, String var4) {
      ByteArrayOutputStream var5 = new ByteArrayOutputStream(var1.directory().length + 1024);
      byte[] var6 = var4.getBytes(StandardCharsets.UTF_8);

      for (PackFileDescriptor var8 : var1.entries()) {
         if (!var3.contains(var8)) {
            var5.write(var1.directory(), var8.offset(), var8.end() - var8.offset());
         } else {
            byte[] var9 = Arrays.copyOfRange(var1.directory(), var8.offset(), var8.commentOffset());
            ByteBuffer.wrap(var9).order(ByteOrder.LITTLE_ENDIAN).putShort(32, (short)var6.length);
            var5.write(var9);
            var5.write(var6);
         }
      }

      byte[] var18 = (byte[])var1.end().clone();
      ByteBuffer.wrap(var18).order(ByteOrder.LITTLE_ENDIAN).putInt(12, var5.size());

      try (
         FileChannel var19 = FileChannel.open(var0, StandardOpenOption.READ);
         FileChannel var20 = FileChannel.open(var2, StandardOpenOption.WRITE);
      ) {
         long var10 = 0L;

         while (var10 < var1.directoryOffset()) {
            long var12 = var19.transferTo(var10, var1.directoryOffset() - var10, var20);
            if (var12 <= 0L) {
               throw new IOException("Incomplete archive copy");
            }

            var10 += var12;
         }

         writeFully(var20, var5.toByteArray());
         writeFully(var20, var18);
         var20.force(true);
      }
   }

   private static boolean matchesSignedBlob(Path var0, SignedPackBlob var1, Path var2, SignedPackBlob var3) {
      if (var1.directoryOffset() == var3.directoryOffset() && var1.entries().size() == var3.entries().size()) {
         if (!Arrays.equals(var1.end(), withCentralDirSize(var3.end(), var1.directory().length))) {
            return false;
         }

         for (int var4 = 0; var4 < var1.entries().size(); var4++) {
            PackFileDescriptor var15 = (PackFileDescriptor)var1.entries().get(var4);
            PackFileDescriptor var16 = (PackFileDescriptor)var3.entries().get(var4);
            byte[] var7 = Arrays.copyOfRange(var3.directory(), var16.offset(), var16.commentOffset());
            ByteBuffer.wrap(var7).order(ByteOrder.LITTLE_ENDIAN).putShort(32, (short)var15.commentLength());
            if (!Arrays.equals(Arrays.copyOfRange(var1.directory(), var15.offset(), var15.commentOffset()), var7)) {
               return false;
            }

            if (var15.commentLength() != 0
               && !readEntryComment(var1, var15).equals(readEntryComment(var3, var16))
               && (
                  markerVersion(var1.marker()) != 1
                     || markerVersion(var3.marker()) != 2
                     || !readEntryComment(var1, var15).equals(var1.marker())
                     || !readEntryComment(var3, var16).equals(var3.marker())
                     || !parseFingerprint(var1.marker()).equals(parseFingerprint(var3.marker()))
               )) {
               return false;
            }
         }

         try (
            FileChannel var14 = FileChannel.open(var0);
            FileChannel var5 = FileChannel.open(var2);
         ) {
            for (long var6 = 0L; var6 < var1.directoryOffset(); var6 += 65536L) {
               int var8 = (int)Math.min(65536L, var1.directoryOffset() - var6);
               if (!Arrays.equals(readFully(var14, var6, var8), readFully(var5, var6, var8))) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static byte[] withCentralDirSize(byte[] var0, int var1) {
      byte[] var2 = (byte[])var0.clone();
      ByteBuffer.wrap(var2).order(ByteOrder.LITTLE_ENDIAN).putInt(12, var1);
      return var2;
   }

   private static String readEntryComment(SignedPackBlob var0, PackFileDescriptor var1) {
      return new String(var0.directory(), var1.commentOffset(), var1.commentLength(), StandardCharsets.UTF_8);
   }

   private static void replaceAtomically(Path var0, Path var1) {
      Path var2 = Files.createTempFile(var1.getParent(), ".", ".tmp");

      try {
         Files.copy(var0, var2, StandardCopyOption.REPLACE_EXISTING);

         try (FileChannel var3 = FileChannel.open(var2, StandardOpenOption.WRITE)) {
            var3.force(true);
         }

         if (Files.mismatch(var0, var2) != -1L) {
            throw new IOException("Rollback verification failed");
         }

         Files.move(var2, var1, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         PackIdentityFile.syncDirectory(var1.getParent());
      } finally {
         Files.deleteIfExists(var2);
      }
   }

   static String sha256File(Path var0) {
      MessageDigest var1 = PackIdentityFile.sha256Digest();

      try (InputStream var2 = Files.newInputStream(var0)) {
         byte[] var3 = new byte[65536];

         int var4;
         while ((var4 = var2.read(var3)) != -1) {
            var1.update(var3, 0, var4);
         }
      }

      return HexFormat.of().formatHex(var1.digest());
   }

   private static void verifyJarEntries(Path var0) {
      try (JarFile var1 = new JarFile(var0.toFile(), true)) {
         long var2 = 0L;
         byte[] var4 = new byte[65536];

         for (JarEntry var6 : var1.stream().toList()) {
            CRC32 var7 = new CRC32();
            long var8 = 0L;

            int var11;
            try (InputStream var10 = var1.getInputStream(var6)) {
               while ((var11 = var10.read(var4)) != -1) {
                  var2 += var11;
                  var8 += var11;
                  if (var2 > 1073741824L) {
                     throw new IOException("Archive exceeds validation limits");
                  }

                  var7.update(var4, 0, var11);
               }
            }

            if (var8 != var6.getSize() || var7.getValue() != var6.getCrc()) {
               throw new IOException("Damaged archive entry");
            }
         }
      } catch (SecurityException var17) {
         throw new IOException("Invalid signed archive");
      }
   }

   private static boolean isItemsAdderPlugin(Path var0) {
      try (ZipFile var1 = new ZipFile(var0.toFile())) {
         ZipEntry var2 = var1.getEntry("plugin.yml");
         if (var2 == null || var2.getSize() > 32768L) {
            return false;
         }

         try (InputStream var3 = var1.getInputStream(var2)) {
            return Pattern.compile("(?m)^name:\\s*['\"]?ItemsAdder['\"]?\\s*(?:#.*)?$")
               .matcher(new String(var3.readNBytes(32769), StandardCharsets.UTF_8))
               .find();
         }
      } catch (ZipException var10) {
         return false;
      }
   }

   private static byte[] readFully(FileChannel var0, long var1, int var3) {
      ByteBuffer var4 = ByteBuffer.allocate(var3);

      while (var4.hasRemaining()) {
         int var5 = var0.read(var4, var1 + var4.position());
         if (var5 <= 0) {
            throw new IOException("Truncated archive");
         }
      }

      return var4.array();
   }

   private static void writeFully(FileChannel var0, byte[] var1) {
      ByteBuffer var2 = ByteBuffer.wrap(var1);

      while (var2.hasRemaining()) {
         var0.write(var2);
      }
   }

   private static int unsignedShort(short var0) {
      return Short.toUnsignedInt(var0);
   }
}
