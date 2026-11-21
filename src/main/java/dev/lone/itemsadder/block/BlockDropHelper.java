package dev.lone.itemsadder.block;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

public final class BlockDropHelper {
   private BlockDropHelper() {
   }

   public static void spawnItemParticles(Block var0, BoundingBox var1, ItemStack var2, double var3, double var5) {
      if (var2 != null && !var2.getType().isAir()) {
         World var7 = var0.getWorld();
         double var8 = var1.getMinX() - var0.getX();
         double var10 = var1.getMinY() - var0.getY();
         double var12 = var1.getMinZ() - var0.getZ();
         double var14 = Math.min(1.0, var1.getWidthX());
         double var16 = Math.min(1.0, var1.getHeight());
         double var18 = Math.min(1.0, var1.getWidthZ());
         int var20 = Math.max(2, (int)Math.ceil(var14 / var3));
         int var21 = Math.max(2, (int)Math.ceil(var16 / var3));
         int var22 = Math.max(2, (int)Math.ceil(var18 / var3));
         double var23 = var0.getX();
         double var25 = var0.getY();
         double var27 = var0.getZ();

         for (int var29 = 0; var29 < var20; var29++) {
            double var30 = (var29 + 0.5) / var20;
            double var32 = var23 + var8 + var30 * var14;

            for (int var34 = 0; var34 < var21; var34++) {
               double var35 = (var34 + 0.5) / var21;
               double var37 = var25 + var10 + var35 * var16;

               for (int var39 = 0; var39 < var22; var39++) {
                  double var40 = (var39 + 0.5) / var22;
                  double var42 = var27 + var12 + var40 * var18;
                  var7.spawnParticle(Particle.ITEM, var32, var37, var42, 1, 0.2, 0.2, 0.2, var5, var2, true);
               }
            }
         }
      }
   }

   public static void spawnItemParticlesAtBlock(Block var0, ItemStack var1) {
      BoundingBox var2 = getBlockBounds(var0);
      spawnItemParticles(var0, var2, var1, 0.25, 0.05);
   }

   public static void spawnItemParticlesInBox(Block var0, BoundingBox var1, ItemStack var2) {
      spawnItemParticles(var0, var1, var2, 0.2, 0.05);
   }

   public static void playBoneMealEffect(Block var0) {
      var0.getWorld().playEffect(var0.getLocation(), Effect.BONE_MEAL_USE, 0);
      Location var1 = var0.getLocation().add(0.5, 0.5, 0.5);

      for (int var2 = 0; var2 < 15; var2++) {
         double var3 = (Math.random() - 0.5) * 0.8;
         double var5 = (Math.random() - 0.5) * 0.8;
         double var7 = (Math.random() - 0.5) * 0.8;
         var0.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, var1.clone().add(var3, var5, var7), 1, 0.0, 0.0, 0.0, 0.0);
      }
   }

   private static BoundingBox getBlockBounds(Block var0) {
      try {
         return var0.getBoundingBox();
      } catch (NoSuchMethodError | UnsupportedOperationException var7) {
         double var1 = var0.getX();
         double var3 = var0.getY();
         double var5 = var0.getZ();
         return new BoundingBox(var1, var3, var5, var1 + 1.0, var3 + 1.0, var5 + 1.0);
      }
   }
}
