package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.block.CustomBlockStateSettings;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.gui.GuiPage;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.recipe.RecipeLoader;
import dev.lone.itemsadder.resourcepack.PackDeliveryBase;
import dev.lone.itemsadder.util.Log;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InfoCommand extends IaCommandBase {
   public InfoCommand() {
      Main.getCommand("iainfo").setExecutor(this);
   }

   @Override
   public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
      if (!this.method1423(commandSender, command)) {
         return true;
      }

      Log.sendPlain(
         commandSender,
         ChatColor.YELLOW
            + "-"
            + ChatColor.GOLD
            + "="
            + ChatColor.DARK_GRAY
            + "["
            + ChatColor.GREEN
            + ChatColor.BOLD
            + " ItemsAdder "
            + ChatColor.DARK_GRAY
            + "]"
            + ChatColor.GOLD
            + "="
            + ChatColor.YELLOW
            + "-"
      );
      Log.sendPlain(commandSender, ChatColor.GOLD + "Version: " + ChatColor.AQUA + Main.getInstance().getDescription().getVersion());
      PackDeliveryBase.printStatus(commandSender);
      Log.sendPlain(commandSender, ChatColor.GRAY + "     Last update: " + ChatColor.AQUA + ContentManager.method8084().aL.generator.getPackFileDateText());
      Log.sendPlain(commandSender, ChatColor.GRAY + "     Host type: " + ChatColor.AQUA + ContentManager.method8084().aL.delivery.getName());
      int var5 = CustomBlockStateSettings.getInstance().getRealCount();
      int var6 = CustomBlockStateSettings.getInstance().getNoteCount();
      int var7 = CustomBlockStateSettings.getInstance().getChorusCount();
      int var8 = CustomBlockStateSettings.getInstance().getWireCount();
      int var9 = CustomBlockStateSettings.getInstance().getFireCount();
      int var10 = ContentManager.method8084().aE.wU();
      String var11 = "Loading ...";
      if (ContentManager.method8084().bB != null && ContentManager.method8084().bB.aJX != null) {
         HashSet var12 = new HashSet();

         for (GuiPage var14 : ContentManager.method8084().bB.aJX.LO()) {
            if (!var14.LY()) {
               for (RecipeLoader var16 : var14.Ma()) {
                  var12.add(var16.Mc().getNamespacedID());
               }
            }
         }

         var11 = var12.size() + "";
      }

      Log.sendPlain(commandSender, ChatColor.GOLD + "Data");
      Log.sendPlain(commandSender, ChatColor.GRAY + " - Items: " + ChatColor.AQUA + ContentManager.method8084().field92.size());
      Log.sendPlain(commandSender, ChatColor.GRAY + " - Items in /ia: " + ChatColor.AQUA + var11);
      Log.sendPlain(commandSender, ChatColor.GRAY + " - Namespaces: " + ChatColor.AQUA + ContentManager.method8084().as.size());
      Log.sendPlain(commandSender, ChatColor.GRAY + " - Materials: " + ChatColor.AQUA + ContentManager.method8084().ag.size());
      if (Settings.bK()) {
         int var21 = 0;

         for (CustomBlockDefinition var25 : ContentManager.method8084().al) {
            if (var25.anF != null && var25.anF.Fx()) {
               var21++;
            }
         }

         if (Settings.get().customBlocksMushroom) {
            Log.sendPlain(commandSender, ChatColor.GRAY + " - REAL: " + ChatColor.AQUA + var5 + ChatColor.GRAY + "/" + ChatColor.AQUA + "188");
         }

         if (Settings.get().customBlocksNote) {
            Log.sendPlain(commandSender, ChatColor.GRAY + " - REAL_NOTE: " + ChatColor.AQUA + var6 + ChatColor.GRAY + "/" + ChatColor.AQUA + "750");
         }

         if (Settings.get().customBlocksChorus) {
            Log.sendPlain(commandSender, ChatColor.GRAY + " - REAL_TRANSPARENT: " + ChatColor.AQUA + var7 + ChatColor.GRAY + "/" + ChatColor.AQUA + "63");
         }

         if (Settings.get().customBlocksWire) {
            Log.sendPlain(commandSender, ChatColor.GRAY + " - REAL_WIRE: " + ChatColor.AQUA + var8 + ChatColor.GRAY + "/" + ChatColor.AQUA + "127");
         }

         if (Settings.get().customBlocksFire) {
            Log.sendPlain(commandSender, ChatColor.GRAY + " - FIRE: " + ChatColor.AQUA + var9 + ChatColor.GRAY + "/" + ChatColor.AQUA + "14");
         }

         if (var21 > 0) {
            Log.sendPlain(commandSender, ChatColor.GRAY + " - Directional states: " + ChatColor.AQUA + var21);
         }
      }

      Log.sendPlain(
         commandSender, ChatColor.GRAY + " Font images: " + ChatColor.AQUA + var10 + ChatColor.GRAY + "/" + ChatColor.AQUA + ContentManager.method8084().aE.Ym
      );
      int var22 = 0;
      byte var24 = 0;
      int var26 = 0;

      for (World var28 : Bukkit.getWorlds()) {
         var26 += var28.getLoadedChunks().length;

         for (LivingEntity var18 : var28.getLivingEntities()) {
            if (var18 instanceof ArmorStand var19) {
               ItemStack var20 = var19.getHelmet();
               if (var20 != null && CustomItemDefinition.isCustomItem(var20)) {
                  var22++;
               }
            }
         }
      }

      Log.sendPlain(commandSender, ChatColor.GOLD + "Loaded chunks " + ChatColor.GRAY + "(" + var26 + ")");
      Log.sendPlain(commandSender, ChatColor.GRAY + "     Armorstands: " + ChatColor.AQUA + var22);
      Log.sendPlain(commandSender, ChatColor.GRAY + "     Mobs: " + ChatColor.AQUA + var24);
      return true;
   }
}
