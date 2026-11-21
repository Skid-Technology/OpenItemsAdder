package dev.lone.itemsadder.item;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class BlockDestroyListener implements Listener {
   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   private void method9646(BlockDestroyEvent var1) {
      Block var2 = var1.getBlock();
      if (PlantItemDefinition.aD(var2)) {
         CustomBlockChunkStoreBase var3 = ContentManager.method8084().method8183(var2);
         String var4 = var3.getCropSeedId(var2);
         if (var4 == null) {
            return;
         }

         PlantItemDefinition var5 = (PlantItemDefinition)ContentManager.method8084().method8153(PlantItemDefinition.class, var4);
         if (var5 == null) {
            return;
         }

         var1.setCancelled(true);
         if (BlockUtils.isFullyGrown(var2)) {
            var5.method8840(var2.getLocation());
         } else {
            ItemStack var6 = var5.Ds();
            LocationScanner.dropItem(var2.getLocation(), var6.clone());
         }

         var3.removeCustomBlockAndRefreshNms(var2, true, true);
      }
   }
}
