package dev.lone.itemsadder.Core.OtherPlugins;

import com.fastasyncworldedit.core.extent.processor.ProcessorScope;
import com.fastasyncworldedit.core.queue.IBatchProcessor;
import com.fastasyncworldedit.core.queue.IChunk;
import com.fastasyncworldedit.core.queue.IChunkGet;
import com.fastasyncworldedit.core.queue.IChunkSet;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.sk89q.worldedit.world.block.BlockTypesCache;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.Core.OtherPlugins.FaweWorldEditBridge.Change;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import org.bukkit.Bukkit;
import org.bukkit.World;

final class FaweWorldEditBridge$Processor implements IBatchProcessor {
   private final World world;
   private final WorldEditIntegration integration;
   private final BitSet missingBlockOrdinals;

   private FaweWorldEditBridge$Processor(World world, WorldEditIntegration integration) {
      this.world = world;
      this.integration = integration;
      this.missingBlockOrdinals = integration.missingBlockOrdinals();
   }

   public IChunkSet processSet(IChunk chunk, IChunkGet before, IChunkSet after) {
      if (!this.missingBlockOrdinals.isEmpty()) {
         for (int var4 = after.getMinSectionPosition(); var4 <= after.getMaxSectionPosition(); var4++) {
            char[] var5 = after.loadIfPresent(var4);
            if (var5 != null) {
               boolean var6 = false;

               for (int var7 = 0; var7 < var5.length; var7++) {
                  if (this.missingBlockOrdinals.get(var5[var7])) {
                     var5[var7] = BlockTypes.AIR.getDefaultState().getOrdinalChar();
                     var6 = true;
                  }
               }

               if (var6) {
                  after.setBlocks(var4, var5);
               }
            }
         }
      }

      after.entities().removeIf(entity -> this.integration.missingEntityDefinition(new CompoundTag(entity.linTag())));
      return after;
   }

   public Future postProcessSet(IChunk chunk, IChunkGet before, IChunkSet after) {
      ArrayList var4 = new ArrayList();
      int var5 = chunk.getX();
      int var6 = chunk.getZ();

      for (int var7 = after.getMinSectionPosition(); var7 <= after.getMaxSectionPosition(); var7++) {
         if (after.hasSection(var7)) {
            char[] var8 = after.loadIfPresent(var7);
            if (var8 != null) {
               for (int var9 = 0; var9 < var8.length; var9++) {
                  char var10 = var8[var9];
                  if (var10 != 0) {
                     int var11 = (var5 << 4) + (var9 & 15);
                     int var12 = (var7 << 4) + (var9 >> 8);
                     int var13 = (var6 << 4) + (var9 >> 4 & 15);
                     BlockState var14 = BlockTypesCache.states[var10];
                     var4.add(new Change(var11, var12, var13, this.integration.idOf(var14), var14.getBlockType()));
                  }
               }
            }
         }
      }

      if (var4.isEmpty()) {
         return CompletableFuture.completedFuture(null);
      }

      CompletableFuture var15 = new CompletableFuture();
      Runnable var16 = () -> {
         try {
            CustomBlockChunkStoreBase var5x = ContentManager.method8084().method8185(this.world, var5 >> 5, var6 >> 5);

            for (Change var7x : var4) {
               this.integration.afterFaweBlockChange(this.world, var7x.method1(), var7x.method2(), var7x.method3(), var7x.blockType());
               if (var5x != null) {
                  String var8x = var5x.getCustomBlockIdAt(var7x.method1(), var7x.method2(), var7x.method3(), var5, var6);
                  if (!Objects.equals(var8x, var7x.id())) {
                     if (var8x != null) {
                        var5x.removeCustomBlockAt(var5, var6, var7x.method1(), var7x.method2(), var7x.method3());
                     }

                     if (var7x.id() != null) {
                        var5x.setCustomBlockAt(var5, var6, var7x.method1(), var7x.method2(), var7x.method3(), var7x.id());
                     }
                  }
               }
            }

            var15.complete(null);
         } catch (Throwable var9x) {
            var15.completeExceptionally(var9x);
         }
      };
      if (Bukkit.isPrimaryThread()) {
         var16.run();
      } else {
         Bukkit.getScheduler().runTask(Main.getInstance(), var16);
      }

      return var15;
   }

   public Extent construct(Extent child) {
      return child;
   }

   public ProcessorScope getScope() {
      try {
         return ProcessorScope.valueOf("READING_BLOCKS");
      } catch (IllegalArgumentException var2) {
         return ProcessorScope.valueOf("READING_SET_BLOCKS");
      }
   }
}
