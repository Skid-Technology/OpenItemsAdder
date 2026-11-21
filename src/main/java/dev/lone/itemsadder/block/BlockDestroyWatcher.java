package dev.lone.itemsadder.block;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BlockDestroyWatcher implements Listener {
   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   private void onBlockDestroy(BlockDestroyEvent var1) {
      Block var2 = var1.getBlock();
      if (var2.getType() == Material.TRIPWIRE) {
         CustomBlockChunkStoreBase var3 = ContentManager.method8084().method8183(var2);
         String var4 = var3.getCustomBlockId(var2);
         if (var4 != null) {
            CustomBlockDefinition var5 = ContentManager.method8084().method8160(var4);
            if (var5 == null) {
               return;
            }

            var1.setCancelled(true);
            SchedulerUtils.runLaterSync(() -> var5.placeBlockDefault(var2), 1L);
         }
      }
   }
}
