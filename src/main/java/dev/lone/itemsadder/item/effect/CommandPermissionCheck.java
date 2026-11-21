package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.nms.CommandCaptureResult;
import dev.lone.itemsadder.nms.NmsCommandSenderProvider;
import me.clip.placeholderapi.util.Msg;

class CommandPermissionCheck {
   public static boolean hasPermission(String var0, CommandEffect var1) {
      CommandCaptureResult var2 = NmsCommandSenderProvider.Ok().method3171(var0, var1.aji);
      if (var2.aUP != null && var1.ajj != null && var2.aUP.contains(var1.ajj)) {
         return true;
      }

      if (var2.aUQ == null) {
         return false;
      }

      if (!var1.aji) {
         Msg.info("Executed item command: " + var0 + " | Response: " + var2, new Object[0]);
      }

      return var2.aUQ;
   }
}
