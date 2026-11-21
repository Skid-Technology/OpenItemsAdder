package dev.lone.itemsadder.api;

import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
import org.bukkit.Bukkit;

public final class CustomEntity$Attachment {
   private final CustomEntity entity;

   private CustomEntity$Attachment(CustomEntityImpl renderer) {
      this.entity = new CustomEntity(renderer);
   }

   public CustomEntity getEntity() {
      return this.entity;
   }

   public void detach() {
      if (!Bukkit.isPrimaryThread()) {
         throw new IllegalStateException("Detach models on the server thread");
      }

      if (!this.entity.renderer.isRemoved()) {
         CustomEntityManager.getInstance().removeWithEffects(this.entity.renderer, false, false);
      }
   }
}
