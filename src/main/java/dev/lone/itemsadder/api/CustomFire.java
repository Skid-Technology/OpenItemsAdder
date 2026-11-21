package dev.lone.itemsadder.api;

import dev.lone.itemsadder.block.AgeableBlockDefinition;
import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomFire extends CustomStack {
   private final AgeableBlockDefinition internalFire;
   private Block bukkitBlock;

   CustomFire(AgeableBlockDefinition internalFire, Block bukkitBlock) {
      super(internalFire);
      this.internalFire = internalFire;
      this.bukkitBlock = bukkitBlock;
   }

   CustomFire(AgeableBlockDefinition internalFire) {
      super(internalFire);
      this.internalFire = internalFire;
   }

   @Nullable
   private static CustomFire getOriginal(String namespacedID) {
      if (!ContentManager.method8084().method8155(namespacedID)) {
         return null;
      } else {
         return ContentManager.method8084().method8154(namespacedID) instanceof AgeableBlockDefinition var1 ? new CustomFire(var1) : null;
      }
   }

   @Nullable
   public static CustomFire getInstance(String namespacedID) {
      CustomFire var1 = getOriginal(namespacedID);
      if (var1 != null) {
         var1.itemStack = var1.internalItem.Ds();
      }

      return var1;
   }

   @Nullable
   public static CustomFire byItemStack(ItemStack itemStack) {
      return CustomStack.byItemStack(itemStack) instanceof CustomFire var1 ? var1 : null;
   }

   @Nullable
   public static CustomFire place(String namespacedId, Location location) {
      return ContentManager.method8084().method8154(namespacedId) instanceof AgeableBlockDefinition var2 && var2.placeBlockAt(location)
         ? new CustomFire(var2, location.getBlock())
         : null;
   }

   @Nullable
   public static CustomFire byAlreadyPlaced(Block block) {
      if (block.getType() != Material.FIRE) {
         return null;
      }

      CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8183(block);
      return ContentManager.method8084().method8154(var1.getCustomBlockId(block)) instanceof AgeableBlockDefinition var2 ? new CustomFire(var2, block) : null;
   }

   public static boolean remove(Location location) {
      location.getBlock().setType(Material.AIR, false);
      CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8184(location);
      var1.removeCustomBlock(location.getBlock());
      BlockUtils.clearBlockState(location.getBlock());
      return true;
   }

   public static int getAge(@NotNull Block block) {
      if (block.getType() != Material.FIRE) {
         throw new ClassCastException("This block is not a fire block: " + block.getLocation());
      } else {
         return AgeableBlockDefinition.getAge(block);
      }
   }

   public static void setAge(@NotNull Block block, int age) {
      if (block.getType() != Material.FIRE) {
         throw new ClassCastException("This block is not a fire block: " + block.getLocation());
      }

      AgeableBlockDefinition.setAge(block, age);
   }

   @Nullable
   public Location getLocation() {
      return this.bukkitBlock == null ? null : this.bukkitBlock.getLocation();
   }

   public boolean isPlaced() {
      return this.bukkitBlock != null;
   }

   @Nullable
   public CustomFire place(Location location) {
      if (!this.internalFire.placeBlockAt(location)) {
         return null;
      }

      this.bukkitBlock = location.getBlock();
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

   public int getAge() {
      return AgeableBlockDefinition.getAge(this.bukkitBlock);
   }

   public void setAge(int age) {
      AgeableBlockDefinition.setAge(this.bukkitBlock, age);
   }
}
