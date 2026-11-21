package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.NativeValue;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.DisplayEntity;
import dev.lone.itemsadder.item.ItemVariantKey;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.Nullable;

public final class NativeValueCache {
   private final Cache aRB = CacheBuilder.newBuilder().maximumSize(1024L).expireAfterAccess(10L, TimeUnit.MINUTES).build();
   private final Set aRC = ConcurrentHashMap.newKeySet();

   @Nullable
   public NativeValue method3152(int var1, int var2, boolean var3) {
      ItemVariantKey var4 = new ItemVariantKey(var1, var2, var3);
      NativeValue var5 = (NativeValue)this.aRB.getIfPresent(var4);
      if (var5 != null) {
         return var5;
      }

      if (var1 == -1) {
         return this.method3153(var4);
      }

      if (CustomEntityManager.isOwnerThread()) {
         if (this.aRC.add(var4)) {
            SchedulerUtils.runOnMainThread(() -> {
               try {
                  this.method3153(var4);
               } finally {
                  this.aRC.remove(var4);
               }
            });
         }

         return null;
      } else {
         return this.method3153(var4);
      }
   }

   private NativeValue method3153(ItemVariantKey var1) {
      return this.aRB
         .asMap()
         .computeIfAbsent(var1, var0 -> NativePacketsHolder.PE().Py().wrapBukkitItem(DisplayEntity.method3077(var0.aRD, var0.aRE, var0.aRF)));
   }

   public void NT() {
      this.aRB.invalidateAll();
   }

   long size() {
      this.aRB.cleanUp();
      return this.aRB.size();
   }

   void method3154(ItemVariantKey var1) {
      this.aRB.put(var1, (NativeValue)() -> var1);
   }
}
