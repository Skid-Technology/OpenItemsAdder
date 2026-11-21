package dev.lone.itemsadder.command;

public class PermissionRuleOutcome {
   private final PermissionRuleStatus iO;

   public PermissionRuleOutcome(PermissionRuleStatus var1) {
      this.iO = var1;
   }

   public static PermissionRuleOutcome cy() {
      return new PermissionRuleOutcome(PermissionRuleStatus.FAILED);
   }

   public static PermissionRuleOutcome cz() {
      return new PermissionRuleOutcome(PermissionRuleStatus.SUCCESS);
   }

   public PermissionRuleStatus cA() {
      return this.iO;
   }
}
