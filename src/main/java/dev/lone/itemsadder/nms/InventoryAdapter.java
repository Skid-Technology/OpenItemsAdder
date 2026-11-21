package dev.lone.itemsadder.nms;

import java.util.Map;
import java.util.function.IntUnaryOperator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface InventoryAdapter {
   Inventory method3552(InventoryHolder var1, Map var2, IntUnaryOperator var3);

   Object an(ItemStack var1);

   ItemStack method3553(Object var1);

   default boolean method3554(Object item) {
      return this.method3553(item).getType().isAir();
   }
}
