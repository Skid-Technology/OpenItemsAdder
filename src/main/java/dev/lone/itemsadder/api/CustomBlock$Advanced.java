package dev.lone.itemsadder.api;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.nms.NmsWorldService;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomBlock$Advanced {
   @Nullable
   public static String getInCustomRegion(Location location) {
      CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8184(location);
      return !var1.isCustomBlock(location.getBlock()) ? null : var1.getCustomBlockIdAtLocation(location);
   }

   public static void placeInCustomRegion(@NotNull CustomBlock customBlock, Location location) {
      CustomBlockChunkStoreBase var2 = ContentManager.method8084().method8184(location);
      var2.setCustomBlockAtLocation(location, customBlock.getNamespacedID());
   }

   public static boolean placeInCustomRegion(String namespacedId, Location location) {
      if (!ContentManager.method8084().method8158(namespacedId)) {
         return false;
      }

      CustomBlockChunkStoreBase var2 = ContentManager.method8084().method8184(location);
      var2.setCustomBlockAtLocation(location, namespacedId);
      return true;
   }

   public static boolean removeFromCustomRegion(Location location) {
      CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8184(location);
      if (!var1.isCustomBlock(location.getBlock())) {
         return false;
      }

      var1.removeCustomBlock(location.getBlock());
      return true;
   }

   public static void deleteAllCustomBlocksInChunk(Chunk chunk) {
      CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8182(chunk);
      if (var1 != null) {
         var1.clearChunk(chunk);
      }
   }

   public static void deleteAllCustomBlocksInChunk(Chunk chunk, boolean removeVanillaBlock, boolean sendChunkPacket) {
      if (!removeVanillaBlock) {
         deleteAllCustomBlocksInChunk(chunk);
      } else {
         CustomBlockChunkStoreBase var3 = ContentManager.method8084().method8182(chunk);
         if (var3 != null) {
            SchedulerUtils.runOnMainThread(() -> {
               runActionOnBlocks(chunk, (s, location) -> var3.removeCustomBlockAndRefresh(location.getBlock(), removeVanillaBlock));
               var3.clearChunk(chunk);
               if (sendChunkPacket) {
                  for (Player var5 : chunk.getWorld().getPlayers()) {
                     NmsWorldService.Om().method3196(chunk, var5);
                  }
               }
            });
         }
      }
   }

   @Nullable
   public static List getAllBlocksLocationsList(Chunk chunk) {
      CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8182(chunk);
      return var1 == null ? null : var1.getAllBlocksLocationsList(chunk);
   }

   @Nullable
   public static Map getAllBlocksLocations(Chunk chunk) {
      CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8182(chunk);
      return var1 == null ? null : var1.getAllBlocksLocations(chunk);
   }

   public static void runActionOnBlocks(Chunk chunk, BiConsumer action) {
      CustomBlockChunkStoreBase var2 = ContentManager.method8084().method8182(chunk);
      if (var2 != null) {
         var2.forEachBlockInChunk(chunk, action);
      }
   }
}
