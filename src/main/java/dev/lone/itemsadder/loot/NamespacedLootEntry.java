package dev.lone.itemsadder.loot;

import lombok.Generated;

public class NamespacedLootEntry extends LootTable {
   String namespacedId;

   public NamespacedLootEntry(String var1, boolean var2) {
      super(var2);
      this.namespacedId = var1;
   }

   @Generated
   public String Eo() {
      return this.namespacedId;
   }
}
