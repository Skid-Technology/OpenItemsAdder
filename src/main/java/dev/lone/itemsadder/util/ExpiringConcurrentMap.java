package dev.lone.itemsadder.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Deprecated
public class ExpiringConcurrentMap extends ConcurrentHashMap {
   private final Map timestamps = new ConcurrentHashMap();
   private long expireAfterMillis = 6000L;
   @Nullable
   private BukkitTask cleanupTask;

   public ExpiringConcurrentMap() {
      this.init();
   }

   public ExpiringConcurrentMap(long var1) {
      this.expireAfterMillis = var1;
      this.init();
   }

   public void init() {
      this.cleanupTask = SchedulerUtils.scheduleRepeating(var1 -> this.evictExpired(), this.expireAfterMillis, this.expireAfterMillis);
   }

   @Override
   public Object put(@NotNull Object key, @NotNull Object value) {
      this.timestamps.put(key, System.currentTimeMillis());
      return super.put(key, value);
   }

   @Override
   public void putAll(Map m) {
      for (Entry var3 : m.entrySet()) {
         this.put(var3.getKey(), var3.getValue());
      }
   }

   @Override
   public Object putIfAbsent(Object key, Object value) {
      Object var3 = super.putIfAbsent(key, value);
      this.timestamps.put(key, System.currentTimeMillis());
      return var3;
   }

   @Override
   public Object remove(@NotNull Object key) {
      Object var2 = super.remove(key);
      if (var2 != null) {
         this.timestamps.remove(key);
         if (this.size() == 0) {
            this.stopCleanupTask();
         }
      }

      return var2;
   }

   @Override
   public boolean remove(Object key, Object value) {
      boolean var3 = super.remove(key, value);
      if (var3) {
         this.timestamps.remove(key, value);
         if (this.size() == 0) {
            this.stopCleanupTask();
         }
      }

      return var3;
   }

   public void clearAll() {
      this.clear();
      this.timestamps.clear();
      this.stopCleanupTask();
   }

   private void evictExpired() {
      long var1 = System.currentTimeMillis();
      Iterator var3 = this.timestamps.entrySet().iterator();

      while (var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         if (var1 >= (Long)this.timestamps.get(var4.getKey()) + this.expireAfterMillis) {
            var3.remove();
            this.timestamps.remove(var4.getKey());
         }
      }

      if (this.size() == 0) {
         this.stopCleanupTask();
      }
   }

   private void stopCleanupTask() {
      if (this.cleanupTask != null) {
         this.cleanupTask.cancel();
         this.cleanupTask = null;
      }
   }
}
