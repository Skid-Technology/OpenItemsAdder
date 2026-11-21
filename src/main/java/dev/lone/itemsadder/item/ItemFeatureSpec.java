package dev.lone.itemsadder.item;

import java.util.List;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;

public class ItemFeatureSpec {
   private final int apV;
   private final float apW;
   private final boolean apX;
   private final int apY;
   @Nullable
   private final String apZ;
   @Nullable
   private final String aqa;
   @Nullable
   private final Boolean aqb;
   @Nullable
   private final List aqc;

   public ItemFeatureSpec(
      int var1, float var2, boolean var3, int var4, @Nullable String var5, @Nullable String var6, @Nullable Boolean var7, @Nullable List var8
   ) {
      this.apV = var1;
      this.apW = var2;
      this.apX = var3;
      this.apY = var4;
      this.apZ = var5;
      this.aqa = var6;
      this.aqb = var7;
      this.aqc = var8;
   }

   @Generated
   public static ItemFeatureSpecBuilder Ev() {
      return new ItemFeatureSpecBuilder();
   }

   public int Ew() {
      return this.apV;
   }

   public float Ex() {
      return this.apW;
   }

   public boolean Ey() {
      return this.apX;
   }

   public int Ez() {
      return this.apY;
   }

   @Nullable
   public String rl() {
      return this.apZ;
   }

   @Nullable
   public String lP() {
      return this.aqa;
   }

   @Nullable
   public Boolean EA() {
      return this.aqb;
   }

   @Nullable
   public List zn() {
      return this.aqc;
   }
}
