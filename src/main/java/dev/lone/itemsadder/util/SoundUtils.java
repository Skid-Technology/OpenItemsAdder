package dev.lone.itemsadder.util;

import dev.lone.itemsadder.network.NativePacketsHolder;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class SoundUtils {
   public static void playSound(Location var0, String var1, SoundCategory var2, float var3, float var4) {
      NativePacketsHolder.PD().playSound(var1, var3, var4, var2, var0);
   }

   public static void playSound(Location var0, String var1, SoundCategory var2) {
      playSound(var0, var1, var2, 1.0F, 1.0F);
   }

   public static void playSound(Location var0, Sound var1, SoundCategory var2) {
      playSound(var0, var1, var2, 1.0F, 1.0F);
   }

   public static void playSoundTo(Player var0, Location var1, String var2, SoundCategory var3, float var4, float var5) {
      var0.playSound(var1, var2, var3, var4, var5);
   }

   public static void playSound(Location var0, SoundCategory var1, String var2) {
      NativePacketsHolder.PD().playSoundSimple(var2, var1, var0);
   }

   public static void playSound(Location var0, Sound var1, SoundCategory var2, float var3, float var4) {
      NativePacketsHolder.PD().playSound(var1.getKey().toString(), var3, var4, var2, var0);
   }
}
