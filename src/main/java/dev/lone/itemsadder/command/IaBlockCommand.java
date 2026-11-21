package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.item.PlacedBlockItemDefinition;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import dev.lone.itemsadder.world.InstrumentRegistry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class IaBlockCommand extends IaCommandBase {
   public IaBlockCommand() {
      Main.getCommand("iablock").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      } else if (!(commandSender instanceof Player var5)) {
         Log.warnLocalized("command-ingame-only");
         return true;
      } else {
         if (!PermissionNodes.method1632(var5, "ia.admin.iablock")) {
            return true;
         }

         Block var6 = PlayerInteractionFacade.cM(var5);
         if (var6 != null && var6.getType() != Material.AIR) {
            CustomBlockChunkStoreBase var7 = ContentManager.method8084().method8183(var6);
            if (!var7.isCustomBlock(var6)) {
               if (var7.hasBlockEntryAt(var6)) {
                  String var8 = var7.getCropSeedId(var6);
                  if (var8 != null) {
                     var5.sendMessage(ChatColor.AQUA + "Crop Seed: " + var8);
                  }

                  Integer var9 = var7.getStoredInt(var6);
                  if (var9 != null) {
                     var5.sendMessage(ChatColor.AQUA + "Fire Age: " + var9);
                  }
               } else {
                  var5.sendMessage("" + ChatColor.AQUA + var6.getType());
               }
            } else {
               String var11 = var7.getCustomBlockId(var6);
               PlacedBlockItemDefinition var12 = (PlacedBlockItemDefinition)ContentManager.method8084().method8154(var11);
               if (var12 == null) {
                  if (ContentManager.method8084().ay.containsKey(var11)) {
                     var5.sendMessage(ChatColor.AQUA + "Type: " + var11 + ChatColor.DARK_GRAY + " (liquid)");
                  } else {
                     var5.sendMessage(ChatColor.AQUA + "Type: " + var11 + ChatColor.DARK_GRAY + " (removed from IA configs)");
                  }
               } else {
                  var5.sendMessage(ChatColor.AQUA + "Type: " + var12.getNamespacedID());
                  var5.sendMessage(ChatColor.AQUA + "Chunk: " + var6.getChunk().getX() + ", " + var6.getChunk().getZ());
                  if (var6.getType() == Material.FIRE && var7.hasBlockEntryAt(var6)) {
                     Integer var10 = var7.getStoredInt(var6);
                     if (var10 != null) {
                        var5.sendMessage(ChatColor.AQUA + "Fire age: " + var10);
                     }
                  }
               }
            }

            if (InstrumentRegistry.aR(var6)) {
               var5.sendMessage("(Is a tile entity)");
            }

            return true;
         } else {
            return true;
         }
      }
   }
}
