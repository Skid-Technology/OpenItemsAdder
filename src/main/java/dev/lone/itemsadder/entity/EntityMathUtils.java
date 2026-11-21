package dev.lone.itemsadder.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public final class EntityMathUtils {
   private EntityMathUtils() {
   }

   public static boolean method8646(Entity var0) {
      Location var1 = var0.getLocation();
      World var2 = var1.getWorld();
      if (var2 == null) {
         return false;
      }

      double var3 = var0.getBoundingBox().getMinY();
      Block var5 = blockAt(var2, var1.getBlockX(), (int)Math.floor(var3), var1.getBlockZ());
      if (var5 != null && ((Levelled)var5.getBlockData()).getLevel() == 0) {
         Material var6 = var5.getRelative(0, 1, 0).getType();
         if (var6 != Material.AIR && var6 != Material.CAVE_AIR && var6 != Material.VOID_AIR) {
            return false;
         }

         double var7 = var5.getY() + method8650(0);
         return var3 >= var7 - 0.25 && var3 <= var7 + 0.7;
      } else {
         return false;
      }
   }

   public static int method8647(Entity var0, double var1, int var3) {
      int var4 = var0.getTicksLived();
      if (var4 != var3) {
         method8648(var0, var1);
      }

      return var4;
   }

   public static void method8648(Entity var0, double var1) {
      if (var0.isValid()) {
         Location var3 = var0.getLocation();
         World var4 = var3.getWorld();
         if (var4 != null) {
            double var5 = var0.getBoundingBox().getMinY();
            int var7 = var3.getBlockX();
            int var8 = (int)Math.floor(var5);
            int var9 = var3.getBlockZ();
            Vector var10 = var0.getVelocity();
            Block var11 = blockAt(var4, var7, var8, var9);
            if (var11 == null && (var10.getX() != 0.0 || var10.getZ() != 0.0)) {
               double var12 = var3.getX() + var10.getX() + Math.signum(var10.getX()) * var0.getBoundingBox().getWidthX() / 2.0;
               double var14 = var3.getZ() + var10.getZ() + Math.signum(var10.getZ()) * var0.getBoundingBox().getWidthZ() / 2.0;
               var11 = blockAt(var4, (int)Math.floor(var12), var8, (int)Math.floor(var14));
            }

            if (var11 != null) {
               if (var11.getRelative(0, 1, 0).getType() == Material.WATER) {
                  var10.setY(Math.max(var10.getY(), 0.12));
               } else {
                  int var16 = ((Levelled)var11.getBlockData()).getLevel();
                  double var13 = var11.getY() + method8650(var16) + var1;
                  if (var5 > var13 + 0.7) {
                     return;
                  }

                  var10.setY(method8651(var13 - var5, var10.getY()));
               }

               var0.setVelocity(var10);
            }
         }
      }
   }

   private static Block blockAt(World var0, int var1, int var2, int var3) {
      Block var4 = var0.getBlockAt(var1, var2, var3);
      if (var4.getType() == Material.WATER) {
         return var4;
      }

      if (var2 > var0.getMinHeight()) {
         Block var5 = var0.getBlockAt(var1, var2 - 1, var3);
         if (var5.getType() == Material.WATER) {
            return var5;
         }
      }

      return null;
   }

   static double method8650(int var0) {
      return (var0 >= 8 ? 8 : 8 - var0) / 9.0;
   }

   static double method8651(double var0, double var2) {
      return Math.max(-0.12, Math.min(0.12, var0 * 0.2 - var2 * 0.3));
   }
}
