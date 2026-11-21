package dev.lone.itemsadder.world;

import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.serial.SerialRecordC;
import dev.lone.itemsadder.util.serial.SerialRecordF;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CustomBlockChunkStore extends CustomBlockChunkStoreBase {
   @Nullable
   @Override
   public SerialRecordC loadChunkRecord(int var1, int var2, boolean var3) {
      Log.warn("DummyRegionFile.loadChunk() called, something is wrong.");
      return null;
   }

   @Nullable
   @Override
   public String getVanillaCustomBlockId(Location var1) {
      return null;
   }

   @Override
   public boolean isCustomBlockLoaded(Location var1) {
      return false;
   }

   @Override
   public boolean hasChunkRecord(int var1, int var2) {
      return false;
   }

   @Override
   public boolean hasChunkRecordOf(Chunk var1) {
      return false;
   }

   @Override
   public boolean hasBlockEntryInChunk(Chunk var1, int var2, int var3, int var4) {
      return false;
   }

   @Override
   public boolean hasBlockEntry(int var1, int var2, int var3, int var4, int var5) {
      return false;
   }

   @Override
   public boolean hasBlockEntryAt(Block var1) {
      return false;
   }

   @Nullable
   @Override
   public List getStoredItems(Block var1) {
      return null;
   }

   @Nullable
   @Override
   public ItemStack getStoredItem(Block var1) {
      return null;
   }

   @Nullable
   @Override
   SerialRecordF getItemRecord(Block var1) {
      return null;
   }

   @Override
   public boolean hasStoredItems(Block var1) {
      return false;
   }

   @Override
   public boolean isCustomBlock(Block block) {
      return false;
   }

   @Override
   public boolean isCustomBlockAt(Location var1) {
      return false;
   }

   @Nullable
   @Override
   public String getCustomBlockIdInChunk(int var1, int var2, int var3, Chunk var4) {
      return null;
   }

   @Nullable
   @Override
   public String getCustomBlockIdAt(int var1, int var2, int var3, int var4, int var5) {
      return null;
   }

   @Nullable
   @Override
   public String getCustomBlockId(Block var1) {
      return null;
   }

   @Nullable
   @Override
   public String getCustomBlockIdAtLocation(Location var1) {
      return null;
   }

   @Nullable
   @Override
   public String getBlockStringData(Block var1) {
      return null;
   }

   @Override
   public void setBlockStringData(Block var1, String var2) {
   }

   @Override
   public void setBlockStringDataAt(int var1, int var2, int var3, int var4, int var5, String var6) {
   }

   @Nullable
   @Override
   public String getCropSeedId(Block var1) {
      return null;
   }

   @Override
   public void setCropSeedId(Block var1, String var2) {
   }

   @Override
   public void setCropSeedIdAt(int var1, int var2, int var3, int var4, int var5, String var6) {
   }

   @Nullable
   @Override
   public String getCustomBlockIdIfFlagged(Block var1) {
      return null;
   }

   @Override
   public void setCustomBlockIdAndFlag(Block var1, String var2) {
   }

   @Override
   public List getFlaggedBlockIds(Chunk var1) {
      return List.of();
   }

   @Nullable
   @Override
   public List getAllBlocksLocationsList(Chunk chunk) {
      return null;
   }

   @Nullable
   @Override
   public Map getAllBlocksLocations(Chunk chunk) {
      return null;
   }

   @Override
   public void forEachBlockInChunk(Chunk var1, BiConsumer var2) {
   }

   @Nullable
   @Override
   public String getSecondaryBlockId(Block var1) {
      return null;
   }

   @Override
   public void setSecondaryBlockId(Block var1, String var2) {
   }

   @Override
   public void setSecondaryBlockIdAt(int var1, int var2, int var3, int var4, int var5, String var6) {
   }

   @Nullable
   @Override
   public Byte getStoredByte(Block var1) {
      return null;
   }

   @Override
   public byte getStoredByteOrDefault(Block var1, byte var2) {
      Log.warn("DummyRegionFile.getInstrument() called, something is wrong.");
      return 0;
   }

   @Override
   public void setStoredByte(Block var1, int var2) {
   }

   @Override
   public void setStoredByteAt(int var1, int var2, int var3, int var4, int var5, int var6) {
   }

   @Nullable
   @Override
   public Integer getStoredInt(Block var1) {
      return null;
   }

   @Override
   public void setStoredInt(Block var1, int var2) {
   }

   @Override
   public void setStoredIntAt(int var1, int var2, int var3, int var4, int var5, int var6) {
   }

   @Override
   public boolean isStoredFlagSet(Block var1) {
      return false;
   }

   @Override
   public void setStoredFlag(Block var1, boolean var2) {
   }

   @Override
   public void setStoredFlagAt(int var1, int var2, int var3, int var4, int var5, boolean var6) {
   }

   @Override
   public void setCustomBlockAt(int var1, int var2, int var3, int var4, int var5, String var6) {
   }

   @Override
   public void setCustomBlockInChunk(Chunk var1, int var2, int var3, int var4, String var5) {
   }

   @Override
   public void setCustomBlock(Block var1, String var2) {
   }

   @Override
   public void setCustomBlockAtLocation(Location var1, String var2) {
   }

   @Override
   public void removeCustomBlockInChunk(Chunk var1, int var2, int var3, int var4) {
   }

   @Override
   public void removeCustomBlockAt(int var1, int var2, int var3, int var4, int var5) {
   }

   @Override
   public void removeCustomBlock(Block var1) {
   }

   @Override
   public void removeCustomBlockAndRefresh(Block var1, boolean var2) {
   }

   @Override
   public void removeCustomBlockAndRefreshNms(Block var1, boolean var2, boolean var3) {
   }

   @Override
   public void clearChunk(Chunk var1) {
   }

   @Override
   public void setStoredItems(Block var1, List var2) {
   }

   @Override
   public void setStoredItem(Block var1, ItemStack var2) {
   }

   @Override
   public void setStoredItemAt(int var1, int var2, int var3, int var4, int var5, ItemStack var6) {
   }

   @Override
   public void resetBlockAttributes(Block var1) {
   }

   @Override
   public void save() {
   }

   @Override
   public void saveRegion(boolean var1) {
   }

   @Override
   public int unloadExpiredChunks() {
      return 0;
   }

   @Override
   public SaveStatus handleAutosave() {
      throw new RuntimeException("DummyRegionFile.handleAutosave() called, something is wrong.");
   }

   @Override
   public void unloadChunk(Chunk var1) {
   }

   @Override
   public void unloadChunkKey(String var1) {
   }

   @Override
   public String getDebugName() {
      return null;
   }

   @Override
   public int getLoadedChunkCount() {
      return 0;
   }

   @Override
   public String getFileName() {
      return null;
   }

   @Override
   public String getFileSizeText() {
      return "0";
   }

   @Override
   public void closeFiles() {
   }

   @Override
   public boolean isIdle() {
      return false;
   }

   @Override
   void markAccessed() {
   }

   @Override
   public boolean isChunkExpired(String var1) {
      return false;
   }

   @Override
   void markChunkAccessed(String var1) {
   }

   @Override
   public int hashCode() {
      return super.hashCode();
   }

   @Override
   public boolean equals(Object obj) {
      return super.equals(obj);
   }

   @Override
   protected Object clone() {
      return super.clone();
   }

   @Override
   public String toString() {
      return super.toString();
   }

   @Override
   protected void finalize() {
   }
}
