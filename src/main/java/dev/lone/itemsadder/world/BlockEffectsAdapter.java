package dev.lone.itemsadder.world;

import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.loot.NbtDropTrigger;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BlockEffectsAdapter {
   default boolean handlesEvent(Event event) {
      return false;
   }

   void placeTileBlock(Block var1, CustomBlockDefinition var2);

   @Nullable
   Sound getBreakSound(Material var1);

   @Nullable
   Sound getPlaceSound(Material var1);

   @Nullable
   Sound getHitSound(Material var1);

   @Nullable
   Sound getStepSound(Material var1);

   Sound getFallSound(Material var1);

   void updateNeighborShapes(Block var1);

   void notifyBlockUpdate(Block var1);

   void setObserverPowered(Block var1, boolean var2);

   boolean replaceFluidsAround(Block var1);

   void setLightBlocks(Block var1, int var2);

   default void clearLightBlocks(Block var1) {
      this.setLightBlocks(var1, 0);
   }

   boolean matchesNbtDrop(Block var1, NbtDropTrigger var2);

   boolean isHarvestableWith(Block var1, ItemStack var2);

   String getMinecraftId(@NotNull Block var1);
}
