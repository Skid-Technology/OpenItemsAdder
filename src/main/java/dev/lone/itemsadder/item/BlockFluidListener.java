package dev.lone.itemsadder.item;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.nms.NmsWorldService;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import dev.lone.itemsadder.world.InstrumentRegistry;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.FluidLevelChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BlockFluidListener implements IaListener {
   private final ContentManager afd;

   public BlockFluidListener(Plugin var1, ContentManager var2) {
      this.afd = var2;
      EventRegistrar.registerIfAbsent(this, var1);
      var2.aK.method933(new ItemHandlerBinding(PlayerInteractEvent.class, this::method541));
      var2.aK.method933(new ItemHandlerBinding(PlayerItemConsumeEvent.class, this::method540));
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method533(BlockPistonExtendEvent var1) {
      for (Block var3 : var1.getBlocks()) {
         if (ItemMaterialAction.method380(var3)) {
            CustomBlockChunkStoreBase var4 = this.afd.method8182(var3.getChunk());
            if (var4.hasBlockEntryAt(var3)) {
               var1.setCancelled(true);
               return;
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method534(EntityChangeBlockEvent var1) {
      if (var1.getEntityType() == EntityType.FALLING_BLOCK) {
         if (var1.getTo().isSolid()) {
            this.method542(var1.getBlock());
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method535(PlayerBucketFillEvent var1) {
      Block var2 = var1.getBlock();
      String var3 = this.method542(var2);
      if (var3 != null) {
         String var4 = (String)ContentManager.method8084().by.get(var3);
         if (var4 != null) {
            var1.setItemStack(ContentManager.method8084().method8179(var4));
            this.method542(var2);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method536(BlockPlaceEvent var1) {
      if (!EventRegistrar.isCustomBlockPlaceEvent(var1)) {
         this.method542(var1.getBlockReplacedState().getBlock());
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method537(BlockFromToEvent var1) {
      Block var2 = var1.getBlock();
      Block var3 = var1.getToBlock();
      if (var2.getType() == Material.WATER && InstrumentRegistry.aP(var3)) {
         CustomBlockChunkStoreBase var4 = this.afd.method8182(var2.getChunk());
         String var5 = var4.getCustomBlockIdIfFlagged(var2);
         if (var5 != null) {
            var1.setCancelled(true);
            if (var2.getBlockData() instanceof Levelled) {
               Levelled var6 = (Levelled)var2.getBlockData();
               if (var6.getLevel() == 0) {
                  Levelled var7 = (Levelled)Material.WATER.createBlockData();
                  var7.setLevel(1);
                  var3.setBlockData(var7, false);
                  String var8 = var4.getSecondaryBlockId(var2);
                  String[] var9 = var5.split(":");
                  var4.setCustomBlockIdAndFlag(var3, var5);
                  var4.setSecondaryBlockId(var3, var8);
                  NmsWorldService.Om().method3194(var3, var9[0], var9[1]);
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method538(FluidLevelChangeEvent var1) {
      Block var2 = var1.getBlock();
      if (var2.getType() == Material.WATER) {
         CustomBlockChunkStoreBase var3 = this.afd.method8182(var2.getChunk());
         String var4 = var3.getCustomBlockIdIfFlagged(var2);
         if (var4 != null) {
            if (var2.getBlockData() instanceof Levelled && var1.getNewData() instanceof Levelled) {
               var1.setCancelled(true);
            } else if (var1.getNewData().getMaterial().isAir()) {
               this.method542(var2);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.LOWEST)
   private void method539(PlayerInteractEvent var1) {
      if (var1.getItem() != null && (var1.getItem().getType() == Material.GLASS_BOTTLE || var1.getItem().getType() == Material.POTION)) {
         if (ContentManager.method8084().isCustomItem(var1.getItem())) {
            return;
         }

         if (var1.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block var2 = var1.getClickedBlock().getRelative(var1.getBlockFace());
            if (var2.getType() == Material.WATER) {
               CustomBlockChunkStoreBase var3 = this.afd.method8183(var2);
               if (var3.getCustomBlockIdIfFlagged(var2) != null) {
                  var1.setCancelled(true);
               }
            }
         }
      }
   }

   @ItemEventHandler
   private void method540(PlayerItemConsumeEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var2.aoh.hasAction("liquid_bucket")) {
         var1.setCancelled(true);
      }
   }

   @ItemEventHandler
   private void method541(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var1.useInteractedBlock() != Result.DENY) {
         if (!ItemStackUtils.method7250(var1)) {
            if (var1.getAction() == Action.RIGHT_CLICK_BLOCK && var1.getClickedBlock() != null) {
               Block var4 = var1.getClickedBlock().getRelative(var1.getBlockFace());
               if (InstrumentRegistry.aP(var4)) {
                  if (var2.aoh.hasAction("liquid_bucket")) {
                     if (BlockFaceUtils.simulatePlace(var4, var1.getPlayer())) {
                        var1.setCancelled(true);
                        ItemMaterialAction var5 = (ItemMaterialAction)var2.aoh.getAction("liquid_bucket");
                        if (var5.AO().getNamespacedID().equals(NmsWorldService.Om().bj(var4)) && var4.getBlockData() instanceof Levelled) {
                           Levelled var6 = (Levelled)var4.getBlockData();
                           if (var6.getLevel() == 0) {
                              NmsWorldService.Om().method3194(var4, var5.AO().getNamespace(), var5.AO().getId());
                              return;
                           }
                        }

                        if (NmsWorldService.Om().method3193(var5.AO().getNamespace(), var5.AO().getId()) == null) {
                           Log.sendLocalizedWith(
                              var1.getPlayer(), "liquid-not-completely-registered", var1x -> var1x.replace("{liquid}", var5.AO().getNamespacedID())
                           );
                           Log.error("Fluid " + var5.AO().getNamespacedID() + " is not completely registered. Please restart the server.");
                        } else {
                           CustomBlockChunkStoreBase var12 = this.afd.method8183(var4);
                           if (var12.hasChunkRecordOf(var4.getChunk())) {
                              for (String var8 : var12.getFlaggedBlockIds(var4.getChunk())) {
                                 if (!var5.AO().getNamespacedID().equals(var8)) {
                                    Log.sendPrefixed(var1.getPlayer(), Main.aFi.lj("liquid-bucket-place-near-error"));
                                    return;
                                 }
                              }
                           }

                           for (Chunk var14 : LocationScanner.getChunksAroundLocation(var4.getLocation(), 1)) {
                              CustomBlockChunkStoreBase var9 = this.afd.method8182(var14);

                              for (String var11 : var9.getFlaggedBlockIds(var14)) {
                                 if (!var5.AO().getNamespacedID().equals(var11)) {
                                    Log.sendPrefixed(var1.getPlayer(), Main.aFi.lj("liquid-bucket-place-near-error"));
                                    return;
                                 }
                              }
                           }

                           var12.setCustomBlockIdAndFlag(var4, var5.AO().getNamespacedID());
                           var12.setSecondaryBlockId(var4, NmsWorldService.method3192(var4, Biome.PLAINS).getKey().toString());
                           if (!var5.AO().getNamespacedID().equals(NmsWorldService.Om().bj(var4))) {
                              NmsWorldService.Om().method3194(var4, var5.AO().getNamespace(), var5.AO().getId());
                           }

                           SchedulerUtils.runLaterSync(() -> var4.setType(Material.WATER), 2L);
                           if (var1.getPlayer().getGameMode() != GameMode.CREATIVE) {
                              ItemStackUtils.au(var1.getItem());
                              var1.getPlayer().getInventory().addItem(new ItemStack[]{new ItemStack(Material.BUCKET)});
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private String method542(Block var1) {
      CustomBlockChunkStoreBase var2 = this.afd.method8182(var1.getChunk());
      if (!var2.hasBlockEntryAt(var1)) {
         return null;
      }

      String var3 = var2.getSecondaryBlockId(var1);
      if (var3 == null) {
         return null;
      }

      String var4 = var2.getCustomBlockIdIfFlagged(var1);
      String[] var5 = var3.split(":");
      String[] var6 = var4.split(":");
      NmsWorldService.Om().method3195(var1.getLocation(), var5[0], var5[1], false);
      var2.removeCustomBlock(var1);
      this.method543(var1, var6);
      return var4;
   }

   private void method543(Block var1, String[] var2) {
      for (BlockFace var6 : LocationScanner.CARDINAL_FACES) {
         Block var7 = var1.getRelative(var6);
         if (var7.getType() == Material.WATER) {
            String var8 = this.afd.method8182(var7.getChunk()).getCustomBlockIdIfFlagged(var7);
            if (var8 != null) {
               NmsWorldService.Om().method3194(var7, var2[0], var2[1]);
               break;
            }
         }
      }
   }
}
