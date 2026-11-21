package dev.lone.itemsadder.resourcepack;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class PackHttpResponse implements PackResponseSink {
   private final ChannelHandlerContext bKA;
   private final HttpHeaders bKB;
   private final PackHttpServer bKC;

   public PackHttpResponse(ChannelHandlerContext var1) {
      this(var1, new DefaultHttpHeaders(), PackHttpPipelineInstaller.bKq);
   }

   public PackHttpResponse(ChannelHandlerContext var1, HttpHeaders var2, PackHttpServer var3) {
      this.bKA = var1;
      this.bKB = var2;
      this.bKC = var3;
   }

   @Override
   public void send(int var1, byte[] var2, String var3) {
      byte[] var4 = var2 != null ? var2 : new byte[0];
      DefaultFullHttpResponse var5 = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(var1), Unpooled.wrappedBuffer(var4));
      var5.headers().set(this.bKB);
      var5.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
      var5.headers().set(HttpHeaderNames.CONTENT_TYPE, var3);
      var5.headers().set(HttpHeaderNames.CONTENT_LENGTH, var4.length);
      var5.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
      this.bKA.writeAndFlush(var5).addListener(ChannelFutureListener.CLOSE);
   }

   private void sendNoContent() {
      DefaultFullHttpResponse var1 = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NO_CONTENT);
      var1.headers().set(this.bKB);
      var1.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
      var1.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, DELETE, OPTIONS");
      var1.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization");
      var1.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
      this.bKA.writeAndFlush(var1).addListener(ChannelFutureListener.CLOSE);
   }

   public ChannelHandlerContext ctx() {
      return this.bKA;
   }

   public HttpHeaders headers() {
      return this.bKB;
   }

   public PackHttpServer transport() {
      return this.bKC;
   }
}
