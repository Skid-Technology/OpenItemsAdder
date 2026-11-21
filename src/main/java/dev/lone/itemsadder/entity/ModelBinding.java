package dev.lone.itemsadder.entity;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public class ModelBinding {
   public final ModelDefinitionData AB;
   public final CustomEntityDefinition AC;
   private final List AD;
   private final ModelPartTransform[] AE;
   private final int AF;

   public ModelBinding(ModelDefinitionData var1, CustomEntityDefinition var2, List var3) {
      this.AB = var1;
      this.AC = var2;
      this.AD = var3;
      this.AE = null;
      this.AF = 0;
   }

   private ModelBinding(ModelDefinitionData var1, CustomEntityDefinition var2, ModelPartTransform[] var3, int var4) {
      this.AB = var1;
      this.AC = var2;
      this.AD = null;
      this.AE = var3;
      this.AF = var4;
   }

   public static ModelBinding create(ModelDefinitionData var0, CustomEntityDefinition var1, List var2) {
      ModelPartTransform[] var3 = var2.toArray(new ModelPartTransform[0]);
      if (var3.length > 1) {
         ModelPartTransform var4 = var3[0];
         boolean var5 = true;

         for (int var6 = 1; var6 < var3.length; var6++) {
            if (var3[var6] != var4) {
               var5 = false;
               break;
            }
         }

         if (var5) {
            var3 = new ModelPartTransform[]{var4};
         }
      }

      return new ModelBinding(var0, var1, var3, var2.size());
   }

   @Nullable
   public ModelPartTransform getPart(int var1) {
      if (var1 >= 0 && var1 < this.getPartCount()) {
         return this.AD != null ? (ModelPartTransform)this.AD.get(var1) : (this.AE.length == 1 ? this.AE[0] : this.AE[var1]);
      } else {
         return null;
      }
   }

   public int getPartCount() {
      return this.AD != null ? this.AD.size() : this.AF;
   }
}
