package dev.lone.itemsadder.loot;

import beer.devs.nativepackets.api.MerchantOffersPacketView;
import beer.devs.nativepackets.api.NativeValue;
import dev.lone.itemsadder.item.ItemRegistryBridgeV1_21_R7;
import dev.lone.itemsadder.util.IntPair;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.inventory.InventoryMerchant;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.trading.IMerchant;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantRecipe;
import net.minecraft.world.item.trading.MerchantRecipeList;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftInventoryMerchant;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftMerchant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;

public class MerchantOfferInterceptorImplD implements MerchantOfferInterceptor {
   @Override
   public boolean method5384(MerchantInventory inventory, int recipeIndex, int ingredientSlot, ItemStack candidate) {
      MerchantRecipe var5 = (MerchantRecipe)((CraftMerchant)inventory.getMerchant()).getMerchant().b().get(recipeIndex);
      ItemCost var6 = ingredientSlot == 0 ? var5.c : (ItemCost)var5.d.orElse(null);
      return var6 != null && var6.a(CraftItemStack.asNMSCopy(candidate));
   }

   @Override
   public void method5383(Player player, MerchantOffersPacketView packet) {
      MerchantRecipeList var3 = (MerchantRecipeList)packet.offers().nativeHandle();
      if (!var3.isEmpty()) {
         this.method5422(packet, player, var3);
         MerchantOfferInterceptorHolder.bpV.put(player, true);
      }
   }

   public MerchantRecipeList method5396(MerchantRecipeList var1) {
      MerchantRecipeList var2 = new MerchantRecipeList();

      for (MerchantRecipe var4 : var1) {
         var2.add(var4.v());
      }

      return var2;
   }

   private synchronized void method5422(MerchantOffersPacketView var1, Player var2, MerchantRecipeList var3) {
      if (var2.getOpenInventory().getTopInventory() instanceof CraftInventoryMerchant) {
         MerchantRecipeList var4 = this.method5396(var3);
         EntityPlayer var5 = ((CraftPlayer)var2).getHandle();
         PlayerInventory var6 = var5.gK();
         ArrayList var7 = new ArrayList();

         for (net.minecraft.world.item.ItemStack var9 : var6.getContents()) {
            if (this.method5424(var9)) {
               var7.add(var9);
            }
         }

         CraftInventoryMerchant var22 = (CraftInventoryMerchant)var2.getOpenInventory().getTopInventory();
         IMerchant var23 = ((CraftMerchant)var22.getMerchant()).getMerchant();
         IntPair var10 = (IntPair)bpR.getIfPresent(var2);
         if (var10 != null) {
            int var11 = 0;
            boolean var12 = false;

            for (Iterator var13 = var4.iterator(); var13.hasNext(); var11++) {
               var13.next();
               if (var11 > var10.zU && var11 < var23.b().size()) {
                  var13.remove();
                  var23.b().remove(var11);
                  var12 = true;
               }
            }

            if (var12) {
               bpR.invalidate(var2);
            }

            if (var2.getOpenInventory().getTopInventory() instanceof MerchantInventory) {
               InventoryMerchant var26 = ((CraftInventoryMerchant)var2.getOpenInventory().getTopInventory()).getInventory();
               net.minecraft.world.item.ItemStack var14 = var26.a(0);
               if (this.method5424(var14)) {
                  var7.add(var14);
               }

               net.minecraft.world.item.ItemStack var15 = var26.a(1);
               if (this.method5424(var15)) {
                  var7.add(var15);
               }
            }
         }

         if (!var7.isEmpty()) {
            int var24 = var4.size() - 1;
            ArrayList var25 = new ArrayList();
            int var27 = 0;

            for (int var29 = var4.size(); var27 < var29; var27++) {
               MerchantRecipe var30 = (MerchantRecipe)var4.get(var27);
               net.minecraft.world.item.ItemStack var16 = var30.a();
               net.minecraft.world.item.ItemStack var17 = var30.c();
               net.minecraft.world.item.ItemStack var18 = null;
               net.minecraft.world.item.ItemStack var19 = null;

               for (net.minecraft.world.item.ItemStack var21 : var7) {
                  if (this.method5425(var16, var21) && !this.method5423(var21, var16)) {
                     var18 = var21;
                  }

                  if (this.method5425(var17, var21) && !this.method5423(var21, var17)) {
                     var19 = var21;
                  }
               }

               if (var18 != null || var19 != null) {
                  MerchantRecipe var31 = var30.v();
                  if (var18 != null) {
                     var31.c = new ItemCost(var18.h().e(), var16.N(), DataComponentExactPredicate.a(var18.a()));
                  }

                  if (var19 != null) {
                     var31.d = Optional.of(new ItemCost(var19.h().e(), var17.N(), DataComponentExactPredicate.a(var19.a())));
                  }

                  var25.add(var31);
               }
            }

            if (!var25.isEmpty()) {
               bpR.put(var2, new IntPair(var1.containerId(), var24));
               var23.b().addAll(var25);
               NativeValue var28 = () -> var4;
               var1.offers(var28);
            }
         }
      }
   }

   private boolean method5423(net.minecraft.world.item.ItemStack var1, net.minecraft.world.item.ItemStack var2) {
      if (var2.f() && var1.f()) {
         return true;
      }

      net.minecraft.world.item.ItemStack var3 = var1.v();
      if (var3.p() > 0) {
         var3.b(var3.o());
      }

      return net.minecraft.world.item.ItemStack.c(var3, var2);
   }

   private boolean method5424(net.minecraft.world.item.ItemStack var1) {
      return var1.h() == Items.AIR ? false : ItemRegistryBridgeV1_21_R7.method3444(var1);
   }

   private boolean method5425(net.minecraft.world.item.ItemStack var1, net.minecraft.world.item.ItemStack var2) {
      if (var1 == null && var2 == null) {
         return false;
      }

      if (var1 != null && var2 != null) {
         if (var1.h() == Items.AIR && var2.h() == Items.AIR) {
            return false;
         }

         if (var1.o() != var2.o()) {
            return false;
         }

         if (var1.c(DataComponents.CUSTOM_DATA) && var2.c(DataComponents.CUSTOM_DATA)) {
            NBTTagCompound var3 = ((CustomData)var1.a(DataComponents.CUSTOM_DATA)).getUnsafe();
            NBTTagCompound var4 = ((CustomData)var2.a(DataComponents.CUSTOM_DATA)).getUnsafe();
            if (var3.b("itemsadder") && var4.b("itemsadder")) {
               NBTTagCompound var5 = (NBTTagCompound)var3.m("itemsadder").get();
               NBTTagCompound var6 = (NBTTagCompound)var4.m("itemsadder").get();
               return var5.i("namespace").equals(var6.i("namespace")) && var5.i("id").equals(var6.i("id"));
            }
         }

         return false;
      } else {
         return false;
      }
   }
}
