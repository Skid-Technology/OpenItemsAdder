package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.util.Utils;

public class CustomEntityModelBridge extends CustomEntityModel {
   public CustomEntityModelBridge(String var1, String var2) {
      super(var1, var2);
      this.AM = true;
   }

   @Override
   public String bg(String var1) {
      String var2 = Utils.normalizeSlashes(var1);
      var2 = Utils.removeSuffix(var2, "/.player_advanced_animations");
      return Utils.lastSegment(var2);
   }
}
