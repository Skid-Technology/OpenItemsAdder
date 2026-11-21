package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.nbt.NItem;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EventRegistrar;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class InventoryItemListener implements IaListener {
   private final ContentManager afa;

   public InventoryItemListener(Plugin var1, ContentManager var2) {
      this.afa = var2;
      EventRegistrar.registerIfAbsent(this, var1);
      var2.aK.method933(new ItemHandlerBinding(PlayerInteractEvent.class, this::method666));
      var2.aK.method933(new ItemHandlerBinding(InventoryClickEvent.class, this::method667));
      var2.aK.method933(new ItemHandlerBinding(InventoryClickEvent.class, this::method665));
   }

   @ItemEventHandler
   private void method665(InventoryClickEvent var1, CustomItemDefinition var2, ItemStack var3) {
      Player var4 = (Player)var1.getWhoClicked();
      if (var1.getInventory().getType() == InventoryType.CRAFTING) {
         CustomItemDefinition var5 = this.afa.method8149(var1.getCurrentItem());
         if (var5 != null) {
            if (var1.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY
               && (var4.getInventory().getHelmet() == null || var4.getInventory().getHelmet().getType() == Material.AIR)
               && var5.aoh.hasAction("hat")) {
               var5.method8761(var4, ItemEventType.WEAR, var2x -> var2x.applyOnInventoryClick(var1, var5, var1.getCurrentItem()));
               var1.setCancelled(true);
               var4.getInventory().setHelmet(var1.getCurrentItem());
               var1.setCurrentItem(null);
            } else if (var1.getAction() == InventoryAction.PLACE_ALL || var1.getAction() == InventoryAction.NOTHING) {
               var5.method8761(var4, ItemEventType.WEAR, var2x -> var2x.applyOnInventoryClick(var1, var5, var1.getCurrentItem()));
               CustomItemDefinition var6 = this.afa.method8149(var1.getCursor());
               if (var6 != null && var6.DX() && var5.aoh.hasAction("hat")) {
                  var5.method8761(var1.getWhoClicked(), ItemEventType.UNWEAR, var2x -> var2x.applyOnInventoryClick(var1, var5, var1.getCurrentItem()));
               }
            }
         } else {
            CustomItemDefinition var7 = this.afa.method8149(var1.getCursor());
            if (var7 != null
               && var1.getRawSlot() == 5
               && var7.aoh.hasAction("hat")
               && (var4.getInventory().getHelmet() == null || var4.getInventory().getHelmet().getType() == Material.AIR)) {
               var7.method8761(var4, ItemEventType.WEAR, var2x -> var2x.applyOnInventoryClick(var1, var7, var1.getCursor()));
               var1.setCancelled(true);
               var4.getInventory().setHelmet(var1.getCursor());
               var1.setCursor(null);
            }
         }
      }
   }

   @ItemEventHandler
   private void method666(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (!ItemStackUtils.method7250(var1)) {
         if (var1.getAction() == Action.RIGHT_CLICK_BLOCK || var1.getAction() == Action.RIGHT_CLICK_AIR) {
            if (var1.getPlayer().getInventory().getHelmet() == null || var1.getPlayer().getInventory().getHelmet().getType() == Material.AIR) {
               if (var2.aoh.hasAction("hat")) {
                  var1.setCancelled(true);
                  if (!Main.aFg || !new NItem(var3).hasKey("BPIsBackPack")) {
                     ItemStack var4 = var1.getItem().clone();
                     var4.setAmount(1);
                     var1.getPlayer().getInventory().setHelmet(var4);
                     ItemStackUtils.dl(var1.getPlayer());
                     var2.method8761(var1.getPlayer(), ItemEventType.UNHELD, var3x -> var3x.applyOnInteract(var1, var2, var3));
                     var2.method8761(var1.getPlayer(), ItemEventType.WEAR, var3x -> var3x.applyOnInteract(var1, var2, var3));
                  }
               }
            }
         }
      }
   }

   @ItemEventHandler
   private void method667(InventoryClickEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var1.getSlotType().equals(SlotType.ARMOR)) {
         if (var1.getCursor() != null) {
            if (var2 != null) {
               if (var2.aoh.hasAction("hat")) {
                  var2.method8761(var1.getWhoClicked(), ItemEventType.UNWEAR, var3x -> var3x.applyOnInventoryClick(var1, var2, var3));
               }
            } else if (CustomItemDefinition.isCustomItem(var1.getCursor())) {
               CustomItemDefinition var4 = this.afa.method8149(var1.getCursor());
               if (var4 != null) {
                  if (!var4.aoh.hasAction("hat")) {
                     return;
                  }

                  var4.method8761(var1.getWhoClicked(), ItemEventType.UNWEAR, var2x -> var2x.applyOnInventoryClick(var1, var4, var1.getCursor()));
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method668(PlayerInteractAtEntityEvent var1) {
      if (var1.getRightClicked() instanceof ArmorStand) {
         ItemStack var2 = var1.getPlayer().getInventory().getItemInMainHand();
         CustomItemDefinition var3 = this.afa.method8149(var2);
         if (var3 != null && var3.aoh.hasAction("hat")) {
            ArmorStand var4 = (ArmorStand)var1.getRightClicked();
            if (var4.getEquipment() != null) {
               var1.setCancelled(true);
               if (FurnitureItemEntity.method409(var4) || ItemThrowAction.method397(var4)) {
                  return;
               }

               if (var4.getEquipment().getHelmet().getType() != Material.AIR) {
                  ItemStackUtils.method7243(var1.getPlayer(), var4.getEquipment().getHelmet());
               }

               ItemStack var5 = var2.clone();
               var5.setAmount(1);
               ItemStackUtils.dl(var1.getPlayer());
               var4.getEquipment().setHelmet(var5);
               ItemStack var6 = var4.getEquipment().getHelmet();
               var3.method8761(var1.getPlayer(), ItemEventType.ARMOR_STAND_PUT_ITEM, var3x -> var3x.applyOnInteractEntity(var1, var3, var6));
               var3.method8761(var1.getPlayer(), ItemEventType.UNHELD, var3x -> var3x.applyOnInteractEntity(var1, var3, var6));
               var3.method8761(var1.getPlayer(), ItemEventType.UNHELD_OFFHAND, var3x -> var3x.applyOnInteractEntity(var1, var3, var6));
            }
         }
      }
   }
}
