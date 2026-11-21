package dev.lone.itemsadder.worldgen;

import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.FurnitureItemEntity;
import dev.lone.itemsadder.item.PlaceableItemSpec;
import dev.lone.itemsadder.world.ChunkBlockHookHolder;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDecorator {
   public String yK;
   @Nullable
   public final CustomBlockDefinition aDu;
   @Nullable
   public final CustomItemDefinition aDv;
   public final int aDw;
   public final String aDx;
   public final int aDy;
   public final int aDz;
   public final double aDA;
   public final HashSet aDB;
   public final HashSet aDC;
   public boolean aDD;
   public final List aDE;
   public final boolean aDF;

   protected AbstractDecorator(String var1, String var2, @NotNull List var3, CustomBlockDefinition var4, int var5, int var6, int var7, double var8) {
      this(var1, var2, var3, var4, null, var5, var6, var7, var8);
   }

   protected AbstractDecorator(
      String var1,
      String var2,
      @NotNull List var3,
      @Nullable CustomBlockDefinition var4,
      @Nullable CustomItemDefinition var5,
      int var6,
      int var7,
      int var8,
      double var9
   ) {
      this.yK = var1;
      this.aDx = var2;
      this.aDy = var7;
      this.aDz = var8;
      if (var8 >= var7) {
         throw new IllegalArgumentException("min_height must be less than max_height");
      }

      this.aDu = var4;
      this.aDv = var5;
      this.aDw = var6;
      this.aDA = var9;
      this.aDB = new HashSet();
      this.aDC = new HashSet();
      this.aDE = var3;
      this.aDF = var3.contains("*");
   }

   public void method2310(Object var1) {
      this.aDB.add(var1);
   }

   public boolean ei(String var1) {
      NamespacedKey var2 = ChunkBlockHookHolder.parseNamespacedKey(var1);
      if (var2 == null) {
         return false;
      }

      Object var3 = ChunkBlockHookHolder.getInstance().toMinecraftKey(var2);
      if (var3 == null) {
         return false;
      }

      this.method2310(var3);
      return true;
   }

   public void method2311(Material var1) {
      this.aDC.add(var1);
      if (var1 == Material.WATER || var1 == Material.LAVA) {
         this.aDD = true;
      }
   }

   public boolean method2312(Material var1) {
      return this.aDC.isEmpty() ? true : this.aDC.contains(var1);
   }

   public boolean aL(Block var1) {
      if (this.aDB.isEmpty()) {
         return true;
      }

      Object var2 = ChunkBlockHookHolder.getInstance().getBiomeKeyAt(var1);
      return var2 == null ? false : this.aDB.contains(var2);
   }

   protected boolean method2313(World var1) {
      if (!Settings.get().blockWorlds.isWorldActive(var1)) {
         return false;
      }

      if (this.aDF) {
         return true;
      }

      if (this.aDE.contains(var1.getName())) {
         return true;
      }

      for (String var3 : this.aDE) {
         if (FilenameUtils.wildcardMatch(var1.getName(), var3)) {
            return true;
         }
      }

      return false;
   }

   public void aM(Block var1) {
      this.aDu.placeBlockFull(null, var1, true, false, false, false);
   }

   public boolean method2314(Block var1, BlockFace var2) {
      if (this.aDv == null) {
         return this.aDu.placeBlockFull(null, var1, true, false, false, false);
      }

      FurnitureItemEntity var3 = (FurnitureItemEntity)this.aDv.aoh.getAction("furniture");
      if (var3 != null) {
         return var3.method412(this.aDv.Ds(), var1, null, null, null, var2) != null;
      }

      PlaceableItemSpec var4 = (PlaceableItemSpec)this.aDv.aoh.getAction("complex_furniture");
      return var4 != null && var4.placeInstance(null, this.aDv, null, var1.getLocation().add(0.5, 0.0, 0.5), var2, false) != null;
   }

   public abstract void method2315(World var1);

   public void method2316(World var1) {
      this.method2315(var1);
   }
}
