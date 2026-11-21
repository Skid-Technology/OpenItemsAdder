package dev.lone.itemsadder.world;

import dev.lone.itemsadder.util.BlockDataSetter;
import org.bukkit.Location;
import org.bukkit.TreeType;

public interface TreeFeatureHook {
   boolean growTree(Location var1, TreeType var2, BlockDataSetter var3, boolean var4);

   void injectDefaultBlockstateFeatures();

   void restoreDefaultBlockstateFeatures();
}
