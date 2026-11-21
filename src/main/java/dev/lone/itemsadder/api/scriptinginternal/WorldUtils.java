package dev.lone.itemsadder.api.scriptinginternal;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.ItemUseHandler;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import ia.sh.customblockdata.CustomBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Nullable;

public class WorldUtils {
   public static boolean isCustom(Block block) {
      if (block == null) {
         return false;
      } else {
         return block.getType() == Material.AIR ? false : ContentManager.isCustomBlock(block);
      }
   }

   @Nullable
   public static CustomBlock customBlock(Block block) {
      if (block == null) {
         return null;
      } else {
         return block.getType() == Material.AIR ? null : CustomBlock.byAlreadyPlaced(block);
      }
   }

   @Nullable
   public static CustomBlock customBlock(Location location) {
      if (location == null) {
         return null;
      }

      Block var1 = location.getBlock();
      return customBlock(var1);
   }

   @Nullable
   public static Block block(String worldName, int x, int y, int z) {
      if (worldName == null) {
         return null;
      }

      World var4 = Bukkit.getWorld(worldName);
      return var4 == null ? null : var4.getBlockAt(x, y, z);
   }

   public static Block block(World world, int x, int y, int z) {
      return world.getBlockAt(x, y, z);
   }

   @Nullable
   public static Block block(Location location) {
      return location.getWorld() == null ? null : location.getWorld().getBlockAt(location);
   }

   public static void removeBlock(Block block) {
      if (block != null) {
         if (block.getType() != Material.AIR) {
            Location var1 = block.getLocation();
            CustomBlockChunkStoreBase var2 = ContentManager.method8084().method8184(var1);
            if (!var2.isCustomBlock(block)) {
               block.setType(Material.AIR);
            } else {
               var2.removeCustomBlock(block);
               BlockUtils.clearBlockState(block);
            }
         }
      }
   }

   public static void placeBlock(Block block, Material material) {
      if (block != null) {
         if (block.getType() != material) {
            Location var2 = block.getLocation();
            CustomBlockChunkStoreBase var3 = ContentManager.method8084().method8184(var2);
            if (var3.isCustomBlock(block)) {
               var3.removeCustomBlock(block);
               BlockUtils.clearBlockState(block);
            }

            block.setType(material);
         }
      }
   }

   public static boolean placeBlock(Block block, String identifier) {
      BlockUtils.clearBlockState(block);
      Material var2 = Material.matchMaterial(identifier);
      if (var2 != null) {
         placeBlock(block, var2);
         return true;
      } else {
         CustomBlock var3 = CustomBlock.getInstance(identifier);
         if (var3 != null) {
            var3.place(block.getLocation());
            return true;
         } else {
            return false;
         }
      }
   }

   public static void placeBlock(Block block, CustomBlock customBlock) {
      BlockUtils.clearBlockState(block);
      customBlock.place(block.getLocation());
   }

   public static Location location(String worldName, int x, int y, int z) {
      if (worldName == null) {
         return null;
      }

      World var4 = Bukkit.getWorld(worldName);
      return var4 == null ? null : new Location(var4, x, y, z);
   }

   public static void breakBlockNaturally(Player player, int x, int y, int z) {
      Location var4 = new Location(player.getWorld(), x, y, z);
      Block var5 = var4.getBlock();
      breakBlockNaturally(player, var5);
   }

   public static void breakBlockNaturally(Player player, Block block) {
      if (block != null && block.getType() != Material.AIR) {
         Location var2 = block.getLocation();
         CustomBlockChunkStoreBase var3 = ContentManager.method8084().method8184(var2);
         if (!var3.isCustomBlock(block)) {
            block.breakNaturally();
         } else {
            CustomBlock var4 = CustomBlock.byAlreadyPlaced(block);
            if (var4 == null) {
               throw new RuntimeException("Unknown custom block: " + var3.getCustomBlockId(block));
            }

            var4.breakNaturally(player);
         }
      }
   }

   public static void breakBlockNaturally(Block block) {
      if (block != null && block.getType() != Material.AIR) {
         Location var1 = block.getLocation();
         CustomBlockChunkStoreBase var2 = ContentManager.method8084().method8184(var1);
         if (!var2.isCustomBlock(block)) {
            block.breakNaturally();
         } else {
            String var3 = var2.getCustomBlockId(block);
            CustomBlockDefinition var4 = ContentManager.method8084().method8160(var3);
            if (var4 == null) {
               throw new RuntimeException("Unknown custom block: " + var3);
            }

            ItemUseHandler.method1444(var2, block, var4);
         }
      }
   }

   public static void playParticle(Location location, String particleName) {
      playParticle(location, particleName, 1, 0.0, 0.0, 0.0, 0.0);
   }

   public static void playParticle(Location location, String particleName, int count) {
      playParticle(location, particleName, count, 0.0, 0.0, 0.0, 0.0);
   }

   public static void playParticle(
      World world, double x, double y, double z, String particleName, int count, double offsetX, double offsetY, double offsetZ, double extra
   ) {
      playParticle(new Location(world, x, y, z), particleName, count, offsetX, offsetY, offsetZ, extra);
   }

