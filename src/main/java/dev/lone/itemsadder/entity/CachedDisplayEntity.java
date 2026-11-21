package dev.lone.itemsadder.entity;

import beer.devs.nativepackets.api.NativeValue;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.lone.itemsadder.block.CustomBlockStateSettings;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.bukkit.Location;

public final class CachedDisplayEntity extends DisplayEntity {
   private static final Cache aSI = CacheBuilder.newBuilder().maximumSize(128L).expireAfterAccess(15L, TimeUnit.MINUTES).build();
   private final CustomEntityImpl aSJ;
   private String aSK;
   private int aSL = -1;
   private boolean aSM;
   private boolean aSN;
   private boolean aSO;

   public CachedDisplayEntity(Location var1, CustomEntityModel var2, CustomEntityImpl var3) {
      super(var1, false, var2, var3);
      this.aSJ = var3;
   }

   @Override
   public void addUpdatePackets(List viewers, int customModelData) {
      this.cy(customModelData);
      this.method3078(viewers);
   }

   @Override
   public void updateFor(EntityViewer viewer, int customModelData) {
      this.cy(customModelData);
      this.method3079(viewer);
   }

   @Override
   public void addTickPackets(List viewers, int customModelData) {
      this.addUpdatePackets(viewers, customModelData);
   }

   @Override
   public void addSpawnPackets(List viewers) {
      if (!this.Ob()) {
         super.addSpawnPackets(viewers);
         if (viewers.stream().anyMatch(EntityViewer::kW)) {
            this.aSO = true;
         }

         this.aSN = false;
      }
   }

   @Override
   public void spawnFor(EntityViewer viewer) {
      if (!this.Ob()) {
         super.spawnFor(viewer);
         if (viewer.kW()) {
            this.aSO = true;
         }

         this.aSN = false;
      }
   }

   private boolean Ob() {
      if (this.aSJ.jD() && !this.aSO && !this.aSM) {
         this.aSN = true;
         return true;
      } else {
         return false;
      }
   }

   private void cy(int var1) {
      if (var1 == -1) {
         this.aSL = -1;
         this.aSM = false;
         super.method3073(-1, false);
      } else {
         String var2 = this.playerSkin;
         if (var2 != null) {
            if (var1 != this.aSL || !var2.equals(this.aSK)) {
               this.aSL = var1;
               this.aSK = var2;
               this.aSM = false;
               CompletableFuture var3 = aSI.asMap().computeIfAbsent(var2, CachedDisplayEntity::eW);
               var3.whenComplete((var2x, var3x) -> this.aSJ.dispatchToManager(() -> {
                  if (var3x == null) {
                     this.method3122(var2, var2x);
                  }
               }));
            }
         }
      }
   }

   public boolean method3122(String var1, Map var2) {
      if (this.aSL == -1 || this.aSM) {
         return true;
      }

      if (var1.equals(this.playerSkin) && var1.equals(this.aSK)) {
         NativeValue var3 = (NativeValue)var2.get(this.aSL);
         if (var3 == null) {
            return false;
         }

         this.aSM = true;
         this.method3076(var3);
         if (this.aSN) {
            this.addSpawnPackets(this.aSJ.getViewers());
         }

         this.method3078(this.aSJ.getViewers());
         return true;
      } else {
         return false;
      }
   }

   public static CompletableFuture fetchTextureAsync(String var0) {
      return aSI.asMap().computeIfAbsent(var0, CachedDisplayEntity::eW);
   }

   public static void clearCache() {
      aSI.invalidateAll();
   }

   private static CompletableFuture eW(String var0) {
      CompletableFuture var1 = new CompletableFuture();
      SchedulerUtils.submitAsync(
         () -> {
            try {
               int[] var2 = new int[]{
                  CustomBlockStateSettings.bvr,
                  CustomBlockStateSettings.bvt,
                  CustomBlockStateSettings.bvs,
                  CustomBlockStateSettings.bvw,
                  CustomBlockStateSettings.bvu,
                  CustomBlockStateSettings.bvv
               };
               HashMap var3 = new HashMap(var2.length);

               for (int var7 : var2) {
                  var3.put(var7, AbstractFakeEntity.method3028(var7, var0));
               }

               SchedulerUtils.runOnMainThread(() -> {
                  try {
                     HashMap var2x = new HashMap(var3.size());
                     var3.forEach((var1xxx, var2xx) -> var2x.put(var1xxx, NativePacketsHolder.PE().Py().wrapBukkitItem(var2xx)));
                     var1.complete(Map.copyOf(var2x));
                  } catch (RuntimeException var3x) {
                     var1.completeExceptionally(var3x);
                  }
               });
            } catch (RuntimeException var8) {
               var1.completeExceptionally(var8);
            }
         }
      );
      var1.whenComplete((var1x, var2) -> {
         if (var2 != null) {
            aSI.invalidate(var0);
            Log.warn("Could not prepare V2 player model skin for " + var0, var2);
         }
      });
      return var1;
   }
}
