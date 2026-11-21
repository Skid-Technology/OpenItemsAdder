package dev.lone.itemsadder.entity;

import java.util.List;
import org.joml.Matrix4f;

public final class BoneTransformTable {
   public static final int DE = 65536;
   private final BoneTransformEntry[][] DF;

   public BoneTransformTable(ModelDefinitionData var1, List var2) {
      this.DF = new BoneTransformEntry[var1.getMaxPartCount()][var2.size()];
      TransformDecomposition[] var3 = new TransformDecomposition[var2.size()];
      TransformScratch var4 = new TransformScratch();
      Matrix4f var5 = new Matrix4f();
      Matrix4f var6 = new Matrix4f().scaling(-1.0F, 1.0F, -1.0F);

      for (int var7 = 0; var7 < this.DF.length; var7++) {
         for (int var8 = 0; var8 < var2.size(); var8++) {
            CustomEntityDefinition var9 = (CustomEntityDefinition)var2.get(var8);
            CustomEntityDefinition var10 = var9.getParent();
            int var11 = var10 == null ? -1 : var10.nH();
            TransformDecomposition var12 = var1.isBoneAnimated(var8) ? var1.getPartDecomposition(var8, var7, false) : null;
            if (var12 == null) {
               var12 = var9.getDecomposition();
            }

            if (var7 <= 0 || var12 != var3[var8] || var11 >= 0 && this.DF[var7][var11] != this.DF[var7 - 1][var11]) {
               TransformBuilder var13 = new TransformBuilder(var4);
               var9.getDecomposition().applyTo(var13);
               var13.method8668(var12, var1.getBoneFlag(var8), 1.0F);
               var13.method8687(var5);
               Matrix4f var14 = var11 < 0 ? new Matrix4f(var5) : new Matrix4f(this.DF[var7][var11].DH).mul(var5);
               Matrix4f var15 = new Matrix4f(var6).scale(1.0F).mul(var14).scale(var9.getScaleFactor());
               this.DF[var7][var8] = new BoneTransformEntry(var13, var14, var15, !TransformDecomposition.method8657(var15));
            } else {
               this.DF[var7][var8] = this.DF[var7 - 1][var8];
            }

            var3[var8] = var12;
         }
      }
   }

   public int method8898(int var1, boolean var2) {
      return var2 ? this.DF.length - 1 : Math.min(Math.max(var1, 0), this.DF.length - 1);
   }

   public boolean method8899(int var1, int var2, int var3) {
      return var2 >= 0 && this.DF[var2][var1] == this.DF[var3][var1];
   }

   public boolean method8900(int var1, int var2) {
      return this.DF[var1][var2].DJ;
   }

   public void method8901(int var1, int var2, TransformBuilder var3) {
      var3.copyOf(this.DF[var1][var2].DG);
   }

   public void method8902(int var1, int var2, Matrix4f var3) {
      var3.set(this.DF[var1][var2].DH);
   }

   public void method8903(int var1, int var2, Matrix4f var3) {
      var3.set(this.DF[var1][var2].DI);
   }
}
