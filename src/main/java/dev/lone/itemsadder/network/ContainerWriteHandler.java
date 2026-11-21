package dev.lone.itemsadder.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

class ContainerWriteHandler extends ChannelOutboundHandlerAdapter {
   ContainerWriteHandler(ContainerConnection var1) {
      this.aLG = var1;
   }

   public void write(ChannelHandlerContext context, Object packet, ChannelPromise promise) {
      synchronized (this.aLG) {
         long var5 = !this.aLG.cz && this.aLG.aKW && this.aLG.aLC.appliedValue == this.aLG && (!this.aLG.aKV || !this.aLG.aLm)
            ? (this.aLG.aKV ? this.aLG.aLF.aKK.method3559(packet, this.aLG.aLD) : this.aLG.aLF.aKK.method3558(packet, this.aLG.aLD))
            : 0L;
         boolean var7 = !this.aLG.cz && this.aLG.aLn && this.aLG.aLF.aKK.method3559(packet, this.aLG.aLD) == 68719476735L;
         if (var5 == 0L && !var7) {
            context.write(packet, promise);
         } else {
            ArrayList var8 = new ArrayList();
            Iterable var9 = this.aLG.aLF.aKK.method3560(packet);
            if (var9 == null) {
               var8.add(packet);
            } else {
               var9.forEach(var8::add);
            }

            if (var8.size() >= 4096) {
               this.aLG.Ms();
               context.write(packet, promise);
            } else {
               int var10 = ContainerPacketListener.aKJ.incrementAndGet();
               if (var5 != 0L) {
                  this.aLG.aLj.method2588(var10, var5);
               }

               if (var7) {
                  this.aLG.aLk.put(var10, this.aLG.aLl);
               }

               var8.add(this.aLG.aLF.aKL.ping(var10));
               if (this.aLG.aLt == null) {
                  this.aLG.aLt = context.executor().schedule(this.aLG::Ms, 5L, TimeUnit.SECONDS);
               }

               context.write(this.aLG.aLF.aKL.bundle(var8), promise);
            }
         }
      }
   }
}
