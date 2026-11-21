package dev.lone.itemsadder.fontimage;

import java.io.File;
import org.jetbrains.annotations.Nullable;

public class FontImageSettingsRecord {
   private final String asL;
   private final File asM;
   @Nullable
   private final Float asN;
   @Nullable
   private final Float asO;
   @Nullable
   private final Integer asP;
   @Nullable
   private final Boolean asQ;
   @Nullable
   private final Integer asR;
   @Nullable
   private final Boolean asS;
   @Nullable
   private final Float asT;
   @Nullable
   private final Integer asU;
   @Nullable
   private final Boolean asV;

   public FontImageSettingsRecord(
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
      this.asP = var5;
      this.asQ = var6;
      this.asR = var7;
      this.asS = var8;
      this.asT = var9;
      this.asU = var10;
      this.asV = var11;
   }

   public String path() {
      return this.asL;
   }

   public File Gw() {
      return this.asM;
   }

   @Nullable
   public Float Gx() {
      return this.asN;
   }

   @Nullable
   public Float Gy() {
      return this.asO;
   }

   @Nullable
   public Integer Gz() {
      return this.asP;
   }

   @Nullable
   public Boolean GA() {
      return this.asQ;
   }

   @Nullable
   public Integer GB() {
      return this.asR;
   }

   @Nullable
   public Boolean GC() {
      return this.asS;
   }

   @Nullable
   public Float GD() {
      return this.asT;
   }

   @Nullable
   public Integer GE() {
      return this.asU;
   }

   @Nullable
   public Boolean GF() {
      return this.asV;
   }
}
