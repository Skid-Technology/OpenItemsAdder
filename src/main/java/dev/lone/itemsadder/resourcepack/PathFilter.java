package dev.lone.itemsadder.resourcepack;

import java.util.regex.Pattern;

class PathFilter {
   private final Pattern namespace;
   private final Pattern path;

   private PathFilter(Pattern var1, Pattern var2) {
      this.namespace = var1;
      this.path = var2;
   }

   boolean matches(String var1) {
      int var2 = var1.indexOf(58);
      return this.namespace.matcher(var1.substring(0, var2)).matches() && this.path.matcher(var1.substring(var2 + 1)).matches();
   }
}
