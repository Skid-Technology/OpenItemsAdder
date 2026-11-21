package dev.lone.itemsadder.block;

import org.bukkit.Location;
import org.bukkit.block.Block;

public interface BlockPlacementRule {
   boolean placeBlock(Block var1, boolean var2);

   boolean placeBlockAt(Location var1);

   boolean placeBlockNoPhysics(Block var1);

   boolean placeBlockDefault(Block var1);

   boolean placeBlockWithPhysics(Block var1, boolean var2);
}
