package dev.lone.itemsadder.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Mob;

public final class BlockSearchScanner {
   private static final Map aaO = new ConcurrentHashMap();
   private final List aaP;
   private final int aaQ;
   private Location Sd;
   private int aaR;

   public BlockSearchScanner(int var1, int var2, int var3) {
      int var4 = Math.min(16, var1);
      this.aaQ = var3;
      this.aaP = aaO.computeIfAbsent(var4 * 32 + var2, var2x -> {
         ArrayList var3x = new ArrayList();

         for (int var4x = -var4; var4x <= var4; var4x++) {
            for (int var5 = -var2; var5 <= var2; var5++) {
               for (int var6 = -var4; var6 <= var4; var6++) {
                  if (var4x * var4x + var6 * var6 <= var4 * var4) {
                     var3x.add(new int[]{var4x, var5, var6});
                  }
               }
            }
         }

         var3x.sort(Comparator.comparingInt(var0x -> var0x[0] * var0x[0] + var0x[1] * var0x[1] + var0x[2] * var0x[2]));
         return List.copyOf(var3x);
      });
   }

   public Block method9629(Mob var1, Predicate var2) {
      if (this.Sd == null || this.Sd.getWorld() != var1.getWorld() || this.Sd.distanceSquared(var1.getLocation()) > 9.0) {
         this.Sd = var1.getLocation();
         this.aaR = 0;
      }

      for (int var3 = 0; var3 < this.aaQ && var3 < this.aaP.size(); var3++) {
         int[] var4 = (int[])this.aaP.get(this.aaR++);
         if (this.aaR == this.aaP.size()) {
            this.aaR = 0;
         }

         int var5 = this.Sd.getBlockX() + var4[0];
         int var6 = this.Sd.getBlockY() + var4[1];
         int var7 = this.Sd.getBlockZ() + var4[2];
         if (var6 >= var1.getWorld().getMinHeight() && var6 < var1.getWorld().getMaxHeight() && var1.getWorld().isChunkLoaded(var5 >> 4, var7 >> 4)) {
            Block var8 = var1.getWorld().getBlockAt(var5, var6, var7);
            if (var2.test(var8)) {
               return var8;
            }
         }
      }

      return null;
   }
}
