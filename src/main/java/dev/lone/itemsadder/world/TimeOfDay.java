package dev.lone.itemsadder.world;

import org.bukkit.World;

public enum TimeOfDay {
   ANY(-1, -1),
   UNKNOWN(-1, -1),
   DAY(1000, 6000),
   NOON(6000, 13000),
   NIGHT(13000, 18000),
   MIDNIGHT(18000, 23999);

   final int startTick;
   final int endTick;

   TimeOfDay(int var3, int var4) {
      this.startTick = var3;
      this.endTick = var4;
   }

   public int getStartTick() {
      return this.startTick;
   }

   public int getEndTick() {
      return this.endTick;
   }

   public static TimeOfDay fromTime(long var0) {
      for (TimeOfDay var5 : values()) {
         if (var0 >= var5.getStartTick() && var0 < var5.getEndTick()) {
            return var5;
         }
      }

      return UNKNOWN;
   }

   public static TimeOfDay fromWorld(World var0) {
      return fromTime(var0.getTime());
   }

   public TimeOfDay parseTimeOfDay(String var1) {
      var1 = var1.toUpperCase();

      try {
         return valueOf(var1);
      } catch (Exception var3) {
         return UNKNOWN;
      }
   }

   public static boolean isBetween(World var0, TimeOfDay var1, TimeOfDay var2) {
      return var0.getTime() >= var1.getStartTick() && var0.getTime() <= var2.getEndTick();
   }
}
