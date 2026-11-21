package dev.lone.itemsadder.entity;

import java.util.UUID;

public class EquipmentSlotRecord {
   private final String CI;
   private final String CJ;
   private final UUID CK;
   private final String CL;
   private final Vector3fRecord CM;
   private final Vector3fRecord CN;
   private final Vector3fRecord CO;
   private final float CP;

   public EquipmentSlotRecord(String var1, String var2, UUID var3, String var4, Vector3fRecord var5, Vector3fRecord var6, Vector3fRecord var7, float var8) {
      this.CI = var1;
      this.CJ = var2;
      this.CK = var3;
      this.CL = var4;
      this.CM = var5;
      this.CN = var6;
      this.CO = var7;
      this.CP = var8;
   }

   public String ph() {
      return this.CI;
   }

   public String pi() {
      return this.CJ;
   }

   public UUID pj() {
      return this.CK;
   }

   public String bone() {
      return this.CL;
   }

   public Vector3fRecord translation() {
      return this.CM;
   }

   public Vector3fRecord rotation() {
      return this.CN;
   }

   public Vector3fRecord scale() {
      return this.CO;
   }

   public float size() {
      return this.CP;
   }
}
