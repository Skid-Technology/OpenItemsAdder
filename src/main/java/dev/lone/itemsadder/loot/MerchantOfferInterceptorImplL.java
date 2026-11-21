package dev.lone.itemsadder.loot;

import beer.devs.nativepackets.api.MerchantOffersPacketView;
import beer.devs.nativepackets.api.NativeValue;
import dev.lone.itemsadder.item.ItemRegistryBridgeGenericDup2;
import dev.lone.itemsadder.util.IntPair;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MerchantContainer;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftInventoryMerchant;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftMerchant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;

public class MerchantOfferInterceptorImplL implements MerchantOfferInterceptor {
   @Override
   public boolean method5384(MerchantInventory inventory, int recipeIndex, int ingredientSlot, ItemStack candidate) {
      MerchantOffer var5 = (MerchantOffer)((CraftMerchant)inventory.getMerchant()).getMerchant().getOffers().get(recipeIndex);
      ItemCost var6 = ingredientSlot == 0 ? var5.baseCostA : (ItemCost)var5.costB.orElse(null);
      return var6 != null && var6.test(CraftItemStack.asNMSCopy(candidate));
   }

   @Override
   public void method5383(Player player, MerchantOffersPacketView packet) {
      MerchantOffers var3 = (MerchantOffers)packet.offers().nativeHandle();
      if (!var3.isEmpty()) {
         this.method5452(packet, player, var3);
         MerchantOfferInterceptorHolder.bpV.put(player, true);
      }
   }

   public MerchantOffers method5385(MerchantOffers var1) {
      MerchantOffers var2 = new MerchantOffers();

      for (MerchantOffer var4 : var1) {
         var2.add(var4.copy());
      }

      return var2;
   }

   private synchronized void method5452(MerchantOffersPacketView var1, Player var2, MerchantOffers var3) {
      if (var2.getOpenInventory().getTopInventory() instanceof CraftInventoryMerchant) {
         MerchantOffers var4 = this.method5385(var3);
         ServerPlayer var5 = ((CraftPlayer)var2).getHandle();
         Inventory var6 = var5.getInventory();
         ArrayList var7 = new ArrayList();

         for (net.minecraft.world.item.ItemStack var9 : var6.getContents()) {
            if (this.method5454(var9)) {
               var7.add(var9);
            }
         }

         CraftInventoryMerchant var22 = (CraftInventoryMerchant)var2.getOpenInventory().getTopInventory();
         Merchant var23 = ((CraftMerchant)var22.getMerchant()).getMerchant();
         IntPair var10 = (IntPair)bpR.getIfPresent(var2);
         if (var10 != null) {
            int var11 = 0;
            boolean var12 = false;

            for (Iterator var13 = var4.iterator(); var13.hasNext(); var11++) {
               var13.next();
               if (var11 > var10.zU && var11 < var23.getOffers().size()) {
                  var13.remove();
                  var23.getOffers().remove(var11);
                  var12 = true;
               }
            }

            if (var12) {
               bpR.invalidate(var2);
            }

            if (var2.getOpenInventory().getTopInventory() instanceof MerchantInventory) {
               MerchantContainer var26 = ((CraftInventoryMerchant)var2.getOpenInventory().getTopInventory()).getInventory();
               net.minecraft.world.item.ItemStack var14 = var26.getItem(0);
               if (this.method5454(var14)) {
                  var7.add(var14);
               }

               net.minecraft.world.item.ItemStack var15 = var26.getItem(1);
               if (this.method5454(var15)) {
                  var7.add(var15);
               }
            }
         }

         if (!var7.isEmpty()) {
            int var24 = var4.size() - 1;
            ArrayList var25 = new ArrayList();
            int var27 = 0;

            for (int var29 = var4.size(); var27 < var29; var27++) {
               MerchantOffer var30 = (MerchantOffer)var4.get(var27);
               net.minecraft.world.item.ItemStack var16 = var30.getBaseCostA();
               net.minecraft.world.item.ItemStack var17 = var30.getCostB();
               net.minecraft.world.item.ItemStack var18 = null;
               net.minecraft.world.item.ItemStack var19 = null;

               for (net.minecraft.world.item.ItemStack var21 : var7) {
                  if (this.method5455(var16, var21) && !this.method5453(var21, var16)) {
                     var18 = var21;
                  }

                  if (this.method5455(var17, var21) && !this.method5453(var21, var17)) {
                     var19 = var21;
                  }
               }

               if (var18 != null || var19 != null) {
                  MerchantOffer var31 = var30.copy();
                  if (var18 != null) {
                     var31.baseCostA = new ItemCost(
                        var18.getItem().builtInRegistryHolder(), var16.getCount(), DataComponentExactPredicate.allOf(var18.getComponents())
                     );
                  }

                  if (var19 != null) {
                     var31.costB = Optional.of(
                        new ItemCost(var19.getItem().builtInRegistryHolder(), var17.getCount(), DataComponentExactPredicate.allOf(var19.getComponents()))
                     );
                  }

                  var25.add(var31);
               }
            }

            if (!var25.isEmpty()) {
               bpR.put(var2, new IntPair(var1.containerId(), var24));
               var23.getOffers().addAll(var25);
               NativeValue var28 = () -> var4;
               var1.offers(var28);
            }
         }
      }
   }

   private boolean method5453(net.minecraft.world.item.ItemStack var1, net.minecraft.world.item.ItemStack var2) {
      if (var2.isEmpty() && var1.isEmpty()) {
         return true;
      }

      net.minecraft.world.item.ItemStack var3 = var1.copy();
      if (var3.getMaxDamage() > 0) {
         var3.setDamageValue(var3.getDamageValue());
      }

      return net.minecraft.world.item.ItemStack.isSameItemSameComponents(var3, var2);
   }

   private boolean method5454(net.minecraft.world.item.ItemStack var1) {
      return var1.getItem() == Items.AIR ? false : ItemRegistryBridgeGenericDup2.method3444(var1);
   }

   private boolean method5455(net.minecraft.world.item.ItemStack var1, net.minecraft.world.item.ItemStack var2) {
      if (var1 == null && var2 == null) {
         return false;
      }

      if (var1 != null && var2 != null) {
         if (var1.getItem() == Items.AIR && var2.getItem() == Items.AIR) {
            return false;
         }

         if (var1.getDamageValue() != var2.getDamageValue()) {
            return false;
         }

         if (var1.has(DataComponents.CUSTOM_DATA) && var2.has(DataComponents.CUSTOM_DATA)) {
            CompoundTag var3 = ((CustomData)var1.get(DataComponents.CUSTOM_DATA)).getUnsafe();
            CompoundTag var4 = ((CustomData)var2.get(DataComponents.CUSTOM_DATA)).getUnsafe();
            if (var3.contains("itemsadder") && var4.contains("itemsadder")) {
               CompoundTag var5 = (CompoundTag)var3.getCompound("itemsadder").get();
               CompoundTag var6 = (CompoundTag)var4.getCompound("itemsadder").get();
               return var5.getString("namespace").equals(var6.getString("namespace")) && var5.getString("id").equals(var6.getString("id"));
            }
         }

         return false;
      } else {
         return false;
      }
   }
}
