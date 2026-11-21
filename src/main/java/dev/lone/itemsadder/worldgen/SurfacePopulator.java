package dev.lone.itemsadder.worldgen;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import dev.lone.itemsadder.world.InstrumentRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.HeightMap;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public class SurfacePopulator extends AbstractPopulator {
   public static final String aEh = "surface_decorator";
   public static List qj = new ArrayList();
   private final boolean aEi;
   public Map aEc = new HashMap();

   public SurfacePopulator(World var1) {
      super(var1);
      this.aEi = var1.getEnvironment() == Environment.NETHER;
   }

   @Override
   protected String Kc() {
      return "surface_decorator";
   }

   @Override
   public void start() {
      World var1 = this.ez();
      if (var1 == null) {
         Log.error("World is null in SurfaceDecorator. This is a bug.");
      } else if (this.KO()) {
         var1.getPopulators().add(this);
      }
   }

   @Override
   public void KR() {
      super.KR();
   }

   public void method1575(SurfaceFeature var1) {
      if (!this.aDJ.contains(var1)) {
         super.method1364(var1);
         int var2 = var1.aDv == null ? var1.aDu.aol : var1.aDv.aol;
         this.aEc.computeIfAbsent(var2, var0 -> new ArrayList<>()).add(var1);
      }
   }

   public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk source) {
      long var4 = this.KQ();
      if (this.method1498(var4)) {
         for (SurfaceFeature var7 : this.KP()) {
            if (!this.method1498(var4)) {
               return;
            }

            this.method1576(source, random, var7, var4);
         }
      }
   }

   private boolean method1576(Chunk var1, Random var2, SurfaceFeature var3, long var4) {
      double var6 = Utils.randomDouble();
      if (var3.aDA != -1.0 && var6 * 100.0 > var3.aDA) {
         return false;
      }

      int var8 = var2.nextInt(15);
      int var9 = var2.nextInt(15);
      boolean var10 = false;
      boolean var11 = false;
      int var12 = 0;
      Block var13 = null;
      Block var14 = null;
      int var15 = 0;
      int var16 = 0;
      int var17 = 0;

      for (int var18 = 0; var18 < var3.aDw; var18++) {
         if (!this.method1498(var4)) {
            return var10;
         }

         while (!var11 && var12 < 8) {
            if (!this.method1498(var4)) {
               return var10;
            }

            var12++;
            var15 = var8 + var2.nextInt(method1577(var3.aDw / 2, 1)) - var3.aDw / 4;
            var15 = bU(var15);
            var15 &= 15;
            var17 = var9 + var2.nextInt(method1577(var3.aDw / 2, 1)) - var3.aDw / 4;
            var17 = bU(var17);
            var17 &= 15;
            if (this.aEi) {
               var16 = (int)(var1.getWorld().getMaxHeight() / 4.0F);
            } else {
               var16 = InstrumentRegistry.getHeightAt(var1, var15, var17, HeightMap.MOTION_BLOCKING_NO_LEAVES) + 1;
            }

            if (var16 <= var3.aDy && var16 >= var3.aDz) {
               var13 = var1.getBlock(var15, var16, var17);
               var14 = var13.getRelative(BlockFace.DOWN);
               if (!var3.aDD && InstrumentRegistry.aT(var14)) {
                  var11 = false;
               } else {
                  var11 = this.aP(var13) && var3.aL(var13) && var3.method2312(var14.getType());
               }
            } else {
               var11 = false;
            }
         }

         if (var11) {
            this.method1499(var3, var13, BlockFace.UP, var4);
            if (Settings.get().logSurfaceDecoratorGen) {
               Log.log(
                  "Generated "
                     + (var3.aDv == null ? var3.aDu : var3.aDv).getNamespacedID()
                     + " surface decoration at "
                     + BlockFaceUtils.blockCoordsString(var1.getBlock(var15, var16, var17))
                     + " in world "
                     + var1.getWorld().getName()
                     + " "
                     + BlockFaceUtils.tpCommandForBlock(var1.getBlock(var15, var16, var17))
               );
            }
         }

         var12 = 0;
         var11 = false;
      }

      return var10;
   }

   private boolean aP(Block block) {
      return block.getType().isAir() || InstrumentRegistry.getAdapter().method2800(block.getType());
   }

   public static int method1577(int var0, int var1) {
      return var0 <= 0 ? var1 : var0;
   }

   public static int bU(int var0) {
      return var0 < 0 ? 0 : Math.min(var0, 15);
   }
}
