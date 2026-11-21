package dev.lone.itemsadder.world;

import dev.lone.itemsadder.nms.BlockSetterAdapter;
import dev.lone.itemsadder.nms.NmsBlockSetterService;
import dev.lone.itemsadder.nms.NmsRegistry;
import dev.lone.itemsadder.util.BlockDataBatch;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.SoundUtils;
import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public final class BlockEffectsService {
   private final BlockEffectsAdapter adapter = (BlockEffectsAdapter)NmsRegistry.method2777(BlockEffectsAdapter.class);
   private static BlockEffectsService instance;
   private static final BlockData airData = Bukkit.createBlockData(Material.AIR);
   public static final BlockFace[] NEIGHBOR_FACES = new BlockFace[]{
      BlockFace.DOWN, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.WEST
   };

   BlockEffectsService() {
   }

   public static BlockEffectsService getInstance() {
      if (instance == null) {
         instance = new BlockEffectsService();
      }

      return instance;
   }

   public static BlockEffectsAdapter getAdapter() {
      return getInstance().adapter;
   }

   public static void clearBlocks(Collection var0, Block var1) {
      Collection var2 = BlockFaceUtils.playersInView(var1.getLocation());
      BlockDataBatch var3 = new BlockDataBatch(var2);
      BlockSetterAdapter var4 = NmsBlockSetterService.OD();

      for (Block var6 : var0) {
         var3.setBlock(var6.getLocation().toVector(), airData);
         var4.bl(var6);
      }

      var3.flush();
   }

   public static void clearBlockForViewers(Block var0) {
      NmsBlockSetterService.OD().bl(var0);

      for (Player var2 : BlockFaceUtils.playersInView(var0.getLocation())) {
         var2.sendBlockChange(var0.getLocation(), airData);
      }
   }

   public static void breakBlock(Block var0) {
      var0.getWorld().playEffect(var0.getLocation(), Effect.STEP_SOUND, var0.getType());
      var0.setType(Material.AIR, false);
   }

   public static void refreshNeighbors(Collection var0, Block var1) {
      Collection var2 = BlockFaceUtils.playersInView(var1.getLocation());
      BlockDataBatch var3 = new BlockDataBatch(var2);
      ArrayList var4 = new ArrayList();

      for (Block var6 : var0) {
         for (BlockFace var10 : NEIGHBOR_FACES) {
            Block var11 = var6.getRelative(var10);
            if (!var4.contains(var11)) {
               var4.add(var11);
               var3.setBlock(var11.getLocation().toVector(), var11.getBlockData());
            }
         }
      }

      var3.flush();
   }

   public static void spawnBreakParticles(Block var0) {
      spawnBlockParticles(var0.getLocation(), var0.getBlockData());
   }

   public static void spawnBlockParticles(Location var0, BlockData var1) {
      var0.getWorld()
         .spawnParticle(Particle.BLOCK, var0.clone().add(0.5, 0.5, 0.5), 40, 0.2333333333333333, 0.2333333333333333, 0.2333333333333333, 0.09, var1);
   }

   public static void sendBlockChange(BlockData var0, Location var1) {
      if (var0 != null) {
         for (Player var3 : BlockFaceUtils.playersInView(var1)) {
            var3.sendBlockChange(var1, var0);
         }
      }
   }

   public static void playBlockSound(Block var0) {
      SoundUtils.playSound(var0.getLocation(), getAdapter().getPlaceSound(var0.getType()), SoundCategory.BLOCKS, 1.0F, 0.8F);
   }
}
