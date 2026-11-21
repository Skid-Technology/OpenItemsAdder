package dev.lone.itemsadder.nms;

import dev.lone.itemsadder.util.BlockFaceUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public interface BlockSetterAdapter {
   void bk(Block var1);

   boolean method3401(Block var1, Material var2);

   default boolean method3402(Block var1, Material var2, boolean var3) {
      boolean var4 = this.method3401(var1, var2);
      if (var4 && var3) {
         Location var5 = var1.getLocation();
         BlockData var6 = var1.getBlockData();
         this.method3405(var5, var6);
      }

      return var4;
   }

   boolean setBlockData(Block var1, BlockData var2);

   default boolean method3403(Block var1, BlockData var2, boolean var3) {
      boolean var4 = this.setBlockData(var1, var2);
      if (var4 && var3) {
         Location var5 = var1.getLocation();
         this.method3405(var5, var2);
      }

      return var4;
   }

   boolean bl(Block var1);

   default boolean method3404(Block var1, boolean var2) {
      boolean var3 = this.bl(var1);
      if (var3 && var2) {
         Location var4 = var1.getLocation();
         BlockData var5 = var1.getBlockData();
         this.method3405(var4, var5);
      }

      return var3;
   }

   default void method3405(Location var1, BlockData var2) {
      for (Player var4 : BlockFaceUtils.playersInView(var1)) {
         var4.sendBlockChange(var1, var2);
      }
   }
}
