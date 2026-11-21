package dev.lone.itemsadder.item;

import dev.lone.itemsadder.Core.OtherPlugins.ProtectionPlugins.ProtectionPlugins;
import dev.lone.itemsadder.api.Events.FurnitureInteractEvent;
import dev.lone.itemsadder.api.Events.FurniturePlaceEvent;
import dev.lone.itemsadder.api.Events.FurniturePreUnsitEvent;
import dev.lone.itemsadder.api.Events.FurnitureUnsitEvent;
import dev.lone.itemsadder.block.CustomBlockListener;
import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.MountTrackingListener;
import dev.lone.itemsadder.item.effect.EffectOutcome;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.ObjectPair;
import dev.lone.itemsadder.util.SoundUtils;
import dev.lone.itemsadder.util.SupplierCallable;
import dev.lone.itemsadder.world.InstrumentRegistry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class PlacedBlockInteractListener extends ItemListenerBase {
   public PlacedBlockInteractListener(Plugin var1) {
      ContentManager var2 = ContentManager.method8084();
      EventRegistrar.registerIfAbsent(this, var1);
      var2.aK.method933(new ItemHandlerBinding(BlockPlaceEvent.class, this::onPlaceBlockItem));
      var2.aK.method933(new ItemHandlerBinding(BlockMultiPlaceEvent.class, this::onPlaceBlockItem));
      var2.aK.method933(new ItemHandlerBinding(PlayerInteractEvent.class, this::onInteractPlacedBlock));
      var2.aK.method933(new ItemHandlerBinding(InventoryClickEvent.class, this::onInventoryClickPlaced));
   }

   @ItemEventHandler
   void onPlaceBlockItem(BlockPlaceEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var2.aoh.hasAction("furniture")) {
         var1.setCancelled(true);
         Block var4 = var1.getBlockPlaced();
         Player var5 = var1.getPlayer();
         if (var1.getPlayer().isSneaking()
            || !InstrumentRegistry.isInstrumentBlocked(var1.getBlockAgainst()) && !ContentManager.method8086(var1.getBlockAgainst())) {
            if (ItemEntityAction.aC(var5) == null) {
               FurnitureItemEntity var6 = (FurnitureItemEntity)var2.aoh.getAction("furniture");
               if (PermissionNodes.method1625(var5, var6.AE())) {
                  if (EventRegistrar.callEvent(new FurniturePlaceEvent(var5, var6))) {
                     Entity var7 = var6.method412(var3, var4, var1.getHand(), var5, this, CustomBlockListener.dn.method714(var5));
                     if (var7 != null) {
                        ContentManager.method8084().aV.aK(var5);
                     }
                  }
               }
            }
         }
      }
   }

   @ItemEventHandler
   void onInteractPlacedBlock(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var1.getAction() == Action.RIGHT_CLICK_BLOCK) {
         if (!this.method1482(var1)) {
            if (!var3.getType().isBlock()) {
               if (var2.aoh.hasAction("furniture")) {
                  Block var4 = var1.getClickedBlock();
                  if (var1.getPlayer().isSneaking() || var4 == null || !InstrumentRegistry.isInstrumentBlocked(var4) && !ContentManager.method8086(var4)) {
                     var1.setUseInteractedBlock(Result.DENY);
                     var1.setUseItemInHand(Result.DENY);
                     if (var4 != null) {
                        Block var5 = var1.getClickedBlock().getRelative(var1.getBlockFace());
                        Player var6 = var1.getPlayer();
                        if (BlockFaceUtils.simulatePlace(var5, var6)) {
                           if (var1.getHand() != EquipmentSlot.OFF_HAND
                              || !ContentManager.method8084().hasBehaviour(var1.getPlayer().getItemInHand(), "furniture")) {
                              ObjectPair var7 = ItemEntityAction.aD(var6);
                              if (var7 == null
                                 || ((Location)var7.second).getBlock().getRelative(var1.getBlockFace().getOppositeFace()).getType() == Material.BARRIER
                                 || var1.getPlayer().isSneaking()) {
                                 FurnitureItemEntity var8 = (FurnitureItemEntity)var2.aoh.getAction("furniture");
                                 if (PermissionNodes.method1625(var6, var8.AE())) {
                                    if (!var8.method406() || !BlockFaceUtils.playerOverlapsBlock(var6, var5)) {
                                       if (EventRegistrar.callEvent(new FurniturePlaceEvent(var6, var8))) {
                                          Entity var9 = var8.method412(var3, var5, var1.getHand(), var6, this, var1.getBlockFace());
                                          if (var9 != null) {
                                             ContentManager.method8084().aV.aK(var6);
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
            }
         }
      }
   }

   boolean method1482(PlayerInteractEvent var1) {
      if (ItemStackUtils.method7250(var1)) {
         return false;
      } else if (var1.getAction() != Action.RIGHT_CLICK_BLOCK) {
         return false;
      } else if (var1.getPlayer().isSneaking()) {
         return false;
      } else if (var1.getPlayer().isInsideVehicle()) {
         return false;
      } else if (var1.getClickedBlock().getType() != Material.BARRIER) {
         return false;
      } else if (ContentManager.method8084().aV.hasAttachment(var1.getPlayer())) {
         return false;
      } else {
         Entity var2 = findEntityAt(var1.getClickedBlock());
         if (var2 == null) {
            return false;
         } else {
            return ProtectionPlugins.canSitOnFurniture(var1.getPlayer(), var2, var1)
               ? this.method1486(var2, var1.getPlayer(), var1, var1.getClickedBlock())
               : false;
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   void onInteractAtEntity(PlayerInteractAtEntityEvent var1) {
      if (!ItemStackUtils.method7251(var1)) {
         if (!ContentManager.method8084().aV.hasAttachment(var1.getPlayer())) {
            Entity var2 = var1.getRightClicked();
            if (var1.getRightClicked() instanceof ArmorStand || var1.getRightClicked() instanceof ItemFrame) {
               this.method1487(
                  var2,
                  var1.getPlayer(),
                  var1,
                  null,
                  () -> !var1.getPlayer().isSneaking()
                     && !var1.getPlayer().isInsideVehicle()
                     && ProtectionPlugins.canSitOnFurniture(var1.getPlayer(), var2, var1)
               );
            }

            ItemEventType var4 = null;
            ItemStack var3;
            if (var2 instanceof ArmorStand) {
               if (!ProtectionPlugins.canInteract_furniture(var1)) {
                  var1.setCancelled(true);
                  return;
               }

               ArmorStand var5 = (ArmorStand)var2;
               var3 = var5.getHelmet();
               var4 = ItemEventType.PLACED_ARMORSTAND_INTERACT;
            } else if (var2 instanceof ItemFrame) {
               if (!ProtectionPlugins.canInteract_furniture(var1)) {
                  var1.setCancelled(true);
                  return;
               }

               var3 = FurnitureItemEntity.A(var2);
               var4 = ItemEventType.PLACED_ITEMFRAME_INTERACT;
            } else {
               var3 = null;
            }

            CustomItemDefinition var8 = ContentManager.method8084().method8149(var3);
            if (var8 != null) {
               FurnitureItemEntity var6 = (FurnitureItemEntity)var8.aoh.getAction("furniture");
               if (var6 != null) {
                  if (!EventRegistrar.callEvent(new FurnitureInteractEvent(var1.getPlayer(), var2, Action.RIGHT_CLICK_AIR, var6))) {
                     var1.setCancelled(true);
                  } else {
                     var8.method8761(var1.getPlayer(), ItemEventType.PLACED_FURNITURE_INTERACT, var3x -> var3x.applyOnInteractEntity(var1, var8, var3));
                     var8.method8761(var1.getPlayer(), var4, var3x -> var3x.applyOnInteractEntity(var1, var8, var3));
                     if (!var1.isCancelled() && var6.AC() && var2 instanceof ItemFrame && var1.getPlayer().isSneaking()) {
                        ItemFrame var7 = (ItemFrame)var2;
                        var7.setRotation(var7.getRotation().rotateClockwise());
                        SoundUtils.playSound(var2.getLocation(), "entity.item_frame.rotate_item", SoundCategory.BLOCKS);
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   void onInteract(PlayerInteractEvent var1) {
      if (!ItemStackUtils.method7250(var1)) {
         if (var1.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (var1.getClickedBlock() != null) {
               Player var2 = var1.getPlayer();
               if (!CustomBlockListener.dn.method723(var2)) {
                  Entity var3 = var1.getClickedBlock().getType() == Material.BARRIER ? findEntityAt(var1.getClickedBlock()) : ItemEntityAction.aC(var2);
                  if (var3 != null) {
                     if (!ContentManager.method8084().aV.hasAttachment(var2)) {
                        if (var1.getClickedBlock().getType() == Material.BARRIER) {
                           ItemStack var4 = FurnitureItemEntity.A(var3);
                           this.method1484(var4, var1, var3);
                           this.method1487(
                              var3,
                              var2,
                              var1,
                              var1.getClickedBlock(),
                              () -> !var2.isSneaking() && !var2.isInsideVehicle() && ProtectionPlugins.canSitOnFurniture(var2, var3, var1)
                           );
                        } else if (var3.getType() == EntityType.ITEM_DISPLAY) {
                           ItemStack var6 = FurnitureItemEntity.A(var3);
                           this.method1484(var6, var1, var3);
                           if (CustomItemDefinition.isCustomItem(var6)) {
                              CustomItemDefinition var5 = ContentManager.method8084().method8149(var6);
                              if (var5 != null && var5.aoh.hasAction("furniture_sit") && ProtectionPlugins.canSitOnFurniture(var2, var3, var1)) {
                                 this.method1486(var3, var2, var1, null);
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

   private void method1484(ItemStack var1, PlayerInteractEvent var2, Entity var3) {
      if (CustomItemDefinition.isCustomItem(var1)) {
         CustomItemDefinition var4 = ContentManager.method8084().method8149(var1);
         if (var4 != null) {
            if (!ProtectionPlugins.canInteract_furniture(var2, var3)) {
               var2.setUseInteractedBlock(Result.DENY);
            } else {
               FurnitureItemEntity var5 = (FurnitureItemEntity)var4.aoh.getAction("furniture");
               if (!EventRegistrar.callEvent(new FurnitureInteractEvent(var2.getPlayer(), var3, var2.getAction(), var5))) {
                  var2.setUseInteractedBlock(Result.DENY);
               } else {
                  var4.method8761(var2.getPlayer(), ItemEventType.PLACED_FURNITURE_INTERACT, var4x -> {
                     EffectOutcome var5x = var4x.applyOnInteract(var2, var4, var1);
                     if (var5x.alb != null) {
                        FurnitureItemEntity.method398(var3, var5x.alb);
                     }

                     return var5x;
                  });
                  this.method1485(var4, var1, var2, var3, ItemEventType.PLACED_ARMORSTAND_INTERACT);
                  this.method1485(var4, var1, var2, var3, ItemEventType.PLACED_ITEMFRAME_INTERACT);
                  ItemDamageAction var6 = (ItemDamageAction)var4.aoh.getAction("furniture_consumable");
                  if (var6 != null) {
                     var6.method339(var2.getPlayer(), var3, var6, var4);
                  }
               }
            }
         }
      }
   }

   private void method1485(CustomItemDefinition var1, ItemStack var2, PlayerInteractEvent var3, Entity var4, ItemEventType var5) {
      if (var1.aog.hasEffects(var5)) {
         var1.method8761(var3.getPlayer(), var5, var4x -> {
            EffectOutcome var5x = var4x.applyOnInteract(var3, var1, var2);
            if (var5x.alb != null) {
               FurnitureItemEntity.method398(var4, var5x.alb);
            }

            return var5x;
         });
      }
   }

   private boolean method1486(Entity var1, Player var2, Cancellable var3, @Nullable Block var4) {
      return this.method1487(var1, var2, var3, var4, null);
   }

   private boolean method1487(Entity var1, Player var2, Cancellable var3, @Nullable Block var4, SupplierCallable var5) {
      ItemStack var6 = FurnitureItemEntity.A(var1);
      if (!CustomItemDefinition.isCustomItem(var6)) {
         return false;
      }

      CustomItemDefinition var7 = ContentManager.method8084().method8149(var6);
      if (var7 == null) {
         return false;
      }

      if (!var7.aoh.hasAction("furniture_sit") || !var7.aoh.hasAction("furniture")) {
         return false;
      }

      if (var5 != null && !(Boolean)var5.call()) {
         return false;
      }

      var3.setCancelled(true);
      FurnitureItemEntity var8 = (FurnitureItemEntity)var7.aoh.getAction("furniture");
      if (var8 == null) {
         return false;
      }

      if (!MountTrackingListener.getInstance().hasMount(var2)) {
         if (var4 != null && !var1.getBoundingBox().overlaps(var4.getBoundingBox().clone().expand(-0.3))) {
            return false;
         }

         if (var8.acW != var1.getType()) {
            return false;
         }

         ItemFlagAction var9 = (ItemFlagAction)var7.aoh.getAction("furniture_sit");
         return MountTrackingListener.getInstance().tryMount(var2, var1, var9.AK() ? var4 : null, var9.AJ(), var8.Ay(), false, true, var9.AL(), var9.AM());
      } else {
         return false;
      }
   }

   @Nullable
   private static Entity findEntityAt(Block var0) {
      return var0.getWorld()
         .getNearbyEntities(var0.getLocation().add(0.5, 0.5, 0.5), 8.0, 8.0, 8.0)
         .stream()
         .filter(FurnitureItemEntity::method409)
         .filter(var1 -> var1.getBoundingBox().overlaps(var0.getBoundingBox().clone().expand(-0.3)))
         .findFirst()
         .orElse(null);
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   public void onEntityDismount(EntityDismountEvent var1) {
      if (var1.getDismounted() instanceof ArmorStand
         && var1.getEntity().getType() == EntityType.PLAYER
         && var1.getEntity() instanceof Player var2
         && MountTrackingListener.getInstance().hasMount((Player)var1.getEntity())) {
         CustomItemDefinition var5 = FurnitureItemEntity.B(var1.getDismounted());
         if (var5 != null) {
            FurnitureItemEntity var4 = (FurnitureItemEntity)var5.aoh.getAction("furniture");
            if (var4 != null && !EventRegistrar.callEvent(new FurniturePreUnsitEvent(var2, var1.getDismounted(), var4))) {
               var1.setCancelled(true);
               return;
            }
         }

         MountTrackingListener.getInstance().method1124((Player)var1.getEntity());
         var1.getEntity().teleport(var1.getEntity().getLocation().add(0.0, 0.5, 0.0));
         EventRegistrar.callEvent(new FurnitureUnsitEvent(var2, var1.getDismounted(), null));
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   void onArmorStandManipulate(PlayerArmorStandManipulateEvent var1) {
      ItemStack var2 = var1.getRightClicked().getHelmet();
      if (CustomItemDefinition.isCustomItem(var2)) {
         CustomItemDefinition var3 = ContentManager.method8084().method8149(var2);
         if (var3 != null) {
            if (var3.aoh.hasAction("furniture")) {
               var1.setCancelled(true);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   void onEntityDamage(EntityDamageEvent var1) {
      if (var1.getEntity().getType() == EntityType.PLAYER) {
         if (var1.getCause() == DamageCause.SUFFOCATION) {
            if (MountTrackingListener.getInstance().hasMount((Player)var1.getEntity())) {
               var1.setCancelled(true);
            }
         }
      }
   }

   @ItemEventHandler
   private void onInventoryClickPlaced(InventoryClickEvent var1, CustomItemDefinition var2, @Nullable ItemStack var3) {
      if (var1.getInventory() instanceof HorseInventory) {
         if (var2.getType() != Material.LEATHER_HORSE_ARMOR) {
            return;
         }

         if (var2.aoh.aiy.size() > 0) {
            var1.setCancelled(true);
         }
      }
   }
}
