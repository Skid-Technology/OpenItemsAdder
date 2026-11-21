package dev.lone.itemsadder.entity;

import org.jetbrains.annotations.Nullable;

public class EquipmentItemRecord {
   private final String CV;
   @Nullable
   private final Integer CW;
   @Nullable
   private final String CX;
   @Nullable
   private final String CY;
   private final boolean CZ;

   public EquipmentItemRecord(String var1, @Nullable Integer var2, @Nullable String var3, @Nullable String var4, boolean var5) {
      this.CV = var1;
      this.CW = var2;
      this.CX = var3;
      this.CY = var4;
      this.CZ = var5;
   }

   public String material() {
      return this.CV;
   }

   @Nullable
   public Integer customModelData() {
      return this.CW;
   }

   @Nullable
   public String pt() {
      return this.CX;
   }

   @Nullable
   public String pu() {
      return this.CY;
   }

   public boolean enchanted() {
      return this.CZ;
   }
}
