package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;

class PackHttpConnection extends Thread {
   private static final String[] bJU = new String[]{"..", "./", "/.", "~", ";", "|", "%"};
   private static final int bJV = 5000;
   private static final int bJW = 15;
   private static final int bJX = 8192;
   private static final int bJY = 32768;
   private static final int bJZ = 65536;
   private static final long bKa = TimeUnit.SECONDS.toNanos(30L);
   private final Socket bKb;
   private final File bKc;
   private final PackDownloadThrottle bKd;
   private final Runnable bKe;
   private final ScheduledExecutorService bKf;
   private final boolean bKg;
   private final long bKh;
   private volatile long bKi;

   public PackHttpConnection(Socket var1, File var2, PackDownloadThrottle var3) {
      this(var1, var2, var3, () -> {}, newWatchdogExecutor(), true, bKa);
   }

   PackHttpConnection(Socket var1, File var2, PackDownloadThrottle var3, Runnable var4) {
      this(var1, var2, var3, var4, newWatchdogExecutor(), true, bKa);
   }

   PackHttpConnection(Socket var1, File var2, PackDownloadThrottle var3, Runnable var4, ScheduledExecutorService var5) {
      this(var1, var2, var3, var4, var5, false, bKa);
   }

   PackHttpConnection(Socket var1, File var2, PackDownloadThrottle var3, Runnable var4, long var5) {
      this(var1, var2, var3, var4, newWatchdogExecutor(), true, var5);
   }

   private PackHttpConnection(Socket var1, File var2, PackDownloadThrottle var3, Runnable var4, ScheduledExecutorService var5, boolean var6, long var7) {
      this.bKb = var1;
      this.bKc = var2;
      this.bKd = var3;
      this.bKe = var4;
      this.bKf = var5;
      this.bKg = var6;
      this.bKh = Math.max(1L, var7);
      this.start();
   }

   static ScheduledThreadPoolExecutor newWatchdogExecutor() {
      ScheduledThreadPoolExecutor var0 = new ScheduledThreadPoolExecutor(1, var0x -> {
         Thread var1 = new Thread(var0x, "ItemsAdder-classic-pack-watchdog");
         var1.setDaemon(true);
         return var1;
      });
      var0.setRemoveOnCancelPolicy(true);
      return var0;
   }

