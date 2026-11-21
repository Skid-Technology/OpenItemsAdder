package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.core.ContentManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.timeout.ReadTimeoutException;
import java.io.File;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PackHttpRequestHandler extends SimpleChannelInboundHandler {
   private final PackDownloadThrottle bKu;
   private final PackHttpServer bKv;
   private boolean bKw;
   private ScheduledFuture bKx;

   public PackHttpRequestHandler(PackDownloadThrottle var1) {
      this(var1, PackHttpPipelineInstaller.bKq);
   }

   PackHttpRequestHandler(PackDownloadThrottle var1, PackHttpServer var2) {
      this.bKu = var1;
      this.bKv = var2;
   }

   public void handlerAdded(ChannelHandlerContext ctx) {
      this.bKx = ctx.executor().schedule(() -> ctx.close(), 15L, TimeUnit.SECONDS);
      ctx.channel().closeFuture().addListener(var1x -> this.bKx.cancel(false));
   }

   protected void handleRequest(ChannelHandlerContext var1, FullHttpRequest var2) {
      if (!this.bKw) {
         this.bKw = true;
         this.bKx.cancel(false);
         var1.channel().config().setAutoRead(false);
         PackHttpResponse var3 = new PackHttpResponse(var1, new DefaultHttpHeaders(), this.bKv);
         if (!var2.decoderResult().isSuccess()) {
            var3.sendStatus(400, "Bad Request", "text/plain");
         } else {
            PackHttpRequest var4;
            try {
               var4 = new PackHttpRequest(var2);
            } catch (IllegalArgumentException var14) {
               var3.sendStatus(400, "Bad Request", "text/plain");
               return;
            }

            String var5 = var4.method();
            if (var5.equalsIgnoreCase("OPTIONS")) {
               var3.sendNoContent();
            } else {
               PackDownloadResult var6 = this.bKu.checkRequest(PackHttpPipelineInstaller.remoteIp(var1), var2.headers()::get);
               if (var6 != null) {
                  if (var6.retryAfterSeconds() > 0L) {
                     var3.headers().set(HttpHeaderNames.RETRY_AFTER, var6.retryAfterSeconds());
                  }

                  var3.sendStatus(var6.status(), var6.message(), "text/plain");
               } else {
                  String var7 = var4.path();
                  if (var7 != null && var5.equals("GET")) {
                     File var8 = var7.startsWith("/resourcepack/client_packs/") ? ContentManager.method8084().aL.manager.getClientPackFile(var7) : null;
                     if (!var7.equals("/resourcepack") && var8 == null) {
                        var3.sendStatus(404, "Not Found", "text/plain");
                     } else {
                        String var9 = PackHttpPipelineInstaller.remoteIp(var1);
                        Runnable var10 = this.bKu.acquireSlot(var9);
                        if (var10 == null) {
                           var3.sendStatus(429, "Too many active downloads", "text/plain");
                        } else {
                           File var11 = var8 != null ? var8 : new File(ContentManager.method8087(), "generated.zip");

                           try {
                              this.bKv.serveFile(var1, var11, var3.headers(), "application/zip", this.bKu.maxBytesPerSecond(), var10, var10);
                           } catch (RuntimeException var13) {
                              var10.run();
                              throw var13;
                           }
                        }
                     }
                  } else {
                     var3.sendStatus(400, "Bad Request", "text/plain");
                  }
               }
            }
         }
      }
   }

   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
      if (cause instanceof ReadTimeoutException) {
         ctx.close();
      } else {
         cause.printStackTrace();
         ctx.close();
      }
   }
}
