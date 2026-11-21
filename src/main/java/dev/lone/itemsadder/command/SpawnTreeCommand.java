package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.worldgen.TreeDecorator;
import java.util.WeakHashMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class SpawnTreeCommand extends IaCommandBase implements IaListener {
   WeakHashMap aHA = new WeakHashMap();

   public SpawnTreeCommand() {
      Main.getCommand("iaspawntree").setExecutor(this);
      EventRegistrar.registerIfAbsent(this, Main.getInstance());
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      } else if (!(commandSender instanceof Player var5)) {
         Log.warnLocalized("command-ingame-only");
         return true;
      } else {
         if (args.length == 0) {
            Log.sendPrefixed(commandSender, Main.aFi.lj("wrong-command-usage") + ChatColor.YELLOW + " /iaspawntree <namespace>. Example: /iaspawntree example");
            return true;
         }

         if (TreeDecorator.aDN.containsKey(args[0])) {
            this.aHA.put(var5, args[0]);
            Log.sendLocalized(var5, "rightclick-a-block");
         } else {
            Log.sendPrefixed(var5, Main.aFi.lj("tree-not-found").replace("{item}", args[0]));
         }

         return true;
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   void method630(PlayerInteractEvent var1) {
      if (var1.getAction() == Action.RIGHT_CLICK_BLOCK) {
         TreeDecorator var2 = (TreeDecorator)TreeDecorator.aDN.get(this.aHA.get(var1.getPlayer()));
         if (var2 != null) {
            Block var3 = var1.getClickedBlock().getRelative(BlockFace.UP);
            if (BlockFaceUtils.simulatePlace(var3, var1.getPlayer())) {
               if (!var3.getType().isOccluding()) {
                  var3.setType(Material.AIR, false);
               }

               if (var2.method2321(var3, false)) {
                  Log.sendLocalized(var1.getPlayer(), "spawned-tree");
               } else {
                  Log.sendLocalized(var1.getPlayer(), "spawned-tree-error");
               }

               this.aHA.remove(var1.getPlayer());
            }
         }
      }
   }
}
