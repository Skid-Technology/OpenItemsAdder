package dev.lone.itemsadder.entity;

import beer.devs.nativepackets.api.NativeValue;

class EquipmentEntry {
   private final EquipmentSlotRecord xf;
   private final EquipmentItemRecord xg;
   private final String xh;
   private final float xi;
   private final NativeValue xj;

   private EquipmentEntry(EquipmentSlotRecord var1, EquipmentItemRecord var2, String var3, float var4, NativeValue var5) {
      this.xf = var1;
      this.xg = var2;
      this.xh = var3;
      this.xi = var4;
      this.xj = var5;
   }

   public EquipmentSlotRecord slot() {
      return this.xf;
   }

   public EquipmentItemRecord item() {
      return this.xg;
   }

   public String lY() {
      return this.xh;
   }

   public float scale() {
      return this.xi;
   }

   public NativeValue nativeValue() {
      return this.xj;
   }
}
