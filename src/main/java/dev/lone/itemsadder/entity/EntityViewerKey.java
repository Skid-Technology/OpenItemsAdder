package dev.lone.itemsadder.entity;

import java.util.UUID;
import org.bukkit.entity.LivingEntity;

public class EntityViewerKey {
   private final UUID pP;
   private final LivingEntity pQ;

   public EntityViewerKey(UUID var1, LivingEntity var2) {
      this.pP = var1;
      this.pQ = var2;
   }

   public UUID uuid() {
      return this.pP;
   }

   public LivingEntity entity() {
      return this.pQ;
   }
}
