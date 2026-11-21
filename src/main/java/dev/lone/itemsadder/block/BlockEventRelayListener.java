package dev.lone.itemsadder.block;

import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.ItemUseHandler;
import dev.lone.itemsadder.nms.NmsBlockSetterService;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.plugin.Plugin;

public final class BlockEventRelayListener implements IaListener {
   public void register(Plugin plugin) {
      EventRegistrar.registerIfAbsent(this, plugin);
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   private void onLiquidFlow(BlockFromToEvent var1) {
      if (var1.getToBlock().getType() == Material.CHORUS_PLANT && ContentManager.isCustomBlock(var1.getToBlock())) {
         var1.setCancelled(true);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onBlockBreak(BlockBreakEvent var1) {
      if (!EventRegistrar.isCustomBlockBreakEvent(var1)) {
         if (var1.getBlock().getType() == Material.CHORUS_PLANT) {
            if (var1.getBlock().getRelative(BlockFace.UP).getType() != Material.CHORUS_PLANT
               && var1.getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
               var1.setCancelled(true);
               NmsBlockSetterService.OD().method3401(var1.getBlock(), Material.AIR);
               ContentManager.method8084().bm.method1163(var1.getBlock().getLocation());
            } else {
               var1.getBlock().setType(Material.AIR, true);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
   private void onItemSpawn(ItemSpawnEvent var1) {
      if (var1.getEntity().getItemStack().getType() == Material.CHORUS_FRUIT) {
         if (ContentManager.isCustomBlock(var1.getLocation().getBlock()) || ContentManager.method8084().bm.method1167(var1.getLocation().getBlock())) {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler(priority = EventPriority.LOWEST)
   private void onProjectileHit(ProjectileHitEvent var1) {
      if (var1.getHitBlock() != null && var1.getHitBlock().getType() == Material.CHORUS_PLANT) {
         CustomBlockChunkStoreBase var2 = ContentManager.method8084().method8183(var1.getHitBlock());
         if (var2.isCustomBlock(var1.getHitBlock())) {
            if (ProjectileTypeHelper.method3230(var1.getEntity())) {
               ProjectileTypeHelper.method3227(var1.getEntity());
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.LOWEST)
   private void onCustomBlockInteract(CustomBlockInteractEvent var1) {
      if (var1.getItem() != null && var1.getItem().getType() == Material.WATER_BUCKET) {
         if (var1.internal.aps == BlockRealizationMode.REAL_TRANSPARENT || var1.internal.aps == BlockRealizationMode.REAL_WIRE) {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onBucketEmpty(PlayerBucketEmptyEvent var1) {
      if (var1.getBlockClicked().getType() == Material.CHORUS_PLANT) {
         CustomBlockChunkStoreBase var2 = ContentManager.method8084().method8183(var1.getBlockClicked());
         if (var2.isCustomBlock(var1.getBlockClicked())) {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onBlockPlace(BlockPlaceEvent var1) {
      if (!EventRegistrar.isCustomBlockPlaceEvent(var1)) {
         if (!ItemUseHandler.aX(var1.getPlayer())) {
            if (var1.getBlockPlaced().getType() == Material.CHORUS_PLANT) {
               NmsBlockSetterService.OD().bk(var1.getBlock());
            }
         }
      }
   }
}
