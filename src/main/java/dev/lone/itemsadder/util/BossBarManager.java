package dev.lone.itemsadder.util;

import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public final class BossBarManager {
   private static final Object stateLock = new Object();
   private static final Object adminLock = new Object();
   private static Object consoleReader;
   private static Method setPromptMethod;
   private static Method redisplayMethod;
   private static BossBar bossBar;
   private static volatile boolean enabled = true;
   private static String currentTitle = " ";
   private static double currentProgress = 1.0;
   private static long updateGeneration;

   private BossBarManager() {
   }

   public static boolean isActive() {
      synchronized (stateLock) {
         return enabled && consoleReader != null;
      }
   }

   public static void enable() {
      long var0;
      synchronized (stateLock) {
         enabled = true;
         var0 = ++updateGeneration;
      }

      sendAdminMessage("> ", var0);
   }

   public static void disable() {
      synchronized (stateLock) {
         enabled = false;
         updateGeneration++;
      }
   }

   public static void setTitle(String var0) {
      setTitleAndProgress(var0, 1.0);
   }

   public static void setTitleAndProgress(String var0, double var1) {
      long var3;
      synchronized (stateLock) {
         currentTitle = var0;
         currentProgress = var1;
         var3 = ++updateGeneration;
      }

      if (enabled) {
         sendAdminMessage(var0 + "\n> ", var3);
      }

      scheduleUpdate(var0, var1, var3);
   }

   public static void setTitleKeepProgress(String var0) {
      setTitleAndProgress(var0, currentProgress);
   }

   public static void setProgress(double var0) {
      setTitleAndProgress(currentTitle, var0);
   }

   public static String getTitle() {
      synchronized (stateLock) {
         return currentTitle;
      }
   }

   public static void clearIfTitle(String var0) {
      synchronized (stateLock) {
         if (currentTitle.equals(var0)) {
            clear();
         }
      }
   }

   private static void scheduleUpdate(String var0, double var1, long var3) {
      synchronized (stateLock) {
         if (enabled && var3 == updateGeneration) {
            SchedulerUtils.runOnMainThread(() -> {
               synchronized (stateLock) {
                  if (!enabled || var3 != updateGeneration) {
                     return;
                  }
               }

               if (bossBar == null) {
                  bossBar = Bukkit.createBossBar(" ", BarColor.WHITE, BarStyle.SEGMENTED_20, new BarFlag[0]);
               }

               bossBar.setVisible(true);
               bossBar.setTitle(var0);
               bossBar.setProgress(Math.max(0.0, Math.min(1.0, var1)));

               for (Player var6 : Bukkit.getOnlinePlayers()) {
                  if (var6.isOp() || var6.hasPermission("ia.admin.iazip")) {
                     bossBar.addPlayer(var6);
                  }
               }
            });
         }
      }
   }

   public static void clear() {
      long var0;
      boolean var2;
      synchronized (stateLock) {
         currentTitle = " ";
         currentProgress = 1.0;
         var0 = ++updateGeneration;
         var2 = enabled;
      }

      if (var2) {
         sendAdminMessage("> ", var0);
      }

      if (Bukkit.isPrimaryThread()) {
         hideBar();
      } else if (var2) {
         SchedulerUtils.runOnMainThread(BossBarManager::hideBar);
      }
   }

   private static void hideBar() {
      if (bossBar != null) {
         bossBar.setVisible(false);
         bossBar.removeAll();
      }
   }

   private static void sendAdminMessage(String var0, long var1) {
      synchronized (stateLock) {
         if (enabled && var1 == updateGeneration) {
            SchedulerUtils.runSync(() -> {
               synchronized (adminLock) {
                  Object var4;
                  Method var5;
                  Method var6;
                  synchronized (stateLock) {
                     if (!enabled || var1 != updateGeneration) {
                        return;
                     }

                     var4 = consoleReader;
                     var5 = setPromptMethod;
                     var6 = redisplayMethod;
                  }

                  if (var4 == null) {
                     if (!ensureConsoleReader()) {
                        return;
                     }

                     synchronized (stateLock) {
                        if (!enabled || var1 != updateGeneration) {
                           return;
                        }

                        var4 = consoleReader;
                        var5 = setPromptMethod;
                        var6 = redisplayMethod;
                     }
                  }

                  method7214(var4, var5, var6, var0);
               }
            });
         }
      }
   }

   private static boolean ensureConsoleReader() {
      synchronized (stateLock) {
         if (consoleReader != null) {
            return true;
         }
      }

      try {
         Class var9 = Class.forName("net.minecrell.terminalconsole.TerminalConsoleAppender");
         Object var1 = var9.getMethod("getReader").invoke(null);
         if (var1 == null) {
            return false;
         }

         Method var2 = var1.getClass().getMethod("setPrompt", String.class);
         Method var3 = var1.getClass().getMethod("redisplay");
         synchronized (stateLock) {
            consoleReader = var1;
            setPromptMethod = var2;
            redisplayMethod = var3;
         }

         return true;
      } catch (ReflectiveOperationException var7) {
         consoleReader = null;
         return false;
      }
   }

   private static void method7214(Object var0, Method var1, Method var2, String var3) {
      try {
         var1.invoke(var0, var3);
         var2.invoke(var0);
      } catch (ReflectiveOperationException var8) {
         synchronized (stateLock) {
            if (consoleReader == var0) {
               consoleReader = null;
            }
         }
      }
   }
}
