package dev.lone.itemsadder.fontimage;

import java.io.File;
import org.jetbrains.annotations.Nullable;

public class FontImageSettingsBuilder {
   public String asL;
   public File asM;
   @Nullable
   public Float asN;
   @Nullable
   public Float asO;
   @Nullable
   public Integer apN;
   @Nullable
   public Boolean asQ;
   @Nullable
   public Integer asR;
   @Nullable
   public Boolean asS;
   @Nullable
   public Float asT;
   @Nullable
   public Integer cJ;
   @Nullable
   public Boolean cK;

   public FontImageSettingsBuilder(String var1, File var2) {
      this.asL = var1;
      this.asM = var2;
   }

   public FontImageSettingsBuilder(
      String var1,
      File var2,
      @Nullable Float var3,
      @Nullable Float var4,
      @Nullable Integer var5,
      @Nullable Boolean var6,
      @Nullable Integer var7,
      @Nullable Boolean var8,
      @Nullable Float var9,
      @Nullable Integer var10,
      @Nullable Boolean var11
   ) {
      this.asL = var1;
      this.asM = var2;
      this.asN = var3;
      this.asO = var4;
      this.apN = var5;
      this.asQ = var6;
      this.asR = var7;
      this.asS = var8;
      this.asT = var9;
      this.cJ = var10;
      this.cK = var11;
   }

   public FontImageSettingsBuilder method1947(@Nullable Float var1) {
      this.asN = var1;
      return this;
   }

   public FontImageSettingsBuilder method1948(@Nullable Float var1) {
      this.asO = var1;
      return this;
   }

   public FontImageSettingsBuilder method1949(@Nullable Integer var1) {
      this.apN = var1;
      return this;
   }

   public FontImageSettingsBuilder method1950(@Nullable Boolean var1) {
      this.asQ = var1;
      return this;
   }

   public FontImageSettingsBuilder method1951(@Nullable Integer var1) {
      this.asR = var1;
      return this;
   }

   public FontImageSettingsBuilder method1952(@Nullable Boolean var1) {
      this.asS = var1;
      return this;
   }

   public FontImageSettingsBuilder method1953(@Nullable Float var1) {
      this.asT = var1;
      return this;
   }

   public FontImageSettingsBuilder method1954(@Nullable Integer var1) {
      this.cJ = var1;
      return this;
   }

   public FontImageSettingsBuilder method1955(@Nullable Boolean var1) {
      this.cK = var1;
      return this;
   }

   public FontImageSettingsRecord method1956() {
      return new FontImageSettingsRecord(this.asL, this.asM, this.asN, this.asO, this.apN, this.asQ, this.asR, this.asS, this.asT, this.cJ, this.cK);
   }
}
