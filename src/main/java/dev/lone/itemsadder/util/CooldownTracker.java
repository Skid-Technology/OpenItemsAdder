package dev.lone.itemsadder.util;

import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import org.bukkit.entity.Entity;

public class CooldownTracker {
   private int lastEntityId = -1;
   private long lastSeenMillis;
   private int hitCount;

   public boolean checkCooldown(Entity var1, long var2) {
      ProjectileTypeHelper.getAdapter().ag(var1);
      return this.isExpired(var1.getEntityId(), System.currentTimeMillis(), var2);
   }

   boolean isExpired(int var1, long var2, long var4) {
      if (var1 != this.lastEntityId || var2 - this.lastSeenMillis > var4) {
         this.lastEntityId = var1;
         this.hitCount = 0;
      }

      this.lastSeenMillis = var2;
      return ++this.hitCount >= 2;
   }
}
