package dev.lone.itemsadder.item;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;

class InventoryMoveListener implements IaListener {
   InventoryMoveListener(BlockBreakListener var1) {
      this.afg = var1;
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void method561(InventoryMoveItemEvent var1) {
      if (var1.getDestination().getType() == InventoryType.JUKEBOX) {
         Jukebox var2 = (Jukebox)var1.getDestination().getHolder();
         Block var3 = var2.getBlock();
         CustomBlockChunkStoreBase var4 = ContentManager.method8084().method8183(var3);
         if (this.afg.afe.containsKey(var3) || var4.getBlockStringData(var3) != null) {
            this.afg.method548(var3, var4);
            var2.eject();
         }
      } else if (var1.getSource().getType() == InventoryType.JUKEBOX) {
         Block var5 = ((Jukebox)var1.getSource().getHolder()).getBlock();
         CustomBlockChunkStoreBase var6 = ContentManager.method8084().method8183(var5);
         if (this.afg.afe.containsKey(var5) || var6.getBlockStringData(var5) != null) {
            this.afg.method548(var5, var6);
         }
      }
   }
}
