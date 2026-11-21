package dev.lone.itemsadder.Core.OtherPlugins;

import com.sk89q.worldedit.math.BlockVector3;
import dev.lone.itemsadder.Core.OtherPlugins.WorldEditSchematicSidecar.Data;
import dev.lone.itemsadder.Core.OtherPlugins.WorldEditSchematicSidecar.Stamp;
import dev.lone.itemsadder.resourcepack.ByteNamedKey;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

final class WorldEditSchematicSidecar {
   private static final int MAGIC = 1229018949;
   private static final int VERSION = 1;
   private static final int MAX_ENTRIES = 1000000;
   private static final long MAX_BYTES = 67108864L;

   private WorldEditSchematicSidecar() {
   }

   static Stamp stamp(URI schematic) {
      Path var1 = Path.of(schematic);
      return Files.isRegularFile(var1) ? new Stamp(Files.size(var1), Files.getLastModifiedTime(var1)) : null;
   }

   static Path path(URI schematic) {
      if (schematic != null && "file".equalsIgnoreCase(schematic.getScheme())) {
         Path var1 = Path.of(schematic);
         return var1.resolveSibling(var1.getFileName() + ".ia-data");
      } else {
         return null;
      }
   }

   static void writeWhenReady(URI schematic, Data data, Stamp before) {
      Path var3 = path(schematic);
      if (var3 != null) {
         Path var4 = Path.of(schematic);
         long var5 = -1L;
         FileTime var7 = null;
         byte var8 = 0;
         long var9 = System.nanoTime() + 300000000000L;

         while (System.nanoTime() < var9) {
            if (Thread.interrupted()) {
               throw new InterruptedException();
            }

            Stamp var11 = stamp(schematic);
            if (var11 != null && !var11.equals(before)) {
               long var12 = var11.size();
               FileTime var14 = var11.modified();
               var8 = (byte)(var12 > 0L && var12 == var5 && var14.equals(var7) ? var8 + 1 : 0);
               var5 = var12;
               var7 = var14;
               if (var8 >= 5) {
                  byte[] var15 = digest(var4);
                  if (var12 == Files.size(var4) && var14.equals(Files.getLastModifiedTime(var4))) {
                     if (data.isEmpty()) {
                        Files.deleteIfExists(var3);
                     } else {
                        write(var3, var15, data);
                     }

                     return;
                  }

                  var8 = 0;
                  continue;
               }
            }

            Thread.sleep(100L);
         }

         throw new IOException("Schematic save did not finish: " + var4);
      }
   }

   private static void write(Path sidecar, byte[] digest, Data data) {
      Path var3 = Files.createTempFile(sidecar.getParent(), ".itemsadder-schematic-", ".tmp");

      try {
         try (DataOutputStream var4 = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(var3)))) {
            var4.writeInt(1229018949);
            var4.writeInt(1);
            var4.write(digest);
            var4.writeInt(data.blocks().size());

            for (Entry var6 : data.blocks().entrySet()) {
               writePosition(var4, (BlockVector3)var6.getKey());
               var4.writeUTF((String)var6.getValue());
            }

            var4.writeInt(data.crops().size());

            for (Entry var17 : data.crops().entrySet()) {
               writePosition(var4, (BlockVector3)var17.getKey());
               var4.writeUTF(((ByteNamedKey)var17.getValue()).namespacedId());
               var4.writeByte(((ByteNamedKey)var17.getValue()).age());
            }
         }

         try {
            Files.move(var3, sidecar, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         } catch (AtomicMoveNotSupportedException var13) {
            Files.move(var3, sidecar, StandardCopyOption.REPLACE_EXISTING);
         }
      } finally {
         Files.deleteIfExists(var3);
      }
   }

   static Data read(URI schematic) {
      Path var1 = path(schematic);
      if (var1 != null && Files.isRegularFile(var1)) {
         if (Files.size(var1) > 67108864L) {
            throw new IOException("ItemsAdder schematic companion is too large: " + var1);
         }

         try (DataInputStream var2 = new DataInputStream(new BufferedInputStream(Files.newInputStream(var1)))) {
            if (var2.readInt() != 1229018949 || var2.readInt() != 1) {
               throw new IOException("Invalid ItemsAdder schematic companion: " + var1);
            }

            byte[] var3 = var2.readNBytes(32);
            if (var3.length != 32 || !MessageDigest.isEqual(var3, digest(Path.of(schematic)))) {
               throw new IOException("ItemsAdder schematic companion does not match: " + schematic);
            }

            HashMap var4 = new HashMap();
            int var5 = count(var2);

            for (int var6 = 0; var6 < var5; var6++) {
               var4.put(readPosition(var2), var2.readUTF());
            }

            HashMap var11 = new HashMap();
            var5 = count(var2);

            for (int var7 = 0; var7 < var5; var7++) {
               var11.put(readPosition(var2), new ByteNamedKey(var2.readUTF(), var2.readByte()));
            }

            if (var2.read() != -1) {
               throw new IOException("Trailing data in ItemsAdder schematic companion: " + var1);
            } else {
               return new Data(Map.copyOf(var4), Map.copyOf(var11));
            }
         }
      } else {
         return Data.EMPTY;
      }
   }

   private static int count(DataInputStream in) {
      int var1 = in.readInt();
      if (var1 >= 0 && var1 <= 1000000) {
         return var1;
      } else {
         throw new IOException("Invalid ItemsAdder schematic entry count: " + var1);
      }
   }

   private static void writePosition(DataOutputStream out, BlockVector3 position) {
      out.writeInt(position.x());
      out.writeInt(position.y());
      out.writeInt(position.z());
   }

   private static BlockVector3 readPosition(DataInputStream in) {
      return BlockVector3.at(in.readInt(), in.readInt(), in.readInt());
   }

   private static byte[] digest(Path file) {
      try {
         MessageDigest var1 = MessageDigest.getInstance("SHA-256");

         try (BufferedInputStream var2 = new BufferedInputStream(Files.newInputStream(file))) {
            byte[] var3 = new byte[65536];

            int var4;
            while ((var4 = var2.read(var3)) != -1) {
               var1.update(var3, 0, var4);
            }
         }

         return var1.digest();
      } catch (NoSuchAlgorithmException var7) {
         throw new IllegalStateException(var7);
      }
   }
}
