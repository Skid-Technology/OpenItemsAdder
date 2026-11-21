package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.network.NativePacketsHolder;
import io.netty.channel.ChannelHandlerContext;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import net.kyori.adventure.key.Key;

public final class PackHttpPipelineInstaller {
   private static final String bKp = "itemsadder_http_packet_listener";
   private static volatile PackHttpServer bKq = new PackHttpServer();

   private PackHttpPipelineInstaller() {
   }

   public static void install() {
      bKq.close();
      PackHttpServer var0 = new PackHttpServer();
      bKq = var0;
      Settings var1 = Settings.get();
      PackDownloadThrottle var2 = new PackDownloadThrottle(var1.simpleSelfHostDef);
      NativePacketsHolder.PD()
         .registerPacketHook(
            "itemsadder_http_packet_listener", Key.key("itemsadder", "http_packet_listener"), "timeout", () -> new PackHttpRequestDecoder(var2, var0)
         );
   }

   public static void uninstall() {
      bKq.close();
      NativePacketsHolder.PD().unregisterHook("itemsadder_http_packet_listener");
   }

   private static String remoteIp(ChannelHandlerContext var0) {
      if (var0.channel().remoteAddress() instanceof InetSocketAddress var1) {
         InetAddress var3 = var1.getAddress();
         return var3 == null ? var1.getHostString() : var3.getHostAddress();
      } else {
         return "unknown";
      }
   }
}
