package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.resourcepack.CdnPackDelivery;
import dev.lone.itemsadder.resourcepack.HostedPackDelivery;
import dev.lone.itemsadder.resourcepack.ProgressPackDelivery;
import dev.lone.itemsadder.resourcepack.ResourcePackLoadState;
import dev.lone.itemsadder.resourcepack.SelfHostedPackDelivery;
import dev.lone.itemsadder.util.BossBarManager;
import dev.lone.itemsadder.util.JsonFileMerger;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.NullableBox;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.io.File;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ZipCommand extends IaCommandBase {
   public static volatile boolean aHx;
   private static final AtomicReference aHC = new AtomicReference();

   public ZipCommand() {
      Main.getCommand("iazip").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      }

      method1506(commandSender, args);
      return true;
   }

   public static void method1505(CommandSender var0) {
      method1506(var0, new String[0]);
   }

   public static void method1506(CommandSender var0, @NotNull String[] var1) {
      if (!ContentManager.method8084().field91.isReady()) {
         c(var0, ChatColor.RED + "Vanilla assets are not downloaded yet. Please wait.");
      } else {
         ContentManager var2 = ContentManager.method8084();
         if (!aHx && !ReloadCommand.aHx && !var2.method8140()) {
            NullableBox var3 = NullableBox.of(false);
            NullableBox var4 = NullableBox.of(TargetScope.ALL);
            NullableBox var5 = NullableBox.of(false);
            NullableBox var6 = NullableBox.of(false);
            NullableBox var7 = NullableBox.of(false);
            NullableBox var8 = NullableBox.of(-1);

            for (int var9 = 0; var9 < var1.length; var9++) {
               String var10 = var1[var9];
               if (var10.equalsIgnoreCase("--uncompressed")) {
                  var3.set(true);
               } else if (var10.equalsIgnoreCase("--apply-to")) {
                  if (var9 + 1 >= var1.length) {
                     c(var0, ChatColor.RED + "No argument provided for --apply-to.");
                     return;
                  }

                  String var11 = var1[var9 + 1];
                  if (var11.equalsIgnoreCase("all")) {
                     var4.set(TargetScope.ALL);
                  } else if (var11.equalsIgnoreCase("none")) {
                     var4.set(TargetScope.NONE);
                  } else {
                     if (!var11.equalsIgnoreCase("self")) {
                        c(var0, ChatColor.RED + "Invalid argument for --apply-to. Use 'all', 'none', or 'self'.");
                        return;
                     }

                     var4.set(TargetScope.SELF);
                  }
               } else if (var10.equalsIgnoreCase("--skip-recipes")) {
                  var5.set(true);
                  Log.warnToSender(var0, "Skipping recipes reloading.");
               } else if (var10.equalsIgnoreCase("--open-ia-menu")) {
                  var6.set(true);
               } else if (var10.equalsIgnoreCase("--check-duplicate-images")) {
                  var7.set(true);
               }
            }

            ResourcePackLoadState var13 = var2.method8137();
            if (var13 == null) {
               c(var0, ChatColor.YELLOW + "A resource-pack operation is already running. Please wait before using " + ChatColor.AQUA + "/iazip");
            } else {
               aHC.set(var13);
               aHx = true;
               ReloadCommand.method1535(var13);
               var13.getFuture().thenRun(() -> {
                  ReloadCommand.method1507(var13);
                  method1507(var13);
               });

               try {
                  ReloadCommand.method1534((Boolean)var5.value, var13)
                     .whenComplete(
                        (var8x, var9x) -> {
                           if (var9x != null) {
                              method1508(var13, var9x);
                           } else {
                              ReloadCommand.method1507(var13);
                              if (!var13.isActive()) {
                                 method1507(var13);
                              } else {
                                 try {
                                    var2.method8135(
                                       (Boolean)var7.value || Settings.get().logCompressionDuplicateTextures,
                                       () -> BossBarManager.setTitleAndProgress("Compressing...", 0.0),
                                       var6xx -> {
                                          method1507(var13);
                                          BossBarManager.clear();
                                          if (!var6xx) {
                                             c(var0, ChatColor.RED + "Resource-pack generation failed. Check the console for details.");
                                          } else {
                                             var2.aL.delivery.hostPackAndNotify(() -> method1509(var0, (Boolean)var3.value, (TargetScope)var4.value), var0);
                                             TextureCommand.aHB.clear();
                                             SchedulerUtils.runLaterSync(() -> {
                                                if ((Boolean)var6.value) {
                                                   if (var0 instanceof Player var2xxx) {
                                                      var2xxx.performCommand("ia");
                                                   } else {
                                                      for (Player var4xxx : Bukkit.getServer().getOnlinePlayers()) {
                                                         var4xxx.performCommand("ia");
                                                      }
                                                   }
                                                }
                                             }, 20L);
                                          }
                                       },
                                       var1xx -> {
                                          int var3xx = (int)(var1xx * 100.0);
                                          if (var3xx != (Integer)var8.value) {
                                             var8.set(var3xx);
                                             BossBarManager.setTitleAndProgress("Writing pack archive... " + var3xx + "%", var1xx);
                                          }
                                       },
                                       var0 instanceof Player ? (Player)var0 : null,
                                       var13
                                    );
                                 } catch (Throwable var11x) {
                                    method1508(var13, var11x);
                                 }
                              }
                           }
                        }
                     );
               } catch (Throwable var12) {
                  method1508(var13, var12);
               }
            }
         } else {
            c(var0, ChatColor.YELLOW + "Already reloading... Please wait before using " + ChatColor.AQUA + "/iazip");
         }
      }
   }

   private static void method1507(ResourcePackLoadState var0) {
      if (aHC.compareAndSet(var0, null)) {
         aHx = false;
      }
   }

   private static void method1508(ResourcePackLoadState var0, Throwable var1) {
      ReloadCommand.method1507(var0);
      method1507(var0);
      var0.complete();
      BossBarManager.clear();
      Log.error("Failed to zip resourcepack, uncaught error. Something is probably misconfigured.", var1);
   }

   public static void method1509(CommandSender var0, Boolean var1, TargetScope var2) {
      ContentManager var3 = ContentManager.method8084();

      try {
         if (var3.aL.delivery.isUnconfigured()) {
            return;
         }

         if (!(var3.aL.delivery instanceof HostedPackDelivery) || !((HostedPackDelivery)var3.aL.delivery).bJb) {
            if (var3.aL.delivery instanceof CdnPackDelivery
               || var3.aL.delivery instanceof ProgressPackDelivery
               || var3.aL.delivery instanceof HostedPackDelivery) {
               switch (var2) {
                  case ALL:
                     int var4 = var3.aL.delivery.broadcastToOnlinePlayers();
                     Log.logToSender(var0, ChatColor.GREEN + "[Pack] Sent to " + ChatColor.YELLOW + var4 + ChatColor.GREEN + " players");
                  case NONE:
                  default:
                     break;
                  case SELF:
                     if (var0 instanceof Player) {
                        var3.aL.delivery.sendPackTo((Player)var0);
                        Log.logToSender(var0, ChatColor.GREEN + "[Pack] Sent to " + ChatColor.YELLOW + var0.getName());
                     }
               }
            } else if (var3.aL.delivery instanceof SelfHostedPackDelivery) {
               if (var0 != null) {
                  Log.sendLocalized(var0, "external-pack-host-guide");
               }

               Bukkit.getOnlinePlayers()
                  .stream()
                  .filter(var1x -> var1x.isOp() && var1x != var0)
                  .forEach(var0x -> Log.sendLocalized(var0x, "external-pack-host-guide"));
            }

            if (!var1) {
               return;
            }

            Log.logToSender(var0, ChatColor.AQUA + "[Pack] Uncompressing...");
            BossBarManager.setTitle("Uncompressing...");
            File var9 = var3.aL.generator.getPackFile();
            if (var9 != null && var9.exists()) {
               File var5 = new File(Main.getInstance().getDataFolder(), "output_uncompressed");
               JsonFileMerger.mergeZipAndRemap(var9, var5, true);
               Log.logToSender(var0, ChatColor.GREEN + "[Pack] Uncompressed successfully.");
            }

            return;
         }
      } finally {
         BossBarManager.clear();
      }
   }
}
