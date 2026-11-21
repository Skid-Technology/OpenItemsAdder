package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.fontimage.EmojiReplacer;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.text.ColorTagParser;
import dev.lone.itemsadder.util.Log;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RenameCommand extends IaCommandBase {
   public RenameCommand() {
      Main.getCommand("iarename").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      } else if (!(commandSender instanceof Player var5)) {
         Log.warnLocalized("command-ingame-only");
         return true;
      } else {
         ItemStack var6 = var5.getInventory().getItemInMainHand();
         if (var6.getType() == Material.AIR) {
            Log.sendPrefixed(commandSender, Main.aFi.lj("cannot-repair"));
            return true;
         } else {
            TextComponent var7 = Component.text(String.join(" ", args));
            Component var8 = ColorTagParser.method2358(var7);
            Component var9 = EmojiReplacer.method2333(var8);
            Component var10 = var9.style(var0 -> var0.decoration(TextDecoration.ITALIC, false));
            ItemStackUtils.method7223(var6, var10);
            Log.sendPrefixed(commandSender, Main.aFi.lj("renamed-successfully"));
            return true;
         }
      }
   }
}
