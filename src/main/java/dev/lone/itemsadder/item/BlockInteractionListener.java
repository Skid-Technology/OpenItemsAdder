package dev.lone.itemsadder.item;

import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EnvironmentDetector;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BlockInteractionListener implements IaListener {
   final Plugin afq;
   final ContentManager afr;

   public BlockInteractionListener(Plugin var1, ContentManager var2) {
      this.afq = var1;
      this.afr = var2;
      EventRegistrar.registerIfAbsent(this, var1);
      if (EnvironmentDetector.paperAdventure) {
         try {
            EventRegistrar.registerIfAbsent(new BlockDestroyListener(), var1);
         } catch (Throwable var4) {
            Log.warn("Failed to register Paper event listener for tripwires.", var4);
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void method536(BlockPlaceEvent var1) {
      if (!EventRegistrar.isCustomBlockPlaceEvent(var1)) {
         if (this.afr.method8167(var1.getItemInHand())) {
            PlantItemDefinition var2 = (PlantItemDefinition)this.afr.method8149(var1.getItemInHand());
            var2.aC(var1.getBlockPlaced());
            CustomBlockChunkStoreBase var3 = this.afr.method8183(var1.getBlockPlaced());
            var3.setCropSeedId(var1.getBlockPlaced(), var2.getNamespacedID());
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void method546(BlockBreakEvent var1) {
      if (!EventRegistrar.isCustomBlockBreakEvent(var1)) {
         Block var2 = var1.getBlock();
         if (!this.method558(var2, var1)) {
            this.method558(var2.getRelative(BlockFace.UP), var1);
         }
      }
   }

   private boolean method558(Block var1, BlockBreakEvent var2) {
      if (PlantItemDefinition.aD(var1)) {
         CustomBlockChunkStoreBase var3 = this.afr.method8183(var1);
         String var4 = var3.getCropSeedId(var1);
         if (var4 == null) {
            return false;
         }

         var2.setDropItems(false);
         PlantItemDefinition var5 = (PlantItemDefinition)this.afr.method8153(PlantItemDefinition.class, var4);
         if (var5 != null) {
            if (var2.getPlayer().getGameMode() != GameMode.CREATIVE) {
               if (BlockUtils.isFullyGrown(var1)) {
                  var5.method8839(var2.getPlayer().getItemInHand(), var1.getLocation());
               } else {
                  ItemStack var6 = var5.Ds();
                  LocationScanner.dropItem(var1.getLocation(), var6.clone());
               }
            }
         } else {
            Log.warn(var2.getPlayer().getName() + " mined unknown crop " + var4 + " " + LocationScanner.formatCoords(var1.getLocation()));
         }

         var3.removeCustomBlock(var1);
         return true;
      } else {
         return false;
      }
   }

   @EventHandler(ignoreCancelled = true)
   private void method537(BlockFromToEvent var1) {
      if (var1.getBlock().getType() == Material.WATER) {
         Location var2 = var1.getToBlock().getLocation();
         Block var3 = var1.getToBlock();
         if (PlantItemDefinition.aD(var3)) {
            CustomBlockChunkStoreBase var4 = this.afr.method8183(var3);
            String var5 = var4.getCropSeedId(var3);
            if (var5 == null) {
               return;
            }

            var1.setCancelled(true);
            PlantItemDefinition var6 = (PlantItemDefinition)this.afr.method8153(PlantItemDefinition.class, var5);
            if (var6 != null) {
               if (BlockUtils.isFullyGrown(var3)) {
                  var6.method8840(var3.getLocation());
               } else {
                  ItemStack var7 = var6.Ds();
                  var7.setAmount(1);
                  LocationScanner.dropItem(var2, var7);
               }
            } else {
               Log.warn("Someone mined unknown crop " + var5 + " " + LocationScanner.formatCoords(var3.getLocation()));
            }

            var1.getToBlock().setType(Material.WATER);
            Levelled var8 = (Levelled)var1.getToBlock().getBlockData();
            var8.setLevel(1);
            var1.getToBlock().setBlockData(var8);
            var4.removeCustomBlock(var3);
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   private void method539(PlayerInteractEvent var1) {
      if (var1.useInteractedBlock() != Result.DENY) {
         Block var2 = var1.getClickedBlock();
         if (var1.getAction() == Action.PHYSICAL && var2 != null && var2.getType() == Material.FARMLAND) {
            Block var8 = var2.getRelative(BlockFace.UP);
            if (PlantItemDefinition.aD(var8)) {
               CustomBlockChunkStoreBase var9 = this.afr.method8183(var8);
               String var10 = var9.getCropSeedId(var8);
               if (var10 == null) {
                  return;
               }

               var1.setCancelled(true);
               PlantItemDefinition var6 = (PlantItemDefinition)this.afr.method8154(var10);
               if (var6 != null) {
                  if (!BlockUtils.isFullyGrown(var8)) {
                     ItemStack var7 = var6.Ds();
                     var7.setAmount(1);
                     LocationScanner.dropItem(var8.getLocation(), var7);
                  } else {
                     var6.method8840(var8.getLocation());
                  }
               } else {
                  Log.warn("Someone mined unknown crop '" + var10 + "' " + LocationScanner.formatCoords(var8.getLocation()));
               }

               var9.removeCustomBlockAndRefreshNms(var8, true, true);
               var2.setType(Material.DIRT);
            }
         } else {
            if (var1.getAction() == Action.RIGHT_CLICK_BLOCK) {
               if (var2 == null) {
                  return;
               }

               if (var2.getType() == Material.SWEET_BERRY_BUSH) {
                  if (!BlockUtils.isFullyGrown(var2)) {
                     if (var1.getItem() != null && var1.getItem().getType() == Material.BONE_MEAL) {
                        return;
                     }

                     if (ContentManager.method8188(var2)) {
                        var1.setCancelled(true);
                     }

                     return;
                  }

                  CustomBlockChunkStoreBase var3 = this.afr.method8183(var2);
                  String var4 = var3.getCropSeedId(var2);
                  if (var4 == null) {
                     return;
                  }

                  var1.setCancelled(true);
                  BlockUtils.setAge(var2, 1);
                  PlantItemDefinition var5 = (PlantItemDefinition)this.afr.method8154(var4);
                  if (var5 != null) {
                     var5.method8840(var2.getLocation());
                  } else {
                     Log.warn(var1.getPlayer().getName() + " mined unknown crop " + var4 + " " + LocationScanner.formatCoords(var2.getLocation()));
                  }
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   private void method559(EntityDamageByBlockEvent var1) {
      if (var1.getCause() == DamageCause.CONTACT) {
         if (var1.getDamager() != null) {
            if (this.afr.method8183(var1.getDamager()).getCropSeedId(var1.getDamager()) != null) {
               var1.setCancelled(true);
               var1.setDamage(0.0);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method560(BlockPistonExtendEvent var1) {
      for (Block var3 : var1.getBlocks()) {
         if (PlantItemDefinition.aD(var3)) {
            CustomBlockChunkStoreBase var4 = this.afr.method8183(var3);
            String var5 = var4.getCropSeedId(var3);
            if (var5 == null) {
               return;
            }

            PlantItemDefinition var6 = (PlantItemDefinition)this.afr.method8154(var5);
            if (var6 != null) {
               if (!BlockUtils.isFullyGrown(var3)) {
                  ItemStack var7 = var6.Ds();
                  var7.setAmount(1);
                  LocationScanner.dropItem(var3.getLocation(), var7);
               } else {
                  var6.method8840(var3.getLocation());
               }
            } else {
               Log.warn("A piston has broken an unknown crop '" + var5 + "' " + LocationScanner.formatCoords(var3.getLocation()));
            }

            var4.removeCustomBlockAndRefreshNms(var3, true, true);
         }
      }
   }
}
