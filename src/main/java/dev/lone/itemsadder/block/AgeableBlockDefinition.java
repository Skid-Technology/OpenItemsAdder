package dev.lone.itemsadder.block;

import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.PlacedBlockItemDefinition;
import dev.lone.itemsadder.util.TickCounter;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Fire;

public class AgeableBlockDefinition extends PlacedBlockItemDefinition {
   public TickCounter apn;

   public AgeableBlockDefinition(YamlConfig var1, String var2, String var3, Material var4, int var5) {
      super(var1, var2, var3, var4, BlockRealizationMode.FIRE);
      this.apn = new TickCounter(var5);
   }

   @Override
   public boolean placeBlock(Block var1, boolean var2) {
      if (!var1.getRelative(BlockFace.DOWN).getType().isSolid()) {
         return false;
      }

      Fire var3 = (Fire)Bukkit.createBlockData(Material.FIRE);
      var3.setAge(this.apn.ticks);
      CustomBlockChunkStoreBase var4 = ContentManager.method8084().method8183(var1);
      var4.setCustomBlock(var1, this.getNamespacedID());
      var4.setStoredInt(var1, 0);
      var1.setBlockData(var3, var2);
      return true;
   }

   @Override
   public boolean placeBlockNoPhysics(Block var1) {
      return this.placeBlock(var1, false);
   }

   @Override
   public boolean placeBlockAt(Location var1) {
      return this.placeBlockNoPhysics(var1.getBlock());
   }

   @Override
   public boolean placeBlockDefault(Block var1) {
      return this.placeBlockNoPhysics(var1);
   }

   @Override
   public boolean placeBlockWithPhysics(Block var1, boolean var2) {
      return this.placeBlock(var1, var2);
   }

   public static int getAge(Block block) {
      CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8183(block);
      if (var1.hasBlockEntryAt(block)) {
         Integer var2 = var1.getStoredInt(block);
         if (var2 != null) {
            return var2;
         }
      }

      return 0;
   }

   public static void setAge(Block block, int level) {
      CustomBlockChunkStoreBase var2 = ContentManager.method8084().method8183(block);
      var2.setStoredInt(block, level);
   }
}
