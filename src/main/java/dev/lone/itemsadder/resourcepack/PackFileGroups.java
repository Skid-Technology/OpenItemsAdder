package dev.lone.itemsadder.resourcepack;

import java.util.List;

class PackFileGroups {
   private final List byk;
   private final List byl;

   private PackFileGroups(List var1, List var2) {
      this.byk = var1;
      this.byl = var2;
   }

   public List selected() {
      return this.byk;
   }

   public List assetNames() {
      return this.byl;
   }
}
