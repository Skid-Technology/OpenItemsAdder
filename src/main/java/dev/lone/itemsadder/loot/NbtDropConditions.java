package dev.lone.itemsadder.loot;

import java.util.ArrayList;
import java.util.List;

public class NbtDropConditions extends LootDropRules {
   final List aul = new ArrayList();

   public void method1997(NbtDropTrigger var1) {
      this.aul.add(var1);
   }

   public List Hv() {
      return this.aul;
   }

   public boolean Hw() {
      return this.aul.size() > 0;
   }
}
