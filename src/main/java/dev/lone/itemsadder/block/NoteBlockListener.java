package dev.lone.itemsadder.block;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.item.ItemUseHandler;
import dev.lone.itemsadder.item.PlacementResult;
import dev.lone.itemsadder.nms.NmsBlockSetterService;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.Utils;
import dev.lone.itemsadder.world.BlockEffectsService;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import dev.lone.itemsadder.world.InstrumentRegistry;
import dev.lone.itemsadder.world.NoteBlockData;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.block.data.type.Observer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

public final class NoteBlockListener implements IaListener {
   public void register(Plugin plugin) {
      EventRegistrar.registerIfAbsent(this, plugin);
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onNoteBlockPhysics(BlockPhysicsEvent var1) {
      if (var1.getBlock().getType() == Material.NOTE_BLOCK) {
         if (!method1088(var1.getBlock())) {
            CustomBlockChunkStoreBase var2 = ContentManager.method8084().method8183(var1.getBlock());
            if (var2.isCustomBlock(var1.getBlock())) {
               var1.setCancelled(true);
               CustomBlockDefinition var3 = ContentManager.method8084().method8160(var2.getCustomBlockId(var1.getBlock()));
               if (var3 != null) {
                  SchedulerUtils.runOnMainThread(() -> var3.placeBlockDefault(var1.getBlock()));
                  method1081(var1.getBlock());
               } else {
                  NoteBlock var4 = (NoteBlock)var1.getBlock().getBlockData();
                  byte var5 = var4.getNote().getId();
                  if (var5 == 0) {
                     var5 = 24;
                  } else {
                     var5--;
                  }

                  var4.setNote(new Note(var5));
                  var1.getBlock().setBlockData(var4, false);
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onNotePlay(NotePlayEvent var1) {
      if (!method1088(var1.getBlock())) {
         if (ContentManager.isCustomBlock(var1.getBlock())) {
            var1.setCancelled(true);
         } else {
            var1.setCancelled(true);
            boolean var2 = false;
            CustomBlockChunkStoreBase var4 = ContentManager.method8084().method8183(var1.getBlock());
            if (var4.hasBlockEntryAt(var1.getBlock())) {
               byte var3 = var4.getStoredByteOrDefault(var1.getBlock(), (byte)0);
               if (var3 != -1) {
                  InstrumentRegistry.getAdapter().method2799(var1.getBlock(), var3);
                  var1.setInstrument(InstrumentRegistry.instrumentOf(var3));
                  var2 = true;
               }
            }

            if (!var2) {
               NoteBlockData var5 = InstrumentRegistry.getAdapter().aY(var1.getBlock());
               if (var5 != null) {
                  byte var6 = var5.instrument.getType();
                  var1.setInstrument(InstrumentRegistry.instrumentOf(var6));
               }
            }

            this.method1085(var1.getBlock());
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onNoteInteract(PlayerInteractEvent var1) {
      Block var2 = var1.getClickedBlock();
      if (var2 != null && var2.getType() == Material.NOTE_BLOCK) {
         if (var1.useInteractedBlock() != Result.DENY && var1.useItemInHand() != Result.DENY) {
            boolean var3 = ContentManager.method8084().method8182(var2.getChunk()).isCustomBlock(var2);
            if (var1.getAction() == Action.LEFT_CLICK_BLOCK) {
               method1081(var2);
               if (!var3 && ItemStackUtils.ad(var2.getRelative(BlockFace.UP).getType())) {
                  InstrumentRegistry.getAdapter().aY(var2);
               }
            } else {
               if (var1.getAction() == Action.RIGHT_CLICK_BLOCK) {
                  if (var1.getHand() == EquipmentSlot.OFF_HAND) {
                     return;
                  }

                  if (method1088(var2)) {
                     return;
                  }

                  if (!var1.getPlayer().isSneaking() && !var3) {
                     this.method1079(var1);
                  } else {
                     if (EventRegistrar.isReentrantCall()) {
                        return;
                     }

                     if (!this.method1078(var2, var1.getPlayer().getInventory().getItemInMainHand(), var1, EquipmentSlot.HAND)) {
                        this.method1078(var2, var1.getPlayer().getInventory().getItemInOffHand(), var1, EquipmentSlot.OFF_HAND);
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onNoteBreak(BlockBreakEvent var1) {
      if (!EventRegistrar.isCustomBlockBreakEvent(var1)) {
         Block var2 = var1.getBlock();
         if (var2.getType() == Material.NOTE_BLOCK) {
            if (var1.getPlayer().getGameMode() != GameMode.SURVIVAL || !ContentManager.isCustomBlock(var2)) {
               ContentManager.method8084().method8183(var2).removeCustomBlock(var2);
            }
         } else {
            Block var3 = var2.getRelative(BlockFace.UP);
            if (var3.getType() != Material.NOTE_BLOCK) {
               var3 = var2.getRelative(BlockFace.DOWN);
            }

            if (var3.getType() == Material.NOTE_BLOCK) {
               if (ContentManager.isCustomBlock(var3) && !ContentManager.method8189(var3.getLocation())) {
                  Block var5 = var3;
                  SchedulerUtils.runLaterSync(() -> {
                     if (var5.getType() == Material.NOTE_BLOCK && ContentManager.isCustomBlock(var5)) {
                        this.method1086(var5);
                     }
                  }, 1L);
                  ContentManager.method8084().bm.method1161(var1.getBlock().getLocation());
               } else {
                  Block var4 = var3;
                  SchedulerUtils.runLaterSync(() -> {
                     if (var4.getType() == Material.NOTE_BLOCK) {
                        this.method1083(var4);
                     }
                  }, 1L);
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onNotePlace(BlockPlaceEvent var1) {
      if (!EventRegistrar.isCustomBlockPlaceEvent(var1)) {
         Block var2 = var1.getBlockPlaced();
         Block var3 = var2.getRelative(BlockFace.UP);
         if (var3.getType() == Material.NOTE_BLOCK) {
            ContentManager.method8084().bm.method1161(var2.getLocation());
         }

         if (!ItemUseHandler.aX(var1.getPlayer())) {
            boolean var4 = var2.getType() == Material.NOTE_BLOCK;
            if (var4) {
               if (ContentManager.isCustomBlock(var2)) {
                  SchedulerUtils.runLaterSync(() -> this.method1086(var2), 1L);
                  return;
               }

               if (!method1087(var2, var1.getItemInHand(), (NoteBlock)var2.getBlockData())) {
                  method1089(var2);
                  return;
               }

               SchedulerUtils.runLaterSync(() -> this.method1083(var2), 1L);
            }

            if (var3.getType() == Material.NOTE_BLOCK) {
               if (ContentManager.isCustomBlock(var3)) {
                  SchedulerUtils.runLaterSync(() -> this.method1086(var3), 1L);
               } else {
                  SchedulerUtils.runLaterSync(() -> this.method1083(var3), 1L);
               }
            }

            Block var5 = var2.getRelative(BlockFace.DOWN);
            if (var5.getType() == Material.NOTE_BLOCK) {
               if (ContentManager.isCustomBlock(var5)) {
                  SchedulerUtils.runLaterSync(() -> this.method1086(var5), 1L);
               } else {
                  SchedulerUtils.runLaterSync(() -> this.method1083(var5), 1L);
               }
            }
         }
      }
   }

   private boolean method1078(Block var1, ItemStack var2, PlayerInteractEvent var3, EquipmentSlot var4) {
      if (var2 != null && var2.getType() != Material.AIR) {
         Block var5 = var1.getRelative(var3.getBlockFace());
         if (!BlockFaceUtils.playerOverlapsBlock(var3.getPlayer(), var5) && var3.useItemInHand() != Result.DENY) {
            if (Main.aEY && var2.hasItemMeta()) {
               PersistentDataContainer var6 = var2.getItemMeta().getPersistentDataContainer();
               if (var6.has(new NamespacedKey("slimefun", "slimefun_item"))) {
                  return false;
               }
            }

            if (!ContentManager.method8084().method8156(var2)) {
               return PlayerInteractionFacade.getHandler()
                  .placeBlock(var3.getPlayer(), var4, var3.getClickedBlock(), Utils.clickedLocation(var3), var3.getBlockFace());
            }

            var3.setCancelled(true);
            if (InstrumentRegistry.aP(var5)) {
               CustomBlockDefinition var11 = ContentManager.method8084().method8159(var2);
               Block var7 = var11 != null && var11.anF != null && var11.anF.arb ? var5.getRelative(BlockFace.UP) : var5;
               BlockState var8 = var5.getState();
               BlockState var9 = var7 == var5 ? null : var7.getState();
               if (ContentManager.method8084().aX.method1442(var3.getPlayer(), var2, var5, var1, var3.getBlockFace(), false) == PlacementResult.PLACED) {
                  ItemStack var10 = var2.clone();
                  if (!BlockFaceUtils.simulatePlaceWithSlot(var7, var3.getPlayer(), var1, var10, var4)) {
                     CustomBlock.remove(var7.getLocation());
                     if (var9 != null) {
                        var9.update(true, false);
                     }

                     var8.update(true, false);
                     return true;
                  }

                  if (var3.getPlayer().getGameMode() != GameMode.CREATIVE) {
                     var2.setAmount(var2.getAmount() - 1);
                  }

                  if (var3.getPlayer().isSneaking() && var1.getType() == Material.NOTE_BLOCK) {
                     PlayerInteractionFacade.method5310(var3.getPlayer(), var4);
                  }

                  return true;
               }
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void method1079(PlayerInteractEvent var1) {
      if (var1.getHand() != EquipmentSlot.OFF_HAND) {
         Block var2 = var1.getClickedBlock();
         if (var2.getType() == Material.NOTE_BLOCK) {
            var1.setCancelled(true);
            this.method1082(var2, var1.getAction(), true);
            method1081(var2);
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method1080(BlockPlaceEvent var1) {
      if (!EventRegistrar.isCustomBlockPlaceEvent(var1)) {
         if (!ItemUseHandler.aX(var1.getPlayer())) {
            Block var2 = var1.getBlock();
            if (var2.getType() == Material.NOTE_BLOCK) {
               CustomBlockChunkStoreBase var3 = ContentManager.method8084().method8183(var2);
               if (var3.isCustomBlock(var2)) {
                  var1.setCancelled(true);
               }
            }
         }
      }
   }

   public static void method1081(Block var0) {
      for (BlockFace var4 : LocationScanner.NEIGHBOR_AND_VERTICAL_FACES) {
         Block var5 = var0.getRelative(var4);
         if (var5.getType() == Material.OBSERVER) {
            Observer var6 = (Observer)var5.getBlockData();
            if (var6.getFacing().getOppositeFace().equals(var4)) {
               BlockEffectsService.getAdapter().setObserverPowered(var5, true);
               BlockEffectsService.getAdapter().notifyBlockUpdate(var5);
            }
         }
      }
   }

   private void method1082(Block var1, Action var2, boolean var3) {
      NoteBlock var4 = (NoteBlock)var1.getBlockData();
      int var5 = var4.getNote().getId();
      if (var2 == Action.RIGHT_CLICK_BLOCK) {
         var5++;
      }

      if (var5 > 24) {
         var5 = 0;
      }

      var4.setInstrument(Instrument.PIANO);
      var4.setNote(new Note(var5));
      NmsBlockSetterService.OD().method3403(var1, var4, true);
      CustomBlockChunkStoreBase var6 = ContentManager.method8084().method8183(var1);
      if (!var6.hasBlockEntryAt(var1)) {
         Instrument var7 = InstrumentRegistry.getAdapter().aZ(var1);
         if (var7 != null) {
            var6.setStoredByte(var1, var7.getType());
         }
      }

      if (var3) {
         EventRegistrar.callEvent(new NotePlayEvent(var1, var4.getInstrument(), var4.getNote()));
      }
   }

   private void method1083(Block var1) {
      Instrument var2 = InstrumentRegistry.getAdapter().aZ(var1);
      this.method1084(var1, var2.getType());
   }

   private void method1084(Block var1, byte var2) {
      CustomBlockChunkStoreBase var3 = ContentManager.method8084().method8183(var1);
      var3.setStoredByte(var1, var2);
      this.method1085(var1);
   }

   private void method1085(Block var1) {
      NoteBlock var2 = (NoteBlock)var1.getBlockData();
      var2.setInstrument(Instrument.PIANO);
      var1.setBlockData(var2, false);
   }

   private void method1086(Block var1) {
      CustomBlockChunkStoreBase var2 = ContentManager.method8084().method8183(var1);
      CustomBlockDefinition var3 = ContentManager.method8084().method8160(var2.getCustomBlockId(var1));
      if (var3 != null && var3.anF != null) {
         var3.anF.aE(var1);
      }
   }

   private static boolean method1087(Block var0, ItemStack var1, NoteBlock var2) {
      return var1.getType() == Material.NOTE_BLOCK
         ? true
         : var2.getInstrument() == CustomBlockVariant.aG(var0.getRelative(BlockFace.DOWN)) && var2.getNote().getId() == 0;
   }

   private static boolean method1088(Block var0) {
      CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8183(var0);
      return var1.isStoredFlagSet(var0);
   }

   private static void method1089(Block var0) {
      CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8183(var0);
      var1.setStoredFlag(var0, true);
   }
}
