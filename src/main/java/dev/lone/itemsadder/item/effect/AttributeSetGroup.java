package dev.lone.itemsadder.item.effect;

import java.util.Map;

class AttributeSetGroup {
   private final Map alv;
   private final Map alw;

   AttributeSetGroup(Map var1, Map var2) {
      this.alv = var1;
      this.alw = var2;
   }

   public Map CE() {
      return this.alv;
   }

   public Map CF() {
      return this.alw;
   }
}
