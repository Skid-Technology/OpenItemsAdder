package dev.lone.itemsadder.resourcepack;

import java.util.List;
import org.apache.commons.io.FilenameUtils;

class PackValidationError {
   private final String declaration;
   private final String root;
   private final String model;
   private final List chain;
   private final String reason;

   PackValidationError(String var1, String var2, String var3, List var4, String var5) {
      var4 = List.copyOf(var4);
      this.declaration = var1;
      this.root = var2;
      this.model = var3;
      this.chain = var4;
      this.reason = var5;
   }

   String message() {
      return "Modern atlas validation failed for material '"
         + FilenameUtils.getBaseName(this.declaration)
         + "', item declaration '"
         + this.declaration
         + "', root '"
         + this.root
         + "', model '"
         + this.model
         + "', parent chain '"
         + (this.chain.isEmpty() ? "<none>" : String.join(" -> ", this.chain))
         + "': "
         + this.reason;
   }
}
