package dev.lone.itemsadder.resourcepack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

final class PackPathUtils {
   private PackPathUtils() {
   }

   static Path resolveRealSource(Path var0, Path var1) {
      Path var2 = var1.toRealPath();
      Path var3 = var0.toRealPath();
      if (var3.getParent().equals(var2) && !Files.isSymbolicLink(var0)) {
         return var3;
      }

      if (!var3.getParent().equals(var2.resolve(".paper-remapped"))) {
         throw new IOException("Unsupported plugin source");
      }

      Path var4 = var3.getParent().resolve("index.json");
      if (Files.isRegularFile(var4) && Files.size(var4) <= 4194304L) {
         try {
            JsonObject var5 = JsonParser.parseString(Files.readString(var4)).getAsJsonObject().getAsJsonObject("hashes");
            ArrayList var6 = new ArrayList();

            try (Stream var7 = Files.list(var2)) {
               List var8 = var7.filter(var0x -> var0x.getFileName().toString().matches("ItemsAdder.*\\.jar")).toList();
               if (var8.size() > 32) {
                  throw new IOException("Ambiguous plugin sources");
               }

               for (Path var10 : var8) {
                  if (!Files.isSymbolicLink(var10)) {
                     String var11 = PackInstallationManifest.sha256File(var10);
                     JsonElement var12 = var5.get(var11);
                     JsonElement var13 = var5.get(var11.toUpperCase(Locale.ROOT));
                     if (var12 != null && var13 != null && !var12.equals(var13)) {
                        throw new IOException("Conflicting remapping provenance");
                     }

                     if (var12 == null) {
                        var12 = var13;
                     }

                     if (var12 != null && var3.getFileName().toString().equals(var12.getAsString())) {
                        var6.add(var10);
                     }
                  }
               }
            }

            if (var6.size() != 1) {
               throw new IOException("Ambiguous plugin sources");
            } else {
               return (Path)var6.getFirst();
            }
         } catch (RuntimeException var16) {
            throw new IOException("Invalid remapping provenance");
         }
      } else {
         throw new IOException("Missing remapping provenance");
      }
   }
}
