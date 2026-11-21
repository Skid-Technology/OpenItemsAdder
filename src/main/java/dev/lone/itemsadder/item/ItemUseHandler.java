package dev.lone.itemsadder.item;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.Core.OtherPlugins.ProtectionPlugins.ProtectionPlugins;
import dev.lone.itemsadder.api.CustomBlockActionEvent;
import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import dev.lone.itemsadder.api.Events.CustomBlockPlaceEvent;
import dev.lone.itemsadder.block.BlockRealizationMode;
import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.block.NoteBlockListener;
import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.item.effect.EffectOutcome;
import dev.lone.itemsadder.item.effect.ItemEffect;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.RayTraceTarget;
import dev.lone.itemsadder.util.RayTraceWalker;
import dev.lone.itemsadder.util.TypedList;
import dev.lone.itemsadder.world.BlockEffectsService;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import dev.lone.itemsadder.world.InstrumentRegistry;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemUseHandler extends ItemListenerBase {
   BlockSoundListener ago;
   private static final TypedList agp = TypedList.ofType(Player.class);

   public ItemUseHandler() {
      super(50);
      if (Settings.get().customBlockSounds) {
         this.ago = new BlockSoundListener();
      }

      EventRegistrar.registerIfAbsent(this, Main.getInstance());
      BlockDigListener.BR().register();
      ContentManager.method8084().aK.method933(new ItemHandlerBinding(PlayerInteractEvent.class, this::method1434));
      ContentManager.method8084().aK.method933(new ItemHandlerBinding(PlayerInteractEvent.class, this::method1438));
   }

   public static boolean aX(Player var0) {
      return agp.contains(var0);
   }

   public static void method1433(Player var0, boolean var1) {
      if (var1) {
         agp.add(var0);
      } else {
         agp.remove(var0);
      }
   }

   @Override
   public void unregister() {
      super.unregister();
      BlockDigListener.BR().unregister();
      if (this.ago != null) {
         this.ago.unregister();
      }

      agp.clear();
   }

   private void method1434(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      this.method1435(var1, var2, var3, true, true);
   }

   public boolean method1435(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3, boolean var4, boolean var5) {
      if (var1.getAction() != Action.RIGHT_CLICK_BLOCK) {
         return false;
      }

      if (var1.getPlayer().getGameMode() == GameMode.ADVENTURE || var1.getPlayer().getGameMode() == GameMode.SPECTATOR) {
         return false;
      }

      if (!(var2 instanceof PlacedBlockItemDefinition)) {
         return false;
      }

      if (var3.getType().isBlock()) {
         return false;
      }

      Block var6 = var1.getClickedBlock();
      if (var5 && var6.getType() == Material.NOTE_BLOCK) {
         return false;
      }

      boolean var7 = this.method1436(var1, var6, var2, var6, var3, var4);
      return !var7 ? this.method1436(var1, var6.getRelative(var1.getBlockFace()), var2, var6, var3, var4) : var7;
   }

   private boolean method1436(PlayerInteractEvent var1, Block var2, CustomItemDefinition var3, Block var4, ItemStack var5, boolean var6) {
      PlacedBlockItemDefinition var7 = (PlacedBlockItemDefinition)var3;
      Player var8 = var1.getPlayer();
      if (InstrumentRegistry.aP(var2) && PermissionNodes.method1623(var8, var7.getPlacePermissionSuffix()) && BlockFaceUtils.simulatePlace(var2, var8)) {
         if (BlockFaceUtils.playerOverlapsBlock(var8, var2)) {
            var1.setUseItemInHand(Result.DENY);
            return false;
         }

         if (var1.getHand() == EquipmentSlot.OFF_HAND && ContentManager.method8084().method8156(var8.getItemInHand())) {
            var1.setUseItemInHand(Result.DENY);
            return false;
         }

         PlacementResult var9 = this.method1439(var8, var5, var7, var2, var4, var1.getBlockFace(), false, false);
         if (var9 == PlacementResult.PLACED) {
            if (var6 && var8.getGameMode() != GameMode.CREATIVE) {
               var1.getItem().setAmount(var1.getItem().getAmount() - 1);
            }

            PlayerInteractionFacade.method5310(var8, var1.getHand());
            method1437(var2);
            return true;
         }
      } else {
         var1.setUseItemInHand(Result.DENY);
      }

      return false;
   }

   private static void method1437(Block var0) {
      Block var1 = var0.getRelative(BlockFace.UP);
      if (InstrumentRegistry.isTallGrass(var1) && ((Bisected)var1.getBlockData()).getHalf() == Half.TOP) {
         BlockEffectsService.breakBlock(var1);
      }

      Block var2 = var0.getRelative(BlockFace.DOWN);
      if (InstrumentRegistry.isTallGrass(var2) && ((Bisected)var2.getBlockData()).getHalf() == Half.BOTTOM) {
         BlockEffectsService.breakBlock(var2);
      }
   }

   private void method1438(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var1.getAction() == Action.RIGHT_CLICK_AIR) {
         if (var1.getPlayer().getGameMode() != GameMode.ADVENTURE && var1.getPlayer().getGameMode() != GameMode.SPECTATOR) {
            if (var2 instanceof CustomBlockDefinition var4) {
               if (var4.anF == null || var4.anF.ard || var4.anF.are) {
                  Player var5 = var1.getPlayer();
                  RayTraceWalker var6 = new RayTraceWalker(var5, 5.0);
                  var6.setForceStepping(true);
                  if (var6.matchesTarget(RayTraceTarget.BLOCK)) {
                     if (var6.hasHitBlock()) {
                        Block var7 = var6.getHitBlock();
                        Block var8 = var7.getRelative(BlockFace.UP);
                        if (var8.getType() == Material.AIR) {
                           if (var4.anF.ard && var7.getType() == Material.WATER || var4.anF.are && var7.getType() == Material.LAVA) {
                              if (BlockFaceUtils.simulatePlace(var8, var1.getPlayer()) && InstrumentRegistry.aP(var8)) {
                                 if (BlockFaceUtils.playerOverlapsBlock(var1.getPlayer(), var8)) {
                                    var1.setCancelled(true);
                                    return;
                                 }

                                 if (var1.getHand() == EquipmentSlot.OFF_HAND && ContentManager.method8084().method8156(var1.getPlayer().getItemInHand())) {
                                    var1.setCancelled(true);
                                    return;
                                 }

                                 if (this.method1439(var1.getPlayer(), var3, (PlacedBlockItemDefinition)var2, var8, var7, var1.getBlockFace(), true, false)
                                    == PlacementResult.PLACED) {
                                    PlayerInteractionFacade.method5310(var1.getPlayer(), var1.getHand());
                                    if (var1.getPlayer().getGameMode() != GameMode.CREATIVE) {
                                       var1.getItem().setAmount(var1.getItem().getAmount() - 1);
                                    }
                                 }
                              } else {
                                 var1.setCancelled(true);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private PlacementResult method1439(
      Player var1, ItemStack var2, PlacedBlockItemDefinition var3, Block var4, Block var5, @Nullable BlockFace var6, boolean var7, boolean var8
   ) {
      PlacementResult var9 = this.method1440(var1, var2, var3, var4, var5, var6, var7, var8);
      if (var9 == PlacementResult.PLACED) {
         if (var1.getGameMode() != GameMode.CREATIVE) {
            ItemStack var10 = var1.getInventory().getItemInOffHand();
            if (!ItemStackUtils.aE(var10) && var10.getType().isBlock()) {
               PlayerInteractionFacade.cI(var1);
            }
         }

         NoteBlockListener.method1081(var4);
      }

      return var9;
   }

   private PlacementResult method1440(
      Player var1, ItemStack var2, PlacedBlockItemDefinition var3, Block var4, Block var5, @Nullable BlockFace var6, boolean var7, boolean var8
   ) {
      if (this.aY(var1)) {
         return PlacementResult.NOT_PLACED;
      }

      if (!var1.isSneaking() && method1441(var6, var5.getType()) && !ContentManager.isCustomBlock(var5)) {
         return PlacementResult.NOT_PLACED;
      }

      if (!var1.isSneaking() && var5.getType() != Material.NOTE_BLOCK && InstrumentRegistry.isInstrumentBlocked(var5)) {
         return PlacementResult.NOT_PLACED_INTERACTABLE;
      }

      if (!var7 && var3.aps == BlockRealizationMode.REAL_WIRE) {
         CustomBlockDefinition var9 = (CustomBlockDefinition)var3;
         if (var9.anF == null || !var9.anF.arc) {
            Block var10 = var4.getRelative(BlockFace.DOWN);
            if (var10.getType() == Material.TRIPWIRE || !InstrumentRegistry.aW(var10) && InstrumentRegistry.aP(var10)) {
               return PlacementResult.NOT_PLACED;
            }
         }
      }

      if (var3.aps == BlockRealizationMode.FIRE) {
         if (var5.getType() != Material.FIRE) {
            if (var3.placeBlock(var4, true)) {
               return PlacementResult.PLACED;
            }

            return PlacementResult.NOT_PLACED;
         }
      } else {
         if (!Settings.get().blockWorlds.isWorldActive(var4.getWorld())) {
            return PlacementResult.NOT_PLACED;
         }

         CustomBlockDefinition var12 = (CustomBlockDefinition)var3;
         CustomBlockPlaceEvent var13 = new CustomBlockPlaceEvent(var1, var4, var12, true, var5, var4.getState(), var2);
         Bukkit.getPluginManager().callEvent(var13);
         if (var13.isCancelled()) {
            return PlacementResult.NOT_PLACED;
         }

         boolean var11 = var12.placeDirectionalAt(var1, var4.getLocation(), var6, true, var8);
         if (var11) {
            var12.acY.apR.play(var1, var4.getLocation());
         }

         if (var12.aps == BlockRealizationMode.REAL_TRANSPARENT) {
            ContentManager.method8084();
            ContentManager.method8084().bm.method1163(var4.getLocation());
         }

         if (!var11) {
            return PlacementResult.NOT_PLACED;
         }
      }

      this.aZ(var1);
      return PlacementResult.PLACED;
   }

   static boolean method1441(@Nullable BlockFace var0, Material var1) {
      return var0 != BlockFace.SELF && var1 == Material.NOTE_BLOCK;
   }

   public PlacementResult method1442(Player var1, ItemStack var2, Block var3, Block var4, @Nullable BlockFace var5, boolean var6) {
      CustomBlockDefinition var7 = ContentManager.method8084().method8159(var2);
      return var7 != null ? this.method1439(var1, var2, var7, var3, var4, var5, false, var6) : PlacementResult.VANILLA_ITEM_NOT_PLACED;
   }

   public static void method1443(CustomBlockActionEvent var0) {
      Block var1 = var0.getBlock();
      Player var2 = var0.getPlayer();
      CustomBlockDefinition var3 = var0.Bu();
      var0.Bv().removeCustomBlockAndRefreshNms(var1, true, false);
      BlockUtils.clearBlockState(var0.getBlock());
      var3.anF.method10030(var1.getLocation());

      for (Player var5 : BlockFaceUtils.playersInView(var1.getLocation())) {
         var5.sendBlockChange(var1.getLocation(), var1.getBlockData());
         var3.acY.apQ.play(var5, var0.getBlock().getLocation());
      }

      if (Settings.get().fixLiquidsFlow) {
         try {
            BlockEffectsService.getAdapter().replaceFluidsAround(var1);
         } catch (NoSuchMethodError var6) {
         }
      }

      if (Settings.get().fixFloatingBlocks) {
         BlockEffectsService.getAdapter().updateNeighborShapes(var1);
      }

      ItemStack var7 = var2.getInventory().getItemInMainHand();
      if (!ItemStackUtils.aq(var7)) {
         var3.method8761(var2, ItemEventType.PLACED_BLOCK_BREAK, var2x -> var2x.applyOnCustomBlockAction(var0, var3, null));
      }

      if (var2.getGameMode() != GameMode.CREATIVE && var0.Bw()) {
         if (!var3.Dj()) {
            if (ItemStackUtils.aq(var7)) {
               if (var3.Dk()) {
                  var0.aJ(true);
                  LocationScanner.dropItem(var1.getLocation(), var3);
               }
            } else if (var7.getType() == Material.SHEARS && var3.Dl()) {
               var0.aJ(true);
               LocationScanner.dropItem(var1.getLocation(), var3);
            }
         } else {
            LocationScanner.dropItem(var1.getLocation(), var3);
         }
      }
   }

   public static void method1444(CustomBlockChunkStoreBase var0, Block var1, CustomBlockDefinition var2) {
      var0.removeCustomBlockAndRefreshNms(var1, true, false);
      BlockUtils.clearBlockState(var1);
      var2.anF.method10030(var1.getLocation());

      for (Player var4 : BlockFaceUtils.playersInView(var1.getLocation())) {
         var4.sendBlockChange(var1.getLocation(), var1.getBlockData());
         var2.acY.apQ.play(var4, var1.getLocation());
      }

      if (Settings.get().fixLiquidsFlow) {
         try {
            BlockEffectsService.getAdapter().replaceFluidsAround(var1);
         } catch (NoSuchMethodError var5) {
         }
      }

      if (Settings.get().fixFloatingBlocks) {
         BlockEffectsService.getAdapter().updateNeighborShapes(var1);
      }

      if (var2.Dj()) {
         LocationScanner.dropItem(var1.getLocation(), var2);
      }
   }

   public static void method1445(Block var0, Material var1) {
      if (var1 == Material.SPAWNER) {
         var0.getWorld()
            .spawnParticle(
               Particle.BLOCK,
               var0.getLocation().clone().add(0.5, 0.5, 0.5),
               40,
               0.2333333333333333,
               0.2333333333333333,
               0.2333333333333333,
               0.09,
               Material.OBSIDIAN.createBlockData()
            );
      } else {
         BlockEffectsService.spawnBreakParticles(var0);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   void method740(CustomBlockActionEvent var1) {
      method1443(var1);
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   void method1446(BlockPlaceEvent var1) {
      if (!EventRegistrar.isCustomBlockPlaceEvent(var1)) {
         Player var2 = var1.getPlayer();
         if (var1.getHand() == EquipmentSlot.OFF_HAND && ContentManager.method8084().method8156(var2.getItemInHand())) {
            var1.setCancelled(true);
         } else {
            ItemStack var3 = var1.getItemInHand().clone();
            Block var4 = var1.getBlockAgainst();
            Block var5 = var1.getBlockPlaced();
            BlockFace var6 = var4.getFace(var5);
            PlacementResult var7 = this.method1442(var1.getPlayer(), var3, var1.getBlock(), var1.getBlockAgainst(), var6, true);
            if (var7 == PlacementResult.NOT_PLACED) {
               var1.setCancelled(true);
            }

            method1433(var1.getPlayer(), var7 == PlacementResult.PLACED);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method1447(BlockPlaceEvent var1) {
      if (!EventRegistrar.isCustomBlockPlaceEvent(var1) && aX(var1.getPlayer())) {
         method1437(var1.getBlockPlaced());
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   void method1448(BlockBreakEvent var1) {
      if (!EventRegistrar.isCustomBlockBreakEvent(var1)) {
         Block var2 = var1.getBlock();
         if (CustomBlockDefinition.isCustomBlockType(var2)) {
            Material var3 = var2.getType();
            CustomBlockChunkStoreBase var4 = ContentManager.method8084().method8183(var2);
            if (var4.isCustomBlock(var2)) {
               var1.setCancelled(true);
               Player var5 = var1.getPlayer();
               if (var5.getGameMode() != GameMode.CREATIVE && var3 == Material.TRIPWIRE) {
                  return;
               }

               CustomBlockDefinition var6 = ContentManager.method8084().method8160(var4.getCustomBlockId(var2));
               if (var6 == null) {
                  var1.setCancelled(false);
                  return;
               }

               if (var5.getGameMode() != GameMode.CREATIVE) {
                  if (Settings.get().logUnusualBlockBreak) {
                     Log.warn(
                        "Player '"
                           + var5.getName()
                           + "' somehow tried to break a custom block even when not normally possible. They are probably hacking or using Minecraft Bedrock client (Geyser)."
                     );
                  }

                  return;
               }

               Location var7 = var2.getLocation();
               if (var3 == Material.NOTE_BLOCK) {
                  ContentManager.method8084();
                  ContentManager.method8084().bm.method1161(var7);
               }

               var6.anF.method10030(var2.getLocation());
               boolean var8 = !EventRegistrar.callEvent(new CustomBlockBreakEvent(var5, var2, var6));
               if (!var8) {
                  var4.removeCustomBlockAndRefreshNms(var2, true, false);
                  BlockUtils.clearBlockState(var2);
               }

               if (var5.getGameMode() == GameMode.CREATIVE) {
                  for (Player var10 : BlockFaceUtils.playersInView(var7)) {
                     var10.sendBlockChange(var7, var2.getBlockData());
                  }

                  var6.acY.apQ.play(var7, SoundCategory.BLOCKS);
               }

               ContentManager.method8084();
               ContentManager.method8084().bm.method1162(var7, false);
               if (!var8) {
                  if (Settings.get().fixLiquidsFlow) {
                     try {
                        BlockEffectsService.getAdapter().replaceFluidsAround(var2);
                     } catch (NoSuchMethodError var12) {
                     }
                  }

                  if (Settings.get().fixFloatingBlocks) {
                     BlockEffectsService.getAdapter().updateNeighborShapes(var2);
                  }
               }
            } else if (var3 == Material.SPAWNER
               || Settings.get().customBlocksMushroom && CustomBlockDefinition.isMushroomMaterial(var3)
               || Settings.get().customBlocksNote && CustomBlockDefinition.isNoteMaterial(var3)
               || Settings.get().customBlocksChorus && CustomBlockDefinition.isChorusMaterial(var3)) {
               method1445(var2, var3);
               if (Settings.get().fixLiquidsFlow) {
                  try {
                     BlockEffectsService.getAdapter().replaceFluidsAround(var2);
                  } catch (NoSuchMethodError var11) {
                  }
               }
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   void method1449(PlayerInteractEvent var1) {
      if (var1.getAction() == Action.RIGHT_CLICK_BLOCK) {
         if (!ItemStackUtils.method7250(var1)) {
            Block var2 = var1.getClickedBlock();
            if (var2 != null) {
               if (!ContentManager.method8084().aX.aY(var1.getPlayer())) {
                  if (ProtectionPlugins.canInteract_placedBlock(var1)) {
                     if (CustomBlockDefinition.isCustomBlockType(var2)) {
                        ContentManager.method8084();
                        CustomBlockChunkStoreBase var3 = ContentManager.method8084().method8183(var2);
                        if (!var3.isCustomBlock(var2)) {
                           return;
                        }

                        ContentManager.method8084();
                        CustomBlockDefinition var4 = ContentManager.method8084().method8160(var3.getCustomBlockId(var2));
                        if (var4 == null) {
                           return;
                        }

                        CustomBlockInteractEvent var5 = new CustomBlockInteractEvent(
                           var1.getPlayer(), var1.getAction(), var1.getItem(), var2, var1.getBlockFace(), var1.getHand(), var4
                        );
                        Bukkit.getPluginManager().callEvent(var5);
                        if (var5.isCancelled()) {
                           var1.setCancelled(true);
                           return;
                        }

                        var4.method8761(var1.getPlayer(), ItemEventType.PLACED_BLOCK_INTERACT, var2x -> method1450(var1, var4, var2x));
                     }
                  }
               }
            }
         }
      }
   }

   static EffectOutcome method1450(PlayerInteractEvent var0, CustomBlockDefinition var1, ItemEffect var2) {
      ItemStack var3 = var0.getPlayer().getInventory().getItemInMainHand();
      if (var3.getType() == Material.AIR) {
         return var2.applyOnInteract(var0, var1, var1.Ds());
      }

      EffectOutcome var4 = var2.applyOnInteract(var0, var1, var3);
      var0.getPlayer().getInventory().setItemInMainHand(var3);
      return var4;
   }
}
