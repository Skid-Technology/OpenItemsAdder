package dev.lone.itemsadder.util;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.MinecraftServer.ServerResourcePackInfo;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R4.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class AnnouncementSinkImplG implements AnnouncementSink {
   private static final Pattern bkY = Pattern.compile("^[a-fA-F0-9]{40}$");

   @Override
   public void sendResourcePack(Player player, String url, @Nullable String uuid, String hash, boolean required, String promptMessage) {
      EntityPlayer var7 = ((CraftPlayer)player).getHandle();
      var7.f
         .b(
            new ClientboundResourcePackPushPacket(
               uuid == null ? UUID.nameUUIDFromBytes(url.getBytes(StandardCharsets.UTF_8)) : UUID.fromString(uuid),
               url,
               hash,
               required,
               Optional.ofNullable(CraftChatMessage.fromStringOrNull(promptMessage, true))
            )
         );
   }

   @Override
   public void setServerResourcePack(String url, @Nullable String uuid, String hash, boolean required, Component adventurePrompt) {
      DedicatedServer var6 = (DedicatedServer)MinecraftServer.getServer();
      Optional var7;
      if (url.isEmpty()) {
         var7 = Optional.empty();
      } else {
         if (hash.isEmpty()) {
            Log.warn("You specified a resource pack without providing an hash. Pack will be updated on the client only if you change the URL of the pack.");
         } else if (!bkY.matcher(hash).matches()) {
            Log.warn("Invalid resourcepack hash: " + hash);
         }

         IChatBaseComponent var8 = (IChatBaseComponent)AdventureText.toNative(adventurePrompt);
         UUID var9;
         if (uuid != null && !uuid.isEmpty()) {
            try {
               var9 = UUID.fromString(uuid);
            } catch (IllegalArgumentException var12) {
               Log.warn(MessageFormat.format("Failed to parse \"{0}\" into UUID", uuid));
               return;
            }
         } else {
            var9 = UUID.nameUUIDFromBytes(url.getBytes(StandardCharsets.UTF_8));
            Log.warn("resource-pack-id missing, using default: " + var9);
         }

         var7 = Optional.of(new ServerResourcePackInfo(var9, url, hash, required, var8));
      }

      Field var13 = ServerCompat.findFieldByType(var6.a().getClass(), var6.a().T.getClass());
      var13.setAccessible(true);

      try {
         var13.set(var6.a(), var7);
      } catch (IllegalAccessException var11) {
         throw new RuntimeException(var11);
      }
   }

   @Override
   public void clearServerResourcePack() {
      DedicatedServer var1 = (DedicatedServer)MinecraftServer.getServer();
      Field var2 = ServerCompat.findFieldByType(var1.a().getClass(), var1.a().T.getClass());
      var2.setAccessible(true);

      try {
         var2.set(var1.a(), Optional.empty());
      } catch (IllegalAccessException var4) {
         throw new RuntimeException(var4);
      }
   }
}
