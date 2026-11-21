package dev.lone.itemsadder.util;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.nms.NmsRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

public final class AnnouncementSinkHolder implements Listener {
   private final AnnouncementSink sink = (AnnouncementSink)NmsRegistry.method2777(AnnouncementSink.class);
   private static AnnouncementSinkHolder instance;

   AnnouncementSinkHolder() {
      EventRegistrar.registerListener(this, Main.getInstance());
   }

   public static AnnouncementSink getSink() {
      if (instance == null) {
         instance = new AnnouncementSinkHolder();
      }

      return instance.sink;
   }

   public static void sendResourcePack(Player player, String url, @Nullable String uuid, String hash, boolean required, String promptMessage) {
      getSink().sendResourcePack(player, url, uuid, hash, required, promptMessage);
   }
}
