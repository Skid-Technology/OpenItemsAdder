package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.item.ItemMaterialAction;
import dev.lone.itemsadder.nms.NmsWorldService;
import dev.lone.itemsadder.util.DebugMarkerSender;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import java.awt.Color;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LiquidCommand extends IaCommandBase {
   public LiquidCommand() {
      Main.getCommand("ialiquid").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      } else if (!(commandSender instanceof Player var5)) {
         Log.warnLocalized("command-ingame-only");
         return true;
      } else {
         if (!PermissionNodes.method1632(var5, "ia.admin.ialiquid")) {
            return true;
         }

         Block var6 = null;
         if (args.length == 0) {
            var6 = PlayerInteractionFacade.cL(var5);
         } else if (args.length == 1) {
            if (var5.isOp()) {
               if (args[0].equals("debug")) {
                  byte var7 = 5;
                  Location var8 = var5.getLocation();

                  for (int var9 = var8.getBlockX() - var7; var9 < var8.getBlockX() + var7; var9++) {
                     for (int var10 = var8.getBlockY() - var7; var10 < var8.getBlockY() + var7; var10++) {
                        for (int var11 = var8.getBlockZ() - var7; var11 < var8.getBlockZ() + var7; var11++) {
                           Location var12 = new Location(var8.getWorld(), var9, var10, var11);
                           String var13 = NmsWorldService.Om().bj(var12.getBlock());
                           if (var13.startsWith("ia")) {
                              DebugMarkerSender.sendMarkerWithColor(
                                 var5, var12, new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), 127).getRGB(), 3000, var13
                              );
                           }
                        }
                     }
                  }
               } else if (args[0].equals("clean")) {
                  byte var17 = 5;
                  Location var19 = var5.getLocation();

                  for (int var21 = var19.getBlockX() - var17; var21 < var19.getBlockX() + var17; var21++) {
                     for (int var22 = var19.getBlockY() - var17; var22 < var19.getBlockY() + var17; var22++) {
                        for (int var23 = var19.getBlockZ() - var17; var23 < var19.getBlockZ() + var17; var23++) {
                           Location var24 = new Location(var19.getWorld(), var21, var22, var23);
                           String var25 = NmsWorldService.Om().bj(var24.getBlock());
                           if (var25.startsWith("ia")) {
                              Block var14 = var24.getBlock();
                              CustomBlockChunkStoreBase var15 = ContentManager.method8084().method8182(var14.getChunk());
                              if (var15.hasBlockEntryAt(var14)) {
                                 String[] var16 = var15.getSecondaryBlockId(var24.getBlock()).split(":");
                                 NmsWorldService.Om().method3194(var24.getBlock(), var16[0], var16[1]);
                                 var15.removeCustomBlockAndRefresh(var6, true);
                              } else {
                                 NmsWorldService.Om().method3194(var24.getBlock(), "minecraft", "plains");
                              }

                              commandSender.sendMessage("Reset biome around player");
                           }
                        }
                     }
                  }
               }
            }
         } else if (args.length == 3 && Utils.isNumeric(args[0]) && Utils.isNumeric(args[1]) && Utils.isNumeric(args[2])) {
            var6 = var5.getWorld().getBlockAt(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
         } else {
            Log.sendPrefixed(commandSender, Main.aFi.lj("wrong-command-usage"));
            Log.sendPrefixed(commandSender, Utils.toSectionCodes("&e/ialiquid &b[x] [y] [z]"));
            Log.sendPrefixed(commandSender, Utils.toSectionCodes("&6Example: &e/ialiquid"));
         }

         if (var6 == null) {
            return true;
         }

         if (ItemMaterialAction.method381(var6.getType())) {
            CustomBlockChunkStoreBase var18 = ContentManager.method8084().method8182(var6.getChunk());
            if (!var18.hasBlockEntryAt(var6)) {
               var5.sendMessage("" + ChatColor.AQUA + var6.getType());
            } else {
               String var20 = var18.getCustomBlockIdIfFlagged(var6);
               var5.sendMessage(ChatColor.AQUA + "Liquid: " + var20);
               if (var20 != null) {
                  var5.sendMessage(ChatColor.AQUA + "Bucket: " + (String)ContentManager.method8084().by.get(var20));
               }
            }
         } else {
            var5.sendMessage("" + ChatColor.AQUA + var6.getType());
         }

         var5.sendMessage(ChatColor.AQUA + "Block Biome: " + NmsWorldService.Om().bj(var6));
         return true;
      }
   }
}
