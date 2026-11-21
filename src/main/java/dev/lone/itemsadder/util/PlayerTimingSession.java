package dev.lone.itemsadder.util;

import org.bukkit.entity.Player;

public class PlayerTimingSession {
   public final Player player;
   private final int timeoutMs;
   private final int windowMs;
   private final int maxClicks;
   public long lastStopMs = System.currentTimeMillis();
   public long windowStartMs = System.currentTimeMillis();
   public long lastWarnMs = -1L;
   public int clickCount = 0;

   public PlayerTimingSession(Player var1, int var2, int var3, int var4) {
      this.player = var1;
      this.timeoutMs = var2;
      this.windowMs = var3;
      this.maxClicks = var4;
   }

   public void start() {
      if (this.lastWarnMs == -1L || System.currentTimeMillis() - this.lastWarnMs >= 350L) {
         Log.error("Warning! " + this.player.getName() + " " + this.player.getUniqueId() + " is spamming recipe click packets!");
         this.lastWarnMs = System.currentTimeMillis();
      }
   }

   public void stop() {
      this.lastStopMs = System.currentTimeMillis();
      this.clickCount++;
      if (System.currentTimeMillis() - this.windowStartMs >= this.windowMs) {
         this.clickCount = 0;
      }
   }

   public boolean isTimedOut() {
      return System.currentTimeMillis() - this.lastStopMs < this.timeoutMs;
   }

   public boolean isCompleted() {
      if (System.currentTimeMillis() - this.windowStartMs >= this.windowMs) {
         this.windowStartMs = System.currentTimeMillis();
         boolean var1 = this.clickCount > this.maxClicks;
         this.clickCount = 0;
         return var1;
      } else {
         return false;
      }
   }
}
