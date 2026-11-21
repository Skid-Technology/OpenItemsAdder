package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.ConnectionHandle;
import beer.devs.nativepackets.api.FastPacketBatch;
import beer.devs.nativepackets.api.FastPacketCapability;
import beer.devs.nativepackets.api.NativePacketFactory;

public final class FastRelativeMove extends FastPacketAction {
   private final int aSE;
   private final short aSF;
   private final short aSG;
   private final short aSH;

   public FastRelativeMove(int var1, short var2, short var3, short var4) {
      this.aSE = var1;
      this.aSF = var2;
      this.aSG = var3;
      this.aSH = var4;
   }

   @Override
   public boolean method3162(ConnectionHandle var1) {
      return var1.supportsFast(FastPacketCapability.RELATIVE_MOVE);
   }

   @Override
   public void method3163(FastPacketBatch var1) {
      var1.entityRelativeMove(this.aSE, this.aSF, this.aSG, this.aSH, false);
   }

   @Override
   protected Object method3164(NativePacketFactory var1) {
      return var1.relativeMove(this.aSE, this.aSF / 4096.0, this.aSG / 4096.0, this.aSH / 4096.0, false);
   }
}
