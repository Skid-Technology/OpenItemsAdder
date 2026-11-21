package dev.lone.itemsadder.block;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.NamedThreadFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.joml.Math;

public final class BlockSoundScheduler implements Listener {
   private static final int mM = 7;
   private static final int mN = 20;
   private final Plugin mO;
   private final ChunkBlockTracker mP;
   private final int mQ;
   private ExecutorService mR;
   private BukkitTask mS;
   private volatile boolean mT = false;

   public BlockSoundScheduler(Plugin var1, ChunkBlockTracker var2, int var3) {
      this.mO = var1;
      this.mP = var2;
      this.mQ = var3;
   }

   public void applySettings() {
      this.mT = true;
      int var1 = Math.max(1, this.mQ > 0 ? this.mQ : Runtime.getRuntime().availableProcessors() - 1);
      if (this.mR != null && !this.mR.isShutdown()) {
         this.mR.shutdownNow();
      }

      this.mR = new ThreadPoolExecutor(var1, var1, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new NamedThreadFactory("ItemsAdder-Crops-Grow-Worker"));
      if (this.mS != null) {
         this.mS.cancel();
      }

      this.mS = Bukkit.getScheduler().runTaskTimer(this.mO, () -> {
         try {
            this.tick();
         } catch (Throwable var2) {
            var2.printStackTrace();
         }
      }, 20L, 20L);
   }

   public void stop() {
      this.mT = false;
      if (this.mR != null) {
         this.mR.shutdownNow();
      }

      if (this.mS != null) {
         this.mS.cancel();
         this.mS = null;
      }
   }

   public void tick() {
      if (this.mT && !this.mR.isShutdown()) {
         this.mP.snapshotChunks(var1 -> {
            List var2 = var1.view().entries();
            if (!var2.isEmpty()) {
               ChunkKey var3 = var1.key();
               Chunk var4 = var3.resolveChunk();
               if (var4 != null) {
                  int var5 = var3.x();
                  int var6 = var3.z();
                  boolean var7 = var4.getWorld().getPlayers().stream().anyMatch(var2x -> {
                     Chunk var3x = var2x.getLocation().getChunk();
                     int var4x = java.lang.Math.abs(var3x.getX() - var5);
                     int var5x = java.lang.Math.abs(var3x.getZ() - var6);
                     return var4x <= 1 && var5x <= 1;
                  });
                  if (var7) {
                     Integer var8 = (Integer)var4.getWorld().getGameRuleValue(GameRule.RANDOM_TICK_SPEED);
                     double var9 = var8 != null ? var8.intValue() / 3.0 : 1.0;
                     this.mR.submit(() -> {
                        double var5x = 1.0;
                        ArrayList var7x = new ArrayList();
                        ThreadLocalRandom var8x = ThreadLocalRandom.current();

                        for (ChunkLocalBlock var10 : var2) {
                           BlockSoundProfile var11 = (BlockSoundProfile)ContentManager.method8084().au.get(var10.namespacedId());
                           if (var10.age() < var11.jW) {
                              double var12 = Math.clamp(var5x / var11.kb * var9, 0.0, 1.0);
                              if (var8x.nextDouble() < var12) {
                                 byte var14 = (byte)Math.min(var10.age() + 1, var11.jW);
                                 var7x.add(new ChunkBlockStateRecord(var10.relX(), var10.y(), var10.relZ(), var11, var14));
                              }
                           }
                        }

                        if (!var7x.isEmpty()) {
                           Bukkit.getScheduler().runTask(this.mO, () -> this.method7996(var1, var7x));
                        }
                     });
                  }
               }
            }
         });
      }
   }

   private void method7996(ChunkBlockEntry var1, List var2) {
      int var3 = var1.version();
      ChunkKey var4 = var1.key();
      Chunk var5 = var4.resolveChunk();
      if (var5 != null) {
         ArrayList var6 = new ArrayList(var2.size());

         for (ChunkBlockStateRecord var8 : var2) {
            BlockSoundProfile var9 = (BlockSoundProfile)ContentManager.method8084().au.get(var8.cropInfo().jP);
            Block var10 = var5.getBlock(var8.relX(), var8.y(), var8.relZ());
            byte var11 = var10.getRelative(0, 1, 0).getLightLevel();
            if (var11 >= var9.kc) {
               int var12 = 0;
               double var13 = 1.0;
               if (var9.kf != 0.0) {
                  Block var15 = var10.getRelative(0, -1, 0);
                  if (var15.getBlockData() instanceof Farmland var17) {
                     var12 = var17.getMoisture();
                  }

                  var13 = Math.lerp(1.0, var9.kf, var12 / 7.0);
               }

               double var24 = this.method7997(var10, var9);
               double var25 = var13 * var24;
               int var19 = (int)var25;
               double var20 = var25 - var19;
               boolean var22 = false;

               for (int var23 = 0; var23 < var19; var23++) {
                  var22 = true;
               }

               if (!var22 && var20 > 0.0) {
                  var22 = ThreadLocalRandom.current().nextDouble() < var20;
               }

               if (var22) {
                  var6.add(var8);
                  Log.devLog("[Crops] Growth accepted at" + var10.getLocation() + " to age " + var8.nextAge());
               }
            }
         }

         if (!var6.isEmpty()) {
            this.mP.applyChunkRecords(var4, var3, var6);
         }
      }
   }

   private double method7997(Block var1, BlockSoundProfile var2) {
      World var3 = var1.getWorld();
      if (var3.isThundering()) {
         return 1.0 + var2.kh;
      } else if (var3.hasStorm()) {
         return var1.getTemperature() <= 0.15 ? 1.0 + var2.ki : 1.0 + var2.kg;
      } else {
         return 1.0;
      }
   }
}
