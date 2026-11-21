package dev.lone.itemsadder.resourcepack;

import java.util.LinkedHashMap;
import java.util.List;

final class PackIssueLog {
   private final LinkedHashMap bBK = new LinkedHashMap();

   void addIssue(PackIssueCategory var1, PackResolutionKind var2, String var3) {
      this.recordResult(new PackResolutionResult(var1, var2, var3));
   }

   void addValidationIssue(PackIssueCategory var1, PackResolutionKind var2, PackValidationError var3) {
      this.recordResult(new PackResolutionResult(var1, var2, var3.message(), var3));
   }

   private void recordResult(PackResolutionResult var1) {
      this.bBK.putIfAbsent(var1.kind() + "\u0000" + var1.provenance() + "\u0000" + var1.message(), var1);
   }

   void mergeFrom(PackIssueLog var1) {
      var1.bBK.values().forEach(this::recordResult);
   }

   List getResults() {
      return List.copyOf(this.bBK.values());
   }

   boolean hasFatal() {
      return this.bBK.values().stream().anyMatch(PackResolutionResult::isFatal);
   }
}
