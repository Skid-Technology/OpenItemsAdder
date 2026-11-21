package dev.lone.itemsadder.item;

import lombok.Generated;

public class ItemUpdatePolicy {
   final boolean aqG;
   final UpdateMode aqH;

   @Generated
   ItemUpdatePolicy(boolean var1, UpdateMode var2) {
      this.aqG = var1;
      this.aqH = var2;
   }

   @Generated
   public static ItemUpdatePolicyBuilder builder() {
      return new ItemUpdatePolicyBuilder();
   }

   @Generated
   public boolean isEnabled() {
      return this.aqG;
   }

   @Generated
   public UpdateMode getMode() {
      return this.aqH;
   }
}
