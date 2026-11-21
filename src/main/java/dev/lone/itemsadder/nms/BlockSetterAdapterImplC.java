package dev.lone.itemsadder.nms;

import net.minecraft.core.BlockPosition;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_21_R5.CraftChunk;
import org.bukkit.craftbukkit.v1_21_R5.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_21_R5.util.CraftMagicNumbers;

public class BlockSetterAdapterImplC implements BlockSetterAdapter {
   private static final IBlockData aYD = CraftMagicNumbers.getBlock(Material.AIR).m();
   private static final IBlockData aYE = ((CraftBlockData)Bukkit.createBlockData("chorus_plant[down=true,east=true,north=true,south=true,up=true,west=true]"))
      .getState();

   public static Chunk method3406(org.bukkit.Chunk var0) {
      return (Chunk)((CraftChunk)var0).getHandle(ChunkStatus.n);
   }

   @Override
   public void bk(Block bukkitblock) {
      BlockPosition var2 = new BlockPosition(bukkitblock.getX(), bukkitblock.getY(), bukkitblock.getZ());
      Chunk var3 = method3406(bukkitblock.getChunk());
      if (bukkitblock.getType() == Material.CHORUS_PLANT) {
         this.method3416(var2, var3, aYE);
      } else {
         this.method3416(var2, var3, CraftMagicNumbers.getBlock(bukkitblock.getType()).m());
      }
   }

   @Override
   public boolean method3401(Block bukkitblock, Material type) {
      BlockPosition var3 = new BlockPosition(bukkitblock.getX(), bukkitblock.getY(), bukkitblock.getZ());
      Chunk var4 = method3406(bukkitblock.getChunk());
      IBlockData var5 = CraftMagicNumbers.getBlock(type).m();
      return this.method3416(var3, var4, var5);
   }

   @Override
   public boolean setBlockData(Block bukkitblock, BlockData bukkitBlockData) {
      BlockPosition var3 = new BlockPosition(bukkitblock.getX(), bukkitblock.getY(), bukkitblock.getZ());
      Chunk var4 = method3406(bukkitblock.getChunk());
      IBlockData var5 = ((CraftBlockData)bukkitBlockData).getState();
      return this.method3416(var3, var4, var5);
   }

   @Override
   public boolean bl(Block bukkitblock) {
      BlockPosition var2 = new BlockPosition(bukkitblock.getX(), bukkitblock.getY(), bukkitblock.getZ());
      Chunk var3 = method3406(bukkitblock.getChunk());
      return this.method3416(var2, var3, aYD);
   }

   private boolean method3416(BlockPosition var1, Chunk var2, IBlockData var3) {
      return var2.a(var1, var3, 0) != null;
   }
}
