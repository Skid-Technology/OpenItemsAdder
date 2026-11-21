package dev.lone.itemsadder.loot;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Generated;

@Generated
public abstract class AbstractLootConditions {
   @Generated
   private boolean atT;
   @Generated
   private List atU;
   @Generated
   private boolean atV;
   @Generated
   private List atW;
   @Generated
   private boolean atX;
   @Generated
   private List atY;
   @Generated
   private boolean atZ;
   @Generated
   private HashSet aua;
   @Generated
   private boolean aub;
   @Generated
   private HashSet auc;
   @Generated
   private boolean atH;
   @Generated
   private boolean atI;
   @Generated
   private Integer atJ;
   @Generated
   private Integer atK;
   @Generated
   private Double atL;
   @Generated
   private Double atM;
   @Generated
   private boolean atN;
   @Generated
   private boolean atO;
   @Generated
   private boolean atP;
   @Generated
   private boolean aud;
   @Generated
   private Set aue;
   @Generated
   private boolean auf;
   @Generated
   private Set aug;
   @Generated
   private boolean auh;
   @Generated
   private Set aui;

   @Generated
   public AbstractLootConditions method2019(List var1) {
      this.atU = var1;
      this.atT = true;
      return this.dE();
   }

   @Generated
   public AbstractLootConditions method2020(List var1) {
      this.atW = var1;
      this.atV = true;
      return this.dE();
   }

   @Generated
   public AbstractLootConditions method2021(List var1) {
      this.atY = var1;
      this.atX = true;
      return this.dE();
   }

   @Generated
   public AbstractLootConditions method2022(HashSet var1) {
      this.aua = var1;
      this.atZ = true;
      return this.dE();
   }

   @Generated
   public AbstractLootConditions method2023(HashSet var1) {
      this.auc = var1;
      this.aub = true;
      return this.dE();
   }

   @Generated
   public AbstractLootConditions bh(boolean var1) {
      this.atH = var1;
      return this.dE();
   }

   @Generated
   public AbstractLootConditions bi(boolean var1) {
      this.atI = var1;
      return this.dE();
   }

   @Generated
   public AbstractLootConditions method2024(Integer var1) {
      this.atJ = var1;
      return this.dE();
   }

   @Generated
   public AbstractLootConditions method2025(Integer var1) {
      this.atK = var1;
      return this.dE();
   }

   @Generated
   public AbstractLootConditions method2026(Double var1) {
      this.atL = var1;
      return this.dE();
   }

   @Generated
   public AbstractLootConditions method2027(Double var1) {
      this.atM = var1;
      return this.dE();
   }

   @Generated
   public AbstractLootConditions bj(boolean var1) {
      this.atN = var1;
      return this.dE();
   }

   @Generated
   public AbstractLootConditions bk(boolean var1) {
      this.atO = var1;
      return this.dE();
   }

   @Generated
   public AbstractLootConditions bl(boolean var1) {
      this.atP = var1;
      return this.dE();
   }

   @Generated
   public AbstractLootConditions method2028(Set var1) {
      this.aue = var1;
      this.aud = true;
      return this.dE();
   }

   @Generated
   public AbstractLootConditions method2029(Set var1) {
      this.aug = var1;
      this.auf = true;
      return this.dE();
   }

   @Generated
   public AbstractLootConditions method2030(Set var1) {
      this.aui = var1;
      this.auh = true;
      return this.dE();
   }

   @Generated
   protected abstract AbstractLootConditions dE();

   @Generated
   public abstract LootDropRules dD();

   @Generated
   @Override
   public String toString() {
      return "LootEntry.LootEntryBuilder(lootItems$value="
         + this.atU
         + ", lootExpDrops$value="
         + this.atW
         + ", lootExecuteCommands$value="
         + this.atY
         + ", biomes$value="
         + this.aua
         + ", worlds$value="
         + this.auc
         + ", dropOnlyFirst="
         + this.atH
         + ", hasAnyWorldRule="
         + this.atI
         + ", heightBelow="
         + this.atJ
         + ", heightAbove="
         + this.atK
         + ", temperatureBelow="
         + this.atL
         + ", temperatureAbove="
         + this.atM
         + ", hasWeatherRule="
         + this.atN
         + ", hasTimeRule="
         + this.atO
         + ", hasMoonPhaseRule="
         + this.atP
         + ", weather$value="
         + this.aue
         + ", time$value="
         + this.aug
         + ", moonPhases$value="
         + this.aui
         + ")";
   }
}
