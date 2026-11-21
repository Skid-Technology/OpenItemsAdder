package dev.lone.itemsadder.util;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

public interface BlockDataSetter {
   boolean setBlockData(Block var1, @NotNull BlockData var2);
}
