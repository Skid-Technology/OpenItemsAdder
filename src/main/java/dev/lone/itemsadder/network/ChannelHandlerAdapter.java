package dev.lone.itemsadder.network;

import dev.lone.itemsadder.entity.PacketDisplayBridge;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.function.Supplier;

public class ChannelHandlerAdapter extends ChannelInboundHandlerAdapter {
   ChannelHandlerAdapter(PacketDisplayBridge var1, String var2, String var3, Supplier var4) {
      this.blh = var2;
      this.bli = var3;
      this.blj = var4;
   }

   public void channelRead(ChannelHandlerContext ctx, Object msg) {
      if (msg instanceof Channel var3) {
         PacketDisplayBridge.injectChannelHandler(var3, this.blh, this.bli, this.blj);
      }

      super.channelRead(ctx, msg);
   }

   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
      cause.printStackTrace();
      super.exceptionCaught(ctx, cause);
   }
}
