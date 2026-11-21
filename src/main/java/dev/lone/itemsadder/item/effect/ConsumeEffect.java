package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.SchedulerUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ConsumeEffect extends ItemEffect {
   public final int aiZ;

   public ConsumeEffect(int var1) {
      this.aiZ = var1;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      if (var3 != null && var3.getType() != Material.AIR) {
         if (!ItemStackUtils.aB(var3)) {
            ItemStack var5 = null;
            if (var3.getAmount() > 1) {
               var5 = var3.clone();
               ItemStackUtils.au(var5);
               var3.setAmount(1);
            }

            Integer var6 = CustomItemDefinition.method8765(var3);
            if (var6 == null) {
               return EffectOutcome.success();
            }

            if (var6 - this.aiZ <= 0) {
               ItemStackUtils.au(var3);
               return EffectOutcome.failure();
            }

            CustomItemDefinition.method8764(var3, this.aiZ);
            if (var5 != null) {
               ItemStackUtils.method7243(var4, var5);
            }

            CustomItemDefinition.method8766(var3);
         } else {
            ItemStack var11 = null;
            ItemStack var12 = null;
            boolean var7 = false;
            if (var3.isSimilar(var4.getInventory().getItemInMainHand())) {
               var12 = var4.getInventory().getItemInMainHand();
            } else if (var3.isSimilar(var4.getInventory().getItemInOffHand())) {
               var12 = var4.getInventory().getItemInOffHand();
               var7 = true;
            }

            var12 = var12.clone();
            if (var12.getAmount() > 1) {
               var11 = var12.clone();
               ItemStackUtils.au(var11);
               var12.setAmount(1);
            }

            Integer var8 = CustomItemDefinition.method8765(var12);
            if (var8 == null) {
               return EffectOutcome.success();
            }

            if (var8 - this.aiZ <= 0) {
               ItemStackUtils.au(var12);
               return EffectOutcome.failure();
            }

            CustomItemDefinition.method8764(var12, this.aiZ);
            if (var11 != null && var11.getAmount() > 0) {
               ItemStack var9 = var11;
               SchedulerUtils.runLaterSync(() -> ItemStackUtils.method7243(var4, var9), 2L);
            }

            CustomItemDefinition.method8766(var12);
            boolean var14 = var7;
            ItemStack var10 = var12;
            SchedulerUtils.runLaterSync(() -> {
               if (var14) {
                  var4.getInventory().setItemInOffHand(var10);
               } else {
                  var4.getInventory().setItemInMainHand(var10);
               }
            }, 1L);
         }

         return EffectOutcome.failure();
      } else {
         return EffectOutcome.success();
      }
   }

   @Override
   public EffectOutcome applyOnConsume(PlayerItemConsumeEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var3 != null && var3.getType() != Material.AIR) {
         EquipmentSlot var4 = null;
         PlayerInventory var5 = var1.getPlayer().getInventory();
         if (CustomItemDefinition.method8785(var3, var5.getItemInMainHand())) {
            var4 = EquipmentSlot.HAND;
         } else if (CustomItemDefinition.method8785(var3, var5.getItemInOffHand())) {
            var4 = EquipmentSlot.OFF_HAND;
         }

         if (var4 == null) {
            return EffectOutcome.success();
         }

         Integer var6 = CustomItemDefinition.method8765(var3);
         if (var6 == null) {
            return EffectOutcome.success();
         }

         if (var6 - this.aiZ > 0) {
            CustomItemDefinition.method8764(var3, this.aiZ);
            var1.setCancelled(true);
         } else {
            CustomItemDefinition.method8763(var3);
            if (ItemStackUtils.au(var3) > 0) {
               var1.setCancelled(true);
            }
         }

         CustomItemDefinition.method8766(var3);
         var1.getPlayer().getInventory().setItem(var4, var3);
         return EffectOutcome.failure();
      } else {
         return EffectOutcome.success();
      }
   }
}
