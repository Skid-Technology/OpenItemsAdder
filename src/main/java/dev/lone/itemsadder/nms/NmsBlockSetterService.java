package dev.lone.itemsadder.nms;

import dev.lone.itemsadder.util.BlockFaceUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

@Deprecated
public final class NmsBlockSetterService {
   private final BlockSetterAdapter aYj = (BlockSetterAdapter)NmsRegistry.method2777(BlockSetterAdapter.class);
   public static NmsBlockSetterService aYk;

   NmsBlockSetterService() {
   }

   public static NmsBlockSetterService OC() {
      if (aYk == null) {
         aYk = new NmsBlockSetterService();
      }

      return aYk;
   }

   public static BlockSetterAdapter OD() {
      return OC().aYj;
   }

   public static void method3400(Block var0, BlockData var1) {
      Location var2 = var0.getLocation();

      for (Player var5 : BlockFaceUtils.playersInView(var2)) {
         var5.sendBlockChange(var2, var1);
      }
   }
}
