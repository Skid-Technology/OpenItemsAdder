package dev.lone.itemsadder.util;

import dev.lone.itemsadder.item.ItemStackUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class LocationScanner {
   public static final BlockFace[] CARDINAL_FACES = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
   public static final BlockFace[] NEIGHBOR_FACES = new BlockFace[]{
      BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST
   };
   public static final BlockFace[] NEIGHBOR_AND_VERTICAL_FACES = new BlockFace[]{
      BlockFace.NORTH,
      BlockFace.EAST,
      BlockFace.SOUTH,
      BlockFace.WEST,
      BlockFace.NORTH_EAST,
      BlockFace.NORTH_WEST,
      BlockFace.SOUTH_EAST,
      BlockFace.SOUTH_WEST,
      BlockFace.UP,
      BlockFace.DOWN
   };

   public static boolean dropItem(Location var0, @Nullable ItemStack var1) {
      return dropItemNear(var0, var1, null);
   }

   public static boolean dropItemNear(Location var0, @Nullable ItemStack var1, @Nullable Player var2) {
      if (var1 != null && var1.getType() != Material.AIR) {
         int var3 = var1.getAmount();
         if (var3 <= 0) {
            return false;
         }

         try {
            var0 = var0.clone().add(0.5, 0.5, 0.5);
            if (var0.getBlock().getType().isSolid()) {
               Block var4 = var0.getBlock().getRelative(BlockFace.UP);
               Block var5 = var0.getBlock().getRelative(BlockFace.DOWN);
               if (var2 != null) {
                  if (var0.getY() > var2.getLocation().getY()) {
                     if (!var5.getType().isSolid()) {
                        var0 = var5.getLocation();
                     } else if (!var4.getType().isSolid()) {
                        var0 = var4.getLocation();
                     }
                  } else if (!var4.getType().isSolid()) {
                     var0 = var4.getLocation();
                  } else if (!var5.getType().isSolid()) {
                     var0 = var5.getLocation();
                  }

                  var0 = var0.add(0.5, 0.5, 0.5);
               } else {
                  if (!var4.getType().isSolid()) {
                     var0 = var4.getLocation();
                  } else if (!var5.getType().isSolid()) {
                     var0 = var5.getLocation();
                  }

                  var0 = var0.add(0.5, 0.5, 0.5);
               }
            }

            World var10 = var0.getWorld();
            int var11 = var1.getType().getMaxStackSize();
            if (var3 > var11) {
               float var6 = (float)var3 / var11;

               for (int var7 = 0; var7 < var6 - 1.0F; var7++) {
                  var10.dropItemNaturally(var0, ItemStackUtils.method7263(var1, var11));
               }

               if (var6 % 1.0F != 0.0F) {
                  int var12 = var3 - var11 * (int)var6;
                  var10.dropItemNaturally(var0, ItemStackUtils.method7263(var1, var12));
               }
            } else {
               var10.dropItemNaturally(var0, var1);
            }

            return true;
         } catch (IllegalArgumentException var8) {
            return false;
         }
      } else {
         return false;
      }
   }

   public static void dropItems(Location var0, Collection var1) {
      for (ItemStack var3 : var1) {
         dropItem(var0, var3);
      }
   }

   public static boolean isValidBiome(String var0) {
      try {
         Biome.valueOf(var0);
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   public static boolean isValidTreeType(String var0) {
      try {
         TreeType.valueOf(var0);
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   public static Collection getChunksAroundPlayer(Player var0, boolean var1) {
      return getChunksAroundPlayerRange(var0, var1, -5, 0, 5);
   }

   public static Collection getDefaultChunksAroundPlayer(Player var0) {
      return getChunksAroundPlayerRange(var0, false, -5, 0, 5);
   }

   public static Collection getChunksAroundPlayerRange(Player var0, boolean var1, int var2, int var3, int var4) {
      return getChunksAroundLocationRange(var0.getLocation(), var1, var2, var3, var4);
   }

   public static List locationsInCube(Location var0, int var1) {
      World var2 = var0.getWorld();
      int var3 = var0.getBlockX();
      int var4 = var0.getBlockZ();
      int var5 = var0.getBlockY();
      ArrayList var6 = new ArrayList();

      for (int var7 = -var1; var7 <= var1; var7++) {
         for (int var8 = -var1; var8 <= var1; var8++) {
            for (int var9 = -var1; var9 <= var1; var9++) {
               var6.add(new Location(var2, var3 + var7, var5 + var9, var4 + var8));
            }
         }
      }

      return var6;
   }

   public static Collection getChunksAroundLocation(Location var0, int var1) {
      return getChunksAroundLocationRange(var0, true, var1, var1, var1);
   }

   public static Collection getChunksAroundLocationRange(Location var0, boolean var1, int var2, int var3, int var4) {
      int[] var5 = new int[]{var2, var3, var4};
      World var6 = var0.getWorld();
      int var7 = var0.getChunk().getX();
      int var8 = var0.getChunk().getZ();
      HashSet var9 = new HashSet();

      for (int var13 : var5) {
         for (int var17 : var5) {
            if (var6.isChunkLoaded(var7 + var13, var8 + var17)) {
               Chunk var18 = var6.getChunkAt(var7 + var13, var8 + var17);
               var9.add(var18);
            }
         }
      }

      return var9;
   }

   public static List blocksInCube(Location var0, int var1) {
      World var2 = var0.getWorld();
      int var3 = (int)var0.getX();
      int var4 = (int)var0.getZ();
      int var5 = (int)var0.getY();
      ArrayList var6 = new ArrayList();

      for (int var7 = -var1; var7 <= var1; var7++) {
         for (int var8 = -var1; var8 <= var1; var8++) {
            for (int var9 = -var1; var9 <= var1; var9++) {
               var6.add(new Location(var2, var3 + var7, var5 + var9, var4 + var8).getBlock());
            }
         }
      }

      return var6;
   }

   public static List nearbyLocations(Location var0) {
      return locationsInBox(var0, 2, 0);
   }

   public static List locationsInBox(Location var0, int var1, int var2) {
      ArrayList var3 = new ArrayList();

      for (int var4 = var0.getBlockX() - var1; var4 <= var0.getBlockX() + var1; var4++) {
         for (int var5 = var0.getBlockY() - var2; var5 <= var0.getBlockY() + var2; var5++) {
            for (int var6 = var0.getBlockZ() - var1; var6 <= var0.getBlockZ() + var1; var6++) {
               var3.add(new Location(var0.getWorld(), var4, var5, var6));
            }
         }
      }

      return var3;
   }

   public static List blocksInBoxExcludingCenter(Location var0, int var1, int var2) {
      World var3 = var0.getWorld();
      ArrayList var4 = new ArrayList();

      for (int var5 = var0.getBlockX() - var1; var5 <= var0.getBlockX() + var1; var5++) {
         for (int var6 = var0.getBlockY() - var2; var6 <= var0.getBlockY() + var2; var6++) {
            for (int var7 = var0.getBlockZ() - var1; var7 <= var0.getBlockZ() + var1; var7++) {
               if (var5 != var0.getBlockX() || var6 != var0.getBlockY() || var7 != var0.getBlockZ()) {
                  var4.add(new Location(var3, var5, var6, var7).getBlock());
               }
            }
         }
      }

      return var4;
   }

   public static List vectorsInBoxExcludingCenter(Location var0, int var1, int var2) {
      ArrayList var3 = new ArrayList();

      for (int var4 = var0.getBlockX() - var1; var4 <= var0.getBlockX() + var1; var4++) {
         for (int var5 = var0.getBlockY() - var2; var5 <= var0.getBlockY() + var2; var5++) {
            for (int var6 = var0.getBlockZ() - var1; var6 <= var0.getBlockZ() + var1; var6++) {
               if (var4 != var0.getBlockX() || var5 != var0.getBlockY() || var6 != var0.getBlockZ()) {
                  var3.add(new Vector(var4, var5, var6));
               }
            }
         }
      }

      return var3;
   }

   public static List blocksInBoxMatching(Location var0, int var1, int var2, List var3) {
      ArrayList var4 = new ArrayList();

      for (int var5 = var0.getBlockX() - var1; var5 <= var0.getBlockX() + var1; var5++) {
         for (int var6 = var0.getBlockY() - var2; var6 <= var0.getBlockY() + var2; var6++) {
            for (int var7 = var0.getBlockZ() - var1; var7 <= var0.getBlockZ() + var1; var7++) {
               if (var5 != var0.getBlockX() || var6 != var0.getBlockY() || var7 != var0.getBlockZ()) {
                  Block var8 = new Location(var0.getWorld(), var5, var6, var7).getBlock();
                  if (var3.contains(var8.getType())) {
                     var4.add(var8);
                  }
               }
            }
         }
      }

      return var4;
   }

   public static List blocksInBoxFiltered(Location var0, int var1, int var2) {
      ArrayList var3 = new ArrayList();

      for (int var4 = var0.getBlockX() - var1; var4 <= var0.getBlockX() + var1; var4++) {
         for (int var5 = var0.getBlockY() - var2; var5 <= var0.getBlockY() + var2; var5++) {
            for (int var6 = var0.getBlockZ() - var1; var6 <= var0.getBlockZ() + var1; var6++) {
               if (var4 != var0.getBlockX() || var5 != var0.getBlockY() || var6 != var0.getBlockZ()) {
                  Block var7 = new Location(var0.getWorld(), var4, var5, var6).getBlock();
                  if (ItemStackUtils.aj(var7.getType()) || ItemStackUtils.ai(var7.getType())) {
                     var3.add(var7);
                  }
               }
            }
         }
      }

      return var3;
   }

   public static List getNeighborBlocks(Block var0) {
      return getNeighborBlocksWith(var0, false, false);
   }

   public static List getNeighborBlocksWith(Block var0, boolean var1, boolean var2) {
      ArrayList var3 = new ArrayList();

      for (BlockFace var7 : var2 ? NEIGHBOR_AND_VERTICAL_FACES : NEIGHBOR_FACES) {
         var3.add(var0.getRelative(var7));
      }

      if (var1) {
         var3.add(var0.getRelative(BlockFace.SELF));
      }

      return var3;
   }

   public static List columnAround(Block var0, int var1) {
      ArrayList var2 = new ArrayList();
      Location var3 = var0.getLocation().clone();

      for (int var4 = -var1 / 2; var4 < var1 / 2; var4++) {
         var2.add(var3.clone().add(0.0, var4, 0.0));
      }

      return var2;
   }

   public static String formatCoords(Location var0) {
      return var0.getBlockX() + ", " + var0.getBlockY() + ", " + var0.getBlockZ();
   }

   public int hashCoords(double var1, double var3, double var5) {
      int var7 = 3;
      var7 = 19 * var7 + (int)(Double.doubleToLongBits(var1) ^ Double.doubleToLongBits(var1) >>> 32);
      var7 = 19 * var7 + (int)(Double.doubleToLongBits(var3) ^ Double.doubleToLongBits(var3) >>> 32);
      return 19 * var7 + (int)(Double.doubleToLongBits(var5) ^ Double.doubleToLongBits(var5) >>> 32);
   }
}
