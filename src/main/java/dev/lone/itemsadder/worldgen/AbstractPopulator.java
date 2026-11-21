package dev.lone.itemsadder.worldgen;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPopulator extends org.bukkit.generator.BlockPopulator {
   private static int aDG = 0;
   final int aDH;
   public final WeakReference aDI;
   public final List aDJ = new ArrayList();
   private volatile List aDK = List.of();
   private volatile long field31 = -1L;
   private volatile boolean cV;
   final int aDL;
   final int aDM;

   public AbstractPopulator(World var1) {
      this.aDH = aDG++;
      this.aDI = new WeakReference<>(var1);
      this.aDM = var1.getMaxHeight() - 1;
      this.aDL = var1.getMinHeight();
   }

   public void method1364(AbstractDecorator var1) {
      if (!this.aDJ.contains(var1)) {
         this.aDJ.add(var1);
         if (Settings.get().isLoggingEnabled("logging.world_generation." + this.KN() + ".loading")) {
            World var2 = (World)this.aDI.get();
            if (var2 != null) {
               Log.log("[" + this.Kc() + "] '" + var1.yK + "' registered in world '" + var2.getName() + "'. File: " + var1.aDx);
            }
         }
      }
   }

   public String KN() {
      return switch (this.Kc()) {
         case "block_populator" -> "blocks_populators";
         case "surface_decorator" -> "surface_decorators";
         case "tree_populator" -> "trees_populators";
         case "cave_decorator" -> "cave_decorators";
         default -> {
            Log.error("Unknown type name: " + this.Kc());
            yield "unknown";
         }
      };
   }

   protected abstract String Kc();

   @Nullable
   public World ez() {
      return this.aDI == null ? null : (World)this.aDI.get();
   }

   public abstract void start();

   protected final boolean KO() {
      if (this.cV) {
         return false;
      }

      ContentManager var1 = ContentManager.method8084();
      this.field31 = var1 == null ? -1L : var1.method8114();
      this.aDK = List.copyOf(this.aDJ);
      this.cV = true;
      return true;
   }

   public final List KP() {
      return this.aDK;
   }

   public final long KQ() {
      return this.field31;
   }

   public final boolean method1498(long var1) {
      ContentManager var3 = ContentManager.method8084();
      return this.cV && var1 == this.field31 && var3 != null && var3.method8110() && var3.method8115(var1);
   }

   protected final boolean method1499(AbstractDecorator var1, Block var2, BlockFace var3, long var4) {
      if (var1.aDv == null) {
         return var1.method2314(var2, var3);
      }

      Location var6 = var2.getLocation();
      Material var7 = var2.getType();
      Bukkit.getScheduler().runTask(Main.getPlugin(), () -> {
         World var7x = var6.getWorld();
         if (this.method1498(var4) && var7x != null && var7x.isChunkLoaded(var6.getBlockX() >> 4, var6.getBlockZ() >> 4) && var6.getBlock().getType() == var7) {
            var1.method2314(var6.getBlock(), var3);
         }
      });
      return true;
   }

   public void KR() {
      this.cV = false;
      World var1 = (World)this.aDI.get();
      if (var1 != null) {
         var1.getPopulators().removeIf(var1x -> !(var1x instanceof AbstractPopulator) ? false : this.aDH == ((AbstractPopulator)var1x).aDH);
      }
   }
}
