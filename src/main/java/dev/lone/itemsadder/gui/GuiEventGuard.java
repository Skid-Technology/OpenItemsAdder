package dev.lone.itemsadder.gui;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.Plugin;

public final class GuiEventGuard {
   private static final AtomicBoolean bQf = new AtomicBoolean(false);

   private GuiEventGuard() {
      throw new UnsupportedOperationException();
   }

   public static void register(Plugin plugin) {
      Objects.requireNonNull(plugin, "plugin");
      if (bQf.getAndSet(true)) {
         throw new IllegalStateException("FastInv is already registered");
      }

      Bukkit.getPluginManager().registerEvents(new CustomInventoryListener(plugin), plugin);
   }

   public static void LF() {
      Bukkit.getOnlinePlayers()
         .stream()
         .filter(var0 -> var0.getOpenInventory().getTopInventory().getHolder() instanceof CustomInventoryHolder)
         .forEach(HumanEntity::closeInventory);
   }
}
