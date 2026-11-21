package dev.lone.itemsadder.block;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.StringListMatcher;
import dev.lone.itemsadder.world.ChunkBlockHookHolder;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

public class ExplosiveBlockSpec {
   public static NamespacedKey art = NamespacedKey.fromString("itemsadder:primeable_block_id");
   public static final int aru = 1;
   public static final int arv = 2;
   public static final int arw = 4;
   public static final int arx = 8;
   public static boolean ary;
   public final StringListMatcher arz;
   public final int arA;
   public final double arB;
   public final boolean arC;
   public final boolean arD;
   public final int arE;
   public final int arF;

   public static int combineFlags(int var0, int var1) {
      return var0 | var1;
   }

   public boolean hasTrigger(int var1) {
      return (this.arF & var1) != 0;
   }

   public boolean explode(CustomBlockDefinition var1, Location var2) {
      return this.explodeAt(var1, var2, null);
   }

   public boolean explodeAt(CustomBlockDefinition var1, Location var2, @Nullable Player var3) {
      if (!this.arC) {
         for (BlockFace var7 : LocationScanner.CARDINAL_FACES) {
            if (var2.getBlock().getRelative(var7).getType() == Material.WATER) {
               return false;
            }
         }
      }

      var2 = var2.clone().add(0.5, 0.5, 0.5);
      BlockData var9 = var1.anF.getBlockData();
      TNTPrimed var10 = ChunkBlockHookHolder.getInstance()
         .hook
         .spawnPrimedTnt(var2, this.arE, var9, var3, var1x -> var1x.getPersistentDataContainer().set(art, PersistentDataType.STRING, var1.getNamespacedID()));
      boolean var11 = var10 != null && !var10.isDead();
      if (var11) {
         CustomBlock.remove(var2);
      }

      return var11;
   }

   public static void recomputeTriggerState() {
      resetPrimeableState();

      for (CustomBlockDefinition var1 : ContentManager.method8084().al) {
         ExplosiveBlockSpec var2 = var1.getExplosiveSpec();
         if (var2 != null && var2.hasTrigger(2)) {
            ary = true;
         }
      }
   }

   public static void resetPrimeableState() {
      ary = false;
   }

   @Generated
   ExplosiveBlockSpec(StringListMatcher var1, int var2, double var3, boolean var5, boolean var6, int var7, int var8) {
      this.arz = var1;
      this.arA = var2;
      this.arB = var3;
      this.arC = var5;
      this.arD = var6;
      this.arE = var7;
      this.arF = var8;
   }

   @Generated
   public static ExplosiveBlockSpecBuilder builder() {
      return new ExplosiveBlockSpecBuilder();
   }
}
