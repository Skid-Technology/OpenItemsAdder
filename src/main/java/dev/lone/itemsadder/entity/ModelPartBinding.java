package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.util.IntPair;
import org.bukkit.Location;

public class ModelPartBinding {
   public final ModelDefinitionData Hf;
   public final ModelBinding Hg;
   public final ModelPartDisplay Hh;
   public final ModelPartTransform Hi;

   public ModelPartBinding(ModelBinding var1, ModelPartDisplay var2) {
      this.Hf = var1.AB;
      this.Hg = var1;
      this.Hh = var2;
      this.Hi = var1.getPart(0);
   }

   public ModelPartTransform resolveTransform(ModelInstance var1) {
      int var2 = var1.Fa;
      if ("hold".equals(this.Hf.yL) && !var1.EY && var1.qX()) {
         var2 = (int)this.Hf.yO;
      }

      for (int var4 = Math.min(var2, this.Hg.getPartCount() - 1); var4 >= 0; var4--) {
         ModelPartTransform var3 = this.Hg.getPart(var4);
         if (var3 != null) {
            return var3;
         }
      }

      return null;
   }

   public void apply(ModelInstance var1) {
      IntPair var2 = this.Hf.getLoopFrames();
      if (var2 != null && !var1.Fk) {
         if (var1.Fa >= var2.zV) {
            var1.Fa = var2.zU;
         }

         var1.Fb = var1.Fa >= var2.zU;
      } else {
         if (this.Hf.hasLoopRange()) {
            if (var1.Fk) {
               if (var1.Fb) {
                  var1.Fb = false;
               } else if (var1.Fa >= 0 && var1.Fa < this.Hf.yR.zU) {
                  var1.method8911(true, true);
               }
            } else if (var1.Fa > this.Hf.yR.zV) {
               var1.Fb = true;
               var1.Fa = this.Hf.yR.zU;
            } else if (var1.Fa > this.Hf.yR.zU && !var1.Fb) {
               var1.Fb = true;
               var1.Fa = this.Hf.yR.zU;
            }
         } else if (var1.Fk) {
            var1.method8911(true, true);
         }
      }
   }

   public void applyAt(Location var1, ModelInstance var2) {
      ModelPartTransform var3 = this.resolveTransform(var2);
      if (var3 != null) {
         this.applyTransformAt(var1, var3);
      }
   }

   public void applyTransformAt(Location var1, ModelPartTransform var2) {
      this.Hh.applyTransform(var1, var2);
   }
}
