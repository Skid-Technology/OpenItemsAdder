package dev.lone.itemsadder.util;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface AnnouncementSink {
   void sendResourcePack(Player var1, String var2, @Nullable String var3, String var4, boolean var5, String var6);

   void setServerResourcePack(String var1, @Nullable String var2, String var3, boolean var4, @Nullable Component var5);

   void clearServerResourcePack();
}
