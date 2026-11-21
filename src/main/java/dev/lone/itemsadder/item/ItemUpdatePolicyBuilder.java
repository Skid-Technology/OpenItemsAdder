package dev.lone.itemsadder.item;

import lombok.Generated;

@Generated
public class ItemUpdatePolicyBuilder {
   @Generated
   private boolean aqG;
   @Generated
   private UpdateMode aqH;

   @Generated
   ItemUpdatePolicyBuilder() {
   }

   @Generated
   public ItemUpdatePolicyBuilder enabled(boolean var1) {
      this.aqG = var1;
      return this;
   }

   @Generated
   public ItemUpdatePolicyBuilder mode(UpdateMode var1) {
      this.aqH = var1;
      return this;
   }

   @Generated
   public ItemUpdatePolicy build() {
      return new ItemUpdatePolicy(this.aqG, this.aqH);
   }

   @Generated
   @Override
   public String toString() {
      return "ItemUpdateRules.ItemUpdateRulesBuilder(fixUnstackableItemsOnClick=" + this.aqG + ", enchantmentMode=" + this.aqH + ")";
   }
}
