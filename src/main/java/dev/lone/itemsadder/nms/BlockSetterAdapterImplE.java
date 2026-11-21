package dev.lone.itemsadder.nms;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.v1_21_R7.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_21_R7.util.CraftMagicNumbers;

public class BlockSetterAdapterImplE implements BlockSetterAdapter {
   private static final BlockState aYH = CraftMagicNumbers.getBlock(Material.AIR).defaultBlockState();
   private static final BlockState aYI = ((CraftBlockData)Bukkit.createBlockData("chorus_plant[down=true,east=true,north=true,south=true,up=true,west=true]"))
      .getState();

   public static LevelChunk method3417(Chunk var0) {
      return (LevelChunk)((CraftChunk)var0).getHandle(ChunkStatus.FULL);
   }

   @Override
   public void bk(Block bukkitblock) {
      BlockPos var2 = new BlockPos(bukkitblock.getX(), bukkitblock.getY(), bukkitblock.getZ());
      LevelChunk var3 = method3417(bukkitblock.getChunk());
      if (bukkitblock.getType() == Material.CHORUS_PLANT) {
         this.method3421(var2, var3, aYI);
      } else {
         this.method3421(var2, var3, CraftMagicNumbers.getBlock(bukkitblock.getType()).defaultBlockState());
      }
   }

   @Override
   public boolean method3401(Block bukkitblock, Material type) {
      BlockPos var3 = new BlockPos(bukkitblock.getX(), bukkitblock.getY(), bukkitblock.getZ());
      LevelChunk var4 = method3417(bukkitblock.getChunk());
      BlockState var5 = CraftMagicNumbers.getBlock(type).defaultBlockState();
      return this.method3421(var3, var4, var5);
   }

   @Override
   public boolean setBlockData(Block bukkitblock, BlockData bukkitBlockData) {
      BlockPos var3 = new BlockPos(bukkitblock.getX(), bukkitblock.getY(), bukkitblock.getZ());
      LevelChunk var4 = method3417(bukkitblock.getChunk());
      BlockState var5 = ((CraftBlockData)bukkitBlockData).getState();
      return this.method3421(var3, var4, var5);
   }

   @Override
   public boolean bl(Block bukkitblock) {
      BlockPos var2 = new BlockPos(bukkitblock.getX(), bukkitblock.getY(), bukkitblock.getZ());
      LevelChunk var3 = method3417(bukkitblock.getChunk());
      return this.method3421(var2, var3, aYH);
   }

   private boolean method3421(BlockPos var1, LevelChunk var2, BlockState var3) {
      return var2.setBlockState(var1, var3, 0) != null;
   }
}
