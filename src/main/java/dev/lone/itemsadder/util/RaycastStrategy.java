package dev.lone.itemsadder.util;

import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public interface RaycastStrategy {
   RaycastResult traceBlocks(Object var1, Object var2, Object var3, HashSet var4);

   RaycastResult traceEntity(Entity var1, HashSet var2);

   @Nullable
   Vector traceVector(Location var1, Location var2, boolean var3);
}
