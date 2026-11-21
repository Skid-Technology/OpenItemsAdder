package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.NativePacketFactory;
import dev.lone.itemsadder.entity.PassengerRegistry;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;

final class PassengersChannelHandler extends ChannelDuplexHandler {
   private static final String blR = "itemsadder_passengers";
   private static final int[] blS = new int[0];
   private final Channel blT;
   private final int blU;
   private final PassengerRegistry blV;
   private final NativePacketFactory blW;
   private final LinkedHashMap blX = new LinkedHashMap();
   private final HashSet blY = new HashSet();
   private int[] blZ = blS;
   private volatile ChannelHandlerContext bma;

   PassengersChannelHandler(Channel var1, int var2, PassengerRegistry var3, NativePacketFactory var4) {
      this.blT = var1;
      this.blU = var2;
      this.blV = var3;
      this.blW = var4;
   }

   void PG() {
      this.method5255(() -> {
         if (this.blT.isOpen()) {
            String var1 = this.blT.pipeline().get("encoder") != null ? "encoder" : "outbound_config";
            if (this.blT.pipeline().get(var1) == null) {
               throw new IllegalStateException("Missing outbound encoder for passenger overlay");
            }

            this.blT.pipeline().addAfter(var1, "itemsadder_passengers", this);
         }
      });
   }

   boolean method5254(Object var1, int var2) {
      if (this.bma != null && this.blT.isOpen()) {
         this.method5255(() -> {
            if (this.bma != null) {
               Integer var3 = this.blX.put(var1, var2);
               this.blY.add(var2);
               this.blZ = this.method5256(this.blZ);
               if (var3 == null || var3 != var2) {
                  this.sP();
               }
            }
         });
         return true;
      } else {
         return false;
      }
   }

   void ak(Object var1) {
      this.method5255(() -> {
         if (this.blX.remove(var1) != null) {
            this.sP();
         }
      });
   }

   void close() {
      this.method5255(() -> {
         boolean var1 = !this.blX.isEmpty();
         this.blX.clear();
         if (var1) {
            this.sP();
         }

         if (this.blT.pipeline().context(this) != null) {
            this.blT.pipeline().remove(this);
         }
      });
   }

   private void method5255(Runnable var1) {
      if (this.blT.eventLoop().inEventLoop()) {
         var1.run();
      } else {
         this.blT.eventLoop().execute(var1);
      }
   }

   public void handlerAdded(ChannelHandlerContext context) {
      this.bma = context;
   }

   public void handlerRemoved(ChannelHandlerContext context) {
      this.bma = null;
      this.blZ = blS;
      this.blX.clear();
      this.blY.clear();
   }

   public void channelInactive(ChannelHandlerContext context) {
      this.handlerRemoved(context);
      super.channelInactive(context);
   }

   public void write(ChannelHandlerContext context, Object message, ChannelPromise promise) {
      context.write(this.al(message), promise);
   }

   private Object al(Object var1) {
      if (this.blV.hasPassenger(var1)) {
         this.blZ = blS;
         this.blX.clear();
         return var1;
      }

      if (this.blV.getEntityId(var1) == this.blU) {
         int[] var12 = this.blV.getPassengerIds(var1);
         this.blZ = (int[])this.method5256(var12).clone();
         int[] var14 = this.PH();
         return Arrays.equals(var12, var14) ? var1 : this.blW.passengers(this.blU, var14);
      }

      int[] var2 = this.blZ.length == 0 && this.blX.isEmpty() ? null : this.blV.getVehicleIds(var1);
      if (var2 != null) {
         for (int var17 : var2) {
            if (var17 == this.blU) {
               this.blZ = blS;
               this.blX.clear();
               break;
            }

            this.blZ = method5257(this.blZ, var17);
            this.blX.values().removeIf(var1x -> var1x == var17);
         }

         return var1;
      } else {
         Iterable var3 = this.blV.getPassengers(var1);
         if (var3 == null) {
            return var1;
         }

         ArrayList var4 = null;
         int var5 = 0;

         for (Object var7 : var3) {
            Object var8 = this.al(var7);
            if (var8 != var7 && var4 == null) {
               var4 = new ArrayList();
               int var9 = 0;

               for (Object var11 : var3) {
                  if (var9++ == var5) {
                     break;
                  }

                  var4.add(var11);
               }
            }

            if (var4 != null) {
               var4.add(var8);
            }

            var5++;
         }

         return var4 == null ? var1 : this.blW.bundle(var4);
      }
   }

   private int[] method5256(int[] var1) {
      int var2 = 0;

      for (int var6 : var1) {
         if (!this.blY.contains(var6)) {
            var2++;
         }
      }

      if (var2 == var1.length) {
         return var1;
      }

      int[] var9 = new int[var2];
      int var10 = 0;

      for (int var8 : var1) {
         if (!this.blY.contains(var8)) {
            var9[var10++] = var8;
         }
      }

      return var9;
   }

   private static int[] method5257(int[] var0, int var1) {
      int var2 = 0;

      for (int var6 : var0) {
         if (var6 != var1) {
            var2++;
         }
      }

      if (var2 == var0.length) {
         return var0;
      }

      int[] var9 = new int[var2];
      int var10 = 0;

      for (int var8 : var0) {
         if (var8 != var1) {
            var9[var10++] = var8;
         }
      }

      return var9;
   }

   private int[] PH() {
      int[] var1 = Arrays.copyOf(this.blZ, this.blZ.length + this.blX.size());
      int var2 = this.blZ.length;

      for (int var4 : this.blX.values()) {
         var1[var2++] = var4;
      }

      return var1;
   }

   private void sP() {
      ChannelHandlerContext var1 = this.bma;
      if (var1 != null && this.blT.isOpen()) {
         var1.writeAndFlush(this.blW.passengers(this.blU, this.PH()));
      }
   }
}
