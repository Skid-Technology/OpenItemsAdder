package dev.lone.itemsadder.util;

import dev.lone.itemsadder.Main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class SchedulerUtils {
   private static final HashMap tasksByClass = new HashMap();
   private static final ExecutorService executor = Executors.newCachedThreadPool();

   private static String callerClassName() {
      return Thread.currentThread().getStackTrace()[3].getClassName();
   }

   public static void cancelTasksFor(Class var0) {
      List var1 = (List)tasksByClass.get(var0.getName());
      if (var1 != null) {
         for (ListIterator var2 = var1.listIterator(); var2.hasNext(); var2.remove()) {
            BukkitTask var3 = (BukkitTask)var2.next();
            if (var3 != null) {
               var3.cancel();
            }
         }
      }

      tasksByClass.remove(var0.getName());
   }

   public static void runRepeating(Consumer var0, long var1, long var3) {
      BukkitTask var5 = new ConsumerTaskA(var0).runTaskTimer(Main.getInstance(), var1, var3);
      tasksByClass.computeIfAbsent(callerClassName(), var0x -> new ArrayList<>()).add(var5);
   }

   public static void runLater(Runnable var0, long var1) {
      BukkitTask var3 = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), var0, var1, var1);
      tasksByClass.computeIfAbsent(callerClassName(), var0x -> new ArrayList<>()).add(var3);
   }

   @NotNull
   public static BukkitScheduler scheduler() {
      return Bukkit.getServer().getScheduler();
   }

   @NotNull
   public static BukkitTask runSync(Runnable var0) {
      return Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), var0);
   }

   public static void runLaterAsync(Runnable var0, long var1) {
      Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(), var0, var1);
   }

   @NotNull
   public static BukkitTask runAsync(Consumer var0) {
      BukkitTask var1 = new ConsumerTaskB(var0).runTaskAsynchronously(Main.getInstance());
      tasksByClass.computeIfAbsent(callerClassName(), var0x -> new ArrayList<>()).add(var1);
      return var1;
   }

   public static BukkitTask scheduleRepeating(Consumer var0, long var1, long var3) {
      BukkitTask var5 = new ConsumerTaskC(var0).runTaskTimerAsynchronously(Main.getInstance(), var1, var3);
      tasksByClass.computeIfAbsent(callerClassName(), var0x -> new ArrayList<>()).add(var5);
      return var5;
   }

   public static void runOnMainThread(Runnable var0) {
      if (!Bukkit.isPrimaryThread()) {
         Bukkit.getScheduler().runTask(Main.getPlugin(), var0);
      } else {
         var0.run();
      }
   }

   public static void runLaterSync(Runnable var0, long var1) {
      Bukkit.getScheduler().runTaskLater(Main.getPlugin(), var0, var1);
   }

   public static void runLaterIf(Runnable var0, long var1, boolean var3) {
      if (var3) {
         Bukkit.getScheduler().runTaskLater(Main.getPlugin(), var0, var1);
      } else {
         var0.run();
      }
   }

   public static BukkitTask scheduleRepeatingSync(Consumer var0, long var1, long var3) {
      return new ConsumerTaskD(var0).runTaskTimer(Main.getInstance(), var1, var3);
   }

   public static void submitIf(Runnable var0, boolean var1) {
      if (var1) {
         executor.submit(var0);
      } else {
         var0.run();
      }
   }

   public static void submitAsync(Runnable var0) {
      executor.submit(var0);
   }

   public static void submitAfterSleep(Runnable var0, int var1) {
      executor.submit(() -> {
         try {
            Thread.sleep(var1);
         } catch (InterruptedException var3) {
         }

         var0.run();
      });
   }

   public static void runNowOrLater(Runnable var0, boolean var1) {
      runNowOrLaterDelayed(var0, var1, 0L);
   }

   public static void runNowOrLaterDelayed(Runnable var0, boolean var1, long var2) {
      if (var1) {
         runLaterSync(var0, var2);
      } else {
         var0.run();
      }
   }

   public static void pollUntil(Callable var0, Runnable var1, int var2) {
      Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), var2x -> {
         try {
            if ((Boolean)var0.call()) {
               var1.run();
               var2x.cancel();
            }
         } catch (Throwable var4) {
            var4.printStackTrace();
         }
      }, 0L, var2);
   }

   public static void teleportOnMainThread(Entity var0, Location var1) {
      runOnMainThread(() -> var0.teleport(var1));
   }
}
