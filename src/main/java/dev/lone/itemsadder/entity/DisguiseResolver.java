package dev.lone.itemsadder.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.Core.OtherPlugins.Citizens.CitizensHook;
import dev.lone.itemsadder.Core.OtherPlugins.Citizens.CustomEntityTrait;
import dev.lone.itemsadder.command.IaCommandBase;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.Utils;
import java.util.HashMap;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.ClickEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DisguiseResolver implements CommandExecutor {
   HashMap aHq = new HashMap();
   HashMap aHr = new HashMap();

   public DisguiseResolver() {
      SchedulerUtils.runRepeating(var1 -> {
         this.aHq.clear();
         this.aHr.clear();
      }, 0L, 6000L);
      Main.getCommand("iaentity").setExecutor(this);
   }

   private static boolean canSpawnEntity(CommandSender var0, String var1, Location var2) {
      CustomMobSettings var3 = CustomEntityManager.getInstance().getSettings(var1);
      boolean var4 = ProjectileTypeHelper.getAdapter().method3258(var3.vw);
      if (!isSpawnAllowedOnDifficulty(var2.getWorld().getDifficulty(), var4)) {
         String var5 = Main.aFi.lj("custom-entity-cannot-spawn-peaceful").replace("{entity}", var1).replace("{type}", var3.vw.toString());
         Log.warn(AdventureText.toPlainText(AdventureText.parseRichText(var5)));
         if (var0 instanceof Player) {
            Log.sendComponent(var0, Log.prefixComponent.append(AdventureText.parseRichText(var5)));
         }

         return false;
      } else {
         return true;
      }
   }

   static boolean isSpawnAllowedOnDifficulty(Difficulty var0, boolean var1) {
      return var0 != Difficulty.PEACEFUL || var1;
   }

   private static void summonPlayerEntity(CommandSender var0, String var1, Location var2) {
      String var3 = Settings.get().emoteModernMethod && CustomEntityManager.getInstance().getSettings("_iainternal:player_v2") != null
         ? "_iainternal:player_v2"
         : "_iainternal:player";
      if (canSpawnEntity(var0, var3, var2)) {
         String var4 = var1 == null
            ? "ewogICJ0aW1lc3RhbXAiIDogMTY2NjMwMjkxNzkzOCwKICAicHJvZmlsZUlkIiA6ICJjOWRlZTM4MDUzYjg0YzI5YjZlZjA5YjJlMDM5OTc0ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTQVJfRGVjZW1iZXI1IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzMxZjQ3N2ViMWE3YmVlZTYzMWMyY2E2NGQwNmY4ZjY4ZmE5M2EzMzg2ZDA0NDUyYWIyN2Y0M2FjZGYxYjYwY2IiCiAgICB9CiAgfQp9"
            : var1;
         if (CustomEntityImpl.isMineskinUrl(var4)) {
            SchedulerUtils.submitAsync(() -> {
               String var3x = CustomEntityImpl.resolveSkinTexture(var4);
               SchedulerUtils.runOnMainThread(() -> CustomEntityManager.getInstance().spawnEntityAt(var3, var3x, var2, true, false, false));
            });
         } else {
            CustomEntityManager.getInstance().spawnEntityAt(var3, var4, var2, true, false, false);
         }
      }
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (!Settings.get().customEntitiesEnabled) {
         Log.sendLocalized(sender, "custom-entities-disabled");
         return true;
      }

      if (IaCommandBase.method1428(3, args) && args[0].equals("citizens")) {
         String var6 = args[1];
         NPC var5;
         if (var6.equals("selected")) {
            var5 = CitizensAPI.getDefaultNPCSelector().getSelected(sender);
         } else if (Utils.isNumeric(var6)) {
            var5 = CitizensAPI.getNPCRegistry().getById(Utils.parseIntOr(var6, 0));
         } else {
            var5 = null;
         }

         if (var5 == null) {
            Log.sendLocalized(sender, "no-npc-selected");
            return true;
         }

         if (args[2].equals("model")) {
            if (!isKnownEntity(args[3])) {
               return true;
            }

            CustomEntityTrait var41 = (CustomEntityTrait)var5.getTraitNullable(CustomEntityTrait.class);
            if (var41 == null) {
               var41 = CustomEntityTrait.inject(var5);
               var41.applyModel(args[3], true);
            } else {
               var41.applyModel(args[3], false);
            }

            CitizensHook.saveAll(true);
            return true;
         }

         if (args[2].equals("clean")) {
            CustomEntityTrait var40 = (CustomEntityTrait)var5.getTraitNullable(CustomEntityTrait.class);
            if (var40 != null) {
               CustomEntityManager.getInstance().removeWithEffects(var40.getRenderer(), false, false);
               var5.removeTrait(CustomEntityTrait.class);
               CitizensHook.respawn(var5);
               CitizensHook.reload();
            }

            return true;
         }

         CustomEntityTrait var8 = (CustomEntityTrait)var5.getTraitNullable(CustomEntityTrait.class);
         boolean var7 = var8 == null;
         if (args[2].equals("anim")) {
            if (var7) {
               Log.sendLocalized(sender, "no-npc-selected");
               return true;
            }

            if (args[3].equals("play")) {
               int var50 = 0;
               if (args.length == 6) {
                  var50 = Utils.parseIntOr(args[5], 0);
               }

               var8.playAnim(args[4], var50);
            } else {
               var8.pauseAnim();
            }

            CitizensHook.saveAll(true);
            return true;
         }

         if (args[2].equals("playerskin")) {
            if (var8 == null) {
               var8 = CustomEntityTrait.inject(var5);
               var8.initPlayerModel(args[3]);
            } else {
               var8.changePlayerSkin(args[3]);
            }

            CitizensHook.saveAll(true);
            return true;
         }
      }

      if (args.length > 0) {
         if (args[0].equals("summon")) {
            if (args.length == 1) {
               this.sendHelp(sender);
               return true;
            }

            String var19 = args[1];
            if (!isKnownEntity(var19)) {
               Log.sendLocalizedWith(sender, "unknown-entity", var1x -> var1x.replace("{entity}", var19));
               return true;
            }

            if (args.length < 6) {
               if (sender instanceof Player) {
                  Location var31 = ((Player)sender).getLocation().clone();
                  var31.setYaw(0.0F);
                  var31.setPitch(0.0F);
                  if (canSpawnEntity(sender, var19, var31)) {
                     CustomEntityManager.getInstance().spawnCustomEntitySimple(var19, var31, true);
                  }
               }

               return true;
            }

            if (Utils.isNumericAt(args, 3) && Utils.isNumericAt(args, 4) && Utils.isNumericAt(args, 5)) {
               World var30 = Bukkit.getWorld(args[2]);
               if (var30 == null) {
                  this.sendHelp(sender);
                  return true;
               }

               Location var39 = new Location(var30, Float.parseFloat(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
               if (args.length >= 7) {
                  if (Utils.isNumericAt(args, 6) && Utils.isNumericAt(args, 7)) {
                     var39.setYaw(Float.parseFloat(args[6]));
                     var39.setPitch(Float.parseFloat(args[7]));
                  } else {
                     this.sendHelp(sender);
                  }
               }

               if (canSpawnEntity(sender, var19, var39)) {
                  CustomEntityManager.getInstance().spawnCustomEntitySimple(var19, var39, true);
               }
            } else {
               this.sendHelp(sender);
            }

            return true;
         }

         if (args[0].equals("summonplayer")) {
            String var18 = args.length >= 2 ? args[1] : null;
            if (args.length >= 5) {
               if (Utils.isNumericAt(args, 3) && Utils.isNumericAt(args, 4) && Utils.isNumericAt(args, 5)) {
                  World var28 = Bukkit.getWorld(args[2]);
                  if (var28 == null) {
                     this.sendHelp(sender);
                     return true;
                  }

                  Location var38 = new Location(var28, Float.parseFloat(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
                  if (args.length >= 7) {
                     if (Utils.isNumericAt(args, 6) && Utils.isNumericAt(args, 7)) {
                        var38.setYaw(Float.parseFloat(args[6]));
                        var38.setPitch(Float.parseFloat(args[7]));
                     } else {
                        this.sendHelp(sender);
                     }
                  }

                  summonPlayerEntity(sender, var18, var38);
               } else {
                  this.sendHelp(sender);
               }
            } else if (sender instanceof Player) {
               Location var29 = ((Player)sender).getLocation().clone();
               var29.setYaw(0.0F);
               var29.setPitch(0.0F);
               summonPlayerEntity(sender, var18, var29);
            } else {
               this.sendHelp(sender);
            }

            return true;
         }

         if (args[0].equals("kill")) {
            if (args.length > 1) {
               int var11 = 0;
               if (args[1].equals("all")) {
                  UnmodifiableIterator var21 = CustomEntityManager.getInstance().getAllEntities().iterator();

                  while (var21.hasNext()) {
                     CustomEntityImpl var33 = (CustomEntityImpl)var21.next();
                     if (!Main.aFc || !CitizensHook.isNPC(var33.getBukkitEntity())) {
                        CustomEntityManager.getInstance().removeWithEffects(var33, true, false);
                        var11++;
                     }
                  }

                  Log.sendPrefixed(sender, Main.aFi.lj("custom-entity-kill-all").replace("{amount}", var11 + ""));
                  return true;
               }

               if (!isKnownEntity(args[1])) {
                  return true;
               }

               UnmodifiableIterator var20 = CustomEntityManager.getInstance().getAllEntities().iterator();

               while (var20.hasNext()) {
                  CustomEntityImpl var32 = (CustomEntityImpl)var20.next();
                  if (var32.getNamespacedId().equals(args[1]) && (!Main.aFc || !CitizensHook.isNPC(var32.getBukkitEntity()))) {
                     CustomEntityManager.getInstance().removeWithEffects(var32, true, false);
                     var11++;
                  }
               }

               Log.sendPrefixed(sender, Main.aFi.lj("custom-entity-kill").replace("{mob}", args[1]).replace("{amount}", var11 + ""));
               return true;
            }
         } else {
            if (args[0].equals("count")) {
               HashMap var17 = new HashMap();
               HashMap var27 = new HashMap();
               HashMap var37 = new HashMap();
               UnmodifiableIterator var46 = CustomEntityManager.getInstance().getAllEntities().iterator();

               while (var46.hasNext()) {
                  CustomEntityImpl var49 = (CustomEntityImpl)var46.next();
                  var17.merge(var49.getNamespacedId(), 1, Integer::sum);
                  var27.merge(var49.getNamespacedId(), var49.rQ.size(), Integer::sum);
                  if (!var37.containsKey(var49.getNamespacedId())) {
                     var37.put(var49.getNamespacedId(), var49.rQ.size());
                  }
               }

               var17.forEach(
                  (var3x, var4x) -> sender.sendMessage(
                     ChatColor.YELLOW + var3x + ChatColor.GRAY + " x" + var4x + " (x" + var37.get(var3x) + " bones | total x" + var27.get(var3x) + " bones)"
                  )
               );
               return true;
            }

            if (args[0].equals("inspect")) {
               if (args.length > 1) {
                  if (args[1].equals("next")) {
                     if (this.aHr.containsKey(sender)) {
                        this.aHr.merge(sender, 1, Integer::sum);
                     }
                  } else if (args[1].equals("prev") && this.aHr.containsKey(sender)) {
                     this.aHr.merge(sender, -1, (var0, var1x) -> Math.max(var0 + var1x, 0));
                  }
               } else {
                  this.aHq.put(sender, CustomEntityManager.getInstance().getAllEntities());
                  this.aHr.put(sender, 0);
               }

               byte var16 = 5;
               Integer var26 = (Integer)this.aHr.get(sender);
               ImmutableList var36 = (ImmutableList)this.aHq.get(sender);
               int var45 = var26 * var16;

               for (int var48 = var45; var48 < var48 + var16 && var48 < var36.size(); var48++) {
                  CustomEntityImpl var10 = (CustomEntityImpl)var36.get(var48);
                  sender.spigot()
                     .sendMessage(
                        AdventureText.toBungee(
                           AdventureText.fromLegacy(ChatColor.YELLOW + var10.getNamespacedId())
                              .hoverEvent(
                                 HoverEvent.showText(
                                    AdventureText.fromLegacy(
                                       var10.getNamespacedId()
                                          + "\nLoc: "
                                          + BlockFaceUtils.formatLocation(var10.getLocation(), true)
                                          + "\nAnim: "
                                          + var10.getModelInstance().bm("none")
                                          + "\n"
                                          + ChatColor.GRAY
                                          + "Click to teleport"
                                    )
                                 )
                              )
                              .clickEvent(
                                 ClickEvent.clickEvent(
                                    Action.RUN_COMMAND,
                                    "/minecraft:tp "
                                       + var10.getLocation().getBlockX()
                                       + " "
                                       + var10.getLocation().getBlockY()
                                       + " "
                                       + var10.getLocation().getBlockZ()
                                       + " "
                                 )
                              )
                        )
                     );
               }

               if (var45 + var16 < var36.size()) {
                  sender.spigot()
                     .sendMessage(
                        AdventureText.toBungee(
                           AdventureText.fromLegacy("Next page ->").clickEvent(ClickEvent.clickEvent(Action.RUN_COMMAND, "/iaentity inspect next"))
                        )
                     );
               } else if (var26 > 0) {
                  sender.spigot()
                     .sendMessage(
                        AdventureText.toBungee(
                           AdventureText.fromLegacy("<- Prev page").clickEvent(ClickEvent.clickEvent(Action.RUN_COMMAND, "/iaentity inspect prev"))
                        )
                     );
               }

               return true;
            }

            if (args[0].equals("anim")) {
               if (sender instanceof Player) {
                  Location var15 = ((Player)sender).getLocation();
                  switch (args[1]) {
                     case "play":
                        CustomEntityImpl var44 = CustomEntityManager.getInstance().findNearestEntity(var15);
                        if (var44 != null) {
                           String var9 = args[2];
                           if (var44.sZ) {
                              CustomEntityManager.method8320(var44, var9);
                           } else {
                              var44.playAnimation(var9);
                           }
                        } else {
                           Log.sendLocalized(sender, "no-near-entity-found");
                        }
                        break;
                     case "stop":
                     case "pause":
                        CustomEntityImpl var43 = CustomEntityManager.getInstance().findNearestEntity(var15);
                        if (var43 != null) {
                           var43.getModelInstance().qN();
                        } else {
                           Log.sendLocalized(sender, "no-near-entity-found");
                        }
                  }

                  return true;
               }

               Log.warnLocalized("command-ingame-only");
               return true;
            }

            if (args[0].equals("playerskin")) {
               if (sender instanceof Player) {
                  Location var14 = ((Player)sender).getLocation();
                  CustomEntityImpl var24 = CustomEntityManager.getInstance().findNearestEntity(var14);
                  if (var24 != null) {
                     var24.changePlayerSkin(args[1]);
                     Log.sendLocalized(sender, "skin-changed");
                  } else {
                     Log.sendLocalized(sender, "no-near-entity-found");
                  }

                  return true;
               }

               Log.warnLocalized("command-ingame-only");
               return true;
            }

            if (args[0].equals("disguise")) {
               if (args.length == 1) {
                  this.sendHelp(sender);
                  return true;
               }

               String var12 = args[1];
               if (!isKnownEntity(var12)) {
                  Log.sendLocalizedWith(sender, "unknown-entity", var1x -> var1x.replace("{entity}", var12));
                  return true;
               }

               Player var22 = null;
               if (args.length >= 3) {
                  var22 = Bukkit.getPlayer(args[2]);
                  if (var22 == null) {
                     Log.sendLocalizedWith(sender, "player-not-found", var1x -> var1x.replace("{player}", args[2]));
                     return true;
                  }
               } else if (sender instanceof Player) {
                  var22 = (Player)sender;
               }

               if (var22 == null) {
                  Log.sendPrefixed(sender, Main.aFi.lj("command-ingame-only"));
                  return true;
               }

               boolean var34 = CustomEntityManager.getInstance().startEmoteActor(var22, var12);
               if (!var34) {
                  Log.sendLocalizedWith(sender, "disguise-failed", var1x -> var1x.replace("{entity}", var12));
                  return true;
               }
            } else if (args[0].equals("undisguise")) {
               if (args.length == 1) {
                  this.sendHelp(sender);
                  return true;
               }

               Player var13 = Bukkit.getPlayer(args[1]);
               if (var13 == null) {
                  Log.sendLocalizedWith(sender, "player-not-found", var1x -> var1x.replace("{player}", args[1]));
                  return true;
               }

               boolean var23 = CustomEntityManager.getInstance().stopEmote(var13);
               if (!var23) {
                  Log.sendLocalizedWith(sender, "undisguise-failed-player-not-disguised", var1x -> var1x.replace("{player}", var13.getName()));
                  return true;
               }
            }
         }
      }

      this.sendHelp(sender);
      return true;
   }

   public static boolean isKnownEntity(String var0) {
      return CustomEntityManager.getInstance().listSettingsNames(false).contains(var0);
   }

   private void sendHelp(CommandSender var1) {
      var1.sendMessage(ChatColor.YELLOW + "Command usage:");
      var1.sendMessage(ChatColor.YELLOW + "   /iaentity disguise <name> [player] - Disguise as an entity");
      var1.sendMessage(ChatColor.YELLOW + "   /iaentity undisguise [player] - Undisguise yourself or another player");
      var1.sendMessage(ChatColor.YELLOW + "   /iaentity summon <name> - Spawns a new entity");
      var1.sendMessage(ChatColor.YELLOW + "   /iaentity summon <name> [world] [x] [y] [z] [yaw] [pitch] - Spawns a new entity at specific location");
      var1.sendMessage(ChatColor.YELLOW + "   /iaentity summonplayer <name> - Spawns a new player entity");
      var1.sendMessage(
         ChatColor.YELLOW + "   /iaentity summonplayer <name> [world] [x] [y] [z] [yaw] [pitch] - Spawns a new player entity at specific location"
      );
      var1.sendMessage(ChatColor.YELLOW + "   /iaentity playerskin <name|url|skin id> - Change skin of the nearest player entity");
      var1.sendMessage(ChatColor.YELLOW + "   /iaentity anim <play|pause> <animation> [repeatCount] - Change/pause nearest entity animation");
      var1.sendMessage(ChatColor.YELLOW + "   /iaentity kill <name|all> - Kills all entities or all entities of a type");
      var1.sendMessage(ChatColor.YELLOW + "   /iaentity count - Counts all entities");
      var1.sendMessage(ChatColor.YELLOW + "   /iaentity inspect [prev|next] - Get paged info about spawned custom entities");
      var1.sendMessage(ChatColor.YELLOW + "   /iaentity citizens <id|selected> model <model> - Change model of a Citizens NPC");
      var1.sendMessage(ChatColor.YELLOW + "   /iaentity citizens <id|selected> clean - Detach ItemsAdder from a Citizens NPC");
      var1.sendMessage(ChatColor.YELLOW + "   /iaentity citizens <id|selected> playerskin <name|url|skin id> - Change player skin of a Citizen NPC");
   }
}
