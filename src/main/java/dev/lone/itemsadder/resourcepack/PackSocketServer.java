package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.io.File;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import org.bukkit.plugin.Plugin;

public class PackSocketServer {
   private static final int MAX_CONCURRENT_DOWNLOADS = 128;
   final Plugin plugin;
   public final int port;
   final File folder;
   volatile boolean running = false;
   private final PackDownloadThrottle throttle;
   private final Semaphore downloadSlots = new Semaphore(128);
   private final Set activeSockets = ConcurrentHashMap.newKeySet();
   private ServerSocket serverSocket;
   private ScheduledThreadPoolExecutor watchdogExecutor;
   private long generation;

   public PackSocketServer(Plugin var1, File var2, int var3) {
      this.plugin = var1;
      this.port = var3;
      this.folder = var2;
      this.throttle = new PackDownloadThrottle(Settings.get().selfHostDef);
   }

   public boolean isRunning() {
      return this.running;
   }

   public void start(Consumer var1) {
      long var2;
      ScheduledThreadPoolExecutor var4;
      synchronized (this) {
         if (this.running) {
            return;
         }

         this.running = true;
         var2 = ++this.generation;
         var4 = PackHttpConnection.newWatchdogExecutor();
         this.watchdogExecutor = var4;
      }

      try {
         Files.createDirectories(this.folder.toPath());
      } catch (Throwable var10) {
         Log.error("\n\n");
         Log.error("Failed to create folder: " + this.folder.getAbsolutePath(), var10);
         Log.error("\n\n");
         synchronized (this) {
            if (this.generation == var2) {
               this.running = false;
               this.watchdogExecutor = null;
            }
         }

         var4.shutdownNow();
         var1.accept(false);
         return;
      }

      try {
         SchedulerUtils.submitAsync(() -> {
            boolean var5 = false;

            try {
               try (ServerSocket var6 = new ServerSocket(this.port)) {
                  synchronized (this) {
                     if (!this.running || this.generation != var2) {
                        return;
                     }

                     this.serverSocket = var6;
                     var5 = true;
                  }

                  while (this.isCurrentGeneration(var2)) {
                     try {
                        Socket var7 = var6.accept();
                        if (!this.tryAcquireSlot(var7, var2)) {
                           closeQuietly(var7);
                        } else {
                           try {
                              new PackHttpConnection(var7, this.folder, this.throttle, () -> this.releaseSlot(var7), var4);
                           } catch (Throwable var40) {
                              this.releaseSlot(var7);
                              closeQuietly(var7);
                              throw var40;
                           }
                        }
                     } catch (SocketException var42) {
                        if (!this.isCurrentGeneration(var2) || var6.isClosed()) {
                           return;
                        }

                        Log.error("SocketException while running self-host", var42);
                     } catch (Throwable var43) {
                        Log.error("Uncaught error while running self-host", var43);
                     }
                  }
               } catch (BindException var46) {
                  Log.error("\n\n");
                  Log.error("Error running self-host: " + var46.getMessage());
                  Log.error("Make sure the port '" + this.port + "' is not used by another process.");
                  Log.error("\n\n");
               } catch (Throwable var47) {
                  Log.error("\n\n");
                  Log.error("Uncaught error running self-host", var47);
                  Log.error("\n\n");
               }
            } finally {
               Socket[] var19 = null;
               synchronized (this) {
                  if (this.generation == var2) {
                     this.running = false;
                     this.serverSocket = null;
                     this.watchdogExecutor = null;
                     var19 = this.activeSockets.toArray(Socket[]::new);
                  }
               }

               if (var19 != null) {
                  for (Socket var23 : var19) {
                     closeQuietly(var23);
                  }
               }

               var4.shutdownNow();
               var1.accept(var5);
            }
         });
      } catch (Throwable var12) {
         synchronized (this) {
            if (this.generation == var2) {
               this.running = false;
               this.watchdogExecutor = null;
            }
         }

         var4.shutdownNow();
         Log.error("Failed to schedule self-host", var12);
         var1.accept(false);
      }
   }

   public void stop() {
      ServerSocket var1;
      Socket[] var2;
      ScheduledThreadPoolExecutor var3;
      synchronized (this) {
         this.running = false;
         this.generation++;
         var1 = this.serverSocket;
         this.serverSocket = null;
         var2 = this.activeSockets.toArray(Socket[]::new);
         var3 = this.watchdogExecutor;
         this.watchdogExecutor = null;
      }

      closeQuietly(var1);

      for (Socket var7 : var2) {
         closeQuietly(var7);
      }

      if (var3 != null) {
         var3.shutdownNow();
      }
   }

   private synchronized boolean isCurrentGeneration(long var1) {
      return this.running && this.generation == var1;
   }

   private synchronized boolean tryAcquireSlot(Socket var1, long var2) {
      if (this.running && this.generation == var2 && this.downloadSlots.tryAcquire()) {
         this.activeSockets.add(var1);
         return true;
      } else {
         return false;
      }
   }

   private void releaseSlot(Socket var1) {
      if (this.activeSockets.remove(var1)) {
         this.downloadSlots.release();
      }
   }

   private static void closeQuietly(AutoCloseable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (Throwable var2) {
            Log.error("Uncaught error stopping self-host", var2);
         }
      }
   }
}
