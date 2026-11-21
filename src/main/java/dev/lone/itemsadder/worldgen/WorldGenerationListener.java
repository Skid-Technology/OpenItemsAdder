package dev.lone.itemsadder.worldgen;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Utils;
import dev.lone.itemsadder.world.ChunkBlockHookHolder;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldGenerationListener implements Listener {
   WorldGenerationListener() {
      EventRegistrar.registerIfAbsent(this, Main.getPlugin());
   }

   @EventHandler
   private void method1971(WorldLoadEvent var1) {
      ContentManager var2 = ContentManager.method8084();
      if (var2 != null && var2.method8110()) {
         WorldGenerationManager.method1961(var1.getWorld(), BlockDecorator.aDN);
         WorldGenerationManager.method1961(var1.getWorld(), TreeDecorator.aDN);
         WorldGenerationManager.method1961(var1.getWorld(), SurfaceFeature.aDT);
         WorldGenerationManager.method1961(var1.getWorld(), AttachedDecorator.aDN);
         WorldGenerationManager.method1959(var1.getWorld(), BlockPopulator.qj);
         WorldGenerationManager.method1959(var1.getWorld(), TreePopulator.qj);
         WorldGenerationManager.method1959(var1.getWorld(), SurfacePopulator.qj);
         WorldGenerationManager.method1959(var1.getWorld(), CavePopulator.qj);
      }
   }

   @EventHandler
   private void method1972(WorldUnloadEvent var1) {
      WorldGenerationManager.method1963(var1.getWorld());
   }

   @EventHandler
   private void method1973(ChunkPopulateEvent var1) {
      ContentManager var2 = ContentManager.method8084();
      if (var2 != null && var2.method8110()) {
         CavePopulator var3 = (CavePopulator)CavePopulator.aEe.get(var1.getWorld().getUID());
         if (var3 != null) {
            long var4 = var3.KQ();
            if (var3.method1498(var4)) {
               List var6 = ChunkBlockHookHolder.getInstance().hook.findAirBlocksInRange(var1.getChunk(), var3.aEf, var3.aEg);

               for (AttachedDecorator var8 : var3.KP()) {
                  if (!var3.method1498(var4)) {
                     return;
                  }

                  var3.method1363(var6, var1.getChunk(), Utils.random, var8, var4);
               }
            }
         }
      }
   }
}
