package dev.lone.itemsadder.entity;

import java.util.List;

public class SpawnGroupBuilder {
   private SpawnMode qC;
   private List qD;

   public SpawnGroupBuilder mode(SpawnMode var1) {
      this.qC = var1;
      return this;
   }

   public SpawnGroupBuilder members(List var1) {
      this.qD = var1;
      return this;
   }

   public SpawnMode getMode() {
      return this.qC;
   }

   public List getMembers() {
      return this.qD;
   }

   public SpawnGroup build() {
      return new SpawnGroup(this.qC, this.qD);
   }
}
