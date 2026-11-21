package dev.lone.itemsadder.nms;

import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.PacketPlayOutSetSlot;
import net.minecraft.network.protocol.game.PacketPlayOutWindowItems;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.tags.TagsItem;
import net.minecraft.tags.TagNetworkSerialization.a;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.TooltipDisplay;
import org.bukkit.craftbukkit.v1_21_R5.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_21_R5.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class ContainerClickAdapterImplA implements ContainerClickAdapter {
   private static final BundleContents bdc = new BundleContents(List.of(new ItemStack(Items.rV), new ItemStack(Items.rV), new ItemStack(Items.rV)));

   @Override
   public Object an(org.bukkit.inventory.ItemStack item) {
      return CraftItemStack.asNMSCopy(item);
   }

   @Override
   public boolean method3554(Object item) {
      return ((ItemStack)item).f();
   }

   @Override
   public org.bukkit.inventory.ItemStack method3553(Object item) {
      return CraftItemStack.asBukkitCopy((ItemStack)item);
   }

   @Override
   public Inventory method3552(InventoryHolder holder, Map contents, IntUnaryOperator slots) {
      return new CraftInventory(new BundleInventoryA(this, holder, slots, contents));
   }

   @Override
   public long method3559(Object nativePacket, int window) {
      if (nativePacket instanceof PacketPlayOutWindowItems var10) {
         int var12 = var10.b();
         List var13 = var10.f();
         long var14 = 0L;

         for (int var8 = 54; var8 < 90 && var8 < var13.size(); var8++) {
            if (var12 == window) {
               var14 |= 1L << var8 - 54;
            }
         }

         for (int var15 = 9; var15 < 45 && var15 < var13.size(); var15++) {
            if (var12 == 0) {
               var14 |= 1L << var15 - 9;
            }
         }

         return var14;
      } else if (nativePacket instanceof PacketPlayOutSetSlot var9) {
         int var11 = var9.b();
         int var5 = var9.e();
         if (var11 == window && var5 >= 54 && var5 < 90) {
            return 1L << var5 - 54;
         } else if (var11 == 0 && var5 >= 9 && var5 < 45) {
            return 1L << var5 - 9;
         } else {
            return var11 == -2 && var5 >= 0 && var5 < 36 ? 1L << (var5 < 9 ? 27 + var5 : var5 - 9) : 0L;
         }
      } else {
         Iterable var3 = this.method3560(nativePacket);
         if (var3 == null) {
            return 0L;
         }

         long var4 = 0L;

         for (Object var7 : var3) {
            var4 |= this.method3559(var7, window);
         }

         return var4;
      }
   }

   @Override
   public long method3558(Object nativePacket, int window) {
      if (nativePacket instanceof PacketPlayOutWindowItems var3 && var3.b() == window) {
         return 18014398509481983L;
      } else if (nativePacket instanceof PacketPlayOutSetSlot var8 && var8.b() == window && var8.e() >= 0 && var8.e() < 54) {
         return 1L << var8.e();
      } else {
         Iterable var9 = this.method3560(nativePacket);
         if (var9 == null) {
            return 0L;
         }

         long var4 = 0L;

         for (Object var7 : var9) {
            var4 |= this.method3558(var7, window);
         }

         return var4;
      }
   }

   @Override
   public Iterable method3560(Object nativePacket) {
      return nativePacket instanceof ClientboundBundlePacket var2 ? var2.b() : null;
   }

   @Override
   public Object OQ() {
      return Registries.N;
   }

   @Override
   public Object method3561(Object nativeTagMap) {
      HashMap var2 = new HashMap();
      ((Map)nativeTagMap).forEach((var1x, var2x) -> {
         PacketDataSerializer var3 = new PacketDataSerializer(Unpooled.buffer());

         try {
            var2x.a(var3);
            var2.put(var1x, a.b(var3));
         } finally {
            var3.release();
         }
      });
      return var2;
   }

   @Override
   public Object ab(Object nativeTagMap) {
      HashMap var2 = new HashMap((Map)nativeTagMap);
      a var3 = var2.getOrDefault(Registries.N, a.a);
      PacketDataSerializer var4 = new PacketDataSerializer(Unpooled.buffer());

      try {
         var3.a(var4);
         HashMap var5 = new HashMap(var4.a(PacketDataSerializer::q, PacketDataSerializer::a));
         IntArrayList var6 = new IntArrayList();
         IntList var7 = (IntList)var5.get(TagsItem.bV.b());
         if (var7 != null) {
            var6.addAll(var7);
         }

         int var8 = BuiltInRegistries.g.a(Items.if_);
         if (var6.contains(var8)) {
            return nativeTagMap;
         }

         var6.add(var8);
         var5.put(TagsItem.bV.b(), var6);
         var4.y();
         var4.a(var5, PacketDataSerializer::a, PacketDataSerializer::a);
         var2.put(Registries.N, a.b(var4));
         return var2;
      } finally {
         var4.release();
      }
   }

   @Override
   public boolean ac(Object nativeItem) {
      return nativeItem instanceof ItemStack var2 && !var2.f() && !(var2.h() instanceof BundleItem);
   }

   @Override
   public Object method3562(Object nativeItem, int revision) {
      ItemStack var3 = (ItemStack)nativeItem;
      if (!this.ac(var3)) {
         return var3;
      }

      ItemStack var4 = new ItemStack(Items.if_, var3.M());
      var4.b(var3.a());
      var4.b(DataComponents.i, (MinecraftKey)var3.a(DataComponents.i, BuiltInRegistries.g.b(var3.h())));
      if (!var3.c(DataComponents.h)) {
         var4.b(DataComponents.h, var3.y());
      }

      var4.b(DataComponents.Q, bdc);
      var4.b(DataComponents.al, MinecraftKey.a("_iainternal:gui/scroll_" + revision));
      return var4;
   }

   @Override
   public Object method3563(Object nativeItem, float offset, int revision, boolean showTooltip, String model, boolean fast) {
      ItemStack var7 = new ItemStack(Items.if_);
      ItemStack var8 = (ItemStack)nativeItem;
      var7.b(var8.a());
      boolean var9 = !showTooltip || !var8.c(DataComponents.g);
      var7.b(DataComponents.q, new TooltipDisplay(var9, TooltipDisplay.c.b()));
      var7.b(DataComponents.i, MinecraftKey.a(model));
      var7.b(DataComponents.p, new CustomModelData(List.of(offset, fast ? 1.0F : 0.0F), List.of(), List.of(), List.of()));
      var7.b(DataComponents.Q, bdc);
      var7.b(DataComponents.al, MinecraftKey.a("_iainternal:gui/scroll_" + revision));
      return var7;
   }
}
