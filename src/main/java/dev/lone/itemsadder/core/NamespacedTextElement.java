package dev.lone.itemsadder.core;

import dev.lone.itemsadder.lang.TextFormatCache;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class NamespacedTextElement extends NamespacedElementWithEffects {
   final List abP;
   final int abQ = 0;
   float abR;

   public NamespacedTextElement(
      PlayerEffectState var1,
      String var2,
      String var3,
      String var4,
      int var5,
      boolean var6,
      boolean var7,
      boolean var8,
      boolean var9,
      float var10,
      float var11,
      float var12,
      List var13
   ) {
      super(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
      this.abP = Collections.synchronizedList(new ArrayList());
   }

   public NamespacedTextElement method505(PlayerEffectState var1) {
      NamespacedTextElement var2 = new NamespacedTextElement(
         var1, this.dk, this.dl, this.abF, this.abp, this.abu, this.abv, this.abw, this.abx, this.abI, this.abJ, this.abK, this.abo
      );
      var2.method501(this);
      this.zF();
      return var2;
   }

   @Override
   public float method499(float var1) {
      if (this.abR == var1) {
         return this.abR;
      }

      this.abR = var1;
      return this.abR;
   }

   @Override
   public float zP() {
      return this.abR;
   }

   @Nullable
   @Override
   public Float method500(@Nullable Float var1) {
      return this.zP();
   }

   @Override
   public int zC() {
      if (!this.zE()) {
         return 0;
      }

      synchronized (this.abP) {
         int var2 = 0;

         for (int var3 = 0; var3 < this.abP.size(); var3++) {
            CustomTexture var4 = (CustomTexture)this.abP.get(var3);
            var2 += var4.getWidth();
         }

         return var2;
      }
   }

   @Override
   public String zD() {
      if (this.abr == null) {
         return "";
      }

      if (this.abB != null && !this.abE) {
         return this.abB;
      }

      synchronized (this.abP) {
         StringBuilder var2 = new StringBuilder();

         for (int var3 = 0; var3 < this.abP.size(); var3++) {
            CustomTexture var4 = (CustomTexture)this.abP.get(var3);
            var2.append(var4.getFormattedText());
         }

         var2 = new StringBuilder(TextFormatCache.method9634(var2.toString(), 0));
         var2 = new StringBuilder(TextFormatCache.method9634(var2.toString(), this.abr));
         var2 = new StringBuilder(TextFormatCache.method9634(var2.toString(), this.abp));
         this.abB = var2.toString();
         this.abE = false;
         return this.abB;
      }
   }

   public void method506(CustomTexture var1) {
      this.zF();
      synchronized (this.abP) {
         this.abP.add(var1);
      }
   }

   public void method507(int var1, CustomTexture var2) {
      this.zF();
      synchronized (this.abP) {
         this.abP.add(var1, var2);
      }
   }

   public void bp(int var1) {
      this.zF();
      synchronized (this.abP) {
         this.abP.remove(var1);
      }
   }

   public void zT() {
      this.zF();
      synchronized (this.abP) {
         this.abP.clear();
      }
   }

   public int zU() {
      synchronized (this.abP) {
         return this.abP.size();
      }
   }
}
