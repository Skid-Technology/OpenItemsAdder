package dev.lone.itemsadder.item.effect;

import beer.devs.fastnbt.nms.nbt.NCompound;
import beer.devs.fastnbt.nms.nbt.NItem;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.SchedulerUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;

public class ToolStateListener implements Listener {
   public ToolStateListener() {
      EventRegistrar.registerListener(this, Main.getInstance());
   }

   @EventHandler(ignoreCancelled = true)
   private void onInventoryOpen(InventoryOpenEvent var1) {
      if (Settings.get().migrateLegacyV3Durability) {
         refreshToolStates(var1.getInventory());
      }
   }

   @EventHandler(ignoreCancelled = true)
   private void onPlayerJoin(PlayerJoinEvent var1) {
      if (Settings.get().migrateLegacyV3Durability) {
         PlayerInventory var2 = var1.getPlayer().getInventory();
         refreshToolStates(var2);
      }
   }

   @EventHandler(ignoreCancelled = true)
   private void onWorldChange(PlayerChangedWorldEvent var1) {
      if (Settings.get().migrateLegacyV3Durability) {
         SchedulerUtils.runLaterSync(() -> {
            PlayerInventory var1x = var1.getPlayer().getInventory();
            refreshToolStates(var1x);
         }, 25L);
      }
   }

   @EventHandler(ignoreCancelled = true)
   private void onPickup(PlayerPickupItemEvent var1) {
      if (Settings.get().migrateLegacyV3Durability) {
         refreshItemState(var1.getItem().getItemStack());
      }
   }

   @EventHandler(ignoreCancelled = true)
   private void onEntityDamage(EntityDamageEvent var1) {
      if ((var1.getCause() == DamageCause.ENTITY_ATTACK || var1.getCause() == DamageCause.ENTITY_SWEEP_ATTACK)
         && var1.getDamageSource().getCausingEntity() instanceof LivingEntity var2
         && var2.getEquipment() != null
         && this.isToolItem(var2.getEquipment().getItemInMainHand())) {
         CustomItemDefinition.method8826(var2.getEquipment().getItemInMainHand(), 1, var2);
      }

      if (var1.getEntity() instanceof LivingEntity var7 && var7.getEquipment() != null) {
         for (ItemStack var6 : var7.getEquipment().getArmorContents()) {
            if (this.isToolItem(var6)) {
               CustomItemDefinition.method8826(var6, 1, var7);
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   private void onEntityDamageByEntity(EntityDamageByEntityEvent var1) {
      if (var1.getDamager() instanceof LivingEntity var2) {
         if (var2.getEquipment() != null && this.isToolItem(var2.getEquipment().getItemInMainHand())) {
            CustomItemDefinition.method8826(var2.getEquipment().getItemInMainHand(), 1, var2);
         }

         if (var1.getEntity() instanceof LivingEntity var8 && var8.getEquipment() != null) {
            for (ItemStack var7 : var8.getEquipment().getArmorContents()) {
               if (this.isToolItem(var7)) {
                  CustomItemDefinition.method8826(var7, 1, var8);
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true)
   private void onBlockBreak(BlockBreakEvent var1) {
      Player var2 = var1.getPlayer();
      if (this.isToolItem(var2.getEquipment().getItemInMainHand())) {
         CustomItemDefinition.method8826(var2.getEquipment().getItemInMainHand(), 1, var2);
      }
   }

   boolean isToolItem(ItemStack var1) {
      return !ItemStackUtils.aE(var1) && !ItemStackUtils.ae(var1.getType()) && ItemStackUtils.aA(var1) && ContentManager.method8084().isCustomItem(var1);
   }

   private static void refreshToolStates(Inventory var0) {
      for (ItemStack var4 : var0.getContents()) {
         refreshItemState(var4);
      }
   }

   private static void refreshItemState(ItemStack var0) {
      if (!ItemStackUtils.aE(var0)) {
         if (var0.getItemMeta() instanceof Damageable var1) {
            Integer var6 = null;
            Integer var3 = null;
            NItem var4 = new NItem(var0);
            if (var4.hasKey("itemsadder")) {
               NCompound var5 = var4.getCompound("itemsadder");
               if (var5.hasKey("max_custom_durability")) {
                  var6 = var5.getInt("max_custom_durability");
               }

               if (var5.hasKey("custom_durability")) {
                  var3 = var5.getInt("custom_durability");
               }

               var5.remove("max_custom_durability");
               var5.remove("custom_durability");
               var5.remove("fake_durability");
               var4.save();
               if (var6 != null) {
                  var1.setMaxDamage(var6);
                  var1.setMaxStackSize(1);
               }

               if (var3 != null && var1.hasMaxDamage()) {
                  var1.setDamage(var1.getMaxDamage() - var3);
               }

               var0.setItemMeta(var1);
            }
         }
      }
   }
}
