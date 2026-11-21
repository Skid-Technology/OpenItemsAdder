package dev.lone.itemsadder.network;

import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.EnumProtocol;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.EnumProtocolDirection;

class PacketListenerStubA implements PacketListener {
   public EnumProtocolDirection P_() {
      return EnumProtocolDirection.a;
   }

   public EnumProtocol method5321() {
      return EnumProtocol.b;
   }

   public void method5322(DisconnectionDetails details) {
   }

   public boolean method5323() {
      return true;
   }
}
