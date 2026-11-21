package dev.lone.itemsadder.api;

import java.util.UUID;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class CustomEntity$DamageablePart {
   private final UUID id;
   private final String bone;
   @Nullable
   private final Float health;
   private final boolean broken;
   @Nullable
   private final LivingEntity hitbox;

   public CustomEntity$DamageablePart(UUID id, String bone, @Nullable Float health, boolean broken, @Nullable LivingEntity hitbox) {
      this.id = id;
      this.bone = bone;
      this.health = health;
      this.broken = broken;
      this.hitbox = hitbox;
   }
}
