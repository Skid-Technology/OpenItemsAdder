package dev.lone.itemsadder.item.effect;

import com.jeff_media.armorequipevent.ArmorEquipEvent;
import dev.lone.itemsadder.api.CustomBlockActionEvent;
import dev.lone.itemsadder.api.PlayerActionEvent;
import dev.lone.itemsadder.api.PlayerEntityInteractEvent;
import dev.lone.itemsadder.api.PlayerEquipEvent;
import dev.lone.itemsadder.api.PlayerLivingActionEvent;
import dev.lone.itemsadder.event.EntityEquipmentChangeEvent;
import dev.lone.itemsadder.event.PlayerHeldItemEvent;
import dev.lone.itemsadder.item.CustomItemDefinition;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public interface ItemEffectHandler {
   EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4);

   default EffectOutcome applyToBlock(Event var1, Block var2, CustomItemDefinition var3, ItemStack var4, Player var5) {
      return this.applyEffect(var1, var3, var4, var5);
   }

   default EffectOutcome applyOnBlockBreak(BlockBreakEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyToBlock(var1, var1.getBlock(), var2, var3, var1.getPlayer());
   }

   default EffectOutcome applyOnCustomBlockAction(CustomBlockActionEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyToBlock(var1, var1.getBlock(), var2, var3, var1.getPlayer());
   }

   default EffectOutcome applyOnInteract(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyToBlock(var1, var1.getClickedBlock(), var2, var3, var1.getPlayer());
   }

   default EffectOutcome applyOnDamage(EntityDamageByEntityEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, (Player)var1.getDamager());
   }

   default EffectOutcome applyOnEntityDeath(EntityDeathEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, var1.getEntity().getKiller());
   }

   default EffectOutcome applyOnInteractEntity(PlayerInteractEntityEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, var1.getPlayer());
   }

   default EffectOutcome applyOnDrop(PlayerDropItemEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, var1.getPlayer());
   }

   default EffectOutcome applyOnHeldItem(PlayerHeldItemEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, var1.getPlayer());
   }

   default EffectOutcome applyOnShootBow(EntityShootBowEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, (Player)var1.getEntity());
   }

   default EffectOutcome applyOnEquipmentChange(EntityEquipmentChangeEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, (Player)var1.getEntity());
   }

   default EffectOutcome applyOnConsume(PlayerItemConsumeEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, var1.getPlayer());
   }

   default EffectOutcome applyOnFoodLevel(FoodLevelChangeEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, (Player)var1.getEntity());
   }

   default EffectOutcome applyOnEditBook(PlayerEditBookEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, var1.getPlayer());
   }

   default EffectOutcome applyOnFish(PlayerFishEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, var1.getPlayer());
   }

   default EffectOutcome applyOnInventoryClick(InventoryClickEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, (Player)var1.getWhoClicked());
   }

   default EffectOutcome applyOnProjectileLaunch(ProjectileLaunchEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, (Player)var1.getEntity().getShooter());
   }

   default EffectOutcome applyOnProjectileHit(ProjectileHitEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, (Player)var1.getEntity().getShooter());
   }

   default EffectOutcome applyOnItemHeld(PlayerItemHeldEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, var1.getPlayer());
   }

   default EffectOutcome applyOnInventoryDrag(InventoryDragEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, (Player)var1.getWhoClicked());
   }

   default EffectOutcome applyOnPickup(PlayerPickupItemEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, var1.getPlayer());
   }

   default EffectOutcome applyOnBucketEmpty(PlayerBucketEmptyEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyToBlock(var1, var1.getBlock(), var2, var3, var1.getPlayer());
   }

   default EffectOutcome applyOnBucketFill(PlayerBucketFillEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyToBlock(var1, var1.getBlock(), var2, var3, var1.getPlayer());
   }

   default EffectOutcome applyOnArmorEquip(ArmorEquipEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, var1.getPlayer());
   }

   default EffectOutcome applyOnItemBreak(PlayerItemBreakEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, var1.getPlayer());
   }

   default EffectOutcome applyOnSwapHand(PlayerSwapHandItemsEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, var1.getPlayer());
   }

   default EffectOutcome applyOnPlayerEvent(PlayerEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, var1.getPlayer());
   }

   default EffectOutcome applyOnLivingAction(PlayerLivingActionEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, var1.anj);
   }

   default EffectOutcome applyOnEntityInteract(PlayerEntityInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, var1.ang);
   }

   default EffectOutcome applyOnEquip(PlayerEquipEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, var1.anl);
   }

   default EffectOutcome applyOnAction(PlayerActionEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.applyEffect(var1, var2, var3, var1.ani);
   }
}
