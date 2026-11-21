package dev.lone.itemsadder.item;

import com.jeff_media.armorequipevent.ArmorEquipEvent;
import com.jeff_media.armorequipevent.ArmorType;
import dev.lone.itemsadder.api.CustomBlockActionEvent;
import dev.lone.itemsadder.api.PlayerEntityInteractEvent;
import dev.lone.itemsadder.api.PlayerEquipEvent;
import dev.lone.itemsadder.api.PlayerLivingActionEvent;
import dev.lone.itemsadder.event.EntityEquipmentChangeEvent;
import dev.lone.itemsadder.util.IntTableConstantsD;
import org.bukkit.entity.Player;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public enum ItemEventType {
   BLOCK_BREAK("block_break"),
   ATTACK("attack"),
   KILL("kill"),
   INTERACT_ENTITY("interact.entity"),
   INTERACT_LEFT("interact.left"),
   INTERACT_LEFT_SHIFT("interact.left_shift"),
   INTERACT_RIGHT("interact.right"),
   INTERACT_RIGHT_SHIFT("interact.right_shift"),
   INTERACT_MAINHAND_LEFT("interact_mainhand.left"),
   INTERACT_MAINHAND_LEFT_SHIFT("interact_mainhand.left_shift"),
   INTERACT_MAINHAND_RIGHT("interact_mainhand.right"),
   INTERACT_MAINHAND_RIGHT_SHIFT("interact_mainhand.right_shift"),
   INTERACT_OFFHAND_LEFT("interact_offhand.left"),
   INTERACT_OFFHAND_LEFT_SHIFT("interact_offhand.left_shift"),
   INTERACT_OFFHAND_RIGHT("interact_offhand.right"),
   INTERACT_OFFHAND_RIGHT_SHIFT("interact_offhand.right_shift"),
   DROP("drop"),
   PICKUP("pickup"),
   EAT("eat"),
   DRINK("drink"),
   BOW_SHOT("bow_shot"),
   GUN_SHOT("gun_shot"),
   GUN_NO_AMMO("gun_no_ammo"),
   GUN_RELOAD("gun_reload"),
   BOOK_WRITE("book_write"),
   BOOK_READ("book_read"),
   FISHING_START("fishing_start"),
   FISHING_CAUGHT("fishing_caught"),
   FISHING_FAILED("fishing_failed"),
   FISHING_CANCEL("fishing_cancel"),
   FISHING_BITE("fishing_bite"),
   FISHING_IN_GROUND("fishing_in_ground"),
   WEAR("wear"),
   UNWEAR("unwear"),
   HELD("held"),
   UNHELD("unheld"),
   HELD_OFFHAND("held_offhand"),
   UNHELD_OFFHAND("unheld_offhand"),
   ITEM_THROW("item_throw"),
   ITEM_HIT_GROUND("item_hit_ground"),
   ITEM_HIT_ENTITY("item_hit_entity"),
   TRIDENT_THROW("trident_throw"),
   TRIDENT_HIT_BLOCK("trident_hit_block"),
   TRIDENT_HIT_ENTITY("trident_hit_entity"),
   ITEM_BREAK("item_break"),
   GLIDE_START("glide_start"),
   GLIDE_STOP("glide_stop"),
   ELYTRA_BOOST("elytra_boost"),
   ELYTRA_COLLISION("elytra_collision"),
   PLACED_BLOCK_INTERACT("placed_block.interact"),
   PLACED_BLOCK_BREAK("placed_block.break"),
   PLACED_FURNITURE_INTERACT("placed_furniture.interact"),
   PLACED_FURNITURE_BREAK("placed_furniture.break"),
   @Deprecated
   PLACED_ARMORSTAND_INTERACT("placed_armorstand.interact"),
   @Deprecated
   PLACED_ARMORSTAND_BREAK("placed_armorstand.break"),
   @Deprecated
   PLACED_ITEMFRAME_INTERACT("placed_itemframe.interact"),
   @Deprecated
   PLACED_ITEMFRAME_BREAK("placed_itemframe.break"),
   ITEM_FRAME_PUT_ITEM("item_frame.put_item"),
   ITEM_FRAME_REMOVE_ITEM("item_frame.remove_item"),
   ARMOR_STAND_PUT_ITEM("armor_stand.put_item"),
   ARMOR_STAND_REMOVE_ITEM("armor_stand.remove_item"),
   HOLDING_ITEM_PLAYER_DAMAGED_BY_ENTITY("holding_item.player_damaged_by_entity"),
   HOLDING_ITEM_OFFHAND_PLAYER_DAMAGED_BY_ENTITY("holding_item_offhand.player_damaged_by_entity"),
   EQUIPPED_ARMOR_ITEM_PLAYER_DAMAGED_BY_ENTITY("equipped_armor_item.player_damaged_by_entity"),
   BUCKET_EMPTY("bucket_empty"),
   BUCKET_FILL("bucket_fill");

   private final String text;

   ItemEventType(String var3) {
      this.text = var3;
   }

   @Override
   public String toString() {
      return this.text;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Nullable
   public static EquipmentSlot method178(ItemStack var0, ItemEventType var1) {
      switch (var1) {
         case WEAR:
         case UNWEAR:
            ArmorType var2 = ArmorType.matchType(var0);
            if (var2 == null) {
               return EquipmentSlot.HEAD;
            } else {
               return switch (IntTableConstantsD.SWITCH_MAP[var2.ordinal()]) {
                  case 1 -> EquipmentSlot.HEAD;
                  case 2 -> EquipmentSlot.CHEST;
                  case 3 -> EquipmentSlot.LEGS;
                  case 4 -> EquipmentSlot.FEET;
                  default -> throw new MatchException(null, null);
               };
            }
         case HELD:
         case UNHELD:
            return EquipmentSlot.HAND;
         case HELD_OFFHAND:
         case UNHELD_OFFHAND:
            return EquipmentSlot.OFF_HAND;
         default:
            return null;
      }
   }

   public void method179(BlockBreakEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnBlockBreak(var1, var2, var3));
   }

   public void method180(CustomBlockActionEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnCustomBlockAction(var1, var2, var3));
   }

   public void method181(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnInteract(var1, var2, var3));
   }

   public void method182(EntityDamageByEntityEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnDamage(var1, var2, var3));
   }

   public void method183(EntityDeathEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnEntityDeath(var1, var2, var3));
   }

   public void method184(PlayerInteractEntityEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnInteractEntity(var1, var2, var3));
   }

   public void method185(PlayerDropItemEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnDrop(var1, var2, var3));
   }

   public void method186(EntityShootBowEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnShootBow(var1, var2, var3));
   }

   public void method187(EntityEquipmentChangeEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnEquipmentChange(var1, var2, var3));
   }

   public void method188(PlayerItemConsumeEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnConsume(var1, var2, var3));
   }

   public void method189(FoodLevelChangeEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnFoodLevel(var1, var2, var3));
   }

   public void method190(PlayerEditBookEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnEditBook(var1, var2, var3));
   }

   public void method191(PlayerFishEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnFish(var1, var2, var3));
   }

   public void method192(InventoryClickEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnInventoryClick(var1, var2, var3));
   }

   public void method193(ProjectileLaunchEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnProjectileLaunch(var1, var2, var3));
   }

   public void method194(ProjectileHitEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnProjectileHit(var1, var2, var3));
   }

   public void method195(PlayerItemHeldEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnItemHeld(var1, var2, var3));
   }

   public void method196(InventoryDragEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnInventoryDrag(var1, var2, var3));
   }

   public void method197(PlayerPickupItemEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnPickup(var1, var2, var3));
   }

   public void method198(PlayerBucketEmptyEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnBucketEmpty(var1, var2, var3));
   }

   public void method199(PlayerBucketFillEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnBucketFill(var1, var2, var3));
   }

   public void method200(ArmorEquipEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnArmorEquip(var1, var2, var3));
   }

   public void method201(PlayerItemBreakEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnItemBreak(var1, var2, var3));
   }

   public void method202(PlayerSwapHandItemsEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnSwapHand(var1, var2, var3));
   }

   public void method203(PlayerEvent var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      var2.method8761(var4, this, var3x -> var3x.applyOnPlayerEvent(var1, var2, var3));
   }

   public void method204(PlayerLivingActionEvent var1, CustomItemDefinition var2, ItemStack var3) {
      var2.method8761(var1.anj, this, var3x -> var3x.applyOnLivingAction(var1, var2, var3));
   }

   public void method205(PlayerEntityInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      var2.method8761(var1.ang, this, var3x -> var3x.applyOnEntityInteract(var1, var2, var3));
   }

   public void method206(PlayerEquipEvent var1, CustomItemDefinition var2, ItemStack var3) {
      var2.method8761(var1.anl, this, var3x -> var3x.applyOnEquip(var1, var2, var3));
   }
}
