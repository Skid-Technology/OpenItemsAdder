package dev.lone.itemsadder.command;

import beer.devs.nativepackets.api.SchedulerAccess;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.particle.ParticleEmitter;
import dev.lone.itemsadder.util.Log;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.jetbrains.annotations.NotNull;

public final class ParticleCommand extends IaCommandBase implements TabCompleter, Listener {
   private static final String aHv = "ia.admin.iaparticle";
   private final Map aHw = new ConcurrentHashMap();

   public ParticleCommand() {
      Main.getCommand("iaparticle").setExecutor(this);
      Main.getCommand("iaparticle").setTabCompleter(this);
      Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
   }

   @Override
   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (!sender.hasPermission("ia.admin.iaparticle")) {
         Log.sendPrefixed(sender, Main.aFi.lj("no-permission-detail").replace("{permission}", "ia.admin.iaparticle"));
         return true;
      }

      if (args.length != 0 && (args.length != 1 || !args[0].equalsIgnoreCase("help"))) {
         if (!this.method1423(sender, command)) {
            return true;
         }

         this.aHw.values().removeIf(ParticleEmitter::Jd);
         String var5 = args[0].toLowerCase(Locale.ROOT);
         if (var5.equals("list") && args.length <= 2) {
            List var12 = Ls();
            if (var12.isEmpty()) {
               Log.sendPrefixed(sender, "No particle effects loaded. Put a .iaparticle bundle in ItemsAdder/contents/<folder>/, then run /iazip.");
               return true;
            }

            int var13 = (var12.size() + 19) / 20;

            int var14;
            try {
               var14 = args.length == 2 ? Integer.parseInt(args[1]) : 1;
            } catch (NumberFormatException var11) {
               var14 = 0;
            }

            if (var14 >= 1 && var14 <= var13) {
               Log.sendPrefixed(
                  sender,
                  "Particle effects (" + var14 + "/" + var13 + "): " + String.join(", ", var12.subList((var14 - 1) * 20, Math.min(var14 * 20, var12.size())))
               );
               return true;
            } else {
               Log.sendPrefixed(sender, "Choose a page from 1 to " + var13 + ": /iaparticle list [page]");
               return true;
            }
         } else {
            boolean var6 = var5.equals("play");
            if (var6 && args.length >= 2 && args.length <= 3 || var5.equals("stop") && args.length <= 2) {
               int var7 = var6 ? 2 : 1;
               Player var8 = args.length > var7 ? Bukkit.getPlayerExact(args[var7]) : (sender instanceof Player var9 ? var9 : null);
               if (var8 == null) {
                  Log.sendPrefixed(sender, "Choose an online player. Console usage: /iaparticle " + (var6 ? "play <namespace:id> <player>" : "stop <player>"));
                  return true;
               }

               if (var6 && !ContentManager.method8084().field85.getAssets().bundles().containsKey(args[1])) {
                  Log.sendPrefixed(
                     sender, "Unknown particle effect: " + args[1] + ". Use /iaparticle list or Tab; the ID is stored inside the bundle, not its filename."
                  );
                  return true;
               }

               if (!NativePacketsHolder.aN()) {
                  Log.sendPrefixed(sender, "Particle playback requires a supported NativePackets adapter.");
                  return true;
               }

               Runnable var15 = () -> {
                  if (this.method1423(sender, command)) {
                     UUID var6x = var8.getUniqueId();
                     if (var6) {
                        try {
                           ParticleEmitter var10x = ContentManager.method8084().field86.method2151(args[1], var8.getLocation(), List.of(var6x), 13);
                           ParticleEmitter var8x = this.aHw.put(var6x, var10x);
                           if (var8x != null) {
                              var8x.close();
                           }

                           Log.sendPrefixed(
                              sender,
                              var10x.Jd()
                                 ? "Particle effect skipped for " + var8.getName() + ": no compatible active viewer or server backend."
                                 : "Playing " + args[1] + " for " + var8.getName() + ". Stop: /iaparticle stop " + var8.getName()
                           );
                        } catch (IllegalArgumentException | IllegalStateException var9x) {
                           Log.sendPrefixed(sender, "Could not start particle effect: " + var9x.getMessage());
                        }
                     } else {
                        ParticleEmitter var7x = (ParticleEmitter)this.aHw.remove(var6x);
                        if (var7x != null && !var7x.Jd()) {
                           var7x.close();
                           Log.sendPrefixed(sender, "Stopped command particle effect for " + var8.getName() + ".");
                        } else {
                           Log.sendPrefixed(sender, "No command particle effect is playing for " + var8.getName() + ".");
                        }
                     }
                  }
               };
               SchedulerAccess var10 = NativePacketsHolder.PE().IP();
               if (var10.ownsEntity(var8)) {
                  var15.run();
               } else {
                  var10.entity(
                     Main.getInstance(),
                     var8,
                     var15,
                     () -> Log.sendPrefixed(sender, "Player left before the particle command ran. Try again with an online player.")
                  );
               }

               return true;
            } else {
               Log.sendPrefixed(sender, "Usage: /iaparticle list [page] | play <namespace:id> [player] | stop [player]");
               return true;
            }
         }
      } else {
         Log.sendPrefixed(sender, "/iaparticle list [page] | play <namespace:id> [player] | stop [player]");
         Log.sendPrefixed(
            sender,
            "Play starts at the player's current position, visible only to that player. A new play replaces their previous command effect. Accept the resource pack first (/iatexture)."
         );
         return true;
      }
   }

   private static List Ls() {
      return ContentManager.method8084().field85.getAssets().bundles().keySet().stream().sorted().toList();
   }

   public List onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
      if (sender.hasPermission("ia.admin.iaparticle") && ContentManager.method8084().method8090()) {
         List var5;
         if (args.length == 1) {
            var5 = List.of("help", "list", "play", "stop");
         } else if (args.length == 2 && args[0].equalsIgnoreCase("play")) {
            var5 = Ls();
         } else {
            if ((args.length != 2 || !args[0].equalsIgnoreCase("stop")) && (args.length != 3 || !args[0].equalsIgnoreCase("play"))) {
               return List.of();
            }

            var5 = Bukkit.getOnlinePlayers().stream().map(Player::getName).sorted().toList();
         }

         String var6 = args[args.length - 1].toLowerCase(Locale.ROOT);
         return var5.stream().filter(var1x -> var1x.toLowerCase(Locale.ROOT).startsWith(var6)).toList();
      } else {
         return List.of();
      }
   }

   @EventHandler
   public void method587(PlayerQuitEvent var1) {
      ParticleEmitter var2 = (ParticleEmitter)this.aHw.remove(var1.getPlayer().getUniqueId());
      if (var2 != null) {
         var2.close();
      }
   }

   @EventHandler
   public void method1528(PluginDisableEvent var1) {
      if (var1.getPlugin() == Main.getInstance()) {
         this.aHw.values().forEach(ParticleEmitter::close);
         this.aHw.clear();
      }
   }
}
