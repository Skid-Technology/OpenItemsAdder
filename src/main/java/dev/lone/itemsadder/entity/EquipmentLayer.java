package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.resourcepack.HumanoidEquipmentPack;
import java.util.Map;

class EquipmentLayer {
   private final HumanoidEquipmentPack xk;
   private final HumanoidEquipmentSet xl;
   private final EquipmentSnapshot xm;
   private final Map xn;

   private EquipmentLayer(HumanoidEquipmentPack var1, HumanoidEquipmentSet var2, EquipmentSnapshot var3, Map var4) {
      this.xk = var1;
      this.xl = var2;
      this.xm = var3;
      this.xn = var4;
   }

   public HumanoidEquipmentPack pack() {
      return this.xk;
   }

   public HumanoidEquipmentSet set() {
      return this.xl;
   }

   public EquipmentSnapshot snapshot() {
      return this.xm;
   }

   public Map mappings() {
      return this.xn;
   }
}
