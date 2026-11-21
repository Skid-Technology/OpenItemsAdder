package dev.lone.itemsadder.api;

import dev.lone.itemsadder.api.CustomEntity.Attachment;
import dev.lone.itemsadder.api.CustomEntity.MountBone;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.ModelPartDisplay;
import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.util.Transformation;
import org.joml.Matrix4f;

public class CustomEntity$Bone {
   int ordinal;
   int id;
   ModelPartDisplay internal;

   static CustomEntity$Bone wrap(ModelPartDisplay boneRenderer, int index) {
      return boneRenderer.getRider() != null ? new MountBone(index, boneRenderer) : new CustomEntity$Bone(index, boneRenderer);
   }

   CustomEntity$Bone(int ordinal, ModelPartDisplay internal) {
      this.ordinal = ordinal;
      this.internal = internal;
      this.id = internal.Hn.aQn;
   }

   public String getName() {
      return this.internal.Ho.getId();
   }

   public Attachment attachModel(String namespacedId) {
      return new Attachment(CustomEntityManager.getInstance().attachModelPart(this.internal, namespacedId, new Matrix4f()));
   }

   public Attachment attachModel(String namespacedId, Transformation offset) {
      Objects.requireNonNull(offset, "offset");
      Matrix4f var3 = new Matrix4f()
         .translation(offset.getTranslation())
         .rotate(offset.getLeftRotation())
         .scale(offset.getScale())
         .rotate(offset.getRightRotation());
      return new Attachment(CustomEntityManager.getInstance().attachModelPart(this.internal, namespacedId, var3));
   }

   public int getOrdinal() {
      return this.ordinal;
   }

   public int getId() {
      return this.id;
   }

   public Location getLocation() {
      return this.internal.getPartLocation();
   }

   public int getColor() {
      if (this.internal.Hn == null) {
         throw new RuntimeException("Client entity is null! Something failed to load.");
      } else {
         return this.internal.getColorValue();
      }
   }

   public void setColor(int color) {
      if (this.internal.Hn == null) {
         throw new RuntimeException("Client entity is null! Something failed to load.");
      }

      this.internal.setColorSilently(color);
      this.internal.Hm.dispatchToManager(this.internal::refreshColor);
   }

   public boolean isVisible() {
      return this.internal.isEnchanted();
   }

   public void setVisible(boolean visible) {
      this.internal.setEnchantedFlag(visible);
      this.internal.Hm.dispatchToManager(this.internal::refreshVisibility);
   }

   public boolean getEnchanted() {
      if (this.internal.Hn == null) {
         throw new RuntimeException("Client entity is null! Something failed to load.");
      } else {
         return this.internal.hasEnchantGlint();
      }
   }

   public void setEnchanted(boolean enchanted) {
      if (this.internal.Hn == null) {
         throw new RuntimeException("Client entity is null! Something failed to load.");
      }

      this.internal.setEnchantGlintSilently(enchanted);
      this.internal.Hm.dispatchToManager(this.internal::refreshEnchantGlint);
   }
}
