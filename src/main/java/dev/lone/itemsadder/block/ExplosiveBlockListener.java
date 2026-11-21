package dev.lone.itemsadder.block;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.Events.CustomBlockPlaceEvent;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.SoundUtils;
import java.util.HashMap;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;

public class ExplosiveBlockListener implements IaListener {
   public static final BlockFace[] alY = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};

   public ExplosiveBlockListener() {
      EventRegistrar.registerIfAbsent(this, Main.getInstance());
   }

   static CustomBlockDefinition getPrimeableDefinition(TNTPrimed var0) {
      if (!var0.getPersistentDataContainer().has(ExplosiveBlockSpec.art)) {
         return null;
      }

      String var1 = (String)var0.getPersistentDataContainer().get(ExplosiveBlockSpec.art, PersistentDataType.STRING);
      return ContentManager.method8084().method8160(var1);
   }

   static ExplosiveBlockSpec getPrimeableSpec(TNTPrimed var0) {
      CustomBlockDefinition var1 = getPrimeableDefinition(var0);
      return var1 == null ? null : var1.getExplosiveSpec();
   }

   static ExplosiveBlockSpec getSpecForBlock(Block var0) {
      CustomBlockDefinition var1 = ContentManager.method8187(var0);
      return var1 == null ? null : var1.getExplosiveSpec();
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onInteractIgnite(PlayerInteractEvent var1) {
      Block var2 = var1.getClickedBlock();
      if (var2 != null) {
         ExplosiveBlockSpec var3 = getSpecForBlock(var2);
         if (var3 != null && var3.arz != null) {
            if (!var3.arz.matchesHeldItem(var1.getPlayer())) {
               return;
            }

            SchedulerUtils.runLaterSync(() -> var3.explodeAt(ContentManager.method8187(var2), var2.getLocation(), var1.getPlayer()), 1L);
         }

         HashMap var4 = new HashMap();
         PlayerInventory var5 = var1.getPlayer().getInventory();
         if (var5.getItemInMainHand().getType() == Material.FLINT_AND_STEEL || var5.getItemInOffHand().getType() == Material.FLINT_AND_STEEL) {
            if (var3 != null && var3.hasTrigger(4)) {
               SchedulerUtils.runLaterSync(() -> {
                  SoundUtils.playSound(var2.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                  SoundUtils.playSound(var2.getLocation(), Sound.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                  var3.explodeAt(ContentManager.method8187(var2), var2.getLocation(), var1.getPlayer());
               }, 2L);
            }

            Block var6 = var2.getRelative(var1.getBlockFace());

            for (BlockFace var10 : alY) {
               Block var11 = var6.getRelative(var10);
               ExplosiveBlockSpec var12 = getSpecForBlock(var11);
               if (var12 != null && var12.hasTrigger(4)) {
                  var4.put(var11, var12);
               }
            }

            if (!var4.isEmpty()) {
               SchedulerUtils.runLaterSync(
                  () -> {
                     for (Entry var3x : var4.entrySet()) {
                        ((ExplosiveBlockSpec)var3x.getValue())
                           .explodeAt(ContentManager.method8187((Block)var3x.getKey()), ((Block)var3x.getKey()).getLocation(), var1.getPlayer());
                     }
                  },
                  40L
               );
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onProjectileHit(ProjectileHitEvent var1) {
      Block var2 = var1.getHitBlock();
      if (var2 != null) {
         if (var1.getEntity().getFireTicks() > 0) {
            CustomBlockDefinition var3 = ContentManager.method8187(var2);
            if (var3 != null) {
               ExplosiveBlockSpec var4 = var3.getExplosiveSpec();
               if (var4 != null) {
                  if (var4.hasTrigger(1)) {
                     if (var4.explode(var3, var2.getLocation())) {
                        var1.setCancelled(true);
                        var1.getEntity().teleport(var1.getEntity().getLocation().add(0.0, 0.1, 0.0));
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onExplosionPrime(ExplosionPrimeEvent var1) {
      if (var1.getEntity() instanceof TNTPrimed var2) {
         if (var2.getPersistentDataContainer().has(ExplosiveBlockSpec.art)) {
            ExplosiveBlockSpec var4 = getPrimeableSpec(var2);
            if (var4 == null) {
               return;
            }

            var1.setFire(var4.arD);
            var1.setRadius(var4.arA);
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onRedstone(BlockRedstoneEvent var1) {
      if (ExplosiveBlockSpec.ary) {
         if (var1.getNewCurrent() > 0) {
            for (BlockFace var5 : alY) {
               Block var6 = var1.getBlock().getRelative(var5);
               ExplosiveBlockSpec var7 = getSpecForBlock(var6);
               if (var7 != null && var7.hasTrigger(2)) {
                  var7.explode(ContentManager.method8187(var6), var6.getLocation());
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onBlockPlace(BlockPlaceEvent var1) {
      if (ExplosiveBlockSpec.ary) {
         if (var1.getBlockPlaced().getType() == Material.REDSTONE_BLOCK) {
            for (BlockFace var5 : alY) {
               Block var6 = var1.getBlockPlaced().getRelative(var5);
               ExplosiveBlockSpec var7 = getSpecForBlock(var6);
               if (var7 != null && var7.hasTrigger(2)) {
                  var7.explode(ContentManager.method8187(var6), var6.getLocation());
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onCustomBlockPlace(CustomBlockPlaceEvent var1) {
      if (ExplosiveBlockSpec.ary) {
         CustomBlockDefinition var2 = var1.internal;
         if (var2.getExplosiveSpec() != null) {
            ExplosiveBlockSpec var3 = var2.getExplosiveSpec();
            boolean var4 = var3.hasTrigger(2);
            boolean var5 = var3.hasTrigger(4);
            if (var4 || var5) {
               Block var6 = var1.getBlock();

               for (BlockFace var10 : alY) {
                  Block var11 = var6.getRelative(var10);
                  Material var12 = var11.getType();
                  if (var4 && var12 == Material.REDSTONE_BLOCK || var5 && var12 == Material.FIRE || var11.isBlockPowered()) {
                     SchedulerUtils.runLaterSync(() -> var3.explodeAt(var2, var6.getLocation(), var1.getPlayer()), 40L);
                     return;
                  }
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onPistonExtend(BlockPistonExtendEvent var1) {
      if (ExplosiveBlockSpec.ary) {
         for (Block var3 : var1.getBlocks()) {
            if (var3.getType() != Material.REDSTONE) {
               return;
            }

            for (BlockFace var7 : alY) {
               Block var8 = var3.getRelative(var7);
               ExplosiveBlockSpec var9 = getSpecForBlock(var8);
               if (var9 != null && var9.hasTrigger(2)) {
                  var9.explode(ContentManager.method8187(var8), var8.getLocation());
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onEntityDamage(EntityDamageByEntityEvent var1) {
      if (var1.getCause() == DamageCause.ENTITY_EXPLOSION) {
         if (var1.getDamager() instanceof TNTPrimed var2) {
            ExplosiveBlockSpec var4 = getPrimeableSpec(var2);
            if (var4 != null) {
               if (var4.arB != 1.0) {
                  var1.setDamage(var1.getDamage() * var4.arB);
               }
            }
         }
      }
   }
}
