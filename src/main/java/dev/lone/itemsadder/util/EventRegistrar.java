package dev.lone.itemsadder.util;

import dev.lone.itemsadder.Main;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.jetbrains.annotations.NotNull;

public class EventRegistrar {
   static final String BUKKIT_PACKAGE = "org.bukkit";
   static PluginManager pluginManager = Bukkit.getPluginManager();
   static String classLoaderName = EventRegistrar.class.getClassLoader().getName();

   public static void registerIfAbsent(Listener var0, Plugin var1) {
      for (RegisteredListener var3 : HandlerList.getRegisteredListeners(var1)) {
         if (var0.getClass().isInstance(var3.getListener())) {
            return;
         }
      }

      pluginManager.registerEvents(var0, var1);
   }

   public static void registerListener(Listener var0, Plugin var1) {
      pluginManager.registerEvents(var0, var1);
   }

   public static void registerWithMainPlugin(Listener var0) {
      pluginManager.registerEvents(var0, Main.getInstance());
   }

   public static void unregisterListener(Listener var0) {
      if (var0 != null) {
         HandlerList.unregisterAll(var0);
      }
   }

   public static boolean callEvent(Event var0) {
      pluginManager.callEvent(var0);
      return var0 instanceof Cancellable ? !((Cancellable)var0).isCancelled() : true;
   }

   public static boolean isCustomBlockBreakEvent(BlockBreakEvent var0) {
      return var0.getClass() != BlockBreakEvent.class;
   }

   public static boolean isCustomBlockPlaceEvent(BlockPlaceEvent var0) {
      Class var1 = var0.getClass();
      return var1 != BlockPlaceEvent.class && var1 != BlockMultiPlaceEvent.class;
   }

   public static boolean isCustomEntityDamageEvent(EntityDamageByEntityEvent var0) {
      return var0.getClass() != EntityDamageByEntityEvent.class;
   }

   public static boolean isCustomFoodLevelEvent(FoodLevelChangeEvent var0) {
      return var0.getClass() != FoodLevelChangeEvent.class;
   }

   @Deprecated
   static boolean isNonBukkitEvent(Event var0) {
      return !var0.getClass().getPackage().getName().startsWith("org.bukkit");
   }

   public static boolean isReentrantCall() {
      StackTraceElement[] var0 = Thread.currentThread().getStackTrace();
      if (var0.length < 2) {
         return false;
      }

      String var1 = var0[2].getMethodName();
      String var2 = var0[2].getClassName();
      int var3 = 3;

      for (int var4 = var0.length; var3 < var4; var3++) {
         StackTraceElement var5 = var0[var3];
         if (Objects.equals(classLoaderName, var5.getClassLoaderName()) && var1.equals(var5.getMethodName()) && var2.equals(var5.getClassName())) {
            return true;
         }
      }

      return false;
   }

   public static boolean isLeftClick(PlayerInteractEvent var0) {
      return var0.getAction() == Action.LEFT_CLICK_AIR || var0.getAction() == Action.LEFT_CLICK_BLOCK;
   }

   public static void registerHandler(@NotNull Class var0, EventPriority var1, boolean var2, @NotNull EventHandlerCallback var3) {
      Bukkit.getPluginManager().registerEvent(var0, new EventListenerStub(), var1, new EventExecutorAdapter(var0, var3), Main.getInstance(), var2);
   }

   public static void registerHandler(@NotNull Class var0, @NotNull EventHandlerCallback var1) {
      registerHandler(var0, EventPriority.NORMAL, false, var1);
   }
}
