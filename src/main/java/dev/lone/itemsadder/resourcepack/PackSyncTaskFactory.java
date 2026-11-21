package dev.lone.itemsadder.resourcepack;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class PackSyncTaskFactory {
   private PackSyncTaskFactory() {
   }

   public static Runnable method6928(JavaPlugin var0, Path var1, PackSignaturePayload var2) {
      Server var3 = var0.getServer();
      Path var4 = var0.getDataFolder().toPath().toAbsolutePath();
      String var5 = var3.getUpdateFolder();
      UUID var6 = var3.getWorlds().isEmpty() ? null : ((World)var3.getWorlds().getFirst()).getUID();
      return () -> {
         if (var0.isEnabled()) {
            try {
               Path var6x = PackInstallationManifest.resolveUpdateDir(var4.getParent(), var5);
               if (var6x == null) {
                  return;
               }

               if (var6 == null) {
                  throw new IOException("Primary world unavailable");
               }

               Path var7 = PackPathUtils.resolveRealSource(var1, var4.getParent());
               String var8 = PackInstallationManifest.readFingerprint(var7);
               if (PackInstallationManifest.readMarkerVersion(var7) == 2) {
                  return;
               }

               PackInstallationManifest.prepareUpdateDir(var7.getParent(), var6x);
               PackIdentityFile var9 = PackIdentityFile.open(var4, var6);
               if (var8 != null && !var8.equals(var9.getIdentityHash())) {
                  return;
               }

               Path var10 = var9.getStatePath().resolve(PackIdentityFile.sha256Hex("ia-rollback-v1\n" + var9.id()));
               if (PackInstallationManifest.stageUpdate(var7, var6x, var10, PackSignatureService.signPayload(var9, var6, var2))) {
                  var0.getLogger().info("Startup metadata prepared. Restart the server to apply it.");
               }
            } catch (Exception var11) {
               var0.getLogger().warning("Could not prepare startup metadata (" + var11.getClass().getSimpleName() + "). The original JAR was preserved.");
            }
         }
      };
   }
}
