package dev.lone.itemsadder.entity;

import beer.devs.fastnbt.nms.nbt.NItem;
import dev.lone.itemsadder.Core.OtherPlugins.ProtectionPlugins.ProtectionPlugins;
import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.PlayerEntityInteractEvent;
import dev.lone.itemsadder.api.Events.FurnitureBreakEvent;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.block.CustomBlockListener;
import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.FurnitureItemEntity;
import dev.lone.itemsadder.item.ItemEntityAction;
import dev.lone.itemsadder.item.ItemEventType;
import dev.lone.itemsadder.item.ItemThrowAction;
import dev.lone.itemsadder.item.PlaceableItemSpec;
import dev.lone.itemsadder.item.PlacedItemInstance;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.CooldownTracker;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityAttachmentListener implements IaListener {
   private static final long afh = 20L;
   public final WeakHashMap afi = new WeakHashMap();
   private final WeakHashMap afj = new WeakHashMap();
   private final WeakHashMap afk = new WeakHashMap();
   private final WeakHashMap afl = new WeakHashMap();
   public final HashMap afm = new HashMap();
   private final HashMap afn = new HashMap();

   public EntityAttachmentListener(Plugin var1) {
      EventRegistrar.registerIfAbsent(this, var1);
   }

   @Override
   public void unregister() {
      IaListener.super.unregister();
      this.bW();
   }

   public void updateAttachment(Entity var1, boolean var2) {
      this.method564(var1, null, var2, false, true);
   }

   public void updateAttachmentFor(Entity var1, @Nullable Player var2, boolean var3) {
      this.method564(var1, var2, var3, true, true);
   }

   public void method564(Entity var1, @Nullable Player var2, boolean var3, boolean var4, boolean var5) {
      if (!var4) {
         this.method566(var1, var2, var3, var5);
      } else if (var1 != null) {
         method565(this.afm, this.afn, var1.getEntityId(), var2, () -> this.method566(var1, var2, var3, var5));
      }
   }

   static void method565(Map var0, Map var1, int var2, @Nullable Player var3, Runnable var4) {
      boolean var5 = !var0.containsKey(var2);
      if (var5 || var0.get(var2) == var3) {
         if (var5) {
            var0.put(var2, var3);
         }

         try {
            var4.run();
         } finally {
            if (var5 && var0.get(var2) == var3) {
               var0.remove(var2);
               var1.remove(var2);
            }
         }
      }
   }

   private void method566(Entity var1, @Nullable Player var2, boolean var3, boolean var4) {
      ContentManager var5 = ContentManager.method8084();
      if (var5 != null && var5.method8111()) {
         if (var1 != null && var1.isValid()) {
            ItemStack var6 = switch (var1) {
               case ArmorStand var9 -> var9.getHelmet().clone();
               case ItemFrame var10 -> var10.getItem().clone();
               case ItemDisplay var11 -> var11.getItemStack().clone();
               default -> throw new RuntimeException("Unknown furniture entity type " + var1.getClass());
            };
            if (CustomItemDefinition.isCustomItem(var6)) {
               CustomItemDefinition var15 = var5.method8149(var6);
               if (var15 == null) {
                  var1.remove();
               } else if (var15.aoh.hasAction("vehicle") || var15.aoh.hasAction("furniture")) {
                  if (var15.aoh.hasAction("vehicle") && (var2 == null || ProtectionPlugins.canRemoveVehicle(var2, (Entity)var1, var6))
                     || var15.aoh.hasAction("furniture") && (var2 == null || method567(var2, (Entity)var1))) {
                     FurnitureItemEntity var16 = (FurnitureItemEntity)var15.aoh.getAction("furniture");
                     if (var16 != null && var2 != null) {
                        if (!PermissionNodes.method1624(var2, var16.AD())) {
                           return;
                        }

                        if (!EventRegistrar.callEvent(new FurnitureBreakEvent(var2, (Entity)var1, var16))) {
                           return;
                        }
                     }

                     PlayerEntityInteractEvent var17 = new PlayerEntityInteractEvent(var2, (Entity)var1);
                     switch (var1) {
                        case ArmorStand var12:
                           var15.method8761(var2, ItemEventType.PLACED_ARMORSTAND_BREAK, var3x -> var3x.applyOnEntityInteract(var17, var15, var6));
                           break;
                        case ItemFrame var13:
                           var15.method8761(var2, ItemEventType.PLACED_ITEMFRAME_BREAK, var3x -> var3x.applyOnEntityInteract(var17, var15, var6));
                           break;
                        case ItemDisplay var14:
                           var15.method8761(var2, ItemEventType.PLACED_ITEMFRAME_BREAK, var3x -> var3x.applyOnEntityInteract(var17, var15, var6));
                           break;
                        default:
                     }

                     var15.method8761(var2, ItemEventType.PLACED_FURNITURE_BREAK, var3x -> var3x.applyOnEntityInteract(var17, var15, var6));
                     if (var17.isCancelled()) {
                        return;
                     }

                     int var19 = 0;
                     if (var16 != null) {
                        var19 = var16.Az();
                        if (var16.method406()) {
                           var16.method370((Entity)var1);
                        }

                        if (var3 && !var16.AB()) {
                           var3 = false;
                        }
                     }

                     if (var19 > 0) {
                        BlockUtils.clearBlockState(var1.getLocation().getBlock());
                     }

                     if (var3) {
                        ItemStack var21 = var16 == null ? var6 : var15.Ds();
                        var21.setAmount(1);
                        NItem var23 = new NItem(var21);
                        if (var23.hasKey("IAOrigName")) {
                           var23.setCustomName(var23.getString("IAOrigName"));
                           var23.remove("IAOrigName");
                           var23.save();
                        }

                        if (ProjectileTypeHelper.isAllowedEntityType(var1.getType())) {
                           Location var24 = var1.getLocation().clone();
                           BlockFace var25 = BlockFaceUtils.vectorToFace(var24.getDirection());
                           if (var25 == BlockFace.DOWN) {
                              var24.add(0.0, -1.0, 0.0);
                           }

                           LocationScanner.dropItem(var24, var21);
                        } else {
                           LocationScanner.dropItem(var1.getLocation(), var21);
                        }
                     }

                     var1.remove();
                     if (var2 != null) {
                        NativePacketsHolder.method5250(var2, var1.getEntityId());
                        MountTrackingListener.getInstance().method1124(var2);
                        MountTrackingListener.getInstance().method1125((Entity)var1);
                        ItemEntityAction var22 = var15.aoh.getEntityAction();
                        if (var22 != null && var4) {
                           var22.acY.apQ.play(var2, var1.getLocation());
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static boolean method567(Entity var0, Entity var1) {
      if (var0 == var1) {
         return false;
      } else if (var0 instanceof Player var2 && !ProtectionPlugins.canBreakFurniture(var2, var1)) {
         return false;
      } else if (var1 instanceof ItemDisplay) {
         ArmorStand var7 = (ArmorStand)var1.getWorld().spawn(var1.getLocation(), ArmorStand.class, var0x -> {
            var0x.setVisible(false);
            var0x.setRemoveWhenFarAway(true);
         });

         try {
            return BlockFaceUtils.simulateDamage(var0, var7);
         } finally {
            var7.remove();
         }
      } else {
         return BlockFaceUtils.simulateDamage(var0, var1);
      }
   }

   public void refreshAttachment(Entity var1, @Nullable Player var2) {
      int var3 = var1.getEntityId();
      Object var4 = new Object();
      this.afm.put(var3, var2);
      this.afn.put(var3, var4);
      SchedulerUtils.runLaterSync(() -> method569(this.afm, this.afn, var3, var2, var4), 20L);
   }

   static void method569(Map var0, Map var1, int var2, @Nullable Player var3, Object var4) {
      if (var1.get(var2) == var4 && var0.get(var2) == var3) {
         var0.remove(var2);
         var1.remove(var2);
      }
   }

   public boolean isAttachedTo(Entity var1, @Nullable Player var2) {
      return this.afm.containsKey(var1.getEntityId()) && this.afm.get(var1.getEntityId()) != var2;
   }

   public void aK(Player var1) {
      this.afk.put(var1, System.currentTimeMillis());
   }

   public boolean hasAttachment(Player var1) {
      if (!this.afk.containsKey(var1)) {
         return false;
      }

      int var2 = Math.max(200, var1.getPing() + 200);
      return System.currentTimeMillis() - (Long)this.afk.get(var1) < var2;
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onEntitiesLoad(EntitiesLoadEvent var1) {
      ContentManager var2 = ContentManager.method8084();
      if (var2 != null && var2.method8110()) {
         this.method573(var1.getEntities());
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onItemsAdderLoad(ItemsAdderLoadDataEvent var1) {
      ContentManager var2 = ContentManager.method8084();
      if (var2 != null && var2.method8110()) {
         for (World var4 : Bukkit.getWorlds()) {
            this.method573(var4.getEntities());
         }
      }
   }

   private void method573(@NotNull List var1) {
      for (Entity var3 : var1) {
         if ((var3 instanceof ArmorStand || var3 instanceof ItemFrame || var3 instanceof ItemDisplay)
            && !var3.isCustomNameVisible()
            && var3.getCustomName() != null) {
            if (FurnitureItemEntity.method409(var3)) {
               FurnitureItemEntity.method404(var3);
            } else if (ItemThrowAction.method397(var3)) {
               ItemThrowAction.method396(var3);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   private void onDamageByEntity(EntityDamageByEntityEvent var1) {
      if (!EventRegistrar.isCustomEntityDamageEvent(var1)) {
         if (var1.getDamager() instanceof Player && var1.getEntity() instanceof ArmorStand && FurnitureItemEntity.method409(var1.getEntity())) {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onAnimation(PlayerAnimationEvent var1) {
      if (var1.getAnimationType() == PlayerAnimationType.ARM_SWING) {
         Player var2 = var1.getPlayer();
         if (var2.getGameMode() != GameMode.SPECTATOR) {
            int var3 = Math.max(200, var2.getPing() + 200);
            if (!this.afj.containsKey(var2) || System.currentTimeMillis() - (Long)this.afj.get(var2) >= var3) {
               if (!this.afi.containsKey(var2) || System.currentTimeMillis() - (Long)this.afi.get(var2) >= var3) {
                  if (!this.afk.containsKey(var2) || System.currentTimeMillis() - (Long)this.afk.get(var2) >= var3) {
                     if (!CustomBlockListener.dn.method726(var2)) {
                        if (var2.getOpenInventory().getType() == InventoryType.CRAFTING || var2.getOpenInventory().getType() == InventoryType.CREATIVE) {
                           if (!CustomBlockListener.dn.method718(var2)) {
                              Entity var4 = ItemEntityAction.aC(var2);
                              if (var4 != null) {
                                 if (CustomBlockListener.dn.method725(var1.getPlayer(), var4)) {
                                    return;
                                 }

                                 this.afj.put(var2, System.currentTimeMillis());
                                 if (this.isAttachedTo(var4, var2)) {
                                    return;
                                 }

                                 if (var2.getGameMode() == GameMode.ADVENTURE && CustomFurniture.byAlreadySpawned(var4) != null) {
                                    return;
                                 }

                                 this.refreshAttachment(var4, var2);
                                 SchedulerUtils.runLaterSync(
                                    () -> {
                                       if (!CustomBlockListener.dn.method730(var2)) {
                                          boolean var5x = true;
                                          if (var2.getGameMode() != GameMode.CREATIVE && (var4 instanceof ArmorStand || var4 instanceof ItemDisplay)) {
                                             CooldownTracker var6 = this.afl.computeIfAbsent(var2, var0 -> new CooldownTracker());
                                             var5x = var6.checkCooldown(var4, Math.max(800L, var3 * 2L));
                                          }

                                          if (var5x) {
                                             this.method564(
                                                var4, var1.getPlayer(), var2.getGameMode() != GameMode.CREATIVE || ItemThrowAction.method397(var4), false, true
                                             );
                                          }
                                       }
                                    },
                                    2L
                                 );
                              }

                              PlacedItemInstance var5 = PlaceableItemSpec.findByPlayer(var2);
                              if (var5 != null) {
                                 this.afj.put(var2, System.currentTimeMillis());
                                 if (this.isAttachedTo(var5.entity().getBukkitEntity(), var2)) {
                                    return;
                                 }

                                 PlaceableItemSpec.method346(var2, var5);
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

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   void onHangingBreak(HangingBreakByEntityEvent var1) {
      if (var1.getRemover() instanceof Player) {
         if (((Player)var1.getRemover()).getGameMode() == GameMode.CREATIVE) {
            Hanging var2 = var1.getEntity();
            if (ProjectileTypeHelper.isAllowedEntityType(var2.getType())) {
               this.updateAttachmentFor(var2, (Player)var1.getRemover(), false);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void onBlockBreak(BlockBreakEvent var1) {
      if (!EventRegistrar.isCustomBlockBreakEvent(var1)) {
         Player var2 = var1.getPlayer();
         if (var2.getGameMode() == GameMode.CREATIVE) {
            Entity var3 = ItemEntityAction.aC(var2);
            if (var3 != null) {
               var1.setCancelled(true);
            } else {
               this.afi.put(var1.getPlayer(), System.currentTimeMillis());
            }

            PlacedItemInstance var4 = PlaceableItemSpec.findByPlayer(var2);
            if (var4 != null) {
               var1.setCancelled(true);
               PlaceableItemSpec.method346(var2, var4);
            }
         }
      }
   }
}
