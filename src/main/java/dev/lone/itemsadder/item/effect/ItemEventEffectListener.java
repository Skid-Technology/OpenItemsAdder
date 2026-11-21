package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.Core.OtherPlugins.Glow;
import dev.lone.itemsadder.Core.OtherPlugins.ProtectionPlugins.ProtectionPlugins;
import dev.lone.itemsadder.api.CustomBlockActionEvent;
import dev.lone.itemsadder.api.PlayerEquipEvent;
import dev.lone.itemsadder.api.PlayerLivingActionEvent;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.event.PlayerHeldItemEvent;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemEventDispatcher;
import dev.lone.itemsadder.item.ItemEventHandler;
import dev.lone.itemsadder.item.ItemEventType;
import dev.lone.itemsadder.item.ItemHandlerBinding;
import dev.lone.itemsadder.item.ItemMetaTracker;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.world.InstrumentRegistry;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ItemEventEffectListener implements IaListener {
   public EquipmentEffectListener alM = new EquipmentEffectListener();

   public ItemEventEffectListener(Plugin var1, ContentManager var2) {
      EventRegistrar.registerIfAbsent(this, var1);
      var2.aK.method933(new ItemHandlerBinding(EntityDamageByEntityEvent.class, this::onDamageByEntity));
      var2.aK.method933(new ItemHandlerBinding(PlayerEquipEvent.class, this::onPlayerEquip));
      var2.aK.method933(new ItemHandlerBinding(PlayerLivingActionEvent.class, this::onLivingAction));
      var2.aK.method933(new ItemHandlerBinding(PlayerInteractEntityEvent.class, this::onInteractEntity));
      var2.aK.method933(new ItemHandlerBinding(PlayerInteractAtEntityEvent.class, this::onInteractEntity));
      var2.aK.method933(new ItemHandlerBinding(PlayerInteractEvent.class, this::onInteract));
      var2.aK.method933(new ItemHandlerBinding(BlockBreakEvent.class, this::onBlockBreak));
      var2.aK.method933(new ItemHandlerBinding(CustomBlockActionEvent.class, this::onCustomBlockAction));
      var2.aK.method933(new ItemHandlerBinding(PlayerDropItemEvent.class, this::onDrop));
      var2.aK.method933(new ItemHandlerBinding(PlayerHeldItemEvent.class, this::onHeldItem));
      var2.aK.method933(new ItemHandlerBinding(PlayerPickupItemEvent.class, this::onPickup));
      var2.aK.method933(new ItemHandlerBinding(PlayerBucketEmptyEvent.class, this::onBucketEmpty));
      var2.aK.method933(new ItemHandlerBinding(PlayerBucketFillEvent.class, this::onBucketFill));
      var2.aK.method933(new ItemHandlerBinding(PlayerItemBreakEvent.class, this::onItemBreak));
   }

   public void registerAll() {
      if (ItemEventDispatcher.aiA) {
         this.alM.register();
      }
   }

   @Override
   public void unregister() {
      IaListener.super.unregister();
      this.alM.unregister();
   }

   @ItemEventHandler
   private void onDamageByEntity(EntityDamageByEntityEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var1.getDamager() instanceof LivingEntity) {
         if (!(var1.getDamager() instanceof Player var4 && !ProtectionPlugins.canUseItem(var4, var2))) {
            if (!EventRegistrar.isReentrantCall()) {
               var2.method8761((LivingEntity)var1.getDamager(), ItemEventType.ATTACK, var3x -> var3x.applyOnDamage(var1, var2, var3));
            }
         }
      }
   }

   @ItemEventHandler
   private void onPlayerEquip(PlayerEquipEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var1.anm) {
         ItemEventType.EQUIPPED_ARMOR_ITEM_PLAYER_DAMAGED_BY_ENTITY.method206(var1, var2, var3);
      } else if (var1.ann == EquipmentSlot.HAND) {
         ItemEventType.HOLDING_ITEM_PLAYER_DAMAGED_BY_ENTITY.method206(var1, var2, var3);
      } else if (var1.ann == EquipmentSlot.OFF_HAND) {
         ItemEventType.HOLDING_ITEM_OFFHAND_PLAYER_DAMAGED_BY_ENTITY.method206(var1, var2, var3);
      }
   }

   @ItemEventHandler
   private void onLivingAction(PlayerLivingActionEvent var1, CustomItemDefinition var2, ItemStack var3) {
      var2.method8761(var1.anj, ItemEventType.KILL, var3x -> var3x.applyOnLivingAction(var1, var2, var3));
   }

   @ItemEventHandler
   private void onInteractEntity(PlayerInteractEntityEvent var1, CustomItemDefinition var2, ItemStack var3) {
      var2.method8761(var1.getPlayer(), ItemEventType.INTERACT_ENTITY, var3x -> var3x.applyOnInteractEntity(var1, var2, var3));
      Entity var4 = var1.getRightClicked();
      EntityType var5 = var4.getType();
      if (ProjectileTypeHelper.isAllowedEntityType(var5)) {
         ItemFrame var6 = (ItemFrame)var4;
         if (var6.getItem().getType().isAir()) {
            var2.method8761(var1.getPlayer(), ItemEventType.ITEM_FRAME_PUT_ITEM, var3x -> var3x.applyOnInteractEntity(var1, var2, var3));
            var2.method8761(var1.getPlayer(), ItemEventType.UNHELD, var3x -> var3x.applyOnInteractEntity(var1, var2, var3));
            var2.method8761(var1.getPlayer(), ItemEventType.UNHELD_OFFHAND, var3x -> var3x.applyOnInteractEntity(var1, var2, var3));
         }
      }
   }

   @ItemEventHandler
   private void onInteract(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      Player var4 = var1.getPlayer();
      if (var1.getAction() != Action.LEFT_CLICK_AIR && var1.getAction() != Action.LEFT_CLICK_BLOCK) {
         if (var1.getAction() == Action.RIGHT_CLICK_AIR || var1.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (var1.getClickedBlock() != null
               && var1.getClickedBlock().getType() != Material.AIR
               && InstrumentRegistry.isInstrumentBlocked(var1.getClickedBlock())
               && var1.getClickedBlock().getType() != Material.NOTE_BLOCK) {
               return;
            }

            if (var4.isSneaking()) {
               var2.method8762(ItemEventType.INTERACT_RIGHT_SHIFT, var1, var3);
               if (var1.getHand() == EquipmentSlot.HAND) {
                  var2.method8762(ItemEventType.INTERACT_MAINHAND_RIGHT_SHIFT, var1, var3);
               } else {
                  var2.method8762(ItemEventType.INTERACT_OFFHAND_RIGHT_SHIFT, var1, var3);
               }
            } else {
               var2.method8762(ItemEventType.INTERACT_RIGHT, var1, var3);
               if (var1.getHand() == EquipmentSlot.HAND) {
                  var2.method8762(ItemEventType.INTERACT_MAINHAND_RIGHT, var1, var3);
               } else {
                  var2.method8762(ItemEventType.INTERACT_OFFHAND_RIGHT, var1, var3);
               }
            }
         }
      } else if (var4.isSneaking()) {
         var2.method8762(ItemEventType.INTERACT_LEFT_SHIFT, var1, var3);
         if (var1.getHand() == EquipmentSlot.HAND) {
            var2.method8762(ItemEventType.INTERACT_MAINHAND_LEFT_SHIFT, var1, var3);
         } else {
            var2.method8762(ItemEventType.INTERACT_OFFHAND_LEFT_SHIFT, var1, var3);
         }
      } else {
         var2.method8762(ItemEventType.INTERACT_LEFT, var1, var3);
         if (var1.getHand() == EquipmentSlot.HAND) {
            var2.method8762(ItemEventType.INTERACT_MAINHAND_LEFT, var1, var3);
         } else {
            var2.method8762(ItemEventType.INTERACT_OFFHAND_LEFT, var1, var3);
         }
      }
   }

   @ItemEventHandler
   private void onBlockBreak(BlockBreakEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (CustomItemDefinition.method8820(var3)) {
         var1.setCancelled(true);
      } else {
         var2.method8761(var1.getPlayer(), ItemEventType.BLOCK_BREAK, var3x -> var3x.applyOnBlockBreak(var1, var2, var3));
      }
   }

   @ItemEventHandler
   private void onCustomBlockAction(CustomBlockActionEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (CustomItemDefinition.method8820(var3)) {
         var1.setCancelled(true);
      } else {
         ItemEventType.BLOCK_BREAK.method180(var1, var2, var3, var1.getPlayer());
      }
   }

   @ItemEventHandler
   private void onDrop(PlayerDropItemEvent var1, CustomItemDefinition var2, ItemStack var3) {
      var2.method8761(var1.getPlayer(), ItemEventType.DROP, var3x -> var3x.applyOnDrop(var1, var2, var3));
      var1.getItemDrop().setItemStack(var3);
      if (var2.DW()) {
         var1.getItemDrop().setCustomNameVisible(true);
         var1.getItemDrop().setCustomName(var3.getItemMeta().getDisplayName());
      }

      if (var2.DA() != null) {
         Glow.set(var1.getItemDrop(), var2.DA());
      }
   }

   @ItemEventHandler
   private void onHeldItem(PlayerHeldItemEvent var1, CustomItemDefinition var2, ItemStack var3) {
      var2.method8761(var1.getPlayer(), ItemEventType.UNHELD, var3x -> var3x.applyOnHeldItem(var1, var2, var3));
      if (var2.Dy()) {
         SchedulerUtils.runLaterSync(() -> {
            Player var2x = var1.getPlayer();
            if (var2x.isOnline() && (var2.method8779(var2x.getInventory().getItemInMainHand()) || var2.method8779(var2x.getInventory().getItemInOffHand()))) {
               ContentManager.method8084().aK.aiN.showBar(var2, var2x, true);
            }
         }, 1L);
      }
   }

   @ItemEventHandler
   private void onPickup(PlayerPickupItemEvent var1, CustomItemDefinition var2, ItemStack var3) {
      var2.method8761(var1.getPlayer(), ItemEventType.PICKUP, var3x -> var3x.applyOnPickup(var1, var2, var3));
      if (ItemStackUtils.method7260(var1)) {
         var2.method8761(var1.getPlayer(), ItemEventType.HELD, var3x -> var3x.applyOnPickup(var1, var2, var3));
      }

      var1.getItem().setItemStack(var3);
   }

   @ItemEventHandler
   private void onBucketEmpty(PlayerBucketEmptyEvent var1, CustomItemDefinition var2, ItemStack var3) {
      var2.method8761(var1.getPlayer(), ItemEventType.BUCKET_EMPTY, var3x -> var3x.applyOnBucketEmpty(var1, var2, var3));
   }

   @ItemEventHandler
   private void onBucketFill(PlayerBucketFillEvent var1, CustomItemDefinition var2, ItemStack var3) {
      var2.method8761(var1.getPlayer(), ItemEventType.BUCKET_FILL, var3x -> var3x.applyOnBucketFill(var1, var2, var3));
   }

   @ItemEventHandler
   private void onItemBreak(PlayerItemBreakEvent var1, CustomItemDefinition var2, ItemStack var3) {
      var2.method8761(var1.getPlayer(), ItemEventType.UNHELD, var3x -> var3x.applyOnItemBreak(var1, var2, var3));
      var2.method8761(var1.getPlayer(), ItemEventType.ITEM_BREAK, var3x -> var3x.applyOnItemBreak(var1, var2, var3));
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onItemDamage(PlayerItemDamageEvent var1) {
      ItemStack var2 = var1.getItem();
      CustomItemDefinition var3 = ContentManager.method8084().method8149(var2);
      if (var3 != null) {
         if (!var3.DN() && ItemStackUtils.aw(var2) - var1.getDamage() <= 0) {
            var1.setCancelled(true);
            ItemStackUtils.method7235(var2, 0);
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void onInventoryClick(InventoryClickEvent var1) {
      if (var1.getAction() != InventoryAction.NOTHING) {
         if (var1.getClick() == ClickType.LEFT || var1.getClick() == ClickType.RIGHT) {
            if (var1.getSlotType() == SlotType.QUICKBAR || var1.getSlotType() == SlotType.ARMOR || var1.getSlot() == 40) {
               Player var2 = (Player)var1.getWhoClicked();
               ItemStack var3 = var1.getCursor();
               CustomItemDefinition var4 = ContentManager.method8084().method8149(var3);
               ItemStack var5 = var1.getCurrentItem();
               CustomItemDefinition var6 = ContentManager.method8084().method8149(var5);
               if (var4 != null) {
                  var4.method8761(var2, ItemEventType.WEAR, var3x -> var3x.applyOnInventoryClick(var1, var4, var3));
                  if (var1.getSlotType() == SlotType.QUICKBAR && var1.getSlot() == var2.getInventory().getHeldItemSlot()) {
                     var4.method8761(var2, ItemEventType.HELD, var3x -> var3x.applyOnInventoryClick(var1, var4, var3));
                  } else if (var1.getSlot() == 40) {
                     var4.method8761(var2, ItemEventType.HELD_OFFHAND, var3x -> var3x.applyOnInventoryClick(var1, var4, var3));
                  }
               }

               if (var6 != null) {
                  var6.method8761(var2, ItemEventType.UNWEAR, var3x -> var3x.applyOnInventoryClick(var1, var6, var5));
                  if (var1.getSlotType() == SlotType.QUICKBAR && var1.getSlot() == var2.getInventory().getHeldItemSlot()) {
                     var6.method8761(var2, ItemEventType.UNHELD, var3x -> var3x.applyOnInventoryClick(var1, var6, var3));
                  } else if (var1.getSlot() == 40) {
                     var6.method8761(var2, ItemEventType.UNHELD_OFFHAND, var3x -> var3x.applyOnInventoryClick(var1, var6, var3));
                  }
               }
            }

            if (Settings.get().fixUnstackableItemsOnClick) {
               ItemMetaTracker.stackSimilarItemsOnClick(var1);
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onSwapHand(PlayerSwapHandItemsEvent var1) {
      ItemStack var2 = var1.getMainHandItem();
      ItemStack var3 = var1.getOffHandItem();
      CustomItemDefinition var4 = ContentManager.method8084().method8149(var2);
      CustomItemDefinition var5 = ContentManager.method8084().method8149(var3);
      if (var4 != null) {
         var4.method8761(var1.getPlayer(), ItemEventType.UNHELD_OFFHAND, var3x -> var3x.applyOnSwapHand(var1, var4, var2));
         var4.method8761(var1.getPlayer(), ItemEventType.HELD, var3x -> var3x.applyOnSwapHand(var1, var4, var2));
      }

      if (var5 != null) {
         var5.method8761(var1.getPlayer(), ItemEventType.UNHELD, var3x -> var3x.applyOnSwapHand(var1, var5, var3));
         var5.method8761(var1.getPlayer(), ItemEventType.HELD_OFFHAND, var3x -> var3x.applyOnSwapHand(var1, var5, var3));
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onItemHeld(PlayerItemHeldEvent var1) {
      ItemStack var2 = var1.getPlayer().getInventory().getItem(var1.getPreviousSlot());
      ItemStack var3 = var1.getPlayer().getInventory().getItem(var1.getNewSlot());
      CustomItemDefinition var4 = ContentManager.method8084().method8149(var2);
      CustomItemDefinition var5 = ContentManager.method8084().method8149(var3);
      if (var4 != null) {
         ItemEventType.UNHELD.method195(var1, var4, var2, var1.getPlayer());
      }

      if (var5 != null) {
         ItemEventType.HELD.method195(var1, var5, var3, var1.getPlayer());
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onEntityDamage(EntityDamageByEntityEvent var1) {
      Entity var2 = var1.getEntity();
      if (var1.getDamager() instanceof LivingEntity) {
         if (var1.getDamager().getType() == EntityType.PLAYER) {
            if (CustomItemDefinition.method8820(((Player)var1.getDamager()).getInventory().getItemInMainHand())) {
               var1.setCancelled(true);
            } else {
               if (ProjectileTypeHelper.isAllowedEntityType(var2.getType())) {
                  ItemFrame var3 = (ItemFrame)var2;
                  ItemStack var4 = var3.getItem();
                  if (CustomItemDefinition.isCustomItem(var4)) {
                     CustomItemDefinition var5 = ContentManager.method8084().method8149(var4);
                     if (var5 == null) {
                        return;
                     }

                     var5.method8761((LivingEntity)var1.getDamager(), ItemEventType.ITEM_FRAME_REMOVE_ITEM, var3x -> var3x.applyOnDamage(var1, var5, var4));
                  }
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onArmorStandManipulate(PlayerArmorStandManipulateEvent var1) {
      this.applyArmorStandEvent(var1, var1.getPlayerItem(), ItemEventType.ARMOR_STAND_PUT_ITEM, true);
      this.applyArmorStandEvent(var1, var1.getArmorStandItem(), ItemEventType.ARMOR_STAND_REMOVE_ITEM, false);
   }

   private void applyArmorStandEvent(PlayerArmorStandManipulateEvent var1, ItemStack var2, ItemEventType var3, boolean var4) {
      CustomItemDefinition var5 = ContentManager.method8084().method8149(var2);
      if (var5 != null) {
         var5.method8761(var1.getPlayer(), var3, var3x -> var3x.applyOnInteractEntity(var1, var5, var2));
         if (ItemEventDispatcher.aiA) {
            if (var4) {
               var5.method8761(var1.getPlayer(), ItemEventType.UNHELD, var3x -> var3x.applyOnInteractEntity(var1, var5, var2));
               var5.method8761(var1.getPlayer(), ItemEventType.UNHELD_OFFHAND, var3x -> var3x.applyOnInteractEntity(var1, var5, var2));
            } else {
               var5.method8761(var1.getPlayer(), ItemEventType.HELD, var3x -> var3x.applyOnInteractEntity(var1, var5, var2));
            }
         }
      }
   }
}
