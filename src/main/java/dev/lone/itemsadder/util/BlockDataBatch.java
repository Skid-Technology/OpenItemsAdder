package dev.lone.itemsadder.util;

import beer.devs.nativepackets.api.NativeBlockState;
import beer.devs.nativepackets.api.SectionBlockChange;
import dev.lone.itemsadder.network.NativePacketsHolder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BlockDataBatch {
   private static final long LOG_THROTTLE_MS = 30000L;
   private static long lastErrorLogMs;
   private final Collection players;
   private final Object2ObjectOpenHashMap sectionsByChunk = new Object2ObjectOpenHashMap();

   public BlockDataBatch(Collection var1) {
      this.players = var1;
   }

   public void setBlock(Vector var1, BlockData var2) {
      this.setBlockAt(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ(), var2);
   }

   public void setBlockAt(int var1, int var2, int var3, BlockData var4) {
      BlockPositionRecord var5 = new BlockPositionRecord(var1 >> 4, var2 >> 4, var3 >> 4);
      Short2ObjectOpenHashMap var6 = (Short2ObjectOpenHashMap)this.sectionsByChunk.computeIfAbsent(var5, var0 -> new Short2ObjectOpenHashMap());
      var6.put(sectionIndex(var1, var2, var3), var4 == null ? null : var4.clone());
   }

   private static short sectionIndex(int var0, int var1, int var2) {
      int var3 = var0 & 15;
      int var4 = var1 & 15;
      int var5 = var2 & 15;
      return (short)(var3 << 8 | var5 << 4 | var4);
   }

   public void flush() {
      ObjectIterator var1 = this.sectionsByChunk.entrySet().iterator();

      while (var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         BlockPositionRecord var3 = (BlockPositionRecord)var2.getKey();
         ArrayList var4 = new ArrayList(((Short2ObjectOpenHashMap)var2.getValue()).size());
         ObjectIterator var5 = ((Short2ObjectOpenHashMap)var2.getValue()).short2ObjectEntrySet().iterator();

         while (var5.hasNext()) {
            it.unimi.dsi.fastutil.shorts.Short2ObjectMap.Entry var6 = (it.unimi.dsi.fastutil.shorts.Short2ObjectMap.Entry)var5.next();
            int var7 = Short.toUnsignedInt(var6.getShortKey());
            NativeBlockState var8 = NativePacketsHolder.PE().PA().nativeBlockData(var6.getValue());
            var4.add(new SectionBlockChange(var7 >>> 8 & 15, var7 & 15, var7 >>> 4 & 15, var8));
         }

         Object var10 = NativePacketsHolder.PE().Pv().sectionBlocksUpdate(var3.x(), var3.y(), var3.z(), var4);

         for (Player var12 : this.players) {
            try {
               NativePacketsHolder.PE().method1867(var12, var10);
            } catch (Exception var9) {
               logSendError(var9);
            }
         }
      }
   }

   private static synchronized void logSendError(Exception var0) {
      long var1 = System.currentTimeMillis();
      if (var1 - lastErrorLogMs >= 30000L) {
         lastErrorLogMs = var1;
         Log.error("Failed to send a multi block change packet.", var0);
      }
   }
}
