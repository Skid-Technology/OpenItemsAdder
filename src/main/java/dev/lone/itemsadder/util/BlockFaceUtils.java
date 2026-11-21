package dev.lone.itemsadder.util;

import dev.lone.itemsadder.internal.SwitchMap86;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.TNTPrimeEvent;
import org.bukkit.event.block.TNTPrimeEvent.PrimeCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class BlockFaceUtils {
   public static final BlockFace[] CARDINAL_FACES = new BlockFace[]{BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST};
   public static final BlockFace[] OCTANT_FACES = new BlockFace[]{
      BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST
   };
   public static final Set PASSABLE_MATERIALS = new HashSet<>(
      Arrays.asList(
         Material.AIR,
         Material.CAVE_AIR,
         Material.VOID_AIR,
         Material.LIGHT,
         Material.ACACIA_FENCE,
         Material.BIRCH_FENCE,
         Material.DARK_OAK_FENCE,
         Material.JUNGLE_FENCE,
         Material.SPRUCE_FENCE,
         Material.NETHER_BRICK_FENCE,
         Material.OAK_FENCE,
         Material.TORCH,
         Material.END_ROD,
         Material.BLACK_CARPET,
         Material.BLUE_CARPET,
         Material.BROWN_CARPET,
         Material.CYAN_CARPET,
         Material.GRAY_CARPET,
         Material.GREEN_CARPET,
         Material.LIGHT_BLUE_CARPET,
         Material.LIGHT_GRAY_CARPET,
         Material.LIME_CARPET,
         Material.MAGENTA_CARPET,
         Material.ORANGE_CARPET,
         Material.PINK_CARPET,
         Material.PURPLE_CARPET,
         Material.RED_CARPET,
         Material.RED_CARPET,
         Material.WHITE_CARPET,
         Material.YELLOW_CARPET,
         Material.LANTERN,
         Material.SHORT_GRASS,
         Material.TALL_GRASS,
         Material.FERN,
         Material.LARGE_FERN
      )
   );
   public static final Set REPLACEABLE_MATERIALS = new HashSet<>(
      Arrays.asList(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR, Material.LIGHT, Material.WATER, Material.LAVA)
   );

   public static Location topCenter(Block var0) {
      Location var1 = var0.getLocation();
      var1.setY(var1.getY() + 0.9F);
      var1.setX(var1.getX() + 0.5);
      var1.setZ(var1.getZ() + 0.5);
      return var1;
   }

   public static boolean simulateBreak(Block var0, Player var1) {
      if (var0.getType() == Material.AIR) {
         return true;
      }

      SyntheticBlockBreakEvent var2 = new SyntheticBlockBreakEvent(var0, var1);
      var2.setDropItems(false);
      Bukkit.getServer().getPluginManager().callEvent(var2);
      boolean var3 = false;
      if (!var2.isCancelled()) {
         var3 = true;
      }

      var2.setCancelled(true);
      return var3;
   }

   public static boolean simulatePlace(Block var0, Player var1) {
      SyntheticBlockPlaceEvent var2 = new SyntheticBlockPlaceEvent(
         var0, var0.getState(), var0.getRelative(BlockFace.DOWN), new ItemStack(Material.STONE), var1, true, EquipmentSlot.HAND
      );
      Bukkit.getServer().getPluginManager().callEvent(var2);
      boolean var3 = false;
      if (!var2.isCancelled()) {
         var3 = true;
      }

      var2.setCancelled(true);
      return var3;
   }

   public static boolean simulatePlaceAgainst(Block var0, Player var1, Block var2, ItemStack var3) {
      return simulatePlaceWithSlot(var0, var1, var2, var3, EquipmentSlot.HAND);
   }

   public static boolean simulatePlaceWithSlot(Block var0, Player var1, Block var2, ItemStack var3, EquipmentSlot var4) {
      SyntheticBlockPlaceEvent var5 = new SyntheticBlockPlaceEvent(var0, var0.getState(), var2, var3, var1, true, var4);
      Bukkit.getServer().getPluginManager().callEvent(var5);
      return !var5.isCancelled();
   }

   public static boolean simulateTntPrime(Block var0) {
      return EventRegistrar.callEvent(new TNTPrimeEvent(var0, PrimeCause.FIRE, null, null));
   }

   public static BlockFace yawToFace(float var0) {
      return yawToBlockFace(var0, true);
   }

   public static BlockFace yawToBlockFace(float var0, boolean var1) {
      return var1 ? OCTANT_FACES[Math.round(var0 / 45.0F) & 7] : CARDINAL_FACES[Math.round(var0 / 90.0F) & 3];
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static int yawToRotationDegrees(float var0, boolean var1) {
      return switch (SwitchMap86.bMR[yawToBlockFace(var0, var1).ordinal()]) {
         case 1 -> -180;
         case 2, 3, 4 -> 90;
         case 5, 6, 7 -> -90;
         default -> 0;
      };
   }

   public static BlockFace facingOf(Player var0) {
      float var1 = var0.getLocation().getYaw();
      var1 = (var1 % 360.0F + 360.0F) % 360.0F;
      if (var1 >= 45.0F && var1 < 135.0F) {
         return BlockFace.WEST;
      } else if (var1 >= 135.0F && var1 < 225.0F) {
         return BlockFace.NORTH;
      } else {
         return var1 >= 225.0F && var1 < 315.0F ? BlockFace.EAST : BlockFace.SOUTH;
      }
   }

   public static BlockFace verticalFacingOf(Player var0) {
      float var1 = var0.getLocation().getPitch();
      if (var1 >= 45.0F) {
         return BlockFace.DOWN;
      } else {
         return var1 <= -45.0F ? BlockFace.UP : BlockFace.SELF;
      }
   }

   public static float snapYaw(float var0, boolean var1) {
      return var1 ? Math.round(var0 / 45.0F) * 45.0F : Math.round(var0 / 90.0F) * 90.0F;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static Rotation yawToRotation(float var0) {
      BlockFace var1 = yawToFace(var0);

      return switch (SwitchMap86.bMR[var1.ordinal()]) {
         case 1 -> Rotation.FLIPPED;
         case 2 -> Rotation.CLOCKWISE;
         case 3 -> Rotation.CLOCKWISE_135;
         case 4 -> Rotation.CLOCKWISE_45;
         case 5 -> Rotation.COUNTER_CLOCKWISE;
         case 6 -> Rotation.FLIPPED_45;
         case 7 -> Rotation.COUNTER_CLOCKWISE_45;
         case 8 -> Rotation.NONE;
         default -> Rotation.NONE;
      };
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static BlockFace rotationToFace(Rotation var0) {
      return switch (SwitchMap86.bMS[var0.ordinal()]) {
         case 1 -> BlockFace.NORTH;
         case 2 -> BlockFace.NORTH_EAST;
         case 3 -> BlockFace.EAST;
         case 4 -> BlockFace.SOUTH_EAST;
         case 5 -> BlockFace.SOUTH;
         case 6 -> BlockFace.SOUTH_WEST;
         case 7 -> BlockFace.WEST;
         case 8 -> BlockFace.NORTH_WEST;
         default -> throw new MatchException(null, null);
      };
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static int faceToRotationDegrees(BlockFace var0) {
      return switch (SwitchMap86.bMR[var0.ordinal()]) {
         case 1, 9, 10 -> 180;
         case 2 -> 90;
         case 3, 12 -> 135;
         case 4, 16 -> 45;
         case 5 -> -90;
         case 6, 11 -> -135;
         case 7, 15 -> -45;
         case 8, 13, 14 -> 0;
         default -> 0;
      };
   }

   public static BlockFace vectorToFace(Vector var0) {
      if (var0.getY() == 1.0 && var0.getZ() > 0.0) {
         return BlockFace.UP;
      } else if (var0.getY() == -1.0 && var0.getZ() > 0.0) {
         return BlockFace.DOWN;
      } else if (var0.getX() == 1.0 && var0.getZ() < 0.0) {
         return BlockFace.EAST;
      } else if (var0.getX() == -1.0 && var0.getZ() > 0.0) {
         return BlockFace.WEST;
      } else if (var0.getZ() == 1.0) {
         return BlockFace.SOUTH;
      } else {
         return var0.getZ() == -1.0 ? BlockFace.NORTH : BlockFace.DOWN;
      }
   }

   public static List nearbyLivingEntitiesInView(Player var0) {
      ArrayList var1 = new ArrayList();

      for (Entity var3 : var0.getNearbyEntities(10.0, 10.0, 10.0)) {
         if (var3 instanceof LivingEntity && isLookingAt(var0, (LivingEntity)var3)) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public static boolean isLookingAt(Player var0, LivingEntity var1) {
      Location var2 = var0.getEyeLocation();
      Vector var3 = var1.getEyeLocation().toVector().subtract(var2.toVector());
      double var4 = var3.normalize().dot(var2.getDirection());
      return var4 > 0.99;
   }

   public static boolean simulateDamage(Entity var0, Entity var1) {
      SyntheticEntityDamageEvent var2 = new SyntheticEntityDamageEvent(var0, var1, DamageCause.ENTITY_ATTACK, 0.0);
      Bukkit.getPluginManager().callEvent(var2);
      boolean var3 = !var2.isCancelled();
      var2.setCancelled(true);
      return var3;
   }

   public static boolean isChunkLoadedAt(Block var0) {
      return var0.getWorld().isChunkLoaded(Math.floorDiv(var0.getX(), 16), Math.floorDiv(var0.getZ(), 16));
   }

   public static boolean isChunkLoaded(Chunk var0) {
      return var0.getWorld().isChunkLoaded(var0.getX(), var0.getZ());
   }

   public static boolean isChunkLoadedAtCoords(World var0, int var1, int var2) {
      return var0.isChunkLoaded(var1, var2);
   }

   public static Collection playersInView(Location var0) {
      int var1 = var0.getWorld().getViewDistance() * 16;
      return playersWithin(var0, var1);
   }

   public static Collection playersWithin(Location var0, int var1) {
      if (ServerCompat.isPaper()) {
         return var0.getWorld().getNearbyPlayers(var0, var0.getWorld().getViewDistance() * 16);
      }

      ArrayList var2 = new ArrayList();

      for (Player var5 : var0.getWorld().getPlayers()) {
         if (var5.getWorld() == var0.getWorld() && var5.getLocation().distance(var0) <= var1) {
            var2.add(var5);
         }
      }

      return var2;
   }

   public static Collection entitiesOfTypeNear(Location var0, double var1, EntityType var3) {
      return EnvironmentDetector.paperAdventure
         ? var0.getNearbyEntitiesByType(var3.getEntityClass(), var1)
         : var0.getWorld().getNearbyEntities(var0, var1, var1, var1, var1x -> var1x.getType() == var3);
   }

   public static Collection entitiesNear(Location var0, double var1) {
      return EnvironmentDetector.paperAdventure ? var0.getNearbyEntities(var1, var1, var1) : var0.getWorld().getNearbyEntities(var0, var1, var1, var1);
   }

   public static boolean playerOverlapsBlock(Player var0, Block var1) {
      return var0.getBoundingBox().overlaps(BoundingBox.of(var1));
   }

   public static String blockCoordsString(Block var0) {
      return formatLocation(var0.getLocation(), false);
   }

   public static String locationCoordsString(Location var0) {
      return formatLocation(var0, false);
   }

   public static String formatLocation(Location var0, boolean var1) {
      return var0.getX() + ", " + var0.getY() + ", " + var0.getZ() + (var1 ? " " + var0.getWorld().getName() : "");
   }

   public static String tpCommandForBlock(Block var0) {
      return tpCommandForLocation(var0.getLocation());
   }

   public static String tpCommandForLocation(Location var0) {
      return "/tppos " + var0.getX() + " " + var0.getY() + " " + var0.getZ();
   }

   public static double horizontalDistance(Location var0, Location var1) {
      return Math.sqrt(horizontalDistanceSquared(var0, var1));
   }

   private static double horizontalDistanceSquared(Location var0, Location var1) {
      if (var1.getWorld() != null && var0.getWorld() != null) {
         if (var1.getWorld() != var0.getWorld()) {
            throw new IllegalArgumentException("Cannot measure distance between " + var0.getWorld().getName() + " and " + var1.getWorld().getName());
         } else {
            return NumberConversions.square(var0.getX() - var1.getX()) + NumberConversions.square(var0.getZ() - var1.getZ());
         }
      } else {
         throw new IllegalArgumentException("Cannot measure distance to a null world");
      }
   }

   public static double horizontalDistanceUnchecked(Location var0, Location var1) {
      return Math.sqrt(NumberConversions.square(var0.getX() - var1.getX()) + NumberConversions.square(var0.getZ() - var1.getZ()));
   }

   public static boolean hasBlockAbove(Player var0) {
      Location var1 = var0.getLocation();
      Location var2 = new Location(var1.getWorld(), var1.getX(), var1.getY(), var1.getZ(), 0.0F, 90.0F);
      RayTraceWalker var3 = new RayTraceWalker(var2, 1.0);
      return var3.matchesTarget(RayTraceTarget.BLOCK) ? var3.hasHitBlock() : false;
   }

   public static void copyLocationInto(Location var0, Location var1) {
      var1.set(var0.getX(), var0.getY(), var0.getZ());
      var1.setPitch(var0.getPitch());
      var1.setYaw(var0.getYaw());
      var1.setWorld(var0.getWorld());
   }

   public static boolean isSameBlock(Location var0, Location var1, boolean var2) {
      if (var0 != null && var1 != null) {
         if (var2) {
            World var3 = var0.getWorld();
            World var4 = var1.getWorld();
            if (!Objects.equals(var3, var4)) {
               return false;
            }
         }

         return var0.getBlockX() == var1.getBlockX() && var0.getBlockY() == var1.getBlockY() && var0.getBlockZ() == var1.getBlockZ();
      } else {
         return false;
      }
   }

   public static boolean isSameBlockAsVector(Location var0, IntVector3 var1) {
      return var0 != null && var0.getBlockX() == var1.x && var0.getBlockY() == var1.y && var0.getBlockZ() == var1.z;
   }

   public static float normalizeYaw(float var0) {
      float var1 = var0 % 360.0F;
      if (var1 >= 180.0F) {
         var1 -= 360.0F;
      }

      if (var1 < -180.0F) {
         var1 += 360.0F;
      }

      return var1;
   }

   public static float lerpYaw(float var0, float var1, float var2) {
      float var3 = normalizeYaw(var1 - var0);
      return normalizeYaw(var0 + var3 * var2);
   }

   public static Location blockLocation(Location var0) {
      return new Location(var0.getWorld(), var0.getBlockX(), var0.getBlockY(), var0.getBlockZ());
   }

   static {
      REPLACEABLE_MATERIALS.add(Material.LIGHT);
   }
}
