package dev.lone.itemsadder.nms;

import java.util.Map;
import java.util.function.IntUnaryOperator;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

final class InventoryAdapterV1_20_R4 implements InventoryAdapter {
   @Override
   public Object an(ItemStack item) {
      return CraftItemStack.asNMSCopy(item);
   }

   @Override
   public ItemStack method3553(Object item) {
      return CraftItemStack.asBukkitCopy((net.minecraft.world.item.ItemStack)item);
   }

   @Override
   public Inventory method3552(InventoryHolder holder, Map contents, IntUnaryOperator slots) {
      return new CraftInventory(new InventoryWrapperV1_20_R4(this, holder, slots, contents));
   }
}
