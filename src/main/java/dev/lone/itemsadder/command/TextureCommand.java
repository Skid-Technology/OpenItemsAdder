package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.WeakHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class TextureCommand extends IaCommandBase implements Listener {
   public static WeakHashMap aHB = new WeakHashMap();

   public TextureCommand() {
      Main.getCommand("iatexture").setExecutor(this);
      EventRegistrar.registerIfAbsent(this, Main.getInstance());
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      }

      if (args.length > 0) {
         if (PermissionNodes.method1632(commandSender, "ia.user.iatexture.all")) {
            if (args[0].equals("all")) {
               Log.log(ChatColor.AQUA + "[Pack] Applying every player... ");
               int var5 = ContentManager.method8084().aL.delivery.broadcastToOnlinePlayers();
               Log.log(ChatColor.GREEN + "[Pack] Applied to " + ChatColor.YELLOW + var5 + ChatColor.GREEN + " players");
            } else {
               Player var8 = Bukkit.getPlayer(args[0]);
               if (var8 == null) {
                  Log.sendPrefixed(commandSender, Main.aFi.lj("player-offline"));
                  return true;
               }

               this.bP(var8);
            }
         }
      } else {
         long var9 = Settings.get().packCommandCooldownSeconds * 1000L;
         if (var9 > 0L && aHB.containsKey(commandSender) && System.currentTimeMillis() - (Long)aHB.get(commandSender) < var9) {
            String var7 = new SimpleDateFormat("mm:ss").format(new Date(var9 - (System.currentTimeMillis() - (Long)aHB.get(commandSender))));
            Log.sendPrefixed(commandSender, Main.aFi.lj("please-wait-command-cooldown").replace("{interval}", var7));
            return true;
         }

         if (commandSender instanceof Player) {
            this.bP((Player)commandSender);
            aHB.put(commandSender, System.currentTimeMillis());
         } else {
            commandSender.sendMessage(Main.aFi.lj("wrong-command-usage") + ChatColor.YELLOW + " /iatexture [all|player]. Example: /iatexture all");
            Log.warnLocalized("command-ingame-only");
         }
      }

      return true;
   }

   private void bP(Player var1) {
      ContentManager.method8084().aL.delivery.sendPackTo(var1);
   }

   @EventHandler
   private void method516(PlayerQuitEvent var1) {
      aHB.remove(var1.getPlayer());
   }
}
