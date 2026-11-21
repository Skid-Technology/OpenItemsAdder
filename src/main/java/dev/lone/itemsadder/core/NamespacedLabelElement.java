package dev.lone.itemsadder.core;

import dev.lone.itemsadder.lang.TextFormatCache;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class NamespacedLabelElement extends NamespacedElementWithEffects {
   public final List abX;
   @Nullable
   CustomTexture abY;
   float abZ;
   final int aca = 0;

   public NamespacedLabelElement(
      PlayerEffectState var1,
      String var2,
      String var3,
      String var4,
      List var5,
      int var6,
      boolean var7,
      boolean var8,
      boolean var9,
      boolean var10,
      float var11,
      float var12,
      float var13,
      List var14
   ) {
      super(var1, var2, var3, var4, var6, var7, var8, var9, var10, var11, var12, var13, var14);
      this.abX = var5;
   }

   public NamespacedLabelElement method502(PlayerEffectState var1) {
      NamespacedLabelElement var2 = new NamespacedLabelElement(
         var1, this.dk, this.dl, this.abF, this.abX, this.abp, this.abu, this.abv, this.abw, this.abx, this.abI, this.abJ, this.abK, this.abo
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

      CustomTexture var2 = this.method503(var1);
      if (var2 == null) {
         return "";
      }

      String var3 = var2.getFormattedText();
      var3 = TextFormatCache.method9634(var3, 0);
      var3 = TextFormatCache.method9634(var3, this.abr);
      var3 = TextFormatCache.method9634(var3, this.abp);
      this.abB = var3;
      this.abE = false;
      return var3;
   }

   @Nullable
   public CustomTexture method503(float var1) {
      int var2;
      if (var1 <= this.abJ) {
         var2 = 0;
      } else if (var1 >= this.abJ + this.abX.size() - 1.0F) {
         var2 = this.abX.size() - 1;
      } else {
         var2 = (int)(var1 - this.abJ);
      }

      CustomTexture var3 = (CustomTexture)this.abX.get(var2);
      this.abY = var3;
      this.abZ = var1;
      return var3;
   }

   @Override
   public int zC() {
      if (!this.zE()) {
         return 0;
      }

      if (this.abY == null) {
         this.method503(this.zP());
      }

      return this.abY.getWidth();
   }
}
