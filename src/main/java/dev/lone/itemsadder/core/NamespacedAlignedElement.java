package dev.lone.itemsadder.core;

import dev.lone.itemsadder.lang.TextFormatCache;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import java.util.List;
import lombok.Generated;

public class NamespacedAlignedElement extends NamespacedElementWithEffects {
   final CustomTexture acb;
   final CustomTexture acc;
   final CustomTexture acd;
   final int ace;
   final int acf = 0;
   final ElementAlignment acg;

   public NamespacedAlignedElement(
      PlayerEffectState var1,
      String var2,
      String var3,
      String var4,
      ElementAlignment var5,
      CustomTexture var6,
      CustomTexture var7,
      CustomTexture var8,
      int var9,
      int var10,
      boolean var11,
      boolean var12,
      boolean var13,
      boolean var14,
      float var15,
      float var16,
      float var17,
      List var18
   ) {
      super(var1, var2, var3, var4, var9, var11, var12, var13, var14, var15, var16, var17, var18);
      this.acg = var5;
      this.acb = var6;
      this.acc = var7;
      this.acd = var8;
      this.ace = var10;
   }

   public NamespacedAlignedElement method504(PlayerEffectState var1) {
      NamespacedAlignedElement var2 = new NamespacedAlignedElement(
         var1,
         this.dk,
         this.dl,
         this.abF,
         this.acg,
         this.acb,
         this.acc,
         this.acd,
         this.abp,
         this.ace,
         this.abu,
         this.abv,
         this.abw,
         this.abx,
         this.abI,
         this.abJ,
         this.abK,
         this.abo
      );
      var2.method501(this);
      this.zF();
      return var2;
   }

   @Override
   public String zD() {
      if (this.abr == null) {
         return "";
      }

      float var1 = this.zP();
      if (this.abB != null && !this.abE) {
         return this.abB;
      }

      if (this.acb != null && this.acc != null && this.acd != null) {
         StringBuilder var2 = new StringBuilder();

         for (int var3 = 0; var3 < var1; var3++) {
            if (var3 < var1 && var3 == (int)var1) {
               var2.append(TextFormatCache.method9634(this.acc.getFormattedText(), 0));
            } else {
               var2.append(TextFormatCache.method9634(this.acb.getFormattedText(), 0));
            }
         }

         for (int var6 = (int)var1; var6 < this.abK; var6++) {
            if (!(var6 < var1) || var6 != (int)var1) {
               var2.append(TextFormatCache.method9634(this.acd.getFormattedText(), 0));
            }
         }

         if (this.acg == ElementAlignment.LEFT) {
            var2 = new StringBuilder(new StringBuilder(var2.toString()).reverse().toString());
         }

         var2 = new StringBuilder(TextFormatCache.method9634(var2.toString(), this.abr));
         var2 = new StringBuilder(TextFormatCache.method9634(var2.toString(), this.abp));
         this.abB = var2.toString();
         this.abE = false;
         return this.abB;
      } else {
         return "";
      }
   }

   @Override
   public int zC() {
      return !this.zE() ? 0 : this.ace * this.acb.getWidth();
   }

   @Generated
   public CustomTexture zV() {
      return this.acb;
   }
}
