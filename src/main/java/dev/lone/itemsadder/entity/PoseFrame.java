package dev.lone.itemsadder.entity;

import java.util.List;

public class PoseFrame {
   private final CustomEntityDefinition BH;
   private final List BI;
   private final float BJ;
   private final float BK;
   private final float BL;

   public PoseFrame(CustomEntityDefinition var1, List var2, float var3, float var4, float var5) {
      this.BH = var1;
      this.BI = var2;
      this.BJ = var3;
      this.BK = var4;
      this.BL = var5;
   }

   public CustomEntityDefinition definition() {
      return this.BH;
   }

   public List samples() {
      return this.BI;
   }

   public float offsetX() {
      return this.BJ;
   }

   public float offsetY() {
      return this.BK;
   }

   public float offsetZ() {
      return this.BL;
   }
}
