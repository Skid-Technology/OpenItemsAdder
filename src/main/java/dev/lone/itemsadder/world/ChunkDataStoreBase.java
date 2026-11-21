package dev.lone.itemsadder.world;

import dev.lone.itemsadder.Core.OtherPlugins.AdvancedSlimePaperWrapper;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.PersistentCounter;
import java.io.File;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public abstract class ChunkDataStoreBase {
   final Plugin plugin;
   final String regionFolder;
   protected final Map regionStores = new ConcurrentHashMap();
   final AtomicBoolean saving = new AtomicBoolean(false);
   private final ConcurrentHashMap worldLocks = new ConcurrentHashMap();

   public ReadWriteLock getWorldLock(String var1) {
      return this.worldLocks.computeIfAbsent(var1, var0 -> new ReentrantReadWriteLock());
   }

   public ChunkDataStoreBase(Plugin var1) {
      this.plugin = var1;
      this.regionFolder = "/region/";
   }

   public Map getRegionStores() {
      return this.regionStores;
   }

   public abstract CustomBlockChunkStoreBase getOrCreateRegion(World var1, int var2, int var3);

   public abstract CustomBlockChunkStoreBase getOrLoadRegion(World var1, int var2, int var3);

   public CustomBlockChunkStoreBase getRegionForChunk(Chunk var1) {
      return this.getOrCreateRegion(var1.getWorld(), var1.getX(), var1.getZ());
   }

   public CustomBlockChunkStoreBase getRegionForLocation(Location var1) {
      return this.getRegionForChunk(var1.getChunk());
   }

   public boolean isSaving() {
      return this.saving.get();
   }

   public boolean hasRegion(int var1) {
      return this.regionStores.containsKey(var1);
   }

   @Nullable
   public CustomBlockChunkStoreBase loadExistingRegion(World var1, int var2, int var3) {
      ReadWriteLock var4 = this.getWorldLock(var1.getName());
      var4.readLock().lock();

      try {
         File var5 = null;

         try {
            var5 = regionFile(var1, var2, var3, this.regionFolder);
            return new CustomBlockChunkStoreBase(var5, var1);
         } catch (Exception var10) {
            Log.error("Could not load region data '" + var2 + ", " + var3 + "'. Original file preserved: " + var5, var10);
         }
      } finally {
         var4.readLock().unlock();
      }

      return null;
   }

   @Nullable
   public CustomBlockChunkStoreBase loadOrCreateRegion(World var1, int var2, int var3, @Nullable Consumer var4) {
      ReadWriteLock var5 = this.getWorldLock(var1.getName());
      var5.readLock().lock();

      try {
         File var6 = null;
         CustomBlockChunkStoreBase var7 = null;

         try {
            var6 = this.getRegionFile(var1, var2, var3);
            var7 = new CustomBlockChunkStoreBase(var6, var1);
            if (var4 != null) {
               var4.accept(var7);
            }

            return var7;
         } catch (Exception var15) {
            if (var7 != null) {
               try {
                  var7.closeFiles();
               } catch (Exception var14) {
                  var15.addSuppressed(var14);
               }
            }

            Log.error("Could not load region data for chunk '" + var2 + ", " + var3 + "'. Original file preserved: " + var6, var15);
         }
      } finally {
         var5.readLock().unlock();
      }

      return null;
   }

   public synchronized void saveAllRegions(boolean var1) {
      if (!Bukkit.isPrimaryThread()) {
         throw new IllegalStateException("Live region data must be saved on the server thread");
      }

      this.saving.set(true);

      try {
         PersistentCounter var2 = new PersistentCounter();
         int var3 = 0;

         for (Iterator var4 = this.regionStores.entrySet().iterator(); var4.hasNext(); var3++) {
            CustomBlockChunkStoreBase var5 = (CustomBlockChunkStoreBase)((Entry)var4.next()).getValue();
            var5.save();
            var4.remove();
         }

         if (Settings.get().logBlockSaveLoad && var2.lapFromStart() >= Settings.get().logBlockSaveLoadMinMs) {
            Log.log(ChatColor.YELLOW + "Saved " + var3 + " regions of all worlds in " + var2.lapFromStart() + "ms");
         }
      } finally {
         this.saving.set(false);
      }
   }

   void saveWorldRegions(World var1, boolean var2, boolean var3) {
      if (!Bukkit.isPrimaryThread()) {
         throw new IllegalStateException("Live region data must be saved on the server thread");
      }

      this.saving.set(true);

      try {
         Iterator var4 = this.regionStores.entrySet().iterator();

         while (var4.hasNext()) {
            CustomBlockChunkStoreBase var5 = (CustomBlockChunkStoreBase)((Entry)var4.next()).getValue();
            if (var5.world.getName().equals(var1.getName())) {
               var5.saveRegion(var3);
               if (var3) {
                  var4.remove();
               }
            }
         }
      } finally {
         if (var3) {
            this.worldLocks.remove(var1.getName());
         }

         this.saving.set(false);
      }
   }

   static File regionFile(World var0, int var1, int var2, String var3) {
      if (AdvancedSlimePaperWrapper.inst().isSlimeWorld(var0)) {
         File var4 = Bukkit.getServer().getWorldContainer();
         return Paths.get(new File(var4, "slime_worlds").getPath(), "itemsadder/" + var0.getName(), var3, "r." + var1 + "." + var2 + ".pregion").toFile();
      } else {
         return Paths.get(var0.getWorldFolder().getPath(), "itemsadder", var3, "r." + var1 + "." + var2 + ".pregion").toFile();
      }
   }

   static File regionFileForChunk(World var0, int var1, int var2, String var3) {
      int var4 = var1 >> 5;
      int var5 = var2 >> 5;
      return regionFile(var0, var4, var5, var3);
   }

   File getRegionFile(World var1, int var2, int var3) {
      return regionFileForChunk(var1, var2, var3, this.regionFolder);
   }
}
