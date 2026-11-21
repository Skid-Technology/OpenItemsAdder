package dev.lone.itemsadder.anim;

import dev.lone.itemsadder.entity.AnimationBlend;
import dev.lone.itemsadder.entity.ModelDefinitionData;
import org.jetbrains.annotations.Nullable;

public final class BlendState {
   private final ModelDefinitionData[] LB;
   private final float LC;
   private final float LD;
   private final float LE;
   private boolean LF;
   private float LG;
   private float LH;
   private float LI;

   private BlendState(AnimationBlend var1) {
      this.LB = new ModelDefinitionData[]{var1.method65(), var1.op(), var1.oq()};
      this.LC = var1.or();
      this.LD = var1.os();
      this.LE = var1.ot();
   }

   @Nullable
   static BlendState method9063(@Nullable AnimationBlend var0) {
      return var0 == null ? null : new BlendState(var0);
   }

   boolean method9064(ModelDefinitionData var1) {
      return var1 == this.LB[0];
   }

   void method9065(float var1, long var2) {
      if (!this.LF) {
         this.LF = true;
         this.LG = var1;
         this.LH = 0.0F;
      } else {
         float var4 = method9067(var1 - this.LG) / (float)Math.max(1L, var2);
         this.LG = var1;
         float var5 = Math.abs(var4);
         this.LH = var5 <= this.LC ? 0.0F : Math.copySign(Math.min(1.0F, (var5 - this.LC) / (this.LD - this.LC)), var4);
      }
   }

   boolean aq(boolean var1) {
      if (!var1) {
         return false;
      }

      float var2 = this.LI + (this.LH - this.LI) * this.LE;
      if (this.LI * var2 < 0.0F) {
         var2 = 0.0F;
      }

      if (Math.abs(var2 - this.LH) < 1.0E-4F) {
         var2 = this.LH;
      }

      boolean var3 = var2 != this.LI;
      this.LI = var2;
      return var3;
   }

   void method9066(float[] var1) {
      var1[0] = 1.0F - Math.abs(this.LI);
      var1[1] = Math.max(-this.LI, 0.0F);
      var1[2] = Math.max(this.LI, 0.0F);
   }

   float sO() {
      return this.LI;
   }

   static float method9067(float var0) {
      var0 %= 360.0F;
      if (var0 >= 180.0F) {
         var0 -= 360.0F;
      }

      if (var0 < -180.0F) {
         var0 += 360.0F;
      }

      return var0;
   }
}
