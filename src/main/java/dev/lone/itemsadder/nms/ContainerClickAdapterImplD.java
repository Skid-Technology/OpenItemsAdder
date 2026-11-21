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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerInventoryPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagNetworkSerialization.NetworkPayload;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.TooltipDisplay;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public final class ContainerClickAdapterImplD implements ContainerClickAdapter {
   private static final BundleContents bdx = new BundleContents(
      List.of(new ItemStackTemplate(Items.PAPER), new ItemStackTemplate(Items.PAPER), new ItemStackTemplate(Items.PAPER))
   );

   @Override
   public Object an(ItemStack item) {
      return CraftItemStack.asNMSCopy(item);
   }

   @Override
   public boolean method3554(Object item) {
      return ((net.minecraft.world.item.ItemStack)item).isEmpty();
   }

   @Override
   public ItemStack method3553(Object item) {
      return CraftItemStack.asBukkitCopy((net.minecraft.world.item.ItemStack)item);
   }

   @Override
   public Inventory method3552(InventoryHolder holder, Map contents, IntUnaryOperator slots) {
      return new CraftInventory(new BundleContainerD(this, holder, slots, contents));
   }

   @Override
   public long method3559(Object nativePacket, int window) {
      if (nativePacket instanceof ClientboundContainerSetContentPacket var11) {
         int var14 = var11.containerId();
         List var15 = var11.items();
         long var16 = 0L;

         for (int var8 = 54; var8 < 90 && var8 < var15.size(); var8++) {
            if (var14 == window) {
               var16 |= 1L << var8 - 54;
            }
         }

         for (int var17 = 9; var17 < 45 && var17 < var15.size(); var17++) {
            if (var14 == 0) {
               var16 |= 1L << var17 - 9;
            }
         }

         return var16;
      } else if (nativePacket instanceof ClientboundContainerSetSlotPacket var10) {
         int var13 = var10.getContainerId();
         int var5 = var10.getSlot();
         if (var13 == window && var5 >= 54 && var5 < 90) {
            return 1L << var5 - 54;
         } else if (var13 == 0 && var5 >= 9 && var5 < 45) {
            return 1L << var5 - 9;
         } else {
            return var13 == -2 && var5 >= 0 && var5 < 36 ? 1L << (var5 < 9 ? 27 + var5 : var5 - 9) : 0L;
         }
      } else if (nativePacket instanceof ClientboundSetPlayerInventoryPacket var9) {
         int var12 = var9.slot();
         return var12 >= 0 && var12 < 36 ? 1L << (var12 < 9 ? 27 + var12 : var12 - 9) : 0L;
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
      if (nativePacket instanceof ClientboundContainerSetContentPacket var3 && var3.containerId() == window) {
         return 18014398509481983L;
      } else if (nativePacket instanceof ClientboundContainerSetSlotPacket var8
         && var8.getContainerId() == window
         && var8.getSlot() >= 0
         && var8.getSlot() < 54) {
         return 1L << var8.getSlot();
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
      return nativePacket instanceof ClientboundBundlePacket var2 ? var2.subPackets() : null;
   }

   @Override
   public Object OQ() {
      return Registries.ITEM;
   }

   @Override
   public Object method3561(Object nativeTagMap) {
      HashMap var2 = new HashMap();
      ((Map)nativeTagMap).forEach((var1x, var2x) -> {
         FriendlyByteBuf var3 = new FriendlyByteBuf(Unpooled.buffer());

         try {
            var2x.write(var3);
            var2.put(var1x, NetworkPayload.read(var3));
         } finally {
            var3.release();
         }
      });
      return var2;
   }

   @Override
   public Object ab(Object nativeTagMap) {
      HashMap var2 = new HashMap((Map)nativeTagMap);
      NetworkPayload var3 = var2.getOrDefault(Registries.ITEM, NetworkPayload.EMPTY);
      FriendlyByteBuf var4 = new FriendlyByteBuf(Unpooled.buffer());

      try {
         var3.write(var4);
         HashMap var5 = new HashMap(var4.readMap(FriendlyByteBuf::readIdentifier, FriendlyByteBuf::readIntIdList));
         IntArrayList var6 = new IntArrayList();
         IntList var7 = (IntList)var5.get(ItemTags.BUNDLES.location());
         if (var7 != null) {
            var6.addAll(var7);
         }

         int var8 = BuiltInRegistries.ITEM.getId(Items.BARRIER);
         if (var6.contains(var8)) {
            return nativeTagMap;
         }

         var6.add(var8);
         var5.put(ItemTags.BUNDLES.location(), var6);
         var4.clear();
         var4.writeMap(var5, FriendlyByteBuf::writeIdentifier, FriendlyByteBuf::writeIntIdList);
         var2.put(Registries.ITEM, NetworkPayload.read(var4));
         return var2;
      } finally {
         var4.release();
      }
   }

   @Override
   public boolean ac(Object nativeItem) {
      return nativeItem instanceof net.minecraft.world.item.ItemStack var2 && !var2.isEmpty() && !(var2.getItem() instanceof BundleItem);
   }

   @Override
   public Object method3562(Object nativeItem, int revision) {
      net.minecraft.world.item.ItemStack var3 = (net.minecraft.world.item.ItemStack)nativeItem;
      if (!this.ac(var3)) {
         return var3;
      }

      net.minecraft.world.item.ItemStack var4 = new net.minecraft.world.item.ItemStack(Items.BARRIER, var3.getCount());
      var4.applyComponents(var3.getComponents());
      var4.set(DataComponents.ITEM_MODEL, (Identifier)var3.getOrDefault(DataComponents.ITEM_MODEL, BuiltInRegistries.ITEM.getKey(var3.getItem())));
      if (!var3.has(DataComponents.ITEM_NAME)) {
         var4.set(DataComponents.ITEM_NAME, var3.getHoverName());
      }

      var4.set(DataComponents.BUNDLE_CONTENTS, bdx);
      var4.set(DataComponents.NOTE_BLOCK_SOUND, Identifier.parse("_iainternal:gui/scroll_" + revision));
      return var4;
   }

   @Override
   public Object method3563(Object nativeItem, float offset, int revision, boolean showTooltip, String model, boolean fast) {
      net.minecraft.world.item.ItemStack var7 = new net.minecraft.world.item.ItemStack(Items.BARRIER);
      net.minecraft.world.item.ItemStack var8 = (net.minecraft.world.item.ItemStack)nativeItem;
      var7.applyComponents(var8.getComponents());
      boolean var9 = !showTooltip || !var8.has(DataComponents.CUSTOM_NAME);
      var7.set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(var9, TooltipDisplay.DEFAULT.hiddenComponents()));
      var7.set(DataComponents.ITEM_MODEL, Identifier.parse(model));
      var7.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(offset, fast ? 1.0F : 0.0F), List.of(), List.of(), List.of()));
      var7.set(DataComponents.BUNDLE_CONTENTS, bdx);
      var7.set(DataComponents.NOTE_BLOCK_SOUND, Identifier.parse("_iainternal:gui/scroll_" + revision));
      return var7;
   }
}
