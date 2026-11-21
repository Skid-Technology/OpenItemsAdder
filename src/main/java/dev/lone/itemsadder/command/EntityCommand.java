package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.block.BlockSoundProfile;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.EntityEventTracker;
import dev.lone.itemsadder.entity.MobDisguiseSpec;
import dev.lone.itemsadder.entity.ModelPhysicsLoop;
import dev.lone.itemsadder.entity.ScenarioMessage;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import dev.lone.itemsadder.world.ChunkBlockHookHolder;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityCommand extends IaCommandBase {
   static final int aHi = 10;
   static final List aHj = List.of(
      "enableDebugNametag", "disableDebugNametag", "summonNoAi", "changeAnimation", "enableRagdoll", "armRagdoll", "ragdollFriction"
   );
   static final List aHk = List.of("0", "0.1", "0.2", "0.4", "0.6", "0.8", "1", "1.5", "2");
   private final Map aHl = new HashMap();

   public EntityCommand() {
      Main.getCommand("iadebug").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      }

      if (!IaCommandBase.method1428(1, args)) {
         Log.sendPrefixed(commandSender, Main.aFi.lj("wrong-command-usage"));
         Log.sendPrefixed(commandSender, Utils.toSectionCodes("&e/iadebug <action> [arguments]"));
         Log.sendPrefixed(commandSender, Utils.toSectionCodes("&6Example: &e/iadebug region"));
         return true;
      }

      if (args[0].equals("region")) {
         CustomBlockChunkStoreBase var5 = null;
         if (commandSender instanceof Player var6 && args.length == 1) {
            var5 = ContentManager.method8084().method8184(var6.getLocation());
         }

         if (var5 == null) {
            if (!IaCommandBase.method1428(4, args)) {
               Log.sendPrefixed(commandSender, Main.aFi.lj("wrong-command-usage"));
               Log.sendPrefixed(commandSender, Utils.toSectionCodes("&e/iadebug <action> [arguments]"));
               Log.sendPrefixed(commandSender, Utils.toSectionCodes("&6Example: &e/iadebug region"));
               return true;
            }

            try {
               var5 = ContentManager.method8084().method8185(Bukkit.getWorld(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            } catch (Throwable var18) {
               Log.sendPrefixed(commandSender, "Command error. Make sure you used it correctly.");
               return true;
            }
         }

         Log.sendPrefixed(commandSender, "Region: " + var5.getFileName());
         Log.sendPrefixed(commandSender, "Loaded Chunks: " + var5.getLoadedChunkCount());
         Log.sendPrefixed(commandSender, "Size: " + var5.getFileSizeText());
      } else if (args[0].equals("placeCrops")) {
         BlockSoundProfile var19 = (BlockSoundProfile)ContentManager.method8084().au.get(args[1]);
         if (var19 == null) {
            Log.sendPrefixed(commandSender, "Crop not found: " + args[1]);
            return true;
         }

         boolean var25 = IaCommandBase.method1427(args, 4, true);
         Player var7 = (Player)commandSender;
         Location var8 = var7.getLocation().add(0.0, 1.0, 0.0);
         World var9 = var8.getWorld();
         int var10 = IaCommandBase.method1425(args, 2, 10);
         int var11 = IaCommandBase.method1425(args, 3, -1);

         for (int var12 = -var10; var12 <= var10; var12++) {
            for (int var13 = -var10; var13 <= var10; var13++) {
               Location var14 = var8.clone().add(var12, 0.0, var13);
               var14.setY(var9.getHighestBlockYAt(var14) + 1);
               if ((var14.getBlock().getType().isAir() || var14.getBlock().isReplaceable())
                  && (var25 || !ContentManager.method8084().method8142().isCropAreaFull(var14))) {
                  Location var15 = var14.clone().add(0.0, -1.0, 0.0);
                  if (var15.getBlock().getType().isSolid() && var15.getBlock().getType().isOccluding()) {
                     if (var19.jZ && var15.getBlock().getType() != Material.FARMLAND) {
                        var15.getBlock().setType(Material.FARMLAND);
                     }

                     int var16 = var11;
                     if (var16 == -1) {
                        var16 = Utils.randomInt(0, var19.jW);
                     }

                     ContentManager.method8084().method8142().mZ.setBlockStateInt(var14.getBlock(), var19.jP, var16);
                     var14.getBlock().setType(Material.AIR);
                  }
               }
            }
         }
      } else if (args[0].equals("cleanCrops")) {
         AtomicInteger var20 = new AtomicInteger();
         ContentManager.method8084().method8142().mZ.forEachChunkTable((var1x, var2x) -> {
            if (!ContentManager.method8084().method8142().mZ.getChunkTable(var1x).snapshot().entries().isEmpty()) {
               var20.getAndIncrement();
               ContentManager.method8084().method8142().mZ.unloadChunkTable(var1x);
            }
         });
         Log.sendPrefixed(commandSender, "Cleaned crops data in " + var20.get() + " loaded chunks.");
      } else if (args[0].equals("stopEmote")) {
         CustomEntityManager.getInstance().method8327((Player)commandSender);
      } else if (args[0].equals("tnt")) {
         String var21 = IaCommandBase.method1424(args, 1, null);
         if (var21 == null) {
            Log.sendPrefixed(commandSender, "You must provide a namespaced ID of a custom block.");
            return true;
         }

         CustomBlockDefinition var26 = ContentManager.method8084().method8160(var21);
         if (var26 == null) {
            Log.sendPrefixed(commandSender, "Custom block not found: " + var21);
            return true;
         }

         BlockData var29 = var26.anF.getBlockData();
         ChunkBlockHookHolder.getInstance().hook.spawnPrimedTnt(((Player)commandSender).getLocation(), 5, var29, (Player)commandSender, null);
      } else if (args[0].equalsIgnoreCase("starve")) {
         Player var22 = (Player)commandSender;
         var22.setFoodLevel(0);
         var22.setSaturation(0.0F);
      } else if (args[0].equalsIgnoreCase("get-item-with-custom-model-data")) {
         Player var23 = (Player)commandSender;
         Material var27 = BukkitRegistryLookup.material(IaCommandBase.method1424(args, 1, null));
         if (var27 == null) {
            Log.sendPrefixed(commandSender, "Material not found");
            return true;
         }

         ItemStack var30 = new ItemStack(var27);
         ItemStackUtils.method7259(var30, IaCommandBase.method1425(args, 2, 0));
         var23.getInventory().addItem(new ItemStack[]{var30});
         Log.sendPrefixed(commandSender, "Item has been added to the inventory.");
      } else if (args[0].equalsIgnoreCase("messages")) {
         if (!(commandSender instanceof Player var24)) {
            Log.sendLocalized(commandSender, "command-ingame-only");
            return true;
         }

         int var28 = 0;
         if (args.length > 1) {
            try {
               var28 = Integer.parseInt(args[1]);
            } catch (NumberFormatException var17) {
               return true;
            }

            if (!Integer.valueOf(var28).equals(this.aHl.get(var24.getUniqueId()))) {
               return true;
            }
         }

         this.method1564(var24, var28);
      } else if (args[0].equalsIgnoreCase("customEntities")) {
         this.method1563(commandSender, args);
      } else if (args[0].equalsIgnoreCase("entities")) {
         this.method1559(commandSender, args);
      }

      return true;
   }

   private void method1559(CommandSender var1, String[] var2) {
      if (var1 instanceof Player var3) {
         if (var2.length > 1 && var2[1].equalsIgnoreCase("equipment")) {
            method1561(var3, var2);
         } else if (var2.length >= 3 && var2[1].equalsIgnoreCase("compare")) {
            EntityEventTracker var4 = EntityEventTracker.getInstance();
            switch (var2[2].toLowerCase(Locale.ROOT)) {
               case "start":
                  MobDisguiseSpec var7 = method1562(var2);
                  if (var7 == null) {
                     method1560(var1);
                     return;
                  }

                  ScenarioMessage var8 = var4.createMessage(var3, var7.type(), var7.skin(), var7.name(), var7.Lp());
                  Log.sendPrefixed(var1, var8.message());
                  if (var8.isPublic()) {
                     Log.sendPrefixed(var1, "Use /iadebug entities compare status " + var8.playerId());
                  }
                  break;
               case "status":
               case "report":
                  var4.method1839(var3.getUniqueId(), var2.length > 3 ? var2[3] : null).forEach(var1x -> Log.sendPrefixed(var1, var1x));
                  break;
               case "stop":
                  Log.sendPrefixed(
                     var1,
                     var4.hasScenario(var3.getUniqueId(), var2.length > 3 ? var2[3] : null)
                        ? "Comparison session stopped and cleaned up."
                        : "No matching comparison session."
                  );
                  break;
               default:
                  method1560(var1);
            }
         } else {
            method1560(var1);
         }
      } else {
         Log.sendLocalized(var1, "command-ingame-only");
      }
   }

   private static void method1560(CommandSender var0) {
      Log.sendPrefixed(var0, "/iadebug entities compare start <model> <zombie|pig> [combat|navigation] [--capture]");
      Log.sendPrefixed(var0, "/iadebug entities compare <status|report|stop> [session]");
      Log.sendPrefixed(var0, "/iadebug entities equipment <mainhand|offhand|head|chest|legs|feet>");
   }

   private static void method1561(Player var0, String[] var1) {
      EquipmentSlot var2 = var1.length == 3 ? eD(var1[2]) : null;
      if (var2 == null) {
         Log.sendPrefixed(var0, "/iadebug entities equipment <mainhand|offhand|head|chest|legs|feet>");
      } else {
         CustomEntityImpl var3 = CustomEntityManager.getInstance().getTargetEntity(var0);
         LivingEntity var4 = var3 == null ? null : var3.getBukkitEntity();
         EntityEquipment var5 = var4 != null && var4.isValid() && !var4.isDead() ? var4.getEquipment() : null;
         if (var5 == null) {
            Log.sendPrefixed(var0, "Look at a living custom entity within 5 blocks.");
         } else if (!var3.hasEquipmentSlot(var2)) {
            Log.sendPrefixed(var0, "This model has no visible equipment pivot for " + var2 + ".");
         } else {
            ItemStack var6 = var0.getInventory().getItemInMainHand();
            var5.setItem(var2, var6.clone());
            Log.sendPrefixed(var0, "Set " + var2 + " on the custom entity to " + var6.getType() + ".");
         }
      }
   }

   @Nullable
   static EquipmentSlot eD(String var0) {
      return switch (var0.toLowerCase(Locale.ROOT)) {
         case "mainhand", "hand" -> EquipmentSlot.HAND;
         case "offhand", "off_hand" -> EquipmentSlot.OFF_HAND;
         case "head" -> EquipmentSlot.HEAD;
         case "chest" -> EquipmentSlot.CHEST;
         case "legs" -> EquipmentSlot.LEGS;
         case "feet" -> EquipmentSlot.FEET;
         default -> null;
      };
   }

   @Nullable
   static MobDisguiseSpec method1562(String[] var0) {
      if (var0.length < 5) {
         return null;
      }

      String var1 = "combat";
      boolean var2 = false;
      boolean var3 = false;

      for (int var4 = 5; var4 < var0.length; var4++) {
         if ("--capture".equalsIgnoreCase(var0[var4]) && !var3) {
            var3 = true;
         } else {
            if (var0[var4].startsWith("--") || var2) {
               return null;
            }

            var1 = var0[var4];
            var2 = true;
         }
      }

      return new MobDisguiseSpec(var0[3], var0[4], var1, var3);
   }

   private void method1563(CommandSender var1, String[] var2) {
      if (var2.length < 2) {
         Log.sendPrefixed(var1, Utils.toSectionCodes("&e/iadebug customEntities <" + String.join("|", aHj) + ">"));
      } else {
         CustomEntityManager var3 = CustomEntityManager.getInstance();
         if (var2[1].equalsIgnoreCase("enableDebugNametag")) {
            var3.setNametagsEnabled(true);
            Log.sendPrefixed(var1, "Custom entity debug nametags enabled.");
         } else if (var2[1].equalsIgnoreCase("disableDebugNametag")) {
            var3.setNametagsEnabled(false);
            Log.sendPrefixed(var1, "Custom entity debug nametags disabled.");
         } else if (var2[1].equalsIgnoreCase("ragdollFriction")) {
            if (var2.length == 2) {
               Log.sendPrefixed(var1, "Global ragdoll ground friction: " + ModelPhysicsLoop.getGroundFriction());
            } else if (var2.length != 3) {
               Log.sendPrefixed(var1, "/iadebug customEntities ragdollFriction <0..2>");
            } else {
               try {
                  ModelPhysicsLoop.setGroundFriction(Double.parseDouble(var2[2]));
               } catch (IllegalArgumentException var9) {
                  Log.sendPrefixed(var1, "Ragdoll ground friction must be a finite number from 0 to 2.");
                  return;
               }

               Log.sendPrefixed(var1, "Global ragdoll ground friction set to " + ModelPhysicsLoop.getGroundFriction());
            }
         } else if (var1 instanceof Player var4) {
            if (!Settings.get().customEntitiesEnabled) {
               Log.sendLocalized(var1, "custom-entities-disabled");
            } else if (var2[1].equalsIgnoreCase("summonNoAi")) {
               if (var2.length == 3 && var3.getSettings(var2[2]) != null) {
                  Location var11 = var4.getLocation().clone();
                  double var12 = Math.toRadians(var11.getYaw());
                  var11.add(-Math.sin(var12) * 2.0, 0.0, Math.cos(var12) * 2.0);
                  var11.setPitch(0.0F);
                  CustomEntityImpl var13 = var3.spawnCustomEntitySimple(var2[2], var11, true);
                  if (var13 == null) {
                     Log.sendPrefixed(var1, "Failed to summon custom entity: " + var2[2]);
                  } else {
                     if (var13.getBukkitEntity() != null) {
                        var13.getBukkitEntity().setAI(false);
                     }

                     var13.method8449(true);
                     Log.sendPrefixed(var1, "Summoned no-AI custom entity: " + var2[2]);
                  }
               } else {
                  Log.sendPrefixed(var1, Utils.toSectionCodes("&e/iadebug customEntities summonNoAi <namespace:entity>"));
               }
            } else if (var2[1].equalsIgnoreCase("changeAnimation")) {
               CustomEntityImpl var10 = var3.findNearestEntity(var4.getLocation());
               if (var2.length == 3 && var10 != null && var10.getModelDefinition(var2[2]) != null) {
                  var10.method8449(true);
                  var10.playAnimationInternal(var2[2], 0, false, null);
                  Log.sendPrefixed(var1, "Nearest custom entity animation changed to: " + var2[2]);
               } else {
                  Log.sendPrefixed(
                     var1,
                     var10 == null ? "No custom entity found nearby." : "Unknown animation for nearest custom entity: " + (var2.length > 2 ? var2[2] : "")
                  );
               }
            } else {
               boolean var5 = var2[1].equalsIgnoreCase("armRagdoll");
               if (!var2[1].equalsIgnoreCase("enableRagdoll") && !var5) {
                  Log.sendPrefixed(var1, "Unknown customEntities action: " + var2[1]);
               } else if (var2.length != 2) {
                  Log.sendPrefixed(var1, Utils.toSectionCodes("&e/iadebug customEntities " + var2[1]));
               } else {
                  CustomEntityImpl var6 = var3.findNearestEntity(var4.getLocation());
                  if (var6 == null) {
                     Log.sendPrefixed(var1, "No custom entity found nearby.");
                  } else {
                     LivingEntity var7 = var6.getBukkitEntity();
                     if (var5 && var7 != null && var7.getLocation().distanceSquared(var4.getLocation()) > 64.0) {
                        Log.sendPrefixed(var1, "No custom entity found within 8 blocks.");
                     } else if (var7 != null && !var7.isDead() && !(var7.getHealth() <= 0.0) && var6.markRagdollPending()) {
                        String var8 = var6.getModel().getNamespacedID();
                        if (var5) {
                           Log.sendPrefixed(var1, "Ragdoll armed for nearest custom entity: " + var8 + ". Kill it normally to test.");
                        } else {
                           var7.setHealth(0.0);
                           Log.sendPrefixed(var1, "Triggered ragdoll death for nearest custom entity: " + var8);
                        }
                     } else {
                        Log.sendPrefixed(var1, "Nearest custom entity is not a living V2 model that supports ragdoll.");
                     }
                  }
               }
            }
         } else {
            Log.sendLocalized(var1, "command-ingame-only");
         }
      }
   }

   private void method1564(Player var1, int var2) {
      ArrayList var3 = new ArrayList(Main.aFi.abO());
      Collections.sort(var3);
      List var4 = method1565(var3, var2);
      if (!var4.isEmpty()) {
         int var5 = var2 * 10 + 1;
         int var6 = var5 + var4.size() - 1;
         Log.sendComponent(var1, Component.text("Language messages " + var5 + "–" + var6 + " / " + var3.size(), NamedTextColor.DARK_GRAY));

         for (String var8 : var4) {
            Component var9 = ((TextComponent)Component.text(var8, NamedTextColor.AQUA).append(Component.text(" » ", NamedTextColor.DARK_GRAY)))
               .append(Main.aFi.method7070(var8));
            Log.sendComponent(var1, var9);
         }

         int var10 = var2 + 1;
         if (var10 * 10 < var3.size()) {
            this.aHl.put(var1.getUniqueId(), var10);
            Component var11 = ((TextComponent)((TextComponent)Component.text("[ Show next 10 » ]", NamedTextColor.AQUA).decorate(TextDecoration.BOLD))
                  .clickEvent(ClickEvent.runCommand("/iadebug messages " + var10)))
               .hoverEvent(Component.text("Print the next 10 language messages", NamedTextColor.GRAY));
            Log.sendComponent(var1, var11);
         } else {
            this.aHl.remove(var1.getUniqueId());
            Log.sendComponent(var1, Component.text("End of language messages.", NamedTextColor.DARK_GRAY));
         }
      }
   }

   static List method1565(List var0, int var1) {
      if (var1 >= 0 && var1 <= var0.size() / 10) {
         int var2 = var1 * 10;
         return var2 == var0.size() ? List.of() : List.copyOf(var0.subList(var2, Math.min(var2 + 10, var0.size())));
      } else {
         return List.of();
      }
   }
}
