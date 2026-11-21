package dev.lone.itemsadder.worldgen;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class BlockPopulator extends AbstractPopulator {
   public static final String aEb = "block_populator";
   public static List qj = new ArrayList();
   public final Map aEc = new HashMap();

   public BlockPopulator(World var1) {
      super(var1);
   }

   @Override
   protected String Kc() {
      return "block_populator";
   }

   public void method1570(BlockDecorator var1) {
      if (!this.aDJ.contains(var1)) {
         super.method1364(var1);
         this.aEc.computeIfAbsent(var1.aDu.aol, var0 -> new ArrayList<>()).add(var1);
      }
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

   public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
      long var4 = this.KQ();
      if (this.method1498(var4)) {
         for (BlockDecorator var7 : this.KP()) {
            if (!this.method1498(var4)) {
               return;
            }

            if (var7.aDA == -1.0 || !(random.nextDouble() * 100.0 > var7.aDA)) {
               this.method1571(random, var7, chunk, var4);
            }
         }
      }
   }

   private void method1571(@NotNull Random var1, BlockDecorator var2, @NotNull Chunk var3, long var4) {
      for (int var6 = 0; var6 < var2.aDO; var6++) {
         if (!this.method1498(var4)) {
            return;
         }

         this.method1572(var3, var1, var2, var4);
      }
   }

   private void method1572(Chunk var1, Random var2, BlockDecorator var3, long var4) {
      int var6 = var2.nextInt(16);
      int var7 = Utils.randomIntWith(var2, var3.aDz, var3.aDy);
      var7 = this.bT(var7);
      int var8 = var2.nextInt(16);

      for (int var9 = 0; var9 < var3.aDw; var9++) {
         if (!this.method1498(var4)) {
            return;
         }

         int var10 = var6 + var2.nextInt(Math.max(var3.aDw / 2, 2)) - var3.aDw / 4;
         var10 &= 15;
         int var11 = var7 + var2.nextInt(Math.max(var3.aDw / 4, 2)) - var3.aDw / 8;
         var11 = this.bT(var11);
         int var12 = var8 + var2.nextInt(Math.max(var3.aDw / 2, 2)) - var3.aDw / 4;
         var12 &= 15;
         Block var13 = var1.getBlock(var10, var11, var12);
         this.method1573(var13, var1, var3);
      }
   }

   private void method1573(Block var1, Chunk var2, BlockDecorator var3) {
      if (var3.aN(var1) && var3.aL(var1)) {
         var3.aM(var1);
         if (Settings.get().logBlocksPopulatorGen) {
            Log.log(
               "Spawned "
                  + var3.aDu.getNamespacedID()
                  + " custom block at "
                  + BlockFaceUtils.blockCoordsString(var1)
                  + " in world "
                  + var2.getWorld().getName()
                  + " "
                  + BlockFaceUtils.tpCommandForBlock(var1)
            );
         }
      }
   }

   int bT(int var1) {
      if (var1 > this.aDM) {
         return this.aDM;
      } else {
         return var1 < this.aDL ? this.aDL : var1;
      }
   }
}
