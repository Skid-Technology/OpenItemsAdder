package dev.lone.itemsadder.api;

import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.PlantItemDefinition;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CustomCrop {
   private final Block block;
   private final CustomStack seed_customStack;

   private CustomCrop(Block block, CustomStack seed_customStack) {
      this.block = block;
      this.seed_customStack = seed_customStack;
   }

   @Nullable
   public static CustomCrop place(String seedId, Location location) {
      PlantItemDefinition var2 = (PlantItemDefinition)ContentManager.method8084().method8154(seedId);
      if (var2 == null) {
         return null;
      }

      CustomCrop var3 = new CustomCrop(location.getBlock(), new CustomStack(var2));
      CustomBlockChunkStoreBase var4 = ContentManager.method8084().method8183(var3.block);
      var4.setCropSeedId(var3.block, seedId);
      var2.aC(location.getBlock());
      return var3;
   }

   @Nullable
   public static CustomCrop byAlreadyPlaced(Block block) {
      if (!PlantItemDefinition.aD(block)) {
         return null;
      }

      PlantItemDefinition var1 = get(block);
      return var1 == null ? null : new CustomCrop(block, new CustomStack(var1));
   }

   public static boolean isSeed(ItemStack itemStack) {
      return ContentManager.method8084().method8167(itemStack);
   }

   @Nullable
   public static List getLoot(Block block, @Nullable ItemStack toolUsed) {
      PlantItemDefinition var2 = get(block);
      return var2 == null ? null : var2.method8838(toolUsed);
   }

   @Nullable
   public static List getLoot(Block block) {
      return getLoot(block, null);
   }

   public CustomStack getSeed() {
      return this.seed_customStack;
   }

   @Nullable
   public List getLoot(@Nullable ItemStack toolUsed) {
      return this.block == null ? null : getLoot(this.block, toolUsed);
   }

   @Nullable
   public List getLoot() {
      return getLoot(this.block, null);
   }

   public int getAge() {
      return BlockUtils.getAge(this.block);
   }

   public void setAge(int age) {
      BlockUtils.setAge(this.block, age);
   }

   public boolean isFullyGrown() {
      return BlockUtils.isFullyGrown(this.block);
   }

   public int getMaxAge() {
      return BlockUtils.getMaxAge(this.block);
   }

   public void setFullyGrown() {
      this.setAge(this.getMaxAge());
   }

   public void incrementAge() {
      this.setAge(this.getAge() + 1);
   }

   @Nullable
   private static PlantItemDefinition get(Block block) {
      CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8183(block);
      String var2 = var1.getCropSeedId(block);
      return var2 == null ? null : (PlantItemDefinition)ContentManager.method8084().method8154(var2);
   }
}
