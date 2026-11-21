package dev.lone.itemsadder.world;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class ChunkViewerState {
   private final UUID playerId;
   private final PackedBlockStore blockStore;
   private final AtomicBoolean needsFullUpdate = new AtomicBoolean(true);
   private final AtomicBoolean renderPending = new AtomicBoolean();
   private final AtomicBoolean sendPending = new AtomicBoolean();
   private final ArrayList placementQueue = new ArrayList();
   private volatile UUID worldId;
   private UUID lastWorldId;
   private long lastUpdateMs;
   private volatile double cachedX;
   private volatile double cachedZ;
   private volatile int viewDistance;
   private volatile int minSectionY = Integer.MAX_VALUE;
   private int lastQuantX = Integer.MIN_VALUE;
   private int lastQuantY;
   private int lastQuantZ;
   private int lastQuantYaw;
   private int lastQuantPitch;

   private ChunkViewerState(Player var1, int var2, int var3) {
      this.playerId = var1.getUniqueId();
      this.worldId = var1.getWorld().getUID();
      this.blockStore = new PackedBlockStore(var2, var3);
   }

   private boolean hasViewChanged(Location var1) {
      int var2 = quantize(var1.getX() * 2.0);
      int var3 = quantize(var1.getY() * 2.0);
      int var4 = quantize(var1.getZ() * 2.0);
      int var5 = quantize(var1.getYaw() / 5.0F);
      int var6 = quantize(var1.getPitch() / 5.0F);
      if (var2 == this.lastQuantX && var3 == this.lastQuantY && var4 == this.lastQuantZ && var5 == this.lastQuantYaw && var6 == this.lastQuantPitch) {
         return false;
      }

      this.lastQuantX = var2;
      this.lastQuantY = var3;
      this.lastQuantZ = var4;
      this.lastQuantYaw = var5;
      this.lastQuantPitch = var6;
      return true;
   }

   private void updateViewPosition(Location var1) {
      this.hasViewChanged(var1);
   }

   private static int quantize(double var0) {
      int var2 = (int)var0;
      return var0 < var2 ? var2 - 1 : var2;
   }
}
