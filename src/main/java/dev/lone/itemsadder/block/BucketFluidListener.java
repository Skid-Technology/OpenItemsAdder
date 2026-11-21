package dev.lone.itemsadder.block;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.nms.NmsBlockSetterService;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EnvironmentDetector;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import java.util.WeakHashMap;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Tripwire;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public final class BucketFluidListener implements IaListener {
   WeakHashMap ahV = new WeakHashMap();

   public void register(Plugin plugin) {
      EventRegistrar.registerIfAbsent(this, plugin);
      if (EnvironmentDetector.paperAdventure) {
         try {
            EventRegistrar.registerIfAbsent(new BlockDestroyWatcher(), plugin);
         } catch (Throwable var3) {
            Log.warn("Failed to register Paper event listener for tripwires.", var3);
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onTripwireInteract(PlayerInteractEvent var1) {
      if (var1.getAction() == Action.PHYSICAL) {
         if (var1.getClickedBlock().getType() != Material.TRIPWIRE) {
            return;
         }

         var1.setCancelled(true);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   private void onTripwirePhysics(BlockPhysicsEvent var1) {
      Block var2 = var1.getBlock();
      if (var2.getType() == Material.TRIPWIRE) {
         Material var3 = var1.getSourceBlock().getType();
         var1.setCancelled(true);
         CustomBlockChunkStoreBase var4 = ContentManager.method8084().method8183(var2);
         if (var4.isCustomBlock(var2)) {
            CustomBlockDefinition var5 = ContentManager.method8084().method8160(var4.getCustomBlockId(var2));
            if (var5 == null) {
               return;
            }

            Material var6 = var2.getType();
            if (var3 == Material.NOTE_BLOCK) {
               SchedulerUtils.runLaterSync(() -> {
                  if (var2.getType() == var6 && var5.getNamespacedID().equals(var4.getCustomBlockId(var2))) {
                     var5.placeBlockWithPhysics(var2, false);
                  }
               }, 5L);
            } else {
               var5.placeBlockWithPhysics(var2, false);
            }
         } else {
            Tripwire var7 = (Tripwire)var2.getBlockData();
            if (!var7.isAttached() && var3 != Material.TRIPWIRE_HOOK) {
               NmsBlockSetterService.OD().method3401(var2, Material.TRIPWIRE);
               SchedulerUtils.runLaterSync(() -> {
                  for (Player var2x : BlockFaceUtils.playersWithin(var2.getLocation(), 32)) {
                     var2x.sendBlockChange(var2.getLocation(), NoteBlockDataTable.agR);
                  }
               }, 2L);
            } else {
               NmsBlockSetterService.OD().method3401(var2, Material.AIR);
               SchedulerUtils.runLaterSync(() -> {
                  for (Player var3x : BlockFaceUtils.playersWithin(var2.getLocation(), 10)) {
                     if (!this.ahV.containsKey(var3x) || this.ahV.containsKey(var3x) && System.currentTimeMillis() - (Long)this.ahV.get(var3x) > 1000L) {
                        Log.sendPrefixed(var3x, Main.aFi.lj("tripwires-disabled"));
                     }

                     this.ahV.put(var3x, System.currentTimeMillis());
                  }
               }, 2L);
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onTripwirePlace(BlockPlaceEvent var1) {
      if (!EventRegistrar.isCustomBlockPlaceEvent(var1)) {
         Block var2 = var1.getBlock();
         if (var2.getType() == Material.TRIPWIRE) {
            var1.setCancelled(true);
            ItemStackUtils.au(var1.getItemInHand());
            SchedulerUtils.runLaterSync(() -> var2.setBlockData(NoteBlockDataTable.agR, false), 2L);
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   public void onBucketEmptyFilter(PlayerBucketEmptyEvent var1) {
      Block var2 = var1.getBlock();
      CustomBlockChunkStoreBase var3 = ContentManager.method8084().method8183(var2);
      if (var3.isCustomBlock(var2)) {
         var1.setCancelled(true);
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   public void onTripwireFlow(BlockFromToEvent var1) {
      if (var1.getToBlock().getType() == Material.TRIPWIRE) {
         var1.setCancelled(true);
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   public void onTripwireDrop(BlockDropItemEvent var1) {
      if (var1.getItems().size() > 0 && var1.getBlockState().getType() == Material.TRIPWIRE) {
         Block var2 = var1.getBlock();
         CustomBlockChunkStoreBase var3 = ContentManager.method8084().method8183(var2);
         if (var3.isCustomBlock(var2)) {
            var1.getItems().clear();
            var1.setCancelled(true);
         }
      }
   }
}