   @Override
   public void run() {
      ScheduledFuture var1 = null;

      try {
         long var2 = Math.min(TimeUnit.SECONDS.toNanos(1L), this.bKh);
         var1 = this.bKf.scheduleWithFixedDelay(this::checkIdle, var2, var2, TimeUnit.NANOSECONDS);

         try (
            this.bKb;
            InputStream var5 = this.bKb.getInputStream();
            BufferedOutputStream var6 = new BufferedOutputStream(this.bKb.getOutputStream());
         ) {
            PackRequestReader var7 = new PackRequestReader(var5, this.bKb, System.nanoTime() + TimeUnit.SECONDS.toNanos(15L));
            String var8 = null;

            try {
               label903: {
                  PackHttpRoute var10 = readLine(var7, 8192);
                  if (var10 == null) {
                     throw new PackHttpException("Null request string. Failed to initialize.");
                  }

                  int var11 = var10.bytes();
                  String var12 = var10.value();
                  StringTokenizer var13 = new StringTokenizer(var12);
                  if (var13.hasMoreElements() && var13.nextToken().equalsIgnoreCase("GET") && var13.hasMoreElements()) {
                     var8 = var13.nextToken();
                     TreeMap var14 = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

                     while (true) {
                        var10 = readLine(var7, Math.min(8192, 32768 - var11));
                        if (var10 == null) {
                           throw new PackHttpException("HTTP headers ended before the terminating blank line.");
                        }

                        var11 += var10.bytes();
                        var12 = var10.value();
                        if (var12.isBlank()) {
                           String var92 = this.bKb.getInetAddress().getHostAddress();
                           PackDownloadResult var16 = this.bKd.checkRequest(var92, var14::get);
                           if (var16 != null) {
                              String var94 = var16.retryAfterSeconds() > 0L ? "Retry-After: " + var16.retryAfterSeconds() + "\r\n" : "";
                              this.writeAscii(
                                 var6, "HTTP/1.0 " + var16.status() + " " + var16.message() + "\r\n" + var94 + "Content-Length: 0\r\nConnection: close\r\n\r\n"
                              );
                              return;
                           }

                           for (String var20 : bJU) {
                              if (var8.contains(var20)) {
                                 throw new SecurityException();
                              }
                           }

                           if (var8.endsWith("/")) {
                              var8 = var8 + "index.html";
                           }

                           if (var8.contains("?")) {
                              var8 = var8.split("\\?")[0];
                           }

                           while (var8.indexOf("/") == 0) {
                              var8 = var8.substring(1);
                           }

                           File var93 = var8.startsWith("client_packs/") ? ContentManager.method8084().aL.manager.getClientPackFile(var8) : null;
                           if (var8.startsWith("client_packs/") && var93 == null) {
                              this.writeAscii(var6, "HTTP/1.0 404 Not Found\r\nContent-Length: 0\r\n\r\n");
                              return;
                           }

                           var8 = StringUtils.replace(var8, "/", File.separator);
                           File var9 = var93 != null ? var93 : new File(this.bKc, var8);
                           if (var9.isDirectory()) {
                              var8 = StringUtils.replace(var8, "\\", "/");
                              this.writeAscii(var6, "HTTP/1.0 301 Moved Permanently\r\nLocation: /" + var8 + "/\r\n\r\n");
                              return;
                           }

                           Runnable var95 = this.bKd.acquireSlot(var92);
                           if (var95 == null) {
                              this.writeAscii(var6, "HTTP/1.0 429 Too Many Requests\r\nContent-Length: 0\r\nConnection: close\r\n\r\n");
                              return;
                           }

                           try (FileInputStream var96 = new FileInputStream(var9)) {
                              String var97 = URLConnection.guessContentTypeFromName(var8);
                              long var21 = var96.getChannel().size();
                              if (var97 == null) {
                                 if (var8.endsWith(".html") || var8.endsWith(".htm")) {
                                    var97 = "text/html";
                                 } else if (var8.endsWith(".jpg") || var8.endsWith(".jpeg")) {
                                    var97 = "image/jpeg";
                                 } else if (var8.endsWith(".gif")) {
                                    var97 = "image/gif";
                                 } else if (var8.endsWith(".class")) {
                                    var97 = "application/octet-stream";
                                 } else if (var8.endsWith(".zip")) {
                                    var97 = "application/zip";
                                 } else {
                                    var97 = "text/plain";
                                 }
                              }

                              this.writeAscii(var6, "HTTP/1.0 200 OK\r\nContent-Type: " + var97 + "\r\nContent-Length: " + var21 + "\r\n\r\n");
                              byte[] var23 = new byte[65536];
                              long var24 = var21;

                              while (var24 > 0L) {
                                 int var26 = var96.read(var23, 0, (int)Math.min(var23.length, var24));
                                 if (var26 < 0) {
                                    throw new EOFException("Hosted file changed during transfer.");
                                 }

                                 var24 -= var26;
                                 long var27 = System.nanoTime();
                                 this.writeBytes(var6, var23, var26);
                                 long var29 = (var26 * 1000000000L + this.bKd.maxBytesPerSecond() - 1L) / this.bKd.maxBytesPerSecond()
                                    - (System.nanoTime() - var27);
                                 if (var29 > 0L) {
                                    TimeUnit.NANOSECONDS.sleep(var29);
                                 }
                              }
                              break label903;
                           } finally {
                              var95.run();
                           }
                        }

                        int var15 = var12.indexOf(58);
                        if (var15 > 0) {
                           var14.put(var12.substring(0, var15).trim(), var12.substring(var15 + 1).trim());
                        }
                     }
                  }

                  throw new PackHttpException("Invalid request string. Failed to parse tokens.");
               }
            } catch (FileNotFoundException var77) {
               this.writeAscii(
                  var6,
                  "HTTP/1.0 404 Not Found\r\nContent-Type: text/html\r\n\r\n<html><head></head><body>"
                     + Utils.normalizePluginPath(var8)
                     + " not found\n</body></html>"
               );
               return;
            } catch (SecurityException var78) {
               this.writeAscii(
                  var6,
                  "HTTP/1.0 401 Unauthorized\r\nContent-Type: text/html\r\n\r\n<html><head></head><body>No permission for path: "
                     + Utils.normalizePluginPath(var8)
                     + "\n</body></html>"
               );
               return;
            } catch (PackHttpException var79) {
               this.writeAscii(
                  var6,
                  "HTTP/1.0 405 Method Not Allowed\r\nContent-Type: text/html\r\n\r\n<html><head></head><body>Invalid request: "
                     + Utils.normalizePluginPath(var8)
                     + "\n</body></html>"
               );
               return;
            }
         }
      } catch (RejectedExecutionException var83) {
         this.closeSocket();
         return;
      } catch (SocketException var84) {
         if (!this.bKb.isClosed() && !"Connection reset".equals(var84.getMessage())) {
            Log.error("Unexpected error while handling webserver request: ", var84);
         }

         return;
      } catch (SocketTimeoutException var85) {
         return;
      } catch (IOException var86) {
         Log.error("Unexpected I/O error while handling webserver request: ", var86);
         return;
      } catch (InterruptedException var87) {
         Thread.currentThread().interrupt();
         return;
      } catch (Throwable var88) {
         Log.error("Unexpected error while handling webserver request: ", var88);
         return;
      } finally {
         if (var1 != null) {
            var1.cancel(false);
         }

         if (this.bKg) {
            this.bKf.shutdownNow();
         }

         this.bKe.run();
      }
   }

   private void writeAscii(OutputStream var1, String var2) {
      byte[] var3 = var2.getBytes(StandardCharsets.UTF_8);
      this.writeBytes(var1, var3, var3.length);
   }

   private void writeBytes(OutputStream var1, byte[] var2, int var3) {
      long var4 = System.nanoTime();
      this.bKi = var4 == 0L ? 1L : var4;

      try {
         var1.write(var2, 0, var3);
         var1.flush();
      } finally {
         this.bKi = 0L;
      }
   }

   private void checkIdle() {
      long var1 = this.bKi;
      if (var1 != 0L && System.nanoTime() - var1 >= this.bKh) {
         this.closeSocket();
      }
   }

   private void closeSocket() {
      try {
         this.bKb.close();
      } catch (IOException var2) {
      }
   }

   private static PackHttpRoute readLine(PackRequestReader var0, int var1) {
      if (var1 <= 0) {
         throw new PackHttpException("HTTP headers are too large.");
      }

      StringBuilder var2 = new StringBuilder(Math.min(var1, 128));
      int var3 = 0;

      while (true) {
         int var4 = var0.readByte();
         if (var4 == -1) {
            if (var3 == 0) {
               return null;
            }

            throw new PackHttpException("HTTP header line ended unexpectedly.");
         }

         if (++var3 > var1) {
            throw new PackHttpException("HTTP header line is too large.");
         }

         if (var4 == 10) {
            if (!var2.isEmpty() && var2.charAt(var2.length() - 1) == '\r') {
               var2.setLength(var2.length() - 1);
            }

            return new PackHttpRoute(var2.toString(), var3);
         }

         var2.append((char)var4);
      }
   }
}
