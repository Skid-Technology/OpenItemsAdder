package dev.lone.itemsadder.util;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SyntheticBlockPlaceEvent extends BlockPlaceEvent {
   public SyntheticBlockPlaceEvent(Block var1, BlockState var2, Block var3, ItemStack var4, Player var5, boolean var6) {
      this(var1, var2, var3, var4, var5, var6, EquipmentSlot.HAND);
   }

   public SyntheticBlockPlaceEvent(Block var1, BlockState var2, Block var3, ItemStack var4, Player var5, boolean var6, EquipmentSlot var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }
}
