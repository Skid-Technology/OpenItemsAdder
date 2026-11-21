package dev.lone.itemsadder.entity;

public class Vector3fRecord {
   private final float CQ;
   private final float CR;
   private final float CS;

   public Vector3fRecord(float var1, float var2, float var3) {
      this.CQ = var1;
      this.CR = var2;
      this.CS = var3;
   }

   public float x() {
      return this.CQ;
   }

   public float y() {
      return this.CR;
   }

   public float z() {
      return this.CS;
   }
}
