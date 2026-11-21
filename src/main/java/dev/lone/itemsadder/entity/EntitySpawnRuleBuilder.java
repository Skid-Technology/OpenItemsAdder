package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.util.IntRange;
import java.util.List;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;

@Generated
public class EntitySpawnRuleBuilder {
   @Generated
   private CustomMobSettings ql;
   @Generated
   private float qm;
   @Generated
   private int qn;
   @Generated
   private List qo;
   @Generated
   private List qp;
   @Generated
   private SpawnGroup field105;
   @Generated
   private SpawnBoundsPair qr;
   @Generated
   private List qs;
   @Generated
   private List qt;
   @Generated
   private IntRange qu;

   @Generated
   EntitySpawnRuleBuilder() {
   }

   @Generated
   public EntitySpawnRuleBuilder settings(CustomMobSettings var1) {
      this.ql = var1;
      return this;
   }

   @Generated
   public EntitySpawnRuleBuilder weight(float var1) {
      this.qm = var1;
      return this;
   }

   @Generated
   public EntitySpawnRuleBuilder maxPerChunk(int var1) {
      this.qn = var1;
      return this;
   }

   @Generated
   public EntitySpawnRuleBuilder method8383(@Nullable List var1) {
      this.qo = var1;
      return this;
   }

   @Generated
   public EntitySpawnRuleBuilder method8384(@Nullable List var1) {
      this.qp = var1;
      return this;
   }

   @Generated
   public EntitySpawnRuleBuilder group(SpawnGroup var1) {
      this.field105 = var1;
      return this;
   }

   @Generated
   public EntitySpawnRuleBuilder bounds(@Nullable SpawnBoundsPair var1) {
      this.qr = var1;
      return this;
   }

   @Generated
   public EntitySpawnRuleBuilder method8387(@Nullable List var1) {
      this.qs = var1;
      return this;
   }

   @Generated
   public EntitySpawnRuleBuilder method8388(@Nullable List var1) {
      this.qt = var1;
      return this;
   }

   @Generated
   public EntitySpawnRuleBuilder range(@Nullable IntRange var1) {
      this.qu = var1;
      return this;
   }

   @Generated
   public EntitySpawnRule build() {
      return EntitySpawnRule.create(this.ql, this.qm, this.qn, this.qo, this.qp, this.field105, this.qr, this.qs, this.qt, this.qu);
   }

   @Generated
   @Override
   public String toString() {
      return "EntityPopulator.EntityPopulatorBuilder(entity="
         + this.ql
         + ", chance="
         + this.qm
         + ", amount="
         + this.qn
         + ", biomes="
         + this.qo
         + ", worldNameMatchRules="
         + this.qp
         + ", spawnLogic="
         + this.field105
         + ", locationRange="
         + this.qr
         + ", weathers="
         + this.qs
         + ", timeIntervals="
         + this.qt
         + ", lightLevelRange="
         + this.qu
         + ")";
   }
}
