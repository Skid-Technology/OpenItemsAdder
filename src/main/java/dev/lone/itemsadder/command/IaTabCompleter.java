package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.Core.OtherPlugins.Citizens.CitizensHook;
import dev.lone.itemsadder.block.BlockSoundProfile;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import dev.lone.itemsadder.util.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class IaTabCompleter implements IaListener, TabCompleter {
   private static final List aHD = Collections.singletonList("");

   public IaTabCompleter() {
      Main.getCommand("ia").setTabCompleter(this);
      Main.getCommand("iaget").setTabCompleter(this);
      Main.getCommand("iagive").setTabCompleter(this);
      Main.getCommand("iaremove").setTabCompleter(this);
      Main.getCommand("iadrop").setTabCompleter(this);
      Main.getCommand("iarecipe").setTabCompleter(this);
      Main.getCommand("iareload").setTabCompleter(this);
      Main.getCommand("iazip").setTabCompleter(this);
      Main.getCommand("iatexture").setTabCompleter(this);
      Main.getCommand("iaspawntree").setTabCompleter(this);
      Main.getCommand("iaplaytotemanimation").setTabCompleter(this);
      Main.getCommand("iaplayerstat").setTabCompleter(this);
      Main.getCommand("iasha1").setTabCompleter(this);
      Main.getCommand("iacleancache").setTabCompleter(this);
      Main.getCommand("iacustommodeldata").setTabCompleter(this);
      Main.getCommand("iaconfig").setTabCompleter(this);
      Main.getCommand("iaentity").setTabCompleter(this);
      Main.getCommand("iaemote").setTabCompleter(this);
      Main.getCommand("iaplaysound").setTabCompleter(this);
      Main.getCommand("iacolor").setTabCompleter(this);
      Main.getCommand("iadebug").setTabCompleter(this);
      Main.getCommand("iatag").setTabCompleter(this);
      Main.getCommand("iaimage").setTabCompleter(this);
      Main.getCommand("iadisguise").setTabCompleter(this);
      Main.getCommand("iaundisguise").setTabCompleter(this);
      Main.getCommand("crops").setTabCompleter(this);
   }

   public List onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
      if (!ContentManager.method8084().field82) {
         return aHD;
      }

      if (command.getName().equalsIgnoreCase("ia")) {
         if (args.length == 1) {
            List var25 = ContentManager.method8084().bB.aJX.LP();
            return !var25.isEmpty() ? var25 : Collections.singletonList("CategoryNotFound");
         } else {
            return args.length == 2 ? method852(args[1], false, sender) : aHD;
         }
      } else if (command.getName().equalsIgnoreCase("iaget")) {
         if (args.length == 1) {
            List var24 = ContentManager.method8084().method8168(args[0]);
            return !var24.isEmpty() ? var24 : Collections.singletonList("ItemNotFound");
         } else {
            return args.length == 2 ? Arrays.asList("5", "16", "32", "64") : aHD;
         }
      } else if (!command.getName().equalsIgnoreCase("iagive") && !command.getName().equalsIgnoreCase("iaremove")) {
         if (command.getName().equalsIgnoreCase("iadrop")) {
            if (args.length == 1) {
               List var23 = ContentManager.method8084().method8168(args[0]);
               return !var23.isEmpty() ? var23 : Collections.singletonList("ItemNotFound");
            } else if (args.length == 2) {
               return method852(args[1], false, sender);
            } else if (args.length == 3) {
               return Arrays.asList("5", "16", "32", "64");
            } else {
               return args.length == 4 ? Collections.singletonList("silent") : aHD;
            }
         } else if (command.getName().equalsIgnoreCase("iacustommodeldata")) {
            if (args.length == 1) {
               List var22 = ContentManager.method8084().method8168(args[0]);
               return !var22.isEmpty() ? var22 : Collections.singletonList("ItemNotFound");
            } else {
               return aHD;
            }
         } else if (command.getName().equalsIgnoreCase("iarecipe")) {
            if (args.length == 1) {
               List var21 = ContentManager.method8084().method8168(args[0]);
               return !var21.isEmpty() ? var21 : Collections.singletonList("ItemNotFound");
            } else {
               return args.length == 2 && PermissionNodes.method1632(sender, "ia.user.iarecipe.others") ? method852(args[1], false, sender) : aHD;
            }
         } else {
            if (command.getName().equalsIgnoreCase("iatexture")) {
               return args.length == 1 ? method852(args[0], true, sender) : aHD;
            }

            if (command.getName().equalsIgnoreCase("iareload")) {
               return args.length == 1 ? List.of("--scripts-only", "--skip-recipes") : aHD;
            }

            if (command.getName().equalsIgnoreCase("iazip")) {
               ArrayList var20 = new ArrayList();
               String var30 = args.length > 0 ? args[args.length - 1] : "";
               HashSet var34 = new HashSet<>(Arrays.asList("--uncompressed", "--apply-to", "--skip-recipes", "--open-ia-menu", "--check-duplicate-images"));
               Set var41 = Arrays.stream(args).filter(var34::contains).collect(Collectors.toSet());
               if (var30.startsWith("--")) {
                  for (String var47 : var34) {
                     if (var47.startsWith(var30) && !var41.contains(var47)) {
                        var20.add(var47);
                     }
                  }

                  return var20;
               } else {
                  for (int var43 = 0; var43 < args.length - 1; var43++) {
                     if (args[var43].equalsIgnoreCase("--apply-to") && var43 + 1 == args.length - 1) {
                        return Arrays.asList("all", "self", "none");
                     }
                  }

                  for (String var46 : var34) {
                     if (!var41.contains(var46)) {
                        var20.add(var46);
                     }
                  }

                  return var20;
               }
            } else if (command.getName().equalsIgnoreCase("iaspawntree")) {
               if (args.length == 1) {
                  List var19 = ContentManager.method8084().method8170(args[0]);
                  return !var19.isEmpty() ? var19 : Collections.singletonList("TreeNotFound");
               } else {
                  return aHD;
               }
            } else if (command.getName().equalsIgnoreCase("iaplaytotemanimation")) {
               if (args.length == 1) {
                  List var18 = ContentManager.method8084().method8171(Material.TOTEM_OF_UNDYING, args[0]);
                  return !var18.isEmpty() ? var18 : Collections.singletonList("TotemNotFound");
               } else if (args.length == 2) {
                  return method852(args[1], false, sender);
               } else {
                  return args.length == 3 ? Collections.singletonList("silent") : aHD;
               }
            } else if (command.getName().equalsIgnoreCase("iaplayerstat")) {
               if (args.length == 1) {
                  return Arrays.asList("read", "write", "increment", "decrement");
               }

               if (args.length == 2) {
                  return method852(args[1], false, sender);
               }

               if (args.length == 3) {
                  return ContentManager.method8084().method8172(args[2]);
               }

               if (args.length == 4) {
                  if (args[0].equals("write") || args[0].equals("decrement") || args[0].equals("increment")) {
                     return Arrays.asList("0", "5", "10");
                  }

                  if (args[0].equals("read")) {
                     return List.of("");
                  }
               }

               return aHD;
            } else {
               if (command.getName().equalsIgnoreCase("iasha1")) {
                  return args.length == 1 ? Arrays.asList("local", "online") : aHD;
               }

               if (command.getName().equalsIgnoreCase("iacleancache")) {
                  return args.length == 1 ? Arrays.asList("items", "blocks") : aHD;
               }

               if (command.getName().equalsIgnoreCase("iaconfig")) {
                  if (args.length == 1) {
                     return Arrays.asList("item", "namespace", "recipes");
                  }

                  if (args.length == 2) {
                     switch (args[0]) {
                        case "item":
                        case "namespace":
                           return Arrays.asList("delete", "disable");
                        case "recipes":
                           return Arrays.asList("deleteofitem", "disableofitem");
                     }
                  }

                  if (args.length == 3) {
                     switch (args[0]) {
                        case "item":
                        case "recipes":
                           List var33 = ContentManager.method8084().method8168(args[2]);
                           if (!var33.isEmpty()) {
                              return var33;
                           }

                           return Collections.singletonList("ItemNotFound");
                        case "namespace":
                           LinkedList var32 = new LinkedList();

                           for (String var42 : ContentManager.method8084().as) {
                              if (var42.contains(args[2])) {
                                 var32.add(var42);
                              }
                           }

                           if (!var32.isEmpty()) {
                              return new ArrayList(var32);
                           }

                           return Collections.singletonList("NamespaceNotFound");
                     }
                  }

                  return aHD;
               } else {
                  if (command.getName().equalsIgnoreCase("iarename")) {
                     return aHD;
                  }

                  if (command.getName().equalsIgnoreCase("iaentity") && Settings.get().customEntitiesEnabled) {
                     if (args.length == 1) {
                        return Arrays.asList("summon", "anim", "kill", "count", "inspect", "citizens", "summonplayer", "playerskin", "disguise");
                     }

                     if (args.length >= 2) {
                        switch (args[0]) {
                           case "disguise":
                              if (args.length == 2) {
                                 return CustomEntityManager.getInstance().findDefinitions(args[1], false);
                              }

                              if (args.length == 3) {
                                 return method852(args[2], false, sender);
                              }
                              break;
                           case "summon":
                              Location var15;
                              if (sender instanceof Player var39) {
                                 var15 = var39.getLocation().clone();
                              } else {
                                 var15 = new Location((World)Bukkit.getWorlds().get(0), 0.0, 0.0, 0.0);
                              }

                              if (args.length == 2) {
                                 return CustomEntityManager.getInstance().findDefinitions(args[1], false);
                              }

                              if (args.length == 3) {
                                 return List.of(var15.getWorld().getName());
                              }

                              if (args.length == 4) {
                                 return List.of(Utils.formatNumberShort(var15.getX()));
                              }

                              if (args.length == 5) {
                                 return List.of(Utils.formatNumberShort(var15.getY()));
                              }

                              if (args.length == 6) {
                                 return List.of(Utils.formatNumberShort(var15.getZ()));
                              }

                              if (args.length == 7) {
                                 return List.of(Utils.formatNumberShort(var15.getYaw()));
                              }

                              if (args.length == 8) {
                                 return List.of(Utils.formatNumberShort(var15.getPitch()));
                              }
                              break;
                           case "kill":
                              ArrayList var38 = new ArrayList();
                              var38.add("all");
                              var38.addAll(CustomEntityManager.getInstance().findDefinitions(args[1], true));
                              return var38;
                           case "anim":
                              if (args.length == 2) {
                                 return Arrays.asList("play", "pause");
                              }

                              if (args.length == 3) {
                                 if (sender instanceof Player) {
                                    CustomEntityImpl var37 = CustomEntityManager.getInstance().findNearestEntity(((Player)sender).getLocation());
                                    if (var37 == null) {
                                       return Collections.singletonList("");
                                    }

                                    if (var37.getModel().AM) {
                                       return CustomEntityManager.getInstance().getEmoteNames();
                                    }

                                    return var37.getAnimationsNames();
                                 }
                              } else if (args.length == 6) {
                                 return Arrays.asList("0", "1", "2", "3", "4", "5", "6");
                              }
                              break;
                           case "citizens":
                              if (args.length == 2) {
                                 if (Main.aFc) {
                                    ArrayList var36 = new ArrayList();
                                    var36.add("selected");
                                    var36.addAll(CitizensHook.getNpcsIds());
                                    return var36;
                                 }

                                 return Collections.singletonList("");
                              }

                              if (args.length == 3) {
                                 return Arrays.asList("model", "clean", "anim", "playerskin");
                              }

                              switch (args[2]) {
                                 case "model":
                                    return CustomEntityManager.getInstance().findDefinitions(args[3], true);
                                 case "clean":
                                    return aHD;
                                 case "anim":
                                    if (args.length == 4) {
                                       return Arrays.asList("play", "pause");
                                    }

                                    if (args.length == 5) {
                                       if (Main.aFc) {
                                          CustomEntityImpl var10 = CitizensHook.getSelectedNpcEntityRenderer(sender);
                                          if (var10 == null) {
                                             return Collections.singletonList("");
                                          }

                                          if (var10.getModel().AM) {
                                             return CustomEntityManager.getInstance().getEmoteNames();
                                          }

                                          return var10.getAnimationsNames();
                                       }
                                    } else if (args.length == 6) {
                                       return Arrays.asList("0", "1", "2", "3", "4", "5", "6");
                                    }

                                    return aHD;
                                 case "playerskin":
                                    return Arrays.asList("CITIZENS_SKIN", "https://minesk.in/5f1e7ff6409e428bb500cc9315bf7ffb", "PlayerName");
                                 default:
                                    return aHD;
                              }
                           case "summonplayer":
                              Location var14;
                              if (sender instanceof Player var8) {
                                 var14 = var8.getLocation().clone();
                              } else {
                                 var14 = new Location((World)Bukkit.getWorlds().get(0), 0.0, 0.0, 0.0);
                              }

                              if (args.length == 2) {
                                 return Arrays.asList("https://minesk.in/5f1e7ff6409e428bb500cc9315bf7ffb", "PlayerName");
                              }

                              if (args.length == 3) {
                                 return List.of(var14.getWorld().getName());
                              }

                              if (args.length == 4) {
                                 return List.of(Utils.formatNumberShort(var14.getX()));
                              }

                              if (args.length == 5) {
                                 return List.of(Utils.formatNumberShort(var14.getY()));
                              }

                              if (args.length == 6) {
                                 return List.of(Utils.formatNumberShort(var14.getZ()));
                              }

                              if (args.length == 7) {
                                 return List.of(Utils.formatNumberShort(var14.getYaw()));
                              }

                              if (args.length == 8) {
                                 return List.of(Utils.formatNumberShort(var14.getPitch()));
                              }
                              break;
                           case "playerskin":
                              if (args.length == 2) {
                                 return Arrays.asList("https://minesk.in/5f1e7ff6409e428bb500cc9315bf7ffb", "PlayerName");
                              }
                              break;
                           default:
                              return aHD;
                        }
                     }

                     return aHD;
                  } else if (command.getName().equalsIgnoreCase("iaemote") && Settings.get().emotesEnabled) {
                     if (!CustomEntityManager.getInstance().fG()) {
                        return aHD;
                     }

                     if (args.length == 1) {
                        if (sender instanceof Player) {
                           List var13 = CustomEntityManager.getInstance().getEmoteNames();
                           var13.add("stop-now");
                           var13.add("stop");
                           String var26 = args[0].toLowerCase(Locale.ROOT).replace("-", "");
                           return var13.stream()
                              .filter(var1x -> CustomEntityManager.getInstance().matchesEmoteSearch(var1x, var26))
                              .collect(Collectors.toList());
                        }
                     } else if (args.length == 2 && sender.hasPermission("ia.user.iaemote.others")) {
                        return method852(args[1], false, sender);
                     }

                     return aHD;
                  } else {
                     if (command.getName().equalsIgnoreCase("iadisguise") && Settings.get().emotesEnabled) {
                        if (args.length == 1) {
                           return CustomEntityManager.getInstance().findDefinitions(args[0], false);
                        }
                     } else {
                        if (command.getName().equalsIgnoreCase("iaundisguise")) {
                           return aHD;
                        }

                        if (command.getName().equalsIgnoreCase("iaplaysound")) {
                           if (args.length == 1) {
                              return Collections.singletonList("custom_sound_name");
                           }

                           if (args.length == 2) {
                              return method852(args[1], false, sender);
                           }

                           if (args.length == 3) {
                              return List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
                           }

                           if (args.length == 4) {
                              return List.of("0.5", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
                           }
                        } else if (command.getName().equalsIgnoreCase("iacolor")) {
                           if (args.length == 1) {
                              return List.of(
                                 "#ffffff",
                                 "#000000",
                                 "#4287f5",
                                 "#692fba",
                                 "#ba2f41",
                                 "#ba802f",
                                 "255,0,0",
                                 "WHITE",
                                 "SILVER",
                                 "GRAY",
                                 "BLACK",
                                 "RED",
                                 "MAROON",
                                 "YELLOW",
                                 "OLIVE",
                                 "LIME",
                                 "GREEN",
                                 "AQUA",
                                 "TEAL",
                                 "BLUE",
                                 "NAVY",
                                 "FUCHSIA",
                                 "PURPLE",
                                 "ORANGE"
                              );
                           }

                           if (args.length == 2) {
                              return method852(args[1], false, sender);
                           }
                        } else if (command.getName().equalsIgnoreCase("iadebug")) {
                           if (args.length == 1) {
                              return Arrays.asList(
                                 "region",
                                 "placeCrops",
                                 "cleanCrops",
                                 "stopEmote",
                                 "starve",
                                 "get-item-with-custom-model-data",
                                 "messages",
                                 "customEntities",
                                 "entities"
                              );
                           }

                           if (args.length < 2) {
                              return aHD;
                           }

                           if (args[0].equals("region")) {
                              if (args.length != 3 && args.length != 4) {
                                 return Lv();
                              }

                              return Arrays.asList("-2", "-1", "0", "1", "2");
                           }

                           if (args[0].equals("placeCrops")) {
                              if (args.length == 2) {
                                 return List.copyOf(ContentManager.method8084().au.keySet());
                              }

                              if (args.length == 3) {
                                 return Arrays.asList("10", "20", "30", "40", "50");
                              }

                              if (args.length == 4) {
                                 BlockSoundProfile var11 = (BlockSoundProfile)ContentManager.method8084().au.get(args[1]);
                                 if (var11 == null) {
                                    return List.of("0");
                                 }

                                 return Arrays.asList("0", String.valueOf(var11.jW));
                              }
                           } else if (args[0].equals("get-item-with-custom-model-data")) {
                              if (args.length == 2) {
                                 return Arrays.stream(Material.values())
                                    .filter(Material::isItem)
                                    .map(Enum::name)
                                    .filter(var1x -> var1x.contains(args[1].toUpperCase()))
                                    .collect(Collectors.toList());
                              }

                              if (args.length == 3) {
                                 return Arrays.asList("0", "1", "2", "3", "4", "5", "10", "50", "100", "500", "1000");
                              }
                           } else if (args[0].equalsIgnoreCase("customEntities")) {
                              if (args.length == 2) {
                                 return EntityCommand.aHj;
                              }

                              if (args.length == 3 && args[1].equalsIgnoreCase("ragdollFriction")) {
                                 return EntityCommand.aHk.stream().filter(var1x -> var1x.startsWith(args[2])).toList();
                              }

                              if (args.length == 3 && args[1].equalsIgnoreCase("summonNoAi")) {
                                 return CustomEntityManager.getInstance().findDefinitions(args[2], false);
                              }

                              if (args.length == 3 && args[1].equalsIgnoreCase("changeAnimation") && sender instanceof Player var12) {
                                 CustomEntityImpl var6 = CustomEntityManager.getInstance().findNearestEntity(var12.getLocation());
                                 if (var6 == null) {
                                    return aHD;
                                 }

                                 String var7 = args[2].toLowerCase(Locale.ROOT);
                                 return var6.getAnimationsNames().stream().filter(var1x -> var1x.toLowerCase(Locale.ROOT).contains(var7)).toList();
                              }
                           } else if (args[0].equalsIgnoreCase("entities")) {
                              if (args.length == 2) {
                                 return List.of("compare", "equipment");
                              }

                              if (args.length == 3 && args[1].equalsIgnoreCase("equipment")) {
                                 return List.of("mainhand", "offhand", "head", "chest", "legs", "feet");
                              }

                              if (args.length == 3 && args[1].equalsIgnoreCase("compare")) {
                                 return List.of("start", "status", "report", "stop");
                              }

                              if (args.length == 4 && args[2].equalsIgnoreCase("start")) {
                                 return CustomEntityManager.getInstance().findDefinitions(args[3], false);
                              }

                              if (args.length == 5 && args[2].equalsIgnoreCase("start")) {
                                 return List.of("zombie");
                              }

                              if (args.length == 6 && args[2].equalsIgnoreCase("start")) {
                                 return List.of("combat", "--capture");
                              }

                              if (args.length == 7 && args[2].equalsIgnoreCase("start")) {
                                 return List.of("--capture");
                              }
                           }
                        } else if (command.getName().equalsIgnoreCase("iatag")) {
                           if (args.length >= 1) {
                              return List.of("show-in-chat", "show-in-console", "pretty-print");
                           }
                        } else if (command.getName().equalsIgnoreCase("iaimage")) {
                           if (PermissionNodes.method1631(sender, "ia.user.image.hints", true)) {
                              return method853(sender, args);
                           }
                        } else if (command.getName().equalsIgnoreCase("crops")) {
                           if (args.length == 1) {
                              return Arrays.asList("enable", "disable");
                           }

                           if (args.length == 2 && PermissionNodes.method1632(sender, "ia.admin.crops.others")) {
                              return method852(args[1], false, sender);
                           }
                        }
                     }

                     return aHD;
                  }
               }
            }
         }
      } else if (args.length == 1) {
         return method852(args[0], false, sender);
      } else if (args.length == 2) {
         List var5 = ContentManager.method8084().method8168(args[1]);
         return !var5.isEmpty() ? var5 : Collections.singletonList("ItemNotFound");
      } else if (args.length == 3) {
         return Arrays.asList("5", "16", "32", "64");
      } else {
         return args.length == 4 ? Collections.singletonList("silent") : aHD;
      }
   }

   public static List method852(@NotNull String var0, boolean var1, CommandSender var2) {
      ArrayList var3 = new ArrayList();
      if (var1) {
         var3.add("all");
      }

      if (var2 instanceof Player var4) {
         for (Player var6 : Bukkit.getOnlinePlayers()) {
            if (var4.canSee(var6) && (var0.isEmpty() || var6.getName().contains(var0))) {
               var3.add(var6.getName());
            }
         }
      } else {
         for (Player var8 : Bukkit.getOnlinePlayers()) {
            if (var0.isEmpty() || var8.getName().contains(var0)) {
               var3.add(var8.getName());
            }
         }
      }

      return var3;
   }

   public static List Lv() {
      ArrayList var0 = new ArrayList();

      for (World var2 : Bukkit.getWorlds()) {
         var0.add(var2.getName());
      }

      return var0;
   }

   public static List method853(CommandSender var0, String[] var1) {
      Collection var2 = ContentManager.method8084().aE.Yp.values();
      ArrayList var3 = new ArrayList();
      if (var1[var1.length - 1].isEmpty()) {
         for (CustomTexture var5 : var2) {
            if (var5.xf() && var5.hasPermission(var0)) {
               var3.add(var5.getDisplayText(false));
            }
         }
      } else {
         for (CustomTexture var7 : var2) {
            if (var7.xf() && var7.getId().contains(var1[var1.length - 1]) && var7.hasPermission(var0)) {
               var3.add(var7.getDisplayText(false));
            }
         }
      }

      return var3;
   }
}
