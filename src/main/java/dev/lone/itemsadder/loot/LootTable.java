package dev.lone.itemsadder.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Generated;

public class LootTable extends NbtDropConditions {
   final List aty;
   final List atz;
   final boolean atA;

   public LootTable(boolean var1) {
      this.atA = var1;
      this.aty = new ArrayList();
      this.atz = new ArrayList();
   }

   public boolean Ha() {
      return !this.aty.isEmpty();
   }

   @Override
   public void method1997(NbtDropTrigger var1) {
      this.aty.add(var1);
      super.method1997(var1);
   }

   public void method1998(MetadataDropTrigger var1) {
      this.aty.add(var1);
      this.atz.add(var1);
   }

   public boolean Hb() {
      return !this.atz.isEmpty();
   }

   public Optional dy(String var1) {
      if (!this.Hb()) {
         return Optional.empty();
      }

      for (MetadataDropTrigger var3 : this.atz) {
         if (var3.yK.equals(var1)) {
            return Optional.of(var3);
         }
      }

      return Optional.empty();
   }

   @Generated
   public List Hc() {
      return this.aty;
   }

   @Generated
   public boolean Hd() {
      return this.atA;
   }
}
