package ia.sh.customblockdata;

import ia.sh.customblockdata.events.CustomBlockDataMoveEvent;
import ia.sh.customblockdata.events.CustomBlockDataRemoveEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.plugin.Plugin;

final class BlockDataListener implements Listener {
   private final Plugin plugin;
   private final Predicate<Block> customDataPredicate;

   public BlockDataListener(Plugin plugin) {
      this.plugin = plugin;
      this.customDataPredicate = block -> CustomBlockData.hasCustomBlockData(block, plugin);
   }

   private CustomBlockData getCbd(BlockEvent event) {
      return this.getCbd(event.getBlock());
   }

   private CustomBlockData getCbd(Block block) {
      return new CustomBlockData(block, this.plugin);
   }

   private void callAndRemove(BlockEvent blockEvent) {
      if (this.callEvent(blockEvent)) {
         this.getCbd(blockEvent).clear();
      }
   }

   private boolean callEvent(BlockEvent blockEvent) {
      return this.callEvent(blockEvent.getBlock(), blockEvent);
   }

   private boolean callEvent(Block block, Event bukkitEvent) {
      if (CustomBlockData.hasCustomBlockData(block, this.plugin) && !CustomBlockData.isProtected(block, this.plugin)) {
         CustomBlockDataRemoveEvent cbdEvent = new CustomBlockDataRemoveEvent(this.plugin, block, bukkitEvent);
         Bukkit.getPluginManager().callEvent(cbdEvent);
         return !cbdEvent.isCancelled();
      } else {
         return false;
      }
   }

   private void callAndRemoveBlockStateList(List<BlockState> blockStates, Event bukkitEvent) {
      blockStates.stream().<Block>map(BlockState::getBlock).filter(this.customDataPredicate).forEach(block -> this.callAndRemove(block, bukkitEvent));
   }

   private void callAndRemoveBlockList(List<Block> blocks, Event bukkitEvent) {
      blocks.stream().filter(this.customDataPredicate).forEach(block -> this.callAndRemove(block, bukkitEvent));
   }

   private void callAndRemove(Block block, Event bukkitEvent) {
      if (this.callEvent(block, bukkitEvent)) {
         this.getCbd(block).clear();
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onBreak(BlockBreakEvent event) {
      this.callAndRemove(event);
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onPlace(BlockPlaceEvent event) {
      if (!CustomBlockData.isDirty(event.getBlock())) {
         this.callAndRemove(event);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onEntity(EntityChangeBlockEvent event) {
      if (event.getTo() != event.getBlock().getType()) {
         this.callAndRemove(event.getBlock(), event);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onExplode(BlockExplodeEvent event) {
      this.callAndRemoveBlockList(event.blockList(), event);
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onExplode(EntityExplodeEvent event) {
      this.callAndRemoveBlockList(event.blockList(), event);
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onBurn(BlockBurnEvent event) {
      this.callAndRemove(event);
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onPiston(BlockPistonExtendEvent event) {
      this.onPiston(event.getBlocks(), event);
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onPiston(BlockPistonRetractEvent event) {
      this.onPiston(event.getBlocks(), event);
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onFade(BlockFadeEvent event) {
      if (event.getBlock().getType() != Material.FIRE) {
         if (event.getNewState().getType() != event.getBlock().getType()) {
            this.callAndRemove(event);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onStructure(StructureGrowEvent event) {
      this.callAndRemoveBlockStateList(event.getBlocks(), event);
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onFertilize(BlockFertilizeEvent event) {
      this.callAndRemoveBlockStateList(event.getBlocks(), event);
   }

   private void onPiston(List<Block> blocks, BlockPistonEvent bukkitEvent) {
      Map<Block, CustomBlockData> map = new LinkedHashMap<>();
      BlockFace direction = bukkitEvent.getDirection();
      blocks.stream().filter(this.customDataPredicate).forEach(block -> {
         CustomBlockData cbd = new CustomBlockData(block, this.plugin);
         if (!cbd.isEmpty() && !cbd.isProtected()) {
            PistonMoveReaction reaction = block.getPistonMoveReaction();
            if (reaction == PistonMoveReaction.BREAK) {
               this.callAndRemove(block, bukkitEvent);
            } else {
               Block destinationBlock = block.getRelative(direction);
               CustomBlockDataMoveEvent moveEvent = new CustomBlockDataMoveEvent(this.plugin, block, destinationBlock, bukkitEvent);
               Bukkit.getPluginManager().callEvent(moveEvent);
               if (!moveEvent.isCancelled()) {
                  map.put(destinationBlock, cbd);
               }
            }
         }
      });
      BlockDataListener.Utils.reverse(map).forEach((block, cbd) -> {
         cbd.copyTo(block, this.plugin);
         cbd.clear();
      });
   }

   private static final class Utils {
      private static <K, V> Map<K, V> reverse(Map<K, V> map) {
         LinkedHashMap<K, V> reversed = new LinkedHashMap<>();
         List<K> keys = new ArrayList<>(map.keySet());
         Collections.reverse(keys);
         keys.forEach(key -> reversed.put((K)key, map.get(key)));
         return reversed;
      }
   }
}
