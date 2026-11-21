package dev.lone.itemsadder.api.Skript;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import dev.lone.itemsadder.util.Log;
import java.io.IOException;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class SkriptManager {
   private static SkriptAddon addon;

   public void init(JavaPlugin plugin) {
      try {
         addon = Skript.registerAddon(plugin);

         try {
            addon.loadClasses("dev.lone.itemsadder.api.Skript.implementation", new String[]{"expressions"});
         } catch (IOException var3) {
            var3.printStackTrace();
         }

         Log.log(ChatColor.AQUA + "Registered Skript API");
      } catch (Exception var4) {
         Log.log(ChatColor.YELLOW + var4.getMessage());
      }
   }

   public SkriptAddon getAddonInstance() {
      return addon;
   }
}
