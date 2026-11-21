package dev.lone.itemsadder.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public final class YamlFileSnapshot {
   private final Path bNY;
   private final byte[] bNZ;
   private final boolean bOa;
   private final String bOb;
   private Path bOc;
   private final List bOd;

   private YamlFileSnapshot(Path var1, byte[] var2, String var3, boolean var4, List var5) {
      this.bNY = var1;
      this.bNZ = var2;
      this.bOb = var3;
      this.bOa = var4;
      this.bOd = List.copyOf(var5);
   }

   public String IC() {
      return this.bOb;
   }

   public Path ada() {
      return this.bOc;
   }

   public List adb() {
      return this.bOd;
   }

   public void abY() {
      if (this.bOa) {
         Path var1 = this.bNY.toAbsolutePath().getParent();
         Files.createDirectories(var1);
         if (this.bNZ == null ? !Files.exists(this.bNY) : Files.exists(this.bNY) && Arrays.equals(this.bNZ, Files.readAllBytes(this.bNY))) {
            if (this.bNZ != null) {
               this.bOc = var1.resolve("config.yml.backup-" + UUID.randomUUID());
               Files.copy(this.bNY, this.bOc, StandardCopyOption.COPY_ATTRIBUTES);
            }

            Path var2 = Files.createTempFile(var1, ".config-", ".yml");

            try {
               if (this.bNZ != null && Files.getFileStore(this.bNY).supportsFileAttributeView("posix")) {
                  Files.setPosixFilePermissions(var2, Files.getPosixFilePermissions(this.bNY));
               }

               Files.writeString(var2, this.bOb, StandardCharsets.UTF_8);
               new YamlNodeDocument(Files.readString(var2, StandardCharsets.UTF_8));
               if (this.bNZ == null ? Files.exists(this.bNY) : !Files.exists(this.bNY) || !Arrays.equals(this.bNZ, Files.readAllBytes(this.bNY))) {
                  throw new IOException("config.yml changed while saving; retry the reload");
               }

               Files.move(var2, this.bNY, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } finally {
               Files.deleteIfExists(var2);
            }
         } else {
            throw new IOException("config.yml changed while loading; retry the reload");
         }
      }
   }
}
