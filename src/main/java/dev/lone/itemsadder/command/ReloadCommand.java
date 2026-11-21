package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.BaseItemsAdderEvent;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.NamespaceRegistrar;
import dev.lone.itemsadder.entity.AbstractFakeEntity;
import dev.lone.itemsadder.resourcepack.ResourcePackLoadState;
import dev.lone.itemsadder.util.BossBarManager;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.NullableBox;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReloadCommand extends IaCommandBase {
   public static volatile boolean aHx;
   private static final AtomicReference aHy = new AtomicReference();

   public ReloadCommand() {
      Main.getCommand("iareload").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!ContentManager.method8084().method8113() && !this.method1423(sender, command)) {
         return true;
      }

      AbstractFakeEntity.aPC.asMap().clear();
      if (!ContentManager.method8084().field91.isReady()) {
         c(sender, ChatColor.RED + "Vanilla assets are not downloaded yet. Please wait.");
         return true;
      }

      if (!aHx && !ContentManager.method8084().method8140()) {
         if (!PermissionNodes.method1632(sender, "ia.admin.iareload")) {
            return true;
         }

         if (ContentManager.method8084().ad() != null && ContentManager.method8084().ad().isSaving()) {
            c(sender, ChatColor.YELLOW + "Saving regions data... Please wait before using " + ChatColor.AQUA + "/" + command.getName());
            return true;
         }

         Log.log(ChatColor.YELLOW + "Reloading...");
         boolean var5 = args.length > 0 && args[0].equalsIgnoreCase("--scripts-only");
         if (var5) {
            ResourcePackLoadState var13 = ContentManager.method8084().method8137();
            if (var13 == null) {
               c(
                  sender,
                  ChatColor.YELLOW + "A resource-pack operation is already running. Please wait before using " + ChatColor.AQUA + "/" + command.getName()
               );
               return true;
            }

            method1535(var13);
            var13.getFuture().thenRun(() -> method1507(var13));

            try {
               c(sender, ChatColor.YELLOW + "Reloading scripts...");
               NamespaceRegistrar.bf(true);
               c(sender, ChatColor.GREEN + "Reload completed.");
               return true;
            } finally {
               method1507(var13);
               var13.complete();
               BossBarManager.clear();
            }
         } else {
            NullableBox var6 = NullableBox.of(false);

            for (int var7 = 0; var7 < args.length; var7++) {
               String var8 = args[var7];
               if (var8.equalsIgnoreCase("--skip-recipes")) {
                  var6.set(true);
                  Log.warnToSender(sender, "Skipping recipes reloading.");
               }
            }

            try {
               boolean var14 = method1533((Boolean)var6.value, () -> {
                  ContentManager.method8084().aL.delivery.hostPackAndNotify(null, sender);
                  TextureCommand.aHB.clear();
                  c(sender, ChatColor.GREEN + "Reload completed.");
                  BossBarManager.clear();
               }, true);
               if (!var14) {
                  c(
                     sender,
                     ChatColor.YELLOW + "A resource-pack operation is already running. Please wait before using " + ChatColor.AQUA + "/" + command.getName()
                  );
               }
            } catch (Exception var12) {
               BossBarManager.clear();
               var12.printStackTrace();
               Log.error("Failed to reload the plugin. Please check before this message for more information.");
            }

            return true;
         }
      } else {
         c(sender, ChatColor.YELLOW + "Already reloading... Please wait before using " + ChatColor.AQUA + "/" + command.getName());
         return true;
      }
   }

   public static boolean method1532(boolean var0, @Nullable Runnable var1) {
      return method1533(var0, var1, false);
   }

   private static boolean method1533(boolean var0, @Nullable Runnable var1, boolean var2) {
      ContentManager var3 = ContentManager.method8084();
      ResourcePackLoadState var4 = var3.method8137();
      if (var4 == null) {
         return false;
      }

      method1535(var4);
      var4.getFuture().thenRun(() -> method1507(var4));

      try {
         if (var2) {
            EventRegistrar.callEvent(new BaseItemsAdderEvent());
         }

         method1534(var0, var4).whenComplete((var2x, var3x) -> {
            method1507(var4);
            if (var4.complete()) {
               if (var3x != null) {
                  BossBarManager.clear();
                  Log.error("Failed to reload the plugin. Please check before this message for more information.", var3x);
               } else {
                  try {
                     if (var1 != null) {
                        var1.run();
                     }
                  } catch (Throwable var5) {
                     Log.error("Reload completion callback failed.", var5);
                  }
               }
            }
         });
         return true;
      } catch (RuntimeException | Error var6) {
         method1507(var4);
         var4.complete();
         BossBarManager.clear();
         throw var6;
      }
   }

   static CompletableFuture method1534(boolean var0, ResourcePackLoadState var1) {
      ContentManager var2 = ContentManager.method8084();
      return var2.method8103(var0, true, var1, () -> {
         Main.getInstance().Le();
         var2.method8094(true);
      });
   }

   static void method1535(ResourcePackLoadState var0) {
      aHy.set(var0);
      aHx = true;
   }

   static void method1507(ResourcePackLoadState var0) {
      if (aHy.compareAndSet(var0, null)) {
         aHx = false;
      }
   }
}
