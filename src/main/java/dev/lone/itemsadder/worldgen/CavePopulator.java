package dev.lone.itemsadder.worldgen;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import dev.lone.itemsadder.world.InstrumentRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class CavePopulator extends AbstractPopulator implements IaListener {
   private static final String aEd = "cave_decorator";
   public static List qj = new ArrayList();
   public static Map aEe = new ConcurrentHashMap();
   public int aEf;
   public int aEg;

   public CavePopulator(World var1) {
      super(var1);
   }

   @Override
   protected String Kc() {
      return "cave_decorator";
   }

   @Override
   public void start() {
      this.KO();
   }

   @Override
   public void KR() {
      IaListener.super.unregister();
      super.KR();
   }

   public void method1361(AttachedDecorator var1) {
      if (!this.aDJ.contains(var1)) {
         super.method1364(var1);
         if (this.aDJ.size() == 1) {
            this.aEf = var1.aDz;
            this.aEg = var1.aDy;
         } else {
            this.aEf = Math.min(this.aEf, var1.aDz);
            this.aEg = Math.max(this.aEg, var1.aDy);
         }
      }
   }

   public boolean method1362(List var1, Chunk var2, Random var3, AttachedDecorator var4) {
      return this.method1363(var1, var2, var3, var4, this.KQ());
   }

   public boolean method1363(List var1, Chunk var2, Random var3, AttachedDecorator var4, long var5) {
      if (!this.method1498(var5)) {
         return false;
      }

      double var7 = Utils.randomDouble();
      if (var4.aDA != -1.0 && var7 * 100.0 > var4.aDA) {
         return false;
      }

      int var9 = var4.aDw > 0 ? var3.nextInt(var4.aDw) + 1 : 1;
      int var10 = 0;
      int var11 = var1.size();
      if (var11 == 0) {
         return false;
      }

      boolean var12 = false;
      int var15 = var3.nextInt(var11);

      for (int var16 = 0; var16 < var11 && var10 < var9; var16++) {
         if (!this.method1498(var5)) {
            return var12;
         }

         Block var13 = (Block)var1.get(var15);
         if (++var15 == var11) {
            var15 = 0;
         }

         int var17 = var13.getY();
         if (var17 >= var4.aDz && var17 <= var4.aDy) {
            Block var14 = var13.getRelative(BlockFace.DOWN);
            if (var4.aDD || !InstrumentRegistry.aT(var14)) {
               Block var18 = var4.aDR == AttachFace.CEILING ? var13.getRelative(BlockFace.UP) : var14;
               if (var18.getType().isSolid()) {
                  if (this.aP(var13) && var4.aL(var13) && var4.method2312(var18.getType())) {
                     this.method1499(var4, var13, var4.aDR == AttachFace.CEILING ? BlockFace.DOWN : BlockFace.UP, var5);
                     var12 = true;
                     var10++;
                  }

                  if (var12 && Settings.get().logCaveDecoratorGen) {
                     Log.log(
                        "Generated "
                           + (var4.aDv == null ? var4.aDu : var4.aDv).getNamespacedID()
                           + " cave decoration at "
                           + BlockFaceUtils.blockCoordsString(var13)
                           + " in world "
                           + var2.getWorld().getName()
                           + " "
                           + BlockFaceUtils.tpCommandForBlock(var13)
                           + " - scanned "
                           + var1.size()
                           + " AIR blocks."
                     );
                  }
               }
            }
         }
      }

      return var12;
   }

   private boolean aP(Block block) {
      return block.getType().isAir() || InstrumentRegistry.getAdapter().method2800(block.getType());
   }
}
