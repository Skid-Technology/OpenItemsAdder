package dev.lone.itemsadder.resourcepack;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;

final class ChunkedFileSender extends ChannelInboundHandlerAdapter implements Runnable {
   private final ChannelHandlerContext bJv;
   private final File bJw;
   private final HttpHeaders bJx;
   private final String bJy;
   private final long bJz;
   private final Runnable bJA;
   private final Runnable bJB;

   private ChunkedFileSender(ChannelHandlerContext var1, File var2, HttpHeaders var3, String var4, long var5, Runnable var7, Runnable var8) {
      this.bJv = var1;
      this.bJw = var2;
      this.bJx = var3.copy();
      this.bJy = var4;
      this.bJz = Math.max(1L, var5);
      this.bJA = var7;
      this.bJB = var8;
      var1.channel().closeFuture().addListener(var1x -> this.wakeWriter());
   }

   private synchronized void wakeWriter() {
      this.notifyAll();
   }

   public void channelWritabilityChanged(ChannelHandlerContext ctx) {
      this.wakeWriter();
      ctx.fireChannelWritabilityChanged();
   }

   private synchronized void awaitWritable() {
      long var1 = System.nanoTime() + TimeUnit.SECONDS.toNanos(30L);

      while (this.bJv.channel().isActive() && !this.bJv.channel().isWritable()) {
         long var3 = var1 - System.nanoTime();
         if (var3 <= 0L) {
            throw new IOException("HTTP write timed out");
         }

         TimeUnit.NANOSECONDS.timedWait(this, var3);
      }

      if (!this.bJv.channel().isActive()) {
         throw new IOException("HTTP channel closed");
      }
   }

   private void awaitWrite(ChannelFuture var1) {
      if (!var1.await(30L, TimeUnit.SECONDS) || !var1.isSuccess()) {
         throw new IOException("HTTP write failed or timed out", var1.cause());
      }
   }

   @Override
   public void run() {
      boolean var1 = false;

      try {
         this.awaitWritable();

         try (RandomAccessFile var2 = new RandomAccessFile(this.bJw, "r")) {
            long var104 = var2.length();
            DefaultHttpResponse var5 = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            var5.headers().set(this.bJx);
            var5.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            var5.headers().set(HttpHeaderNames.CONTENT_TYPE, this.bJy);
            var5.headers().set(HttpHeaderNames.CONTENT_LENGTH, var104);
            var5.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            this.awaitWrite(this.bJv.writeAndFlush(var5));

            while (var104 > 0L) {
               this.awaitWritable();
               long var6 = System.nanoTime();
               byte[] var8 = new byte[(int)Math.min(65536L, var104)];
               var2.readFully(var8);
               var104 -= var8.length;
               this.awaitWrite(this.bJv.writeAndFlush(new DefaultHttpContent(Unpooled.wrappedBuffer(var8))));
               long var9 = (var8.length * 1000000000L + this.bJz - 1L) / this.bJz - (System.nanoTime() - var6);
               if (var9 > 0L) {
                  TimeUnit.NANOSECONDS.sleep(var9);
               }
            }

            this.awaitWrite(this.bJv.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT));
            var1 = true;
         }
      } catch (FileNotFoundException var100) {
         try {
            DefaultFullHttpResponse var3 = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
            var3.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
            var3.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            this.awaitWrite(this.bJv.writeAndFlush(var3));
         } catch (IOException var96) {
         } catch (InterruptedException var97) {
            Thread.currentThread().interrupt();
         }
      } catch (InterruptedException var101) {
         Thread.currentThread().interrupt();
      } catch (IOException var102) {
      } finally {
         try {
            if (var1) {
               this.bJA.run();
            } else {
               this.bJB.run();
            }
         } finally {
            this.bJv.close();
         }
      }
   }
}
