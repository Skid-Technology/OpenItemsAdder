package dev.lone.itemsadder.network;

import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.EnumProtocol;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.EnumProtocolDirection;

public class PacketListenerStubB implements PacketListener {
   public EnumProtocolDirection N_() {
      return EnumProtocolDirection.a;
   }

   public EnumProtocol method5324() {
      return EnumProtocol.b;
   }

   public void method5325(DisconnectionDetails details) {
   }

   public boolean method5326() {
      return true;
   }
}
