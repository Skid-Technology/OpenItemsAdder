package dev.lone.itemsadder.api;

import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.loot.BlockToolDropRule;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomBlock extends CustomStack {
   private final CustomBlockDefinition internalBlock;
   private Block bukkitBlock;

   CustomBlock(CustomBlockDefinition internalBlock, Block bukkitBlock) {
      super(internalBlock);
      this.internalBlock = internalBlock;
      this.bukkitBlock = bukkitBlock;
   }

   CustomBlock(CustomBlockDefinition internalBlock) {
      super(internalBlock);
      this.internalBlock = internalBlock;
   }

   public static Set getNamespacedIdsInRegistry() {
      return ContentManager.method8084().am.keySet();
   }

   public static boolean isInRegistry(String namespacedId) {
      return ContentManager.method8084().am.containsKey(namespacedId);
   }

   @Nullable
   private static CustomBlock getOriginal(String namespacedID) {
      if (!ContentManager.method8084().method8155(namespacedID)) {
         return null;
      }

      CustomBlockDefinition var1 = ContentManager.method8084().method8160(namespacedID);
      return new CustomBlock(var1);
   }

   @Nullable
   public static CustomBlock getInstance(String namespacedID) {
      CustomBlock var1 = getOriginal(namespacedID);
      if (var1 != null) {
         var1.itemStack = var1.internalItem.Ds();
      }

      return var1;
   }

   @Nullable
   public static CustomBlock byItemStack(ItemStack itemStack) {
      CustomBlockDefinition var1 = ContentManager.method8084().method8159(itemStack);
      return var1 == null ? null : new CustomBlock(var1);
   }

   @Nullable
   public static CustomBlock place(String namespacedId, Location location) {
      CustomBlockDefinition var2 = ContentManager.method8084().method8160(namespacedId);
      if (var2 == null) {
         return null;
      }

      var2.placeBlockAt(location);
      return new CustomBlock(var2, location.getBlock());
   }

   @Nullable
   public static CustomBlock byAlreadyPlaced(Block block) {
      if (!CustomBlockDefinition.isCustomBlockType(block)) {
         return null;
      }

      CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8183(block);
      CustomBlockDefinition var2 = ContentManager.method8084().method8160(var1.getCustomBlockId(block));
      return var2 == null ? null : new CustomBlock(var2, block);
   }

   public static boolean remove(Location location) {
      location.getBlock().setType(Material.AIR, false);
      CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8184(location);
      if (!var1.isCustomBlock(location.getBlock())) {
         return false;
      }

      var1.removeCustomBlock(location.getBlock());
      BlockUtils.clearBlockState(location.getBlock());
      return true;
   }

   @Deprecated
   @Nullable
   public BlockData generateBlockData() {
      return this.internalBlock.anF.getBlockData();
   }

   @Nullable
   public BlockData getBaseBlockData() {
      return this.internalBlock.anF.getBlockData();
   }

   @Nullable
   public static BlockData getBaseBlockData(String namespacedID) {
      CustomBlockDefinition var1 = ContentManager.method8084().method8160(namespacedID);
      if (var1 == null) {
         return null;
      } else {
         return var1.anF != null ? var1.anF.getBlockData() : null;
      }
   }

   @Nullable
   public Block getBlock() {
      return this.bukkitBlock;
   }

   public boolean isPlaced() {
      return this.bukkitBlock != null;
   }

   public CustomBlock place(Location location) {
      this.bukkitBlock = location.getBlock();
      this.internalBlock.placeBlockAt(location);
      return this;
   }

   public boolean remove() {
      if (remove(this.bukkitBlock.getLocation())) {
         this.bukkitBlock = null;
         return true;
      } else {
         return false;
      }
   }

   public boolean breakNaturally(@NotNull Player player) {
      Objects.requireNonNull(player, "player");
      if (this.bukkitBlock != null && !this.bukkitBlock.getType().isAir()) {
         CustomBlockChunkStoreBase var2 = ContentManager.method8084().method8183(this.bukkitBlock);
         if (this.internalBlock.getNamespacedID().equals(var2.getCustomBlockId(this.bukkitBlock))
            && PermissionNodes.method1622(player, this.internalBlock.getBreakPermissionSuffix())
            && BlockFaceUtils.simulateBreak(this.bukkitBlock, player)) {
            boolean var3 = this.internalBlock.matchesPlacementItem(player.getInventory().getItemInMainHand());
            if (!var3 && Settings.get().field37) {
               return false;
            }

            CustomBlockBreakEvent var4 = new CustomBlockBreakEvent(player, this.bukkitBlock, this.internalBlock);
            if (!EventRegistrar.callEvent(var4)) {
               return false;
            }

            CustomBlockActionEvent var5 = new CustomBlockActionEvent(player, this.bukkitBlock, this.internalBlock, var2);
            var5.aI(var3);
            if (!EventRegistrar.callEvent(var5)) {
               return false;
            }

            this.bukkitBlock = null;
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean playBreakEffect() {
      return playBreakEffect(this.bukkitBlock, this.internalBlock);
   }

   public static boolean playBreakEffect(Block bukkitBlock) {
      return playBreakEffect(bukkitBlock, getInternalBlock(bukkitBlock));
   }

   private static boolean playBreakEffect(Block bukkitBlock, CustomBlockDefinition internalBlock) {
      if (internalBlock != null && internalBlock.anF != null) {
         internalBlock.anF.method10030(bukkitBlock.getLocation());
         internalBlock.acY.apQ.play(bukkitBlock.getLocation(), SoundCategory.BLOCKS);
         return true;
      } else {
         return false;
      }
   }

   public boolean playBreakSound() {
      return playBreakSound(this.bukkitBlock, this.internalBlock);
   }

   public static boolean playBreakSound(Block bukkitBlock) {
      return playBreakSound(bukkitBlock, getInternalBlock(bukkitBlock));
   }

   private static boolean playBreakSound(Block bukkitBlock, CustomBlockDefinition internalBlock) {
      if (internalBlock != null && internalBlock.anF != null) {
         internalBlock.acY.apQ.play(bukkitBlock.getLocation(), SoundCategory.BLOCKS);
         return true;
      } else {
         return false;
      }
   }

   public boolean playBreakParticles() {
      return playBreakParticles(this.bukkitBlock, this.internalBlock);
   }

   public static boolean playBreakParticles(Block bukkitBlock) {
      return playBreakParticles(bukkitBlock, getInternalBlock(bukkitBlock));
   }

   private static boolean playBreakParticles(Block bukkitBlock, CustomBlockDefinition internalBlock) {
      if (internalBlock != null && internalBlock.anF != null) {
         internalBlock.anF.method10030(bukkitBlock.getLocation());
         return true;
      } else {
         return false;
      }
   }

   public boolean playPlaceSound() {
      return playPlaceSound(this.bukkitBlock, this.internalBlock);
   }

   public static boolean playPlaceSound(Block bukkitBlock) {
      return playPlaceSound(bukkitBlock, getInternalBlock(bukkitBlock));
   }

   private static boolean playPlaceSound(Block bukkitBlock, CustomBlockDefinition internalBlock) {
      if (internalBlock != null && internalBlock.anF != null) {
         internalBlock.acY.apR.play(bukkitBlock.getLocation(), SoundCategory.BLOCKS);
         return true;
      } else {
         return false;
      }
   }

   public List getLoot(boolean includeSelfBlock) {
      return this.getLoot(null, includeSelfBlock);
   }

   public List getLoot() {
      return this.getLoot(null, true);
   }

   public List getLoot(@Nullable ItemStack tool, boolean includeSelfBlock) {
      return getLoot(this.bukkitBlock, this.internalBlock, tool, includeSelfBlock);
   }

   public static List getLoot(Block bukkitBlock, @Nullable ItemStack tool, boolean includeSelfBlock) {
      ArrayList var3 = new ArrayList();
      CustomBlockChunkStoreBase var4 = ContentManager.method8084().method8183(bukkitBlock);
      if (!var4.isCustomBlock(bukkitBlock)) {
         return var3;
      }

      String var5 = var4.getCustomBlockId(bukkitBlock);
      CustomBlockDefinition var6 = ContentManager.method8084().method8160(var5);
      return getLoot(bukkitBlock, var6, tool, includeSelfBlock);
   }

   static List getLoot(Block bukkitBlock, CustomBlockDefinition internalBlock, @Nullable ItemStack tool, boolean includeSelfBlock) {
      ArrayList var4 = new ArrayList();
      if (!internalBlock.matchesPlacementItem(tool)) {
         return var4;
      }

      List var5 = ContentManager.method8084().aJ.method1985(bukkitBlock, internalBlock);
      if (var5 != null) {
         for (BlockToolDropRule var7 : var5) {
            if (var7.Bu().getNamespacedID().equals(internalBlock.getNamespacedID()) && (tool == null || !var7.method1995(tool, bukkitBlock.getType()))) {
               var4.addAll(var7.method2005(tool));
            }
         }
      }

      if (includeSelfBlock && internalBlock.Dj()) {
         var4.add(internalBlock.Ds());
      }

      return var4;
   }

   public int getOriginalLightLevel() {
      return this.internalBlock.getLightLevel();
   }

   public void setCurrentLightLevel(int level) {
      BlockUtils.markBlockState(this.bukkitBlock, level);
   }

   @Nullable
   private static CustomBlockDefinition getInternalBlock(Block bukkitBlock) {
      CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8183(bukkitBlock);
      if (!var1.isCustomBlock(bukkitBlock)) {
         return null;
      }

      String var2 = var1.getCustomBlockId(bukkitBlock);
      return ContentManager.method8084().method8160(var2);
   }
}
