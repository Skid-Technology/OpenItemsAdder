package dev.lone.itemsadder.worldgen;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class TreePopulator extends AbstractPopulator {
   public static final String aEj = "tree_populator";
   public static List qj = new ArrayList();
   public static HashSet aEk = new HashSet();

   public TreePopulator(World var1) {
      super(var1);
   }

   @Override
   protected String Kc() {
      return "tree_populator";
   }

   public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk source) {
      long var4 = this.KQ();
      if (this.method1498(var4)) {
         for (TreeDecorator var7 : this.KP()) {
            if (!this.method1498(var4)) {
               return;
            }

            for (int var8 = 0; var8 < var7.aDV; var8++) {
               if (!this.method1498(var4)) {
                  return;
               }

               this.method1581(source, random, var7, var4);
            }
         }
      }
   }

   public static boolean method1579(int var0, int var1) {
      return aEk.contains(method1580(var0, var1));
   }

   @Override
   public void start() {
      World var1 = this.ez();
      if (var1 == null) {
         Log.error("World is null in WorldTreePopulator. This is a bug.");
      } else if (this.KO()) {
         var1.getPopulators().add(this);
      }
   }

   @Override
   public void KR() {
      super.KR();
   }

   private static int method1580(int var0, int var1) {
      return var1 ^ var0 << 16;
   }

   private boolean method1581(Chunk var1, Random var2, TreeDecorator var3, long var4) {
      double var6 = Utils.randomDouble();
      if (var3.aDA != -1.0 && var6 * 100.0 > var3.aDA) {
         return false;
      }

      int var8 = var2.nextInt(15);
      int var9 = 1;
      if (var3.aDz != 0 || var3.aDy != 0) {
         var9 = Utils.randomIntWith(var2, var3.aDz, var3.aDy);
      }

      var9 = this.bV(var9);
      int var10 = var2.nextInt(15);
      aEk.add(method1580(var1.getX(), var1.getZ()));
      boolean var11 = false;

      try {
         for (int var13 = 0; var13 < var3.aDw; var13++) {
            if (!this.method1498(var4)) {
               return var11;
            }

            int var14 = var8 + var2.nextInt(this.method1577(var3.aDw / 2, 1)) - var3.aDw / 4;
            var14 = this.bU(var14);
            var14 &= 15;
            int var15 = var9 + var2.nextInt(this.method1577(var3.aDw / 4, 1)) - var3.aDw / 8;
            var15 = this.bV(var15);
            int var16 = var10 + var2.nextInt(this.method1577(var3.aDw / 2, 1)) - var3.aDw / 4;
            var16 = this.bU(var16);
            var16 &= 15;
            Block var12 = var1.getBlock(var14, var15, var16);
            if (var3.aL(var12) && var3.aO(var12) && Settings.get().logTreesPopulatorGen) {
               Log.log(
                  "Spawned "
                     + var3.aDu.getNamespacedID()
                     + " tree at "
                     + BlockFaceUtils.blockCoordsString(var12)
                     + " in world "
                     + var1.getWorld().getName()
                     + " "
                     + BlockFaceUtils.tpCommandForBlock(var12)
               );
            }
         }
      } finally {
         aEk.remove(method1580(var1.getX(), var1.getZ()));
      }

      return var11;
   }

   int method1577(int var1, int var2) {
      return var1 <= 0 ? var2 : var1;
   }

   int bU(int var1) {
      return var1 < 0 ? 0 : Math.min(var1, 15);
   }

   int bV(int var1) {
      if (var1 > this.aDM) {
         return this.aDM;
      } else {
         return var1 < this.aDL ? this.aDL : var1;
      }
   }
}
