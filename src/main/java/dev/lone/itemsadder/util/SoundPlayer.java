package dev.lone.itemsadder.util;

import dev.lone.itemsadder.config.Settings;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class SoundPlayer {
   String name;
   Sound sound;
   Sound fallbackSound;
   float volume;
   float pitch;

   public SoundPlayer(String var1, Sound var2) {
      this.fallbackSound = var2;
      this.pitch = 1.0F;
      this.volume = 1.0F;
      if (var1 != null) {
         try {
            this.sound = Sound.valueOf(var1);
         } catch (Exception var4) {
            this.name = var1;
         }
      }
   }

   public float getVolume() {
      return this.volume;
   }

   public void setVolume(float var1) {
      this.volume = var1;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setPitch(float var1) {
      this.pitch = var1;
   }

   public void play(Player var1, Location var2) {
      this.play(var1, var2, SoundCategory.MASTER);
   }

   public void play(Player var1, Location var2, SoundCategory var3) {
      if (this.isSilent()) {
         if (this.sound != null) {
            var1.playSound(var2, this.sound, var3, this.volume, this.pitch);
         } else if (this.name != null) {
            String var4 = this.name;
            Settings var5 = Settings.get();
            if (var5.customBlockSounds && (var4.startsWith("minecraft:block.stone.") || var4.startsWith("minecraft:block.wood."))) {
               String var6 = var4.substring(var4.lastIndexOf(46) + 1);

               boolean var7 = switch (var6) {
                  case "step" -> var5.customBlockSoundsStep;
                  case "break", "fall", "hit", "place" -> var5.customBlockSoundsNonStep;
                  default -> false;
               };
               if (var7) {
                  var4 = "minecraft:internal." + var4.substring("minecraft:".length());
               }
            }

            var1.playSound(var2, var4, var3, this.volume, this.pitch);
         } else if (this.fallbackSound != null) {
            var1.playSound(var2, this.fallbackSound, var3, this.volume, this.pitch);
         }
      }
   }

   public void play(Location var1, SoundCategory var2) {
      if (this.isSilent()) {
         for (Player var4 : BlockFaceUtils.playersWithin(var1, 16)) {
            this.play(var4, var1, var2);
         }
      }
   }

   public boolean isSilent() {
      return this.sound != null || this.name != null || this.fallbackSound != null;
   }
}
