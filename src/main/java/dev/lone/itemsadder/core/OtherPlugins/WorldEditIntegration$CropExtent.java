package dev.lone.itemsadder.Core.OtherPlugins;

import com.sk89q.jnbt.ByteTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.world.block.BlockTypes;
import dev.lone.itemsadder.block.ChunkBlockTracker;
import dev.lone.itemsadder.resourcepack.ByteNamedKey;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.World;

class WorldEditIntegration$CropExtent extends AbstractDelegateExtent {
   protected final World world;

   private WorldEditIntegration$CropExtent(WorldEditIntegration var1, Extent extent, World world) {
      super(extent);
      this.this$0 = var1;
      this.world = world;
   }

   protected Location location(BlockVector3 position) {
      return new Location(this.world, position.getX(), position.getY(), position.getZ());
   }

   public BaseBlock getFullBlock(BlockVector3 position) {
      BaseBlock var2 = super.getFullBlock(position);
      ChunkBlockTracker var3 = this.this$0.crops();
      if (var3 == null) {
         return var2;
      }

      ByteNamedKey var4 = var3.getBlockStateKey(this.location(position).getBlock());
      if (var4 == null) {
         return var2;
      }

      HashMap var5 = new HashMap();
      if (var2.getNbtData() != null) {
         var5.putAll(var2.getNbtData().getValue());
      }

      var5.put("ItemsAdderCropId", new StringTag(var4.namespacedId()));
      var5.put("ItemsAdderCropAge", new ByteTag(var4.age()));
      return var2.toImmutableState().toBaseBlock(new CompoundTag(var5));
   }

   public boolean setBlock(BlockVector3 position, BlockStateHolder state) {
      ByteNamedKey var3 = this.this$0.cropOf(state);
      boolean var4;
      if (this.this$0.missingDefinition(state)) {
         var4 = super.setBlock(position, BlockTypes.AIR.getDefaultState());
      } else if (state instanceof BaseBlock var5) {
         var4 = super.setBlock(position, this.this$0.withoutItemsAdderTags(var5));
      } else {
         var4 = super.setBlock(position, state);
      }

      this.this$0.applyCrop(this.location(position), var3);
      return var4 || var3 != null;
   }
}
