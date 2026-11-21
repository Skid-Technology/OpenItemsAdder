package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.PlacedBlockItemDefinition;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockMatcherSpec {
   public boolean akX;
   public Material db;
   public PlacedBlockItemDefinition akY;

   public BlockMatcherSpec(Material var1) {
      this.akX = false;
      this.db = var1;
   }

   public BlockMatcherSpec(PlacedBlockItemDefinition var1) {
      this.akX = true;
      this.akY = var1;
   }

   public static BlockMatcherSpec cw(String var0) {
      if (!ContentManager.method8084().method8155(var0)) {
         Material var2 = (Material)BukkitRegistryLookup.enumOrNull(var0.toUpperCase(), Material.class);
         if (var2 == null) {
            throw new IllegalArgumentException("Unknown item/material: " + var0);
         } else {
            return new BlockMatcherSpec(var2);
         }
      } else {
         CustomItemDefinition var1 = ContentManager.method8084().method8154(var0);
         if (var1 == null) {
            throw new IllegalArgumentException("Unknown item/material: " + var0);
         } else if (var1 instanceof PlacedBlockItemDefinition) {
            return new BlockMatcherSpec((PlacedBlockItemDefinition)var1);
         } else {
            throw new IllegalArgumentException("Item is not placeable: " + var0);
         }
      }
   }

   public boolean matches(Block var1) {
      if (this.akX) {
         if (CustomBlockDefinition.isCustomBlockType(var1)) {
            CustomBlockChunkStoreBase var2 = ContentManager.method8084().method8183(var1);
            if (!var2.isCustomBlock(var1)) {
               return false;
            }

            PlacedBlockItemDefinition var3 = (PlacedBlockItemDefinition)ContentManager.method8084().method8154(var2.getCustomBlockId(var1));
            return var3 == null ? false : var3.equals(this.akY);
         } else {
            return false;
         }
      } else {
         return var1.getType().equals(this.db);
      }
   }

   public String lS() {
      return this.akX ? this.akY.aok : this.db.toString();
   }
}
