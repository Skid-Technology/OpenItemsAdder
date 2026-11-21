package dev.lone.itemsadder.resourcepack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Locale;
import java.util.UUID;

public final class PackIdentityFile {
   private final UUID bKT;
   private final String bKU;
   private final Path bKV;

   private PackIdentityFile(UUID var1, String var2, Path var3) {
      this.bKT = var1;
      this.bKU = var2;
      this.bKV = var3;
   }

   public static PackIdentityFile open(Path var0, UUID var1) {
      Path var2 = var0.resolve("storage");
      Path var3 = var2.resolve(sha256Hex("ia-location-v1\n" + var1));
      if (!Files.isSymbolicLink(var0) && !Files.isSymbolicLink(var2) && !Files.isSymbolicLink(var3)) {
         Files.createDirectories(var3);
         Path var4 = var3.resolve(sha256Hex("ia-state-v1\n" + var1));
         Path var6 = var3.resolve(sha256Hex("ia-lock-v1\n" + var1));
         if (!Files.isSymbolicLink(var6) && !Files.isSymbolicLink(var4)) {
            UUID var5;
            try (
               FileChannel var7 = FileChannel.open(var6, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
               FileLock var8 = var7.lock();
            ) {
               if (Files.exists(var4, LinkOption.NOFOLLOW_LINKS)) {
                  if (Files.size(var4) != 36L) {
                     throw new IOException("Invalid installation state");
                  }

                  try {
                     var5 = UUID.fromString(Files.readString(var4, StandardCharsets.US_ASCII));
                  } catch (IllegalArgumentException var13) {
                     throw new IOException("Invalid installation state");
                  }
               } else {
                  var5 = UUID.randomUUID();
                  writeAtomic(var4, var5.toString().getBytes(StandardCharsets.US_ASCII));
               }
            }

            return new PackIdentityFile(var5, buildIdentity(var5, var1, machineFingerprint()), var3);
         } else {
            throw new IOException("Unsafe installation storage");
         }
      } else {
         throw new IOException("Unsafe installation storage");
      }
   }

   public static String buildIdentity(UUID var0, UUID var1, String var2) {
      return sha256Hex("ia-installation-v1\n" + var0 + "\n" + var1 + "\n" + var2);
   }

   static String machineFingerprint() {
      for (Path var3 : new Path[]{Path.of("/etc/machine-id"), Path.of("/var/lib/dbus/machine-id")}) {
         if (Files.isRegularFile(var3)) {
            if (Files.size(var3) > 128L) {
               throw new IOException("Invalid system identity");
            }

            String var4 = Files.readString(var3, StandardCharsets.US_ASCII).strip().toLowerCase(Locale.ROOT);
            if (var4.matches("[a-f0-9]{32}") && !var4.equals("0".repeat(32))) {
               return sha256Hex("itemsadder-machine-v1\n" + var4);
            }

            throw new IOException("Invalid system identity");
         }
      }

      return "portable";
   }

   public UUID id() {
      return this.bKT;
   }

   public String getIdentityHash() {
      return this.bKU;
   }

   public Path getStatePath() {
      return this.bKV;
   }

   public static String sha256Hex(String var0) {
      return HexFormat.of().formatHex(sha256Digest().digest(var0.getBytes(StandardCharsets.UTF_8)));
   }

   static MessageDigest sha256Digest() {
      try {
         return MessageDigest.getInstance("SHA-256");
      } catch (NoSuchAlgorithmException var1) {
         throw new IllegalStateException(var1);
      }
   }

   static void writeAtomic(Path var0, byte[] var1) {
      if (Files.isSymbolicLink(var0)) {
         throw new IOException("Unsafe persistence target");
      }

      Path var2 = Files.createTempFile(var0.getParent(), ".", ".tmp");

      try {
         try (FileChannel var3 = FileChannel.open(var2, StandardOpenOption.WRITE)) {
            ByteBuffer var4 = ByteBuffer.wrap(var1);

            while (var4.hasRemaining()) {
               var3.write(var4);
            }

            var3.force(true);
         }

         Files.move(var2, var0, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         syncDirectory(var0.getParent());
      } finally {
         Files.deleteIfExists(var2);
      }
   }

   static void syncDirectory(Path var0) {
      if (!System.getProperty("os.name", "").startsWith("Windows")) {
         try (FileChannel var1 = FileChannel.open(var0, StandardOpenOption.READ)) {
            var1.force(true);
         }
      }
   }
}
