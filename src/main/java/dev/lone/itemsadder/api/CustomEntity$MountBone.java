package dev.lone.itemsadder.api;

import dev.lone.itemsadder.api.CustomEntity.Bone;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.ModelPartDisplay;
import dev.lone.itemsadder.entity.ModelRiderController;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class CustomEntity$MountBone extends Bone {
   CustomEntity$MountBone(int ordinal, ModelPartDisplay internal) {
      super(ordinal, internal);
   }

   @Nullable
   public LivingEntity getPassenger() {
      ModelRiderController var1 = this.internal.getRider();
      return var1 == null ? null : var1.MZ;
   }

   public boolean setPassenger(LivingEntity entity) {
      ModelRiderController var2 = this.internal.getRider();
      if (var2 == null) {
         return false;
      }

      if (var2.isMounted()) {
         CustomEntityManager.getInstance().removePassenger(var2.MZ);
         if (var2.isMounted()) {
            return false;
         }
      }

      return CustomEntityManager.getInstance().mountRider(entity, var2);
   }

   public void removePassenger() {
      ModelRiderController var1 = this.internal.getRider();
      if (var1 != null) {
         CustomEntityManager.getInstance().removePassenger(var1.MZ);
      }
   }

   public void setLocked(boolean locked) {
      ModelRiderController var2 = this.internal.getRider();
      if (var2 != null) {
         var2.setLocked(locked);
      }
   }

   public boolean isLocked() {
      ModelRiderController var1 = this.internal.getRider();
      return var1 == null ? false : var1.isLocked();
   }

   public void setCanControl(boolean canControl) {
      ModelRiderController var2 = this.internal.getRider();
      if (var2 != null) {
         var2.setCanControl(canControl);
      }
   }

   public boolean canControl() {
      ModelRiderController var1 = this.internal.getRider();
      return var1 == null ? false : var1.canControl();
   }
}
