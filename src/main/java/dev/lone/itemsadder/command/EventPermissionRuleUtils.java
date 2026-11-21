package dev.lone.itemsadder.command;

import dev.lone.itemsadder.nms.CommandCaptureResult;
import dev.lone.itemsadder.nms.NmsCommandSenderProvider;
import me.clip.placeholderapi.util.Msg;

class EventPermissionRuleUtils {
   public static boolean method5373(String var0, EventPermissionRule var1) {
      CommandCaptureResult var2 = NmsCommandSenderProvider.Ok().method3171(var0, var1.iV);
      if (var2.aUP != null && var1.iW != null && var2.aUP.contains(var1.iW)) {
         return true;
      }

      if (var2.aUQ == null) {
         return false;
      }

      if (!var1.iV) {
         Msg.info("Executed item command: " + var0 + " | Response: " + var2, new Object[0]);
      }

      return var2.aUQ;
   }
}
