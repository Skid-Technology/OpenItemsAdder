package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.gui.GuiIconRegistry;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemMetaTracker;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.util.Log;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemCommandTabCompleter extends IaCommandBase implements TabCompleter {
   private static final Set aHs = Set.of("rename", "durability", "repair", "remove", "disable-auto-update");
   final WeakHashMap aHt = new WeakHashMap();

   public ItemCommandTabCompleter() {
      Main.getCommand("iaitem").setExecutor(this);
      Main.getCommand("iaitem").setTabCompleter(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
      if (!this.method1423(sender, command)) {
         return true;
      }

      GuiIconRegistry var5 = ContentManager.method8084().bB;
      if (!ContentManager.method8084().field82 || var5 == null) {
         return true;
      }

      if (args.length == 0) {
         sender.sendMessage(
            Main.aFi.lj("wrong-command-usage") + ChatColor.YELLOW + " /iaitem <rename|durability|repair|remove|disable-auto-update|update> [args]"
         );
         return true;
      }

      String var6 = args[0].toLowerCase(Locale.ROOT);
      if (aHs.contains(var6) && !PermissionNodes.method1632(sender, "ia.admin.iaitem." + var6)) {
         return true;
      }

      if (var6.equals("durability")) {
         if (args.length != 2) {
            return this.method1521(sender, args);
         }

         try {
            Integer.parseInt(args[1]);
         } catch (NumberFormatException var19) {
            return this.method1521(sender, args);
         }
      }

      if (List.of("rename", "durability", "repair", "remove").contains(var6)) {
         String var22 = "ia" + var6;
         PluginCommand var24 = Main.getCommand(var22);
         var24.getExecutor().onCommand(sender, var24, var22, Arrays.copyOfRange(args, 1, args.length));
         return true;
      }

      if (var6.equals("disable-auto-update")) {
         if (args.length != 1) {
            sender.sendMessage(Main.aFi.lj("wrong-command-usage") + ChatColor.YELLOW + " /iaitem disable-auto-update");
            return true;
         }

         if (sender instanceof Player var21) {
            ItemStack var23 = var21.getInventory().getItemInMainHand();
            if (ContentManager.method8084().method8149(var23) == null) {
               sender.sendMessage(ChatColor.RED + "Hold a custom item to disable its auto-update.");
               return true;
            } else {
               ItemMetaTracker.setOverrideAutoUpdate(var23, true);
               var21.getInventory().setItemInMainHand(var23);
               sender.sendMessage(ChatColor.GREEN + "Auto-update disabled for the held item.");
               return true;
            }
         } else {
            return method1525(sender);
         }
      } else if (!args[0].equalsIgnoreCase("reload-update-all")) {
         Player var20 = sender instanceof Player ? (Player)sender : null;
         EquipmentTarget var8 = EquipmentTarget.ALL;
         CustomItemDefinition var9 = null;
         Map var10 = method1524(args);
         if (var10.containsKey("player")) {
            var20 = Bukkit.getPlayer((String)var10.get("player"));
            if (var20 == null || !var20.isOnline()) {
               sender.sendMessage(Main.aFi.lj("player-offline"));
               return true;
            }
         }

         if (var20 == null) {
            sender.sendMessage(ChatColor.RED + "You must specify a player or use the command ingame.");
            return true;
         }

         if (var10.containsKey("slot")) {
            String var11 = ((String)var10.get("slot")).toUpperCase();
            if (!var11.equalsIgnoreCase("all")) {
               var8 = (EquipmentTarget)BukkitRegistryLookup.enumOrDefault(var11, EquipmentTarget.class, null);
               if (var8 == null) {
                  sender.sendMessage(ChatColor.RED + "Invalid slot: " + var11);
                  return true;
               }
            }
         }

         if (var10.containsKey("item")) {
            String var25 = (String)var10.get("item");
            var9 = ContentManager.method8084().method8154(var25);
            if (var9 == null) {
               sender.sendMessage(ChatColor.RED + "Unknown custom item: " + var25);
               return true;
            }
         }

         PendingConfirmation var26 = (PendingConfirmation)this.aHt.get(sender);
         if (args[0].equalsIgnoreCase("update")) {
            if (var26 == null || !var26.method2393(alias, args) || !var26.method2392(alias, args)) {
               PendingConfirmation.method2391(this.aHt, sender, alias, args, "This command has no automatic rollback. Use it with caution!");
               return true;
            }

            if (var8.isSingle()) {
               EquipmentSlot var12 = var8.Lq();
               if (var12 == null) {
                  throw new IllegalStateException("Invalid slot: " + var8);
               }

               ItemStack var13 = var20.getEquipment().getItem(var12);
               CustomItemDefinition var14 = ContentManager.method8084().method8149(var13);
               if (var14 == null) {
                  return true;
               }

               if (var9 != null && !var14.equals(var9)) {
                  return true;
               }

               ItemStack var15 = ItemMetaTracker.updateItemFromDefinition(var13);
               if (var15 != null) {
                  var20.getEquipment().setItem(var12, var15);
                  sender.sendMessage(ChatColor.GREEN + "Item in '" + var8 + "' of " + var20.getName() + " updated.");
               } else {
                  sender.sendMessage(ChatColor.RED + "No custom item found in '" + var8 + "' of " + var20.getName() + ".");
               }
            } else {
               this.method1523(sender, var20, var9);
            }

            this.aHt.remove(sender);
         } else if (args[0].equalsIgnoreCase("set-itemstack-auto-update")) {
            args[0] = args[0].toLowerCase();
            if (args.length < 2 || args.length > 3) {
               sender.sendMessage(
                  Main.aFi.lj("wrong-command-usage")
                     + ChatColor.YELLOW
                     + " /iaitem set-itemstack-auto-update <true|false> --player [player] --slot [slot|all] --item [item]"
               );
               return true;
            }

            String var27 = args[1].toLowerCase();
            if (var26 == null || !var26.method2393(alias, args) || !var26.method2392(alias, args)) {
               PendingConfirmation.method2391(this.aHt, sender, alias, args, "This command has no automatic rollback. Use it with caution!");
               return true;
            }

            boolean var28 = var27.equalsIgnoreCase("true");
            if (var8.isSingle()) {
               EquipmentSlot var30 = var8.Lq();
               if (var30 == null) {
                  throw new IllegalStateException("Invalid slot: " + var8);
               }

               ItemStack var32 = var20.getEquipment().getItem(var30);
               CustomItemDefinition var33 = ContentManager.method8084().method8149(var32);
               if (var33 == null) {
                  return true;
               }

               if (var9 != null && !var33.equals(var9)) {
                  return true;
               }

               ItemMetaTracker.setOverrideAutoUpdate(var32, var28);
               var20.getEquipment().setItem(var30, var32);
               sender.sendMessage(ChatColor.GREEN + "Item in '" + var8 + "' of " + var20.getName() + " auto-update flag set to " + var28 + ".");
            } else {
               int var29 = 0;
               ItemStack[] var31 = var20.getInventory().getContents();

               for (int var16 = 0; var16 < var31.length; var16++) {
                  ItemStack var17 = var31[var16];
                  if (var17 != null) {
                     CustomItemDefinition var18 = ContentManager.method8084().method8149(var17);
                     if (var18 != null && (var9 == null || var18.equals(var9))) {
                        ItemMetaTracker.setOverrideAutoUpdate(var17, var28);
                        var20.getInventory().setItem(var16, var17);
                        var29++;
                     }
                  }
               }

               sender.sendMessage(ChatColor.GREEN + "Set auto-update flag to " + var28 + " of " + var29 + " items in inventory of " + var20.getName() + ".");
            }

            this.aHt.remove(sender);
         } else {
            sender.sendMessage(
               Main.aFi.lj("wrong-command-usage") + ChatColor.YELLOW + " /iaitem <rename|durability|repair|remove|disable-auto-update|update> [args]"
            );
         }

         return true;
      } else if (sender instanceof Player var7) {
         ReloadCommand.method1532(false, () -> {
            Log.sendLocalized(var7, "plugin-reloaded-successfully");
            this.method1523(sender, var7, null);
         });
         return true;
      } else {
         method1525(sender);
         return true;
      }
   }

   private boolean method1521(CommandSender var1, String[] var2) {
      String var3 = Main.aFi.lj("wrong-command-usage")
         + ChatColor.YELLOW
         + " /iaitem durability <add|multiply|set> <hand|offhand|head|chest|leg|feet> <player> <amount>";
      if (var2.length != 5) {
         var1.sendMessage(var3);
         return true;
      }

      String var4 = var2[1].toLowerCase(Locale.ROOT);
      if (!List.of("add", "multiply", "set").contains(var4)) {
         var1.sendMessage(var3);
         return true;
      }

      String var5 = switch (var2[2].toLowerCase(Locale.ROOT)) {
         case "hand" -> "MAIN_HAND";
         case "offhand" -> "OFF_HAND";
         case "leg" -> "LEGS";
         default -> var2[2].toUpperCase(Locale.ROOT);
      };
      EquipmentTarget var13 = (EquipmentTarget)BukkitRegistryLookup.enumOrDefault(var5, EquipmentTarget.class, null);
      if (var13 != null && var13.isSingle()) {
         Player var14 = Bukkit.getPlayerExact(var2[3]);
         if (var14 == null) {
            Log.sendLocalized(var1, "player-offline");
            return true;
         }

         double var8;
         try {
            var8 = Double.parseDouble(var2[4]);
            if (!Double.isFinite(var8) || !var4.equals("multiply") && var8 != Math.rint(var8)) {
               var1.sendMessage(var3);
               return true;
            }
         } catch (NumberFormatException var12) {
            var1.sendMessage(var3);
            return true;
         }

         EquipmentSlot var10 = var13.Lq();
         ItemStack var11 = var14.getEquipment().getItem(var10);
         if (var11 != null && ItemStackUtils.aA(var11)) {
            ItemStackUtils.method7235(var11, method1522(var4, ItemStackUtils.aw(var11), ItemStackUtils.method7233(var11), var8));
            var14.getEquipment().setItem(var10, var11);
            Log.sendLocalized(var1, "set-durability-successfully");
            return true;
         } else {
            Log.sendLocalized(var1, "cannot-set-durability");
            return true;
         }
      } else {
         var1.sendMessage(var3);
         return true;
      }
   }

   static int method1522(String var0, int var1, int var2, double var3) {
      double var5 = switch (var0) {
         case "add" -> var1 + var3;
         case "multiply" -> var1 * var3;
         default -> var3;
      };
      return (int)Math.round(Math.max(0.0, Math.min(var2, var5)));
   }

   void method1523(@NotNull CommandSender var1, @NotNull Player var2, @Nullable CustomItemDefinition var3) {
      int var4 = 0;
      ItemStack[] var5 = var2.getInventory().getContents();

      for (int var6 = 0; var6 < var5.length; var6++) {
         ItemStack var7 = var5[var6];
         if (var7 != null) {
            CustomItemDefinition var8 = ContentManager.method8084().method8149(var7);
            if (var8 != null && (var3 == null || var8.equals(var3))) {
               ItemStack var9 = ItemMetaTracker.updateItemFromDefinition(var7);
               if (var9 != null) {
                  var2.getInventory().setItem(var6, var9);
                  var4++;
               }
            }
         }
      }

      var1.sendMessage(ChatColor.GREEN + "Updated " + var4 + " items in inventory of " + var2.getName() + ".");
   }

   public List onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
      if (args.length == 1) {
         return List.of("rename", "durability", "repair", "remove", "disable-auto-update", "update", "reload-update-all", "set-itemstack-auto-update")
            .stream()
            .filter(var1x -> !aHs.contains(var1x) || sender.hasPermission("ia.admin.iaitem." + var1x))
            .toList();
      }

      String var5 = args[0].toLowerCase(Locale.ROOT);
      if (aHs.contains(var5) && !sender.hasPermission("ia.admin.iaitem." + var5)) {
         return List.of();
      }

      if (args[0].equalsIgnoreCase("remove")) {
         if (args.length == 2) {
            return IaTabCompleter.method852(args[1], false, sender);
         } else {
            return args.length == 3 ? ContentManager.method8084().method8168(args[2]) : List.of();
         }
      } else if (var5.equals("durability")) {
         return switch (args.length) {
            case 2 -> List.of("add", "multiply", "set");
            case 3 -> List.of("hand", "offhand", "head", "chest", "leg", "feet");
            case 4 -> IaTabCompleter.method852(args[3], false, sender);
            default -> List.of();
         };
      } else {
         if (List.of("rename", "repair", "disable-auto-update").contains(var5)) {
            return List.of();
         }

         if (args[0].equalsIgnoreCase("reload-update-all")) {
            return List.of();
         }

         if (args[0].equalsIgnoreCase("set-itemstack-auto-update")) {
            return List.of("true", "false");
         }

         if (args.length >= 3 && args[args.length - 2].startsWith("--")) {
            String var6 = args[args.length - 2].substring(2);
            if (var6.equalsIgnoreCase("slot")) {
               return List.of("main_hand", "off_hand", "head", "chest", "legs", "feet", "all");
            }

            if (var6.equalsIgnoreCase("player")) {
               return IaTabCompleter.method852(args[args.length - 1], false, sender);
            }

            if (var6.equalsIgnoreCase("item")) {
               return ContentManager.method8084().method8168(args[args.length - 1]);
            }
         }

         Map var10 = method1524(args);
         ItemCommandOptions var7 = new ItemCommandOptions(this);

         for (String var9 : var10.keySet()) {
            var7.remove("--" + var9);
         }

         return var7;
      }
   }

   private static Map method1524(String[] var0) {
      HashMap var1 = new HashMap();

      for (int var2 = 1; var2 < var0.length; var2++) {
         if (var0[var2].startsWith("--")) {
            String var3 = var0[var2].substring(2);
            if (var2 + 1 < var0.length) {
               String var4 = var0[var2 + 1];
               var1.put(var3, var4);
            } else {
               var1.put(var3, "");
            }
         }
      }

      return var1;
   }

   public static boolean method1525(CommandSender var0) {
      Log.warnLocalized("command-ingame-only");
      return true;
   }
}