   public static void playParticle(Location location, String particleName, int count, double offsetX, double offsetY, double offsetZ, double extra) {
      if (location != null && particleName != null && !particleName.isEmpty()) {
         Particle var11 = BukkitRegistryLookup.particle(particleName);
         if (var11 == null) {
            throw new RuntimeException("Invalid particle name: " + particleName);
         }

         location.getWorld().spawnParticle(var11, location, count, offsetX, offsetY, offsetZ, extra);
      } else {
         throw new RuntimeException("Invalid parameters for playParticle");
      }
   }

   public static void playSound(Location location, String soundName) {
      if (location != null && soundName != null && !soundName.isEmpty()) {
         location.getWorld().playSound(location, soundName, 1.0F, 1.0F);
      } else {
         throw new RuntimeException("Invalid parameters for playSound");
      }
   }

   public static void playSound(Location location, String soundName, float volume, float pitch) {
      if (location != null && soundName != null && !soundName.isEmpty()) {
         location.getWorld().playSound(location, soundName, volume, pitch);
      } else {
         throw new RuntimeException("Invalid parameters for playSound");
      }
   }

   private static PersistentDataContainer getContainer(Block block) {
      if (block == null) {
         throw new IllegalArgumentException("Block cannot be null");
      }

      ContentManager.method8084().field90.Kz();
      return new CustomBlockData(block, Main.getInstance());
   }

   public static boolean hasData(Block block, String namespace, String key) {
      PersistentDataContainer var3 = getContainer(block);
      return ScriptingUtils.hasData(var3, namespace, key);
   }

   public static void removeData(Block block, String namespace, String key) {
      PersistentDataContainer var3 = getContainer(block);
      ScriptingUtils.removeData(var3, namespace, key);
   }

   public static void setDataByte(Block block, String namespace, String key, byte value) {
      PersistentDataContainer var4 = getContainer(block);
      ScriptingUtils.setDataByte(var4, namespace, key, value);
   }

   public static void setDataShort(Block block, String namespace, String key, short value) {
      PersistentDataContainer var4 = getContainer(block);
      ScriptingUtils.setDataShort(var4, namespace, key, value);
   }

   public static void setDataInt(Block block, String namespace, String key, int value) {
      PersistentDataContainer var4 = getContainer(block);
      ScriptingUtils.setDataInt(var4, namespace, key, value);
   }

   public static void setDataLong(Block block, String namespace, String key, long value) {
      PersistentDataContainer var5 = getContainer(block);
      ScriptingUtils.setDataLong(var5, namespace, key, value);
   }

   public static void setDataFloat(Block block, String namespace, String key, float value) {
      PersistentDataContainer var4 = getContainer(block);
      ScriptingUtils.setDataFloat(var4, namespace, key, value);
   }

   public static void setDataDouble(Block block, String namespace, String key, double value) {
      PersistentDataContainer var5 = getContainer(block);
      ScriptingUtils.setDataDouble(var5, namespace, key, value);
   }

   public static void setDataBool(Block block, String namespace, String key, boolean value) {
      PersistentDataContainer var4 = getContainer(block);
      ScriptingUtils.setDataBool(var4, namespace, key, value);
   }

   public static void setDataString(Block block, String namespace, String key, String value) {
      PersistentDataContainer var4 = getContainer(block);
      ScriptingUtils.setDataString(var4, namespace, key, value);
   }

   public static byte getDataByte(Block block, String namespace, String key, byte def) {
      PersistentDataContainer var4 = getContainer(block);
      return ScriptingUtils.getDataByte(var4, namespace, key, def);
   }

   public static short getDataShort(Block block, String namespace, String key, short def) {
      PersistentDataContainer var4 = getContainer(block);
      return ScriptingUtils.getDataShort(var4, namespace, key, def);
   }

   public static int getDataInt(Block block, String namespace, String key, int def) {
      PersistentDataContainer var4 = getContainer(block);
      return ScriptingUtils.getDataInt(var4, namespace, key, def);
   }

   public static long getDataLong(Block block, String namespace, String key, long def) {
      PersistentDataContainer var5 = getContainer(block);
      return ScriptingUtils.getDataLong(var5, namespace, key, def);
   }

   public static float getDataFloat(Block block, String namespace, String key, float def) {
      PersistentDataContainer var4 = getContainer(block);
      return ScriptingUtils.getDataFloat(var4, namespace, key, def);
   }

   public static double getDataDouble(Block block, String namespace, String key, double def) {
      PersistentDataContainer var5 = getContainer(block);
      return ScriptingUtils.getDataDouble(var5, namespace, key, def);
   }

   public static boolean getDataBool(Block block, String namespace, String key, boolean def) {
      PersistentDataContainer var4 = getContainer(block);
      return ScriptingUtils.getDataBool(var4, namespace, key, def);
   }

   public static String getDataString(Block block, String namespace, String key, String def) {
      PersistentDataContainer var4 = getContainer(block);
      return ScriptingUtils.getDataString(var4, namespace, key, def);
   }
}
