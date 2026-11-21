package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.ConnectionHandle;
import beer.devs.nativepackets.api.FastPacketBatch;
import beer.devs.nativepackets.api.FastPacketCapability;
import beer.devs.nativepackets.api.NativePacketFactory;
import beer.devs.nativepackets.api.NativeVector3;

public final class FastPositionSync extends FastPacketAction {
   private final int aSz;
   private final double aSA;
   private final double aSB;
   private final double aSC;
   private final boolean aSD;

   public FastPositionSync(int var1, double var2, double var4, double var6, boolean var8) {
      this.aSz = var1;
      this.aSA = var2;
      this.aSB = var4;
      this.aSC = var6;
      this.aSD = var8;
   }

   @Override
   public boolean method3162(ConnectionHandle var1) {
      return var1.supportsFast(this.aSD ? FastPacketCapability.POSITION_SYNC : FastPacketCapability.MIXED_BATCH);
   }

   @Override
   public void method3163(FastPacketBatch var1) {
      if (this.aSD) {
         var1.entityPositionSync(this.aSz, this.aSA, this.aSB, this.aSC, 0.0, 0.0, 0.0, 0.0F, 0.0F, false);
      } else {
         var1.entityTeleport(this.aSz, this.aSA, this.aSB, this.aSC, 0.0F, 0.0F, false);
      }
   }

   @Override
   protected Object method3164(NativePacketFactory var1) {
      return this.aSD
         ? var1.entityPositionSync(this.aSz, new NativeVector3(this.aSA, this.aSB, this.aSC), new NativeVector3(0.0, 0.0, 0.0), 0.0F, 0.0F, false)
         : var1.teleport(this.aSz, this.aSA, this.aSB, this.aSC, 0.0F, 0.0F, false);
   }
}
