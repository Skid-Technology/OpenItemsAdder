package dev.lone.itemsadder.Core.OtherPlugins.MythicMobs;

import dev.lone.itemsadder.Core.OtherPlugins.MythicMobs.drops.CustomItemDrop;
import dev.lone.itemsadder.Core.OtherPlugins.MythicMobs.mechanics.BoneMechanic;
import dev.lone.itemsadder.Core.OtherPlugins.MythicMobs.mechanics.CustomEntityMechanic;
import dev.lone.itemsadder.Core.OtherPlugins.MythicMobs.mechanics.DismountMechanic;
import dev.lone.itemsadder.Core.OtherPlugins.MythicMobs.mechanics.MountMechanic;
import dev.lone.itemsadder.Core.OtherPlugins.MythicMobs.targeters.BoneTargeter;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ISkillMechanic;
import io.lumine.mythic.bukkit.events.MythicDropLoadEvent;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.mythic.bukkit.events.MythicTargeterLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class MythicMobsHook implements Listener {
   private static MythicMobsHook instance;

   public static MythicMobsHook inst() {
      if (instance == null) {
         instance = new MythicMobsHook();
      }

      return instance;
   }

   public void register(Plugin plugin) {
      EventRegistrar.registerIfAbsent(inst(), plugin);
   }

   public static void reload() {
      if (instance != null) {
         try {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mythicmobs reload");
         } catch (Exception var1) {
            Log.warn("Failed to reload MythicMobs. Probably something is misconfigured in MythicMobs configs.");
            Log.warn("Try to run '/mythicmobs reload' manually to get more info.");
         }
      }
   }

   public void unregister() {
      if (instance != null) {
         EventRegistrar.unregisterListener(instance);
      }
   }

   @EventHandler
   public void onMythicMechanicLoad(MythicMechanicLoadEvent e) {
      Object var2 = null;
      String var3 = e.getMechanicName().toLowerCase();
      switch (var3) {
         case "customentity":
            var2 = new CustomEntityMechanic(e.getContainer(), e.getConfig().getLine(), e.getConfig());
            break;
         case "bone":
            var2 = new BoneMechanic(e.getContainer(), e.getConfig().getLine(), e.getConfig());
            break;
         case "mountentity":
            var2 = new MountMechanic(e.getContainer(), e.getConfig().getLine(), e.getConfig());
            break;
         case "dismountentity":
            var2 = new DismountMechanic(e.getContainer(), e.getConfig().getLine(), e.getConfig());
      }

      if (var2 != null) {
         e.register((ISkillMechanic)var2);
      }
   }

   @EventHandler
   public void onMythicDropLoad(MythicDropLoadEvent e) {
      ContentManager var2 = ContentManager.method8084();
      if (var2 != null && var2.method8110()) {
         CustomItemDefinition var3 = var2.method8154(e.getConfig().getKey());
         if (var3 != null) {
            e.register(new CustomItemDrop(e.getConfig(), e.getContainer().getLine(), var3));
         }
      }
   }

   @EventHandler
   public void onMythicTargeterLoad(MythicTargeterLoadEvent e) {
      BoneTargeter var2 = null;
      String var3 = e.getTargeterName();
      switch (var3.toLowerCase()) {
         case "bone":
            var2 = new BoneTargeter(e.getConfig());
         default:
            if (var2 != null) {
               e.register(var2);
            }
      }
   }

   public static boolean hasProp(MythicLineConfig conf, String key) {
      return conf.getString(key) != null;
   }

   @Nullable
   public static Integer getInteger(MythicLineConfig conf, String key) {
      return hasProp(conf, key) ? conf.getInteger(key) : null;
   }

   @Nullable
   public static Boolean getBoolean(MythicLineConfig conf, String key) {
      return hasProp(conf, key) ? conf.getBoolean(key) : null;
   }

   @Nullable
   public static Integer getInteger(MythicLineConfig mlc, String[] strings) {
      for (String var5 : strings) {
         Integer var6 = getInteger(mlc, var5);
         if (var6 != null) {
            return var6;
         }
      }

      return null;
   }

   @Nullable
   public static Boolean getBoolean(MythicLineConfig mlc, String[] strings) {
      for (String var5 : strings) {
         Boolean var6 = getBoolean(mlc, var5);
         if (var6 != null) {
            return var6;
         }
      }

      return null;
   }
}
