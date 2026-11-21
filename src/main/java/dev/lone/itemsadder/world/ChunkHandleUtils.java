package dev.lone.itemsadder.world;

import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.bukkit.craftbukkit.v1_20_R4.CraftChunk;

public class ChunkHandleUtils {
   public static Chunk toNmsChunk(org.bukkit.Chunk var0) {
      return (Chunk)((CraftChunk)var0).getHandle(ChunkStatus.n);
   }
}
