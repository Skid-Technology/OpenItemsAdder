package dev.lone.itemsadder.loot;

import lombok.Generated;
import org.bukkit.entity.EntityType;

public class EntityLootTable extends LootTable {
   final EntityType aur;

   public EntityLootTable(EntityType var1, boolean var2) {
      super(var2);
      this.aur = var1;
   }

   @Generated
   public EntityType getType() {
      return this.aur;
   }
}
