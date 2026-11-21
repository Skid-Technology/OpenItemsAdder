package dev.lone.itemsadder.item;

import java.util.List;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;

@Generated
public class ItemFeatureSpecBuilder {
   @Generated
   private int apV;
   @Generated
   private float aef;
   @Generated
   private boolean apX;
   @Generated
   private int apY;
   @Generated
   private String FD;
   @Generated
   private String wQ;
   @Generated
   private Boolean aqb;
   @Generated
   private List aqc;

   @Generated
   ItemFeatureSpecBuilder() {
   }

   @Generated
   public ItemFeatureSpecBuilder bz(int var1) {
      this.apV = var1;
      return this;
   }

   @Generated
   public ItemFeatureSpecBuilder method10004(float var1) {
      this.aef = var1;
      return this;
   }

   @Generated
   public ItemFeatureSpecBuilder aZ(boolean var1) {
      this.apX = var1;
      return this;
   }

   @Generated
   public ItemFeatureSpecBuilder bA(int var1) {
      this.apY = var1;
      return this;
   }

   @Generated
   public ItemFeatureSpecBuilder cY(@Nullable String var1) {
      this.FD = var1;
      return this;
   }

   @Generated
   public ItemFeatureSpecBuilder cZ(@Nullable String var1) {
      this.wQ = var1;
      return this;
   }

   @Generated
   public ItemFeatureSpecBuilder method10005(@Nullable Boolean var1) {
      this.aqb = var1;
      return this;
   }

   @Generated
   public ItemFeatureSpecBuilder method10006(@Nullable List var1) {
      this.aqc = var1;
      return this;
   }

   @Generated
   public ItemFeatureSpec EB() {
      return new ItemFeatureSpec(this.apV, this.aef, this.apX, this.apY, this.FD, this.wQ, this.aqb, this.aqc);
   }

   @Generated
   @Override
   public String toString() {
      return "ConsumableData.ConsumableDataBuilder(nutrition="
         + this.apV
         + ", saturation="
         + this.aef
         + ", canAlwaysEat="
         + this.apX
         + ", consumeSeconds="
         + this.apY
         + ", animation="
         + this.FD
         + ", sound="
         + this.wQ
         + ", particles="
         + this.aqb
         + ", effects="
         + this.aqc
         + ")";
   }
}
