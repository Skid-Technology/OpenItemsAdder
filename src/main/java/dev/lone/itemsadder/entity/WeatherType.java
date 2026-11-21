package dev.lone.itemsadder.entity;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

public enum WeatherType {
   CLEAR,
   RAIN,
   THUNDER,
   SNOW;

   @Nullable
   public static WeatherType method176(Location var0) {
      World var1 = var0.getWorld();
      if (var1 == null) {
         return null;
      } else if (var1.isThundering()) {
         return THUNDER;
      } else if (var1.isClearWeather()) {
         return CLEAR;
      } else {
         return var0.getBlock().getTemperature() < 0.15F ? SNOW : RAIN;
      }
   }
}
