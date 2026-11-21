package dev.lone.itemsadder.util;

import dev.lone.itemsadder.network.NativePacketsHolder;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DebugMarkerSender {
   private static final String MARKER_ADD_CHANNEL = "debug/game_test_add_marker";
   private static final String MARKER_CLEAR_CHANNEL = "debug/game_test_clear";

   public static void sendMarker(Player var0, Location var1, int var2, String var3) {
      sendMarkerWithColor(var0, var1, var2, 999999999, var3);
   }

   public static void sendMarkerWithColor(Player var0, Location var1, int var2, int var3, String var4) {
      Object var5 = NativePacketsHolder.PE().Pv().customPayload("debug/game_test_add_marker", buildMarkerPayload(packPosition(var1), var2, var4, var3));
      NativePacketsHolder.PE().method1867(var0, var5);
   }

   public static void clearMarkers(Player var0) {
      Object var1 = NativePacketsHolder.PE().Pv().customPayload("debug/game_test_clear", new byte[0]);
      NativePacketsHolder.PE().method1868(var0, var1);
   }

   static long packPosition(Location var0) {
      int var1 = var0.getBlockX();
      int var2 = var0.getBlockY();
      int var3 = var0.getBlockZ();
      return (var1 & 67108863L) << 38 | var2 & 4095L | (var3 & 67108863L) << 12;
   }

   static byte[] buildMarkerPayload(long var0, int var2, String var3, int var4) {
      byte[] var5 = var3.getBytes(StandardCharsets.UTF_8);
      ByteArrayOutputStream var6 = new ByteArrayOutputStream(17 + var5.length);

      try {
         DataOutputStream var7 = new DataOutputStream(var6);
         var7.writeLong(var0);
         var7.writeInt(var2);
         writeVarInt(var7, var5.length);
         var7.write(var5);
         var7.writeInt(var4);
         return var6.toByteArray();
      } catch (IOException var8) {
         throw new UncheckedIOException(var8);
      }
   }

   private static void writeVarInt(DataOutputStream var0, int var1) {
      while ((var1 & -128) != 0) {
         var0.writeByte(var1 & 127 | 128);
         var1 >>>= 7;
      }

      var0.writeByte(var1);
   }
}
