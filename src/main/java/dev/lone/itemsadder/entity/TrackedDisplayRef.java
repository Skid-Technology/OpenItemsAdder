package dev.lone.itemsadder.entity;

import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.joml.Matrix4f;

class TrackedDisplayRef {
   private final ModelPartDisplay LJ;
   private final TrackedDisplayEntity LK;

   private TrackedDisplayRef(ModelPartDisplay var1, TrackedDisplayEntity var2) {
      this.LJ = var1;
      this.LK = var2;
   }

   void sP() {
      CustomEntityImpl var1 = this.LJ.Hm;
      if (!var1.isRemoved() && var1.getModelPart(this.LJ.Ho.getId()) == this.LJ && !var1.isRagdolling()) {
         Location var2 = var1.getBaseLocation();
         Set var3 = this.LJ.isVisibleToViewers()
            ? var1.getViewers().stream().filter(EntityViewer::kW).map(var0 -> var0.uz).collect(Collectors.toUnmodifiableSet())
            : Set.of();
         this.LK.update(var1.getEntityUuid(), var2.getX(), var2.getY(), var2.getZ(), this.LJ.getScaledPartMatrix(new Matrix4f()), var3);
      } else {
         this.LK.close();
      }
   }

   public ModelPartDisplay part() {
      return this.LJ;
   }

   public TrackedDisplayEntity entity() {
      return this.LK;
   }
}
