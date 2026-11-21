package dev.lone.itemsadder.block;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public class BlockUtils {
   public static List getBlocksInRegion(Location var0, Location var1, World var2) {
      ArrayList var3 = new ArrayList();
      int var4 = var0.getBlockX();
      int var5 = var0.getBlockY();
      int var6 = var0.getBlockZ();
      int var7 = var1.getBlockX();
      int var8 = var1.getBlockY();
      int var9 = var1.getBlockZ();
      int var10;
      int var13;
      if (var4 > var7) {
         var10 = var7;
         var13 = var4;
      } else {
         var10 = var4;
         var13 = var7;
      }

      int var11;
      int var14;
      if (var5 > var8) {
         var11 = var8;
         var14 = var5;
      } else {
         var11 = var5;
         var14 = var8;
      }

      int var12;
      int var15;
      if (var6 > var9) {
         var12 = var9;
         var15 = var6;
      } else {
         var12 = var6;
         var15 = var9;
      }

      for (int var16 = var10; var16 <= var13; var16++) {
         for (int var17 = var11; var17 <= var14; var17++) {
            for (int var18 = var12; var18 <= var15; var18++) {
               Block var19 = new Location(var2, var16, var17, var18).getBlock();
               var3.add(var19);
            }
         }
      }

      return var3;
   }

   public static List getBlocksInRegionCurrentWorld(Location var0, Location var1) {
      return getBlocksInRegion(var0, var1, var0.getWorld());
   }

   public static boolean isProtectedMaterial(Material var0) {
      return var0.getHardness() == -1.0F
         ? true
         : var0 == Material.END_PORTAL
            || var0 == Material.END_PORTAL_FRAME
            || var0 == Material.NETHER_PORTAL
            || var0 == Material.END_GATEWAY
            || var0 == Material.BEDROCK
            || var0 == Material.BARRIER
            || var0 == Material.AIR
            || var0 == Material.CAVE_AIR
            || var0 == Material.VOID_AIR
            || var0 == Material.TORCH
            || var0 == Material.LAVA
            || var0 == Material.WATER
            || var0 == Material.COMPARATOR
            || var0 == Material.REPEATER
            || var0 == Material.SPAWNER
            || var0.name().equals("TRIAL_SPAWNER")
            || var0.name().equals("VAULT")
            || var0 == Material.SHULKER_BOX
            || var0 == Material.CHEST
            || var0 == Material.BARREL
            || var0 == Material.SMOKER
            || var0 == Material.STONECUTTER
            || var0 == Material.GRINDSTONE
            || var0 == Material.SMITHING_TABLE
            || var0 == Material.ENCHANTING_TABLE
            || var0 == Material.BELL
            || var0 == Material.LOOM
            || var0 == Material.BLAST_FURNACE
            || var0 == Material.FURNACE
            || var0 == Material.REDSTONE
            || var0 == Material.REDSTONE_LAMP
            || var0 == Material.REDSTONE_TORCH
            || var0 == Material.REDSTONE_WALL_TORCH
            || var0 == Material.ANCIENT_DEBRIS
            || var0 == Material.RESPAWN_ANCHOR
            || var0 == Material.LODESTONE;
   }

   public static boolean isOreLike(Block var0) {
      Material var2 = var0.getType();
      boolean var1 = var2.toString().contains("ORE") || var2.toString().equals("ANCIENT_DEBRIS") || var2.toString().equals("CRYING_OBSIDIAN");
      if (var1) {
         return true;
      } else if (CustomBlockDefinition.isCustomBlockType(var0)) {
         CustomBlockChunkStoreBase var3 = ContentManager.method8084().method8183(var0);
         return var3.isCustomBlock(var0);
      } else {
         return false;
      }
   }

   public static void markBlockState(Block var0, int var1) {
      if (var1 != -1) {
         if (Main.aEN != null) {
            Main.aEN.placeLight(var0.getLocation(), var1);
         }
      }
   }

   public static void clearBlockState(Block var0) {
      if (Main.aEN != null) {
         Main.aEN.removeLight(var0.getLocation());
      }
   }

   @Deprecated
   public static Location getAdjustedLocation(Block var0) {
      Location var1 = var0.getLocation();
      return var1.add(var1.getX() > 0.0 ? 0.5 : -0.5, 0.0, var1.getZ() > 0.0 ? 0.5 : -0.5);
   }

   @Nullable
   public static Entity findNearestEntity(Block var0, EntityType var1, double var2) {
      Location var4 = var0.getLocation().add(0.5, 0.0, 0.5);
      double var5 = 999.0;
      Entity var7 = null;

      for (Entity var11 : var0.getWorld().getNearbyEntities(var4, 1.0, 1.0, 1.0)) {
         if (var11 == null || var11.getType() == var1) {
            double var8 = var4.distance(var11.getLocation());
            if (!(var8 > var2) && var8 < var5) {
               var7 = var11;
               var5 = var8;
            }
         }
      }

      return var7;
   }

   @Deprecated
   @Nullable
   public static Entity findNearestEntityDefault(Block var0, EntityType var1) {
      return findNearestEntity(var0, var1, 0.1);
   }

   @Deprecated
   @Nullable
   public static Entity findNearestEntityAny(Block var0) {
      return findNearestEntity(var0, null, 0.1F);
   }

   public static boolean isCropMaterial(Material var0) {
      return var0 == Material.BEETROOT
         || var0 == Material.CARROTS
         || var0 == Material.WHEAT
         || var0 == Material.NETHER_WART
         || var0 == Material.POTATOES
         || var0 == Material.SWEET_BERRY_BUSH;
   }

   public static boolean isFullyGrown(Block var0) {
      if (var0.getBlockData() instanceof Ageable) {
         Ageable var1 = (Ageable)var0.getBlockData();
         return var1.getAge() == var1.getMaximumAge();
      } else {
         return true;
      }
   }

   public static int getAge(Block block) {
      if (block.getBlockData() instanceof Ageable) {
         Ageable var1 = (Ageable)block.getBlockData();
         return var1.getAge();
      } else {
         return 9999;
      }
   }

   public static void setAge(Block block, int age) {
      if (block.getBlockData() instanceof Ageable) {
         Ageable var2 = (Ageable)block.getBlockData();
         var2.setAge(age);
         block.setBlockData(var2);
      }
   }

   public static int getMaxAge(Block var0) {
      if (var0.getBlockData() instanceof Ageable) {
         Ageable var1 = (Ageable)var0.getBlockData();
         return var1.getMaximumAge();
      } else {
         return -1;
      }
   }

   public static BlockFace getClickedFace(Location var0) {
      if (var0.getX() % 1.0 != 0.0 && var0.getY() % 1.0 != 0.0 && var0.getZ() % 1.0 != 0.0) {
         Block var1 = var0.getBlock();
         Location var2 = var1.getLocation().add(0.5, 0.5, 0.5);
         double var3 = var0.getX() - var2.getX();
         double var5 = var0.getY() - var2.getY();
         double var7 = var0.getZ() - var2.getZ();
         double var9 = Math.abs(var3);
         double var11 = Math.abs(var5);
         double var13 = Math.abs(var7);
         Log.devLog("dx: " + var3 + ", dy: " + var5 + ", dz: " + var7 + ", absDx: " + var9 + ", absDy: " + var11 + ", absDz: " + var13);
         Log.devLog("x: " + var0.getX() + ", y: " + var0.getY() + ", z: " + var0.getZ());
         if (var11 > var9 && var11 > var13) {
            return var5 > 0.0 ? BlockFace.UP : BlockFace.DOWN;
         } else if (var9 > var11 && var9 > var13) {
            return var3 > 0.0 ? BlockFace.EAST : BlockFace.WEST;
         } else {
            return var7 > 0.0 ? BlockFace.SOUTH : BlockFace.NORTH;
         }
      } else {
         throw new IllegalArgumentException(
            "Cannot identify the face of the block if location is between 2 blocks. Make sure that the location is touching a face but a bit inside of it."
         );
      }
   }
}
