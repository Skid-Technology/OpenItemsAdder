package dev.lone.itemsadder.Core.OtherPlugins;

import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.util.Location;
import java.util.UUID;

final class WorldEditIntegration$EntityExtent extends AbstractDelegateExtent {
   private WorldEditIntegration$EntityExtent(WorldEditIntegration var1, Extent extent) {
      super(extent);
      this.this$0 = var1;
   }

   public Entity createEntity(Location location, BaseEntity state) {
      return this.this$0.missingEntityDefinition(state) ? null : super.createEntity(location, this.this$0.withEntityTypeId(state));
   }

   public Entity createEntity(Location location, BaseEntity state, UUID uuid) {
      return this.this$0.missingEntityDefinition(state) ? null : super.createEntity(location, this.this$0.withEntityTypeId(state), uuid);
   }
}
