package dev.lone.itemsadder.core;

import beer.devs.fastnbt.nms.nbt.NFile;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class PlayerValueProviderImpl extends AbstractPlayerValueProvider implements IaListener {
   protected final Map dI = Collections.synchronizedMap(new HashMap());
   long dJ = 0L;

   public PlayerValueProviderImpl(Plugin var1, ContentManager var2) {
      super(var1, var2);
      EventRegistrar.registerIfAbsent(this, var1);
      Bukkit.getOnlinePlayers().forEach(this::method701);
   }

   @Override
   public void method701(Player var1) {
      UUID var2 = var1.getUniqueId();
      synchronized (this.dI) {
         if (!this.dI.containsKey(var2)) {
            File var4 = new File(ContentManager.field70 + "players/stats/" + var2 + ".nbt");

            try {
               this.dI.put(var2, new NbtFileStore(new NFile(var4)));
            } catch (Exception var7) {
               this.dF
                  .getLogger()
                  .log(
                     Level.SEVERE,
                     ChatColor.RED
                        + "Error loading player stats file '"
                        + var1.getName()
                        + "' "
                        + ChatColor.AQUA
                        + var2
                        + ChatColor.RED
                        + ": "
                        + ChatColor.YELLOW
                        + var7.getMessage()
                  );
               var7.printStackTrace();
            }
         }
      }
   }

   @Override
   public boolean method702(Player var1) {
      return this.dI.containsKey(var1.getUniqueId());
   }

   @Override
   public boolean method703(Player var1, String var2) {
      NbtFileStore var3 = (NbtFileStore)this.dI.get(var1.getUniqueId());
      return var3 != null && var3.method2388(var2);
   }

   @Override
   public void method704(Player var1, String var2, float var3) {
      if (this.method702(var1)) {
         NbtFileStore var4 = (NbtFileStore)this.dI.get(var1.getUniqueId());
         if (var4 != null) {
            var4.method2389(var2, var3);
         }
      }
   }

   @Nullable
   @Override
   public Float method705(Player var1, String var2, @Nullable Float var3) {
      if (!this.method702(var1)) {
         return var3;
      }

      NbtFileStore var4 = (NbtFileStore)this.dI.get(var1.getUniqueId());
      return var4 == null ? var3 : var4.method2390(var2, var3);
   }

   @Override
   void method706(UUID var1) {
      NbtFileStore var2 = (NbtFileStore)this.dI.get(var1);
      if (var2 != null) {
         try {
            var2.bD();
            synchronized (this.dI) {
               this.dI.remove(var1, var2);
            }
         } catch (IOException var6) {
            Log.error("Error saving player stats: " + var1, var6);
         }
      }
   }

   @Override
   public void bC() {
      int var1 = 0;
      ArrayList var2;
      synchronized (this.dI) {
         var2 = new ArrayList(this.dI.entrySet());
      }

      UncheckedIOException var9 = null;

      for (Entry var5 : var2) {
         try {
            if (((NbtFileStore)var5.getValue()).bD()) {
               var1++;
            }
         } catch (IOException var8) {
            if (var9 == null) {
               var9 = new UncheckedIOException("Could not save player stats: " + var5.getKey(), var8);
            } else {
               var9.addSuppressed(var8);
            }
         }
      }

      if (var9 != null) {
         throw var9;
      }

      if (Settings.get().isLoggingEnabled("logging.player_stats.save")) {
         Log.log(ChatColor.YELLOW + "Saved " + var1 + " player stats files");
      }
   }

   @Override
   public void method707(boolean var1) {
      this.bC();
   }

   @EventHandler(priority = EventPriority.LOWEST)
   void method708(PlayerJoinEvent var1) {
      this.method701(var1.getPlayer());
   }

   @EventHandler
   void method709(PlayerQuitEvent var1) {
      this.method706(var1.getPlayer().getUniqueId());
   }

   @EventHandler
   void method710(WorldSaveEvent var1) {
      if (System.currentTimeMillis() - this.dJ > 10000L) {
         this.dJ = System.currentTimeMillis();
         this.bC();
      }
   }

   @EventHandler
   void method711(PluginDisableEvent var1) {
      if (var1.getPlugin().getName().equals(this.dF.getName())) {
         this.bC();
      }
   }
}
