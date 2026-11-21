package dev.lone.itemsadder.resourcepack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.util.List;

final class PackHttpRequestDecoder extends ByteToMessageDecoder {
   private static final byte[] bKr = new byte[]{71, 69, 84, 32};
   private final PackDownloadThrottle bKs;
   private final PackHttpServer bKt;

   PackHttpRequestDecoder(PackDownloadThrottle var1, PackHttpServer var2) {
      this.bKs = var1;
      this.bKt = var2;
   }

   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) {
      for (int var4 = 0; var4 < Math.min(in.readableBytes(), bKr.length); var4++) {
         if (in.getByte(in.readerIndex() + var4) != bKr[var4]) {
            ctx.pipeline().remove(this);
            return;
         }
      }

      if (in.readableBytes() >= bKr.length) {
         if (!this.bKt.tryRegister(ctx.channel())) {
            in.skipBytes(in.readableBytes());
            ctx.close();
         } else {
            ChannelPipeline var10 = ctx.pipeline();
            if (var10.get("timeout") instanceof ReadTimeoutHandler) {
               var10.remove("timeout");
            }

            boolean var5 = false;

            for (String var9 : var10.names().toArray(new String[0])) {
               if (var5 && var10.get(var9) != null) {
                  var10.remove(var9);
               }

               if (var9.equals(ctx.name())) {
                  var5 = true;
               }
            }

            var10.addLast("itemsadder_http_codec", new HttpServerCodec());
            var10.addLast("itemsadder_http_aggregator", new HttpObjectAggregator(0));
            var10.addLast("itemsadder_http_handler", new PackHttpRequestHandler(this.bKs, this.bKt));
            var10.remove(this);
         }
      }
   }
}
