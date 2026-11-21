package dev.lone.itemsadder.block;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

public class ChunkKey {
   private final UUID jD;
   private final int jE;
   private final int jF;

   public ChunkKey(UUID var1, int var2, int var3) {
      this.jD = var1;
      this.jE = var2;
      this.jF = var3;
   }

   public static ChunkKey of(Chunk var0) {
      return new ChunkKey(var0.getWorld().getUID(), var0.getX(), var0.getZ());
   }

   @Nullable
   public Chunk resolveChunk() {
      World var1 = Bukkit.getWorld(this.jD);
      if (var1 == null) {
         return null;
      } else {
         return var1.isChunkLoaded(this.jE, this.jF) ? var1.getChunkAt(this.jE, this.jF) : null;
      }
   }

   public UUID worldId() {
      return this.jD;
   }

   public int x() {
      return this.jE;
   }

   public int z() {
      return this.jF;
   }
}
