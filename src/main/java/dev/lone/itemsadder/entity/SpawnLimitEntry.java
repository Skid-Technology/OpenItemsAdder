package dev.lone.itemsadder.entity;

import org.jetbrains.annotations.Nullable;

public class SpawnLimitEntry {
   @Nullable
   private final Integer arY;
   @Nullable
   private final Boolean arZ;

   public SpawnLimitEntry(@Nullable Integer var1, @Nullable Boolean var2) {
      this.arY = var1;
      this.arZ = var2;
   }

   @Nullable
   public Integer limit() {
      return this.arY;
   }

   @Nullable
   public Boolean enabled() {
      return this.arZ;
   }
}
