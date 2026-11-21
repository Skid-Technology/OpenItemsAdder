package dev.lone.itemsadder.world;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.lone.itemsadder.Core.OtherPlugins.AdvancedSlimePaperWrapper;
import dev.lone.itemsadder.block.CustomBlockListener;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.IntTableConstantsK;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.Plugin;

public class ChunkDataStore extends ChunkDataStoreBase implements Listener {
   Cache recentlyUnloadedWorlds = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.SECONDS).build();
   private static final CustomBlockChunkStoreBase dummyStore = new CustomBlockChunkStore();

   public ChunkDataStore(Plugin var1) {
      super(var1);
      EventRegistrar.registerListener(this, var1);
      short var2 = 1300;
      SchedulerUtils.scheduleRepeatingSync(var1x -> {
         Iterator var2x = this.regionStores.entrySet().iterator();

         while (var2x.hasNext()) {
            Entry var3 = (Entry)var2x.next();
            CustomBlockChunkStoreBase var4 = (CustomBlockChunkStoreBase)var3.getValue();

            try {
               SaveStatus var5 = var4.handleAutosave();
               switch (IntTableConstantsK.SWITCH_MAP[var5.ordinal()]) {
                  case 1:
                     var2x.remove();
                  case 2:
               }
            } catch (Throwable var6) {
               Log.error("Error saving region file: " + var4.getDebugName(), var6);
            }
         }
      }, var2, var2);
   }

   @Override
   public CustomBlockChunkStoreBase getOrCreateRegion(World var1, int var2, int var3) {
      if (!Settings.get().blockWorlds.isWorldActive(var1)) {
         return dummyStore;
      }

      int var5 = CustomBlockChunkStoreBase.regionHash(var1, var2, var3);
      CustomBlockChunkStoreBase var4;
      if (!this.hasRegion(var5)) {
         var4 = this.loadOrCreateRegion(var1, var2, var3, null);
         if (var4 != null) {
            this.regionStores.put(var4.regionId, var4);
            var4.loadChunkRecord(var2, var3, false);
         }
      } else {
         var4 = (CustomBlockChunkStoreBase)this.regionStores.get(var5);
      }

      return var4;
   }

   @Override
   public CustomBlockChunkStoreBase getOrLoadRegion(World var1, int var2, int var3) {
      if (!Settings.get().blockWorlds.isWorldActive(var1)) {
         return dummyStore;
      }

      int var5 = CustomBlockChunkStoreBase.chunkHash(var1, var2, var3);
      CustomBlockChunkStoreBase var4;
      if (!this.hasRegion(var5)) {
         var4 = this.loadExistingRegion(var1, var2, var3);
         if (var4 != null) {
            this.regionStores.put(var4.regionId, var4);
         }
      } else {
         var4 = (CustomBlockChunkStoreBase)this.regionStores.get(var5);
      }

      return var4;
   }

   public CustomBlockChunkStoreBase lookupRegionForChunk(Chunk var1) {
      return this.getOrCreateRegion(var1.getWorld(), var1.getX(), var1.getZ());
   }

   public CustomBlockChunkStoreBase lookupRegionForBlock(Block var1) {
      return this.getOrCreateRegion(var1.getWorld(), var1.getX() >> 4, var1.getZ() >> 4);
   }

   @EventHandler
   private void onWorldSave(WorldSaveEvent var1) {
      if (this.recentlyUnloadedWorlds.getIfPresent(var1.getWorld().getName()) != null) {
         this.recentlyUnloadedWorlds.invalidate(var1.getWorld().getName());
      } else {
         this.saveWorldRegions(var1.getWorld(), false, false);
      }
   }

   @EventHandler
   private void onWorldUnload(WorldUnloadEvent var1) {
      ContentManager.method8084().ap.unloadWorld(var1.getWorld());
      Settings.get().blockWorlds.forgetWorld(var1.getWorld());
      this.recentlyUnloadedWorlds.put(var1.getWorld().getName(), (byte)0);
      this.saveWorldRegions(var1.getWorld(), false, true);
   }

   @EventHandler
   private void onWorldLoad(WorldLoadEvent var1) {
      Settings.get().blockWorlds.updateWorldState(var1.getWorld());
      this.recentlyUnloadedWorlds.invalidate(var1.getWorld().getName());
   }

   @EventHandler(priority = EventPriority.LOW)
   private void onChunkLoad(ChunkLoadEvent var1) {
      if (Settings.get().blockWorlds.isWorldActive(var1.getWorld())) {
         Chunk var2 = var1.getChunk();
         CustomBlockChunkStoreBase var3 = this.lookupRegionForChunk(var2);
         if (var3 == null) {
            Log.error("Failed to load chunk (region is null). " + var2.getX() + " | " + var2.getZ());
         } else {
            ContentManager.method8084().ap.loadChunkStates(var2, var3);
            if (!AdvancedSlimePaperWrapper.inst().isSlimeWorld(var2.getWorld())) {
               SchedulerUtils.runOnMainThread(() -> CustomBlockListener.method1173(var1, var3));
            }
         }
      }
   }

   @EventHandler
   private void onChunkUnload(ChunkUnloadEvent var1) {
      ContentManager.method8084().ap.unloadChunk(var1.getWorld(), var1.getChunk().getX(), var1.getChunk().getZ());
   }
}
