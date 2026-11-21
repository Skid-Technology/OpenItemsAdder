package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.recipe.AbstractCustomRecipe;
import dev.lone.itemsadder.recipe.AnvilRecipe;
import dev.lone.itemsadder.recipe.CookingRecipe;
import dev.lone.itemsadder.recipe.CraftingRecipe;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.Utils;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.function.Function;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RecipeCommand extends IaCommandBase {
   final WeakHashMap aHh = new WeakHashMap();

   public RecipeCommand() {
      Main.getCommand("iaconfig").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      }

      this.method1544(commandSender, alias, args);
      return true;
   }

   private void method1544(CommandSender var1, String var2, String[] var3) {
      if (var3.length < 3) {
         Log.sendPrefixed(var1, Main.aFi.lj("wrong-command-usage"));
      } else {
         switch (var3[0]) {
            case "item":
               switch (var3[1]) {
                  case "delete":
                     if (!PermissionNodes.method1631(var1, "ia.admin.iaconfig.item.delete", true)) {
                        return;
                     }

                     this.method1547(
                        var1,
                        var2,
                        var3,
                        Utils.toSectionCodes(
                           "&cWARNING: if you delete this item you won't be able to use the command to delete recipes of this item.\nConsider using &6/iaconfig recipes deleteofitem "
                              + var3[2]
                              + "&c before."
                        ),
                        true,
                        var1x -> {
                           var1x.lH().jJ("items." + var1x.getId());
                           var1x.lH().save();
                           Log.sendPrefixed(var1, Utils.toSectionCodes("&aDeleted &e" + var1x.getNamespacedID() + "&a from file: &e" + var1x.DJ()));
                           return true;
                        }
                     );
                     return;
                  case "disable":
                     if (!PermissionNodes.method1631(var1, "ia.admin.iaconfig.item.disable", true)) {
                        return;
                     }

                     this.method1547(
                        var1,
                        var2,
                        var3,
                        Utils.toSectionCodes(
                           "&cWARNING: if you disable this item you won't be able to use the command to disable/delete recipes of this item. You will have to re-enable it manually.\nConsider using &6/iaconfig recipes disableofitem "
                              + var3[2]
                              + "&c before."
                        ),
                        true,
                        var1x -> {
                           var1x.lH().method7048("items." + var1x.getId() + ".enabled", false);
                           var1x.lH().save();
                           Log.sendPrefixed(var1, Utils.toSectionCodes("&aDisabled &e" + var1x.getNamespacedID() + "&a from file: &e" + var1x.DJ()));
                           return true;
                        }
                     );
                     return;
                  default:
                     return;
               }
            case "namespace":
               switch (var3[1]) {
                  case "delete":
                     if (!PermissionNodes.method1631(var1, "ia.admin.iaconfig.namespace.delete", true)) {
                        return;
                     }

                     this.method1548(
                        var1,
                        var2,
                        var3,
                        ChatColor.RED + "WARNING: if you delete this namespace it will delete both items and recipes which use these items!",
                        true,
                        var2x -> {
                           var2x.lH().jJ("items." + var2x.getId());
                           var2x.lH().save();
                           Log.sendPrefixed(var1, ChatColor.GREEN + "Deleted " + var2x.getNamespacedID() + " from file: " + var2x.DJ());
                           int var3x = 0;
                           var3x += this.method1550(CraftingRecipe.class, var2x, this::method1545);
                           var3x += this.method1550(CookingRecipe.class, var2x, this::method1545);
                           var3x += this.method1550(AnvilRecipe.class, var2x, this::method1545);
                           if (var3x > 0) {
                              Log.sendPrefixed(
                                 var1, Utils.toSectionCodes("&aDeleted &e" + var3x + "&a recipes which use/create item &e" + var2x.getNamespacedID())
                              );
                              return true;
                           } else {
                              return true;
                           }
                        }
                     );
                     return;
                  case "disable":
                     if (!PermissionNodes.method1631(var1, "ia.admin.iaconfig.namespace.disable", true)) {
                        return;
                     }

                     this.method1548(
                        var1,
                        var2,
                        var3,
                        null,
                        false,
                        var2x -> {
                           var2x.lH().method7048("items." + var2x.getId() + ".enabled", false);
                           var2x.lH().save();
                           Log.sendPrefixed(var1, Utils.toSectionCodes("&aDisabled &e" + var2x.getNamespacedID() + "&a from file: &e" + var2x.DJ()));
                           int var3x = 0;
                           var3x += this.method1550(CraftingRecipe.class, var2x, this::method1546);
                           var3x += this.method1550(CookingRecipe.class, var2x, this::method1546);
                           var3x += this.method1550(AnvilRecipe.class, var2x, this::method1546);
                           if (var3x > 0) {
                              Log.sendPrefixed(
                                 var1, Utils.toSectionCodes("&aDisabled &e" + var3x + "&a recipes which use/create item &e" + var2x.getNamespacedID())
                              );
                           }

                           return true;
                        }
                     );
                     return;
                  default:
                     return;
               }
            case "recipes":
               switch (var3[1]) {
                  case "deleteofitem":
                     if (!PermissionNodes.method1631(var1, "ia.admin.iaconfig.recipes.deleteofitem", true)) {
                        return;
                     }

                     this.method1547(
                        var1,
                        var2,
                        var3,
                        null,
                        false,
                        var2x -> {
                           int var3x = 0;
                           var3x += this.method1550(CraftingRecipe.class, var2x, this::method1545);
                           var3x += this.method1550(CookingRecipe.class, var2x, this::method1545);
                           var3x += this.method1550(AnvilRecipe.class, var2x, this::method1545);
                           if (var3x > 0) {
                              Log.sendPrefixed(
                                 var1, Utils.toSectionCodes("&aDeleted &e" + var3x + "&a recipes which use/create item &e" + var2x.getNamespacedID())
                              );
                              return true;
                           } else {
                              return false;
                           }
                        }
                     );
                     break;
                  case "disableofitem":
                     if (!PermissionNodes.method1631(var1, "ia.admin.iaconfig.recipes.disableofitem", true)) {
                        return;
                     }

                     this.method1547(
                        var1,
                        var2,
                        var3,
                        null,
                        false,
                        var2x -> {
                           int var3x = 0;
                           var3x += this.method1550(CraftingRecipe.class, var2x, this::method1546);
                           var3x += this.method1550(CookingRecipe.class, var2x, this::method1546);
                           var3x += this.method1550(AnvilRecipe.class, var2x, this::method1546);
                           if (var3x > 0) {
                              Log.sendPrefixed(
                                 var1, Utils.toSectionCodes("&aDisabled &e" + var3x + "&a recipes which use/create item &e" + var2x.getNamespacedID())
                              );
                              return true;
                           } else {
                              return false;
                           }
                        }
                     );
               }
         }
      }
   }

   private Function method1545(AbstractCustomRecipe var1) {
      if (var1.vp != null) {
         Log.log(Utils.toSectionCodes("&eDeleted recipe: &2" + var1.aBm + "&e from file: &2" + var1.vp.DJ()));
         var1.vp.jJ(var1.aBm);
      }

      return null;
   }

   private Function method1546(AbstractCustomRecipe var1) {
      if (var1.vp != null) {
         Log.log(Utils.toSectionCodes("&eDisabled recipe: &2" + var1.aBm + "&e from file: &2" + var1.vp.DJ()));
         var1.vp.method7048(var1.aBm + ".enabled", false);
      }

      return null;
   }

   private void method1547(CommandSender var1, String var2, String[] var3, String var4, boolean var5, Function var6) {
      String var7 = var3[2];
      if (!var7.contains(":")) {
         Log.sendPrefixed(var1, Utils.toSectionCodes("&eSpecify the complete identifier for the item &2" + var7 + "&e. For example &2myitems:red_sword"));
      } else {
         CustomItemDefinition var8 = ContentManager.method8084().method8154(var7);
         if (var8 == null) {
            Log.sendPrefixed(var1, ChatColor.YELLOW + "Can't find item " + var7);
         } else {
            PendingConfirmation var9 = (PendingConfirmation)this.aHh.get(var1);
            if (var9 == null || !var9.method2393(var2, var3)) {
               PendingConfirmation.method2391(this.aHh, var1, var2, var3, var4);
            } else if (var9.method2392(var2, var3)) {
               if (!(Boolean)var6.apply(var8)) {
                  Log.sendPrefixed(
                     var1, Utils.toSectionCodes("&eNo operation done for item &2" + var8.getNamespacedID() + "&e, failed to find anything to change.")
                  );
                  return;
               }

               this.method1549(var1, var5);
            } else {
               PendingConfirmation.method2391(this.aHh, var1, var2, var3, var4);
            }
         }
      }
   }

   private void method1548(CommandSender var1, String var2, String[] var3, String var4, boolean var5, Function var6) {
      String var7 = var3[2];
      if (var7.contains(":")) {
         Log.sendPrefixed(var1, ChatColor.YELLOW + "Specify enter a valid namespace.");
      } else if (!ContentManager.method8084().as.contains(var7)) {
         Log.sendPrefixed(var1, ChatColor.YELLOW + "Can't find namespace " + var7);
      } else {
         PendingConfirmation var8 = (PendingConfirmation)this.aHh.get(var1);
         if (var8 != null && var8.method2393(var2, var3)) {
            if (!var8.method2392(var2, var3)) {
               PendingConfirmation.method2391(this.aHh, var1, var2, var3, var4);
               return;
            }

            boolean var9 = false;
            HashMap var10 = (HashMap)ContentManager.method8084().af.get(var7);
            if (var10 != null) {
               for (Entry var12 : var10.entrySet()) {
                  String var13 = (String)var12.getKey();
                  CustomItemDefinition var14 = (CustomItemDefinition)var12.getValue();
                  if ((Boolean)var6.apply(var14)) {
                     var9 = true;
                  }
               }
            }

            if (!var9) {
               Log.sendPrefixed(var1, Utils.toSectionCodes("&eNo operation done for namespace &2" + var7 + "&e, failed to find anything to change."));
               return;
            }

            this.method1549(var1, var5);
         } else {
            PendingConfirmation.method2391(this.aHh, var1, var2, var3, var4);
         }
      }
   }

   private void method1549(CommandSender var1, boolean var2) {
      if (var2) {
         Log.sendPrefixed(
            var1, ChatColor.YELLOW + "WARNING: Textures and models are not automatically deleted from the resource_pack folder to avoid mistakes."
         );
      }

      Log.sendPrefixed(var1, ChatColor.YELLOW + "Reloading the plugin in 5 seconds...");
      SchedulerUtils.runLaterSync(() -> ReloadCommand.method1532(false, () -> Log.sendPrefixed(var1, ChatColor.GREEN + "Reloaded plugin.")), 100L);
   }

   private int method1550(Class var1, CustomItemDefinition var2, Function var3) {
      int var4 = 0;

      for (AbstractCustomRecipe var6 : ContentManager.method8084().bp.method2230(var1, var2)) {
         var3.apply(var6);
         if (var6.vp != null) {
            var6.vp.save();
            var4++;
         }
      }

      return var4;
   }
}
