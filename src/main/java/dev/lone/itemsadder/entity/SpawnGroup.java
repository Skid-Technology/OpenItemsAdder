package dev.lone.itemsadder.entity;

import java.util.List;

public class SpawnGroup {
   private final SpawnMode qC;
   private final List qD;

   public SpawnGroup(SpawnMode var1, List var2) {
      this.qC = var1;
      this.qD = var2;
   }

   public SpawnMode mode() {
      return this.qC;
   }

   public List members() {
      return this.qD;
   }
}
