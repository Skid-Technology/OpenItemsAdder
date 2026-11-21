package dev.lone.itemsadder.resourcepack;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

final class PackHttpServer implements AutoCloseable {
   private static final int bJq = 65536;
   private static final int bJr = 128;
   private static final long bJs = 30L;
   private final ThreadPoolExecutor bJt = new ThreadPoolExecutor(0, 32, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), var0 -> {
      Thread var1 = new Thread(var0, "ItemsAdder-pack-download");
      var1.setDaemon(true);
      return var1;
   });
   private final Set bJu = new HashSet();
   private boolean cz;

   synchronized boolean tryRegister(Channel var1) {
      if (this.cz || !var1.isActive()) {
         return false;
      }

      if (this.bJu.contains(var1)) {
         return true;
      }

      if (this.bJu.size() >= 128) {
         return false;
      }

      this.bJu.add(var1);
      var1.closeFuture().addListener(var2 -> {
         synchronized (this) {
            this.bJu.remove(var1);
         }
      });
      return true;
   }

   void serveFile(ChannelHandlerContext var1, File var2, HttpHeaders var3, String var4, long var5, Runnable var7, Runnable var8) {
      ChunkedFileSender var9 = new ChunkedFileSender(var1, var2, var3, var4, var5, var7, var8);
      if (!this.tryRegister(var1.channel())) {
         var8.run();
         var1.close();
      } else {
         var1.pipeline().addLast(new ChannelHandler[]{var9});

         try {
            this.bJt.execute(var9);
         } catch (RejectedExecutionException var15) {
            var1.pipeline().remove(var9);

            try {
               new PackHttpResponse(var1).send(503, new byte[0], "text/plain");
            } finally {
               var8.run();
            }
         }
      }
   }

   @Override
   public void close() {
      Channel[] var1;
      synchronized (this) {
         this.cz = true;
         var1 = this.bJu.toArray(Channel[]::new);
      }

      for (Channel var5 : var1) {
         var5.close();
      }

      this.bJt.shutdownNow();
   }
}
