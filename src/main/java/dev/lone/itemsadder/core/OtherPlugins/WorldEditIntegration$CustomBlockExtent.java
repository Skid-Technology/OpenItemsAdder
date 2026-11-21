package dev.lone.itemsadder.Core.OtherPlugins;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.world.block.BlockTypes;
import dev.lone.itemsadder.Core.OtherPlugins.WorldEditIntegration.CropExtent;
import dev.lone.itemsadder.api.CustomBlock.Advanced;
import java.util.HashMap;
import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.World;

final class WorldEditIntegration$CustomBlockExtent extends CropExtent {
   private WorldEditIntegration$CustomBlockExtent(WorldEditIntegration var1, Extent extent, World world) {
      super(var1, extent, world);
      this.this$0 = var1;
   }

   @Override
   public BaseBlock getFullBlock(BlockVector3 position) {
      BaseBlock var2 = super.getFullBlock(position);
      String var3 = Advanced.getInCustomRegion(this.location(position));
      if (var3 == null) {
         return var2;
      }

      HashMap var4 = new HashMap();
      if (var2.getNbtData() != null) {
         var4.putAll(var2.getNbtData().getValue());
      }

      var4.put("ItemsAdderId", new StringTag(var3));
      return var2.toImmutableState().toBaseBlock(new CompoundTag(var4));
   }

   @Override
   public boolean setBlock(BlockVector3 position, BlockStateHolder state) {
      Location var3 = this.location(position);
      String var4 = Advanced.getInCustomRegion(var3);
      boolean var5 = this.this$0.missingDefinition(state);
      String var6 = var5 ? null : this.this$0.idOf(state);
      boolean var7 = super.setBlock(position, state);
      if (!var7 && !super.getBlock(position).equals(var5 ? BlockTypes.AIR.getDefaultState() : state.toImmutableState())) {
         return false;
      }

      if (Objects.equals(var4, var6)) {
         return var7;
      }

      if (var4 != null) {
         Advanced.removeFromCustomRegion(var3);
      }

      if (var6 != null) {
         Advanced.placeInCustomRegion(var6, var3);
      }

      return true;
   }
}
