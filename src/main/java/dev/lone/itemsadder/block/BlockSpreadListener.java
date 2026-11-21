package dev.lone.itemsadder.block;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import dev.lone.itemsadder.world.InstrumentRegistry;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;

public final class BlockSpreadListener implements IaListener {
   Random ahC = new Random();

   public void register(Plugin plugin) {
      EventRegistrar.registerIfAbsent(this, plugin);
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onBlockPhysics(BlockPhysicsEvent var1) {
      if (var1.getBlock().getType() == Material.FIRE) {
         var1.setCancelled(true);
         if (!ContentManager.method8084().method8090()) {
            return;
         }

         Block var2 = var1.getBlock();
         Fire var3 = (Fire)var1.getBlock().getBlockData();
         CustomBlockChunkStoreBase var4 = ContentManager.method8084().method8183(var2);
         Material var5 = var2.getRelative(BlockFace.DOWN).getType();
         if (var5 != Material.NETHERRACK && var5 != Material.MAGMA_BLOCK && var5 != Material.LAVA) {
            int var6;
            if (var4.hasBlockEntryAt(var2)) {
               Integer var7 = var4.getStoredInt(var2);
               var6 = var7 == null ? 1 : var7;
               var6 = Math.min(15, var6 + this.ahC.nextInt(3) / 2);
               if (var7 == null || var7 != var6) {
                  var4.setStoredInt(var2, var6);
               }
            } else {
               var6 = var3.getAge();
               var4.setStoredInt(var2, var3.getAge());
            }

            if (var6 >= 15 && this.ahC.nextInt(4) == 0 && !var5.isFlammable()) {
               var4.removeCustomBlockAndRefresh(var2, true);
               return;
            }

            boolean var13 = false;

            for (BlockFace var11 : LocationScanner.NEIGHBOR_AND_VERTICAL_FACES) {
               if (var2.getRelative(var11).getType().isFlammable()) {
                  var13 = true;
                  break;
               }
            }

            if (!var13 && (var6 > 3 || !var2.getRelative(BlockFace.DOWN).getType().isSolid())) {
               var4.removeCustomBlockAndRefreshNms(var2, true, true);
               return;
            }
         }

         if (var4.isCustomBlock(var2)) {
            if (!(ContentManager.method8084().method8154(var4.getCustomBlockId(var2)) instanceof AgeableBlockDefinition var14)) {
               return;
            }

            if (var3.getAge() != var14.apn.ticks) {
               var3.setAge(var14.apn.ticks);
               var2.setBlockData(var3);
            }
         } else if (var3.getAge() > 0) {
            var3.setAge(0);
            var2.setBlockData(var3);
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onBlockSpread(BlockSpreadEvent var1) {
      if (var1.getNewState().getType() == Material.FIRE) {
         Block var2 = var1.getSource();
         Block var3 = var1.getBlock();
         CustomBlockChunkStoreBase var4 = ContentManager.method8084().method8183(var2);
         if (var4.isCustomBlock(var2)) {
            if (!(ContentManager.method8084().method8154(var4.getCustomBlockId(var2)) instanceof AgeableBlockDefinition var5)) {
               return;
            }

            var1.setCancelled(true);
            Fire var8 = (Fire)var1.getNewState().getBlockData();
            CustomBlockChunkStoreBase var7 = ContentManager.method8084().method8183(var3);
            var7.setStoredInt(var3, var8.getAge());
            var8.setAge(var5.apn.ticks);
            var7.setCustomBlock(var3, var5.getNamespacedID());
            var3.setBlockData(var8);
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onBlockBurn(BlockBurnEvent var1) {
      Block var2 = var1.getIgnitingBlock();
      if (var2 != null) {
         Block var3 = var1.getBlock();
         CustomBlockChunkStoreBase var4 = ContentManager.method8084().method8183(var2);
         if (var4.isCustomBlock(var2)) {
            if (!(ContentManager.method8084().method8154(var4.getCustomBlockId(var2)) instanceof AgeableBlockDefinition var5)) {
               return;
            }

            var1.setCancelled(true);
            Fire var10 = (Fire)var2.getBlockData();
            int var7 = var10.getAge();
            if (this.ahC.nextInt(var7 + 10) < 5 && !InstrumentRegistry.aQ(var3)) {
               var3.setType(Material.FIRE);
               if (var3.getBlockData() instanceof Fire) {
                  Fire var8 = (Fire)var3.getBlockData();
                  CustomBlockChunkStoreBase var9 = ContentManager.method8084().method8183(var3);
                  var9.setStoredInt(var3, 0);
                  var8.setAge(var5.apn.ticks);
                  var9.setCustomBlock(var3, var5.getNamespacedID());
                  var3.setBlockData(var8);
               }
            } else if (var3.getType() != Material.TNT) {
               var3.setType(Material.AIR);
            } else {
               if (!BlockFaceUtils.simulateTntPrime(var3)) {
                  return;
               }

               var3.setType(Material.AIR);
               var3.getWorld().spawn(var3.getLocation(), TNTPrimed.class);
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onBlockFade(BlockFadeEvent var1) {
      if (var1.getBlock().getType() == Material.FIRE) {
         ContentManager.method8084().method8183(var1.getBlock()).removeCustomBlock(var1.getBlock());
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onPlayerInteract(PlayerInteractEvent var1) {
      if (var1.getAction() == Action.LEFT_CLICK_BLOCK) {
         if (var1.getHand() != EquipmentSlot.OFF_HAND) {
            if (var1.getClickedBlock() != null && var1.getClickedBlock().getType() == Material.FIRE) {
               if (BlockFaceUtils.simulateBreak(var1.getClickedBlock(), var1.getPlayer())) {
                  ContentManager.method8084().method8183(var1.getClickedBlock()).removeCustomBlock(var1.getClickedBlock());
               }
            }
         }
      }
   }
}
