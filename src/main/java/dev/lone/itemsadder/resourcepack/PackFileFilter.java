package dev.lone.itemsadder.resourcepack;

import java.util.Set;

class PackFileFilter {
   private final Set bAG;
   private final Set bAH;

   private PackFileFilter(Set var1, Set var2) {
      this.bAG = var1;
      this.bAH = var2;
   }

   boolean isInModelFolder(String var1) {
      int var2 = var1.lastIndexOf(47);
      return var2 > 0 && this.bAH.contains(var1.substring(0, var2));
   }

   public Set supportModels() {
      return this.bAG;
   }

   public Set modelFolders() {
      return this.bAH;
   }
}
