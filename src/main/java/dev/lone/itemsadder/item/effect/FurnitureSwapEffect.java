package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.FurnitureItemEntity;
import dev.lone.itemsadder.item.ItemEntityAction;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.Log;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class FurnitureSwapEffect extends ItemEffect {
   final FurnitureItemEntity ajX;
   final FurnitureItemEntity ajY;
   private final int ajZ;
   private final int aka;
   private final boolean akb;

   public FurnitureSwapEffect(FurnitureItemEntity var1, FurnitureItemEntity var2, int var3, int var4, boolean var5) {
      this.ajX = var1;
      this.ajY = var2;
      this.ajZ = var3;
      this.aka = var4;
      this.akb = var5;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      Log.warn("replace_furniture action cannot be used on this event type.");
      return EffectOutcome.successWith(var3);
   }

   @Override
   public EffectOutcome applyOnInteract(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.swapFurniture(var1, var1.getPlayer(), var3, ItemEntityAction.aC(var1.getPlayer()));
   }

   @Override
   public EffectOutcome applyOnInteractEntity(PlayerInteractEntityEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.swapFurniture(var1, var1.getPlayer(), var3, var1.getRightClicked());
   }

   private EffectOutcome swapFurniture(Event var1, Player var2, ItemStack var3, Entity var4) {
      if (var4 != null && ItemEntityAction.method363(var4)) {
         FurnitureItemEntity var5 = (FurnitureItemEntity)ItemEntityAction.method364(var4, "furniture");
         if (var5 != null && (this.ajX == null || this.ajX.aeT.equals(var5.aeT)) && BlockFaceUtils.simulatePlace(var4.getLocation().getBlock(), var2)) {
            if ((this.aka <= 0 || var3 != null && var3.getAmount() >= this.aka) && (this.ajZ <= 0 || var3 != null && ItemStackUtils.aw(var3) > this.ajZ)) {
               if (!FurnitureItemEntity.method402(this.ajY.aeT, var5.aeT, var4, this.akb ? null : var2)) {
                  return EffectOutcome.success();
               }

               if (this.aka > 0) {
                  ItemStackUtils.method7231(var3, this.aka);
               }

               if (this.ajZ > 0) {
                  ItemStackUtils.method7235(var3, ItemStackUtils.aw(var3) - this.ajZ);
               }

               if (var1 instanceof Cancellable var6) {
                  var6.setCancelled(true);
               }

               return EffectOutcome.failure();
            } else {
               return EffectOutcome.successWith(var3);
            }
         } else {
            return EffectOutcome.success();
         }
      } else {
         return EffectOutcome.success();
      }
   }
}
