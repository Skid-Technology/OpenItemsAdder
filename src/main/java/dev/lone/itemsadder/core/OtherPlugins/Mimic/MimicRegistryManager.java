package dev.lone.itemsadder.Core.OtherPlugins.Mimic;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import ru.endlesscode.mimic.Mimic;
import ru.endlesscode.mimic.MimicApiLevel;
import ru.endlesscode.mimic.items.BukkitItemsRegistry;

public class MimicRegistryManager {
   public static void register(Plugin plugin) {
      if (MimicApiLevel.checkApiLevel(3)) {
         Mimic var1 = Mimic.getInstance();
         var1.registerItemsRegistry(new ItemsAdderItemsRegistryCreator(), 3, plugin);
      } else {
         Bukkit.getServicesManager().register(BukkitItemsRegistry.class, new ItemsAdderItemsRegistryCreator(), plugin, ServicePriority.Normal);
      }
   }
}
