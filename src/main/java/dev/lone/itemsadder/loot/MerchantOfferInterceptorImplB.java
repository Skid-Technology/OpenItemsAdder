package dev.lone.itemsadder.loot;

import beer.devs.nativepackets.api.MerchantOffersPacketView;
import beer.devs.nativepackets.api.NativeValue;
import dev.lone.itemsadder.item.ItemRegistryBridgeV1_21_R1;
import dev.lone.itemsadder.util.IntPair;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import net.minecraft.core.component.DataComponentPredicate;
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
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftInventoryMerchant;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftMerchant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;

public class MerchantOfferInterceptorImplB implements MerchantOfferInterceptor {
   @Override
   public boolean method5384(MerchantInventory inventory, int recipeIndex, int ingredientSlot, ItemStack candidate) {
      MerchantRecipe var5 = (MerchantRecipe)((CraftMerchant)inventory.getMerchant()).getMerchant().gm().get(recipeIndex);
      ItemCost var6 = ingredientSlot == 0 ? var5.c : (ItemCost)var5.d.orElse(null);
      return var6 != null && var6.a(CraftItemStack.asNMSCopy(candidate));
   }

   @Override
   public void method5383(Player player, MerchantOffersPacketView packet) {
      MerchantRecipeList var3 = (MerchantRecipeList)packet.offers().nativeHandle();
      if (!var3.isEmpty()) {
         this.method5397(packet, player, var3);
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

   private synchronized void method5397(MerchantOffersPacketView var1, Player var2, MerchantRecipeList var3) {
      if (var2.getOpenInventory().getTopInventory() instanceof CraftInventoryMerchant) {
         MerchantRecipeList var4 = this.method5396(var3);
         EntityPlayer var5 = ((CraftPlayer)var2).getHandle();
         PlayerInventory var6 = var5.fY();
         ArrayList var7 = new ArrayList();

         for (net.minecraft.world.item.ItemStack var9 : var6.getContents()) {
            if (this.method5399(var9)) {
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
               if (var11 > var10.zU && var11 < var23.gm().size()) {
                  var13.remove();
                  var23.gm().remove(var11);
                  var12 = true;
               }
            }

            if (var12) {
               bpR.invalidate(var2);
            }

            if (var2.getOpenInventory().getTopInventory() instanceof MerchantInventory) {
               InventoryMerchant var26 = ((CraftInventoryMerchant)var2.getOpenInventory().getTopInventory()).getInventory();
               net.minecraft.world.item.ItemStack var14 = var26.a(0);
               if (this.method5399(var14)) {
                  var7.add(var14);
               }

               net.minecraft.world.item.ItemStack var15 = var26.a(1);
               if (this.method5399(var15)) {
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
                  if (this.method5400(var16, var21) && !this.method5398(var21, var16)) {
                     var18 = var21;
                  }

                  if (this.method5400(var17, var21) && !this.method5398(var21, var17)) {
                     var19 = var21;
                  }
               }

               if (var18 != null || var19 != null) {
                  MerchantRecipe var31 = var30.v();
                  if (var18 != null) {
                     var31.c = new ItemCost(var18.g().o(), var16.H(), DataComponentPredicate.a(var18.a()));
                  }

                  if (var19 != null) {
                     var31.d = Optional.of(new ItemCost(var19.g().o(), var17.H(), DataComponentPredicate.a(var19.a())));
                  }

                  var25.add(var31);
               }
            }

            if (!var25.isEmpty()) {
               bpR.put(var2, new IntPair(var1.containerId(), var24));
               var23.gm().addAll(var25);
               NativeValue var28 = () -> var4;
               var1.offers(var28);
            }
         }
      }
   }

   private boolean method5398(net.minecraft.world.item.ItemStack var1, net.minecraft.world.item.ItemStack var2) {
      if (var2.e() && var1.e()) {
         return true;
      }

      net.minecraft.world.item.ItemStack var3 = var1.s();
      if (var3.o() > 0) {
         var3.b(var3.n());
      }

      return net.minecraft.world.item.ItemStack.c(var3, var2);
   }

   private boolean method5399(net.minecraft.world.item.ItemStack var1) {
      return var1.g() == Items.a ? false : ItemRegistryBridgeV1_21_R1.method3444(var1);
   }

   private boolean method5400(net.minecraft.world.item.ItemStack var1, net.minecraft.world.item.ItemStack var2) {
      if (var1 == null && var2 == null) {
         return false;
      }

      if (var1 != null && var2 != null) {
         if (var1.g() == Items.a && var2.g() == Items.a) {
            return false;
         }

         if (var1.n() != var2.n()) {
            return false;
         }

         if (var1.b(DataComponents.b) && var2.b(DataComponents.b)) {
            NBTTagCompound var3 = ((CustomData)var1.a(DataComponents.b)).d();
            NBTTagCompound var4 = ((CustomData)var2.a(DataComponents.b)).d();
            if (var3.e("itemsadder") && var4.e("itemsadder")) {
               NBTTagCompound var5 = var3.p("itemsadder");
               NBTTagCompound var6 = var4.p("itemsadder");
               return var5.l("namespace").equals(var6.l("namespace")) && var5.l("id").equals(var6.l("id"));
            }
         }

         return false;
      } else {
         return false;
      }
   }
}
