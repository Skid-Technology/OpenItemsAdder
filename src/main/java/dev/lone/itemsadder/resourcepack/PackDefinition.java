package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.entity.EquipmentItemRecord;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public class PackDefinition {
   private final String bux;
   private final Map buy;
   private final Map buz;
   private final Map buA;
   private final Map buB;

   public PackDefinition(String var1, Map var2, Map var3, Map var4, Map var5) {
      this.bux = var1;
      this.buy = var2;
      this.buz = var3;
      this.buA = var4;
      this.buB = var5;
   }

   @Nullable
   public PackTemplateGroup resolveTemplate(EquipmentItemRecord var1, String var2) {
      if (var1.pt() != null && var1.pu() != null) {
         String var3 = (String)this.buz.get(var1.pt());
         PackOverrideGroup var4 = (PackOverrideGroup)this.buA.get(var1.pu());
         PackPlan var5 = (PackPlan)this.buy.get(var1.material());
         if (var3 != null && var4 != null && var5 != null) {
            String var6 = var4.overrides().getOrDefault(var1.material(), var4.palette());

            for (PackTextureRef var8 : var5.overrides()) {
               if ((var8.material() == null || var8.material().equals(var1.pu())) && (var8.pattern() == null || var8.pattern().equals(var1.pt()))) {
                  if (var8.texture() != null) {
                     var3 = var8.texture();
                  }

                  var6 = var8.palette();
                  break;
               }
            }

            List var9 = var6 == null ? null : (List)this.buB.get(var6);
            return var6 != null && var9 == null ? null : new PackTemplateGroup(PackImageUtils.withSubfolder(var3, "trims/entity/" + var2 + "/"), var9);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public String version() {
      return this.bux;
   }

   public Map equipment() {
      return this.buy;
   }

   public Map patterns() {
      return this.buz;
   }

   public Map materials() {
      return this.buA;
   }

   public Map palettes() {
      return this.buB;
   }
}
