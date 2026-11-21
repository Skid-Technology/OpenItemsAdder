package dev.lone.itemsadder.item;

import lombok.Generated;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public abstract class ItemSpec {
   public static final String apv = "normal";
   final String apw;
   @Nullable
   private final String apx;
   private final Material apy;
   @Nullable
   private final Boolean apz;
   @Nullable
   private final Boolean apA;
   @Nullable
   private final Integer apB;
   @Nullable
   private final Integer apC;
   @Nullable
   private ItemRuleSpec aob;

   public ItemSpec(
      String var1, Material var2, @Nullable String var3, @Nullable Boolean var4, @Nullable Boolean var5, @Nullable Integer var6, @Nullable Integer var7
   ) {
      this.apw = var1;
      this.apx = var3;
      this.apy = var2;
      this.apz = var4;
      this.apA = var5;
      this.apB = var6;
      this.apC = var7;
   }

   public String Eh() {
      return "ia_overlay_1_21_4_plus/assets/" + this.apw.split(":")[0] + "/items/ia_auto/" + this.apw.split(":")[1] + ".json";
   }

   public String Ei() {
      return this.apw.split(":")[0] + ":ia_auto/" + this.apw.split(":")[1];
   }

   public abstract String Ej();

   public abstract String cU(@Nullable String var1);

   public abstract String Ek();

   public abstract String cV(String var1);

   @Nullable
   public String El() {
      if (this.apx == null) {
         return null;
      }

      String var1 = this.apw.split(":")[0];
      String var2 = this.apw.split(":")[1];
      return "assets/" + var1 + "/models/item/ia_auto/" + var2 + "_icon.json";
   }

   public String Em() {
      if (this.apx == null) {
         return null;
      }

      String var1 = this.apw.split(":")[0];
      String var2 = this.apw.split(":")[1];
      return var1 + ":item/ia_auto/" + var2 + "_icon";
   }

   public boolean En() {
      return this.apx != null && !this.apx.isEmpty();
   }

   @Generated
   public String Eo() {
      return this.apw;
   }

   @Nullable
   @Generated
   public String Du() {
      return this.apx;
   }

   @Generated
   public Material Ep() {
      return this.apy;
   }

   @Nullable
   @Generated
   public Boolean Eq() {
      return this.apz;
   }

   @Nullable
   @Generated
   public Boolean Er() {
      return this.apA;
   }

   @Nullable
   @Generated
   public Integer Es() {
      return this.apB;
   }

   @Nullable
   @Generated
   public Integer Et() {
      return this.apC;
   }

   @Nullable
   @Generated
   public ItemRuleSpec DP() {
      return this.aob;
   }

   @Generated
   public void method9992(@Nullable ItemRuleSpec var1) {
      this.aob = var1;
   }
}
