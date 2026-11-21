package dev.lone.itemsadder.block;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.nms.NmsBlockSetterService;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.LocationScanner;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public final class StructureGrowListener implements IaListener {
   public void register(Plugin plugin) {
      EventRegistrar.registerIfAbsent(this, plugin);
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   protected void onBlockBreak(BlockBreakEvent var1) {
      if (!EventRegistrar.isCustomBlockBreakEvent(var1)) {
         if (method1073(var1.getBlock().getType())) {
            ItemStack var2 = var1.getPlayer().getItemInHand();
            if (var1.getPlayer().getGameMode() == GameMode.SURVIVAL) {
               for (ItemStack var4 : var1.getBlock().getDrops(var2)) {
                  LocationScanner.dropItem(var1.getBlock().getLocation(), var4);
               }
            }

            var1.setCancelled(true);
            NmsBlockSetterService.OD().method3402(var1.getBlock(), Material.AIR, true);
            if (var1.getPlayer().getGameMode() != GameMode.CREATIVE && ItemStackUtils.aA(var2)) {
               ItemStackUtils.method7239(var2, 1, true, true);
            }
         } else if (var1.getBlock().getType() == Material.PISTON && ContentManager.method8084().bm.method1166(var1.getBlock())) {
            for (Block var6 : LocationScanner.getNeighborBlocksWith(var1.getBlock(), false, true)) {
               if (var6.getType() == Material.PISTON_HEAD) {
                  if (var1.getPlayer().getGameMode() == GameMode.CREATIVE) {
                     var1.setCancelled(true);
                     var1.getBlock().setType(Material.AIR, false);
                  }

                  var6.setType(Material.AIR, false);
                  return;
               }
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   protected void onStructureGrow(StructureGrowEvent var1) {
      if (var1.getSpecies().compareTo(TreeType.BROWN_MUSHROOM) == 0 || var1.getSpecies().compareTo(TreeType.RED_MUSHROOM) == 0) {
         var1.setCancelled(true);

         for (BlockState var3 : var1.getBlocks()) {
            if (!BlockUtils.isProtectedMaterial(var3.getType())) {
               var3.getLocation().getBlock().setType(var3.getType());
            }

            ContentManager.method8084().bm.method1157(var3.getBlock(), true, true, true);
         }
      }
   }

   public static boolean method1073(Material var0) {
      return var0 == Material.BROWN_MUSHROOM_BLOCK || var0 == Material.RED_MUSHROOM_BLOCK || var0 == Material.MUSHROOM_STEM;
   }
}
