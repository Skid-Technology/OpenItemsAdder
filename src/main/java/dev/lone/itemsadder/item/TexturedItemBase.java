package dev.lone.itemsadder.item;

import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.core.NamespacedId;
import dev.lone.itemsadder.resourcepack.ModelJsonAsset;
import dev.lone.itemsadder.util.IntTableConstantsQ;
import java.util.Collections;
import java.util.List;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public class TexturedItemBase extends NamespacedId {
   @Nullable
   public final CustomItemDefinition ape;
   public final Material apf;

   public TexturedItemBase(Material var1, String var2, String var3, @Nullable CustomItemDefinition var4) {
      super(var2, var3);
      this.apf = var1;
      this.ape = var4;
   }

   public String getModelPath() {
      return this.buildTexturePath(false, false);
   }

   public String buildTexturePath(boolean var1, boolean var2) {
      if (this.nB()) {
         return (var2 ? this.getNamespace() + ":" : "") + "item/ia_auto/" + this.getId() + (var1 ? ".json" : "");
      } else {
         return this.ape != null
            ? this.ape.method8734(var1, var2)
            : (var2 ? this.getNamespace() + ":" : "") + "item/ia_auto/" + this.getId() + (var1 ? ".json" : "");
      }
   }

   public List getTextures() {
      return this.ape != null ? this.ape.getTextures() : Collections.emptyList();
   }

   public boolean nB() {
      if (this.ape != null) {
         return this.ape.nB();
      } else {
         throw new RuntimeException("Cannot generate model for " + this + ", no texture or custom item provided.");
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Nullable
   public String DO() {
      if (this.ape != null && this.ape.DO() != null) {
         return this.ape.DO();
      }

      if ((this.apf.isBlock() || this.ape instanceof CustomBlockDefinition) && this.ape instanceof CustomBlockDefinition) {
         switch (IntTableConstantsQ.SWITCH_MAP[((CustomBlockDefinition)this.ape).aps.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
               return "block/cube_all";
            case 5:
               return "block/base/block_tile";
         }
      }

      ModelJsonAsset var1 = ModelJsonAsset.forMaterial(this.apf);
      return !var1.hasError() && var1.getParent() != null ? var1.getParent() : "item/generated";
   }
}
