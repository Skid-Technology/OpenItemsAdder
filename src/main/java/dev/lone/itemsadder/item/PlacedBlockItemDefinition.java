package dev.lone.itemsadder.item;

import dev.lone.itemsadder.block.BlockPlacementRule;
import dev.lone.itemsadder.block.BlockRealizationMode;
import dev.lone.itemsadder.config.YamlConfig;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

public abstract class PlacedBlockItemDefinition extends CustomItemDefinition implements BlockPlacementRule {
   public final BlockRealizationMode aps;
   @Nullable
   String adU;
   @Nullable
   String adV;

   public PlacedBlockItemDefinition(YamlConfig var1, String var2, String var3, Material var4, BlockRealizationMode var5) {
      super(var1, var2, var3, var4);
      this.aps = var5;
   }

   public boolean Ef() {
      return this.aps != BlockRealizationMode.TILE;
   }

   @Override
   public boolean placeBlockDefault(Block var1) {
      return this.placeBlockNoPhysics(var1);
   }

   @Override
   public boolean placeBlockWithPhysics(Block var1, boolean var2) {
      return this.placeBlock(var1, var2);
   }

   @Nullable
   public String getBreakPermissionSuffix() {
      return this.adU;
   }

   public void setBreakPermissionSuffix(String var1) {
      this.adU = var1;
   }

   @Nullable
   public String getPlacePermissionSuffix() {
      return this.adV;
   }

   public void setPlacePermissionSuffix(String var1) {
      this.adV = var1;
   }
}
