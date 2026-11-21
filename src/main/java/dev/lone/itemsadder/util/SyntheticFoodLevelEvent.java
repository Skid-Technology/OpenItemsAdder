package dev.lone.itemsadder.util;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SyntheticFoodLevelEvent extends FoodLevelChangeEvent {
   public SyntheticFoodLevelEvent(@NotNull HumanEntity var1, int var2) {
      super(var1, var2);
   }

   public SyntheticFoodLevelEvent(@NotNull HumanEntity var1, int var2, @Nullable ItemStack var3) {
      super(var1, var2, var3);
   }
}
