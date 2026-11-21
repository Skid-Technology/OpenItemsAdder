package dev.lone.itemsadder.nms;

import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface NmsWorldAdapter {
   Object method3193(String var1, String var2);

   String bj(Block var1);

   void method3194(Block var1, String var2, String var3);

   void method3195(Location var1, String var2, String var3, boolean var4);

   void method3196(Chunk var1, Player var2);

   default void method3197(World var1, int var2, int var3, Player var4) {
      this.method3196(var1.getChunkAt(var2 >> 4, var3 >> 4), var4);
   }

   boolean method3198(List var1);
}
