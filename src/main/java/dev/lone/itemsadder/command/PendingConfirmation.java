package dev.lone.itemsadder.command;

import dev.lone.itemsadder.util.Log;
import java.util.WeakHashMap;
import org.bukkit.command.CommandSender;

public class PendingConfirmation {
   CommandSender aHE;
   String aHF;
   long aCp;

   public PendingConfirmation(CommandSender var1, String var2, String[] var3) {
      this.aHE = var1;
      this.aHF = var2 + " " + String.join(" ", var3);
      this.aCp = System.currentTimeMillis();
   }

   public static void method2391(WeakHashMap var0, CommandSender var1, String var2, String[] var3, String var4) {
      var0.put(var1, new PendingConfirmation(var1, var2, var3));
      Log.sendLocalizedWith(var1, "confirm-command", var1x -> var1x.replace("{command}", var2));
      if (var4 != null) {
         Log.sendPrefixed(var1, var4);
      }
   }

   public boolean method2392(String var1, String[] var2) {
      return System.currentTimeMillis() - this.aCp < 5000L;
   }

   public boolean method2393(String var1, String[] var2) {
      return (var1 + " " + String.join(" ", var2)).equals(this.aHF);
   }
}
